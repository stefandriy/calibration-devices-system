angular
    .module('employeeModule')
    .controller('ArchivalDetailsModalController', ['$scope', '$modalInstance', '$log', 'response', '$rootScope', 'VerificationServiceProvider',
        function ($scope, $modalInstance, $log, response, $rootScope, verificationServiceProvider) {

	    	/**
	         * Closes modal window on browser's back/forward button click.
	         */ 
	    	$rootScope.$on('$locationChangeStart', function() {
			    $modalInstance.close();
			});
    	
	    	$scope.verificationData = response.data;

	    	$scope.close = function () {
	    		$modalInstance.close();
          };


      }]);
