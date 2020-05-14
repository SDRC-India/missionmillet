package org.sdrc.missionmillet.service;

import java.util.List;
import java.util.Map;

import org.sdrc.missionmillet.model.DistrictUserNgoListModel;
import org.sdrc.missionmillet.model.NgoReportsModel;

public interface StateUserNgoWorkSpaceService {

	Map<String, List<DistrictUserNgoListModel>> retrieveStatetNgoListSoEStatus();

	Map<String, List<DistrictUserNgoListModel>> retrieveStatetNgoSoEHistory();

	
	/**
	 * @author Subham Ashish (subham@sdrc.co.in)
	 * @return displays history
	 */
	Map<String, List<NgoReportsModel>> stateUserNgoDistList();

	/**
	 * @author Subham Ashish (subham@sdrc.co.in)
	 * @param reportId
	 * @param typeId
	 * @param reportTypeId
	 * @param ngoId
	 * @return
	 */
	String reportAndCertificateDownload(int reportId, int typeId,
			int reportTypeId,int ngoId);

	/**
	 * @author Subham Ashish (subham@sdrc.co.in)
	 * @param reportId
	 * @return
	 */
	boolean deleteReportsAndCertificate(Integer reportId);
}
