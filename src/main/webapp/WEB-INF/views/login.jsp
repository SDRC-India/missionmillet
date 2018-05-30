<!-- 
@author Swarna (swarnaprava@sdrc.co.in)
-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="serror" uri="/WEB-INF/ErrorDescripter.tld"%>
<!DOCTYPE html>

<html ng-app="loginApp">
<head>
<title>Mission Millets-Login</title>
<jsp:include page="fragments/headtags.jsp" />
<style>
footer
{
	position: fixed !important;
	bottom: 0;
	width: 100%;
	z-index: 99;
}

</style>
</head>


<body ng-controller="loginController">
<!-- <div ng-show="invalid" ng-messages="loginForm.username.$error" role="alert"> -->
<!--     <div ng-message="required">Invalid Username or Password</div> -->
<!-- </div> -->

<%-- <jsp:include page="fragments/header.jsp" /> --%>
<serror:Error id="msgBoxlogin"  errorList="${formError}"
								cssInfClass="${className}">
								${formError} 
							</serror:Error>
<input type="hidden" id="loginStatus" value="${formError}">
<input type="hidden" id="userId" value="${userId}">

	<div class="container">
		<div class="row">
			<div class="col-md-8 responsive-view">
				<img src="resources/images/millets image.jpg"
					class="ri img-responsive">
			</div>
			<div class="col-md-4 text-center responsive-view">
				<div class="row">
					<div class="col-md-12 text-center ipad-view">
						<img src="resources/images/OSSSC-Logo.jpg"
							class="odisha-gov img-responsive">
					</div>
				</div>
				<form method="post" action="login" id="loginForm" name="loginForm">
								<div class="row" >
								<div class="col-md-12 col-sm-6 col-xs-8 responsive-ipad" >
								<h3 class="bottom-margin">A Odisha Government Initiative</h3>
								<h2 class="font-info">Special Programme for Promotion in Millets in Tribal Areas of Odisha</h2>
								<h1 class="mission_header"> Odisha Millets Mission </h1>
									
										<div class="form-group">
										<div class="input-group">
												<span class="input-group-addon"> <i
													class="glyphicon glyphicon-user"></i>
												</span> <input required class="form-control" placeholder="Username"
													name="username" ng-model="username" enter-key="submitLogin()"
													 type="text" autofocus>
											</div>
										</div>
										<div class="form-group">
											<div class="input-group">
												<span class="input-group-addon"> <i
													class="glyphicon glyphicon-lock"></i>
												</span> <input required class="form-control" placeholder="Password"
													name="password" ng-model="password" enter-key="submitLogin()"
													 type="password" value="">
											</div>
										</div>
										<div class="form-group">
											<button type="submit" enter-key="submitLogin()"
												class="btn btn-lg btn-primary btn-block loginBtn"
												value="LOGIN">SUBMIT</button>
										</div>
										
										<div class="form-group" ng-click="forgotPassClick();">
											<h5 class="text-center">
												<a class="chnagePass">Forgot password ? </a>
											</h5>
										</div>
										
									</div>
								</div>
							</form>
				<div class="row">
					<div class="col-md-12">
						<img src="resources/images/NCDS70-70px.png" class="ncds-logo" >
						 <img src="resources/images/wassan70-70px.png" class="wassan-logo">
					</div>
				</div>
			</div>
		</div>
	</div>

			<div class="container">
					<div class="row">
						<div class="height-bottom"></div>
					</div>
				</div>

	<div id="forgotPassModal" class="login-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead" style="letter-spacing: 1px;">Forgot Password</div>
					 <div>
						<div class="select-container">
						  <form name="myform">
							<div class="forgotpwbody row">
							  <div class="col-md-12">
								<div class="form-group user-padding">
										<label class="col-md-3 col-sm-3 col-xs-3" for="textinput">User Name<span
											class="mandatory_star">&#42;</span></label>
										<div class="col-md-9">
											<input type="text" id="username" name="username"
												placeholder="Enter User Name" ng-model="forgotUserId"
												class="form-control" 
												autocomplete="off" required
												ng-change ="removeErr();"
												onPaste="return false" />
											<span id="userName" class="userName"></span>
										</div>
									</div>
									<div class="form-group">
										<label class="col-md-3 col-sm-3 col-xs-3" for="textinput">Email ID<span
											class="mandatory_star">&#42;</span></label>
										<div class="col-md-9">
											<input type="text" id="email" name="email"
												placeholder="Enter Email ID" ng-model="regEmail"
												class="form-control inputBackground-email"
												autocomplete="off" required
												ng-change ="removeErr()"
												onPaste="return false"/>
											<span id="emailErr" class="userName"></span>
										</div>
									</div>
									<span id="validErr" class="validUser"></span>
								</div>
							</div>
						</form>
					  </div>
					 </div>
					<button id="sendButtn" type="button" class="btn errorOk" ng-click="checkValidation();">Send</button>
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="clear();">Cancel</button>
				</div>
			</div>
		</div>
	</div>
	
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
	
	
	
  <jsp:include page="fragments/footer.jsp" />
  <script type="text/javascript" src="resources/js/angularController/loginController.js"></script>
  <script type="text/javascript">
	var app = angular.module("loginApp", []);
	var myAppConstructor = angular.module("loginApp");
	myAppConstructor.controller("loginController", loginController);
  </script>
  <script type="text/javascript"
		src="resources/js/angularDirective/directives.js"></script>
  
</body>

<script type="text/javascript">
$(document).ready(function(){
	if( $(".alert")[0] != undefined){
		$("#msgBoxlogin").fadeTo(4000, 0).slideUp(500, function(){
		    $("#msgBoxlogin").slideUp(500);
		});
	}
});
</script>

</html>