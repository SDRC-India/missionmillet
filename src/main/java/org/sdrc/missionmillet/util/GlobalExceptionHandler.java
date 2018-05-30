package org.sdrc.missionmillet.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;

    @ExceptionHandler(value = MultipartException.class)
    public ResponseEntity<CustomErrorMessageModel> handleMultipartException(Exception ex, HttpServletRequest request){
    	CustomErrorMessageModel customErrorMessageModel = new CustomErrorMessageModel();
    	customErrorMessageModel.setMessage(messageSourceNotification.getMessage("max.upload.size", null, null));
    	customErrorMessageModel.setStatusCode(500);
    	return new ResponseEntity<>(customErrorMessageModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}