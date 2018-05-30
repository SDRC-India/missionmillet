package org.sdrc.missionmillet.controller;

import java.util.List;
import java.util.Map;

import org.sdrc.missionmillet.core.Authorize;
import org.sdrc.missionmillet.model.DistrictUserNgoListModel;
import org.sdrc.missionmillet.model.NgoReportsModel;
import org.sdrc.missionmillet.service.StateUserNgoWorkSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Maninee Mahapatra(maninee@sdrc.co.in) This controller for State admin
 *         date(20-12-2017)
 * 
 *
 */
@Controller
public class StateUserNgoWorkSpaceController {

	@Autowired
	StateUserNgoWorkSpaceService stateUserNgoWorkSpaceService;

	/**
	 * @Description Request for populate the ngo list which are upload their SoE for
	 *              approve or reject of all district
	 * @return Map<String, List<DistrictUserNgoListModel>>
	 */
	@Authorize(feature = "state_soe_reports", permission = "edit")
	@RequestMapping(value = "/getStateNgoListSoEStatus", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<DistrictUserNgoListModel>> retrieveStateNgoListSoEStatus() {
		return stateUserNgoWorkSpaceService.retrieveStatetNgoListSoEStatus();
	}

	/**
	 * @Description Request for populate the history page of ngo workspace in state
	 *              admin of all district
	 * @return Map<String, List<DistrictUserNgoListModel>>
	 */
	@Authorize(feature = "state_soe_reports", permission = "edit")
	@RequestMapping(value = "/getStateNgoSoEHistory", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<DistrictUserNgoListModel>> retrieveStateNgoSoEHistory() {
		return stateUserNgoWorkSpaceService.retrieveStatetNgoSoEHistory();
	}

	
	//@author subham (subham@sdrc.co.in)
	
	/**
	 * display all Reports details in admin section of NGOWorkspace
	 */

	@Authorize(feature = "state_soe_reports", permission = "view")
	@RequestMapping(value = "stateUserNgoDistList")
	@ResponseBody
	Map<String, List<NgoReportsModel>> getNgoReportsDetails() {

		return stateUserNgoWorkSpaceService.stateUserNgoDistList();
	}

	/**
	 *  to download report file in admin section of NGOWorkspace
	 * 
	 */
	@Authorize(feature = "state_report", permission = "view")
	@RequestMapping(value = "stateNgoDistReportAndCertificateDownload")
	@ResponseBody
	public String ngoStateDistReportDownload(@RequestParam("reportId") int reportId, @RequestParam("typeId") int typeId,
			@RequestParam("reportTypeId") int reportTypeId, @RequestParam("ngoId") int ngoId) {

		return stateUserNgoWorkSpaceService.reportAndCertificateDownload(reportId, typeId, reportTypeId, ngoId);
	}

	/**
	 * download report in admin section of NGOWorkspace
	 */
	@Authorize(feature = "state_report", permission = "edit")
	@RequestMapping(value = "stateNgoDistReportAndCertificateDelete")
	@ResponseBody
	public boolean deleteReportsAndCertificate(@RequestParam("reportId") Integer reportId) {

		return stateUserNgoWorkSpaceService.deleteReportsAndCertificate(reportId);
	}

}
