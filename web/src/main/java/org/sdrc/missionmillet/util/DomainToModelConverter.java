package org.sdrc.missionmillet.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sdrc.missionmillet.domain.AreaLevel;
import org.sdrc.missionmillet.domain.CollectUser;
import org.sdrc.missionmillet.domain.Feature;
import org.sdrc.missionmillet.domain.FeaturePermissionMapping;
import org.sdrc.missionmillet.domain.Permission;
import org.sdrc.missionmillet.domain.Role;
import org.sdrc.missionmillet.domain.RoleFeaturePermissionScheme;
import org.sdrc.missionmillet.domain.UserAreaMapping;
import org.sdrc.missionmillet.domain.UserRoleFeaturePermissionMapping;
import org.sdrc.missionmillet.domain.XForm;
import org.sdrc.missionmillet.model.AreaLevelModel;
import org.sdrc.missionmillet.model.CollectUserModel;
import org.sdrc.missionmillet.model.FeatureModel;
import org.sdrc.missionmillet.model.FeaturePermissionMappingModel;
import org.sdrc.missionmillet.model.PermissionModel;
import org.sdrc.missionmillet.model.RoleFeaturePermissionSchemeModel;
import org.sdrc.missionmillet.model.RoleModel;
import org.sdrc.missionmillet.model.UserAreaModel;
import org.sdrc.missionmillet.model.UserRoleFeaturePermissionMappingModel;
import org.sdrc.missionmillet.model.ValueObject;
import org.sdrc.missionmillet.model.XFormModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author Subrata
 * 
 * Setting role, feature, permission, area, xform(mobile) for an user.
 * 
 */
@Component
@Transactional(readOnly=true)
public class DomainToModelConverter {

public static List<ValueObject> toUserRoleFeaturePermissionMappingValueObjs(List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings) {
	List<ValueObject> valueObjects = new ArrayList<ValueObject>();
	for (UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : userRoleFeaturePermissionMappings) {
		ValueObject valueObject = new ValueObject(Integer.toString(userRoleFeaturePermissionMapping.getUserRoleFeaturePermissionId()), userRoleFeaturePermissionMapping.getRoleFeaturePermissionScheme().getSchemeName());
		valueObjects.add(valueObject);
	}
	return valueObjects;
}

public static List<CollectUserModel> toUserDetailModels(List<UserAreaMapping> userDetails) {
	List<CollectUserModel> userDetailModels = new ArrayList<CollectUserModel>();
	for (UserAreaMapping userDetail : userDetails) {
		CollectUserModel userDetailModel = new CollectUserModel();

	
		userDetailModel.setPassword(userDetail.getUser().getPassword());
		userDetailModel.setUserId(userDetail.getUser().getUserId());
		userDetailModel.setUsername(userDetail.getUser().getUsername());

		userDetailModels.add(userDetailModel);
	}
	return userDetailModels;
}

public static List<FeaturePermissionMappingModel> toFeaturePermissionMappings(List<FeaturePermissionMapping> featurePermissionMappings) {
	List<FeaturePermissionMappingModel> featurePermissionMappingModels = new ArrayList<FeaturePermissionMappingModel>();
	for (FeaturePermissionMapping featurePermissionMapping : featurePermissionMappings) {
		FeaturePermissionMappingModel featurePermissionMappingModel = new FeaturePermissionMappingModel();
		featurePermissionMappingModel.setFeaturePermissionId(featurePermissionMapping.getFeaturePermissionId());
		featurePermissionMappingModel.setUpdatedBy(featurePermissionMapping.getUpdatedBy());
		featurePermissionMappingModel.setPermission(toPermissionModel(featurePermissionMapping.getPermission()));
		featurePermissionMappingModel.setFeature(toFeatureModels(Arrays.asList(featurePermissionMapping.getFeature())).get(0));
		featurePermissionMappingModel.setRoleFeaturePermissionSchemeModels(toRoleFeaturePermissionSchemeModelValueObjs(featurePermissionMapping.getRoleFeaturePermissionSchemes()));
		featurePermissionMappingModels.add(featurePermissionMappingModel);
	}
	return featurePermissionMappingModels;
}

public static List<ValueObject> toRoleFeaturePermissionSchemeModelValueObjs(List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes) {
	List<ValueObject> valueObjects = new ArrayList<ValueObject>();
	for (RoleFeaturePermissionScheme roleFeaturePermissionScheme : roleFeaturePermissionSchemes) {
		ValueObject valueObject = new ValueObject(roleFeaturePermissionScheme.getRoleFeaturePermissionSchemeId(), roleFeaturePermissionScheme.getSchemeName());
		valueObjects.add(valueObject);
	}
	return valueObjects;
}

public static List<FeatureModel> toFeatureModels(List<Feature> features) {
	List<FeatureModel> featureModels = new ArrayList<FeatureModel>();
	for (Feature feature : features) {
		FeatureModel featureModel = new FeatureModel();
		featureModel.setFeatureId(feature.getFeatureId());
		featureModel.setFeatureCode(feature.getFeatureCode());
		featureModel.setDescription(feature.getDescription());
		featureModel.setFeatureName(feature.getFeatureName());
		featureModel.setFeaturePermissionMappings(toFeaturePermissionMappingModelValueObjs(feature.getFeaturePermissionMappings()));
		featureModel.setUpdatedBy(feature.getUpdatedBy());
		// featureModel.setUpdatedDate(feature.getUpdatedDate());
		featureModels.add(featureModel);
	}
	return featureModels;
}

public static List<ValueObject> toFeaturePermissionMappingModelValueObjs(List<FeaturePermissionMapping> featurePermissionMappings) {
	List<ValueObject> valueObjects = new ArrayList<ValueObject>();
	for (FeaturePermissionMapping featurePermissionMapping : featurePermissionMappings) {
		ValueObject valueObject = new ValueObject(Integer.toString(featurePermissionMapping.getFeaturePermissionId()), featurePermissionMapping.getPermission().getPermissionName());
		valueObjects.add(valueObject);
	}
	return valueObjects;
}

public static PermissionModel toPermissionModel(Permission permission) {
	PermissionModel permissionModel = new PermissionModel();
	permissionModel.setPermissionId(permission.getPermissionId());
	permissionModel.setPermissionCode(permission.getPermissionCode());
	permissionModel.setDescription(permission.getDescription());
	permissionModel.setUpdatedBy(permission.getUpdatedBy());
	permissionModel.setPermissionName(permission.getPermissionName());
	permissionModel.setFeaturePermissionMappings(toFeaturePermissionMappingModelValueObjs(permission.getFeaturePermissionMappings()));

	return permissionModel;
}

public static RoleModel toRoleModel(Role role) {
	RoleModel roleModel = new RoleModel();
	roleModel.setDescription(role.getDescription());
	roleModel.setRoleCode(role.getRoleCode());
	// roleModel.setRoleFeaturePermissionSchemes(toRoleFeaturePermissionSchemeModelValueObjs(role.getRoleFeaturePermissionSchemes()));
	roleModel.setRoleId(role.getRoleId());
	roleModel.setRoleName(role.getRoleName());

	return roleModel;
}

public static List<RoleFeaturePermissionSchemeModel> toRoleFeaturePermissionSchemeModels(List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes) {
	List<RoleFeaturePermissionSchemeModel> roleFeaturePermissionSchemeModels = new ArrayList<RoleFeaturePermissionSchemeModel>();
	for (RoleFeaturePermissionScheme roleFeaturePermissionScheme : roleFeaturePermissionSchemes) {
		RoleFeaturePermissionSchemeModel roleFeaturePermissionSchemeModel = new RoleFeaturePermissionSchemeModel();
		roleFeaturePermissionSchemeModel.setFeaturePermissionMapping(toFeaturePermissionMappings(Arrays.asList(roleFeaturePermissionScheme.getFeaturePermissionMapping())).get(0));
		roleFeaturePermissionSchemeModel.setRoleFeaturePermissionSchemeId(roleFeaturePermissionScheme.getRoleFeaturePermissionSchemeId());
		roleFeaturePermissionSchemeModel.setSchemeName(roleFeaturePermissionScheme.getSchemeName());
		roleFeaturePermissionSchemeModel.setUpdatedBy(roleFeaturePermissionScheme.getUpdatedBy());
		roleFeaturePermissionSchemeModel.setRole(toRoleModel(roleFeaturePermissionScheme.getRole()));
		roleFeaturePermissionSchemeModel.setUserRoleFeaturePermissionMappings(toUserRoleFeaturePermissionMappingModelValueObjs(roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings()));
		roleFeaturePermissionSchemeModels.add(roleFeaturePermissionSchemeModel);
	}
	return roleFeaturePermissionSchemeModels;
}

public static List<ValueObject> toUserRoleFeaturePermissionMappingModelValueObjs(List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings) {
	List<ValueObject> valueObjects = new ArrayList<ValueObject>();
	for (UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : userRoleFeaturePermissionMappings) {
		ValueObject valueObject = new ValueObject(Integer.toString(userRoleFeaturePermissionMapping.getUserRoleFeaturePermissionId()), userRoleFeaturePermissionMapping.getRoleFeaturePermissionScheme().getRole().getRoleName());
		valueObjects.add(valueObject);
	}
	return valueObjects;
}

public static UserRoleFeaturePermissionMappingModel toUserRoleFeaturePermissionMappingModel(UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping) {
	UserRoleFeaturePermissionMappingModel userRoleFeaturePermissionMappingModel = new UserRoleFeaturePermissionMappingModel();
	userRoleFeaturePermissionMappingModel.setRoleFeaturePermissionSchemeModel(toRoleFeaturePermissionSchemeModels(Arrays.asList(userRoleFeaturePermissionMapping.getRoleFeaturePermissionScheme())).get(0));
	userRoleFeaturePermissionMappingModel.setUpdatedBy(userRoleFeaturePermissionMapping.getUpdatedBy());
	userRoleFeaturePermissionMappingModel.setUserDetailModel(toUserDetailModelValueObjs(userRoleFeaturePermissionMapping.getUserAreaMapping().getUser()));
	userRoleFeaturePermissionMappingModel.setUserRoleFeaturePermissionId(userRoleFeaturePermissionMapping.getUserRoleFeaturePermissionId());
	userRoleFeaturePermissionMappingModel.setAreaId(userRoleFeaturePermissionMapping.getUserAreaMapping().getArea().getAreaId());
	return userRoleFeaturePermissionMappingModel;
}

public static List<UserRoleFeaturePermissionMappingModel> toUserRoleFeaturePermissionMappingModels(List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings) {
	List<UserRoleFeaturePermissionMappingModel> userRoleFeaturePermissionMappingModels = new ArrayList<UserRoleFeaturePermissionMappingModel>();
	for (UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : userRoleFeaturePermissionMappings) {
		UserRoleFeaturePermissionMappingModel userRoleFeaturePermissionMappingModel = toUserRoleFeaturePermissionMappingModel(userRoleFeaturePermissionMapping);
		userRoleFeaturePermissionMappingModels.add(userRoleFeaturePermissionMappingModel);
	}
	return userRoleFeaturePermissionMappingModels;
}

public static List<UserAreaModel> toUserAreaMappingModel(List<UserAreaMapping> userAreaMappings) {
	List<UserAreaModel> userAreaModels = new ArrayList<UserAreaModel>();
	for (UserAreaMapping userAreaMapping : userAreaMappings) {
		UserAreaModel userAreaModel = new UserAreaModel();
		userAreaModel.setAreaModel(new ValueObject(userAreaMapping.getArea().getAreaId(), userAreaMapping.getArea().getAreaName(), userAreaMapping.getArea().getAreaLevel().getAreaLevelId()));
		userAreaModel.setUserId(userAreaMapping.getUser().getUserId());
		userAreaModel.setUserAreaMappingId(userAreaMapping.getUserAreaMappingId());
		userAreaModel.setUserRoleFeaturePermissionMappings(toUserRoleFeaturePermissionMappingModels(userAreaMapping.getUserRoleFeaturePermissionMappings()));
		userAreaModels.add(userAreaModel);
	}

	return userAreaModels;
}

public static ValueObject toUserDetailModelValueObjs(CollectUser userDetail) {
	ValueObject valueObject = new ValueObject(Integer.toString(userDetail.getUserId()), userDetail.getUsername());
	return valueObject;
}
/**
 * 
 * @param xForm
 * @return
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 */
public XFormModel toXFormModel(XForm xForm) {
	// TODO Auto-generated method stub
	XFormModel xFormModel = new XFormModel();
	
	xFormModel.setAreaLevelModel(toAreaLevelModel(xForm.getAreaLevel()));
	xFormModel.setAreaXPath(xForm.getAreaXPath());
	xFormModel.setDateOfVisitXPath(xForm.getDateOfVisitXPath());
	xFormModel.setFormId(xForm.getFormId());
	xFormModel.setLive(xForm.isLive());
	xFormModel.setOdkServerURL(xForm.getOdkServerURL());
	xFormModel.setPassword(xForm.getPassword());
	xFormModel.setSecondaryAreaXPath(xForm.getSecondaryAreaXPath());
	xFormModel.setLocationXPath(xForm.getLocationXPath());
	xFormModel.setUsername(xForm.getUsername());
	xFormModel.setxFormId(xForm.getxFormId());
	xFormModel.setToEmailIdsXPath(xForm.getToEmailIdsXPath());
	xFormModel.setCcEmailIds(xForm.getCcEmailIds());
	xFormModel.setxFormTitle(xForm.getxFormIdTitle());
	xFormModel.setSendRawData(xForm.isSendRawData());
	xFormModel.setxFormIdByODK(xForm.getRootElement());
	xFormModel.setRootElement(xForm.getRootElement());
	
	return xFormModel;
}

/**
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @param areaLevel The Entity
 * @return AreaLevelModel The Model
 */
public AreaLevelModel toAreaLevelModel(AreaLevel areaLevel) {
	// TODO Auto-generated method stub
	AreaLevelModel areaLevelModel = new AreaLevelModel();
	
	areaLevelModel.setAreaLevelId(areaLevel.getAreaLevelId());
	areaLevelModel.setAreaLevelName(areaLevel.getAreaLevelName());
	
	return areaLevelModel;
}




}