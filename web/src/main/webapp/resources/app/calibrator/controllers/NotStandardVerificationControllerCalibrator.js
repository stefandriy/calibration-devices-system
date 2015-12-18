angular
    .module('employeeModule')
    .controller('NotStandardVerificationControllerCalibrator', [
        '$rootScope',
        '$scope',
        '$log',
        '$modal',
        'NotStandardVerificationCalibratorService',
        'ngTableParams',
        '$timeout',
        '$filter',
        'toaster',
                function ($rootScope, $scope, $log, $modal, verificationService, ngTableParams, $timeout, $filter, toaster ) {
                    $scope.totalItems = 0;
                    $scope.pageContent = [];
                    $scope.tableParams = new ngTableParams({
                        page: 1,
                        count: 10,
                        sorting: {
                            id: 'desc'
                        }
                    }, {
                        total: 0,
                        filterDelay: 1500,
                        getData: function ($defer, params) {

                            verificationService.getPage(params.page(), params.count())
                                .success(function (result) {
                                    $scope.resultsCount = result.totalItems;
                                    $defer.resolve(result.content);
                                    params.total(result.totalItems);
                                }, function (result) {
                                    $log.debug('error fetching data:', result);
                                });
                        }
                    });
                    $scope.idsOfVerifications = [];
                    $scope.checkedItems = [];
                    $scope.allIsEmpty = true;
                    $scope.idsOfCalibrators = null;


                    /**
                     * push verification id to array
                     */

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
                        console.log("dataToSend" + $scope.idsOfVerifications);
                        if (!$scope.allIsEmpty) {
                            var modalInstance = $modal.open({
                                animation: true,
                                backdrop : 'static',
                                templateUrl: 'resources/app/calibrator/views/modals/verification-sending-to-provider.html',
                                controller: 'NotStandardVerificationSendingControllerCalibrator',
                                size: 'md',
                                resolve: {
                                    response: function () {
                                        return verificationService.getProviders()
                                            .success(function (data) {
                                                    return data;
                                                }
                                            );
                                    }
                                }
                            });

                            /**
                             * executes when modal closing
                             */
                            modalInstance.result.then(function (formData) {
                                console.log("dataToSend" + $scope.idsOfVerifications, formData.provider.id);
                                var dataToSend = {
                                    idsOfVerifications: $scope.idsOfVerifications,
                                    organizationId: formData.provider.id
                                };

                                verificationService.sendVerification(dataToSend)
                                    .success(function () {
                                        $log.debug('success sending');
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

                    /**
                     * check if idsOfVerifications array is empty
                     */
                    var checkForEmpty = function () {
                        $scope.allIsEmpty = $scope.idsOfVerifications.length === 0;
                    };
                }]);


