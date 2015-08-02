angular.module('employeeModule')
    .controller('CalibrationTestEditModalController',
    ['$rootScope', '$scope', '$modalInstance', 'CalibrationTestServiceCalibrator',
        function($rootScope, $scope, $modalInstance, CalibrationTestServiceCalibrator) {

            /**
             * Resets test form
             */
            $scope.resetTestForm = function() {
                $scope.$broadcast('show-errors-reset');
                $rootScope.test = null;
            };

            /**
             * Calls testForm after the view loaded
             */
            $scope.resetTestForm();

            /**
             * Edit test. If everything is ok then
             * resets the test form and closes modal
             * window.
             */
            $scope.editTest = function() {
                var testForm = {
                    name : $rootScope.test.name,
                    temperature : $rootScope.test.temperature,
                    settingNumber : $rootScope.test.settingNumber,
                    latitude : $rootScope.test.latitude,
                    temperature : $rootScope.test.longitude,
                    settingNumber : $rootScope.test.testResult
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
             * Closes edit modal window.
             */
            $scope.closeModal = function() {
                $modalInstance.close();
            };

        } ]);

