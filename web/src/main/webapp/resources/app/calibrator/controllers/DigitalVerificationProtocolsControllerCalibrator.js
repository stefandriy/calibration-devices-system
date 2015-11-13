angular
    .module('employeeModule')
    .controller('DigitalVerificationProtocolsControllerCalibrator', ['$scope', '$log', '$modal', 'DigitalVerificationProtocolsServiceCalibrator',
        '$rootScope', 'ngTableParams',

        function ($scope, $log, $modal, digitalVerificationProtocolsServiceCalibrator, $rootScope, ngTableParams) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];
            $scope.checked = false;

            $scope.clearAll = function () {
                $scope.selectedStatus.name = null;
                $scope.tableParams.filter({});
            }

            $scope.doSearch = function () {
                $scope.tableParams.reload();
            }

            $scope.selectedStatus = {
                name: null
            }
            $scope.statusData = [
                {id: 'TEST_COMPLETED', label: null},
            ];

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

                    if ($scope.selectedStatus.name != null) {
                        params.filter().status = $scope.selectedStatus.name.id;
                    }

                    digitalVerificationProtocolsServiceCalibrator.getProtocols(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
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
                        templateUrl: '/resources/app/calibrator/views/modals/protocols-sending.html',
                        controller: 'SendingModalControllerCalibrator',
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


                        digitalVerificationProtocolsServiceCalibrator
                            .sendProtocolsToVerificator(dataToSend)
                            .success(function () {
                                $log.debug('success sending');
                                $scope.tableParams.reload();
                                $rootScope.$broadcast('verification-sent-to-calibrator');
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


        }])
