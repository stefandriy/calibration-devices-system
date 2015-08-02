angular
    .module('employeeModule')
    .controller('UploadPhotoController', ['$scope', '$route', '$log', '$modalInstance',
        'calibrationTest', 'Upload', '$timeout', function ($scope, $route, $log, $modalInstance, calibrationTest, Upload, $timeout) {

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
                            url: '/calibrator/calibrationTests/uploadPhotos?idCalibrationTest=' +  calibrationTest,
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


