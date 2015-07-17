angular
    .module('employeeModule')
    .controller('CalibrationTestReviewControllerVerificator', ['$scope', '$modalInstance', '$log', 'response',
        function ($scope, $modalInstance, $log, response) {

            $scope.calibrationTest = response.data;

            $log.info($scope.calibrationTest);

            $scope.close = function () {
                $modalInstance.close();
            };
        }]);