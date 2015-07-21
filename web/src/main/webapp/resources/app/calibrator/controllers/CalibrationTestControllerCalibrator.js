angular
    .module('employeeModule')
    .controller('CalibrationTestControllerCalibrator', ['$scope', '$http', '$log', 'CalibrationTestServiceCalibrator',
        function ($scope, $http, $log, calibrationTestServiceCalibrator) {

            $scope.calibrationTests = [];

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

//                        $scope.addFormData = null;

                    });
            }
         
        }]);
