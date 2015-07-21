angular
    .module('employeeModule')
    .controller('CalibrationTestControllerCalibrator', ['$scope', '$http', '$log', 'CalibrationTestServiceCalibrator',
        function ($scope, $http, $log, calibrationTestServiceCalibrator) {

            $scope.calibrationTests = [];

            $log.debug("In controller!");
            function getCalibrationTests(){
                calibrationTestServiceCalibrator
                    .getCalibrationTests()
                    .then(function(data){;
                        $scope.calibrationTests = data.calibrationTests;
                    })
            }

            getCalibrationTests();
            
            $scope.saveCalibrationTest = function() {
                calibrationTestServiceCalibrator
                    .saveCalibrationTest($scope.addFormData)
                    .then(function (data) {
                       $log.debug("saved!");
                    });
            }
         
        }]);
