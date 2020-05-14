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
<title>Mission Millet - NGO Statement of Expenditure</title>
<jsp:include page="fragments/headtags.jsp" />
</head>


<body ng-controller="ngoSoEController" ng-cloak>
<jsp:include page="fragments/header.jsp" />
	<div class="container margin-heading">
		<div class="col-md-12">
			<h1 class="report-heading">Statement of Expenditure</h1>
		</div>
	</div>
	<div class="container"></div>
	<div class="container">
		<div class="col-md-12">
			<h3 class="upload-heading">Upload</h3>
		</div>
	</div>
	<div class="container"></div>
	
	<div class="container">
	<div class="col-md-12 padding-zero report-table">
			<div class="col-md-8 padding-zero">
				<div class="form-group col-md-12 padding-zero">
					<div class="form-group col-md-6 padding-zero">
						<label class="form-group col-md-5 padding-right">Financial Year :</label>
						<div class="form-group col-md-7 select-container  text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" id="report-type" placeholder="Select Year"
									class="form-control not-visible-input" name="report-type"
									readonly="" ng-model="yearName">
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
			</div>


			<div class="col-md-4 padding-zero">
				<div class="form-group col-md-12">
					<div class="form-group col-md-8 padding-zero search-bar">
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
		<div class="col-md-12 table-responsive" ng-show="uploadDetails.length > 0">
		
			<table id="report-history" class="table table-striped"
				cellspacing="0" width="100%" >
				<thead>
					<tr>
						<th>SL. NO.</th>
						<th>MONTH</th>
						<th>YEAR</th>
						<th>DOWNLOAD TEMPLATE</th>
						<th>UPLOAD SoE</th>
						<th>UPLOADED ON</th>
						<th>DOWNLOAD SoE</th>
						<th>DEADLINE</th>
					</tr>
				</thead>
				
				<tbody>
					
					<tr ng-repeat="details in uploadDetails | filter:searchText">
						<td>{{$index+1}}</td>
						<td>{{details.month}}</td>
						<td>{{details.year}}</td>
						<td title="Download report"><button type="button" class="btn btn-default btn-sm" ng-click="downloadReport(details.monthValue,details.year)"
						ng-disabled="details.submitted ==  false">
								<span class="glyphicon glyphicon-download-alt"></span>
							</button></td>
						<td title="Upload SoE Report">
						<label ng-if="!details.submitted" ng-click="showPreviousUploadModal(details.onHalt,details.minMonth)"><span class="glyphicon glyphicon-open upload-icon"></span></label>
						<label for="file-upload"  ng-if="details.submitted" ng-click="modelSet(details.monthValue,details.year,details.submitted);!details.submitted? showPreviousUploadModal(details.onHalt):''; ">
								<span class="glyphicon glyphicon-open upload-icon"></span>
						</label> <input id="{{details.submitted ? 'file-upload' : 'fileCannot'}}" type="file" ng-if="details.submitted"
						 ng-model="selecteduploadSoEReport" name="uploadReport" file-model
									onchange="angular.element(this).scope().getSoEReportDetails(this)"
									 accept=".xls" /></td>
					   
						<td><input tabindex="-1"
											type='text' readonly class='report-Input'
											ng-model='details.lastUploadedDate'></td>
						<td title="Download report"><button type="button"
								class="btn btn-default btn-sm" ng-disabled="details.submitted ==  false || details.lastUploadedDate == null"
								ng-click="downloadUploadedReport(details.monthValue,details.year)">
								<span class="glyphicon glyphicon-download-alt"></span>
							</button></td>
						<td><input tabindex="-1"
											type='text' readonly class='report-Input'
											ng-model='details.deadlineDate'></td>
					</tr>
					
				</tbody>
			</table>
			
			<div class="col-md-12" id="no-data-search" ng-show="(uploadDetails | filter:searchText).length == 0">
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
		
		<div class="col-md-12" id="no_data" ng-hide="uploadDetails.length > 0">
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

	
	<div class="container">
		<div class="col-md-12">
			<h1 class="report-heading">History</h1>
		</div>
	</div>


<div class="container">
	<div class="col-md-12 padding-zero report-table">
			<div class="col-md-8 padding-zero">
				<div class="form-group col-md-12 padding-zero">
					<div class="form-group col-md-6 padding-zero">
						<label class="form-group col-md-5 padding-right">Financial Year :</label>
						<div class="form-group col-md-7 select-container  text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" id="report-type" placeholder="Select Year"
									class="form-control not-visible-input" name="report-type"
									readonly="" ng-model="yearNameforHistory">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<span class="glyphicon glyphicon-menu-down"></span>
									</button>
									<ul class="dropdown-menu year-dropdown" role="menu">
										<li ng-repeat="year in yearsHistory" title="{{year}}"
											ng-click="selectYearofHistory(year)"><a href="">{{year}}</a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>


			<div class="col-md-4 padding-zero">
				<div class="form-group col-md-12">
					<div class="form-group col-md-8 padding-zero search-bar">
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
		<div class="col-md-12 table-responsive" ng-show="soeHistory.length > 0">
		<div
				class="tableMargin table-responsive fix-level-table header-fixed-table"
				style="width: 100%; max-height: 450px; overflow: auto;"
				sdrc-table-header-fix tableuniqueclass="'fix-level-table'"
				tabledata="soeHistory">
			<table id="report-history" class="table table-striped table-responsive" cellspacing="0">
				<thead>
					<tr>
						<th>SL. NO.</th>
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
					
					<tr ng-repeat="history in soeHistory | filter:searchHistory">
						<td>{{$index+1}}</td>
						<td>{{history.month}}</td>
						<td>{{history.year}}</td>
						<td>{{history.createdDate}}</td>
						<td title="Download report"><button type="button" class="btn btn-default btn-sm" ng-disabled="history.ngoSoEReportsId == null" 
						data-toggle="tooltip" title="Download" data-placement="top" ng-click="downloadCertificate(history.ngoSoEReportsId,
						history.typeDetailId)">
								<span class="glyphicon glyphicon-download-alt"></span>
							</button></td>
						<td>
						<p ng-class="{statusInfoApproval:history.status == 'Approved', statusInfoRejection:history.status == 'Rejected'}">{{history.status}}  {{history.actionTakenDate? "(" + history.actionTakenDate+")":""}}</p></td>
						<td>{{history.actionTakenUser}}</td>
						<td><input tabindex="-1"
											type='text' readonly class='report-Input'
											ng-model='history.remarks'>
											<button type="button" class="btn btn-default btn-sm"
								ng-click="readMore(history.remarks)"
								ng-show="history.remarks.length > 20">Read More..</button></td>
											</td>
					</tr>
					
				</tbody>
			</table>
			</div>
			<div class="col-md-12" id="no-data-search" ng-show="(soeHistory | filter:searchHistory).length == 0">
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
		<div class="col-md-12" id="no_data" ng-hide="soeHistory.length > 0">
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
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
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
					<button type="button" class="btn" data-dismiss="modal" onClick="window.location.reload();">OK</button>
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
	
	<div id="validationExcelError" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; ERROR</div>
					<div class="errorbody">Please upload file in excel Format.</div>
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
	<div id="uploadPreviousMonthModal" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
					<div class="warnbody"><p class="remarks-SelectedType">Please wait until the SoE for the Month of {{minMonthDetails}} is Approved.</p></div>
					<button type="button" class="btn errorOk" data-dismiss="modal" data-dismiss="modal">OK</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="haltDetailsModal" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
					<div class="warnbody"><p class="remarks-SelectedType">Please wait until the SoE is revised by the admin.</p></div>
					<button type="button" class="btn errorOk" data-dismiss="modal" data-dismiss="modal">OK</button>
				</div>
			</div>
		</div>
	</div>
	<div id="uploadsizeexceed" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
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
	<div class="container">
		<div class="row">
			<div class="height-bottom"></div>
		</div>
	</div>
	<jsp:include page="fragments/footer.jsp" />
	
<script type="text/javascript"
		src="resources/js/angularController/ngoSoEController.js"></script>
	<script type="text/javascript">
		var app = angular.module("missionMilletApp", []);
		var myAppConstructor = angular.module("missionMilletApp");
		myAppConstructor.controller("ngoSoEController",
				ngoSoEController);
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