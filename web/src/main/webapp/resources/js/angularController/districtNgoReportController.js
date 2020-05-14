/*
 * @author Sasmita
 * 
 */

function ngoReportController($scope, $http, $log, $filter, $window) {

	/***** Filter the data based on year *******/
	$scope.selectYear = function(year) {
		$scope.selectedYear = year;
		$scope.reportdata = $filter('filter')($scope.reportDataAll, {
			financialyear : year 
			}, true);
		$scope.searchText = '';
	};
	
	//-------------------Report Download Starts
	$scope.downloadReport = function(ngoId,ngoName,reportId) {	
		serverURL = "downLoadDistrictNgoReport?ngoId=" +ngoId +'&ngoName='+ ngoName+'&reportFileId='+reportId;
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
	
	
	// remarks info in modal
	$scope.readMore = function(remarksInfo)
	{
		$scope.remarksDetails = remarksInfo;
		$scope.remarksInfo = $scope.remarksDetails;
		$("#remarksDetailsModal").modal("show");
		
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

	/***** history of uploaded report for report types *******/

	$http.get('getDistrictNgoReport').then(function(result) {
		$scope.reportDataDetails = result.data;
		$scope.reportKeys = Object.keys(result.data);
		$scope.selectedYear = $scope.reportKeys[0];
		$scope.reportdata = [];
		
		for (var i = 0; i < $scope.reportKeys.length; i++) {
		 for (var j = 0; j < $scope.reportDataDetails[$scope.reportKeys[i]].length; j++)
			$scope.reportdata.push($scope.reportDataDetails[$scope.reportKeys[i]][j]);
		}
		$scope.reportDataAll = $scope.reportdata;
			
		$scope.reportdata=  $filter('filter')($scope.reportDataAll, {
				financialyear : $scope.selectedYear
		}, true);
	}, function(error) {
		$log.error(error);
	});
}
