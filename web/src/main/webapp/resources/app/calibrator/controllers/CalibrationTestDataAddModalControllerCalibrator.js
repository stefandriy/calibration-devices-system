/**
 * Created by Konyk on 13.08.2015.
 */
angular
    .module('employeeModule')
    .controller('CalibrationTestDataAddModalControllerCalibrator', ['$rootScope', '$scope', '$http', '$log',
        '$modalInstance', 'CalibrationTestServiceCalibrator','$timeout', 'calibrationTest',
        function ($rootScope, $scope, $http, $log, $modalInstance, calibrationTestServiceCalibrator, calibrationTest) {

            $scope.calibrationTestdatas = [];

            $scope.resetTestDataForm = function() {
                $scope.$broadcast('show-errors-reset');
                $scope.TestDataFormData = null;
            };

            $scope.resetTestDataForm();

            /**
             * Saves new testData from the form in database.
             * If everything is ok then resets the testData
             * form and updates table with testDatas.
             */
            $scope.saveCalibrationTestData = function() {
                calibrationTestServiceCalibrator
                    .saveCalibrationTestData($scope.TestDataFormData,  calibrationTest)
                    .then(function (data) {
                        if (data == 201) {
                            $scope.closeModal();
                            $scope.resetTestDataForm();
                            $rootScope.onTableHandling();
                        }
                    });
            }

            /**
             * Closes the modal window for adding new
             * test.
             */
            $rootScope.closeModal = function() {
                $modalInstance.close();
            };

        }]);
