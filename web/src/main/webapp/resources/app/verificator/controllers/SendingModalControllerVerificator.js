angular
    .module('employeeModule')
    .controller('SendingModalControllerVerificator', ['$scope', '$log', '$modalInstance', 'response','$rootScope',
        function ($scope, $log, $modalInstance, response, $rootScope) {

            $scope.providers = response.data;
            $scope.formData={};
            $log.debug('Inside Sending');
            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
            $scope.submit = function () {
                $scope.$broadcast('show-errors-check-validity');

                if ($scope.providerSelectionForm.$valid) {
                    $modalInstance.close($scope.formData);
                }
            }
        }]);