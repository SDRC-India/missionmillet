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
<title>Mission Millet - Statement of Expenditure - Manage</title>
<jsp:include page="fragments/headtags.jsp" />
</head>


<body ng-controller="stateNgoSoEManageController" ng-cloak>
<jsp:include page="fragments/header.jsp" />
	<div class="container margin-heading">
		<div class="col-md-12">
			<h1 class="report-heading">Statement of Expenditure - Manage</h1>
		</div>
	</div>
	
	<div class="container">
		<div class="col-md-12">
			<h3 class="upload-heading">Pending Actions</h3>
		</div>
	</div>
	<div class="container"></div>
	
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
								<input type="text" class="form-control searchbar-box" ng-model="searchText"
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
		<div class="col-md-12 table-responsive padd_lock_table" ng-show="stateNgoSoeManage.length > 0">
		<div
				class="tableMargin table-responsive fix-level-table header-fixed-table"
				style="width: 100%; max-height: 450px; overflow: auto;"
				sdrc-table-header-fix tableuniqueclass="'fix-level-table'"
				tabledata="stateNgoSoeManage">
			<table id="report-history" class="table table-striped"
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
						<th>ACTION</th>
					</tr>
				</thead>
				
				<tbody>
					
					<tr ng-repeat="details in stateNgoSoeManage | filter:searchText">
						<td>{{$index+1}}</td>
						<td>{{details.districtName}}</td>
						<td>{{details.ngoName}}</td>
						<td>{{details.month}}</td>
						<td>{{details.year}}</td>
						<td>{{details.date | date: 'dd-MM-yyyy'}}</td>
						<td title="Download Report"><button type="button" class="btn btn-default btn-sm" ng-click="downloadReport(details.ngoId
						,details.monthInt,details.year,details.ngoName)"
						data-toggle="tooltip" title="Download Template">
								<span class="glyphicon glyphicon-download-alt"></span>
							</button></td>
						<td><button type="button" class="btn btn-success" ng-click="approve(details.ngoId
						,details.monthInt,details.year)">APPROVE</button>
							<button type="button" class="btn btn-danger" ng-click="reject(details.ngoId
						,details.monthInt,details.year)">REJECT</button></td>
						
					</tr>
					
				</tbody>
			</table>
			</div>
			<div class="col-md-12" id="no-data-search" ng-show="(stateNgoSoeManage | filter:searchText).length == 0">
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
		
		<div class="col-md-12" id="no_data" ng-hide="stateNgoSoeManage.length > 0">
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
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_success_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
					<div class="warnbody">{{warningMsg}}</div>
					<div>
						<div class="col-md-12">
							<div class="select-container ">
								<div class="leave-remark-area">
									<h4 class="leave-remark-heading textLeft">Leave a remark</h4>
								</div>
								<textarea type="text" id="remark" maxlength="250" ng-change="inputRemarksforApproval(remarksforApproval)"
									class="form-control not-visible-input remarks-box"
									name="remark" ng-model="remarksforApproval"> </textarea>
								<p></p>
							</div>
							
							<div class="float-right"><b>{{remarksLimit-remarksforApproval.length}}</b> 
					 characters left</div>
						</div>
						<p id="errorMsg" class="textLeft"></p>
					</div>
					<button type="button" class="btn errorOk" ng-click="approveModal()" >Confirm</button>
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="resetBox(remarksforApproval)">Cancel</button>
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
					
					<div>
						<div class="col-md-12">
							<div class="select-container ">
								<div class="leave-remark-area">
									<h4 class="leave-remark-heading textLeft">Leave a remark</h4>
								</div>
								<textarea type="text" id="remark" maxlength="250" ng-change="inputRemarksforReject(remarksforReject)"
									class="form-control not-visible-input remarks-box"
									name="remark" ng-model="remarksforReject"> </textarea>
								<p></p>
							</div>
							<div class="float-right"><b>{{remarksLimit-remarksforReject.length}}</b> 
					 characters left</div>
						</div>
						<p id="errorMsgforReject" class="textLeft"></p>
					</div>
					<button type="button" class="btn errorOk" ng-click="rejectModal()">Confirm</button>
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="resetBox(remarksforReject)">Cancel</button>
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
	
	<div class="container">
		<div class="row">
			<div class="height-bottom"></div>
		</div>
	</div>
	
	<jsp:include page="fragments/footer.jsp" />
	
<script type="text/javascript"
		src="resources/js/angularController/stateNgoSoEManageController.js"></script>
	<script type="text/javascript">
		var app = angular.module("missionMilletApp", []);
		var myAppConstructor = angular.module("missionMilletApp");
		myAppConstructor.controller("stateNgoSoEManageController",
				stateNgoSoEManageController);
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