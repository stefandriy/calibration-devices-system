/**
 * Created by Konyk on 11.08.2015.
 */
angular
    .module('employeeModule')
    .controller('CalibrationTestAddControllerCalibrator', ['$rootScope', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', 'Upload', '$timeout',
        function ($rootScope, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, Upload,  $timeout) {

            $scope.testId = $location.search().param;

            $scope.fileLoaded = false;

            $scope.TestDataFormData = [{}, {}, {}, {}, {}, {}];

            /**
             * Resets Test form
             */
            $scope.resetTestForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.TestForm = null;
                $scope.TestDataFormData = [{}, {}, {}, {}, {}, {}];
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

            $scope.parseBbiFile = function(data) {
                $scope.fileLoaded = true;
                console.log(data);
                $scope.TestForm = data;
                var date = $scope.TestForm.testDate;
                $scope.TestForm.testDate = moment(date).utcOffset(0).format("DD.MM.YYYY HH:mm");
                document.getElementById('testMainPhoto').setAttribute('src', 'data:image/png;base64,' + $scope.TestForm.testPhoto);
                $scope.TestDataFormData = data.listTestData;
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
                $scope.generalForms={testForm:$scope.TestForm, smallForm: $scope.TestDataFormData};
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