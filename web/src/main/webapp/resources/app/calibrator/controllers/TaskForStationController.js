angular
    .module('employeeModule')
    .controller(
    'TaskForStationController',
    [
        '$rootScope',
        '$scope',
        '$modal',
        '$http',
        '$filter',
        '$q',
        'ngTableParams',
        '$translate',
        '$timeout',
        'toaster',
        'CalibrationTaskServiceCalibrator',
        function ($rootScope, $scope, $modal, $http, $filter, $q,
                  ngTableParams, $translate, $timeout, toaster, CalibrationTaskServiceCalibrator) {

            $scope.pageContent = [];

            /**
             * Date
             */
            /*$scope.clearDate = function () {
                //daterangepicker doesn't support null dates
                $scope.myDatePicker.pickerDate = $scope.defaultDate;
                //setting corresponding filters with 'all time' range
                $scope.tableParams.filter().startDateToSearch = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                $scope.tableParams.filter().endDateToSearch = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");
            };

            $scope.myDatePicker = {};
            $scope.myDatePicker.pickerDate = null;
            $scope.defaultDate = null;

            $scope.initDatePicker = function (workDate) {
                /!**
                 *  Date picker and formatter setup
                 *
                $scope.myDatePicker.pickerDate = {
                    startDate: moment().day(-30),
                    //earliest day of  all the verifications available in table
                    //we should reformat it here, because backend currently gives date in format "YYYY-MM-DD"
                    endDate: moment().day(30) // current day
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

            $scope.initDatePicker();

            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'
            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[3];

            $scope.showPicker = function ($event) {
                angular.element("#datepickerfield").trigger("click");
            };

            $scope.isDateDefault = function () {
                //console.log("isDateDefault");
                var pickerDate = $scope.myDatePicker.pickerDate;

                if (pickerDate == null || $scope.defaultDate == null) { //moment when page is just loaded
                    return true;
                }
                if (pickerDate.startDate.isSame($scope.defaultDate.startDate, 'day') //compare by day
                    && pickerDate.endDate.isSame($scope.defaultDate.endDate, 'day')) {
                    return true;
                }
                return false;
            };*/

            $scope.filterMap = {
                moduleType: {
                    type: "enumerated",
                    comparison: "eq"
                },
                moduleNumber: {
                    type: "string",
                    comparison: "like"
                },
                employeeFullName: {
                    type: "string",
                    comparison: "like"
                },
                phoneNumber: {
                    type: "string",
                    comparison: "like"
                }
            };

            $scope.moduleTypes = [
                {id: 'INSTALLATION_FIX', label: $filter('translate')('INSTALLATION_FIX')},
                {id: 'INSTALLATION_PORT', label: $filter('translate')('INSTALLATION_PORT')}
            ];

            $scope.clearAll = function () {
                $scope.tableParams.filter({});
            };

            /**
             * Updates the table.
             */
            $rootScope.onTableHandling = function () {
                $scope.tableParams.reload();
            };

            $scope.isFilter = function () {
                if ($scope.tableParams == null) return false; // table not yet initialized
                var obj = $scope.tableParams.filter();
                for (var i in obj) {
                    if (i == 'isActive' || (i == "startDateToSearch" || i == "endDateToSearch")) {
                        continue;
                    } else if (obj.hasOwnProperty(i) && obj[i]) {
                        return true;
                    }
                }
                return false;
            };

            $scope.tableParams = new ngTableParams({
                    page: 1,
                    count: 5,
                    sorting: {
                        dateOfTask: 'asc'
                    }
                },
                {
                    total: 0,
                    filterDelay: 10000,
                    getData: function ($defer, params) {
                        var sortCriteria = Object.keys(params.sorting())[0];
                        var sortOrder = params.sorting()[sortCriteria];
                        var filterParams = {};
                        for (var param in $scope.filterMap) {
                            if ($scope.filterMap[param].value) {
                                filterParams[param] = $scope.filterMap[param];
                            }
                        }
                        CalibrationTaskServiceCalibrator.getPage(params.page(), params.count(), filterParams, sortCriteria, sortOrder)
                            .success(function (result) {
                                $scope.resultsCount = result.totalItems;
                                $defer.resolve(result.content);
                                params.total(result.totalItems);
                            }, function (result) {
                                $log.debug('error fetching data:', result);
                            });
                    }
                });

            $scope.popNotification = function (title, text) {
                toaster.pop('success', title, text);
            };

        }]);
