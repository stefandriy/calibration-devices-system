angular
    .module('employeeModule')
    .controller('SendingModalControllerCalibrator', ['$scope', '$log', '$modalInstance', 'response', '$rootScope',
        function ($scope, $log, $modalInstance, response, $rootScope) {

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            if (response.data != null) {
                $scope.verificators = response.data;
                $scope.formData = {};
                $scope.formData.verificator = {};
            }

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