
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="missionMilletApp">
<head>
<title>Mission Millet - District Statement of Expenditure</title>
<jsp:include page="fragments/headtags.jsp" />
<style type="text/css">
.stylish-input-group .form-control {
   height: 32px !important;
}
</style>
</head>

<body ng-controller="districtNgoSoEManageController" ng-cloak>
<jsp:include page="fragments/header.jsp" />
	<div class="container margin-heading">
		<div class="col-md-12">
			<h1 class="report-heading">Statement of Expenditure - Manage</h1>
		</div>
	</div>
	<div class="container"></div>
	<div class="container district-margin-heading">
	<div class="col-md-9">
		<div class="col-md-6">
			<h3 class="upload-heading">SoE Template for <b style="color:#31859d;" class="ng-binding">{{uploadDetailsofDistrict[0].timePeriod}}</b> </h3>
		</div>
		
		
			<div class="col-md-6 padding-zero district-margin-heading">
				<div class="form-group col-md-12">
					<div class="form-group col-md-5 padding-zero search-bar">
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
		<div class="col-md-9 table-responsive padd_lock_table" ng-show="(uploadDetailsofDistrict|filter:searchText).length!=0 && uploadDetailsofDistrict.length > 0">
		<div
				class="tableMargin table-responsive dist-level-table header-fixed-table"
				style="width: 100%; max-height: 450px; overflow: auto; border: 1px solid rgb(204, 204, 204);"
				sdrc-table-header-fix tableuniqueclass="'dist-level-table'"
				tabledata="uploadDetailsofDistrict">
			<table id="report-history" class="table table-striped"
				cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>SL. NO.</th>
						<th>NGO</th>
						<th>DOWNLOAD TEMPLATE</th>
						<th>UPLOAD SoE</th>
						<th>STATUS</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="uploadDetails in uploadDetailsofDistrict | filter:searchText | orderBy: 'ngoId'">
						<td>{{$index+1}}</td>
						<td>{{uploadDetails.ngoName}}</td>
						<td ng-if="uploadDetails.ifFalseInOctober==false"><button type="button" class="btn btn-default btn-sm" ng-disabled="uploadDetails.uploadStatus==false" ng-click="downloadReport(uploadDetails.ngoId,uploadDetails.ngoName)">
								<span class="glyphicon glyphicon-download-alt"></span>
							</button></td>
						<td ng-if="uploadDetails.ifFalseInOctober==true"><button type="button" class="btn btn-default btn-sm" ng-click="reviewReport()">
								<span class="glyphicon glyphicon-download-alt"></span>
							</button></td>
						<td><label for="file-upload" class="" ng-click="modelSet(uploadDetails.ngoId, uploadDetails.timePeriodId)">
								<span class="glyphicon glyphicon-open upload-icon" ng-if="uploadDetails.uploadStatus==true" ></span>
							</label> <input id="file-upload" type="file"
						 			 name="uploadReport" 
									onchange="angular.element(this).scope().getSoEReportDetails(this)" accept=".xls" />
									
							<button type="button" class="btn btn-default btn-sm" ng-if="uploadDetails.uploadStatus==false" ng-disabled="uploadDetails.uploadStatus==false">
								<span class="glyphicon glyphicon-open"></span>
							</button>
						</td>
						
						<td><input tabindex="-1"
											type='text' readonly class='report-Input'
											ng-if="uploadDetails.status == true" ng-model='status'>
						<input tabindex="-1"
											type='text' readonly class='report-Input'
											ng-if="uploadDetails.status == false" ng-model='status1'></td>
					</tr>
					
				</tbody>
			</table>
		 </div>	
		</div>
		
	   <div class="col-md-12" id="no_data" ng-show="(uploadDetailsofDistrict|filter:searchText).length==0 && uploadDetailsofDistrict.length > 0">
			<div class="container-fluid">
				<div class="col-md-12">
					<section class="profile-section col-md-12 col-sm-12 col-xs-12">
						<div class="col-md-12 profile-entry">
							<div id="profileTable" class="col-md-12 accordion-content expanded">
								<div class='content'>
									<h2 class="col-md-12 no_records">No Data Found.</h2>
								</div>
							</div>
						</div>
					</section>
				</div>
			</div>
		</div>
		
		<div class="col-md-12" id="no_data" ng-hide="uploadDetailsofDistrict.length > 0">
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
	
	<div class="container district-margin-heading">
		<div class="col-md-12">
			<h1 class="report-heading">Pending  Actions</h1>
		</div>
	</div>

	<div class="container">
	<div class="col-md-12 padding-zero report-table">
			<div class="col-md-6 padding-zero">
				<div class="form-group col-md-12 padding-zero">
					<div class="form-group col-md-8 padding-zero">
						<label class="form-group col-md-5">Financial Year :</label>
						<div class="form-group col-md-7 select-container  text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" id="report-type" placeholder="Select Year"
									class="form-control not-visible-input" name="report-type"
									readonly="" ng-model="selectedYear">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<span class="glyphicon glyphicon-menu-down"></span>
									</button>
									<ul class="dropdown-menu dist-ngodrp" style="min-width: 191px;" role="menu">
										<li ng-repeat="year in pendingActionKeys" title="{{year}}"
											ng-click="selectYear(year)"><a href="">{{year}}</a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="col-md-6 padding-zero">
				<div class="form-group col-md-12">
					<div class="form-group col-md-5 padding-zero search-bar">
						<div id="imaginary_container">
							<div class="input-group stylish-input-group">
								<input type="text" class="form-control searchbar-box" ng-model="searchActions"
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
		<div class="col-md-12 table-responsive padd_lock_table" ng-show="(pendingActionDetails|filter:searchActions).length!=0 && pendingActionDetails.length > 0">
		 <div class="tableMargin table-responsive data-level-table header-fixed-table"
				style="width: 100%; max-height: 450px; overflow: auto; border: 1px solid rgb(204, 204, 204);"
				sdrc-table-header-fix tableuniqueclass="'data-level-table'"
				tabledata="pendingActionDetails">
			<table id="report-history" class="table table-striped"
				cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>SL. NO.</th>
						<th>NGO</th>
						<th>MONTH</th>
						<th>YEAR</th>
						<th>UPLOADED ON</th>
						<th>DOWNLOAD</th>
						<th>ACTION</th>
					</tr>
				</thead>
				
				<tbody>
					<tr ng-repeat="actionDetails in pendingActionDetails | filter:searchActions | orderBy: 'ngoId'">
						<td>{{$index+1}}</td>
						<td>{{actionDetails.ngoName}}</td>
						<td>{{actionDetails.month}}</td>
						<td>{{actionDetails.year}}</td>
						<td>{{actionDetails.date | date: 'dd-MM-yyyy'}}</td>
						<td><button type="button" class="btn btn-default btn-sm" ng-click="downloadSoeReport(actionDetails.ngoId, actionDetails.monthInt, actionDetails.year, actionDetails.ngoName)"
						data-toggle="tooltip" title="Download Template">
								<span class="glyphicon glyphicon-download-alt"></span>
							</button></td>
						<td><button type="button" class="btn btn-success" ng-click="approveReport(actionDetails.ngoId, actionDetails.monthInt, actionDetails.year)">APPROVE</button>
							<button type="button" class="btn btn-danger" ng-click="rejectReport(actionDetails.ngoId, actionDetails.monthInt, actionDetails.year)">REJECT</button></td>
					</tr>
				</tbody>
			</table>
		  </div>	
		</div>
		<div class="col-md-12" id="no_data" ng-show="(pendingActionDetails|filter:searchActions).length==0 && pendingActionDetails.length > 0">
			<div class="container-fluid">
				<div class="col-md-12">
					<section class="profile-section col-md-12 col-sm-12 col-xs-12">
						<div class="col-md-12 profile-entry">
							<div id="profileTable" class="col-md-12 accordion-content expanded">
								<div class='content'>
									<h2 class="col-md-12 no_records">No Data Found.</h2>
								</div>
							</div>
						</div>
					</section>
				</div>
			</div>
		</div>
		<div class="col-md-12" id="no_data" ng-hide="pendingActionDetails.length > 0">
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
	
	<div id="upload" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="successhead"><img alt="" src="resources/images/icons/Messages_success_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; File Uploaded Successfully !</div>
					<div class="successbody">{{msg}}</div>
					<button type="button" class="btn" data-dismiss="modal" ng-click="submitReport()">OK</button>
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
					<div class="successbody">{{uploadSuccessMsg}}</div>
					<button type="button" class="btn" data-dismiss="modal" ng-click='redirectToURL()'>OK</button>
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
	
	<div id="confirmApproveModal" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
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
							
							<div class="float-right"><b>{{remarksLimit-remarksforApproval.length}}</b>characters left</div>
						</div>
						<p id="errorMsg" class="textLeft"></p>
					 </div>
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="approveSoEStatusReport();">Confirm</button>
					<button type="button" class="btn errorOk" data-dismiss="modal">Cancel</button>
				</div>
			</div>
		</div>
	</div>
	<div id="validationExcelError" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; ERROR</div>
					<div class="errorbody">Maximum upload size of 5MB exceeded.</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<div id="validationExcelError1" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; ERROR</div>
					<div class="errorbody">Please upload file in Excel format(.xls only).</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<div id="confirmRejectModal" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
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
					<button type="button" class="btn errorOk" ng-click="rejectSoEStatusReport()">Confirm</button>
					<button type="button" class="btn errorOk" data-dismiss="modal">Cancel</button>
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
		src="resources/js/angularController/districtNgoSoEManageController.js"></script>
	<script type="text/javascript">
		var app = angular.module("missionMilletApp", []);
		var myAppConstructor = angular.module("missionMilletApp");
		myAppConstructor.controller("districtNgoSoEManageController",
				districtNgoSoEManageController);
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