package org.sdrc.missionmillet.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.service.UserService;
import org.sdrc.missionmillet.util.Constants;
import org.sdrc.missionmillet.util.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 
 * @author Subrata
 * 
 * This class will handle login and logout for a user. 
 *
 */

@Controller
public class LoginController implements AuthenticationProvider{
	
	@Autowired
	private ResourceBundleMessageSource messageSourceNotification;
	
	@Autowired
	private MessageDigestPasswordEncoder passwordEncoder;
	
	private final StateManager stateManager;
	
	private static final Logger	LOGGER	= LoggerFactory.getLogger( "LOGGER" );
	
	@Autowired
	public LoginController(StateManager stateManager){
		this.stateManager = stateManager;
	}
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String authorize(HttpServletRequest request, 
							RedirectAttributes redirectAttributes,
							@RequestParam("username") String username,
							@RequestParam("password") String password,
							Model model) throws IOException{
		
		List<String> errMessgs = new ArrayList<String>();
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = this.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (BadCredentialsException e) {
			LOGGER.info("Error description : "+ username+", "+messageSourceNotification.getMessage("user.not.found", null, null), e);
			SecurityContextHolder.getContext().setAuthentication(null);
			errMessgs.add(e.getMessage());
			redirectAttributes.addFlashAttribute("formError", errMessgs);
			redirectAttributes.addFlashAttribute("className",messageSourceNotification.getMessage("bootstrap.alert.danger",null, null));
			return "redirect:/login";
		}
		model.addAttribute("userDetail",((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL)));

		//Based on the type of user and their role, redirecting to their respective home page.
		if(((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL)).getTypeOfUser() == 47){
			return "redirect:/changepassword";
		}else{
			int roleId = ((CollectUserModel)stateManager.getValue(Constants.USER_PRINCIPAL)).
					getUserAreaModels().get(0).getUserRoleFeaturePermissionMappings().get(0).
					getRoleFeaturePermissionSchemeModel().getRole().getRoleId();
			return (roleId==1?"redirect:/stateSoE":roleId==2?"redirect:/districtNgoSoEManage":"redirect:/ngoSoE");	
		}
	}

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		CollectUserModel collectUserModel = userService.findByUsername(authentication.getName());
		String encodedPassword = passwordEncoder.encodePassword(authentication.getName(),(String)authentication.getCredentials());
		
		if (collectUserModel == null ||!collectUserModel.getPassword().equals(encodedPassword))
			throw new BadCredentialsException(messageSourceNotification.getMessage("invalid.username.password", null, null));
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		String ipAddress = userService.getIpAddr(request);
		String userAgent = request.getHeader("User-Agent");
		//when the user logs in web concatenate userAgent with "Web-Login: " 
		Long loginMetaId = userService.saveUserLoginMeta(ipAddress, collectUserModel.getUserId(), "Web-Login: "+userAgent);
		collectUserModel.setUserLoginMetaId(loginMetaId);
		stateManager.setValue(Constants.LOGIN_META_ID, loginMetaId);
		
		stateManager.setValue(Constants.USER_PRINCIPAL, collectUserModel);
		return new UsernamePasswordAuthenticationToken(authentication.getName(), (String)authentication.getCredentials(), null);
	}

	public boolean supports(Class<?> authentication) {
		return false;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse resp, RedirectAttributes redirectAttributes) {
		
		try {
			HttpSession session=request.getSession(false);
			if(session !=null){
				long userLoginMetaId = (long) stateManager.getValue(Constants.LOGIN_META_ID);
				userService.updateLoggedOutStatus(userLoginMetaId);
				stateManager.setValue(Constants.LOGIN_META_ID, null);
				stateManager.setValue(Constants.USER_PRINCIPAL, null);
				ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
				attr.getRequest().getSession(true).removeAttribute(Constants.USER_PRINCIPAL);
				attr.getRequest().getSession(true).invalidate();
				
				List<String> errMessgs = new ArrayList<String>();
				
				errMessgs.add("Successfully logged out!!");
				redirectAttributes.addFlashAttribute("formError", errMessgs);
				redirectAttributes.addFlashAttribute("className",messageSourceNotification.getMessage("bootstrap.alert.success",null, null));
				return "redirect:/login";
			} else{
				request.getSession().invalidate();
				return "redirect:/login";
			}
		} catch (Exception e) {
			LOGGER.info("Error description", e);
			return "redirect:/login";
		}
	}
}
