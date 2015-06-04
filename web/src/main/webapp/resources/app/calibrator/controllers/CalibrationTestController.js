angular
    .module('calibratorModule')
    .controller('CalibrationTestController', ['$scope', '$http', 'CalibrationTestService', 'StatisticService',
        function ($scope, $http, calibrationTestService) {

            $scope.calibrationTests = [];


            getCalibrationTests();

            function getCalibrationTests(){
                calibrationTestService
                    .getCalibrationTests()
                    .then(function(data){;
                        $scope.calibrationTests = data.calibrationTests;
                    })
            }

            function saveCalibrationTest() {
                calibrationTestService
                    .saveCalibrationTest($scope.addFormData)
                    .then(function (data) {
                        $scope.addFormData = null;
                    });
            }
        }]);
