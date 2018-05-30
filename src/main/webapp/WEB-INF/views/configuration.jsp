<!--  
 Author Swarna(swarnaprava@sdrc.co.in)
  -->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="milletApp">
<head>
<title>Mission Millet - Configuration</title>
<jsp:include page="fragments/headtags.jsp" />
</head>

<style>
footer {
    background-color: #242a2c;
    padding: 10px 0 10px 0;
    color: #454e51;
    margin-top: 50px;
    position: fixed;
    bottom: 0;
    width: 100%;
}
</style>

<body ng-controller="configController" ng-cloak>
<jsp:include page="fragments/header.jsp" />
	<div class="container margin-heading">
		<div class="col-md-12">
			<h1 class="report-heading">Configuration</h1>
		</div>
	</div>
	<div class="container"></div>

	<div class="container margin-heading">
	<div class="col-md-12 padding-zero">
			<div class="col-md-6 padding-zero">
				<div class="form-group col-md-12 padding-zero">
					<label class="form-group col-md-4 labelFont sectorleft">Configuration Type :<span class="mandatory_star">&#42;</span></label>
					<div class="form-group col-md-8">
						<div class="input-group">
							<input type="text" id="config-type" placeholder="Select configuration type"
								class="form-control not-visible-input" name="config-type"
								readonly="" ng-model="selectedconfigType.description">
							<div class="input-group-btn" style="position: relative;">
								<button data-toggle="dropdown"
									class="btn btn-color dropdown-toggle" type="button">
									<span class="glyphicon glyphicon-menu-down"></span>
								</button>
								<ul class="dropdown-menu type-dropsowns" role="menu">
									<li ng-repeat="config in description"
										title="{{config.description}}" ng-click="selectConfigtype(config)"><a
										href="">{{config.description}}</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>

	<div class="form-group col-md-12 padding-zero">
					<label class="form-group col-md-4 labelFont sectorleft">Cut off Date (days from the end of the reporting month )<span class="mandatory_star">&#42;</span></label>
					<div class="form-group col-md-8 select-container text-center">
						<div class="input-group">
							<input type="text" id="cutoff-date"	class="form-control textbox-border" maxlength="2"
							ng-model="cutofDate" ng-change="enteredCutofDate(cutofDate)" name="cutoff-date"
							ng-keypress="filterValue($event)">
						</div>
					</div>
				</div>
				
			</div>
	
		</div>
</div>

	<div class="container">
		<div class="col-md-12 padding-zero">
			<div class="form-group col-md-12">
				<div class="form-group col-md-3 padding-zero">
					<button type="submit" ng-click="submitReport()"
						class="btn btn-lg btn-primary btn-block submit-btn" value="SUBMIT">SUBMIT</button>
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
					<div class="errorbody">{{validationMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<div id="errorMessageOfCutofdate" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; ERROR</div>
					<div class="errorbody">{{validationMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal" onClick="window.location.reload();">Close</button>
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
	
	<div id="remarksDetailsModal" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
					<div class="warnbody"><p class="remarks-SelectedType">{{remarksInfo}}</p></div>
					<button type="button" class="btn errorOk" data-dismiss="modal" data-dismiss="modal">OK</button>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="fragments/footer.jsp" />
	
<script type="text/javascript"	src="resources/js/angularController/configController.js"></script>

	<script type="text/javascript">
		var app = angular.module("milletApp", []);
		var myAppConstructor = angular.module("milletApp");
		myAppConstructor.controller("configController",
				configController);
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directives.js"></script>
	
	
	

</body>
</html>