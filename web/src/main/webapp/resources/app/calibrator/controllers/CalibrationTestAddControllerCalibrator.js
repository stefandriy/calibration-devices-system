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

            $log.debug($scope.TestFormData);
            $scope.Case1 = true;
            $scope.Case2 = false;
            $scope.Case3 = false;
            $scope.Case4 = false;
            $scope.Case5 = false;
            $scope.Case6 = false;


            $scope.Test1 = function () {
                $log.debug("in Case 1");
                $log.debug($scope.Case1, $scope.Case2, $scope.Case3, $scope.Case4, $scope.Case5, $scope.Case6);
                $scope.Case1 = true;
                $scope.Case2 = false;
                $scope.Case3 = false;
                $scope.Case4 = false;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test2 = function () {
                $log.debug("in Case 2");
                $log.debug($scope.Case1, $scope.Case2, $scope.Case3, $scope.Case4, $scope.Case5, $scope.Case6);
                $scope.Case1 = false;
                $scope.Case2 = true;
                $scope.Case3 = false;
                $scope.Case4 = false;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test3 = function () {
                $log.debug("in Case 3");
                $log.debug($scope.Case1, $scope.Case2, $scope.Case3, $scope.Case4, $scope.Case5, $scope.Case6);
                $scope.Case1 = false;
                $scope.Case2 = false;
                $scope.Case3 = true;
                $scope.Case4 = false;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test4 = function () {
                $log.debug("in Case 4");
                $log.debug($scope.Case1, $scope.Case2, $scope.Case3, $scope.Case4, $scope.Case5, $scope.Case6);
                $scope.Case1 = false;
                $scope.Case2 = false;
                $scope.Case3 = false;
                $scope.Case4 = true;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test5 = function () {
                $log.debug("in Case 5");
                $log.debug($scope.Case1, $scope.Case2, $scope.Case3, $scope.Case4, $scope.Case5, $scope.Case6);
                $scope.Case1 = false;
                $scope.Case2 = false;
                $scope.Case3 = false;
                $scope.Case4 = false;
                $scope.Case5 = true;
                $scope.Case6 = false;

            };

            $scope.Test6 = function () {
                $log.debug("in Case 6");
                $log.debug($scope.Case1, $scope.Case2, $scope.Case3, $scope.Case4, $scope.Case5, $scope.Case6);
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
            //$scope.resetTestForm = function () {
            //    $scope.$broadcast('show-errors-reset');
            //    $scope.TestFormData = null;
            //};

            /**
             * Calls resetTestForm after the view loaded
             */
            //$scope.resetTestForm();

            function getCalibrationTests() {
                calibrationTestServiceCalibrator
                    .getCalibrationTests()
                    .then(function (data) {
                        ;
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
                console.log("T1" + $scope.TestDataFormData1);

                $scope.smallForm.push($scope.TestDataFormData1, $scope.TestDataFormData2, $scope.TestDataFormData3,
                    $scope.TestDataFormData4, $scope.TestDataFormData5, $scope.TestDataFormData6);

                $scope.generalForms={testForm:$scope.TestForm, smallForm: $scope.smallForm};
                        calibrationTestServiceCalibrator
                            .saveCalibrationTest($scope.generalForms, $scope.testId)
                            .then(function (data) {
                                if (data == 201) {
                                    //$scope.resetTestForm();
                                    $rootScope.onTableHandling();
                                }
                            });
                            $modal.open({
                                animation: true,
                                templateUrl: '/resources/app/calibrator/views/modals/calibration-test-adding-success.html',
                                controller: function ($modalInstance) {
                                    this.ok = function () {
                                        $modalInstance.close();
                                    }
                                },
                                controllerAs: 'successController',
                                size: 'md'
                });

            }

        }]);