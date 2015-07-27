angular
    .module('employeeModule')
    .controller('ArchivalDetailsModalController', ['$scope', '$modalInstance', '$log', 'response', '$rootScope', 'VerificationServiceProvider',
        function ($scope, $modalInstance, $log, response, $rootScope, verificationServiceProvider) {

    	  $scope.verificationData = response.data;

          $scope.close = function () {
              $modalInstance.close();
          };
      }]);
