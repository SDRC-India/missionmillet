package org.sdrc.missionmillet.controller;

import java.util.List;

import org.sdrc.missionmillet.core.Authorize;
import org.sdrc.missionmillet.model.ChangePasswordModel;
import org.sdrc.missionmillet.model.ValueObject;
import org.sdrc.missionmillet.service.UserService;
import org.sdrc.missionmillet.util.CustomErrorMessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@Authorize(feature = "change_password", permission = "edit")
	@RequestMapping(value="/changePassword",method=RequestMethod.POST)
	public ResponseEntity<CustomErrorMessageModel> getchangePassword(@RequestBody ChangePasswordModel changePasswordModel) {
		return new ResponseEntity<CustomErrorMessageModel>(userService.changePassword(changePasswordModel),HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value="/forgotPassword",method=RequestMethod.POST)
	public ResponseEntity<CustomErrorMessageModel> forgotPassowrd(@RequestBody ChangePasswordModel changePasswordModel){
		return new ResponseEntity<CustomErrorMessageModel>(userService.forgotPassword(changePasswordModel),HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value="/getUsers",method=RequestMethod.GET)
	public List<ValueObject> getUsers() {
		return userService.getUsers();
	}
	
	@Authorize(feature = "change_password", permission = "edit")
	@RequestMapping("/changepassword")
	String ngoReport(){
		return "changepassword"; 
	}
}
