/*
 * @author Swarna(swarnaprava@sdrc.co.in) 
 */

function configController ($scope, $http, $log, $window) {
	

	/***** drop down list for configuration type*******/

	$http.get('getTypeOfConfiguration').then(function(result) {
		$scope.description = result.data;
		
	}, function(error) {
		$log.error(error);
	});
	
	/***** value storing for cut of days ******/
	$scope.enteredCutofDate = function(modelData)
	{
		$scope.cutofDate = modelData;
		if($scope.cutofDate > 45)
			{
				$scope.cutofDate = undefined;
				$scope.cutofDate = "";
				$scope.validationMsg = "Cut off days can not be more than 45.";
				$('#errorMessage').modal("show");
			}
	}
	
	
	/***** restricting character ******/

	$scope.filterValue = function($event) {
		if (isNaN(String.fromCharCode($event.charCode))) {
//			$event.preventDefault();
		}
	};
	/***** end ******/

	/***** restricting space ******/
	
	$('input').blur(function() {
		var value = $.trim($(this).val());
		$(this).val(value);
	});
	
	/***** end ******/
	
	$scope.selectConfigtype = function(config) {
//		$scope.configType = config.description;
		$scope.selectedconfigType=config;
		$scope.cutofDate = config.groupName;
	}
	
	
	/**** resetting fields ****/
	
	$scope.resetConfigure = function()
	{
//		$scope.selectedconfigType = undefined;
//		$scope.selectedconfigType = "";
		$scope.cutofDate = undefined;
		$scope.cutofDate = "";
	}
	

	/***** submitting value *******/
	$scope.submitReport = function() {
		if ($scope.selectedconfigType == undefined) {
			$scope.validationMsg = "Please select configuration type.";
			$('#errorMessage').modal("show");
			
		}else if($scope.cutofDate == undefined || $scope.cutofDate == "") {
			$scope.validationMsg = "Please select cut off date.";
			$('#errorMessage').modal("show");
			
		}else if($scope.cutofDate > 45)
			{
				$scope.validationMsg = "Cut off days can not be more than 45.";
				$('#errorMessage').modal("show");
			}
		else {
			
			$http.post('setConfiguration?cutOffDays='+$scope.cutofDate+"&typeDetailsId="+$scope.selectedconfigType.key
					).then(function successCallback(response){
				var result = response.data;
				if (result=="success") {
				$scope.msg = "Configuration changed successfully !";
					$("#pop").modal("show");
				}else if(result == "fromNextMonth")
					{
					$scope.remarksInfo = "This cut of date will be effective from next month.";
					$("#remarksDetailsModal").modal("show");
					}
				else if(result == "alreadyConfigured")
					{
					$scope.remarksInfo = "You can not change your configuration twice in a month.";
					$('#remarksDetailsModal').modal("show");
					}
				else if(result == "failed")
					{
					$scope.validationMsg = "Unable to save the cutoff days.";
					$('#errorMessage').modal("show");
					
					}
			}, function(error) {
				$log.error(error);
				if(error.status===401){
					$window.location.href = "sessionExpire";
				}
			});
		}
	};
}
