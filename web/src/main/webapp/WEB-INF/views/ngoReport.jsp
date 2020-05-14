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
<title>Mission Millet - NGO Reports</title>
<jsp:include page="fragments/headtags.jsp" />
</head>


<body ng-controller="ngoReportController" ng-cloak>
<jsp:include page="fragments/header.jsp" />
	<div class="container margin-heading">
		<div class="col-md-12">
			<h1 class="report-heading">Reports</h1>
		</div>
	</div>
	<div ng-show="reportYears.length > 0">
	<div class="container"></div>
	<div class="container">
		
	<div class="col-md-12 padding-zero">
			<div class="col-md-6 padding-zero">
			
			
			<div class="form-group col-md-12 padding-zero">
						<label class="form-group col-md-7 padding-right">Upload Report for the Financial Year :<span class="mandatory_star">&#42;</span></label>
						<div class="form-group col-md-5 select-container  text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" id="report-type"
									class="form-control not-visible-input" name="report-type"
									readonly="" ng-model="reportyearName" placeholder="Select Year">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<span class="glyphicon glyphicon-menu-down"></span>
									</button> 
									<ul class="dropdown-menu reportYear-dropdown" role="menu">
										<li ng-repeat="year in reportYears" title="{{year}}"
											ng-click="selectYearReport(year)"><a href="">{{year}}</a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
			
				<div class="form-group col-md-12 padding-zero">
					<label class="form-group col-md-4 labelFont sectorleft">Select Report Type :<span class="mandatory_star">&#42;</span></label>
					<div class="form-group col-md-8 select-container  text-center tooltipBox">
						<div class="input-group" style="margin: auto;">
							<input type="text" id="report-type" placeholder="Select Report Type"
								class="form-control not-visible-input" name="report-type"
								readonly="" ng-model="selectedType.typeDetailName">
							<div class="input-group-btn" style="position: relative;">
								<button data-toggle="dropdown"
									class="btn btn-color dropdown-toggle" type="button">
									<span class="glyphicon glyphicon-menu-down"></span>
								</button>
								<ul class="dropdown-menu type-dropsowns" role="menu">
									<li ng-repeat="report in reports"
										title="{{report.typeDetailName}}" ng-click="selectReport(report)"><a
										href="">{{report.typeDetailName}}</a></li>
								</ul>
							</div>
						</div>
						<span class="tooltiptext" ng-hide="selectedType.typeDetailName == undefined">{{selectedType.typeDetailName}}</span>
					</div>
				</div>

				<div class="form-group col-md-12 padding-zero">
				
					<div class="col-md-12 padding-zero">
						<label class="form-group col-md-4 labelFont sectorleft">Month
							:<span class="mandatory_star">&#42;</span></label>
						<div class="form-group col-md-8 select-container  text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" id="report-type" placeholder="Select Month"
									class="form-control not-visible-input" name="report-type"
									readonly="" ng-model="monthName">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<span class="glyphicon glyphicon-menu-down"></span>
									</button>
									<ul class="dropdown-menu type-dropsowns" role="menu">
										<li ng-repeat="month in months" title="{{month.description}}"
											ng-click="selectMonth(month)"><a href="">{{month.description}}</a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				
<!-- 				<div class="form-group col-md-12 col-xs-12 col-sm-6 tooltip"> -->
					
<!-- 				<div class="form-group"> -->
<!-- 						 <label class="btn btn-default upload-file" for="upload-file-selector"> -->
<!-- 							 <input id="upload-file-selector" type="file" ng-model="selecteduploadReport" name="uploadReport" -->
<!-- 									onchange="angular.element(this).scope().getReportDetails(this)" accept=".pdf" >  -->
<!-- 								<span class="glyphicon glyphicon-open upload-icon upload-report"></span> REPORT -->
<!-- 						</label> -->
<!-- 						<span class="tooltiptext">Upload Report</span> -->
<!-- 						<p class="suucessmsg" id="msg"></p> -->
<!-- 						<p class="error-msg" id="msgError"></p> -->
<!-- 					</div> -->
<!-- 				</div> -->
				
					<div class="form-group col-md-12 padding-zero">
					<label class="form-group col-md-4 col-sm-12 col-xs-12 labelFont sectorleft">Upload File :<span class="mandatory_star">&#42;</span></label>
					<div class="form-group col-md-8 col-sm-12 col-xs-12 select-container  text-center tooltipBox">
						 <label class="col-sm-12 col-xs-12 btn btn-default upload-file" for="upload-file-selector">
							 <input id="upload-file-selector" type="file" ng-model="selecteduploadReport" name="uploadReport"
									onchange="angular.element(this).scope().getReportDetails(this)" accept=".pdf"> 
								<span class="glyphicon glyphicon-open upload-icon upload-report"></span> REPORT
						</label>
						<span class="tooltiptext">Upload Report</span>
						<p class="suucessmsg" id="msg"></p>
						<p class="error-msg" id="msgError"></p>
					</div>
				</div>
				
				
				
				
				
				
				
				
				
				
				
				
			</div>

			<div class="col-md-6">
				<div class="select-container ">
					<div class="leave-remark-area">
						<h4 class="leave-remark-heading">Leave a remark</h4>
					</div>
					<textarea type="text" id="remark" maxlength="250" placeholder="Ex : Training 1 _ Date"
						class="form-control not-visible-input remarks-box" name="remark"
						ng-model="remarks"> </textarea>
				</div>
				<div class="float-right"><b>{{remarksLimit-remarks.length}}</b> 
					 characters left</div>
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
	</div>
		
		<div class="col-md-12" id="no_data" ng-hide="reportYears.length > 0">
			<div class="container">
				<div class="col-md-12">
					<section class="profile-section col-md-12 col-sm-12 col-xs-12">
						<div class="col-md-12 profile-entry">
							<div id="profileTable" class="col-md-12 accordion-content expanded">
								<div class="content">
									<h2 class="col-md-12 no_records">Please wait until SoE template is generated.</h2>
								</div>
							</div>
						</div>
					</section>
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
		<div class="col-md-12 table-responsive" ng-show="reportHistory.length > 0">
		<div
				class="tableMargin table-responsive fix-level-table header-fixed-table"
				style="width: 100%; max-height: 450px; overflow: auto;"
				sdrc-table-header-fix tableuniqueclass="'fix-level-table'"
				tabledata="reportHistory">
			<table id="report-history" class="table table-striped"
				cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>SL. NO.</th>
						<th>REPORT TYPE</th>
						<th>MONTH</th>
						<th>YEAR</th>
						<th>UPLOADED ON</th>
						<th>REPORT</th>
						<th>REMARKS</th>
					</tr>
				</thead>
				
				<tbody>
					
					<tr ng-repeat="historyDetails in reportHistory | filter:searchText">
						<td>{{$index+1}}</td>
						<td style="text-align:left;">{{historyDetails.reportType}}</td>
						<td>{{historyDetails.month}}</td>
						<td>{{historyDetails.year}}</td>
						<td>{{historyDetails.createdDate}}</td>
						<td title="Download Report"><button type="button" class="btn btn-default btn-sm" ng-click="downloadReport(historyDetails.reportsId
						,historyDetails.downloadUploadedFile,historyDetails.reportTypeId)" ng-disabled="historyDetails.downloadUploadedFile == null">
								<span class="glyphicon glyphicon-download-alt"></span>
							</button></td>
						<td><input tabindex="-1" type='text' readonly
							class='report-Input' ng-model='historyDetails.remarks'>
							<button type="button" class="btn btn-default btn-sm"
								ng-click="readMore(historyDetails.remarks)"
								ng-show="historyDetails.remarks.length > 20">Read More..</button></td>
					</tr>
				</tbody>
			</table>
			</div>
			<div class="col-md-12" id="no-data-search" ng-show="(reportHistory | filter:searchText).length == 0">
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
		<div class="col-md-12" id="no_data" ng-hide="reportHistory.length > 0">
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
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="monthDetails()">Yes</button>
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="resetYear()">No</button>
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
	
	<div id="errorMessageReset" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; ERROR</div>
					<div class="errorbody padding-errorbody">{{validationMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal" onClick="window.location.reload();">Close</button>
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
		src="resources/js/angularController/ngoReportController.js"></script>
	<script type="text/javascript">
		var app = angular.module("missionMilletApp", []);
		var myAppConstructor = angular.module("missionMilletApp");
		myAppConstructor.controller("ngoReportController",
				ngoReportController);
	</script>
	
	<script type="text/javascript"
		src="resources/js/angularDirective/directives.js"></script>

</body>

</html>