/*
 * @author Swarna(swarnaprava@sdrc.co.in) 
 */
function ngoSoEController($scope, $http, $log, $filter, $window) {

	$scope.yearList = [];
	$scope.yearListHistory = [];
	$scope.validationMsg = "";

	/** *** table data for statement of expenditure ****** */

	$http
			.get('getUploadTableDetails')
			.then(
					function(result) {

						$scope.years = Object.keys(result.data);
						$scope.ngoUploadDetails = result.data;
						$scope.uploadDetailsAll = [];
						$scope.uploadDetails = [];
						for (var i = 0; i < $scope.years.length; i++) {
							for (var j = 0; j < $scope.ngoUploadDetails[$scope.years[i]].length; j++)
								$scope.uploadDetails
										.push($scope.ngoUploadDetails[$scope.years[i]][j]);
							$scope.soeMonth = $scope.uploadDetails[i].minMonth;

						}
						$scope.uploadDetailsAll = $scope.uploadDetails;
						for (var i = 0; i < $scope.years.length; i++) {
							if ($scope.years)
								$scope.yearList.push($scope.years[0]);
						$scope.yearName = $scope.yearList[0];
						$scope.uploadDetails = $filter('filter')($scope.uploadDetailsAll, {
							financialYear : $scope.yearName
						}, true);
						}
						$scope.submitDetails = [];
						$scope.haltDetails = [];
						for (var x = 0; x < $scope.uploadDetails.length; x++) {
							$scope.submitDetails
									.push($scope.uploadDetails[x].submitted);
							$scope.haltDetails
									.push($scope.uploadDetails[x].onHalt);
						}

					}, function(error) {
						$log.error(error);
					});

	$scope.selectYear = function(year) {

		$scope.searchText = undefined;
		$scope.searchText = "";
		$scope.yearName = year;
		$scope.uploadDetails=  $filter('filter')($scope.uploadDetailsAll, {
			financialYear : year
		}, true);
		// $scope.selectedYear=year;
	};

	$scope.getSoEReportDetails = function(element) {
		$scope.val = undefined;
		$scope.regex =undefined;
		$scope.uploadedReport = element.files;
		if ($scope.uploadedReport.length) {
			$scope.val = $("#file-upload").val().toLowerCase();
			$scope.regex = new RegExp("(.*?)\.(xls)$");
			if ($scope.regex.test($scope.val)) {
				if($scope.uploadedReport[0].size > 5000000)
        		{
	        		$scope.uploadedReport = undefined;
	        		angular.element("input[type='file']").val(null);
	        		$('#uploadsizeexceed').modal("show");
        		}else
    				$scope.submitReport();
			}else{
				$('#validationExcelError1').modal("show");
			}
		}
	};
	$scope.showPreviousUploadModal = function(detailsOnhalt,minMonth) {
		if (detailsOnhalt == true) {
			$("#haltDetailsModal").modal('show');
		} else {
			$scope.minMonthDetails = minMonth;
			$("#uploadPreviousMonthModal").modal('show');
		}
	};

	$scope.modelSet = function(monthId, yearId, previousYearSubmitted) {
		$scope.modelData = {
			"month" : monthId,
			"year" : yearId
		};
		$scope.previousYearSubmitted = previousYearSubmitted;
		if (previousYearSubmitted) {

			angular.element("input[type='file']").val(null);
		}
	};

	// -------------------Report Download Starts
	$scope.downloadReport = function(monthId, yearId) {
		serverURL = "downloadSOEreport?month=" + monthId + "&year=" + yearId;
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

	$scope.downloadUploadedReport = function(monthId, yearId) {
		serverURL = "downloadLatestSOE?month=" + monthId + "&year=" + yearId;
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

	$scope.downloadCertificate = function(reportSoeId, typeSoeId) {
		serverURL = "downloadSOEData?reportId=" + reportSoeId + "&typeId="
				+ typeSoeId;
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
		if (url && data) {
			data = typeof data == 'string' ? data : jQuery.param(data);
			var inputs = '';
			jQuery.each(data.split('&'), function() {
				var pair = this.split('=');
				inputs += '<input type="hidden" name="' + pair[0] + '" value="'
						+ pair[1] + '" />';
			});
			jQuery(
					'<form action="' + url + '" method="' + (method || 'post')
							+ '">' + inputs + '</form>').appendTo('body')
					.submit().remove();
		}
		;
		this.export_excel = function() {
			alert("excel exported");
		};
	};
	// -------------------Ends
	// remarks info in modal
	$scope.readMore = function(remarksInfo) {
		$scope.remarksDetails = remarksInfo;
		$scope.remarksInfo = $scope.remarksDetails;
		$("#remarksDetailsModal").modal("show");

	};

	$scope.submitReport = function() {

		var file = [];
		if ($scope.uploadedReport != undefined) {
			file.push($scope.uploadedReport[0]);
		}

		$http({
			url : 'uploadSoe',
			method : 'POST',
			headers : {
				'Content-Type' : undefined
			},
			transformRequest : function(data) {
				var formData = new FormData();

				formData.append('model', new Blob([ (data.model) ], {
					type : "application/json"
				}));

				for (var i = 0; i < data.file.length; i++) {
					formData.append("file", data.file[i]);
				}
				return formData;
			},
			data : {
				model : JSON.stringify($scope.modelData),
				file : file
			}
		})
				.then(
						function successCallback(response) {
							if (response.data == "success") {
								$scope.msg = "The file has been uploaded successfully.";
								$("#pop").modal("show");
							} else if (response.data == "Uploaded excel sheet doesn't match with the template") {
								$scope.validationMsg = "Uploaded excel sheet doesn't match with the template.";
								$('#errorMessage').modal("show");
							} else if (response.data == "The Report is Rejected") {
								$scope.validationMsg = "The Report is Rejected";
								$('#errorMessage').modal("show");
							}else if (response.data == "macro not enable") {
								$scope.validationMsg = "Please click on enable content button of excel sheet and upload again.";
								$("#errorMessage").modal("show");
							}else if(response.data == "The total budget can't be less than the budget define in April."){
								$scope.validationMsg = "The total budget can't be less than the budget define in April.";
								$("#errorMessage").modal("show");
							}else {
								$scope.validationMsg = "Please upload correct file.";
								$('#errorMessage').modal("show");
							}
						},
						function(error) {
							if(error.status===401){
								$window.location.href = "sessionExpire";
							}else{
								if($scope.regex != $scope.val){
									$scope.validationMsg = "Please upload file in Excel format(.xls only).";
									$("#errorMessage").modal("show");
								}else{
									angular.element("input[type='file']").val(null);
									$scope.validationMsg = error.data.message;
									$('#errorMessage').modal("show");
								}
							}
						});
	};

	/** *** history ****** */

	$http
			.get('viewSoeDetails')
			.then(
					function(result) {

						$scope.yearsHistory = Object.keys(result.data);
						$scope.ngoSoeHistorydetails = result.data;
						$scope.soeHistoryAll = [];
						$scope.soeHistory = [];
						for (var i = 0; i < $scope.yearsHistory.length; i++) {
							for (var j = 0; j < $scope.ngoSoeHistorydetails[$scope.yearsHistory[i]].length; j++)
								$scope.soeHistory
										.push($scope.ngoSoeHistorydetails[$scope.yearsHistory[i]][j]);

						}
						$scope.soeHistoryAll = $scope.soeHistory;

						for (var i = 0; i < $scope.yearsHistory.length; i++) {
							if ($scope.yearsHistory)
								$scope.yearListHistory
										.push($scope.yearsHistory[0]);
						$scope.yearNameforHistory = $scope.yearListHistory[0];
						$scope.soeHistory = $filter('filter')($scope.soeHistoryAll, {
							financialYear : $scope.yearNameforHistory
						}, true);
						}
					}, function(error) {
						$log.error(error);
					});

	$scope.selectYearofHistory = function(year) {
		$scope.searchHistory = undefined;
		$scope.searchHistory = "";
		$scope.soeHistory=  $filter('filter')($scope.soeHistoryAll, {
			financialYear : year
		}, true);
		$scope.yearNameforHistory = year;
	};
}
