angular
    .module('verificatorModule')
    .controller('CalibrationTestReviewController', ['$scope', '$modalInstance', '$log', 'response',
        function ($scope, $modalInstance, $log, response) {

            $scope.calibrationTestData = response.data;

            $log.info($scope.calibrationTestData);

            $scope.close = function () {
                $modalInstance.close();
            };
        }]);