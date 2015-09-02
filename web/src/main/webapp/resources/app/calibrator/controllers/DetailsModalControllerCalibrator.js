angular
    .module('employeeModule')
    .controller('DetailsModalControllerCalibrator', ['$scope', '$modalInstance', '$log', 'response',
        function ($scope, $modalInstance, $log, response) {
    		
	    	/**
	         * Closes modal window on browser's back/forward button click.
	         */ 
	    	$scope.$on('$locationChangeStart', function() {
			    $modalInstance.close();
			});
    	
            $scope.verificationData = response.data;

            $log.info($scope.verificationData);

            $scope.close = function () {
                $modalInstance.close();
            };
        }]);
