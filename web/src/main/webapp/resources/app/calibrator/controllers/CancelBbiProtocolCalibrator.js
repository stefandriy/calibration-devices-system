angular
    .module('employeeModule')
    .controller('CancelBbiProtocolCalibrator', ['$scope', '$log', '$modalInstance',
        'verificationId','VerificationServiceCalibrator',  function ($scope, $log, $modalInstance, verificationId,
        VerificationServiceCalibrator) {
            var idVerification = verificationId.data[0];
            $scope.fileName = verificationId.data[1];

            $scope.cancel = function () {
                $modalInstance.close("cancel");
            };

            $scope.submit=function() {

                VerificationServiceCalibrator
                    .deleteBbiProtocol(idVerification)
                    .success(function () {
                        $modalInstance.close("cancel");
                    }).error(function () {
                    $scope.messageError = 'Не вдалось видалити файл' + fileName;

                });
            };


        }]);

