angular
    .module('welcomeModule')
    .controller('FeedbackController', ['$scope', '$modalInstance', '$log', 
        function ($scope, $modalInstance, $log) {
    	$scope.formData={};
    	
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
        
        $scope.sendingStarted = false;
        
        $scope.submit = function () {
        	
            $scope.$broadcast('show-errors-check-validity');
            if ($scope.mailSendingForm.$valid){
            	$scope.sendingStarted = false;
                $modalInstance.close($scope.formData, $scope.sendingStarted);

            }
        }
        }]);
