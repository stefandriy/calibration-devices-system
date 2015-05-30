angular
    .module('providerModule')
    .controller('AddingVerificationsController', ['$scope','DataSendingService', 'DataReceivingService',
        function ($scope,dataSendingService, dataReceivingService) {
            $scope.saveVerification = function () {
                    dataSendingService.sendData("/provider/applications/send", $scope.form)
                        .then(function () {
                        });
            };
            $scope.localities = [];
            dataReceivingService.getData("/initial-verification/localities").then(function (localities) {
                $scope.localities = localities;
            });
        }]);
