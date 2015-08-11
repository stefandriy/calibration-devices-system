angular
    .module('employeeModule')
    .controller('NewVerificationsControllerCalibrator', ['$scope', '$log',
        '$modal', 'VerificationServiceCalibrator',
        '$rootScope', 'ngTableParams', '$timeout', '$filter', '$window', '$location',
        function ($scope, $log, $modal, verificationServiceCalibrator, $rootScope, ngTableParams, $timeout, $filter, $window, $location) {

            $scope.resultsCount = 0;

            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {
                    date: 'desc'     
                }
                	}, {
                total: 0,
                filterDelay: 1500,
                getData: function ($defer, params) {

                	var sortCriteria = Object.keys(params.sorting())[0];
                	var sortOrder = params.sorting()[sortCriteria];
                	
                	verificationServiceCalibrator.getNewVerifications(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
                    				.success(function (result) {
                    					 $scope.resultsCount=result.totalItems;
                    					$defer.resolve(result.content);
                    					params.total(result.totalItems);
                    				}, function (result) {
                    					$log.debug('error fetching data:', result);
                    				});
                 }
            });
            
            $scope.checkFilters = function () {       	 
                var obj = $scope.tableParams.filter();
                for (var i in obj) {
                    if (obj.hasOwnProperty(i) && obj[i]) {
                        return true;
                    }
                }
                return false;         
            };
            
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

            $scope.openTests = function (verificationId) {
                $log.debug("inside");
                var url = $location.path('/calibrator/verifications/calibration-test/').search({param: verificationId});
            }

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

                var modalInstance =  $modal.open({
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
                modalInstance.result.then(function () {
                    $scope.tableParams.reload();
                });
            };

            $scope.cancelBbiFile = function (verification) {
                var idVerification = verification;
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/cancel-bbiFile.html',
                    controller: 'CancelBbiProtocolCalibrator',
                    size: 'md',
                    resolve: {
                        verificationId: function () {
                            return verificationServiceCalibrator.cancelUploadFile(idVerification)
                                .success(function (bbiName) {
                                    return bbiName;
                                }
                            );
                        }
                    }
                });
                modalInstance.result.then(function () {
                    $scope.tableParams.reload();
                });
            };


            /**
             *  Date picker and formatter setup
             *
             */
            $scope.openState = {};
            $scope.openState.isOpen = false;

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openState.isOpen = true;
            };


            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'
            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

            $scope.initiateVerification = function () {

                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/modals/initiate-verification.html',
                    controller: 'AddingVerificationsControllerProvider',
                    size: 'lg',

                });
            };

            $scope.removeCalibratorEmployee = function (verifId) {
                var dataToSend = {
                    idVerification: verifId
                };
                $log.info(dataToSend);
                verificationServiceCalibrator.cleanCalibratorEmployeeField(dataToSend)
                    .success(function () {
                        $scope.tableParams.reload();
                    });
            };

            $scope.addCalibratorEmployee = function (verifId) {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/employee/adding-providerEmployee.html',
                    controller: 'CalibratorEmployeeControllerCalibrator',
                    size: 'md',
                    windowClass: 'xx-dialog',
                    resolve: {
                        calibratorEmploy: function () {
                            return verificationServiceCalibrator.getCalibrators()
                                .success(function (calibrators) {
                                    return calibrators;
                                }
                            );
                        }
                    }
                })
                /**
                 * executes when modal closing
                 */
                modalInstance.result.then(function (formData) {
                    idVerification = 0;
                    var dataToSend = {
                        idVerification: verifId,
                        employeeCalibrator: formData.provider
                    };
                    $log.info(dataToSend);
                    verificationServiceCalibrator
                        .sendEmployeeCalibrator(dataToSend)
                        .success(function () {
                            $scope.tableParams.reload();
                        });
                });
            };

        }]);
