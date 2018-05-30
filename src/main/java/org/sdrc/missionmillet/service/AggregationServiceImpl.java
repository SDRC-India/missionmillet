package org.sdrc.missionmillet.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.sdrc.missionmillet.domain.AggregatedData;
import org.sdrc.missionmillet.domain.RawData;
import org.sdrc.missionmillet.domain.TimePeriod;
import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.model.ValueObject;
import org.sdrc.missionmillet.repository.AggregationRepository;
import org.sdrc.missionmillet.repository.NGOSoEUploadsStatusRepository;
import org.sdrc.missionmillet.repository.RawDataRepository;
import org.sdrc.missionmillet.repository.TimePeriodRepository;
import org.sdrc.missionmillet.util.Constants;
import org.sdrc.missionmillet.util.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 *  Aggregating data on monthly, quarterly and yearly. Ex. For month
 *  April we are calculating the each ngo data values and total ngo data
 *  values. for a quarter we are calculating the each ngo(single)
 *  quarterly data values and total ngo data values. (same for yearly
 *  calculations as monthly)
 *  
 *  @author Subrata
 */
@Service
public class AggregationServiceImpl implements AggregationService {

	@Autowired
	private AggregationRepository aggregationRepository;

	@Autowired
	private RawDataRepository rawDataRepository;

	@Autowired
	private NGOSoEUploadsStatusRepository ngoSoEUploadsStatusRepository;

	@Autowired
	private TimePeriodRepository timePeriodRepository;
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	
	@Autowired
	private StateManager stateManager;
	
	private static final Logger LOGGER = LoggerFactory.getLogger("LOGGER");
	private SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * @author Subrata
	 */
	@Transactional
	@Override
	public String aggregate(int monthId, int periodicity) {
		
		CollectUserModel collectUserModel = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);

		if (collectUserModel == null) {
			LOGGER.error("Error description : "+messageSourceNotification.getMessage("invalid.user", null, null)+" : " +fullDateFormat.format(new Date()));
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.user", null, null));
		}
		
		List<ValueObject> pendingListData = getPendingList(monthId, periodicity);
		if(pendingListData==null)
			return aggregation(monthId, periodicity);
		else
			return "fail";
		
	}
	
	/**
	 * @author Subrata
	 * 
	 * @param monthId
	 * @param periodicity
	 * @return success/fail
	 */

	private String aggregation(int monthId, int periodicity) {
		List<AggregatedData> listAggregatedDatas = new ArrayList<AggregatedData>();

		List<RawData> listOfRawDataMonthly = null;
		List<Object[]> listOfRawDataQuarterlyOrYearly = null;

		/**
		 * For Monthly periodicity =1, for Quarterly periodicity = 3, for Yearly
		 * periodicity = 12
		 */
		Integer period = Integer.valueOf(messageSourceNotification.getMessage("status.periodicity", null, null));
		if (periodicity == period) {
			listOfRawDataMonthly = rawDataRepository.getMonthlyData(monthId);
			if (listOfRawDataMonthly.isEmpty()) {
				return "fail";
			} else {
				double allNgoRawData = rawDataRepository.getAllNgoMonthlyData(monthId);

				for (RawData rawData : listOfRawDataMonthly) {
					
					listAggregatedDatas.add(getAggregatedData(rawData.getNgo().getId(), new TimePeriod(monthId), rawData.getFinance()));
				}
				/*
				 * Setting total NGOs quarterly, yearly data.
				 */
				listAggregatedDatas.add(getAggregatedData(null, new TimePeriod(monthId), allNgoRawData));
			}
		} else {
			/*
			 * For quarterly and yearly calculation we are passing
			 * periodicity=1, for each month calculation in a quarter or year.
			 */
			String periodForQuarter = messageSourceNotification.getMessage("status.periodicity", null, null);
			listOfRawDataQuarterlyOrYearly = rawDataRepository.getRawData(monthId, periodForQuarter);
			if (listOfRawDataQuarterlyOrYearly.isEmpty()) {
				return "fail";
			} else {
				double allNgoRawData = rawDataRepository.getAllNgoData(monthId,	periodForQuarter);

				for (Object[] rawData : listOfRawDataQuarterlyOrYearly) {
					
					listAggregatedDatas.add(getAggregatedData((Integer) rawData[0], new TimePeriod(monthId), (Double) rawData[1]));
				}
				/*
				 * Setting all NGOs quarterly, yearly data.
				 */
				listAggregatedDatas.add(getAggregatedData(null, new TimePeriod(monthId), allNgoRawData));
			}
		}

		/*
		 * Saving all aggregated data.
		 */
		aggregationRepository.save(listAggregatedDatas);
		return "success";
	}

	private AggregatedData getAggregatedData(Integer ngoId, TimePeriod timePeriod,	double finance) {
		
		AggregatedData aggregatedData = new AggregatedData();
		aggregatedData.setNgo(ngoId);
		aggregatedData.setCreatedBy("Admin");
		aggregatedData.setTimePeriod(timePeriod);
		aggregatedData.setDataValue(finance);
		
		return aggregatedData;
	}
	
	@Override
	public List<ValueObject> getPendingList(int monthId, int periodicity) {
		/*
		 * We are aggregating only the approval data for a selected time period.
		 * If there is some task pending for approval, then we are not
		 * aggregating.
		 */
		Integer statusPending = Integer.valueOf(messageSourceNotification.getMessage("status.pending", null, null)); // 1 for pending
		Integer statusRejected = Integer.valueOf(messageSourceNotification.getMessage("typedetails.rejected", null, null)); // 4 for pending
		Integer maxReject = Integer.valueOf(messageSourceNotification.getMessage("rejectcount.limit", null, null)); // 2 for pending
		
		TimePeriod timePeriod = timePeriodRepository.findByTimePeriodId(monthId);
	
		List<Integer> statusId = new ArrayList<Integer>(Arrays.asList(statusPending, statusRejected));
		/*
		 * Checking is there any pending records in NGOSoEUploadsStatus table.
		 * passing parameter statusId, start date and end date from timeperiod. 
		 */
		List<ValueObject> listOfNGOPendingStatus = new ArrayList<ValueObject>();
		List<Object[]> pendingList = ngoSoEUploadsStatusRepository
				.getPendingStatus(statusId, timePeriod.getStartDate(), timePeriod.getEndDate(), maxReject);
		if (!pendingList.isEmpty()) {

			for (Object[] objects : pendingList) {
				ValueObject valueObject = new ValueObject();
				valueObject.setKey(String.valueOf((Integer)objects[0]));
				valueObject.setShortNmae((String)objects[1]);
				valueObject.setGroupName(String.valueOf((Integer)objects[2]));
				valueObject.setDescription((String)objects[3]);
				
				listOfNGOPendingStatus.add(valueObject);
			}
			return listOfNGOPendingStatus;
		} else {
			return null;
		}
	}

}
