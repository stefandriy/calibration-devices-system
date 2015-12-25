angular
    .module('employeeModule')
    .controller('CalibrationTestControllerCalibrator', ['$rootScope', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', 'Upload', '$timeout','ngTableParams', '$translate','VerificationServiceCalibrator', '$sce', '$filter',
        function ($rootScope, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, Upload, $timeout, ngTableParams, $translate, verificationServiceCalibrator, $sce, $filter) {

            $scope.resultsCount = 0;

            /**
             *  get data of selected verification for
             *  created manual test
             */
            $scope.IdsOfVerifications = calibrationTestServiceCalibrator.dataOfVerifications().getIdsOfVerifications();

            $scope.testId = $location.search().param;

            $scope.isSavedScanDoc = true;

            /**
             *  disable use upload single bbi but this functionality can
             *  be necessary in the future
             */
            $scope.disableUseUploadSingleBBI=true;

            $scope.myDatePicker = {};
            $scope.myDatePicker.pickerDate = null;
            $scope.myDatePicker.pickerDate = {
                startDate: moment(),
                endDate: moment() // current day
            };

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


            /**
             *  create entity for send to backend
             */
            function retranslater(){
                $scope.selectedData.dateOfManualTest = new Date($scope.myDatePicker.pickerDate.startDate);
                $scope.selectedData.dateOfManualTest.setHours($scope.selectedData.timeFrom.getHours());
                $scope.selectedData.dateOfManualTest.setMinutes($scope.selectedData.timeFrom.getMinutes());
                testManualForSend = {
                    serialNumber: $scope.selectedData.manufacturerNumber.serialNumber,
                    numberOfTest: $scope.selectedData.numberProtocolManual,
                    listOfCalibrationTestDataManual: $scope.dataOfManualTests,
                    dateOfTest: $scope.convertDateToLong($scope.selectedData.dateOfManualTest),
                    pathToScanDoc:$scope.pathToScanDoc
                }
            }


            /**
             *  creat and update manual test
             */
            $scope.createAndUpdateTest = function () {
                $scope.$broadcast('show-errors-check-validity');
                if($scope.clientForm.$valid) {
                    retranslater();
                    if (!$scope.selectedData.numberProtocol) {
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
                                            };
                                            //closeTime($modalInstance);
                                        },
                                        controllerAs: 'successController',
                                        size: 'md'
                                    });
                                }
                            })
                    } else {
                        calibrationTestServiceCalibrator.editManualTest(testManualForSend, $scope.testId)
                            .then(function (status) {
                                if (status == 201) {
                                    $rootScope.onTableHandling();
                                }
                                if (status == 200) {
                                    $modal.open({
                                        animation: true,
                                        templateUrl: 'resources/app/calibrator/views/modals/calibration-test-edited-success.html',
                                        controller: function ($modalInstance) {
                                            //closeTime($modalInstance);
                                            this.ok = function () {
                                                $modalInstance.close();
                                                window.history.back();
                                            };
                                        },
                                        controllerAs: 'successController',
                                        size: 'md'
                                    });
                                }
                            })
                    }
                }
            };


            /**
             *  review scan doc of manual test
             */
            $scope.review = function () {
                calibrationTestServiceCalibrator.getScanDoc($scope.pathToScanDoc)
                    .then(function (result) {
                        //$scope.dataScanDoc = result.data;
                        var file = new Blob([result.data], {type: 'application/pdf'});
                        var fileURL = window.URL.createObjectURL(file);
                        $scope.dataScanDoc = $sce.trustAsResourceUrl(fileURL);
                        if (result.status == 201) {
                            $rootScope.onTableHandling();
                        }
                        if (result.status == 200) {
                            $modal.open({
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
                        }
                    })
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
            $scope.selectedData.numberProtocol=null;
            $scope.isUploadScanDoc = false;
            $scope.isManualProtocol = true;
            $scope.block = true;
            $scope.selectedData.timeFrom = new Date();
            $scope.pathToScanDoc = null;
            $scope.IsScanDoc = false;
            $scope.selectedData.numberProtocolManual=null;

            /**
             *  receive data of all calibration modules
             */
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

            /**
             *  receive data of manual completed test for edit
             */
            function receiveDataForCompletedTest(map) {
                calibrationTestServiceCalibrator.getDataForCompletedTest($scope.testId)
                    .then(function (result) {
                        var dataCompletedTest = result.data;
                        var dataOfCounter = map.get($scope.testId);
                        var testManual = {
                            id: $scope.testId,
                            standardSize: dataOfCounter.standardSize,
                            symbol: dataOfCounter.symbol,
                            realiseYear: dataOfCounter.realiseYear,
                            numberCounter: dataOfCounter.numberCounter,
                            statusTestFirst: dataCompletedTest.statusTestFirst,
                            statusTestSecond: dataCompletedTest.statusTestSecond,
                            statusTestThird: dataCompletedTest.statusTestThird,
                            statusCommon: dataCompletedTest.statusCommon,
                            status: ['SUCCESS', 'FAILED']
                        };
                        $scope.setDataUseManufacturerNumber(findcalibrationModuleBySerialNumber(dataCompletedTest.calibrationTestManualDTO.serialNumber));
                        $scope.selectedData.numberProtocolManual = dataCompletedTest.calibrationTestManualDTO.numberOfTest;
                        $scope.selectedData.numberProtocol = dataCompletedTest.calibrationTestManualDTO.generatenumber;
                        $scope.selectedData.dateOfManualTest = new Date(dataCompletedTest.calibrationTestManualDTO.dateOfTest);

                        $scope.myDatePicker.pickerDate = {
                            startDate: (new Date(dataCompletedTest.calibrationTestManualDTO.dateOfTest)),
                            endDate: (new Date(dataCompletedTest.calibrationTestManualDTO.dateOfTest))
                        };

                        $scope.selectedData.timeFrom = $scope.selectedData.dateOfManualTest;
                        $scope.dataOfManualTests.push(testManual);
                        $scope.selectedData.standardSize = $scope.dataOfManualTests[0].standardSize;
                        $scope.isManualProtocol = false;
                        $scope.pathToScanDoc = dataCompletedTest.calibrationTestManualDTO.pathToScanDoc;
                        $scope.checkIsScanDoc();
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

            /**
             *  receive data of verifications for review
             */
            function receiveAllVerificationForManualTest(map) {
                map.forEach(function (value, key) {
                    if (value.status == 'TEST_COMPLETED') {
                        receiveDataForCompletedTest(map);
                    } else {
                        $scope.dataOfManualTests.push(creatorTestManual(value, key));
                    }
                }, map);
            }

            /**
             * entity of manual test
             */
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


            /**
             * receive directory of all  manufacturer numbers
             */
            $scope.receiveAllManufacturerNumbers = function (data) {
                var model = null;
                for (var i = 0; i < data.length; i++) {
                    model = data[i];
                    $scope.manufacturerNumbers.push(model);
                }
            };

            /**
             * receive directory of all  original condDesignation
             */
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

            /**
             * receive directory of all  original moduleType
             */
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
                if (currentClibrationModel) {
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
                if (currentClibrationModel) {
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


            /**
             * setup date Datepicker
             */

            $scope.selectedData.dateOfManualTest = new Date();
            $scope.defaultDate = null;

            $scope.initDatePicker = function () {

                if ($scope.defaultDate == null) {
                    //copy of original daterange
                    $scope.defaultDate = angular.copy($scope.myDatePicker.pickerDate);
                }

                $scope.setTypeDataLangDatePicker = function () {

                    $scope.opts = {
                        format: 'DD-MM-YYYY',
                        singleDatePicker: true,
                        showDropdowns: true,
                        minDate: new Date(),
                        eventHandlers: {}
                    };

                };

                $scope.setTypeDataLangDatePicker();
            };

            $scope.showPicker = function () {
                angular.element("#datepickerfieldSingle").trigger("click");
            };

            $scope.initDatePicker();

            $scope.setTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {
                    moment.locale('uk'); //setting locale for momentjs library (to get monday as first day of the week in ranges)
                } else {
                    moment.locale('en'); //setting locale for momentjs library (to get monday as first day of the week in ranges)
                }
            };

            $scope.setTypeDataLanguage();


            $scope.formats = ['dd-MMMM-yyyy', 'yyyy-MM-dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[0];

            $scope.clearDate = function () {
                $scope.selectedData.dateOfManualTest= null;
            };

            $scope.disabled = function (date, mode) {
                return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
            };

            $scope.toggleMin = function () {
                $scope.min = $scope.minDate ? null : new Date();
            };
            $scope.toggleMin();
            $scope.max = new Date(2100, 5, 22);

            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'
            };

            $scope.convertDateToLong = function(date) {
                return (new Date(date)).getTime();
            };


            /**
             *  upload scan document of manual Test
             */
            $scope.uploadScanDoc = function(){
                $modal.open({
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

            /**
             *  delete scan document of manual Test
             */
            function deleteScanDoc(cb) {
                $scope.resultDelete = false;
                calibrationTestServiceCalibrator.deleteScanDoc($scope.pathToScanDoc)
                    .then(function (data) {
                        if (data.status == 201) {
                            $rootScope.onTableHandling();
                            $scope.resultDelete = false;
                        }
                        if (data.status == 200) {
                            $scope.resultDelete = true;
                            $scope.pathToScanDoc = null;
                        }
                        if (cb) {
                            cb();
                        }
                    });
            }

            /**
             *  repeat scan document of manual Test
             */
            $scope.repeatUpload = function () {
                deleteScanDoc(function(){
                    if ($scope.resultDelete) {
                        $scope.uploadScanDoc();
                        $scope.isSavedScanDoc = false;
                    }
                });
            };


            /**
             *  for show icon
             */
            $scope.checkIsScanDoc = function () {
                if ($scope.pathToScanDoc) {
                    $scope.IsScanDoc = true;
                } else {
                    $scope.IsScanDoc = false;
                }
                return
            };

            /**
             *  for disable button create from bbi
             */
            $scope.checkForCreatFromBBI = function () {
                //if ($scope.selectedData.condDesignation != null && $scope.selectedData.moduleType != null && $scope.selectedData.manufacturerNumber) {
                //    $scope.isManualProtocol = false;
                //} else {
                //    $scope.isManualProtocol = true;
                //}
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

            $scope.closeTestManual = function () {
                if ($scope.pathToScanDoc  && $scope.isSavedScanDoc) {
                    deleteScanDoc();
                    window.history.back();
                } else {
                    window.history.back();
                }
            };

            /**
             * close modal use time
             */
            function closeTime($modalInstance) {
                $timeout(function () {
                    $modalInstance.close();
                    window.history.back();
                }, 2500);
            }



            //$scope.verId = $location.search().param;
            //$scope.searchData = null;
            //$scope.fileName = null;


            //$scope.setTypeDataLanguage();
            //$scope.myDatePicker = {};
            //$scope.myDatePicker.pickerDate = null;
            //$scope.defaultDate = null;

            //$scope.initDatePicker = function (date) {
                ///**
                // *  Date picker and formatter setup
                // *
                // */


                ///
                //$scope.myDatePicker.pickerDate = {
                    //startDate: (date ? moment(date, "YYYY-MM-DD") : moment()),
                    ////earliest day of  all the verifications available in table
                    ////we should reformat it here, because backend currently gives date in format "YYYY-MM-DD"
                    //endDate: moment() // current day
                //};

                //if ($scope.defaultDate == null) {
                    ////copy of original daterange
                    //$scope.defaultDate = angular.copy($scope.myDatePicker.pickerDate);
                //}
                //moment.locale('uk'); //setting locale for momentjs library (to get monday as first day of the week in ranges)
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
                //};
            //};

            //$scope.showPicker = function ($event) {
            //    //angular.element("#datepickerfield").trigger("click");
            //};

            //$scope.isDateDefault = function () {
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
            //};


            //verificationServiceCalibrator.getArchivalVerificationEarliestDate().success(function (date) {
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
            //});



            //$scope.checkDateFilters = function () {
            //    if ($scope.tableParams == null) return false; //table not yet initialized
            //    var obj = $scope.tableParams.filter();
            //    for (var i in obj) {
            //        if (obj.hasOwnProperty(i) && obj[i]) {
            //            if (i == 'date' || i == 'endDate')
            //                continue; //check for these filters is in another function
            //            return true;
            //        }
            //    }
            //    return false;
            //};

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


            //$scope.openState = {};



            //$rootScope.onTableHandling = function () {
            //    calibrationTestServiceCalibrator.getPage($scope.currentPage, $scope.itemsPerPage, $scope.searchData, $scope.verId)
            //        .then(function (data) {
            //            $scope.pageContent = data.content;
            //            $scope.totalItems = data.totalItems;
            //        });
            //}
            //$rootScope.onTableHandling();


            $scope.calibrationTests = [];

            
            $scope.openAddTest = function () {
                if ($scope.pathToScanDoc != null && $scope.isSavedScanDoc){
                    //deleteScanDoc();
                }
                calibrationTestServiceCalibrator
                    .getEmptyTest($scope.testId)
                    .then(function (data) {
                        $log.debug("inside");
                        var testId = data.id;
                        var url = $location.path('calibrator/verifications/calibration-test-add/').search({'param': $scope.testId});
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


