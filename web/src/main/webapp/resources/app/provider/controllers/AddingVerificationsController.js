angular
    .module('providerModule')
    .controller('AddingVerificationsController', ['$scope','DataSendingService',
        function ($scope,dataSendingService) {
            $scope.saveVerification = function () {
                    dataSendingService.sendData("/provider/verifications/sendverification/", $scope.form)
                        .success(function () {
                        });
            };
        }]);
