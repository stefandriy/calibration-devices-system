/**
 * Created by Konyk on 11.08.2015.
 */
angular
    .module('employeeModule')
    .controller('CalibrationTestAddControllerCalibrator', ['$rootScope', '$translate', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', 'Upload', '$timeout',
        function ($rootScope, $translate, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, Upload,  $timeout) {

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

            function getProtocolTest(verificationID) {
                calibrationTestServiceCalibrator
                    .getTestProtocol(verificationID)
                    .then(function (data){
                        $scope.parseBbiFile(data);
                        $log.debug("inside");
                    } );
            }

            $scope.selectedStatus = {
                name: null
            }
            $scope.statusData = [
                {id: 'REJECTED', label: null},
                {id: 'SENT_TO_VERIFICATOR', label: null},
                {id: 'TEST_OK', label: null},
                {id: 'TEST_NOK', label: null}
            ];

            $scope.setTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {
                    $scope.statusData[0].label = 'Відхилена';
                    $scope.statusData[1].label = 'Перевірено придатний';
                    $scope.statusData[2].label = 'Перевірено непридатний';
                } else if (lang === 'eng') {
                    $scope.statusData[0].label = 'Rejected';
                    $scope.statusData[1].label = 'Tested OK';
                    $scope.statusData[2].label = 'Tested NOK';
                }
            };

            $scope.setTypeDataLanguage();

            if ( $scope.hasProtocol){
                getProtocolTest($scope.testId);
            }else{
                getCalibrationTests();

            }

            function retranslater() {
                protocol = {
                    fileName:$scope.TestForm.fileName,
                    counterNumber:$scope.TestForm.counterNumber,
                    temperature:$scope.TestForm.temperature,
                    installmentNumber:$scope.TestForm.installmentNumber,
                    latitude:$scope.TestForm.latitude,
                    longitude:$scope.TestForm.longitude,
                    consumptionStatus:$scope.TestForm.consumptionStatus,
                    testResult:$scope.TestForm.testResult,
                    listTestData:$scope.TestForm.listTestData,
                }
            }

            /**
             * update test from the form in database.
             */
                /*$scope.generalForms={testForm:$scope.TestForm, smallForm: $scope.TestDataFormData};
                  $log.debug($scope.generalForms);
                .updateCalibrationTest($scope.TestForm, $scope.testId)*/

            $scope.updateCalibrationTest = function () {
                        retranslater();
                        calibrationTestServiceCalibrator
                            .updateCalibrationTest(protocol,$scope.testId)
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