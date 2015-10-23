angular
    .module('employeeModule')
    .controller('NewVerificationsControllerCalibrator', ['$scope', '$log',
        '$modal', 'VerificationServiceCalibrator',
        '$rootScope', 'ngTableParams', '$timeout', '$filter', '$window', '$location', '$translate',
        function ($scope, $log, $modal, verificationServiceCalibrator, $rootScope, ngTableParams, $timeout, $filter, $window, $location, $translate) {

            $scope.resultsCount = 0;


            /**
             * this function return true if is StateVerificatorEmployee
             */
            $scope.isCalibratorEmployee = function () {
                verificationServiceCalibrator.getIfEmployeeCalibrator().success(function(data){
                    $scope.isEmployee =  data;
                });

            };

            $scope.isCalibratorEmployee();

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

            $scope.statusData = [
                {id: 'IN_PROGRESS', label: null},
                {id: 'TEST_PLACE_DETERMINED', label: null},
                {id: 'SENT_TO_TEST_DEVICE', label: null},
                {id: 'TEST_COMPLETED', label: null},
            ];

            $scope.setTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {
                    $scope.statusData[0].label = 'В роботі';
                    $scope.statusData[1].label = 'Визначено спосіб повірки';
                    $scope.statusData[2].label = 'Відправлено на установку';
                    $scope.statusData[3].label = 'Проведено вимірювання';
                } else if (lang === 'eng') {
                    $scope.statusData[0].label = 'In progress';
                    $scope.statusData[1].label = 'Test place determined';
                    $scope.statusData[2].label = 'Sent to test device';
                    $scope.statusData[3].label = 'Test completed';

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


            verificationServiceCalibrator.getNewVerificationEarliestDate().success(function (date) {
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
                        else{
                            params.filter().status = null; //case when the filter is cleared with a button on the select
                        }

                        params.filter().date = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                        params.filter().endDate = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");

                        verificationServiceCalibrator.getNewVerifications(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
                            .success(function (result) {
                                $scope.resultsCount = result.totalItems;
                                $defer.resolve(result.content);
                                params.total(result.totalItems);
                            }, function (result) {
                                $log.debug('error fetching data:', result);
                            });
                    }
                })});

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

            $scope.markAsRead = function (id) {
                var dataToSend = {
                    verificationId: id,
                    readStatus: 'READ'
                };
                verificationServiceCalibrator.markVerificationAsRead(dataToSend).success(function () {
                    $rootScope.$broadcast('verification-was-read');
                    $scope.tableParams.reload();
                });
            };

            $scope.openDetails = function (verifId, verifDate, verifReadStatus) {
                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/new-verification-details.html',
                    controller: 'DetailsModalControllerCalibrator',
                    size: 'lg',
                    resolve: {
                        response: function () {
                            return verificationServiceCalibrator.getNewVerificationDetails(verifId)
                                .success(function (verification) {
                                    verification.id = verifId;
                                    verification.initialDate = verifDate;
                                    if (verifReadStatus == 'UNREAD') {
                                        $scope.markAsRead(verifId);
                                    }
                                    return verification;
                                });
                        }
                    }
                });
            };

            $scope.openTests = function (verificationId) {
                $log.debug("inside");
                var url = $location.path('/calibrator/verifications/calibration-test/').search({param: verificationId});
            }

            $scope.idsOfVerifications = [];
            $scope.checkedItems = [];
            $scope.allIsEmpty = true;

            $scope.resolveVerificationId = function (id) {

                var index = $scope.idsOfVerifications.indexOf(id);
                if (index === -1) {
                    $scope.idsOfVerifications.push(id);
                    index = $scope.idsOfVerifications.indexOf(id);
                }

                if (!$scope.checkedItems[index]) {
                    $scope.idsOfVerifications.splice(index, 1, id);
                    $scope.checkedItems.splice(index, 1, true);
                } else {
                    $scope.idsOfVerifications.splice(index, 1);
                    $scope.checkedItems.splice(index, 1);
                }
                checkForEmpty();
            };



            $scope.openSendingModal = function () {
                if (!$scope.allIsEmpty) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: '/resources/app/calibrator/views/modals/verification-sending.html',
                        controller: 'SendingModalControllerCalibrator',
                        size: 'md',
                        resolve: {
                            response: function () {
                                //todo need to find verificators by agreements(договорах)
                                return verificationServiceCalibrator.getVerificators()
                                    .success(function (verificators) {
                                        $log.debug(verificators);
                                        return verificators;
                                    });
                            }
                        }
                    });

                    //executes when modal closing
                    modalInstance.result.then(function (verificator) {

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            organizationId: verificator.id
                        };

                        $log.debug(dataToSend);
                        $log.debug(verificator);

                        verificationServiceCalibrator
                            .sendVerificationsToCalibrator(dataToSend)
                            .success(function () {
                                $scope.tableParams.reload();
                                $rootScope.$broadcast('verification-sent-to-verificator');
                            });
                        $scope.idsOfVerifications = [];
                        $scope.checkedItems = [];
                    });
                } else {
                    $scope.isClicked = true;
                }
            };

            var checkForEmpty = function () {
                $scope.allIsEmpty = $scope.idsOfVerifications.length === 0;
            };


            $scope.uploadBbiFile = function (idVerification) {

                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/upload-bbiFile.html',
                    controller: 'UploadBbiFileController',
                    size: 'lg',
                    resolve: {
                        verification: function () {
                            return idVerification;

                        }
                    }
                });
                modalInstance.result.then(function () {
                    $scope.tableParams.reload();
                });
            };

            $scope.cancelBbiFile = function (verification) {
                var idVerification = verification;
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/cancel-bbiFile.html',
                    controller: 'CancelBbiProtocolCalibrator',
                    size: 'md',
                    resolve: {
                        verificationId: function () {
                            return verificationServiceCalibrator.cancelUploadFile(idVerification)
                                .success(function (bbiName) {
                                    return bbiName;
                                }
                            );
                        }
                    }
                });
                modalInstance.result.then(function () {
                    $scope.tableParams.reload();
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

            $scope.initiateVerification = function () {

                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/modals/initiate-verification.html',
                    controller: 'AddingVerificationsControllerProvider',
                    size: 'lg',

                });
            };

            $scope.removeCalibratorEmployee = function (verifId) {
                var dataToSend = {
                    idVerification: verifId
                };
                $log.info(dataToSend);
                verificationServiceCalibrator.cleanCalibratorEmployeeField(dataToSend)
                    .success(function () {
                        $scope.tableParams.reload();
                    });
            };

            $scope.addCalibratorEmployee = function (verifId) {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/employee/assigning-calibratorEmployee.html',
                    controller: 'CalibratorEmployeeControllerCalibrator',
                    size: 'md',
                    windowClass: 'xx-dialog',
                    resolve: {
                        calibratorEmploy: function () {
                            return verificationServiceCalibrator.getCalibrators()
                                .success(function (calibrators) {
                                    return calibrators;
                                }
                            );
                        }
                    }
                })
                /**
                 * executes when modal closing
                 */
                modalInstance.result.then(function (formData) {
                    idVerification = 0;
                    var dataToSend = {
                        idVerification: verifId,
                        employeeCalibrator: formData.provider
                    };
                    $log.info(dataToSend);
                    verificationServiceCalibrator
                        .sendEmployeeCalibrator(dataToSend)
                        .success(function () {
                            $scope.tableParams.reload();
                        });
                });
            };

            $scope.uploadArchive = function() {
                console.log("Entered upload archive function");
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/upload-archive.html',
                    controller: 'UploadArchiveController',
                    size: 'lg'
                });
            }
        }]);

