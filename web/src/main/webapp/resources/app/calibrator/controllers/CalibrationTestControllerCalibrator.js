angular
    .module('employeeModule')
    .controller('CalibrationTestControllerCalibrator', ['$scope', '$http', 'CalibrationTestServiceCalibrator',
        function ($scope, $http, calibrationTestServiceCalibrator) {

            $scope.calibrationTests = [];
           // $rootScope.$broadcast('test-is-created');


            function getCalibrationTests(){
                calibrationTestServiceCalibrator
                    .getCalibrationTests()
                    .then(function(data){;
                        $scope.calibrationTests = data.calibrationTests;
                    })
            }

            getCalibrationTests();
            
            function saveCalibrationTest() {
                calibrationTestServiceCalibrator
                    .saveCalibrationTest($scope.addFormData)
                    .then(function (data) {
                        $scope.addFormData = null;
                    });
            }
        }]);
