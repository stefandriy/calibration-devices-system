angular
    .module('employeeModule')
    .controller('DigitalVerificationProtocolsControllerCalibrator', ['$rootScope','ngTableParams', '$scope', '$modal', 'DigitalVerificationProtocolsServiceCalibrator',
        function ($rootScope,ngTableParams, $scope, $modal, digitalVerificationProtocolsServiceCalibrator) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];


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
            },
            $scope.sentProtocols = function () {
                //  must be chenging status code here
                     $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/send-protocols.html',
                    controller: function ($modalInstance) {
                        this.ok = function () {
                            $modalInstance.close();
                        }
                    },
                    controllerAs: 'successController',
                    size: 'md'
                });
            }
            );

        }]);




