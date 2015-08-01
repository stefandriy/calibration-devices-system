angular
    .module('employeeModule')
    .controller('CalibrationTestControllerCalibrator', ['$rootScope', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', '$stateParams',
        function ($rootScope, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, $stateParams) {
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


            /**
             // * Opens modal window for adding new calibration-test.
             // */
                $scope.openAddCalibrationTestModal = function (verificationId) {
                    var addTestModal = $modal
                        .open({
                            animation: true,
                            controller: 'CalibrationTestAddModalControllerCalibrator',
                            templateUrl: '/resources/app/calibrator/views/modals/calibration-test-add-modal.html',
                            resolve: {
                                verification: function () {
                                    return verificationId;

                                }
                            }
                        });
                };
            //$scope.openTestAddModal = function(){
            //    var addEquipmentModal = $modal
            //        .open({
            //            animation : true,
            //            controller : 'CalibrationTestAddModalControllerCalibrator',
            //            templateUrl : '/resources/app/calibrator/views/modals/calibration-test-add-modal.html',
            //        });
            //};


            function getCalibrationTests(){
                calibrationTestServiceCalibrator
                    .getCalibrationTests()
                    .then(function(data){;
                        $scope.calibrationTests = data.calibrationTests;
                    })
            }

            getCalibrationTests();

            $scope.saveCalibrationTest = function() {

                calibrationTestServiceCalibrator
                    .saveCalibrationTest($scope.addFormData)
                    .then(function (data) {

                       $log.debug("saved!");

//                        $scope.addFormData = null;

                    });
            }
         
        }]);
