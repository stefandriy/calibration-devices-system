angular
    .module('employeeModule')
    .controller('VerificationResetAlertControllerProvider', ['$scope', '$log', '$modalInstance',  '$rootScope',
        function ($scope, $log, $modalInstance, $rootScope) {

            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            $scope.cancel = function () {
                $modalInstance.close();
            };

            $scope.submit = function() {
                $rootScope.$broadcast('provider-reset-form');
                $modalInstance.close();
            };
        }]);
