/*
 * @author Swarna(swarnaprava@sdrc.co.in) 
 */
function aggregationController($scope, $http, $log, $window) {
	
	/***** drop down list for periodicity types *******/
	
	
	
	$("#monthDetails").hide();
	$("#quarterDetails").hide();
	$("#yearDetails").hide();
	
	$scope.selectPeriodicity = function(month) {
		
		$scope.selectedPeriodicity = month;
		
		if(month.key == "35")
			{
				$("#monthDetails").show();
				$("#quarterDetails").hide();
				$("#yearDetails").hide();
				
			}
		else if(month.key == "36")
			{
				$("#monthDetails").hide();
				$("#quarterDetails").show();
				$("#yearDetails").hide();
			}
		else if(month.key == "37")
			{	
				$("#monthDetails").hide();
				$("#quarterDetails").hide();
				$("#yearDetails").show();
			}
		else
			{
				$scope.validationMsg = "No Record Found !";
				$("#errorMessage").modal("show");
			}
	};
	
	$http.get('getTypeOfAggregation').then(function(result) {
		$scope.years = Object.keys(result.data);
		$scope.reportDataDetails = result.data;
	}, function(error) {
		$log.error(error);
	});

	$scope.modelSet = function(monthId,periodicityId)
	{
		$scope.modelforMonth = monthId;
		$scope.modelforPeriodicity = periodicityId;
	};
	
	$scope.selectYear = function(year) {
		$scope.selectedYear = year;
		if($scope.selectedYear == year){
			$http.get('getPeriodicity').then(function(result) {
			$scope.typeinPeriodicity = result.data;
	
			}, function(error) {
				$log.error(error);
			});
		}else{
			$scope.typeinPeriodicity = null;
		}
		$scope.selectedPeriodicity = undefined;		
		$scope.monthlyData = $scope.reportDataDetails[year].monthly;
		$scope.quaterlyData = $scope.reportDataDetails[year].quarterly;
		$scope.yearlyData = $scope.reportDataDetails[year].yearly;
	};
	/***** aggregation for selected periodicity *******/

	$scope.aggregatePeriodicity = function(month) {
		$http.post('getPendingList?monthId='+$scope.modelforMonth+"&periodicity="+$scope.modelforPeriodicity)
		.then(function successCallback(response){
			$scope.pendingList = response.data;
			if($scope.pendingList == undefined || $scope.pendingList == "")	{
				$http.post('aggregate?monthId='+$scope.modelforMonth+"&periodicity="+$scope.modelforPeriodicity)
					.then(function successCallback(response){
					var result = response.data;
					if (result=="success") {
						$scope.msg = "Aggregation Successful.";
						$("#pop").modal("show");
					} else if(result=="fail") {
						if(month.description==1){
							$scope.validationMsg = "No data found for the month of "+month.shortNmae+".";
							$("#errorMessage").modal("show");
						}else if(month.description==3){
							$scope.validationMsg = "No data found for this quarter "+month.shortNmae+", "+month.groupName+".";
							$("#errorMessage").modal("show");
						}else if(month.description==12){
							$scope.validationMsg = "No data found for this financial year "+month.groupName+".";
							$("#errorMessage").modal("show");
						}
					}
				});
			}else{
				$scope.warningMsg = "The files for the following users are in ' Pending Approval/Rejected ' status. Please Review.";
				$("#confirmRejectModal").modal("show");
			}
		}, function(error) {
			$log.error(error);
			if(error.status===401){
				$window.location.href = "sessionExpire";
			}
		});
		
	};
	
}
