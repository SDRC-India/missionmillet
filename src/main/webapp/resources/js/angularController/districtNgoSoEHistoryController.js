/*
 * @author sasmita
 */
function districtNgoSoEHistoryController($scope, $http, $log, $filter, $window) {
	
	//$("#loader-mask").show();

	$scope.selectYear = function(year) {
		$scope.selectedYear = year;
		$scope.reportdata = $filter('filter')($scope.reportDataAll, {
			financialyear : year 
			}, true);
		$scope.searchHistory = '';
	};
	
	/***** history *******/

	$http.get('getDistrictNgoHistory').then(function(result) { 
		$scope.reportDataDetails = result.data;
		$scope.reportKeys = Object.keys(result.data);
		$scope.selectedYear = $scope.reportKeys[0];
		$scope.reportdata = [];
		
		for (var i = 0; i < $scope.reportKeys.length; i++) {
		 for (var j = 0; j < $scope.reportDataDetails[$scope.reportKeys[i]].length; j++)
			$scope.reportdata.push($scope.reportDataDetails[$scope.reportKeys[i]][j]);
		}
		$scope.reportDataAll = $scope.reportdata;
		
		//for(var i=0; i<$scope.reportKeys.length; i++){
			$scope.reportdata=  $filter('filter')($scope.reportDataAll, {
				financialyear : $scope.selectedYear
			}, true);
		//}
	}, function(error) {
		$log.error(error);
	});
	
	
	//-------------------Report Download for SoE template
	$scope.downloadHistoryReport = function(reportId,ngoId,ngoName) {	
		serverURL = "downLoadDistrictNgoHistoryFile?reportId=" + reportId +'&ngoId='+ ngoId+'&ngoName='+ ngoName;
				
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
	
	$scope.readMore = function(remarksInfo)
	{
		$scope.remarksDetails = remarksInfo;
		$scope.remarksInfo = $scope.remarksDetails;
		$("#remarksDetailsModal").modal("show");
	};
}
