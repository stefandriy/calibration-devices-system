angular
    .module('employeeModule')
    .controller('CalibrationTestControllerCalibrator', ['$rootScope', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', 'Upload', '$timeout','ngTableParams', '$translate','VerificationServiceCalibrator',
        function ($rootScope, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, Upload, $timeout, ngTableParams, $translate, verificationServiceCalibrator) {

            $scope.resultsCount = 0;

            $scope.IdsOfVerifications = calibrationTestServiceCalibrator.dataOfVerifications().getIdsOfVerifications();

            $scope.testId = $location.search().param;


            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 50,
                sorting: {
                    date: 'desc'
                }
            }, {
                total: 0,
                counts :[],
                getData: function ($defer, params) {
                            //$defer.resolve($scope.IdsOfVerifications);
                    var arr = [{
                        ids:1,
                        name:"my"
                    },{
                        ids:2,
                        name:"my"
                    }];
                    $defer.resolve(arr);
                    params.total(arr);
                    $scope.dati = arr;
                }
            });

            $scope.selectedData = {
                numberProtocol : null
            };



            function retranslater(){
                testManualForSend = {
                    serialNumber: $scope.selectedData.manufacturerNumber.serialNumber,
                    numberOfTest: $scope.selectedData.numberProtocolManual,
                    listOfCalibrationTestDataManual: $scope.dataOfManualTests,
                    dateOfTest: $scope.convertDateToLong($scope.selectedData.dateOfManualTest)
                }
            }


            $scope.createAndUpdateTest = function () {
                retranslater();
                if($scope.selectedData.numberProtocol==null) {
                    calibrationTestServiceCalibrator.createTestManual(testManualForSend)
                        .then(function (status) {
                            if (status == 201) {
                                $rootScope.onTableHandling();
                            }
                            if (status == 200) {
                                $modal.open({
                                    animation: true,
                                    templateUrl: 'resources/app/calibrator/views/modals/calibration-test-adding-success.html',
                                    controller: function ($modalInstance) {
                                        this.ok = function () {
                                            $modalInstance.close();
                                            window.history.back();
                                        }
                                    },
                                    controllerAs: 'successController',
                                    size: 'md'
                                });
                            }
                        })
                } else {
                    calibrationTestServiceCalibrator.editManualTest(testManualForSend,$scope.testId)
                        .then(function (status) {
                            if (status == 201) {
                                $rootScope.onTableHandling();
                            }
                            if (status == 200) {
                                $modal.open({
                                    animation: true,
                                    templateUrl: 'resources/app/calibrator/views/modals/calibration-test-edited-success.html',
                                    controller: function ($modalInstance) {
                                        this.ok = function () {
                                            $modalInstance.close();
                                            window.history.back();
                                        }
                                    },
                                    controllerAs: 'successController',
                                    size: 'md'
                                });
                            }
                        })
                }
            };

            $scope.closeTestManual = function () {
                window.history.back();
            };

            $scope.testManual = {};
            $scope.symbols = [];
            $scope.moduleTypes = [];
            $scope.manufacturerNumbers = [];
            $scope.dataOfManualTests = [];
            $scope.selectedData.standardSize = null;
            $scope.selectedData.testFirst = [];
            $scope.selectedData.testSecond = [];
            $scope.selectedData.testThird = [];
            $scope.verification = null;
            $scope.selectedData.dateOfManualTest = null;
            $scope.firstCalendar = {};
            $scope.firstCalendar.isOpen = false;
            $scope.selectedData.numberProtocolManual=null;
            $scope.selectedData.numberProtocol=null;
            $scope.isUploadScanDoc = false;
            $scope.isManualProtocol = true;
            $scope.block=true;
            $scope.selectedData.time;
            $scope.pathToScanDoc = null;
            $scope.IsScanDoc = false;
            function receiveAllModule() {
                calibrationTestServiceCalibrator.getAllModule()
                    .then(function (result) {
                        $scope.calibrationModelDATA = result.data;
                        $scope.selectedData.condDesignation = null;
                        $scope.selectedData.moduleType = null;
                        $scope.selectedData.manufacturerNumber = null;
                        $log.debug("inside");
                        $scope.receiveAllOriginalCondDesignation($scope.calibrationModelDATA);
                        $scope.receiveAllOriginalModuleType($scope.calibrationModelDATA);
                        $scope.receiveAllManufacturerNumbers($scope.calibrationModelDATA);
                        receiveAllVerificationForManualTest($scope.IdsOfVerifications);
                        $scope.selectedData.standardSize = $scope.dataOfManualTests[0].standardSize;
                    });
            }

            receiveAllModule();

            function receiveDataForCompletedTest(map) {
                calibrationTestServiceCalibrator.getDataForCompletedTest($scope.testId)
                    .then(function (result) {
                        var dataCompletedTest = result.data;
                        var dataOfCounter = map.get($scope.testId);
                        $scope.testManual.id = $scope.testId;
                        $scope.testManual.standardSize = dataOfCounter.standardSize;
                        $scope.testManual.symbol = dataOfCounter.symbol;
                        $scope.testManual.realiseYear = dataOfCounter.realiseYear;
                        $scope.testManual.numberCounter = dataOfCounter.numberCounter;
                        $scope.testManual.statusTestFirst = dataCompletedTest.statusTestFirst;
                        $scope.testManual.statusTestSecond = dataCompletedTest.statusTestSecond;
                        $scope.testManual.statusTestThird = dataCompletedTest.statusTestThird;
                        $scope.testManual.statusCommon = dataCompletedTest.statusCommon;
                        $scope.testManual.status = ['SUCCESS', 'FAILED'];
                        $scope.setDataUseManufacturerNumber(findcalibrationModuleBySerialNumber(dataCompletedTest.calibrationTestManualDTO.serialNumber));
                        $scope.selectedData.numberProtocolManual = dataCompletedTest.calibrationTestManualDTO.numberOfTest;
                        $scope.selectedData.numberProtocol = dataCompletedTest.calibrationTestManualDTO.generatenumber;
                        $scope.selectedData.dateOfManualTest = new Date(dataCompletedTest.calibrationTestManualDTO.dateOfTest);
                        $scope.dataOfManualTests.push($scope.testManual);
                        $scope.selectedData.standardSize = $scope.dataOfManualTests[0].standardSize;
                        $scope.isManualProtocol = false;
                    });
            }

            function findcalibrationModuleBySerialNumber(snumber) {
                var calibrationModel;
                for (var x = 0; x < $scope.calibrationModelDATA.length; x++) {
                    calibrationModel = $scope.calibrationModelDATA[x];
                    if (calibrationModel.serialNumber == snumber) {
                        break;
                    }
                }
                return calibrationModel;
            }


            function receiveAllVerificationForManualTest(map) {
                map.forEach(function (value, key) {
                    if (value.status == 'TEST_COMPLETED') {
                        receiveDataForCompletedTest(map);
                    } else {
                        $scope.dataOfManualTests.push(creatorTestManual(value, key));
                    }
                }, map);
            }

            function creatorTestManual(value, key) {
                var testManual = {
                    id: key,
                    standardSize: value.standardSize,
                    symbol: value.symbol,
                    realiseYear: value.realiseYear,
                    numberCounter: value.numberCounter,
                    statusTestFirst: 'SUCCESS',
                    statusTestSecond: 'SUCCESS',
                    statusTestThird: 'SUCCESS',
                    statusCommon: 'SUCCESS',
                    status: ['SUCCESS', 'FAILED'],
                    counterId: value.counterId
                };
                return testManual
            }



            $scope.receiveAllManufacturerNumbers = function (data) {
                var model = null;
                for (var i = 0; i < data.length; i++) {
                    model = data[i];
                    $scope.manufacturerNumbers.push(model);
                }

            };

            $scope.receiveAllOriginalCondDesignation = function (data) {
                var maoOfCondDesignation = new Map();
                var symbol = null;
                for (var i = 0; i < data.length; i++) {
                    symbol = data[i];
                    maoOfCondDesignation.set(symbol.condDesignation, symbol);
                }
                maoOfCondDesignation.forEach(function (value, key) {
                    $scope.symbols.push(value);
                }, maoOfCondDesignation)
            };

            $scope.receiveAllOriginalModuleType = function (data) {
                var mapOfmoduleType = new Map();
                var type = null;
                for (var i = 0; i < data.length; i++) {
                    type = data[i];
                    mapOfmoduleType.set(type.moduleType, type);
                }
                mapOfmoduleType.forEach(function (value, key) {
                    $scope.moduleTypes.push(value);
                }, mapOfmoduleType)
            };

            /**
             * set data for drop-down use selected condDesignation
             */
            $scope.setDataUseCondDesignation = function (currentClibrationModel) {
                if (currentClibrationModel != undefined) {
                    $scope.clearManufacturerNumbers();
                    $scope.moduleTypes = [];
                    var map = new Map();
                    var model = null;
                    for (var i = 0; i < $scope.calibrationModelDATA.length; i++) {
                        model = $scope.calibrationModelDATA[i];
                        if (model.condDesignation == currentClibrationModel.condDesignation) {
                            map.set(model.moduleType, model);
                            if ($scope.selectedData.moduleType != null && $scope.selectedData.moduleType.moduleType == model.moduleType) {
                                $scope.manufacturerNumbers.push(model);
                            } else if ($scope.selectedData.moduleType == null) {
                                $scope.manufacturerNumbers.push(model);
                            }
                        }
                    }
                    map.forEach(function (value, key) {
                        $scope.moduleTypes.push(value);
                    }, map)
                } else if (currentClibrationModel == undefined && $scope.selectedData.moduleType != null) {
                    $scope.clearAllArrays();
                    $scope.setDataUseModuleType($scope.selectedData.moduleType);
                    $scope.receiveAllOriginalModuleType($scope.calibrationModelDATA);
                } else {
                    $scope.clearAllArrays();
                    $scope.receiveAllOriginalCondDesignation($scope.calibrationModelDATA);
                    $scope.receiveAllOriginalModuleType($scope.calibrationModelDATA);
                    $scope.receiveAllManufacturerNumbers($scope.calibrationModelDATA);
                }
            };

            /**
             * set data for drop-down use selected moduleType
             */
            $scope.setDataUseModuleType = function (currentClibrationModel) {
                if (currentClibrationModel != undefined) {
                    $scope.clearManufacturerNumbers();
                    $scope.symbols = [];
                    var map = new Map();
                    var model = null;
                    for (var i = 0; i < $scope.calibrationModelDATA.length; i++) {
                        model = $scope.calibrationModelDATA[i];
                        if (model.moduleType == currentClibrationModel.moduleType) {
                            map.set(model.condDesignation, model);
                            if ($scope.selectedData.condDesignation != null && $scope.selectedData.condDesignation.condDesignation == model.condDesignation) {
                                $scope.manufacturerNumbers.push(model);
                            } else if ($scope.selectedData.condDesignation == null) {
                                $scope.manufacturerNumbers.push(model);
                            }
                        }
                    }
                    map.forEach(function (value, key) {
                        $scope.symbols.push(value);
                    }, map)
                } else if (currentClibrationModel == undefined && $scope.selectedData.condDesignation != null) {
                    $scope.clearAllArray();
                    $scope.setDataUseCondDesignation($scope.selectedData.condDesignation);
                    $scope.receiveAllOriginalCondDesignation($scope.calibrationModelDATA);
                } else {
                    $scope.clearAllArray();
                    $scope.receiveAllOriginalCondDesignation($scope.calibrationModelDATA);
                    $scope.receiveAllOriginalModuleType($scope.calibrationModelDATA);
                    $scope.receiveAllManufacturerNumbers($scope.calibrationModelDATA);
                }
            };

            /**
             * set data for drop-down use selected manufacturerNumber
             */
            $scope.setDataUseManufacturerNumber = function (currentClibrationModel) {
                if (currentClibrationModel != undefined) {
                    $scope.selectedData.manufacturerNumber = currentClibrationModel;
                    $scope.selectedData.condDesignation = currentClibrationModel;
                    $scope.selectedData.moduleType = currentClibrationModel;
                }
            };

            $scope.clearAllArrays = function () {
                $scope.symbols = [];
                $scope.moduleTypes = [];
                $scope.manufacturerNumbers = [];
            };

            $scope.clearManufacturerNumbers = function () {
                $scope.selectedData.manufacturerNumber = null;
                $scope.manufacturerNumbers = [];
            };



            /**
             * one of tests is changing status then change status common of test
             */
            $scope.changeStatus = function (verification) {
                if (verification.statusTestFirst == 'FAILED' || verification.statusTestSecond == 'FAILED' || verification.statusTestThird == 'FAILED') {
                    verification.statusCommon = 'FAILED';
                } else if (verification.statusTestFirst == 'SUCCESS' || verification.statusTestSecond == 'SUCCESS' || verification.statusTestThird == 'SUCCESS') {
                    verification.statusCommon = 'SUCCESS';
                }
            };

            $scope.open1 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.firstCalendar.isOpen = true;
            };

            moment.locale('uk');
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'
            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy-MM-dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

            $scope.disabled = function (date, mode) {
                return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
            };

            $scope.toggleMin = function () {
                $scope.minDate = $scope.minDate ? null : new Date();
            };

            $scope.toggleMin();
            $scope.maxDate = new Date(2100, 5, 22);

            $scope.clearDate = function () {
                $scope.selectedData.dateOfManualTest = null;
            };

            $scope.convertDateToLong = function(date) {
                return (new Date(date)).getTime();
            };



            $scope.uploadScanDoc = function(){
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/calibrator/views/modals/upload-scanDoc.html',
                    controller: 'UploadScanDocController',
                    size: 'lg',
                    resolve: {
                        parentScope: function () {
                            return $scope;
                        },
                        checkIsScanDoc: function () {
                            return $scope.checkIsScanDoc;
                        }
                    }
                });
            };

            $scope.review = function () {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/calibrator/views/modals/reviewScanDoc.html',
                    controller: 'ReviewScanDocController',
                    size: 'lg',
                    resolve: {
                        parentScope: function () {
                            return $scope;
                        }
                    }
                });
            };

            $scope.checkIsScanDoc = function () {
                if ($scope.pathToScanDoc != null) {
                    $scope.IsScanDoc = true;
                } else {
                    $scope.IsScanDoc = false;
                }
                return
            };

            $scope.checkForCreatFromBBI = function () {
                if ($scope.selectedData.condDesignation != null || $scope.selectedData.moduleType != null || $scope.selectedData.manufacturerNumber) {
                    $scope.isManualProtocol = false;
                } else {
                    $scope.isManualProtocol = true;
                }
            };


            $scope.checkAll = function (caseForValidation) {
                switch (caseForValidation) {
                    case ('numberProtocolManual'):
                        var numberProtocolManual = $scope.selectedData.numberProtocolManual;
                        if (numberProtocolManual == null) {
                        } else if (/^\d{1,3}$/.test(numberProtocolManual)) {
                            validator('numberProtocolManual', false);
                        } else {
                            validator('numberProtocolManual', true);
                        }
                        break;
                    case ('time'):
                        var time = $scope.selectedData.time;
                        if (/^[0-1]{1}[0-9]{1}(\:)[0-9]{2}(\-)[0-2]{1}[0-9]{1}(\:)[0-9]{2}$/.test(time)) {
                            validator('time', false);
                        } else {
                            validator('time', true);
                        }
                        break;
                }
            };

            function validator(caseForValidation, isValid) {
                switch (caseForValidation) {
                    case ('numberProtocolManual'):
                        $scope.numberValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        };
                        break;
                    case ('time'):
                        $scope.timeValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        };
                        break;

                }
            }


            //$scope.statusData = [
            //    {id: 'success', label: null},
            //    {id: 'failed', label: null},
            //    {id: 'raw', label: null},
            //];
            //
            //
            //
            //$scope.setStatus = function (status) {
            //    $scope.selectedStatus = {
            //        label: status.label,
            //        id: status.id
            //    };
            //};
            //
            //$scope.setTypeDataLanguage = function () {
            //    var lang = $translate.use();
            //    if (lang === 'ukr') {
            //        $scope.statusData[0].label = 'придатний';
            //        $scope.statusData[1].label = 'не придатний';
            //        $scope.statusData[2].label = 'не оброблений';
            //    } else if (lang === 'eng') {
            //        $scope.statusData[0].label = 'success';
            //        $scope.statusData[1].label = 'failed';
            //        $scope.statusData[2].label = 'raw';
            //    }
            //};
            //$scope.setTypeDataLanguage();


            $scope.clearAll = function () {
                //$scope.consumptionStatus.name = null;
                //$scope.selectedDeviceType.name = null;
                //$scope.selectedTestResult.name = null;
                //$scope.tableParams.filter(
                //    {
                //        id : $location.search().param
                //    }
                //);
                //$scope.clearDate(); // sets 'all time' timerange
            };

            $scope.clearDate = function () {
                ////daterangepicker doesn't support null dates
                //$scope.myDatePicker.pickerDate = $scope.defaultDate;
                ////setting corresponding filters with 'all time' range
                //$scope.tableParams.filter()['date'] = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                //$scope.tableParams.filter()['endDate'] = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");

            };

            //$scope.doSearch = function () {
            //    $scope.tableParams.reload();
            //}
            //
            //$scope.consumptionStatus = {
            //    name: null
            //}
            ////for measurement device type
            //$scope.selectedDeviceType = {
            //    name: null
            //}
            //$scope.selectedTestResult = {
            //    name: null
            //}

            //$scope.consumptionStatus = [
            //    {id: 'IN_THE_AREA', label: null},
            //    {id: 'NOT_IN_THE_AREA', label: null}
            //];

            //new select measurementDeviceType for search
            //$scope.deviceTypeData = [
            //    {id: 'ELECTRICAL', label: null},
            //    {id: 'GASEOUS', label: null},
            //    {id: 'WATER', label: null},
            //    {id: 'THERMAL', label: null}
            //];


            //$scope.testResults = [
            //    {id: 'SUCCESS', label: null},
            //    {id: 'FAILED', label: null}
            //];

            //$scope.setTypeDataLanguage = function () {
            //    var lang = $translate.use();
            //    if (lang === 'ukr') {
            //        $scope.consumptionStatus[0].label = 'В зоні';
            //        $scope.consumptionStatus[1].label = 'Не в зоні';
            //
            //        $scope.deviceTypeData[0].label = 'Електричний';
            //        $scope.deviceTypeData[1].label = 'Газовий';
            //        $scope.deviceTypeData[2].label = 'Водний';
            //        $scope.deviceTypeData[3].label = 'Термальний';
            //
            //        $scope.testResults[0].label = 'Придатний';
            //        $scope.testResults[1].label = 'Не придатний';
            //
            //    } else if (lang === 'eng') {
            //        $scope.consumptionStatus[0].label = 'In the area';
            //        $scope.consumptionStatus[1].label = 'Not in the area';
            //
            //        $scope.deviceTypeData[0].label = 'Electrical';
            //        $scope.deviceTypeData[1].label = 'Gaseous';
            //        $scope.deviceTypeData[2].label = 'Water';
            //        $scope.deviceTypeData[3].label = 'Thermal';
            //
            //        $scope.testResults[0].label = 'SUCCESS';
            //        $scope.testResults[1].label = 'FAILED';
            //
            //    } else {
            //        $log.debug(lang);
            //    }
            //};


            /**
             * Updates the table with CalibrationTests.
             */
            $scope.verId = $location.search().param;
            $scope.searchData = null;
            $scope.fileName = null;


            //$scope.setTypeDataLanguage();
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
                    //startDate: (date ? moment(date, "YYYY-MM-DD") : moment()),
                    ////earliest day of  all the verifications available in table
                    ////we should reformat it here, because backend currently gives date in format "YYYY-MM-DD"
                    //endDate: moment() // current day
                };

                if ($scope.defaultDate == null) {
                    ////copy of original daterange
                    //$scope.defaultDate = angular.copy($scope.myDatePicker.pickerDate);
                }
                moment.locale('uk'); //setting locale for momentjs library (to get monday as first day of the week in ranges)
                //$scope.opts = {
                //    format: 'DD-MM-YYYY',
                //    showDropdowns: true,
                //    locale: {
                //        firstDay: 1,
                //        fromLabel: 'Від',
                //        toLabel: 'До',
                //        applyLabel: "Прийняти",
                //        cancelLabel: "Зачинити",
                //        customRangeLabel: "Обрати самостійно"
                //    },
                //    ranges: {
                //        'Сьогодні': [moment(), moment()],
                //        'Вчора': [moment().subtract(1, 'day'), moment().subtract(1, 'day')],
                //        'Цього тижня': [moment().startOf('week'), moment().endOf('week')],
                //        'Цього місяця': [moment().startOf('month'), moment().endOf('month')],
                //        'Попереднього місяця': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                //        'За увесь час': [$scope.defaultDate.startDate, $scope.defaultDate.endDate]
                //    },
                //    eventHandlers: {}
                };
            //};

            $scope.showPicker = function ($event) {
                //angular.element("#datepickerfield").trigger("click");
            };

            $scope.isDateDefault = function () {
                //var pickerDate = $scope.myDatePicker.pickerDate;
                //
                //if (pickerDate == null || $scope.defaultDate == null) { //moment when page is just loaded
                //    return true;
                //}
                //if (pickerDate.startDate.isSame($scope.defaultDate.startDate, 'day') //compare by day
                //    && pickerDate.endDate.isSame($scope.defaultDate.endDate, 'day')) {
                //    return true;
                //}
                //return false;
            };


            verificationServiceCalibrator.getArchivalVerificationEarliestDate().success(function (date) {
            //        //first we will try to receive date period
            //        // to populate ng-table filter
            //        // I did this to reduce reloading and flickering of the table
            //        $scope.initDatePicker(date);
            //        $scope.tableParams = new ngTableParams(
            //            {
            //                page: 1,
            //                count: 10,
            //                sorting: {
            //                    date: 'desc'
            //                }
            //            }, {
            //                counts: [],
            //                total: 0,
            //                filterDelay: 1500,
            //                getData: function ($defer, params) {
            //            //        $defer.resolve(dataT);
            //            //    }
            //            //});
            //
            //            if (params.settings().$scope == null) {
            //                params.settings().$scope = $scope;
            //            }
            //
            //            var sortCriteria = Object.keys(params.sorting())[0];
            //            var sortOrder = params.sorting()[sortCriteria];
            //
            //            params.filter().id = null;
            //
            //            if ($scope.consumptionStatus.name != null) {
            //                params.filter().consumptionStatus = $scope.consumptionStatus.name.id;
            //            }
            //            else {
            //                params.filter().consumptionStatus = null; //case when the filter is cleared with a button on the select
            //            }
            //
            //            if ($scope.selectedDeviceType.name != null) {
            //                params.filter().measurementDeviceType = $scope.selectedDeviceType.name.id;
            //            }
            //            else {
            //                params.filter().measurementDeviceType = null; //case when the filter is cleared with a button on the select
            //            }
            //
            //
            //            if ($scope.selectedTestResult.name != null) {
            //                params.filter().testResult = $scope.selectedTestResult.name.id;
            //            } else {
            //                params.filter().testResult = null;
            //            }
            //
            //            if(true) {
            //                params.filter().id = $location.search().param;
            //            }
            //
            //            //params.filter().id = $location.search().param;
            //            params.filter().date = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
            //            params.filter().endDate = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");
            //
            //            calibrationTestServiceCalibrator.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder).success(function (result) {
            //                $scope.resultsCount = result.totalItems;
            //                $defer.resolve(result.content);
            //                params.total(result.totalItems);
            //            }, function (result) {
            //                $log.debug('error fetching data:', result);
            //            });
            //        }
            //    });
            //    $scope.params.settings().$scope = $scope;
            });



            $scope.checkDateFilters = function () {
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

            //    $scope.checkDateFilters = function () {
            //        if ($scope.tableParams == null) return false; //table not yet initialized
            //        var obj = $scope.tableParams.filter();
            //        if ($scope.isDateDefault())
            //        return false;
            //    else if (!moment(obj.date).isSame($scope.defaultDate.startDate)
            //        || !moment(obj.endDate).isSame($scope.defaultDate.endDate)) {
            //        //filters are string,
            //        // so we are temporarily convertin them to momentjs objects
            //        return true;
            //    }
            //    return false;
            //};

            $scope.openDetails = function (verifId, verifDate) {

                $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/provider/views/modals/archival-verification-details.html',
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
            //$scope.openState.isOpen = false;

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                //$scope.openState.isOpen = true;
            };


            //$scope.dateOptions = {
            //    formatYear: 'yyyy',
            //    startingDay: 1,
            //    showWeeks: 'false'
            //};

            //$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            //$scope.format = $scope.formats[2];
                    //calibrationTestServiceCalibrator.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder, $scope.verId)
                    //    .success(function (result) {
                    //        $scope.resultsCount = result.totalItems;
                    //        console.log(result);
                    //        console.log(result.content);
                    //        console.log($scope.resultsCount);
                    //        $defer.resolve(result.content);
                    //        params.total(result.totalItems);
                    //    }, function (result) {
                    //        $log.debug('error fetching data:', result);
                    //    });
                //}


            //$rootScope.onTableHandling = function () {
            //    calibrationTestServiceCalibrator.getPage($scope.currentPage, $scope.itemsPerPage, $scope.searchData, $scope.verId)
            //        .then(function (data) {
            //            $scope.pageContent = data.content;
            //            $scope.totalItems = data.totalItems;
            //        });
            //}
            //$rootScope.onTableHandling();


            $scope.calibrationTests = [];

            
            $scope.openAddTest = function (verificationID, fileName) {
                calibrationTestServiceCalibrator
                    .getEmptyTest(verificationID)
                    .then(function (data) {
                        $log.debug("inside");
                        var testId = data.id;
                        var url = $location.path('calibrator/verifications/calibration-test-add/').search({'param': verificationID});
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
                        templateUrl: 'resources/app/calibrator/views/modals/calibration-test-edit-modal.html'
                    });
            };

            $scope.deleteTest = function (testId) {
                $rootScope.testId = testId;
                calibrationTestServiceCalibrator.deleteCalibrationTest(testId).then(function (data) {
                    if (data == 200) {
                        $timeout(function () {
                            $scope.tableParams.reload();
                        }, 700);
                    } else {
                        console.log(data.status);
                    }

                })
            };


            //$scope.uploadBbiFile = function () {
            //    console.log("Entered upload bbi function");
            //    var modalInstance =  $modal.open({
            //        animation: true,
            //        templateUrl: 'resources/app/calibrator/views/modals/upload-bbiFile.html',
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
                    templateUrl: 'resources/app/calibrator/views/modals/upload-photo.html',
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
                        templateUrl: 'resources/app/calibrator/views/modals/calibration-testData-add-modal.html',
                        resolve: {
                            calibrationTest: function () {
                                return testId;

                            }
                        }
                    });

            }

        }]);


