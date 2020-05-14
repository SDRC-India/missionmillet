/*
 * @author Swarna(swarnaprava@sdrc.co.in) 
 * 
 */

function stateNgoSoEManageController($scope, $http, $log, $filter, $window) {

	$scope.remarksLimit = 250;
	
	$scope.yearList = [];
	/***** month and financial year *******/
	
$http.get('getStateNgoListSoEStatus').then(function(result) {
	
			$scope.years = Object.keys(result.data);
			$scope.stateNgoSoeManagedetails = result.data;
			$scope.stateNgoSoeManageAll = [];
			$scope.stateNgoSoeManage = [];
			for (var i = 0; i < $scope.years.length; i++) {
				for (j = 0; j < $scope.stateNgoSoeManagedetails[$scope.years[i]].length; j++)
					$scope.stateNgoSoeManage.push($scope.stateNgoSoeManagedetails[$scope.years[i]][j]);

			}
			$scope.stateNgoSoeManageAll = $scope.stateNgoSoeManage;
			
			for(var i=0; i<$scope.years.length; i++){
				if($scope.years)
					$scope.yearList.push($scope.years[0]);
			$scope.yearName = $scope.yearList[0];
			$scope.stateNgoSoetManage=  $filter('filter')($scope.stateNgoSoeManageAll, {
				financialyear : $scope.yearName
			}, true);
			}
	}, function(error) {
		$log.error(error);
	});

	
	$scope.selectYear = function(year) {
		$scope.searchText = undefined;
		$scope.searchText = "";
		$scope.stateNgoSoetManage=  $filter('filter')($scope.stateNgoSoeManageAll, {
			financialyear : year
		}, true);
		$scope.yearName = year;
//		$scope.selectedYear=year;
	};
	
	
	
	//-------------------Report Download Starts
	$scope.downloadReport = function(ngoSoeId,monthSoeId,yearSoeId,ngoname) {
		serverURL = "getDistrictNgoSoEReport?ngoId=" + ngoSoeId
				+ "&month=" + monthSoeId+"&year="+yearSoeId+ "&ngoName=" +ngoname;
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
				inputs += '<input type="hidden" name="' + pair[0]
						+ '" value="' + pair[1] + '" />';
			});
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
	
	
	
	$scope.resetBox = function(remarksValue)
	{
		if (remarksValue != undefined || remarksValue != "") {
			$('.remarks-box').val('');
			document.getElementById("errorMsg").innerHTML = "";
			document.getElementById("errorMsgforReject").innerHTML = "";
			
		}
	};
	
	
	// method for approving 
	
	$scope.approve = function(ngoSoeId,monthSoeId,yearSoeId)
	{
		$scope.warningMsg = "Are you sure you want to approve this file ?";
		$("#confirmDeleteModal").modal("show");
		$scope.statengoSoeid = ngoSoeId;
		$scope.statengoSoemonthid = monthSoeId;
		$scope.statengoSoeyearid = yearSoeId;
	};
	
	$scope.inputRemarksforApproval = function(modelVal)
	{
		$scope.remarksforApproval = modelVal;
	};
	
	$scope.approveModal = function(){
		
	
		if($scope.remarksforApproval == undefined || $scope.remarksforApproval == "")
		{
			$scope.remarksforApproval = "--";
		}
		
		  $http({
		   method: 'get',
		   url: 'approveNgoSoEStatus?ngoId=' + $scope.statengoSoeid
				+ "&month=" + $scope.statengoSoemonthid+"&year="+$scope.statengoSoeyearid+ "&reMark=" +$scope.remarksforApproval,
		  }).then(function successCallback(response) {
					$('#confirmDeleteModal').modal('hide');
					$scope.msg = "File approved successfully.";
					$('#pop').modal("show");
				
		  }, function(error) {
				$log.error(error);
				if(error.status===401){
					$window.location.href = "sessionExpire";
				}
			}); 
		 };
	
	//end method for approving 
	
	// method for rejecting 
	$scope.reject = function(ngoSoeIdReject,monthSoeIdReject,yearSoeIdReject)
	{
		$scope.warningMsg = "Are you sure you want to reject this file ?";
		$("#confirmRejectModal").modal("show");
		$scope.statengoSoeidReject = ngoSoeIdReject;
		$scope.statengoSoemonthidReject = monthSoeIdReject;
		$scope.statengoSoeyearidReject = yearSoeIdReject;
	};
	
	
	$scope.inputRemarksforReject = function(modelValue)
	{
		$scope.remarksforReject = modelValue;
		if(modelValue != undefined || modelValue != "")
		{
			document.getElementById("errorMsgforReject").innerHTML = " ";
		}
	};
	
	
	
	$scope.rejectModal = function(){
		
		if($scope.remarksforReject == undefined || $scope.remarksforReject == "")
			{
			document.getElementById("errorMsgforReject").innerHTML = "Remarks section is mandatory for rejection ";
			}
		
		else{
		  $http({
		   method: 'get',
		   url: 'rejectNgoSoEStatus?ngoId=' + $scope.statengoSoeidReject
				+ "&month=" + $scope.statengoSoemonthidReject+"&year="+$scope.statengoSoeyearidReject+"&reMark=" +$scope.remarksforReject,
		  }).then(function successCallback(response) {
				  $('#confirmRejectModal').modal('hide');
				  $scope.msg = "File is rejected";
			  	  $('#pop').modal("show");
		  }, function(error) {
				$log.error(error);
				if(error.status===401){
					$window.location.href = "sessionExpire";
				}
			}); 
		}
		 };
	
//end method for approving 
}



