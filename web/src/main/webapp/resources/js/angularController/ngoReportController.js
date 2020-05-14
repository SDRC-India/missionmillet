/*
 * @author Swarna(swarnaprava@sdrc.co.in) 
 * 
 */

function ngoReportController($scope, $http, $log, $filter, $window) {


	$scope.yearListHistory = [];
	
	/***** STATEMENT OF EXPENDITURE Upload details*******/
	
	$scope.remarksLimit = 250;

	
$http.get('dropdownValues').then(function(result) {
		
	$scope.reportYears = Object.keys(result.data);
	$scope.reportTypeDetails = result.data;
	$scope.reportDetails = [];
	$scope.reports = [];
	$scope.months = [];
	
	for (var i = 0; i < $scope.reportYears.length; i++) {
		for (var j = 0; j < $scope.reportTypeDetails[$scope.reportYears[i]].length; j++)
			$scope.reportDetails.push($scope.reportTypeDetails[$scope.reportYears[i]][j]);
			$scope.reports = $scope.reportDetails[i].typeDetailsModel;
			$scope.months = $scope.reportDetails[i].timePeriodList;
	}
	
	$scope.reports = '';
	$scope.months = '';
	$scope.reportyearName = $scope.reportYears[0];
	$scope.selectYearReport($scope.reportyearName);
	
}, function(error) {
	$log.error(error);
});
	
	
	
	
	$scope.selectReport = function(report) {
		$scope.selectedType = report;
		if ($scope.months == null) {
			$scope.validationMsg = "Please upload the SoE file before uploading report.";
			$('#errorMessage').modal("show");
		}
	};

	$scope.selectMonth = function(month) {
		
		$scope.monthName=month.description;
		$scope.monthKey = month.key;
	};
	
	
$scope.selectYearReport = function(year) {
	$scope.reportyearName = year;
	if(year != $scope.reportYears[0])
	{
		$scope.warningMsg = "You have selected a previous financial year . Do you want to continue ?";
		$('#confirmRejectModal').modal("show");
		$scope.selectedType = undefined;
		$scope.monthName = undefined;		
		for (var j = 0; j < $scope.reportTypeDetails[year].length; j++) {
			$scope.reportDetails.push($scope.reportTypeDetails[year][j]);
			$scope.reports = $scope.reportDetails[j].typeDetailsModel;
			$scope.months = $scope.reportTypeDetails[year][0].timePeriodList;
		}
	}
	else {
		$scope.selectedType = undefined;
		$scope.monthName = undefined;

		for (var j = 0; j < $scope.reportTypeDetails[year].length; j++) {
			$scope.reportDetails.push($scope.reportTypeDetails[year][j]);
			$scope.reports = $scope.reportDetails[j].typeDetailsModel;
			$scope.months = $scope.reportTypeDetails[year][0].timePeriodList;
		}
	}
		//$scope.reportyearName = year;
	};
	
	
	$scope.resetYear = function()
	{
		$scope.reportyearName = "";
		$scope.reportyearName = undefined;
		$scope.monthName = "";
		$scope.monthName = undefined;
		$scope.months = "";
		$scope.reports = "";
	};
	
	
	$scope.getReportDetails = function(element) {     
		   $scope.uploadedReport = element.files;  
		   if($scope.uploadedReport.length)
		   {
			   var val = $("#upload-file-selector").val().toLowerCase();
		        var regex = new RegExp("(.*?)\.(pdf)$");
		        if((regex.test(val))) {
		        	if($scope.uploadedReport[0].size > 5000000)
		        		{
		        		$scope.uploadedReport = undefined;
				   		document.getElementById("msgError").innerHTML = "Uploaded file can not be more than 5MB.";
				   		document.getElementById("msg").innerHTML = "";
		        		}
		        	
		        	else{
		     	   $scope.uploadedReport = element.files;  
		        	document.getElementById("msg").innerHTML = "File attached Successfully !";
		        	document.getElementById("msgError").innerHTML = "";
		            event.preventDefault();
		        	}
		        }
		        else{
		        	$scope.uploadedReport = undefined;
			   		document.getElementById("msgError").innerHTML = "Please upload files in pdf Format";
			   		document.getElementById("msg").innerHTML = "";
		        }
			   }
		   else
			   {
			   		$scope.uploadedReport = undefined;
			   		document.getElementById("msg").innerHTML = " ";
			   		document.getElementById("msgError").innerHTML = "";
			   }
		};
	
	
	
	$scope.submitReport = function() {
		
		if($scope.reportyearName == undefined)
			{
			$scope.validationMsg = "Please Select Financial Year";
			$('#errorMessage').modal("show");
			}

		else if ($scope.selectedType == undefined) {
			$scope.validationMsg = "Please Select Report Type";
			$('#errorMessage').modal("show");
			
		} else if ($scope.monthName == undefined) {
			$scope.validationMsg = "Please Select Month";
			$('#errorMessage').modal("show");
			
		}  else if ($scope.uploadedReport == undefined) {
			$scope.validationMsg = "Please Upload Report";
			$('#errorMessage').modal("show");
			document.getElementById("msgError").innerHTML = "";
		}
		else {
			var finalObj = {};
			
			finalObj.reportType = $scope.selectedType.typeDetailId;
			finalObj.month = $scope.monthKey;
			
			if($scope.remarks == "" || $scope.remarks == undefined)
				{
				finalObj.remarks = "--";
				}
			else{
			finalObj.remarks = $scope.remarks;
			}
			
			var file=[];
			if($scope.uploadedReport != undefined)
				{
					file.push($scope.uploadedReport[0]);
				}
			
			

			$http(
					{
						url : 'uploadNgoReport',
						method : 'POST',
						headers : {
							'Content-Type' : undefined
						},
						transformRequest : function(data) {
							var formData = new FormData();

							formData.append('model', new Blob(
									[ (data.model) ], {
										type : "application/json"
									}));

							for(var i=0;i<data.file.length;i++)
								{
							formData.append("file", data.file[i]);
								}
							return formData;
						},
						data : {
							model : JSON.stringify(finalObj),
							file : file
						}
					}).then(function successCallback(response){
						if (response.data == "Successfull") {
						$scope.msg = "The file has been uploaded successfully.";
							$("#pop").modal("show");
						}
						else if(response.data == "Upload failed!")
							{
							$scope.validationMsg = "Upload failed !";
							$('#errorMessageReset').modal("show");
							document.getElementById("msg").innerHTML = "";
							$scope.uploadedReport = undefined;
							$scope.uploadedReport = "";
							document.getElementById("msg1").innerHTML = "";
							$scope.selectedType = "";
							$scope.selectedType = undefined;
							$scope.monthName = "";
							$scope.monthName = undefined;
							
							}
						else if(response.data == "No data available in NGO Soe Report for this month.")
						{
						$scope.validationMsg = "No data available in NGO Soe Report for this month.";
						$('#errorMessageReset').modal("show");
						}
						}, function(error) {
							if(error.status===401){
								$window.location.href = "sessionExpire";
							}else{
								$scope.validationMsg = "Some error occurred";
								$('#errorMessage').modal("show");
							}
						});
			}
		};
	
	
	
	//-------------------Report Download Starts
	$scope.downloadReport = function(reportId,typrId,reportTypeId) {	
		
		serverURL = "downloadReportData?reportId=" + reportId
				+ "&typeId=" + typrId+"&reportTypeId="+reportTypeId;
		$http.post(serverURL).then(function(result) {
					var fileName = {
							"fileName" : result.data
						};
						$.download("downloadSheet", fileName, 'POST');														
		}, function(error) {
			$log.error(error);
			if(error.status===401){
				$window.location.href = "sessionExpire";
			}
		});
		
	};
	
	$scope.downloadCertificate = function(reportId,typrId) {	
		serverURL = "downloadCertificateData?reportId=" + reportId
				+ "&typeId=" + typrId;
		$http.post(serverURL).then(function(result) {
					var fileName = {
							"fileName" : result.data
						};
						$.download("downloadSheet", fileName, 'POST');														
		}, function(error) {
			$log.error(error);
			if(error.status===401){
				$window.location.href = "sessionExpire";
			}
		});
	};
	
	$.download = function(url, data, method) {
		// url and data options required
		if (url && data) {
			// data can be string of parameters or array/object
			data = typeof data == 'string' ? data : jQuery.param(data);
			// split params into form inputs
			var inputs = '';
			jQuery.each(data.split('&'), function() {
				var pair = this.split('=');
				inputs += '<input type="hidden" name="' + pair[0]
						+ '" value="' + pair[1] + '" />';
			});
			// send request
			jQuery(
					'<form action="' + url + '" method="'
							+ (method || 'post') + '">' + inputs
							+ '</form>').appendTo('body').submit()
					.remove();
		}
		;
		this.export_excel = function() {
			alert("excel exported");
		};
	};
	//-------------------Ends
	/***** history details of uploaded report  *******/

	$http.get('viewNgoReportData').then(function(result) {
		
		$scope.years = Object.keys(result.data);
		$scope.reportdataDetails = result.data;
		$scope.reportDataAll = [];
		$scope.reportHistory = [];
		for (var i = 0; i < $scope.years.length; i++) {
			for (var j = 0; j < $scope.reportdataDetails[$scope.years[i]].length; j++)
				$scope.reportHistory.push($scope.reportdataDetails[$scope.years[i]][j]);

		}
		$scope.reportDataAll = $scope.reportHistory;
		
		for(var i=0; i<$scope.years.length; i++){
			if($scope.years)
				$scope.yearListHistory.push($scope.years[0]);
		$scope.yearName = $scope.yearListHistory[0];
		$scope.reportHistory=  $filter('filter')($scope.reportDataAll, {
			financialYear : $scope.yearName
		}, true);
		}
	}, function(error) {
		$log.error(error);
	});
		
	
	$scope.selectYear = function(year)
	{
		$scope.searchText = undefined;
		$scope.searchText = "";
		$scope.reportHistory=  $filter('filter')($scope.reportDataAll, {
			financialYear : year
		}, true);
		$scope.yearName = year;
	};
	
	
	$scope.readMore = function(remarksInfo)
	{
		$scope.remarksDetails = remarksInfo;
		$scope.remarksInfo = $scope.remarksDetails;
			$("#remarksDetailsModal").modal("show");
		
	};
	
}
