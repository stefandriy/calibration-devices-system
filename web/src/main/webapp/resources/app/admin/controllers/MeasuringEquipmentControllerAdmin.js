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

            /**
             * Date
             */
            $scope.clearDate = function () {
                //daterangepicker doesn't support null dates
                $scope.myDatePicker.pickerDate = $scope.defaultDate;
                //setting corresponding filters with 'all time' range
                $scope.tableParams.filter().startDateToSearch = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                $scope.tableParams.filter().endDateToSearch = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");
            };

            $scope.myDatePicker = {};
            $scope.myDatePicker.pickerDate = null;
            $scope.defaultDate = null;

            $scope.initDatePicker = function (date) {
                /**
                 *  Date picker and formatter setup
                 *
                 */
                /*TODO: i18n*/
                $scope.myDatePicker.pickerDate = {
                    startDate: (date ? moment(date, "YYYY-MM-DD") : moment()),
                    //earliest day of  all the verifications available in table
                    //we should reformat it here, because backend currently gives date in format "YYYY-MM-DD"
                    endDate: moment() // current day
                };

                if ($scope.defaultDate == null) {
                    //copy of original daterange
                    $scope.defaultDate = angular.copy($scope.myDatePicker.pickerDate);
                }
                moment.locale('uk'); //setting locale for momentjs library (to get monday as first day of the week in ranges)
                $scope.opts = {
                    format: 'DD-MM-YYYY',
                    showDropdowns: true,
                    locale: {
                        firstDay: 1,
                        fromLabel: 'Від',
                        toLabel: 'До',
                        applyLabel: "Прийняти",
                        cancelLabel: "Зачинити",
                        customRangeLabel: "Обрати самостійно"
                    },
                    ranges: {
                        'Сьогодні': [moment(), moment()],
                        'Вчора': [moment().subtract(1, 'day'), moment().subtract(1, 'day')],
                        'Цього тижня': [moment().startOf('week'), moment().endOf('week')],
                        'Цього місяця': [moment().startOf('month'), moment().endOf('month')],
                        'Попереднього місяця': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                        'За увесь час': [$scope.defaultDate.startDate, $scope.defaultDate.endDate]
                    },
                    eventHandlers: {}
                };
            };
            // $scope.initDatePicker();

            $scope.showPicker = function ($event) {
                angular.element("#datepickerfield").trigger("click");
            };

            $scope.isDateDefault = function () {
                var pickerDate = $scope.myDatePicker.pickerDate;

                if (pickerDate == null || $scope.defaultDate == null) { //moment when page is just loaded
                    return true;
                }
                if (pickerDate.startDate.isSame($scope.defaultDate.startDate, 'day') //compare by day
                    && pickerDate.endDate.isSame($scope.defaultDate.endDate, 'day')) {
                    return true;
                }
                return false;
            };

            $scope.selectedDeviceType = {
                name: null
            };

            $scope.selectedModuleType = {
                name: null
            };

            $scope.deviceTypeData = [
                {
                    type: 'WATER',
                    label: $filter('translate')('WATER')
                },
                {
                    type: 'THERMAL',
                    label: $filter('translate')('THERMAL')
                }
            ];

            $scope.moduleTypeData = [
                {
                    type: 'INSTALLATION_FIX',
                    label: $filter('translate')('INSTALLATION_FIX')
                },
                {
                    type: 'INSTALLATION_PORT',
                    label: $filter('translate')('INSTALLATION_PORT')
                }
            ];

            $scope.setTypeDataLanguage = function () {
                $scope.deviceTypeData[0].label = $filter('translate')('WATER');
                $scope.deviceTypeData[1].label = $filter('translate')('THERMAL');
                $scope.moduleTypeData[0].label = $filter('translate')('INSTALLATION_FIX');
                $scope.moduleTypeData[1].label = $filter('translate')('INSTALLATION_PORT');
            };

            $scope.clearAll = function () {
                $scope.selectedDeviceType.name = null;
                $scope.selectedModuleType.name = null;
                $scope.tableParams.filter({});
            };
            /**
             * Updates the table.
             */
            $rootScope.onTableHandling = function () {
                $scope.tableParams.reload();
            };

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
                    count: 5,
                    sorting: {
                        moduleId: 'desc'
                    }
                },
                {
                    total: 0,
                    filterDelay: 10000,
                    getData: function ($defer, params) {

                        var sortCriteria = Object.keys(params.sorting())[0];
                        var sortOrder = params.sorting()[sortCriteria];

                        if ($scope.selectedDeviceType.name != null) {
                            params.filter().deviceType = $scope.selectedDeviceType.name.type;
                        }
                        else {
                            params.filter().deviceType = null; //case when the filter is cleared with a button on the select
                        }

                        if ($scope.selectedModuleType.name != null) {
                            params.filter().moduleType = $scope.selectedModuleType.name.type;
                        }
                        else {
                            params.filter().moduleType = null; //case when the filter is cleared with a button on the select
                        }

                        /*if ($scope.myDatePicker.pickerDate.startDate != undefined
                            && $scope.myDatePicker.pickerDate.startDate != null
                            && $scope.myDatePicker.pickerDate.endDate != undefined
                            && $scope.myDatePicker.pickerDate.endDate != null) {
                            params.filter().startDateToSearch = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                            params.filter().endDateToSearch = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");
                        }*/

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

            /**
             * Opens modal window for adding new calibration module.
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
                    $scope.popNotification($filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_ADDED_CALIBRATION_MODULE'));
                });
            };

            /**
             * Opens modal window for editing calibration module
             */
            $scope.openEditCalibrationModuleModal = function (moduleId) {
                measuringEquipmentServiceAdmin.getCalibrationModuleById(moduleId).then(
                    function (calibrationModule) {
                        var deviceDTOModal = $modal
                            .open({
                                animation: true,
                                controller: 'MeasuringEquipmentAddModalControllerAdmin',
                                templateUrl: 'resources/app/admin/views/modals/measuring-equipment-add-modal.html',
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
                            $scope.popNotification($filter('translate')('INFORMATION'),
                                $filter('translate')('SUCCESSFUL_EDITED_CALIBRATION_MODULE'));
                        });
                    });

            };

            $scope.disableCalibrationModule = function (id) {
                measuringEquipmentServiceAdmin.disableCalibrationModule(id).then(function () {
                    $scope.popNotification($filter('translate')('INFORMATION'),
                        $filter('translate')('SUCCESSFUL_EDITED_CALIBRATION_MODULE'));
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