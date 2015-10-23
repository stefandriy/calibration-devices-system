angular
    .module('adminModule')
    .controller(
    'AgreementController',
    [
        '$rootScope',
        '$scope',
        '$modal',
        '$http',
        '$filter',
        'AgreementService',
        'ngTableParams',
        '$translate',
        '$timeout',
        function ($rootScope, $scope, $modal, $http, $filter, agreementService, ngTableParams, $translate, $timeout) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];

            //for measurement device type
            $scope.selectedDeviceType = {
                name: null
            };

            /**
             * Opens modal window for adding new agreement.
             */
            $scope.openAddAgreementModal = function () {
                var addAgreementModal = $modal.open({
                    animation: true,
                    controller: 'AgreementAddController',
                    templateUrl: '/resources/app/admin/views/modals/agreement-add-modal.html',
                    size: 'md'
                });
            };

            $scope.deviceTypeData = [
                {
                    id: 'WATER',
                    label: $filter('translate')('WATER')
                },
                {
                    id: 'THERMAL',
                    label: $filter('translate')('THERMAL')
                }
            ];

            /**
             * Localization of multiselect for type of organization
             */
            $scope.setTypeDataLanguage = function () {
                $scope.deviceTypeData[0].label = $filter('translate')('WATER');
                $scope.deviceTypeData[1].label = $filter('translate')('THERMAL');
            };

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
                },
                {
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

                        agreementService.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
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
             * Updates the table.
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
             * Opens modal window for editing category of counter.
             */
            $scope.openEditCategoryCounterModal = function (deviceId) {
                $rootScope.categoryId = deviceId;
                agreementService.getDeviceCategoryById(
                    $rootScope.categoryId).then(
                    function (data) {
                        $rootScope.countersCategory = data;
                        console.log($rootScope.countersCategory);

                        var deviceDTOModal = $modal
                            .open({
                                animation: true,
                                controller: 'CategoryDeviceEditModalController',
                                templateUrl: '/resources/app/admin/views/modals/device-category-edit-modal.html',
                                size: 'md'
                            });
                    });

            };

            $scope.deleteDeviceCategory = function (id) {
                $rootScope.deviceCategoryId = id;
                console.log($rootScope.deviceCategoryId);
                agreementService.deleteDeviceCategory(id);
                $timeout(function () {
                    console.log('delete with timeout');
                    $rootScope.onTableHandling();
                }, 700);
            };

        }]);