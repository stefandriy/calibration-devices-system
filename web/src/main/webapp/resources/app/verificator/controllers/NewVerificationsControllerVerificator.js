angular
    .module('employeeModule')
    .controller('NewVerificationsControllerVerificator', ['$scope', '$log', '$modal', 'VerificationServiceVerificator', '$rootScope',
        'ngTableParams', '$filter', '$timeout',
        function ($scope, $log, $modal, VerificationServiceVerificator, $rootScope, ngTableParams, $filter, $timeout) {

            $scope.search = {
                idText:null,
                formattedDate :null,
                lastNameText:null,
                streetText: null,
                status: null
            }

            $scope.clearAll = function(){
                $scope.search.idText=null;
                $scope.search.formattedDate=null;
                $scope.dt = null;
                $scope.search.lastNameText=null;
                $scope.search.streetText=null;
                $scope.search.status = null;
                $scope.tableParams.reload();
            }

            $scope.clearId = function () {
                $scope.search.idText = null;
                $scope.tableParams.reload();
            }
            $scope.clearLastName = function () {
                $scope.search.lastNameText = null;
                $scope.tableParams.reload();
            }
            $scope.clearStreet = function () {
                $scope.search.streetText = null;
                $scope.tableParams.reload();
            }
            $scope.clearStatus = function () {
                $scope.search.status = null;
                $scope.tableParams.reload();
            }

            var promiseSearchTimeOut;
            $scope.doSearch = function() {
                promiseTimeOut = $timeout(function() {
                    $scope.tableParams.reload();
                }, 1500);
            }

            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function ($defer, params) {

                    VerificationServiceVerificator.getNewVerifications(params.page(), params.count(), $scope.search)
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

                VerificationServiceVerificator.markVerificationAsRead(dataToSend).success(function () {
                    $rootScope.$broadcast('verification-was-read');
                    $scope.tableParams.reload();
                });
            };

            $scope.openDetails = function (verifId, verifDate, verifReadStatus) {
                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/verificator/views/modals/new-verification-details.html',
                    controller: 'DetailsModalControllerVerificator',
                    size: 'lg',
                    resolve: {
                        response: function () {
                            return VerificationServiceVerificator.getNewVerificationDetails(verifId)
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

            $scope.testReview = function (verifId) {
                $log.debug(verifId);
                $modal.open({

                    animation: true,
                    templateUrl: '/resources/app/verificator/views/modals/testReview.html',
                    controller: 'CalibrationTestReviewControllerVerificator',
                    size: 'lg',
                    resolve: {
                        response: function () {
                            return VerificationServiceVerificator.getCalibraionTestDetails(verifId)
                                .success(function (calibrationTest) {
                                    //calibrationTest.id = verifId;
                                    $log.debug('CalibrationTest');
                                    $log.debug(calibrationTest);
                                    return calibrationTest;
                                })
                                .error(function () {
                                    console.log('ERROR');
                                });
                        }
                    }
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
                        templateUrl: '/resources/app/verificator/views/modals/verification-sending.html',
                        controller: 'SendingModalControllerVerificator',
                        size: 'md',
                        resolve: {
                            response: function () {
                                return VerificationServiceVerificator.getProviders()
                                    .success(function (providers) {
                                        return providers;
                                    });
                            }
                        }
                    });

                    //executes when modal closing
                    modalInstance.result.then(function (formData) {

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            idsOfProviders: formData.provider.id
                        };

                        $log.info(dataToSend);
                        VerificationServiceVerificator
                            .sendVerificationsToProvider(dataToSend)
                            .success(function () {
                                $scope.tableParams.reload();
                                $rootScope.$broadcast('verification-sent-to-provider');
                            });

                        $scope.idsOfVerifications = [];
                        $scope.checkedItems = [];
                    });
                } else {
                    $scope.isClicked = true;
                }
            };
            //For NOT_OK!!!
            $scope.openSendingModalNotOK = function () {
                if (!$scope.allIsEmpty) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: '/resources/app/verificator/views/modals/verification-sending.html',
                        controller: 'SendingModalControllerVerificator',
                        size: 'md',
                        resolve: {
                            response: function () {
                                return VerificationServiceVerificator.getProviders()
                                    .success(function (providers) {
                                        return providers;
                                    });
                            }
                        }
                    });

                    //executes when modal closing
                    modalInstance.result.then(function (formData) {

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            idsOfProviders: formData.provider.id
                        };

                        $log.info(dataToSend);
                        VerificationServiceVerificator
                            .sendVerificationNotOkStatus(dataToSend)
                            .success(function () {
                                $scope.tableParams.reload();
                                $rootScope.$broadcast('verification-sent-to-provider');
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

            $scope.changeDateToSend = function (val) {

                if (angular.isUndefined(val)) {
                    $scope.search.formattedDate = null;
                    $scope.tableParams.reload();
                } else {
                    var datefilter = $filter('date');
                    $scope.search.formattedDate = datefilter(val, 'dd-MM-yyyy');
                    $scope.tableParams.reload();
                }
            }

        }]);
