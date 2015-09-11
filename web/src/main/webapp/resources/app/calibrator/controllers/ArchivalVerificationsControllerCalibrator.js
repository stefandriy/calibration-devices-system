angular
    .module('employeeModule')
    .controller('ArchivalVerificationsControllerCalibrator', ['$scope', '$modal', '$log', 'VerificationServiceCalibrator', 'ngTableParams', '$filter', '$rootScope', '$timeout', '$translate',

        function ($scope, $modal, $log, verificationServiceCalibrator, ngTableParams, $filter, $rootScope, $timeout, $translate) {

            $scope.resultsCount = 0;

            $scope.clearAll = function () {
                $scope.selectedStatus.name = null;
                $scope.tableParams.filter({});
                $scope.clearDate(); // sets 'all time' timerange
            };

            $scope.clearDate = function () {
                //daterangepicker doesn't support null dates
                $scope.myDatePicker.pickerDate = $scope.defaultDate;
                //setting corresponding filters with 'all time' range
                $scope.tableParams.filter()['date'] = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                $scope.tableParams.filter()['endDate'] = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");

            };
            $scope.doSearch = function () {
                $scope.tableParams.reload();
            }

            $scope.selectedStatus = {
                name: null
            }

            $scope.statusData = [{id: 'SENT', label: null},
                {id: 'ACCEPTED', label: null},
                {id: 'REJECTED', label: null},
                {id: 'IN_PROGRESS', label: null},
                {id: 'TEST_PLACE_DETERMINED', label: null},
                {id: 'SENT_TO_TEST_DEVICE', label: null},
                {id: 'TEST_COMPLETED', label: null},
                {id: 'SENT_TO_VERIFICATOR', label: null},
                {id: 'TEST_OK', label: null},
                {id: 'TEST_NOK', label: null}
            ];

            $scope.setTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {
                    $scope.statusData[0].label = 'Надійшла';
                    $scope.statusData[1].label = 'Прийнята';
                    $scope.statusData[2].label = 'Відхилена';
                    $scope.statusData[3].label = 'В роботі';
                    $scope.statusData[4].label = 'Визначено спосіб повірки';
                    $scope.statusData[5].label = 'Відправлено на установку';
                    $scope.statusData[6].label = 'Проведено вимірювання';
                    $scope.statusData[7].label = 'Предявлено повірнику';
                    $scope.statusData[8].label = 'Перевірено придатний';
                    $scope.statusData[9].label = 'Перевірено непридатний';

                } else if (lang === 'eng') {
                    $scope.statusData[0].label = 'Sent';
                    $scope.statusData[1].label = 'Accepted';
                    $scope.statusData[2].label = 'Rejected';
                    $scope.statusData[3].label = 'In progress';
                    $scope.statusData[4].label = 'Test place determined';
                    $scope.statusData[5].label = 'Sent to test device';
                    $scope.statusData[6].label = 'Test completed';
                    $scope.statusData[7].label = 'Sent to verificator';
                    $scope.statusData[8].label = 'Tested OK';
                    $scope.statusData[9].label = 'Tested NOK';

                } else {
                    console.error(lang);
                }
            };

            $scope.setTypeDataLanguage();
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

            verificationServiceCalibrator.getArchivalVerificationEarliestDate().success(function (date) {
                //first we will try to receive date period
                // to populate ng-table filter
                // I did this to reduce reloading and flickering of the table
                $scope.initDatePicker(date);
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
                        else {
                            params.filter().status = null; //case when the filter is cleared with a button on the select
                        }

                        params.filter().date = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                        params.filter().endDate = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");

                        verificationServiceCalibrator.getArchiveVerifications(params.page(), params.count(), params.filter(), sortCriteria, sortOrder).success(function (result) {
                            $scope.resultsCount = result.totalItems;
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                    }
                })
            });

            $scope.checkFilters = function () {
                if ($scope.tableParams == null) return false; //table not yet initialized
                var obj = $scope.tableParams.filter();
                for (var i in obj) {
                    if (obj.hasOwnProperty(i) && obj[i]) {
                        if (i == 'date' || i == 'endDate')
                            continue; //check for these filters is in another function
                        return true;
                    }
                }
                return false;
            };

            $scope.checkDateFilters = function () {
                if ($scope.tableParams == null) return false; //table not yet initialized
                var obj = $scope.tableParams.filter();
                if ($scope.isDateDefault())
                    return false;
                else if (!moment(obj.date).isSame($scope.defaultDate.startDate)
                    || !moment(obj.endDate).isSame($scope.defaultDate.endDate)) {
                    //filters are string,
                    // so we are temporarily convertin them to momentjs objects
                    return true;
                }
                return false;
            };

            $scope.openDetails = function (verifId, verifDate) {

                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/modals/archival-verification-details.html',
                    controller: 'ArchivalDetailsModalController',
                    size: 'lg',
                    resolve: {
                        response: function () {
                            return verificationServiceCalibrator.getArchivalVerificationDetails(verifId)
                                .success(function (verification) {
                                    verification.id = verifId;
                                    verification.initialDate = verifDate;
                                    return verification;
                                });
                        }
                    }
                });
            };

            /**
             *  Date picker and formatter setup
             *
             */
            $scope.openState = {};
            $scope.openState.isOpen = false;

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openState.isOpen = true;
            };


            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'
            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

        }]);
