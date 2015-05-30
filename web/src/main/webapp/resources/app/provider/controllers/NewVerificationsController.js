angular
    .module('providerModule')
    .controller('NewVerificationsController', ['$scope', '$log', '$modal', 'DataReceivingService',
        'DataUpdatingService',
        function ($scope, $log, $modal, dataReceivingService, dataUpdatingService) {

            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageData = [];

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

            $scope.idsOfVerifications = [];
            $scope.checkedItems = [];

            $scope.resolveVerificationId = function (id, $index) {
                if (!$scope.checkedItems[$index]) {
                    $scope.idsOfVerifications[$index] = id;
                    $scope.checkedItems[$index] = true;
                } else {
                    $scope.idsOfVerifications[$index] = undefined;
                    $scope.checkedItems[$index] = false;
                }
            };

            function sendVerification(calibratorId) {

                var dataToSend = {
                    idsOfVerifications: removeEmptyArrayElements($scope.idsOfVerifications),
                    calibrator: calibratorId
                };
                $log.info(dataToSend);
                dataUpdatingService
                    .updateData('/provider/verifications/new/update', dataToSend)
                    .then(function () {
                        $scope.onTableHandling();
                    });
                $scope.idsOfVerifications = [];
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
                        $scope.idsOfVerifications = [];
                        $scope.checkedItems = [];
                    }
                    $scope.onTableHandling();
                });
            }
        }]);

var removeEmptyArrayElements = function (arr) {

    return arr
        .filter(function (elem) {
            return elem !== null
        });
};
