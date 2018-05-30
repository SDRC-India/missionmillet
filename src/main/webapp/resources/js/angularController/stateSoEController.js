/*
 * @author Swarna(swarnaprava@sdrc.co.in) 
 * 
 */
	
			
	function stateSoEController($scope, $http, $log, $filter,$window)
			{

	/***** month and financial year *******/
		
		$scope.yearList = [];
		
//		var tableHeight = $("#report-history").height();
//		if(tableHeight < 50)
//			{
//			 $("#tableHeader").css("margin-right", "0px");
//			}
	
$http.get('soeMonthAndFinancialYear').then(function(result) {
		
		$scope.monthandYearInfo = result.data;
		$scope.monthInfo =  $scope.monthandYearInfo.currentMonth;
		$scope.financialYearInfo =  $scope.monthandYearInfo.financialYear;

	}, function(error) {
		$log.error(error);
	});
	
	
	
	$scope.selectYear = function(year) {
		$scope.searchText = undefined;
		$scope.searchText = "";
		$scope.reportSoeHistory=  $filter('filter')($scope.reportSoeHistoryAll, {
			financialYear : year
		}, true);
			$scope.yearName = year;
//		$scope.yearName = year;
	};
	
	$scope.getReportDetails = function(element) { 
		
		   $scope.uploadedReport = element.files;  
		  
		   if($scope.uploadedReport.length)
		   {
			   var val = $("#upload-file-selector-utilization").val().toLowerCase();
		        var regex = new RegExp("(.*?)\.(xls)$");
		        if((regex.test(val))) {
		        	if($scope.uploadedReport[0].size > 5000000)
	        		{
		        		$scope.uploadedReport = undefined;
		        		document.getElementById("msgError").innerHTML = "Uploaded file can not be more than 5MB.";
				   		document.getElementById("msg").innerHTML = "";
				   	   $(".submit-btn").attr("disabled", true);
	        		}else{
	        			 $(".submit-btn").removeAttr("disabled");
		     	   $scope.uploadedReport = element.files;  
		        	document.getElementById("msg").innerHTML = "File attached successfully !";
		        	document.getElementById("msgError").innerHTML = "";
		            event.preventDefault();
	        		}
		        }
		        else{
		        	$scope.uploadedReport = undefined;
			   		document.getElementById("msgError").innerHTML = "Please upload files in Excel format";
			   		document.getElementById("msg").innerHTML = "";
			   		$(".submit-btn").attr("disabled", true);
		        }
			   }
		   else
			   {
			   $scope.uploadedReport = undefined;
		   		document.getElementById("msg").innerHTML = " ";
		   		document.getElementById("msgError").innerHTML = "";
			   }
		};
	
	$scope.resetFile = function()
	{
		$scope.uploadedReport = undefined;
		document.getElementById("msg").innerHTML = " ";
		$window.location.reload();
	};
	$scope.sessionOut = function(){
		$window.location.href = "sessionExpire";
	};
	
	$scope.submitReport = function() {
		
		if ($scope.uploadedReport == undefined) {
			$scope.validationMsg = "Please upload Statement of Expenditure.";
			$('#errorMessage').modal("show");
			document.getElementById("msgError").innerHTML = "";
		}else {
			var file=[];
			if($scope.uploadedReport!=undefined)
				{
					file.push($scope.uploadedReport[0]);
				}
			
			$http(
					{
						url : 'uploadStateSoE',
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
							file : file
						}
					}).then(function successCallback(response){
						
						if(response.status===200){
							$scope.msg = response.data;
							$("#pop").modal("show");
						}
						
						}, function(error) {
							if(error.status===401){
								$scope.sessionOut();
							}else{
								$scope.validationMsg = error.data;
								$('#errorMessage').modal("show");
								$log.error(error);
							};
						});
					}
		};
	
	//-------------------Report Download Starts
	$scope.downloadReport = function() {	
		serverURL = "downloadStateTemplate";
		$http.post(serverURL).then(function(result) {
					var fileName = {
							"fileName" : result.data
						};
						$.download("downloadSheet", fileName, 'POST');														
		}, function(error) {
			$log.error(error);
			$scope.sessionOut();
		});
	};
	
	$scope.downloadHistory = function(reportId) {
		serverURL = "monthlySoEDownload?id=" + reportId;
		$http.post(serverURL).then(function(result) {
					var fileName = {
							"fileName" : result.data
						};
						$.download("downloadSheet", fileName, 'POST');														
		}, function(error) {
			$log.error(error);
			$scope.sessionOut();
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
	/***** history of uploaded report for report types *******/

	$http.get('stateSoEHistory').then(function(result) {
		
		$scope.years= Object.keys(result.data);
		$scope.stateSoEHistorydetails= result.data;
		$scope.reportSoeHistoryAll=[];
		$scope.reportSoeHistory=[];
		for(var i=0;i<$scope.years.length;i++)
			{
			for(var j=0;j<$scope.stateSoEHistorydetails[$scope.years[i]].length;j++)
				$scope.reportSoeHistory.push($scope.stateSoEHistorydetails[$scope.years[i]][j]);
			   
			}
		 $scope.reportSoeHistoryAll = $scope.reportSoeHistory;
		 for(var i=0; i<$scope.years.length; i++){
				if($scope.years)
					$scope.yearList.push($scope.years[0]);
		 $scope.yearName = $scope.yearList[0];
		 $scope.reportSoeHistory=  $filter('filter')($scope.reportSoeHistoryAll, {
				financialYear : $scope.yearName
			}, true);
		 }
			
	}, function(error) {
		$log.error(error);
	});
	
	
}



