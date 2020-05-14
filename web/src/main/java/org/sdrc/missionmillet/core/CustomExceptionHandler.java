package org.sdrc.missionmillet.core;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Subham Ashish (subham@sdrc.co.in)
 *
 */
@ControllerAdvice
public class CustomExceptionHandler {
	
	@RequestMapping(value="maintenance")
	@ExceptionHandler(CustomMaintenanceException.class)
	public String handleCustomException(CustomMaintenanceException ex) {
		
		return "maintenance";
		
	}

}
