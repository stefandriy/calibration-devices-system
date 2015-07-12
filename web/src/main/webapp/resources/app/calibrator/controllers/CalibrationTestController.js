angular
    .module('calibratorModule')
    .controller('CalibrationTestController', ['$scope', '$http', 'CalibrationTestService',
        function ($scope, $http, calibrationTestService) {

            $scope.calibrationTests = [];
           // $rootScope.$broadcast('test-is-created');


            function getCalibrationTests(){
                calibrationTestService
                    .getCalibrationTests()
                    .then(function(data){;
                        $scope.calibrationTests = data.calibrationTests;
                    })
            }

            getCalibrationTests();
            
            function saveCalibrationTest() {
                calibrationTestService
                    .saveCalibrationTest($scope.addFormData)
                    .then(function (data) {
                        $scope.addFormData = null;
                    });
            }
        }]);
