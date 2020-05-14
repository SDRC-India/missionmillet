/*
 * @author Swarna(swarnaprava@sdrc.co.in) 
 */
function stateNgoSoEHistoryController($scope, $http, $log, $filter, $window) {
	
	
	$scope.yearList = [];

	/***** drop down list for statement of expenditure history *******/

	$http.get('getStateNgoSoEHistory').then(function(result) {
		
	$scope.years = Object.keys(result.data);
	
	$scope.stateNgoSoetHistorydetails = result.data;
	$scope.stateNgoSoetHistoryAll = [];
	$scope.stateNgoSoetHistory = [];
	for (var i = 0; i < $scope.years.length; i++) {
		for (j = 0; j < $scope.stateNgoSoetHistorydetails[$scope.years[i]].length; j++)
			$scope.stateNgoSoetHistory.push($scope.stateNgoSoetHistorydetails[$scope.years[i]][j]);

	}
	$scope.stateNgoSoetHistoryAll = $scope.stateNgoSoetHistory;
	
	for(var i=0; i<$scope.years.length; i++){
		if($scope.years)
			$scope.yearList.push($scope.years[0]);
		$scope.yearName = $scope.yearList[0];
		$scope.stateNgoSoetHistory=  $filter('filter')($scope.stateNgoSoetHistoryAll, {
			financialyear : $scope.yearName
		}, true);
	}
	

}, function(error) {
	$log.error(error);
});

	
	
	$scope.selectYear = function(year) {
		$scope.searchText = undefined;
		$scope.searchText = "";
		$scope.stateNgoSoetHistory=  $filter('filter')($scope.stateNgoSoetHistoryAll, {
			financialyear : year
		}, true);
		$scope.yearName = year;
//		$scope.selectedYear=year;
	};

	/***** history *******/
	
	
	
	
	
	//-------------------Report Download Starts
	$scope.downloadHistory = function(historyNgoreportId,historyNgoId,historyNgoname) {	
		serverURL = "downLoadDistrictNgoHistoryFile?reportId=" + historyNgoreportId + "&ngoId=" +historyNgoId+ "&ngoName="+ historyNgoname;
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
	
	
	$scope.readMore = function(remarksInfo)
	{
		$scope.remarksDetails = remarksInfo;
		$scope.remarksInfo = $scope.remarksDetails;
			$("#remarksDetailsModal").modal("show");
		
	};

}
