angular
    .module('employeeModule')
    .controller('CalibrationTestControllerCalibrator', ['$rootScope', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', 'Upload', '$timeout',
        function ($rootScope, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, Upload, $timeout) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];

            /**
             * Updates the table with CalibrationTests.
             */
            $scope.verId = $location.search().param;
            $scope.searchData = null;

            $rootScope.onTableHandling = function () {
                calibrationTestServiceCalibrator.getPage($scope.currentPage, $scope.itemsPerPage, $scope.searchData, $scope.verId)
                    .then(function (data) {
                        $scope.pageContent = data.content;
                        $scope.totalItems = data.totalItems;
                    });
            };
            $rootScope.onTableHandling();


            $scope.calibrationTests = [];

            
            $scope.openAddTest = function (verId) {
                calibrationTestServiceCalibrator
                    .getEmptyTest(verId)
                    .then(function (IdTest) {
                        $log.debug("inside");
                        var url = $location.path('/calibrator/verifications/calibration-test-add/').search({param: IdTest});
                    } )
            }

            function getCalibrationTests() {
                calibrationTestServiceCalibrator
                    .getCalibrationTests()
                    .then(function (data) {
                        ;
                        $scope.calibrationTests = data.calibrationTests;
                    })
            }

            getCalibrationTests();

            $scope.saveCalibrationTest = function () {

                calibrationTestServiceCalibrator
                    .saveCalibrationTest($scope.FormData)
                    .then(function (data) {

                        $log.debug("saved!");

                    });
            };



            /**
             * Opens modal window for editing equipment.
             */
            $scope.editCalibrationTest = function (testId) {
                $rootScope.testId = testId;
                calibrationTestServiceCalibrator.getCalibrationTestWithId(
                    $rootScope.testId).then(
                    function (data) {
                        $rootScope.calibrationTest = data;
                    });
                var testDTOModal = $modal
                    .open({
                        animation: true,
                        controller: 'CalibrationTestEditModalController',
                        templateUrl: '/resources/app/calibrator/views/modals/calibration-test-edit-modal.html',
                    });
            };

            $scope.deleteTest = function (testId) {
                $rootScope.testId = testId;
                calibrationTestServiceCalibrator.deleteCalibrationTest(testId);
                $timeout(function() {
                    console.log('delete with timeout');
                    $rootScope.onTableHandling();
                }, 700);
            };

            $scope.uploadPhoto = function (testId) {

                var modalInstance =  $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/upload-photo.html',
                    controller: 'UploadPhotoController',
                    size: 'lg',
                    resolve: {
                        calibrationTest: function () {
                            return testId;

                        }
                    }
                });
                modalInstance.result.then(function () {
                    $rootScope.onTableHandling();
                });
            };

            $scope.openAddCalibrationTestDataModal = function(testId){
                var addTestDataModal = $modal
                    .open({
                        animation: true,
                        controller: 'CalibrationTestDataAddModalControllerCalibrator',
                        templateUrl: '/resources/app/calibrator/views/modals/calibration-testData-add-modal.html',
                        resolve: {
                            calibrationTest: function () {
                                return testId;

                            }
                        }
                    });

            }

        }]);
