/**
 * Created by MAX on 25.07.2015.
 */
angular
    .module('employeeModule')

    .controller('UploadBbiFileController', ['$scope', '$log', '$modalInstance',
        'verification', 'Upload', '$timeout', function ($scope, $log, $modalInstance, verification, Upload, $timeout) {

            $scope.cancel = function () {
                $modalInstance.dismiss();
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
                            url: '/calibrator/verifications/new/upload?idVerification='+verification,
                            file: file
                        }).progress(function (evt) {
                            $scope.uploaded = true;
                            $scope.progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        }).success(function (data, status, headers, config) {
                            $log.debug(data);
                            $timeout(function() {
                                $scope.messageSuccess = 'Ви успішно завантажили файл ' + config.file.name;
                            });
                        }).error(function () {
                            $scope.messageError = 'Не вдалось завантажити ' + config.file.name;
                        })
                    }
                }
            };
        }]);

