/*
 * @author Swarna(swarnaprava@sdrc.co.in) 
 */
function stateReportController($scope, $http, $log, $filter, $window) {
	
	
	$scope.remarksLimit = 250;
	
	$scope.yearList = [];
	
	$scope.months = [];
	/***** drop down list for report types *******/

	$http.get('dropDownList').then(function(result) {
		
		$scope.reportYears = Object.keys(result.data);
		$scope.reportTypeDetails = result.data;
		$scope.reportDetails = [];
		$scope.reports = [];
		$scope.months = [];
		
		for (var i = 0; i < $scope.reportYears.length; i++) {
			for (var j = 0; j < $scope.reportTypeDetails[$scope.reportYears[i]].length; j++)
				$scope.reportDetails.push($scope.reportTypeDetails[$scope.reportYears[i]][j]);
				$scope.reports = $scope.reportDetails[i].typeDetailsModel;
				$scope.months = $scope.reportDetails[i].monthDetails;
		}
		
		$scope.reports = '';
		$scope.months = '';
		$scope.reportyearName = $scope.reportYears[0];
		$scope.selectYearReport($scope.reportyearName);
		
	}, function(error) {
		$log.error(error);
	});

	$scope.selectReport = function(report) {
		
		if($scope.months == '')
			{
			$scope.validationMsg = "Please upload the SoE file before uploading report.";
			$('#errorMessage').modal("show");
			}
		$scope.reportName = report.typeDetailName;
	};
	
	$scope.selectMonth = function(month) {
		
		$scope.selectedmonthName = month;
	};
	
	$scope.selectYearReport = function(year) {
		
		if(year != $scope.reportYears[0])
			{
				$scope.warningMsg = "You have selected a previous financial year . Do you want to continue ?";
				$('#confirmRejectModal').modal("show");
				$scope.reportName = undefined;
				$scope.selectedmonthName = undefined;
				for (var j = 0; j < $scope.reportTypeDetails[year].length; j++) {
					$scope.reportDetails.push($scope.reportTypeDetails[year][j]);
					$scope.reports = $scope.reportDetails[j].typeDetailsModel;
					$scope.months = $scope.reportTypeDetails[year][0].monthDetails;
				}
			}
 		else {

			$scope.reportName = undefined;
			$scope.selectedmonthName = undefined;

			for (j = 0; j < $scope.reportTypeDetails[year].length; j++) {
				$scope.reportDetails.push($scope.reportTypeDetails[year][j]);
				$scope.reports = $scope.reportDetails[j].typeDetailsModel;
				$scope.months = $scope.reportTypeDetails[year][0].monthDetails;
			}
		}
		$scope.reportyearName = year;
	},

	
	$scope.resetYear = function()
	{
		$scope.reportyearName = "";
		$scope.reportyearName = undefined;
		$scope.selectedmonthName = undefined;
		$scope.selectedmonthName = "";
		$scope.reports = "";
		$scope.months = "";
	};

	$scope.readMore = function(remarksInfo)
	{
		$scope.remarksDetails = remarksInfo;
		$scope.remarksInfo = $scope.remarksDetails;
			$("#remarksDetailsModal").modal("show");
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
		        	document.getElementById("msg").innerHTML = "File attached successfully !";
		        	document.getElementById("msgError").innerHTML = "";
		            event.preventDefault();
		        	}
		        }
		        else{
		        	$scope.uploadedReport = undefined;
			   		document.getElementById("msgError").innerHTML = "Please upload files in pdf format.";
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
	
	
	$scope.selectYearHistory = function(year) {
		$scope.searchText = undefined;
		$scope.searchText = "";
		$scope.reportHistory=  $filter('filter')($scope.reportHistoryAll, {
			financialYear : year
		}, true);
		$scope.yearNameHistory = year;
		$scope.selectedYear=year;
	};

	
	$scope.deleteRowdata = function(reportId)
	{
		$scope.index = reportId;
		$scope.warningMsg = "Are you sure you want to delete this report ?";
		$("#confirmDeleteModal").modal("show");
	};	
	$scope.sessionOut = function(){
		$window.location.href = "login";
	};
	
	 $scope.deleteRecord = function(){
		  $http({
		   method: 'post',
		   url: 'deleteReport?wassanReportId='+$scope.index,
		  }).then(function successCallback(response) {
			  if(response.status===200){
					$scope.msg = response.data;
					$("#pop").modal("show");
				}
				
				}, function(error) {
					if(error.status===400){
						$scope.validationMsg = error.data;
						$('#errorMessage').modal("show");
						
					} else if(error.status===401){
						$scope.validationMsg = "Your session has been expired. Please login again to proceed.";
						$('#errorSessionMessage').modal("show");
					}
					$log.error(error);
				});
		 };

	$scope.submitReport = function() {
		
		if($scope.reportyearName == undefined)
			{
			$scope.validationMsg = "Please select Financial Year";
			$('#errorMessage').modal("show");
			}
		else if ($scope.reportName == undefined) {
			$scope.validationMsg = "Please select report type";
			$('#errorMessage').modal("show");
			
		} 
		else if ($scope.userReportName == undefined) {
			$scope.validationMsg = "Please enter report name";
			$('#errorMessage').modal("show");
			
		} else if ($scope.selectedmonthName == undefined) {
			$scope.validationMsg = "Please select month";
			$('#errorMessage').modal("show");
			
		}  else if ($scope.uploadedReport == undefined) {
			document.getElementById("msgError").innerHTML = "";
			$scope.validationMsg = "Please upload report";
			$('#errorMessage').modal("show");
			
		}
		
		else {
			var finalObj = {};
			
			finalObj.reportType = $scope.reportName;
			finalObj.reportName = $scope.userReportName;
			finalObj.month = $scope.selectedmonthName;
			finalObj.financialYear = $scope.reportyearName;
			
			if($scope.remarks == "" || $scope.remarks == undefined)
				{
				finalObj.remarks = "--";
				}
			else{
			finalObj.remarks = $scope.remarks;
			}
			
			var file=[];
			if($scope.uploadedReport!=undefined)
				{
					file.push($scope.uploadedReport[0]);
				}
		
			$http(
					{
						url : 'uploadStateReport',
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
						
						if(response.status===200){
							$scope.msg = response.data;
							$("#pop").modal("show");
						}
						
						}, function(error) {
							
							if(error.status===400){
								
								$scope.validationMsg = error.data;
								$('#errorMessageReset').modal("show");
								$scope.uploadedReport = undefined;
								document.getElementById("msg").innerHTML = " ";
								$scope.reportName = "";
								$scope.reportName = undefined;
								$scope.selectedmonthName = "";
								$scope.selectedmonthName = undefined;
								document.getElementById("msg1").innerHTML = "";
								
							}
							if(error.status===401){
								$window.location.href = "sessionExpire";
							}
							$log.error(error);
						});
					}		
		};
	
	//-------------------Ends
	
	
	
	//-------------------Report Download Starts
	$scope.downloadReport = function(reportId,typeId) {	
		setTimeout(function () {
		serverURL = "downloadReport?reportId=" + reportId
				+ "&typeId=" + typeId;
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
		}, 2000);
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
	
	

	
	/***** history of uploaded report for report types *******/

	$http.get('StateReportHistory').then(function(result) {
		
				$scope.years = Object.keys(result.data);
						$scope.stateReportHistorydetails = result.data;
						$scope.reportHistoryAll = [];
						$scope.reportHistory = [];
						for (var i = 0; i < $scope.years.length; i++) {
							for (var j = 0; j < $scope.stateReportHistorydetails[$scope.years[i]].length; j++)
								$scope.reportHistory.push($scope.stateReportHistorydetails[$scope.years[i]][j]);
						}
						$scope.reportHistoryAll = $scope.reportHistory;
						
						for(var i=0; i<$scope.years.length; i++){
							if($scope.years)
								$scope.yearList.push($scope.years[0]);
					 $scope.yearNameHistory = $scope.yearList[0];
					 $scope.reportHistory=  $filter('filter')($scope.reportHistoryAll, {
							financialYear : $scope.yearNameHistory
						}, true);
						}
					 
					}, function(error) {
						$log.error(error);
					});
	
	
}
