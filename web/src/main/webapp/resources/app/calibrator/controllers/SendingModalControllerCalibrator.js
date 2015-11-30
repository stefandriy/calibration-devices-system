angular
    .module('employeeModule')
    .controller('SendingModalControllerCalibrator', ['$scope', '$log', '$modalInstance', 'response', '$rootScope',
        function ($scope, $log, $modalInstance, response, $rootScope) {

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $scope.formData = [];
            $scope.formData.verificator = undefined;
            $scope.verificators = response.data;

            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
            $scope.submit = function (verificator) {
                $scope.$broadcast('show-errors-check-validity');

                if ($scope.verificatorSelectionForm.$valid) {
                    $modalInstance.close($scope.formData);
                }
            }
        }]);