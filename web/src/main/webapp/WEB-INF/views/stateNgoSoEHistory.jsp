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
<title>Mission Millet - STATEMENT OF EXPENDITURE</title>
<jsp:include page="fragments/headtags.jsp" />
</head>


<body ng-controller="stateNgoSoEHistoryController" ng-cloak>
<jsp:include page="fragments/header.jsp" />
	<div class="container margin-heading">
		<div class="col-md-12">
			<h1 class="report-heading">Statement of Expenditure - History</h1>
		</div>
	</div>
	
	<div class="container form-group">
	<div class="col-md-12 padding-zero report-table">
			<div class="col-md-4 padding-zero">
			
					<div class="form-group col-md-12 padding-zero">
						<label class="form-group col-md-5 padding-right">Financial Year :</label>
						<div class="form-group col-md-7 select-container  text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" id="report-type"
									class="form-control not-visible-input" name="report-type"
									readonly="" ng-model="yearName" placeholder="Select Year">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<span class="glyphicon glyphicon-menu-down"></span>
									</button>
									<ul class="dropdown-menu year-dropdown" role="menu">
										<li ng-repeat="year in years" title="{{year}}"
											ng-click="selectYear(year)"><a href="">{{year}}</a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				
			</div>


			<div class="col-md-8 padding-zero">
				<div class="form-group col-md-12">
					<div class="form-group col-md-4 padding-zero search-bar">
						<div id="imaginary_container">
							<div class="input-group stylish-input-group">
								<input type="text" class="form-control searchbar-box" ng-model="searchHistory"
									placeholder="Search"> <span class="input-group-addon">
									<button type="submit">
										<span class="glyphicon glyphicon-search"></span>
									</button>
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>



	<div class="container">
		<div class="col-md-12 table-responsive padd_lock_table" ng-show="stateNgoSoetHistory.length > 0">
		<div
				class="tableMargin table-responsive fix-level-table header-fixed-table"
				style="width: 100%; max-height: 450px; overflow: auto;"
				sdrc-table-header-fix tableuniqueclass="'fix-level-table'"
				tabledata="stateNgoSoetHistory" >
			<table id="report-history" class="table table-striped "
				cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>SL. NO.</th>
						<th>DISTRICT</th>
						<th>NGO</th>
						<th>MONTH</th>
						<th>YEAR</th>
						<th>UPLOADED ON</th>
						<th>DOWNLOAD</th>
						<th>STATUS</th>
						<th>APPROVED/REJECTED BY</th>
						<th>REMARKS</th>
					</tr>
				</thead>
				
				<tbody>
					
					<tr ng-repeat="details in stateNgoSoetHistory | filter:searchHistory">
						<td>{{$index+1}}</td>
						<td>{{details.districtName}}</td>
						<td>{{details.ngoName}}</td>
						<td>{{details.month}}</td>
						<td>{{details.year}}</td>
						<td ng-if="details.statusDetail !='No action taken'">{{details.date | date: 'dd-MM-yyyy'}}</td>
						<td ng-if="details.statusDetail =='No action taken'"></td>
						<td title="Download"><button type="button" class="btn btn-default btn-sm" ng-disabled="details.statusDetail =='No action taken'"
						data-toggle="tooltip" title="Download" data-placement="top" ng-click="downloadHistory(details.reportId,details.ngoId,details.ngoName)">
								<span class="glyphicon glyphicon-download-alt"></span>
							</button></td>
						<td ng-if="details.statusDetail !='No action taken'"><p ng-class="{statusInfoApproval:details.statusDetail == 'Approved', statusInfoRejection:details.statusDetail == 'Rejected'}">{{details.statusDetail}}({{details.actionTakenDate | date: 'dd-MM-yyyy'}})</p></td>
						<td ng-if="details.statusDetail =='No action taken'"><p>{{details.statusDetail}}</p></td>
						<td>{{details.actionTakenByName}}</td>
						<td><input tabindex="-1" type='text' readonly
							class='report-Input' ng-model='details.remark'>
							<button type="button" class="btn btn-default btn-sm"
								ng-click="readMore(details.remark)"
								ng-show="details.remark.length > 20">Read More..</button></td>
					</tr>
					
				</tbody>
			</table>
			</div>
			<div class="col-md-12" id="no-data-search" ng-show="(stateNgoSoetHistory | filter:searchHistory).length == 0">
			<div class="container-fluid">
				<div class="col-md-12">
					<section class="profile-section col-md-12 col-sm-12 col-xs-12">
						<div class="col-md-12 profile-entry">
							<div id="profileTable" class="col-md-12 accordion-content expanded">
								<div class='content'>
									<h2 class="col-md-12 no_records">No Results Found.</h2>
								</div>
							</div>
						</div>
					</section>
				</div>
			</div>
		</div>
		</div>
		<div class="col-md-12" id="no_data" ng-hide="stateNgoSoetHistory.length > 0">
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
	
	<div id="confirmDeleteModal" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/css/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
					<div class="warnbody">{{warningMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Cancel</button>
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="deleteRecord(index)">Confirm</button>
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
					<button type="button" class="btn" data-dismiss="modal">OK</button>
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
	
	<div class="container">
		<div class="row">
			<div class="height-bottom"></div>
		</div>
	</div>
	
	<jsp:include page="fragments/footer.jsp" />
	
<script type="text/javascript"
		src="resources/js/angularController/stateNgoSoEHistoryController.js"></script>
	<script type="text/javascript">
		var app = angular.module("missionMilletApp", []);
		var myAppConstructor = angular.module("missionMilletApp");
		myAppConstructor.controller("stateNgoSoEHistoryController",
				stateNgoSoEHistoryController);
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directives.js"></script>

	<script>
		$(document).ready(function() {
			$('[data-toggle="tooltip"]').tooltip();
		});
	</script>

</body>

</html>