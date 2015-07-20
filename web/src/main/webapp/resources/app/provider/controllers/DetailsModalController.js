angular
    .module('providerModule')
    .controller('DetailsModalController', ['$scope', '$modalInstance', '$log', 'response', '$rootScope', 'VerificationService',
        function ($scope, $modalInstance, $log, response, $rootScope, verificationService) {

            $scope.verificationData = response.data;
          
	        $scope.acceptVerification = function () {
	        	 var dataToSend = {
							verificationId: $rootScope.verificationID,
							status: 'ACCEPTED'
						};
	        	verificationService.acceptVerification(dataToSend).success(function () {
	        		$rootScope.$broadcast('refresh-table');
	        		 $modalInstance.close();
	        	});
	        };
            
            $scope.rejectVerification = function () {
            	$rootScope.$broadcast("verification_rejected", { verifID: $rootScope.verificationID });
            	 $modalInstance.close();
            };
            
            $scope.close = function () {
                $modalInstance.close();
            };
        }]);
