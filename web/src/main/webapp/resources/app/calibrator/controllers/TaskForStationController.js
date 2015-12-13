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
            $scope.clearDate = function () {
                // date-range picker doesn't support null dates
                $scope.defaultDate.startDate = moment();
                $scope.defaultDate.endDate = moment();
                $scope.myDatePicker.pickerDate = $scope.defaultDate;
                $rootScope.onTableHandling();
            };

            $scope.myDatePicker = {};
            $scope.myDatePicker.pickerDate = null;
            $scope.defaultDate = {
                startDate: moment(),
                endDate: moment() // current day
            };

            $scope.initDatePicker = function (workDate) {
                /**
                 *  Date picker and formatter setup
                 */
                $scope.myDatePicker.pickerDate = {
                    startDate: $scope.defaultDate.startDate,
                    endDate: $scope.defaultDate.endDate // current day
                };

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
                return ($scope.myDatePicker.pickerDate.startDate.isSame($scope.defaultDate.startDate, 'day') // compare by day
                    && $scope.myDatePicker.pickerDate.endDate.isSame($scope.defaultDate.endDate, 'day'));
            };

            $scope.moduleTypes = [
                {id: 'INSTALLATION_FIX', label: $filter('translate')('INSTALLATION_FIX')},
                {id: 'INSTALLATION_PORT', label: $filter('translate')('INSTALLATION_PORT')}
            ];

            $scope.setTypeDataLanguage = function () {
                $scope.moduleTypes[0].label = $filter('translate')('INSTALLATION_FIX');
                $scope.moduleTypes[1].label = $filter('translate')('INSTALLATION_PORT');
            };

            $scope.clearAll = function () {
                $scope.clearDate();
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
                    if (i == 'isForStation') {}
                    else if (obj.hasOwnProperty(i) && obj[i]) {
                        return true;
                    }
                }
                return false;
            };

            $scope.openVerificationListModal = function(calibrationTaskID) {
                var verificationsModal = $modal
                    .open({
                        animation: true,
                        controller: 'VerificationListModalController',
                        templateUrl: '/resources/app/calibrator/views/modals/verification-list-modal.html',
                        size: 'lg',
                        resolve: {
                            taskID: function () {
                                return calibrationTaskID;
                            }
                        }
                });
                verificationsModal.result.then(function() {
                    $rootScope.onTableHandling();
                }, function() {
                    $rootScope.onTableHandling();
                });
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
                        params.filter().isForStation = true;
                        if (!$scope.isDateDefault()) {
                            params.filter().startDateToSearch = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                            params.filter().endDateToSearch = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");
                        } else {
                            params.filter().startDateToSearch = null;
                            params.filter().endDateToSearch = null;
                        }
                        CalibrationTaskServiceCalibrator.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
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
