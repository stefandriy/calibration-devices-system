angular
    .module('providerModule')
    .controller('NewVerificationsController', ['$scope', '$modal', 'DataReceivingService', 'DataUpdatingService',
        function ($scope, $modal, dataReceivingService, dataUpdatingService) {

            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageData = [];

            $scope.verificationIds = [];
            $scope.checkedItems = [];

            $scope.onTableHandling = function () {
                dataReceivingService
                    .getData('/provider/verifications/new/' + $scope.currentPage + '/' + $scope.itemsPerPage)
                    .then(function (verifications) {
                        $scope.pageData = verifications.content;
                        $scope.totalItems = verifications.totalItems;
                    });
            };

            $scope.onTableHandling();

            $scope.openDetails = function ($index) {
                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/new-verification-details.html',
                    controller: 'DetailsModalController',
                    size: 'lg',
                    resolve: {
                        verification: function () {
                            return dataReceivingService.getData('/provider/verifications/new/' +
                                $scope.pageData[$index].id)
                                .then(function (verification) {
                                    verification.id = $scope.pageData[$index].id;
                                    verification.initialDate = $scope.pageData[$index].initialDate;
                                    return verification;
                                });
                        }
                    }
                });
            };



            $scope.resolveVerificationId = function (id, $index) {
                if (!$scope.checkedItems[$index]) {
                    $scope.verificationIds[$index] = id;
                    $scope.checkedItems[$index] = true;
                } else {
                    $scope.verificationIds[$index] = undefined;
                    $scope.checkedItems[$index] = false;
                }
            };

            function sendVerification(calibratorId) {
                var dataToSend = {
                    verificationIds: $scope.verificationIds,
                    calibrator: calibratorId
                };
                dataUpdatingService
                    .updateData('/provider/verifications/new/update', dataToSend)
                    .then(function () {
                        $scope.onTableHandling();
                    });
                $scope.verificationIds = [];
            }

            $scope.openSending = function () {
                var moduleInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/verification-sending.html',
                    controller: 'SendingModalController',
                    size: 'sm',
                    resolve: {
                        calibrators: function () {
                            return dataReceivingService.getData('/provider/verifications/new/calibrators')
                                .then(function (calibrators) {
                                    return calibrators;
                                });
                        }
                    }
                });

                moduleInstance.result.then(function (calibrator) {
                    try {
                        if (calibrator.id !== 'undefined' && calibrator.name !== 'undefined') {
                            sendVerification(calibrator);
                            $scope.checkedItems = [];
                        }
                    }
                    catch (err) {
                        $scope.verificationIds = [];
                        $scope.checkedItems = [];
                    }
                    $scope.onTableHandling();
                });
            }
        }]);
