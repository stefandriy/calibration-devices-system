angular
    .module('employeeModule')
    .controller('NewVerificationsControllerCalibrator', ['$scope', '$log',
        '$modal', 'VerificationServiceCalibrator',
        '$rootScope', 'ngTableParams',
        function ($scope, $log, $modal, verificationServiceCalibrator, $rootScope, ngTableParams) {


            $scope.search = {
                text: null,
                type: null
            }

            $scope.clearInput = function () {
                $scope.search.text = null;
            }

            $scope.doSearch = function () {
                $scope.tableParams.reload();
            }

            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function ($defer, params) {

                    verificationServiceCalibrator.getNewVerifications(params.page(), params.count(), $scope.search.type, $scope.search.text)
                        .success(function (result) {
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                }
            });


            $scope.markAsRead = function (id) {
                var dataToSend = {
                    verificationId: id,
                    readStatus: 'READ'
                };
                verificationServiceCalibrator.markVerificationAsRead(dataToSend).success(function () {
                    $rootScope.$broadcast('verification-was-read');
                    $scope.tableParams.reload();
                });
            };


            $scope.openDetails = function (verifId, verifDate, verifReadStatus) {
                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/new-verification-details.html',
                    controller: 'DetailsModalControllerCalibrator',
                    size: 'lg',
                    resolve: {
                        response: function () {
                            return verificationServiceCalibrator.getNewVerificationDetails(verifId)
                                .success(function (verification) {
                                    verification.id = verifId;
                                    verification.initialDate = verifDate;
                                    if (verifReadStatus == 'UNREAD') {
                                        $scope.markAsRead(verifId);
                                    }
                                    return verification;
                                });
                        }
                    }
                });
            };

            /**
             * Opens modal window for adding new calibration-test.
             */
            $scope.openAddCalibrationTestModal = function () {
                var addTestModal = $modal
                    .open({
                        animation: true,
                        controller: 'CalibrationTestAddModalControllerCalibrator',
                        templateUrl: '/resources/app/calibrator/views/modals/calibration-test-add-modal.html',
                    });
            };

            $scope.idsOfVerifications = [];
            $scope.checkedItems = [];
            $scope.allIsEmpty = true;

            $scope.resolveVerificationId = function (id) {

                var index = $scope.idsOfVerifications.indexOf(id);
                if (index === -1) {
                    $scope.idsOfVerifications.push(id);
                    index = $scope.idsOfVerifications.indexOf(id);
                }

                if (!$scope.checkedItems[index]) {
                    $scope.idsOfVerifications.splice(index, 1, id);
                    $scope.checkedItems.splice(index, 1, true);
                } else {
                    $scope.idsOfVerifications.splice(index, 1);
                    $scope.checkedItems.splice(index, 1);
                }
                checkForEmpty();
            };


            $scope.openSendingModal = function () {
                if (!$scope.allIsEmpty) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: '/resources/app/calibrator/views/modals/verification-sending.html',
                        controller: 'SendingModalControllerCalibrator',
                        size: 'md',
                        resolve: {
                            response: function () {
                                return verificationServiceCalibrator.getVerificators()
                                    .success(function (verificators) {
                                        $log.debug(verificators);
                                        return verificators;
                                    });
                            }
                        }
                    });

                    //executes when modal closing
                    modalInstance.result.then(function (verificator) {

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            verificatorId: verificator.id
                        };

                        $log.debug(dataToSend);
                        $log.debug(verificator);

                        verificationServiceCalibrator
                            .sendVerificationsToCalibrator(dataToSend)
                            .success(function () {
                                $scope.tableParams.reload();
                                $rootScope.$broadcast('verification-sent-to-verifikator');
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


            $scope.uploadBbiFile = function (idVerification) {

                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/upload-bbiFile.html',
                    controller: 'UploadBbiFileController',
                    size: 'lg',
                    resolve: {
                        verification: function () {
                            return idVerification;

                        }
                    }
                });
            };


        }]);
