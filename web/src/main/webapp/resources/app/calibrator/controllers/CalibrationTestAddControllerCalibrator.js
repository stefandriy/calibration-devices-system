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

            function convectorStatus(data) {
                if(data.consumptionStatus=== "IN_THE_AREA"){
                    data.consumptionStatus = "В зоні";
                }else if(data.consumptionStatus=== "NOT_IN_THE_AREA"){
                    data.consumptionStatus = "Не в зоні";
                }
            }

            function convectorTestResult(data) {
                if(data.testResult=='SUCCESS'){
                    data.testResult="Успішний";
                }else if(data.testResult=='FAILED'){
                    data.testResult="Невдалий";
                }else{
                    data.testResult="Необроблений";
                }
            }

            $scope.parseBbiFile = function(data) {
                $scope.fileLoaded = true;
                convectorStatus(data);
                convectorTestResult(data);
                console.log(data.listTestData.length)
                for(var i = 0 ;i<data.listTestData.length;i++){
                    var dataTest = data.listTestData[i];
                    convectorStatus(dataTest);
                    convectorTestResult(dataTest);
                }
                $scope.TestForm = data;
                for (var i = 0; i < $scope.statusData.length; i++) {
                    if ($scope.statusData[i].id === data.status) {
                        $scope.selectedStatus = {
                            label: $scope.statusData[i].label,
                            id: $scope.statusData[i].id
                        };
                    }
                }
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
                label: null
            }
            $scope.statusData = [
                {id: 'TEST_OK', label: null},
                {id: 'TEST_NOK', label: null}
            ];

            $scope.setStatus = function (status) {
                $scope.selectedStatus = {
                    label: status.label,
                    id: status.id
                };
            };

            $scope.setTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {
                    $scope.statusData[0].label = 'придатний';
                    $scope.statusData[1].label = 'непридатний';
                    $scope.statusWaterType[0].label = 'гаряче';
                    $scope.statusWaterType[1].label = 'холодне';
                } else if (lang === 'eng') {
                    $scope.statusData[0].label = 'Tested OK';
                    $scope.statusData[1].label = 'Tested NOK';
                    $scope.statusWaterType[0].label = 'hot';
                    $scope.statusWaterType[1].label = 'cold';
                }
            };

            $scope.selectedWaterType = {
                label: null
            }
            $scope.statusWaterType = [
                {id: 'hot', label: null},
                {id: 'cold', label: null}
            ];

            $scope.setWaterType = function (status) {
                $scope.selectedWaterType = {
                    label: status.label,
                    id: status.id
                };
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
                    testResult:$scope.TestForm.testResult,
                    listTestData:$scope.TestForm.listTestData,
                    status:$scope.selectedStatus.id
                }
            }

            /**
             * update test from the form in database.
             */
                /*$scope.generalForms={testForm:$scope.TestForm, smallForm: $scope.TestDataFormData};
                  $log.debug($scope.generalForms);
                .updateCalibrationTest($scope.TestForm, $scope.testId)*/

            $scope.closeForm = function(){
                window.history.back();
            }

            $scope.updateCalibrationTest = function () {
                        retranslater();
                        calibrationTestServiceCalibrator
                            .updateCalibrationTest(protocol,$scope.testId)
                            .then(function (status) {
                                if (status == 201) {
                                    $rootScope.onTableHandling();
                                }
                                if(status==200){
                                    $modal.open({
                                        animation: true,
                                        templateUrl: 'resources/app/calibrator/views/modals/calibration-test-edited-success.html',
                                        controller: function ($modalInstance) {
                                            this.ok = function () {
                                                $modalInstance.close();
                                                window.history.back();
                                            }
                                        },
                                        controllerAs: 'successController',
                                        size: 'md'
                                    });

                                }
                            });
            }

        }]);