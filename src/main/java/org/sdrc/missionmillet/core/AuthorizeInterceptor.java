package org.sdrc.missionmillet.core;


/**
 * 
 * @author Subrata
 * 
 */
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.model.FeaturePermissionMappingModel;
import org.sdrc.missionmillet.model.UserAreaModel;
import org.sdrc.missionmillet.util.Constants;
import org.sdrc.missionmillet.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {

	private final StateManager stateManager;
	private final ResourceBundleMessageSource messageSourceNotification;

	@Autowired
	public AuthorizeInterceptor(StateManager stateManager, ResourceBundleMessageSource messageSourceNotification) {
		this.stateManager = stateManager;
		this.messageSourceNotification = messageSourceNotification;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws AccessDeniedException, NoSuchMessageException {
		
		if (!(handler instanceof HandlerMethod))
			return true;

		Authorize authorize = ((HandlerMethod) handler).getMethodAnnotation(Authorize.class);

		if (authorize == null)
			return true;

			CollectUserModel user = (CollectUserModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		if (user == null){
			response.setStatus(401);
			throw new AccessDeniedException(messageSourceNotification.getMessage(Constants.ACCESS_DENIED, null, null));
		}
		
		List<String> features=Arrays.asList(authorize.feature().split(","));
		String permission = authorize.permission();
		
		List<UserAreaModel> userAreaModels = user != null ? user.getUserAreaModels() : null;
		
		if (null != userAreaModels) {
			for (UserAreaModel userAreaModel : userAreaModels) {
				if (userAreaModel.getUserRoleFeaturePermissionMappings() != null) {
					for (int i = 0; i < userAreaModel.getUserRoleFeaturePermissionMappings().size(); i++) {
						FeaturePermissionMappingModel fpMapping = userAreaModel.getUserRoleFeaturePermissionMappings()
								.get(i).getRoleFeaturePermissionSchemeModel().getFeaturePermissionMapping();
						if (features.contains(fpMapping.getFeature().getFeatureName())
								&& permission.equals(fpMapping.getPermission().getPermissionName())) {
							return true;
						}
					}

				}
			}
		}
		
		throw new AccessDeniedException(messageSourceNotification.getMessage(
				Constants.ACCESS_DENIED, null, null));
	}

}
