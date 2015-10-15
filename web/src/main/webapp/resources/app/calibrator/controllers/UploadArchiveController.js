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

            //$scope.file = undefined;

            //$scope.$watch('file', function () {
            //    $scope.upload($scope.file);
            //});

            $scope.uploaded = false;

            $scope.progressPercentage = 0;


            $scope.upload = function (file) {
                if (file) {
                        Upload.upload({
                            url: '/calibrator/verifications/new/upload?idVerification=',
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
            };

        }]);

