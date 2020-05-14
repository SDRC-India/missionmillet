function loginController($scope, $http, $log, $window, $filter) {
	$scope.forgotPassClick = function(){
		$('#forgotPassModal').modal('show');
	};
	$scope.checkValidation = function(){
		if($scope.forgotUserId == undefined || $scope.forgotUserId.trim() == ''){
			document.getElementById('userName').innerHTML = "Please enter user name";
		}else if($scope.regEmail == undefined || $scope.regEmail.trim() == ''){
			document.getElementById('emailErr').innerHTML = "Please enter email id";
		}
		else{
			$scope.sendData($scope.regEmail,$scope.forgotUserId);
		}
	};
	$scope.removeErr=function(){
		if($scope.forgotUserId != undefined || $scope.forgotUserId.trim() != ''){
			document.getElementById('userName').innerHTML = "";
			document.getElementById('validErr').innerHTML = "";
			
			if($scope.regEmail != undefined || $scope.regEmail.trim() != ''){
				document.getElementById('emailErr').innerHTML = "";
				document.getElementById('validErr').innerHTML = "";
			}
		}
	};
	$scope.clear = function(){
		$scope.forgotUserId = "";
		$scope.regEmail = "";
		document.getElementById('userName').innerHTML = "";
		document.getElementById('emailErr').innerHTML = "";
		document.getElementById('validErr').innerHTML = "";
	};
	$scope.sendData = function(regEmail,forgotUserId){
		$scope.userDetails = {
				'email': regEmail,
				'userName': forgotUserId
		};
		$http.post('forgotPassword',JSON.stringify($scope.userDetails))
		.then(function(result){
			if(result.data.statusCode == 404){
				document.getElementById('validErr').innerHTML = result.data.message;
				document.getElementById('emailErr').innerHTML = "";
			}else if(result.data.statusCode == 200){
				$('#forgotPassModal').modal('hide');
				$scope.msg = result.data.message;
				document.getElementById('userName').innerHTML = "";
				document.getElementById('emailErr').innerHTML = "";
				document.getElementById('validErr').innerHTML = "";
				$scope.forgotUserId  = undefined;
				$scope.regEmail = undefined;
				//$("#sendButtn").attr("disabled", true);
				$('#pop').modal('show');
			}
		}),function(err){
			$scope.regEmail = undefined;
			$scope.forgotUserId  = undefined;
		};
	};
	
}