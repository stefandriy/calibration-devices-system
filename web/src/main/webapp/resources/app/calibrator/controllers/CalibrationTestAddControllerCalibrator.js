/**
 * Created by Konyk on 11.08.2015.
 */
angular
    .module('employeeModule')
    .controller('CalibrationTestAddControllerCalibrator', ['$rootScope', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', 'Upload', '$timeout',
        function ($rootScope, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, Upload,  $timeout) {

            $scope.testId = $location.search().param;
            $scope.hasProtocol = $location.search().loadProtocol || false;

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

            $scope.uploadBbiFile = function(testId) {
                var modalInstance =  $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/calibrator/views/modals/upload-bbiFile.html',
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
            };

            $scope.parseBbiFile = function(data) {
                $scope.fileLoaded = true;
                $scope.TestForm = data;
                var date = $scope.TestForm.testDate;
                $scope.TestForm.testDate = moment(date).utcOffset(0).format("DD.MM.YYYY HH:mm");
                $scope.TestForm.testPhoto = "data:image/png;base64," + $scope.TestForm.testPhoto;
                $scope.TestDataFormData = data.listTestData;
            };

            $scope.showEditMainPhotoModal = function (id) {
                var modalInstance =  $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/calibrator/views/modals/edit-main-photo-modal.html',
                    controller: 'EditPhotoController',
                    size: 'md',
                    resolve: {
                         photoId: function() {
                            return id;
                        },
                         parentScope: function() {
                            return $scope;
                        }
                    }
                });
            };

            $scope.setMainPhoto = function (data) {
                $scope.TestForm.testPhoto = data;
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
                                templateUrl: 'resources/app/calibrator/views/modals/calibration-test-adding-success.html',
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