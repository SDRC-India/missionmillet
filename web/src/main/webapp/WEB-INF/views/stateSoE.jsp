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
<title>Mission Millet - Statement of Expenditure</title>
<jsp:include page="fragments/headtags.jsp" />
</head>


<body ng-controller="stateSoEController" ng-cloak>
<jsp:include page="fragments/header.jsp" />
	<div class="container margin-heading">
		<div class="col-md-12">
			<h1 class="report-heading">Statement of Expenditure</h1>
		</div>
	</div>
	<div class="container"></div>
	<div class="container">
		<div class="col-md-12">
			<h3 class="upload-heading">Upload Statement of Expenditure for <b style="color:#31859d;">{{monthInfo}}, {{financialYearInfo}}</b></h3>
		</div>
	</div>
	<div class="container">
		<div class="col-md-12 col-sm-12 col-xs-12 padding-zero">

			<div class="form-group col-md-6 col-sm-12 col-xs-12 padding-zero">
			
<!-- 				<div class="form-group col-md-12 col-sm-6 col-xs-12 padding-zero"> -->
<!-- 					<label class="form-group col-md-2 labelFont sectorleft">Step -->
<!-- 						1:</label> -->
<!-- 					<div class="form-group col-md-8"> -->
<!-- 						<label class="btn btn-default upload-file" -->
<!-- 							for="upload-file-selector"> -->
<!-- 							<button type="button" -->
<!-- 								class="btn btn-default btn-sm download-template" -->
<!-- 								ng-click="downloadReport()">DOWNLOAD TEMPLATE</button> -->
<!-- 						</label> -->
<!-- 					</div> -->
<!-- 				</div> -->
				
				<div class="form-group col-md-12 col-sm-6 col-xs-12 padding-zero">
					<label class="form-group col-md-2 labelFont sectorleft">Step
						1:</label>
					<div class="form-group col-md-8 col-xs-12 col-sm-6 tooltip">
						<label class="btn btn-default upload-file"
							for="upload-file-selector">
							  <input ng-click="downloadReport()"
							id="upload-file-selector" type="button" /> 
							<span class="glyphicon glyphicon-download-alt upload-report"></span> TEMPLATE
						</label>
						<span class="tooltiptext">Download Template</span>
					</div>
				</div>


				<div class="form-group col-md-12 col-sm-6 col-xs-12 padding-zero">
					<label class="form-group col-md-2 labelFont sectorleft">Step
						2:</label>
					<div class="form-group col-md-8 col-xs-12 col-sm-6 tooltip">
						<label class="btn btn-default upload-file"
							for="upload-file-selector-utilization">
							  <input ng-model="selecteduploadReport" accept=".xls"
							name="uploadReport" ngf-max-size="5MB" ng-click="resetInputFiles(selecteduploadReport)"
							onchange="angular.element(this).scope().getReportDetails(this)"
							id="upload-file-selector-utilization" type="file" />
							 <span class="glyphicon glyphicon-open upload-icon upload-report"></span> SoE </label>
						<span class="tooltiptext">Upload SoE </span>
						<p class="suucessmsg" id="msg"></p>
						<p class="error-msg" id="msgError"></p>
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


	<div class="container">
		<div class="col-md-12">
			<h3 class="report-heading">History</h3>
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
		<div class="col-md-12 padd_lock_table" ng-show="reportSoeHistory.length > 0">
		<div 
				class="tableMargin table-responsive fix-level-table header-fixed-table"
				style="width: 100%; max-height: 450px; overflow: auto;"
				sdrc-table-header-fix tableuniqueclass="'fix-level-table'"
				tabledata="reportSoeHistory">
			<table id="report-history" class="table table-striped"
				cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>SL. NO.</th>
						<th>MONTH</th>
						<th>YEAR</th>
						<th>UPLOADED ON</th>
						<th>DOWNLOAD</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="td in reportSoeHistory | filter:searchText">
						<td>{{$index+1}}</td>
						<td>{{td.month}}</td>
						<td>{{td.year}}</td>
						<td>{{td.createdDate}}</td>
						<td title="Download"><button type="button" class="btn btn-default btn-sm" ng-click="downloadHistory(td.reportsFileId)">
								<span class="glyphicon glyphicon-download-alt"></span>
							</button></td>
					</tr>
				</tbody>
			</table>
			</div>
			<div class="col-md-12" id="no-data-search" ng-show="(reportSoeHistory | filter:searchText).length == 0">
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
		
		<div class="col-md-12" id="no_data" ng-hide="reportSoeHistory.length > 0">
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
	
	<div id="errorMessage" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; ERROR</div>
					<div class="errorbody">{{validationMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="resetFile()">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="errorMessage-micro" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
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
	
	
	<div class="container">
		<div class="row">
			<div class="height-bottom"></div>
		</div>
	</div>
	
	<jsp:include page="fragments/footer.jsp" />
	
	<script type="text/javascript"
		src="resources/js/angularController/stateSoEController.js"></script>
	<script type="text/javascript">
		var app = angular.module("missionMilletApp", []);
		var myAppConstructor = angular.module("missionMilletApp");
		myAppConstructor.controller("stateSoEController",
				stateSoEController);
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directives.js"></script>
		

</body>

</html>