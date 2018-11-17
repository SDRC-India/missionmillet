/*
 * @author Sasmita Rani
 */
function districtNgoSoEManageController($scope, $http, $log, $window, $filter) {

	$scope.remarksLimit = 250;

	/** *** drop down list for statement of expenditure types ****** */

	$http.get('retrieveDistrictNgoList').then(function(result) {
		$scope.uploadDetailsofDistrict = result.data;
		for (var i = 0; i < $scope.uploadDetailsofDistrict.length; i++) {
			if ($scope.uploadDetailsofDistrict[i].status == true) {
				$scope.status = "Uploaded";
			} else if ($scope.uploadDetailsofDistrict[i].status == false) {
				$scope.status1 = "Pending";
			}
		}
	}, function(error) {
		$log.error(error);
	});

	$http.get('retrieveNgoListSoEStatus').then(function(result) {
		$scope.pendingAction = result.data;
		$scope.pendingActionKeys = Object.keys(result.data);
		$scope.selectedYear = $scope.pendingActionKeys[0];
		$scope.pendingActionDetails =[];
		
		for (var i = 0; i < $scope.pendingActionKeys.length; i++) {
		 for(var j = 0; j < $scope.pendingAction[$scope.pendingActionKeys[i]].length; j++)
			 $scope.pendingActionDetails.push($scope.pendingAction[$scope.pendingActionKeys[i]][j]);
		}
		$scope.pendingActionAll = $scope.pendingActionDetails;
		
		$scope.pendingActionDetails=  $filter('filter')($scope.pendingActionAll, {
				financialyear : $scope.selectedYear
		}, true);
		
	}, function(error) {
		$log.error(error);
	});

	$scope.selectYear = function(year) {
		$scope.selectedYear = year;
		$scope.pendingActionDetails = $filter('filter')(
				$scope.pendingActionAll, {
					financialyear : year
				}, true);
		$scope.searchActions = '';
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
	        		$('#validationExcelError').modal("show");
        		}else
    				$scope.submitReport();
			}else{
				$('#validationExcelError1').modal("show");
			}
		}
	};

	// -------------------Report Download for SoE template
	$scope.downloadReport = function(ngoId, ngoName) {
		serverURL = "downloadTemplate?ngoId=" + ngoId + '&ngoName=' + ngoName;

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

	// -------------------Report Download on pending actions
	$scope.downloadSoeReport = function(ngoId, monthId, yearId, ngoName) {
		serverURL = "getDistrictNgoSoEReport?ngoId=" + ngoId + '&month='
				+ monthId + '&year=' + yearId + '&ngoName=' + ngoName;

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
				inputs += '<input type="hidden" name="' + pair[0] + '" value="'
						+ pair[1] + '" />';
			});
			// send request
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
	$scope.modelSet = function(ngo_Id,timePeriodId) {
		$scope.modelData = ngo_Id;
		$scope.timeperiodId = timePeriodId;
		angular.element("input[type='file']").val(null);
	};

	$scope.approveReport = function(ngoId, monthInt, year) {
		$scope.warningMsg = "Are you sure you want to approve this file ?";
		$("#confirmApproveModal").modal("show");
		$scope.ngoSoeId = ngoId;
		$scope.ngoSoeMonthId = monthInt;
		$scope.ngoSoeYearId = year;
	};
	$scope.rejectReport = function(ngoid, monthid, yearid) {
		$scope.warningMsg = "Are you sure you want to reject this file ?";
		$("#confirmRejectModal").modal("show");
		$scope.rejectNgoId = ngoid;
		$scope.rejectNgoMonthId = monthid;
		$scope.rejectNgoYearId = yearid;
	};

	/** submit the uploaded file * */
	$scope.submitReport = function() {

		var file = [];
		if ($scope.uploadedReport != undefined) {
			file.push($scope.uploadedReport[0]);
		}
		var formData = new FormData();
		formData.append('ngoId', $scope.modelData);
		formData.append('timePeriodId', $scope.timeperiodId);
		formData.append("uploadedFile", file[0]);

		$http
				.post('uploadDistrictSoE', formData, {
					transformRequest : angular.identity,
					headers : {
						'Content-Type' : undefined
					}
				})
				.then(
						function successCallback(response) {
							// console.log(response.data);
							if (response.data == "success") {
								$scope.uploadSuccessMsg = "Uploaded file has been submitted successfully";
								$("#pop").modal("show");
							} else if (response.data == "Invalid excel file") {
								$scope.validationMsg = "Uploaded file is not same file as provided. ";
								$("#errorMessage").modal("show");
							} else if (response.data == "Please upload correct file.") {
								$scope.validationMsg = "Please upload correct file.";
								$("#errorMessage").modal("show");
							} else if (response.data == "macro not enable") {
								$scope.validationMsg = "Please click on enable content button of excel sheet and upload again.";
								$("#errorMessage").modal("show");
							} else if(response.data == "The total budget can't be less than the budget define in April."){
								$scope.validationMsg = response.data;
								$("#errorMessage").modal("show");
							}else if(response.data == "Uploaded excel sheet doesn't match with the template"){
								$scope.validationMsg = response.data;
								$("#errorMessage").modal("show");
							}
							else {
								$scope.validationMsg = "Please upload file in Excel format(.xls only).";
								$("#errorMessage").modal("show");
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

	$scope.inputRemarksforReject = function(modelValue) {
		$scope.remarksforReject = modelValue;
		if (modelValue != undefined || modelValue != "") {
			document.getElementById("errorMsgforReject").innerHTML = " ";
		}
	};

	$scope.inputRemarksforApproval = function(modelVal) {
		$scope.remarksforApproval = modelVal;
	};

	$scope.approveSoEStatusReport = function() {
		if ($scope.remarksforApproval == undefined || $scope.remarksforApproval == "") {
			$scope.remarksforApproval = "--";
		}
		$http.get(
				'approveNgoSoEStatus?ngoId=' + $scope.ngoSoeId + '&month='
						+ $scope.ngoSoeMonthId + '&year=' + $scope.ngoSoeYearId
						+ "&reMark=" + $scope.remarksforApproval).then(
				function(result) {
					$scope.approveSoeStatusDetails = result.data;
					$("#confirmApproveModal").modal("hide");
					$scope.uploadSuccessMsg = "File approved successfully.";
					$('#pop').modal("show");
				}, function(error) {
					$log.error(error);
					if(error.status===401){
						$window.location.href = "sessionExpire";
					}
				});
	};

	$scope.rejectSoEStatusReport = function() {

		if ($scope.remarksforReject == undefined
				|| $scope.remarksforReject == "") {
			document.getElementById("errorMsgforReject").innerHTML = "Remarks section is mandatory for rejection. ";
		} else {
			$http.get(
					'rejectNgoSoEStatus?ngoId=' + $scope.rejectNgoId
							+ '&month=' + $scope.rejectNgoMonthId + '&year='
							+ $scope.rejectNgoYearId + "&reMark="
							+ $scope.remarksforReject).then(function(result) {
				$scope.rejectSoeStatusDetails = result.data;
				$("#confirmRejectModal").modal("hide");
				$scope.uploadSuccessMsg = "File is rejected";
				$('#pop').modal("show");
			}, function(error) {
				$log.error(error);
				if(error.status===401){
					$window.location.href = "sessionExpire";
				}
			});
		}
	};

	$scope.redirectToURL = function() {
		$window.location.href = "";
	};
	$scope.reviewReport = function(){
		$scope.validationMsg = "Please review previous month SoE";
		$("#errorMessage").modal("show");
	};
}
