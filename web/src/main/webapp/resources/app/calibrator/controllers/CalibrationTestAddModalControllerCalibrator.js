angular
    .module('employeeModule')
    .controller('CalibrationTestAddModalControllerCalibrator', ['$rootScope', '$scope', '$http', '$log',
        '$modalInstance', 'CalibrationTestServiceCalibrator', 'verification','Upload','$location','$timeout',
        function ($rootScope, $scope, $http, $log, $modalInstance, calibrationTestServiceCalibrator, verification,Upload,
                  $location,$timeout) {

            $scope.calibrationTests = [];


            $scope.verId = $location.search().param;
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
                    .saveCalibrationTest($scope.TestFormData, verification)
                    .then(function (data) {
                        if (data == 201) {
                            $scope.closeModal();
                            $scope.resetTestForm();
                            $rootScope.onTableHandling();
                        }
                    });
            }

            /**
             * Closes the modal window for adding new
             * test.
             */
            $rootScope.closeModal = function() {
                $modalInstance.close();
            };


            // Upload files
            $scope.cancel = function () {
                $modalInstance.close("cancel");

            };

            $scope.$watch('files', function () {
                $scope.upload($scope.files);
            });

            $scope.uploaded = false;

            $scope.progressPercentage = 0;

            $scope.upload = function (files) {
                if (files && files.length) {
                    for (var i = 0; i < files.length; i++) {
                        var file = files[i];
                        Upload.upload({
                            url: '/calibrator/calibrationTests/uploadPhotos?idVerification=' +  $scope.verId,
                            file: file
                        }).progress(function (evt) {
                            $scope.uploaded = true;
                            $scope.progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        }).success(function (data, status, headers, config) {

                            $timeout(function () {
                                    if (status === 200) {
                                        $scope.messageError = null;
                                        $scope.messageSuccess = 'Ви успішно завантажили файл ' + config.file.name;

                                    } else {
                                        $scope.messageError = 'Не вдалось завантажити ' + config.file.name;
                                        $scope.progressPercentage = parseInt(0);
                                        $scope.uploaded = false;
                                    }
                                }
                            );
                        }).error(function () {
                            $scope.messageError = 'Не вдалось завантажити ' + config.file.name;
                            $scope.progressPercentage = parseInt(0);

                        })
                    }
                }
            };


         
        }]);
