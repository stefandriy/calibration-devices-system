angular
    .module('employeeModule')
    .controller('DigitalVerificationProtocolsControllerCalibrator', ['$scope', '$log', '$modal',
        'DigitalVerificationProtocolsServiceCalibrator', '$rootScope', 'ngTableParams','$filter', 'toaster',

        function ($scope, $log, $modal, digitalVerificationProtocolsServiceCalibrator, $rootScope, ngTableParams,
                  $filter, toaster) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];
            $scope.checked = false;

            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {
                    date: 'desc'
                }
            }, {
                total: 0,
                getData: function ($defer, params) {

                    digitalVerificationProtocolsServiceCalibrator.getProtocols(params.page(), params.count())
                        .success(function (result) {
                            $scope.totalItems = result.totalItems;
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                }
            })
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
                if (!$scope.allIsEmpty) {
                    var modalInstance = $modal.open({
                        animation: true,
                        backdrop : 'static',
                        templateUrl: 'resources/app/calibrator/views/modals/protocols-sending.html',
                        controller: 'DigitalProtocolsSendingModalControllerCalibrator',
                        size: 'md',
                        resolve: {
                            response: function () {
                                return digitalVerificationProtocolsServiceCalibrator.getVerificators()
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

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            organizationId: formData.verificator.id
                        };

                        digitalVerificationProtocolsServiceCalibrator.sendProtocols(dataToSend)
                            .success(function () {
                                $log.debug('success sending');
                                $scope.tableParams.reload();
                                $rootScope.$broadcast('verification-sent-to-verificator');
                                toaster.pop('success', $filter('translate')('INFORMATION'),
                                    $filter('translate')('SUCCESS_SENT'));
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
