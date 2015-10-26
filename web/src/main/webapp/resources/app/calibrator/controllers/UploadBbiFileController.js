/**
 * Created by MAX on 25.07.2015.
 */
angular
    .module('employeeModule')

    .controller('UploadBbiFileController', ['$scope', '$rootScope', '$route', '$log', '$modalInstance',
        'calibrationTest', 'Upload', '$timeout', 'parseBbiFile',
        function ($scope, $rootScope, $route, $log, $modalInstance, verification, Upload, $timeout, parseBbiFile) {

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

            $scope.uploaded = false;

            $scope.progressPercentage = 0;

            $scope.fileName;

            $scope.upload = function (files) {
                if (files && files.length) {
                    for (var i = 0; i < files.length; i++) {
                        var file = files[i];
                        Upload.upload({
                            url: '/calibrator/verifications/new/upload?idVerification=' + verification,
                            file: file
                        }).progress(function (evt) {
                            $scope.uploaded = true;
                            $scope.progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        }).success(function (data, status, headers, config) {
                                $timeout(function () {
                                    if (status === 200) {
                                        console.log("Successfully uploaded!")
                                        $scope.messageError = null;
                                        $scope.fileName = config.file.name;
                                        $scope.messageSuccess = 'Ви успішно завантажили файл ' + config.file.name;
                                        console.log('uploadBbiController: ' + $scope.fileName);
                                        parseBbiFile(data);
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

