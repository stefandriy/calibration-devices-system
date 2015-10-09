/**
 * Created by Konyk on 11.08.2015.
 */
angular
    .module('employeeModule')
    .controller('CalibrationTestAddControllerCalibrator', ['$rootScope', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', 'Upload', '$timeout',
        function ($rootScope, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, Upload,  $timeout) {

            $scope.smallForm = [];

            $scope.testId = $location.search().param;

            $scope.fileLoaded = false;

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

            var self = $scope;
            $scope.uploadBbiFile = function(testId) {
                console.log("Entered upload bbi function");
                var modalInstance =  $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/upload-bbiFile.html',
                    controller: 'UploadBbiFileController',
                    size: 'lg',
                    resolve: {
                        calibrationTest: function () {
                            return testId;
                        },
                        parseBbiFile: function() {
                            return $scope.parseBbiFile;
                        }
                    }
                });

                modalInstance.result.then(function (status, fileName) {
                    $scope.fileName = fileName;
                    console.log(status + " " + fileName);
                    $rootScope.onTableHandling();
                });

            };

            $scope.parseBbiFile = function(fileName) {
                console.log("Entered parse bbi file; File name = " + fileName);
                calibrationTestServiceCalibrator
                    .parseBbiFile(fileName)
                    .then(function(result) {
                        $scope.fileLoaded = true;
                        console.log(result.data);
                        $scope.TestForm = result.data;
                        var date = $scope.TestForm.testDate;
                        $scope.TestForm.testDate = moment(date).utcOffset(0).format("DD.MM.YYYY HH:mm");
                        document.getElementById('testMainPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestForm.testPhoto);
                        $scope.TestDataFormData1 = result.data.listTestData[0];
                        document.getElementById('test1BeginPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData1.beginPhoto);
                        document.getElementById('test1EndPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData1.endPhoto);
                        $scope.TestDataFormData2 = result.data.listTestData[1];
                        document.getElementById('test2BeginPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData2.beginPhoto);
                        document.getElementById('test2EndPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData2.endPhoto);
                        $scope.TestDataFormData3 = result.data.listTestData[2];
                        document.getElementById('test3BeginPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData3.beginPhoto);
                        document.getElementById('test3EndPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData3.endPhoto);
                        $scope.TestDataFormData4 = result.data.listTestData[3];
                        document.getElementById('test4BeginPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData4.beginPhoto);
                        document.getElementById('test4EndPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData4.endPhoto);
                        $scope.TestDataFormData5 = result.data.listTestData[4];
                        document.getElementById('test5BeginPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData5.beginPhoto);
                        document.getElementById('test5EndPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData5.endPhoto);
                        $scope.TestDataFormData6 = result.data.listTestData[5];
                        document.getElementById('test6BeginPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData6.beginPhoto);
                        document.getElementById('test6EndPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestDataFormData6.endPhoto);
                    });
            }

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