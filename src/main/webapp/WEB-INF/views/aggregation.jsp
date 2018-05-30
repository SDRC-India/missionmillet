<!--  
 Author Swarna(swarnaprava@sdrc.co.in)
  -->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="missionMilletApp">
<head>
<title>Mission Millet - Aggregation</title>
<jsp:include page="fragments/headtags.jsp" />
</head>
<style>
footer
{
	position: fixed !important;
	bottom: 0;
	width: 100%;
}

</style>


<body ng-controller="aggregationController" ng-cloak>
<jsp:include page="fragments/header.jsp" />
	<div class="container margin-heading">
		<div class="col-md-12">
			<h1 class="report-heading">Aggregation</h1>
		</div>
	</div>
	<div class="container"></div>
	
	

	<div class="container margin-heading">
	<div class="col-md-12 padding-zero">
	
		<div class="col-md-6 padding-zero">
				<div class="form-group col-md-12 padding-zero">
						<label class="form-group col-md-3 box-width">Financial Year :</label>
						<div class="form-group col-md-5 select-container  text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" id="report-type" placeholder="Select Year"
									class="form-control not-visible-input" name="report-type"
									readonly="" ng-model="selectedYear">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<span class="glyphicon glyphicon-menu-down"></span>
									</button>
									<ul class="dropdown-menu dist-ngodrp" style="min-width: 207px;" role="menu">
										<li ng-repeat="year in years" title="{{year}}"
											ng-click="selectYear(year)"><a href="">{{year}}</a></li>
									</ul>
								</div>
							</div>
						</div>
				</div>
			</div>
			<div class="col-md-6 padding-zero">
			<div class="form-group col-md-12 padding-zero">
					<div class="form-group col-md-8 padding-zero">
						<label class="form-group col-md-5 periodicity-align">Select Periodicity :</label>
						<div class="form-group col-md-7 select-container  text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" id="report-type" placeholder="Select Periodicity"
									class="form-control not-visible-input" name="report-type"
									readonly="" ng-model="selectedPeriodicity.description">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<span class="glyphicon glyphicon-menu-down"></span>
									</button>
									<ul class="dropdown-menu dist-ngodrp" style="min-width: 191px;" role="menu">
										<li ng-repeat="month in typeinPeriodicity"
										title="{{month.description}}" ng-click="selectPeriodicity(month)"><a
										href="">{{month.description}}</a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				</div>
		</div>
</div>

	<div class="container margin-heading margin-bottom" id="monthDetails">
		<div class="col-md-10" ng-show="monthlyData.length > 0 && selectedPeriodicity.description!=undefined">
			<table id="report-history" class="table table-striped"
				cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>SL. NO.</th>
						<th>PERIODICITY</th>
						<th>YEAR</th>
						<th>ACTION</th>
					</tr>
				</thead>
				
				<tbody>
					
					<tr ng-repeat="month in monthlyData">
						<td>{{$index+1}}</td>
						<td>{{month.shortNmae}}</td>
						<td>{{month.groupName}}</td>
						<td><button type="button" class="btn btn-success btn-aggregate" ng-disabled="month.isSelected == true"
						ng-click="modelSet(month.key,month.description); aggregatePeriodicity(month)">AGGREGATE</button>
							</td>
					</tr>
					
				</tbody>
			</table>
		</div>
		<div class="col-md-12" id="no_data" ng-hide="monthlyData.length > 0">
			<div class="container-fluid">
				<div class="col-md-12">
					<section class="profile-section col-md-12 col-sm-12 col-xs-12">
						<div class="col-md-12 profile-entry">
							<div id="profileTable" class="col-md-12 accordion-content expanded">
								<div class='content'>
									<h2 class="col-md-12 no_records">No Data Available.</h2>
								</div>
							</div>
						</div>
					</section>
				</div>
			</div>
		</div>
	</div>
	<div class="container margin-heading" id="quarterDetails">
		<div class="col-md-10" ng-show="quaterlyData.length > 0 && selectedPeriodicity.description!=undefined">
			<table id="report-history" class="table table-striped"
				cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>SL. NO.</th>
						<th>PERIODICITY</th>
						<th>YEAR</th>
						<th>ACTION</th>
					</tr>
				</thead>
				
				<tbody>
					
					<tr ng-repeat="quarter in quaterlyData">
						<td>{{$index+1}}</td>
						<td>{{quarter.shortNmae}}</td>
						<td>{{quarter.groupName}}</td>
						<td><button type="button" class="btn btn-success btn-aggregate" ng-disabled="quarter.isSelected == true"
						ng-click="modelSet(quarter.key,quarter.description); aggregatePeriodicity(quarter)">AGGREGATE</button>
							</td>
					</tr>
					
				</tbody>
			</table>
		</div>
		<div class="col-md-12" id="no_data" ng-hide="quaterlyData.length > 0">
			<div class="container-fluid">
				<div class="col-md-12">
					<section class="profile-section col-md-12 col-sm-12 col-xs-12">
						<div class="col-md-12 profile-entry">
							<div id="profileTable" class="col-md-12 accordion-content expanded">
								<div class='content'>
									<h2 class="col-md-12 no_records">No Data Available.</h2>
								</div>
							</div>
						</div>
					</section>
				</div>
			</div>
		</div>
	</div>
	<div class="container margin-heading" id="yearDetails">
		<div class="col-md-10" ng-show="yearlyData.length > 0 && selectedPeriodicity.description!=undefined">
			<table id="report-history" class="table table-striped"
				cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>SL. NO.</th>
						<th>PERIODICITY</th>
						<th>YEAR</th>
						<th>ACTION</th>
					</tr>
				</thead>
				
				<tbody>
					
					<tr ng-repeat="year in yearlyData">
						<td>{{$index+1}}</td>
						<td>{{year.shortNmae}}</td>
						<td>{{year.groupName}}</td>
						<td><button type="button" class="btn btn-success btn-aggregate" ng-disabled="year.isSelected == true"
						ng-click="modelSet(year.key,year.description); aggregatePeriodicity(year)">AGGREGATE</button>
							</td>
					</tr>
					
				</tbody>
			</table>
		</div>
		
		<div class="col-md-12" id="no_data" ng-hide="yearlyData.length > 0">
			<div class="container-fluid">
				<div class="col-md-12">
					<section class="profile-section col-md-12 col-sm-12 col-xs-12">
						<div class="col-md-12 profile-entry">
							<div id="profileTable" class="col-md-12 accordion-content expanded">
								<div class='content'>
									<h2 class="col-md-12 no_records">No Data Available.</h2>
								</div>
							</div>
						</div>
					</section>
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
	
	<div id="confirmRejectModal" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_success_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
					<div class="warnbody">{{warningMsg}}</div>
					
					<div class="container margin-heading" id="yearDetails">
						<div class="col-md-6">
							<table id="report-history" class="table table-striped"
								cellspacing="0" width="100%">
								<thead>
									<tr>
										<th>SL. NO.</th>
										<th>DISTRICT</th>
										<th>NGO</th>
										
									</tr>
								</thead>
								<tbody>
									<tr ng-repeat="list in pendingList">
										<td>{{$index+1}}</td>
										<td>{{list.shortNmae}}</td>
										<td>{{list.description}}</td>
										
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">OK</button>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="fragments/footer.jsp" />
	
<script type="text/javascript"
		src="resources/js/angularController/aggregationController.js"></script>
	<script type="text/javascript">
		var app = angular.module("missionMilletApp", []);
		var myAppConstructor = angular.module("missionMilletApp");
		myAppConstructor.controller("aggregationController",
				aggregationController);
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directives.js"></script>
<script type="text/javascript">
// $(document).ready(function() {
//     $('#example').DataTable();
// } );
</script>

</body>
</html>