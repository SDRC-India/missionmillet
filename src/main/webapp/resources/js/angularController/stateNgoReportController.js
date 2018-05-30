
// Author Swarna(swarnaprava@sdrc.co.in)


function stateNgoReportController($scope, $http, $log, $filter,$window){
	
	$scope.yearList = [];
	
	
$http.get('stateUserNgoDistList').then(function(result) {
		
		$scope.years= Object.keys(result.data);
		$scope.stateNgoReportDetails= result.data;
		$scope.statereportAll=[];
		$scope.statereportDetails=[];
		for(var i=0;i<$scope.years.length;i++)
			{
			for(j=0;j<$scope.stateNgoReportDetails[$scope.years[i]].length;j++)
				$scope.statereportDetails.push($scope.stateNgoReportDetails[$scope.years[i]][j]);
			   
			}
		 $scope.statereportAll = $scope.statereportDetails;
		 
		 for(var i=0; i<$scope.years.length; i++){
				if($scope.years)
					$scope.yearList.push($scope.years[0]);
		 $scope.yearName = $scope.yearList[0];
		 $scope.statereportDetails=  $filter('filter')($scope.statereportAll, {
				financialYear : $scope.yearName
			}, true);
		 }
	}, function(error) {
		$log.error(error);
	});


$scope.sessionOut = function(){
	$window.location.href = "login";
};

//-------------------Report Download Starts
$scope.downloadReport = function(reportId,typeId,reportTypeId,reportngoId) {
	serverURL = "stateNgoDistReportAndCertificateDownload?reportId=" + reportId
			+ "&typeId=" + typeId+"&reportTypeId=" +reportTypeId+ "&ngoId=" +reportngoId;
	$http.post(serverURL).then(function(result) {
				var fileName = {
						"fileName" : result.data
					};
					$.download("downloadSheet", fileName, 'POST');														
	}, function(error) {
		if(error.status===401){
			$window.location.href = "sessionExpire";
		}
		$log.error(error);
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
	
	

	
	$scope.selectYear = function(year)
	{
		$scope.searchText = undefined;
		$scope.searchText = "";
		$scope.statereportDetails=  $filter('filter')($scope.statereportAll, {
			financialYear : year
		}, true);
		$scope.yearName = year;
		 
	}
	
	
	
	// remarks info in modal
	$scope.readMore = function(remarksInfo)
	{
		$scope.remarksDetails = remarksInfo;
		$scope.remarksInfo = $scope.remarksDetails;
			$("#remarksDetailsModal").modal("show");
		
	};
	
	
	// delete data 
	
	$scope.deleteRowdata = function(reportId)
	{
		$scope.reportidModel = reportId;
		$scope.warningMsg = "Are you sure you want to delete this report ?";
		$("#confirmDeleteModal").modal("show");
		
	};	
	
	
	
	 $scope.deleteRecord = function(){
		  $http({
		   method: 'post',
		   url: 'stateNgoDistReportAndCertificateDelete?reportId='+$scope.reportidModel,
		  }).then(function successCallback(response) {
			  
			  if(response.data == true) {
			  $scope.msg = "Your report has been deleted.";
				$('#pop').modal("show");
			}
			  else
				  {
				  $scope.validationMsg = "Some Error Occured !";
					$('#errorMessage').modal("show");
					$window.location.reload();
				  }
		  },function(error){
			  if(error.status===401){
					$scope.validationMsg = "Your session has been expired. Please login again to proceed.";
					$('#errorSessionMessage').modal("show");
				}
			  
		  }); 
		 
		 }
	 
	 
	 // end delete data
	
	
}