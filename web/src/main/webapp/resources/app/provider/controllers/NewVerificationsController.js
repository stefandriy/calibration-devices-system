angular
    .module('providerModule')
    .controller('NewVerificationsController', ['$scope', '$log', '$modal', 'VerificationService',
        function ($scope, $log, $modal, verificationService) {

            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 10;
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
            $scope.allIsEmpty = true;

            $scope.resolveVerificationId = function (id, $index) {
                if (!$scope.checkedItems[$index]) {
                    $scope.idsOfVerifications[$index] = id;
                    $scope.checkedItems[$index] = true;
                } else {
                    $scope.idsOfVerifications[$index] = undefined;
                    $scope.checkedItems[$index] = false;
                }
                $scope.idsOfVerifications = removeEmptyArrayElements($scope.idsOfVerifications);
                checkForEmpty();
            };


            $scope.openSendingModal = function () {
                if (!$scope.allIsEmpty) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: '/resources/app/provider/views/verification-sending.html',
                        controller: 'SendingModalController',
                        size: 'md',
                        resolve: {
                            response: function () {
                                return verificationService.getCalibrators()
                                    .success(function (calibrators) {
                                        return calibrators;
                                    });
                            }
                        }
                    });

                    //executes when modal closing
                    modalInstance.result.then(function (calibrator) {
                        $log.info(calibrator);

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            calibrator: calibrator
                        };

                        $log.info(dataToSend);

                        verificationService
                            .sendVerificationsToCalibrator(dataToSend)
                            .success(function () {
                                $scope.onTableHandling();
                            });
                        $scope.idsOfVerifications = [];
                        $scope.checkedItems = [];
                    });
                } else {
                    $scope.isClicked = true;
                }
            };

            var checkForEmpty = function () {
                $scope.allIsEmpty = $scope.idsOfVerifications.length === 0;
            };
        }]);


var removeEmptyArrayElements = function (arr) {
    return arr
        .filter(function (elem) {
            return elem !== null && elem !== undefined
        });
};
