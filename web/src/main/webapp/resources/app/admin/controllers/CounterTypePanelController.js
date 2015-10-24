angular
    .module('adminModule')
    .controller(
    'CounterTypePanelController',
    [
        '$rootScope',
        '$scope',
        '$modal',
        '$http',
        'CounterTypeService',
        'ngTableParams',
        '$timeout',
        function ($rootScope, $scope, $modal, $http, counterTypeService, ngTableParams, $timeout) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];

            $scope.clearAll = function () {
                $scope.tableParams.filter({});
            };

            $scope.doSearch = function () {
                $scope.tableParams.reload();
            };

            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {
                    id: 'desc'
                }
            }, {
                total: 0,
                filterDelay: 10000,
                getData: function ($defer, params) {

                    var sortCriteria = Object.keys(params.sorting())[0];
                    var sortOrder = params.sorting()[sortCriteria];

                    counterTypeService.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
                        .success(function (result) {
                            $scope.resultsCount = result.totalItems;
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                }
            });
            /**
             * Updates the table with device.
             */
            $rootScope.onTableHandling = function () {
                $scope.tableParams.reload();
            };

            $rootScope.onTableHandling();

            $scope.isFilter = function () {
                var obj = $scope.tableParams.filter();
                for (var i in obj) {
                    if (obj.hasOwnProperty(i) && obj[i]) {
                        return true;
                    }
                }
                return false;
            };
            /**
             * Opens modal window for adding new category of counters.
             */
            $scope.openAddCounterTypeModal = function() {
                var addCategoryCounter = $modal.open({
                    animation : true,
                    controller : 'CounterTypeAddController',
                    templateUrl : '/resources/app/admin/views/modals/counter-type-add-modal.html',
                    size: 'lg',
                    resolve: {
                        devices: function () {
                            console.log(counterTypeService.findAllDevices());
                            return counterTypeService.findAllDevices();
                        }
                    }
                });
            };

            /**
             * Opens modal window for editing category of counter.
             */
            $scope.openEditCounterTypeModal = function(
                counterTypeId) {
                $rootScope.counterTypeId = counterTypeId;
                counterTypeService.getCounterTypeById(
                    $rootScope.counterTypeId).then(
                    function(data) {
                        $rootScope.countersType = data;
                        console.log($rootScope.countersType);

                        var counterTypeDTOModal = $modal
                            .open({
                                animation : true,
                                controller : 'CounterTypeEditController',
                                templateUrl : '/resources/app/admin/views/modals/counter-type-edit-modal.html',
                                size: 'lg',
                                resolve: {
                                    devices: function () {
                                        console.log(counterTypeService.findAllDevices());
                                        return counterTypeService.findAllDevices();
                                    }
                                }
                            });
                    });

            };

            $scope.deleteCounterType = function (id) {
                $rootScope.counterTypeId = id;
                console.log($rootScope.counterTypeId);
                counterTypeService.deleteCounterType(id);
                $timeout(function() {
                    console.log('delete with timeout');
                    $rootScope.onTableHandling();
                }, 700);
            };

        }]);