angular
    .module('providerModule')
    .controller('NewVerificationsController', ['$scope', '$log', '$modal', 'VerificationService',
        function ($scope, $log, $modal, verificationService) {

            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageData = [];

            $scope.onTableHandling = function () {
                verificationService
                    .getNewVerifications($scope.currentPage, $scope.itemsPerPage)
                    .success(function (verifications) {
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
                        response: function () {
                            return verificationService.getNewVerificationDetails(
                                $scope.pageData[$index].id)
                                .success(function (verification) {
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

            function sendVerifications(calibratorId) {

                var dataToSend = {
                    idsOfVerifications: removeEmptyArrayElements($scope.idsOfVerifications),
                    calibrator: calibratorId
                };
                $log.info(dataToSend);
                verificationService
                    .sendVerificationsToCalibrator(dataToSend)
                    .success(function () {
                        $scope.onTableHandling();
                    });
                $scope.idsOfVerifications = [];
            }

            $scope.openSendingModal = function () {
                var moduleInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/verification-sending.html',
                    controller: 'SendingModalController',
                    size: 'sm',
                    resolve: {
                        response: function () {
                            return verificationService.getCalibrators()
                                .success(function (calibrators) {
                                    return calibrators;
                                });
                        }
                    }
                });

                moduleInstance.result.then(function (calibrator) {
                    try {
                        if (calibrator.id !== 'undefined' && calibrator.name !== 'undefined') {
                            sendVerifications(calibrator);
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
            return elem !== null && elem !== undefined
        });
};
