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
        '$translate',
        '$timeout',
        function ($rootScope, $scope, $modal, $http, devicesService, ngTableParams, $translate, $timeout) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];

            //for measurement device type
            $scope.selectedDeviceType = {
                name: null
            }

            $scope.deviceTypeData = [
                /*{
                 id: 'ELECTRICAL',
                 label: null
                 },
                 {
                 id: 'GASEOUS',
                 label: null
                 },*/
                {
                    id: 'WATER',
                    label: null
                },
                {
                    id: 'THERMAL',
                    label: null
                }
            ];

            /**
             * Localization of multiselect for type of organization
             */
            $scope.setTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {
                    // $scope.deviceTypeData[0].label = 'Електричний';
                    //$scope.deviceTypeData[1].label = 'Газовий';
                    $scope.deviceTypeData[0].label = 'Холодна вода';
                    $scope.deviceTypeData[1].label = 'Гаряча вода';
                } else if (lang === 'eng') {
                    //$scope.deviceTypeData[0].label = 'Electrical';
                    // $scope.deviceTypeData[1].label = 'Gaseous';
                    $scope.deviceTypeData[0].label = 'Cold water';
                    $scope.deviceTypeData[1].label = 'Hot water';
                }
            };

            $scope.setTypeDataLanguage();

            $scope.clearAll = function () {
                $scope.selectedDeviceType.name = null;
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

                    if ($scope.selectedDeviceType.name != null) {
                        params.filter().deviceType = $scope.selectedDeviceType.name.id;
                    }
                    else {
                        params.filter().deviceType = null; //case when the filter is cleared with a button on the select
                    }

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
            $scope.openAddCategoryCounterModal = function() {
                var addCategoryCounter = $modal.open({
                    animation : true,
                    controller : 'CategoryDeviceAddModalController',
                    templateUrl : '/resources/app/admin/views/modals/device-category-add-modal.html',
                    size: 'md'
                });
            };

            /**
             * Opens modal window for editing category of counter.
             */
            $scope.openEditCategoryCounterModal = function(
                deviceId) {
                $rootScope.categoryId = deviceId;
                devicesService.getDeviceCategoryById(
                    $rootScope.categoryId).then(
                    function(data) {
                        $rootScope.countersCategory = data;
                        console.log($rootScope.countersCategory);

                        var deviceDTOModal = $modal
                            .open({
                                animation : true,
                                controller : 'CategoryDeviceEditModalController',
                                templateUrl : '/resources/app/admin/views/modals/device-category-edit-modal.html',
                                size: 'md'
                            });
                    });

            };

            $scope.deleteDeviceCategory = function (id) {
                $rootScope.deviceCategoryId = id;
                console.log($rootScope.deviceCategoryId);
                devicesService.deleteDeviceCategory(id);
                $timeout(function() {
                    console.log('delete with timeout');
                    $rootScope.onTableHandling();
                }, 700);
            };

        }]);