angular
    .module('adminModule')
    .controller(
    'DeviceController',
    [
        '$rootScope',
        '$scope',
        '$modal',
        '$http',
        'DevicesService',
        'ngTableParams',
        function ($rootScope, $scope, $modal, $http, devicesService, ngTableParams) {
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


                    devicesService.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
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
                /*devicesService
                    .getPage($scope.currentPage, $scope.itemsPerPage, $scope.searchData)
                    .then(function (data) {
                        $scope.pageContent = data.content;
                        console.log(data.content);
                        $scope.totalItems = data.totalItems;
                    });*/
            };

            $rootScope.onTableHandling();

            /**
             * Opens modal window for adding new category of counters.
             */
            $scope.openAddCategoryCounterModal = function() {
                var addCategoryCounter = $modal.open({
                    animation : true,
                    controller : 'CategoryCounterAddModalController',
                    templateUrl : '/resources/app/admin/views/modals/organization-add-modal.html',
                    size: 'lg'
                });
            };

            /**
             * Opens modal window for editing category of counter.
             */
            $scope.openEditCategoryCounterModal = function(
                deviceId) {
                $rootScope.categoryId = deviceId;
                devicesService.getDeviceById(
                    $rootScope.categoryId).then(
                    function(data) {
                        $rootScope.countersCategory = data;
                        console.log($rootScope.countersCategory);

                        var deviceDTOModal = $modal
                            .open({
                                animation : true,
                                controller : 'CategoryCounterEditModalController',
                                templateUrl : '/resources/app/admin/views/modals/organization-edit-modal.html',
                                size: 'lg'
                            });
                    });

            };

        }]);