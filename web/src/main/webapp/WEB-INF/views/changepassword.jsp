

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="serror" uri="/WEB-INF/ErrorDescripter.tld"%>
<%@page import="org.sdrc.missionmillet.model.UserRoleFeaturePermissionMappingModel"%>
<%@page import="org.sdrc.missionmillet.util.Constants"%>
<%@page import="org.sdrc.missionmillet.model.CollectUserModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%
Integer role = 0;
Integer typeOfUser = 0;
CollectUserModel user = null;
List<String> features = new ArrayList<String>();
List<String> permissions = new ArrayList<String>();

	if (request.getSession().getAttribute(Constants.USER_PRINCIPAL) != null) {
		user = (CollectUserModel) request.getSession().getAttribute(
				Constants.USER_PRINCIPAL);
		typeOfUser = user.getTypeOfUser();
		if(typeOfUser==46)
			role = user.getUserAreaModels().get(0)
				.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getRole()
				.getRoleId();

	}
%>
<!DOCTYPE html>

<html>
<head>
<title>Mission Millet - Change Password</title>
<jsp:include page="fragments/headtags.jsp" />
</head>
<body ng-app="passwordChangeApp"
	ng-controller="passwordChangeController" ng-cloak >
	<jsp:include page="fragments/header.jsp" />
	<input name="username" type="hidden" id="username" value="<%=user.getName() %>"/>
	<div  class="container change_psswrd margin-top">
		<div class="cont_header_container">
			<div class="text-center changePass_header">Change Password</div>
		</div>

		<form class="form-horizontal form-reg" name="myForm1">
		<div class="row pass-page">
				<div class="form-group">
					<label class="control-label labelcursor col-sm-4 col-md-4"
						for="pwd">Username<span style="color: red">*</span>
					</label>
					<div class="col-sm-5 col-md-4 changePass">
						<input type="text" id="userName" class="form-control not-visible-input changePassList"
							name="username" readonly="" ng-model="selectedUser" 
							placeholder="Enter User Name">
							<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<span class="glyphicon glyphicon-menu-down"></span>
									</button>
									<ul class="dropdown-menu userlist-drpdwn" role="menu" ng-if="userListLength!=1" ng-disabled="userListLength==1">
										<li ng-repeat="names in userListData | orderBy: 'description'" title="{{names.description}}"
											ng-click="selectUserList(names.description,names.groupName)">{{names.description}}</li>
									</ul>
								</div>
					</div>
				</div>

				<div class="form-group" ng-show="oldPass_drpdwn">
					<label class="control-label labelcursor col-sm-4 col-md-4"
						for="pwd">Old password<span style="color: red">*</span>
					</label>
					<div class="col-sm-5 col-md-4">
						<input type="password" id="oldpassword" class="form-control"
							ng-keyup="function1()" name="oldpass" ng-model="user.oldPassword"
							ng-change="oldPassError()" placeholder="Enter old password" disallow-spaces>
						<span id="oldPasserror"></span>
					</div>
				</div>

				<div class="form-group">
					<label class="control-label labelcursor col-sm-4 col-md-4"
						for="mname">New password<span style="color: red">*</span></label>
					<div class="col-sm-5 col-md-4">
						<input type="password" class="form-control" id="txtPassword"
							minlength="8" maxlength="30" ng-keyup="newpass()"
							ng-model="user.newPassword" placeholder="Enter new password"
							ng-change="newPassError()" name="newpass" disallow-spaces>
						    <span  
								class="minChar" ng-show="myForm1.newpass.$error.minlength && myForm1.newpass.$dirty"  
							style="color: red"> The password must have minimum 8 characters. </span> 
						<span id="newPasserror" ng-hide="myForm1.newpass.$error.minlength && myForm1.newpass.$dirty"></span>
					</div>
				</div>

				<div class="form-group">
					<label class="control-label labelcursor col-sm-4 col-md-4"
						for="mname">Confirm password<span style="color: red">*</span></label>
					<div class="col-sm-5 col-md-4">
						<input type="password" class="form-control"
							id="txtConfirmPassword" ng-keyup="hideerrormessage()"
							ng-model="user.confirmPassword" minlength="8" maxlength="30"
							placeholder="Confirm New Password" name="confirmpass"
							ng-change="confirmPassError()" disallow-spaces> <span
							id="confirmPasserror"></span>
					</div>
				</div>


				<div class="form-group">
					<label class="control-label labelcursor col-sm-4 col-md-4"
						for="mname"></label>
					<div class="col-sm-5 col-md-4">
						<button type="submit" id="btnSubmit"
							ng-click="changeUserPassword()"
							class="btn btn-lg btn-primary btn-block loginBtn" value="Submit">Submit</button>
					</div>
				</div>
			</div>
		</form>
	</div>
	<!-- Modal -->

	
	<div id="pop" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="successhead"><img alt="" src="resources/images/icons/Messages_success_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; SUCCESS</div>
					<div class="successbody">{{msg}}</div>
					<button type="button" class="btn" data-dismiss="modal" onClick="window.location.reload();">OK</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="errorMessage" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; ERROR</div>
					<div class="errorbody padding-errorbody">{{validationMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="container">
		<div class="row">
			<div class="height-ChangePass"></div>
		</div>
	</div>
	
	<jsp:include page="fragments/footer.jsp"></jsp:include>
	<script type="text/javascript" src="resources/js/angularController/passwordController.js"></script>
</body>
</html>
 