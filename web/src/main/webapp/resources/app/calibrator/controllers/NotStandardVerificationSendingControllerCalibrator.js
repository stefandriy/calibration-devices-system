angular
    .module('employeeModule')
    .controller('NotStandardVerificationSendingControllerCalibrator', ['$scope', '$log', '$modalInstance', 'response', '$rootScope',
        function ($scope, $log, $modalInstance, response, $rootScope) {

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $scope.formData = [];
            $scope.formData.provider = undefined;
            $scope.providers = response.data;

            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
            $scope.submit = function (provider) {
                $scope.$broadcast('show-errors-check-validity');

                if ($scope.providerSelectionForm.$valid) {
                    $modalInstance.close($scope.formData);
                }
            }
        }]);
