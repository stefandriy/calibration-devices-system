/**
 * Created by MAX on 25.07.2015.
 */
angular
    .module('employeeModule')

    .controller('UploadArchiveController', ['$scope', '$rootScope', '$route', '$log', '$modalInstance',
        'Upload', '$timeout',
        function ($scope, $rootScope, $route, $log, $modalInstance, Upload, $timeout) {

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $scope.$on('$locationChangeStart', function() {
                $modalInstance.close();
            });

            $scope.cancel = function () {
                $modalInstance.close("cancel");

            };

            $scope.$watch('files', function () {
                $scope.upload($scope.files);
            });
            $scope.$watch('file', function () {
                if ($scope.file != null) {
                    $scope.files = [$scope.file];
                }
            });

            $scope.uploaded = false;

            $scope.progressPercentage = 0;


            $scope.upload = function (files) {
                if (files && files.length) {
                    for (var i = 0; i < files.length; i++) {
                        var file = files[i];
                        Upload.upload({
                            url: '/calibrator/verifications/new/upload-archive',
                            file: file
                        }).progress(function (evt) {
                            $scope.uploaded = true;
                            $scope.progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        }).success(function (data, status, headers, config) {
                                $timeout(function () {
                                    if (status === 200) {
                                        $scope.messageError = null;
                                        $scope.fileName = config.file.name;
                                        $scope.messageSuccess = 'Ви успішно завантажили файл ' + config.file.name;
                                        console.log('uploadBbiController: ' + $scope.fileName);
                                    } else {
                                        $scope.messageError = 'Не вдалось завантажити ' + config.file.name;
                                        $scope.progressPercentage = parseInt(0);
                                        $scope.uploaded = false;
                                    }
                                });

                            }
                            )
                            .error(function () {
                                $scope.messageError = 'Не вдалось завантажити ' + config.file.name;
                                $scope.progressPercentage = parseInt(0);

                            })
                    }
                }
            };
        }]);

