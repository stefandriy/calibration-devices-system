angular
    .module('employeeModule')
    .controller('CalibrationTestControllerCalibrator', ['$rootScope', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', 'Upload', '$timeout','ngTableParams', '$translate','VerificationServiceCalibrator',
        function ($rootScope, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, Upload, $timeout, ngTableParams, $translate, verificationServiceCalibrator) {

            $scope.resultsCount = 0;

            $scope.clearAll = function () {
                $scope.consumptionStatus.name = null;
                $scope.selectedDeviceType.name = null;
                $scope.testResult.name = null;
                $scope.tableParams.filter(
                    {
                        id : $location.search().param
                    }
                );
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

            $scope.consumptionStatus = {
                name: null
            }
            //for measurement device type
            $scope.selectedDeviceType = {
                name: null
            }
            $scope.testResult = {
                name: null
            }




            $scope.consumptionStatus = [
                {id: 'IN_THE_AREA', label: null},
                {id: 'NOT_IN_THE_AREA', label: null}
            ];

            //new select measurementDeviceType for search
            $scope.deviceTypeData = [
                {id: 'ELECTRICAL', label: null},
                {id: 'GASEOUS', label: null},
                {id: 'WATER', label: null},
                {id: 'THERMAL', label: null}
            ];


            $scope.testResults = [
                {id: 'SUCCESS', label: null},
                {id: 'FAILED', label: null}
            ];

            $scope.setTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {
                    $scope.consumptionStatus[0].label = 'В зоні';
                    $scope.consumptionStatus[1].label = 'Не в зоні';

                    $scope.deviceTypeData[0].label = 'Електричний';
                    $scope.deviceTypeData[1].label = 'Газовий';
                    $scope.deviceTypeData[2].label = 'Водний';
                    $scope.deviceTypeData[3].label = 'Термальний';

                    $scope.testResults[0].label = 'Придатний';
                    $scope.testResults[1].label = 'Не придатний';

                } else if (lang === 'eng') {
                    $scope.consumptionStatus[0].label = 'In the area';
                    $scope.consumptionStatus[1].label = 'Not in the area';

                    $scope.deviceTypeData[0].label = 'Electrical';
                    $scope.deviceTypeData[1].label = 'Gaseous';
                    $scope.deviceTypeData[2].label = 'Water';
                    $scope.deviceTypeData[3].label = 'Thermal';

                    $scope.testResults[0].label = 'SUCCESS';
                    $scope.testResults[1].label = 'FAILED';

                } else {
                    $log.debug(lang);
                }
            };


            /**
             * Updates the table with CalibrationTests.
             */
            $scope.verId = $location.search().param;
            $scope.searchData = null;
            $scope.fileName = null;


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
                $scope.tableParams = new ngTableParams(
                {
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

                        params.filter().id = null;

                        if ($scope.consumptionStatus.name != null) {
                            params.filter().consumptionStatus = $scope.consumptionStatus.name.id;
                        }
                        else {
                            params.filter().consumptionStatus = null; //case when the filter is cleared with a button on the select
                        }

                        if ($scope.selectedDeviceType.name != null) {
                            params.filter().measurementDeviceType = $scope.selectedDeviceType.name.id;
                        }
                        else {
                            params.filter().measurementDeviceType = null; //case when the filter is cleared with a button on the select
                        }


                        if ($scope.testResult.name != null) {
                            params.filter().protocol_status = $scope.testResult.name.id;
                        } else {
                            params.filter().protocol_status = null;
                        }

                        if(true) {
                            params.filter().id = $location.search().param;
                        }

                        //params.filter().id = $location.search().param;
                        console.log($location.search().param);
                        params.filter().date = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                        params.filter().endDate = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");

                        calibrationTestServiceCalibrator.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder).success(function (result) {
                            console.log(result);
                            $scope.resultsCount = result.totalItems;
                            console.log(result.totalItems);
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                    }
                })
                console.log($scope.tableParams.filter());
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
                //    calibrationTestServiceCalibrator.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder, $scope.verId)
                //        .success(function (result) {
                //            $scope.resultsCount = result.totalItems;
                //            console.log(result);
                //            console.log(result.content);
                //            console.log($scope.resultsCount);
                //            $defer.resolve(result.content);
                //            params.total(result.totalItems);
                //        }, function (result) {
                //            $log.debug('error fetching data:', result);
                //        });
                //}


            //$rootScope.onTableHandling = function () {
            //    calibrationTestServiceCalibrator.getPage($scope.currentPage, $scope.itemsPerPage, $scope.searchData, $scope.verId)
            //        .then(function (data) {
            //            $scope.pageContent = data.content;
            //            $scope.totalItems = data.totalItems;
            //        });
            //};
            //$rootScope.onTableHandling();


            $scope.calibrationTests = [];

            
            $scope.openAddTest = function (verificationID, fileName) {
                calibrationTestServiceCalibrator
                    .getEmptyTest(verificationID)
                    .then(function (data) {
                        $log.debug("inside");
                        var testId = data.id;
                        console.log('filename = ' + fileName);
                        var url = $location.path('/calibrator/verifications/calibration-test-add/').search({'param': verificationID});
                        console.log(url);
                    } )
            };

            function getCalibrationTests(verId) {
                calibrationTestServiceCalibrator
                    .getCalibrationTests(verId)
                    .then(function (data) {
                        $scope.calibrationTests = data.calibrationTests;
                    })
            }

          //  getCalibrationTests();

            $scope.saveCalibrationTest = function () {

                calibrationTestServiceCalibrator
                    .saveCalibrationTest($scope.FormData)
                    .then(function (data) {

                        $log.debug("saved!");

                    });
            };



            /**
             * Opens modal window for editing equipment.
             */
            $scope.editCalibrationTest = function (testId) {
                $rootScope.testId = testId;
                calibrationTestServiceCalibrator.getCalibrationTestWithId(
                    $rootScope.testId).then(
                    function (data) {
                        $rootScope.calibrationTest = data;
                    });
                var testDTOModal = $modal
                    .open({
                        animation: true,
                        controller: 'CalibrationTestEditModalController',
                        templateUrl: '/resources/app/calibrator/views/modals/calibration-test-edit-modal.html'
                    });
            };

            $scope.deleteTest = function (testId) {
                $rootScope.testId = testId;
                calibrationTestServiceCalibrator.deleteCalibrationTest(testId);
                $timeout(function() {
                    console.log('delete with timeout');
                    $scope.tableParams.reload();
                }, 700);

            };


            //$scope.uploadBbiFile = function () {
            //    console.log("Entered upload bbi function");
            //    var modalInstance =  $modal.open({
            //        animation: true,
            //        templateUrl: '/resources/app/calibrator/views/modals/upload-bbiFile.html',
            //        controller: 'UploadBbiFileController',
            //        size: 'lg'
            //    });
                /*
                 modalInstance.result.then(function (fileName) {
                 $rootScope.fileName = fileName;
                 $rootScope.onTableHandling();
                 });
                 */
            //};


            $scope.uploadPhoto = function (testId) {

                var modalInstance =  $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/upload-photo.html',
                    controller: 'UploadPhotoController',
                    size: 'lg',
                    resolve: {
                        calibrationTest: function () {
                            return testId;
                        }
                    }
                });
                modalInstance.result.then(function () {
                    $rootScope.onTableHandling();
                });
            };

            $scope.openAddCalibrationTestDataModal = function(testId){
                var addTestDataModal = $modal
                    .open({
                        animation: true,
                        controller: 'CalibrationTestDataAddModalControllerCalibrator',
                        templateUrl: '/resources/app/calibrator/views/modals/calibration-testData-add-modal.html',
                        resolve: {
                            calibrationTest: function () {
                                return testId;

                            }
                        }
                    });

            }

        }]);


