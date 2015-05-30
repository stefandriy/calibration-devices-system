angular
    .module('providerModule')
    .controller('AddingVerificationsController', ['$scope','DataSendingService',
        function ($scope,dataSendingService) {
            $scope.saveVerification = function () {
                    dataSendingService.sendData("/provider/verifications/send", $scope.form)
                        .then(function () {
                        });
            };
        }]);
