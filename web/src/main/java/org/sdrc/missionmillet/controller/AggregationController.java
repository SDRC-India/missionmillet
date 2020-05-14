package org.sdrc.missionmillet.controller;

import java.util.List;

import org.sdrc.missionmillet.core.Authorize;
import org.sdrc.missionmillet.model.ValueObject;
import org.sdrc.missionmillet.service.AggregationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author Subrata
 * This controller will handle the aggregation request.
 */
@Controller
public class AggregationController {
	
	@Autowired
	AggregationService aggregationService;
	
	
	@Authorize(feature="state_soe_reports", permission="edit")
	@RequestMapping(value = { "/aggregate" }, method = RequestMethod.POST)
	@ResponseBody
	public String aggregate(@RequestParam("monthId") int monthId, @RequestParam("periodicity") int periodicity) {
		return aggregationService.aggregate(monthId, periodicity);
	}
	
	@Authorize(feature="state_soe_reports",permission="view")
	@RequestMapping("/aggregation")
	String aggregation(){
		return "aggregation"; 
	}
	
	@Authorize(feature="state_soe_reports",permission="view")
	@RequestMapping(value="/getPendingList", method = RequestMethod.POST)
	@ResponseBody
	List<ValueObject> getPendingList(@RequestParam("monthId") int monthId, @RequestParam("periodicity") int periodicity){
		return aggregationService.getPendingList(monthId, periodicity); 
	}
}
