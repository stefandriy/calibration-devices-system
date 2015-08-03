angular.module('employeeModule')
    .controller('CalibrationTestEditModalController',
    ['$rootScope', '$scope', '$modalInstance', 'CalibrationTestServiceCalibrator',
        function($rootScope, $scope, $modalInstance, CalibrationTestServiceCalibrator) {
            /**
             * Edit test. If everything is ok then
             * resets the test form and closes modal
             * window.
             */

            $scope.editTest = function() {
                var testForm = {
                    name : $scope.calibrationTest.name,
                    temperature : $scope.calibrationTest.temperature,
                    settingNumber : $scope.calibrationTest.settingNumber,
                    latitude : $scope.calibrationTest.latitude,
                    longitude : $scope.calibrationTest.longitude,
                    consumptionStatus : $scope.calibrationTest.consumptionStatus,
                    testResult : $scope.calibrationTest.testResult
                }
                CalibrationTestServiceCalibrator.editCalibrationTest(
                    testForm,
                    $rootScope.testId).then(
                    function(data) {
                        if (data == 200) {
                            $scope.closeModal();
                            $scope.resetTestForm();
                            $rootScope.onTableHandling();
                        }
                    });
            }


            /**
             * Resets test form
             */
            $scope.resetTestForm = function() {
                $scope.$broadcast('show-errors-reset');
                $scope.calibrationTest = null;
            };

            /**
             * Closes edit modal window.
             */
            $scope.closeModal = function() {
                $modalInstance.close();
            };

        } ]);

