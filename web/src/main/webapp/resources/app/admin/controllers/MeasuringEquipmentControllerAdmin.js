angular
    .module('adminModule')
    .controller(
    'MeasuringEquipmentControllerAdmin',
    [
        '$rootScope',
        '$scope',
        '$modal',
        '$http',
        '$filter',
        'MeasuringEquipmentServiceAdmin',
        'ngTableParams',
        '$translate',
        '$timeout',
        'toaster',
        function ($rootScope, $scope, $modal, $http, $filter, measuringEquipmentServiceAdmin,
                  ngTableParams, $translate, $timeout, toaster) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];

            //for measurement device type
            $scope.selectedDeviceType = {
                name: null
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
            /**
             * Updates the table.
             */
            $rootScope.onTableHandling = function () {
                //if ($scope.tableParams == null) return false; //table not yet initialized
                $scope.tableParams.reload();
            };

            // $rootScope.onTableHandling();

            $scope.isFilter = function () {
                if ($scope.tableParams == null) return false; //table not yet initialized
                var obj = $scope.tableParams.filter();
                for (var i in obj) {
                    if (obj.hasOwnProperty(i) && obj[i]) {
                        return true;
                    }
                }
                return false;
            };

            $scope.tableParams = new ngTableParams({
                    page: 1,
                    count: 5/*,
                     sorting: {
                         condDesignation: 'asc'
                     }*/
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

                        /*params.filter().startDateToSearch = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                         params.filter().endDateToSearch = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");*/

                        measuringEquipmentServiceAdmin.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
                            .success(function (result) {
                                $scope.resultsCount = result.totalItems;
                                $defer.resolve(result.content);
                                params.total(result.totalItems);
                            }, function (result) {
                                $log.debug('error fetching data:', result);
                            });
                    }
                });
            //$rootScope.onTableHandling();

            /**
             * Opens modal window for adding new equipment.
             */
            $scope.openAddCalibrationModuleModal = function () {
                var addCalibrationModuleModal = $modal.open({
                    animation: true,
                    controller: 'MeasuringEquipmentAddModalControllerAdmin',
                    templateUrl: '/resources/app/admin/views/modals/measuring-equipment-add-modal.html',
                    size: 'md',
                    resolve: {
                        calibrationModule: function () {
                            return undefined;
                        }
                    }
                });
                /**
                 * executes when modal closing
                 */
                addCalibrationModuleModal.result.then(function () {
                    $scope.popNotification($filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_ADDED_AGREEMENT'));
                });
            };

            /**
             * Opens modal window for editing agreement
             */
            $scope.openEditCalibrationModuleModal = function (moduleId) {
                measuringEquipmentServiceAdmin.getCalibrationModuleById(moduleId).then(
                    function (calibrationModule) { /* agreement -> calibrationModule */
                        var deviceDTOModal = $modal
                            .open({
                                animation: true,
                                controller: 'MeasuringEquipmentAddModalControllerAdmin',
                                templateUrl: '/resources/app/admin/views/modals/measuring-equipment-add-modal.html',
                                size: 'md',
                                resolve: {
                                    calibrationModule: function () {
                                        return calibrationModule.data;
                                    }
                                }
                            });

                        /**
                         * executes when modal closing
                         */
                        deviceDTOModal.result.then(function () {
                            $scope.popNotification($filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_EDITED_AGREEMENT'));
                        });
                    });

            };

            $scope.disableAgreement = function (id) {
                measuringEquipmentServiceAdmin.disableAgreement(id).then(function () {
                    $scope.popNotification($filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_DISABLED_AGREEMENT'));
                });

                $timeout(function () {
                    console.log('delete with timeout');
                    $rootScope.onTableHandling();
                }, 700);
            };

            $scope.popNotification = function (title, text) {
                toaster.pop('success', title, text);
            };

            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'
            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];


        }]);