angular
    .module('employeeModule')

    .controller('SendingModalControllerProvider', ['$scope', '$log', '$modalInstance', 'response', '$rootScope',
        function ($scope, $log, $modalInstance, response, $rootScope) {

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            if (response.data != null) {

                $scope.calibrators = response.data;
                $scope.formData = {};
                $scope.formData.calibrator = $scope.calibrators[0];
            }
            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
            $scope.submit = function () {
                $scope.$broadcast('show-errors-check-validity');


                if ($scope.calibratorSelectionForm.$valid) {
                    $modalInstance.close($scope.formData);

                }
            }
        }]);


