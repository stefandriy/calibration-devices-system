/**
 * Created by Konyk on 11.08.2015.
 */
angular
    .module('employeeModule')
    .controller('CalibrationTestAddControllerCalibrator', ['$rootScope', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', '$timeout',
        function ($rootScope, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, $timeout) {

            $scope.smallForm = [];


            $scope.testId = $location.search().param;

            $scope.Case1 = true;
            $scope.Case2 = false;
            $scope.Case3 = false;
            $scope.Case4 = false;
            $scope.Case5 = false;
            $scope.Case6 = false;


            $scope.Test1 = function () {
                $log.debug("in Case 1");
                $scope.Case1 = true;
                $scope.Case2 = false;
                $scope.Case3 = false;
                $scope.Case4 = false;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test2 = function () {
                $log.debug("in Case 2");
                $scope.Case1 = false;
                $scope.Case2 = true;
                $scope.Case3 = false;
                $scope.Case4 = false;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test3 = function () {
                $log.debug("in Case 3");
                $scope.Case1 = false;
                $scope.Case2 = false;
                $scope.Case3 = true;
                $scope.Case4 = false;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test4 = function () {
                $log.debug("in Case 4");
                $scope.Case1 = false;
                $scope.Case2 = false;
                $scope.Case3 = false;
                $scope.Case4 = true;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test5 = function () {
                $log.debug("in Case 5");
                $scope.Case1 = false;
                $scope.Case2 = false;
                $scope.Case3 = false;
                $scope.Case4 = false;
                $scope.Case5 = true;
                $scope.Case6 = false;

            };

            $scope.Test6 = function () {
                $log.debug("in Case 6");
                $scope.Case1 = false;
                $scope.Case2 = false;
                $scope.Case3 = false;
                $scope.Case4 = false;
                $scope.Case5 = false;
                $scope.Case6 = true;

            };
            /**
             * Resets Test form
             */
            $scope.resetTestForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.TestForm = null;
                $scope.TestDataFormData1  = null;
                $scope.TestDataFormData2 = null;
                $scope.TestDataFormData3  = null;
                $scope.TestDataFormData4 = null;
                $scope.TestDataFormData5  = null;
                $scope.TestDataFormData6 = null;
            };


            function getCalibrationTests() {
                calibrationTestServiceCalibrator
                    .getCalibrationTests()
                    .then(function (data) {
                        $scope.calibrationTests = data.calibrationTests;
                    })
            }

            getCalibrationTests();


            /**
             * Saves new test from the form in database.
             * If everything is ok then resets the test
             * form and updates table with tests.
             */
            $scope.saveCalibrationTest = function () {

                $scope.smallForm.push($scope.TestDataFormData1, $scope.TestDataFormData2, $scope.TestDataFormData3,
                    $scope.TestDataFormData4, $scope.TestDataFormData5, $scope.TestDataFormData6);



                $scope.generalForms={testForm:$scope.TestForm, smallForm: $scope.smallForm};
                $log.debug($scope.generalForms);
                        calibrationTestServiceCalibrator
                            .saveCalibrationTest($scope.generalForms, $scope.testId)
                            .then(function (data) {
                                if (data == 201) {
                                    $rootScope.onTableHandling();
                                }
                            });
                            $modal.open({
                                animation: true,
                                templateUrl: '/resources/app/calibrator/views/modals/calibration-test-adding-success.html',
                                controller: function ($modalInstance) {
                                    this.ok = function () {
                                        $scope.resetTestForm();
                                        $modalInstance.close();
                                    }
                                },
                                controllerAs: 'successController',
                                size: 'md'
                });

            }

        }]);