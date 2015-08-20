/**
 * Created by Konyk on 11.08.2015.
 */
angular
    .module('employeeModule')
    .controller('CalibrationTestAddControllerCalibrator', ['$rootScope', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', '$stateParams',
        function ($rootScope, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, $stateParams) {
            $scope.calibrationTests = [];


            $scope.verId = $location.search().param;

            $scope.Case1 = true;
            $scope.Case2 = false;
            $scope.Case3 = false;
            $scope.Case4 = false;
            $scope.Case5 = false;
            $scope.Case6 = false;

            $scope.Test1 = function(){
                $log.debug("in Case 1");
                $log.debug($scope.Case1, $scope.Case2,  $scope.Case3, $scope.Case4,  $scope.Case5,  $scope.Case6);
                $scope.Case1 = true;
                $scope.Case2 = false;
                $scope.Case3 = false;
                $scope.Case4 = false;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test2 = function(){
                $log.debug("in Case 2");
                $log.debug($scope.Case1, $scope.Case2,  $scope.Case3, $scope.Case4,  $scope.Case5,  $scope.Case6);
                $scope.Case1 = false;
                $scope.Case2 = true;
                $scope.Case3 = false;
                $scope.Case4 = false;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test3 = function(){
                $log.debug("in Case 3");
                $log.debug($scope.Case1, $scope.Case2,  $scope.Case3, $scope.Case4,  $scope.Case5,  $scope.Case6);
                $scope.Case1 = false;
                $scope.Case2 = false;
                $scope.Case3 = true;
                $scope.Case4 = false;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test4 = function(){
                $log.debug("in Case 4");
                $log.debug($scope.Case1, $scope.Case2,  $scope.Case3, $scope.Case4,  $scope.Case5,  $scope.Case6);
                $scope.Case1 = false;
                $scope.Case2 = false;
                $scope.Case3 = false;
                $scope.Case4 = true;
                $scope.Case5 = false;
                $scope.Case6 = false;

            };

            $scope.Test5 = function(){
                $log.debug("in Case 5");
                $log.debug($scope.Case1, $scope.Case2,  $scope.Case3, $scope.Case4,  $scope.Case5,  $scope.Case6);
                $scope.Case1 = false;
                $scope.Case2 = false;
                $scope.Case3 = false;
                $scope.Case4 = false;
                $scope.Case5 = true;
                $scope.Case6 = false;

            };

            $scope.Test6 = function(){
                $log.debug("in Case 6");
                $log.debug($scope.Case1, $scope.Case2,  $scope.Case3, $scope.Case4,  $scope.Case5,  $scope.Case6);
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
            $scope.resetTestForm = function() {
                $scope.$broadcast('show-errors-reset');
                $scope.TestFormData = null;
            };

            /**
             * Calls resetTestForm after the view loaded
             */
            $scope.resetTestForm();

            function getCalibrationTests(){
                calibrationTestServiceCalibrator
                    .getCalibrationTests()
                    .then(function(data){;
                        $scope.calibrationTests = data.calibrationTests;
                    })
            }

            getCalibrationTests();


            /**
             * Saves new test from the form in database.
             * If everything is ok then resets the test
             * form and updates table with tests.
             */
            $scope.saveCalibrationTest = function() {
                calibrationTestServiceCalibrator
                    .saveCalibrationTest($scope.TestFormData,  $scope.verId)
                    .then(function (data) {
                        if (data == 201) {
                            $scope.resetTestForm();
                            $rootScope.onTableHandling();
                        }
                    });
            }

        }]);