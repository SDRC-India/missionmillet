
var  app = angular
.module('passwordChangeApp', []);


		app.controller(
				'passwordChangeController',
				function($scope, $http, $filter, $timeout, $window) {
					$scope.activeMenu = "changePwd";
					$scope.user = {};
					$scope.user.userName = document.getElementById("username").value;
					$scope.minCharactersDetails = false;
					$scope.oldPass_drpdwn = true;
					
					/** Get user lists under a login user **/
					$http.get('getUsers').then(function(result) {
						$scope.userListData= result.data;
						$scope.userListLength = $scope.userListData.length;
						console.log($scope.userListLength);
						$scope.userListDetails = $filter('filter')($scope.userListData, {
							groupName : -1
						}, false);
						$scope.selectUserList($scope.user.userName,-1);
					}, function(error) {
						$log.error(error);
					});
					
					$scope.selectUserList=function(name, status){
						$scope.selectedUser = name;
						if(status == -1)
						$scope.oldPass_drpdwn = true;
						else
						$scope.oldPass_drpdwn = false;
					};
					
					$scope.removeErr=function(){
						if($scope.user.newPassword != undefined || $scope.user.newPassword != "")
							document.getElementById('newPasserror').innerHTML  = "";
					};
					
					$scope.changeUserPassword = function() {

						 if(($scope.selectedUser == $scope.user.userName) && ($scope.user.oldPassword == undefined
								|| $scope.user.oldPassword == "")){
								document.getElementById('oldPasserror').innerHTML  = "Please enter old password."; 
						} else if ($scope.user.newPassword == undefined
								|| $scope.user.newPassword == "") {
							document.getElementById('newPasserror').innerHTML  = "Please enter new password.";
						}
						else if ($scope.user.newPassword != $scope.user.confirmPassword) {
							document.getElementById('confirmPasserror').innerHTML  = "Password didn't match with new password.";
						}else {
							document.getElementById('oldPasserror').innerHTML  = "";
							document.getElementById('newPasserror').innerHTML  = "";
							document.getElementById('confirmPasserror').innerHTML  = "";
							$scope.userDetails={
									'userName' : $scope.selectedUser,
									'oldPassword' : $scope.user.oldPassword,
									'newPassword' : $scope.user.newPassword,
									'confirmPassword': $scope.user.confirmPassword
							};
							$http.post('changePassword', $scope.userDetails)
									.then(
											function successCallback(response) {
												var result = response;
												if(result.data.statusCode == 200){
													$scope.msg = result.data.message;
													$('#pop').modal('show');
												}else{
													$scope.validationMsg = result.data.message;
													$('#errorMessage').modal('show');
													$scope.user = undefined;
												}

											}, function(error) {
												console.log(error);
												if(error.status===401){
													$window.location.href = "sessionExpire";
												}
											});
						}

					};
					
					
					$scope.userNameError = function()
					{
						if($scope.user.userName != undefined || $scope.user.userName != "")
						document.getElementById('userNameError').innerHTML  = "";
					};
					$scope.oldPassError = function()
					{
						if($scope.user.oldPassword != undefined || $scope.user.oldPassword != "")
						document.getElementById('oldPasserror').innerHTML  = "";
					};
					$scope.newPassError = function()
					{
						if($scope.user.newPassword != undefined || $scope.user.newPassword != "")
						document.getElementById('newPasserror').innerHTML  = "";
					};
					$scope.confirmPassError = function()
					{
						if($scope.user.newPassword == $scope.user.confirmPassword)
						document.getElementById('confirmPasserror').innerHTML  = "";
					};
					
					$scope.function1 = function() {
						$(".minChar").show();
						$("#span_error").hide();
					};
				});




app.directive('disallowSpaces', function() {
	  return {
	    restrict: 'A',

	    link: function($scope, $element) {
	      $element.bind('input', function() {
	        $(this).val($(this).val().replace(/ /g, ''));
	      });
	    }
	  };
	});

/******************** code for change password****************************/
