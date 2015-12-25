angular
    .module('employeeModule')
    .controller('DetailsModalControllerCalibrator', ['$scope', '$modalInstance', '$log', 'response', '$filter',
        'VerificationServiceCalibrator', 'DataReceivingServiceCalibrator',
        function ($scope, $modalInstance, $log, response, $filter, verificationService, dataReceivingService) {

            /**
	         * Closes modal window on browser's back/forward button click.
	         */ 
	    	$scope.$on('$locationChangeStart', function() {
			    $modalInstance.close();
			});

            $scope.verificationData = response.data;

            $scope.close = function () {
                $modalInstance.close();
            };


            $scope.showAddInfoTable = {
                status: false
            };

            $scope.additionalInfo = {};
            $scope.toEdit = false;
            $scope.counterInfo = {};

            $scope.formData = {};
            $scope.selectedData = {};
            $scope.counterData = {};
            $scope.counterData.selectedCount = '1';
            $scope.deviceCountOptions = [1, 2, 3, 4];

            $scope.counterData.dismantled = true;
            $scope.counterData.sealPresence = true;

            $scope.addInfo = {};
            $scope.addInfo.serviceability = true;

            $scope.symbols = [];
            $scope.standardSizes = [];

            $scope.options = {
                hstep: [0.5, 1.0, 1.5, 2.0, 2.5, 3.0]
            };

            /**
             * fill form info about provider and initialDate, id, rejectedMessage of verification
             */
            $scope.verificationDataMain = {
                providerAddress: {}
            };
            $scope.verificationDataMain.initialDate = $scope.verificationData.initialDate;
            $scope.verificationDataMain.id = $scope.verificationData.id;
            $scope.verificationDataMain.rejectedMessage = $scope.verificationData.rejectedMessage;
            $scope.verificationDataMain.provider = $scope.verificationData.provider;
            $scope.verificationDataMain.providerAddress.region = $scope.verificationData.providerAddress.region;
            $scope.verificationDataMain.providerAddress.district = $scope.verificationData.providerAddress.district;
            $scope.verificationDataMain.providerAddress.locality = $scope.verificationData.providerAddress.locality;
            $scope.verificationDataMain.providerAddress.street = $scope.verificationData.providerAddress.street;
            $scope.verificationDataMain.providerAddress.building = $scope.verificationData.providerAddress.building;
            $scope.verificationDataMain.providerAddress.flat = $scope.verificationData.providerAddress.flat;


            $scope.moments = [];


            /**
             * For timepicker
             */
            $scope.updateTimepicker = function() {
                $scope.addInfo.timeFrom = new Date();
                $scope.addInfo.timeFrom.setHours( 8 );
                $scope.addInfo.timeFrom.setMinutes( 0 );

                $scope.updateTimeTo();
            };

            $scope.updateTimeTo = function() {
                $scope.moments = [];
                var time = undefined;
                var plusTime;
                angular.forEach($scope.options.hstep, function(value) {
                    time = moment((new Date($scope.addInfo.timeFrom)).getTime());
                    plusTime = 60 * value;
                    $scope.moments.push(time.add(plusTime, 'minutes').format("HH:mm"));
                });
                $scope.addInfo.timeTo = $scope.moments[3];
            };

            function arrayObjectIndexOf(myArray, searchTerm, property) {
                for (var i = 0, len = myArray.length; i < len; i++) {
                    if (myArray[i][property] === searchTerm) return i;
                }
                return 0;
            }

            function arrayObjectIndexOfMoments(myArray, searchTerm) {
                for (var i = 0, len = myArray.length; i < len; i++) {
                    if (myArray[i] === searchTerm.format("HH:mm")) return i;
                }
                return 0;
            }

            $scope.fillTimeToForEdit = function() {
                $scope.updateTimeTo();
                var index = arrayObjectIndexOfMoments($scope.moments, moment($scope.verificationInfo.timeTo, "HH:mm"));
                $scope.addInfo.timeTo = $scope.moments[index];
            };

            /**
             * Receives all possible devices.
             */
            dataReceivingService.findAllDevices()
                .success(function (devices) {
                    $scope.devices = devices;
                    $log.debug('device');
                    $log.debug(devices);
                    $scope.counterData.selectedDevice = [];
                    $log.debug($scope.counterData.selectedCount);
                });

            /**
             * Receives list of all symbols from table counter_type
             */
            $scope.receiveAllSymbols = function(device) {
                $scope.symbols = [];
                dataReceivingService.findAllSymbols(device.id)
                    .success(function(symbols) {
                        $scope.symbols = symbols;
                        $scope.counterData.counterSymbol = undefined;
                        $scope.counterData.counterStandardSize = undefined;
                    });
            };

            /**
             * Receive list of standardSizes from table counter_type by symbol
             */
            $scope.recieveStandardSizesBySymbol = function (symbol, device) {
                $scope.standardSizes = [];
                dataReceivingService.findStandardSizesBySymbol(symbol, device.id)
                    .success(function(standardSizes) {
                        $scope.standardSizes = standardSizes;
                        $scope.counterData.counterStandardSize = undefined;
                    });
            };

            /**
             * Receives all possible districts.
             * On-select handler in region input form element.
             */
            $scope.receiveDistricts = function (selectedRegion) {
                $scope.districts = [];
                dataReceivingService.findDistrictsByRegionId(selectedRegion.id)
                    .success(function (districts) {
                        $scope.districts = districts;
                        $scope.selectedData.district = "";
                        $scope.selectedData.locality = "";
                        $scope.selectedData.selectedStreet = "";
                    });
            };

            /**
             * Receives all possible localities.
             * On-select handler in district input form element.
             */
            $scope.receiveLocalitiesAndProviders = function (selectedDistrict) {
                dataReceivingService.findLocalitiesByDistrictId(selectedDistrict.id)
                    .success(function (localities) {
                        $scope.localities = localities;
                        $scope.selectedData.locality = "";
                        $scope.selectedData.selectedStreet = "";
                    });
            };

            dataReceivingService.findStreetsTypes().success(function (streetsTypes) {
                $scope.streetsTypes = streetsTypes;
                $scope.selectedData.selectedStreetType = "";
                $log.debug("$scope.streetsTypes");
                $log.debug($scope.streetsTypes);
            });

            /**
             * Receives all possible streets.
             * On-select handler in locality input form element
             */
            $scope.receiveStreets = function (selectedLocality, selectedDistrict) {
                if (!$scope.blockSearchFunctions) {
                    $scope.streets = [];
                    dataReceivingService.findStreetsByLocalityId(selectedLocality.id)
                        .success(function (streets) {
                            $scope.streets = streets;
                            $scope.selectedData.selectedStreet = "";
                        });
                    $scope.indexes = [];
                    dataReceivingService.findMailIndexByLocality(selectedLocality.designation, selectedDistrict.id)
                        .success(function (indexes) {
                            $scope.indexes = indexes;
                            $scope.selectedData.index = indexes[0];
                        });
                }
            };

            /**
             * Receives all possible buildings.
             * On-select handler in street input form element.
             */
            $scope.receiveBuildings = function (selectedStreet) {
                $scope.buildings = [];
                dataReceivingService.findBuildingsByStreetId(selectedStreet.id)
                    .success(function (buildings) {
                        $scope.buildings = buildings;
                    });
            };

            verificationService.getVerificationById($scope.verificationData.id)
                .success(function(info) {
                    $scope.verificationInfo = info;
                    $scope.convertCounterForView();
                    $scope.convertInfoForView();
                    $scope.fillClientInfoForEdit();
                    $scope.fillCounterForEdit();
                    $scope.fillAddInfoForEdit();
                });

            $scope.convertCounterForView = function() {

                // COUNTER
                $scope.counterInfo.deviceName = $scope.verificationInfo.deviceName;
                $scope.counterInfo.counterStatus = ($scope.verificationInfo.dismantled) ? $filter('translate')('YES') : $filter('translate')('NO');
                $scope.counterInfo.dateOfDismantled = ($scope.verificationInfo.dateOfDismantled)
                    ? new Date($scope.verificationInfo.dateOfDismantled).toLocaleDateString() : $filter('translate')('NO_TIME');
                $scope.counterInfo.dateOfMounted = ($scope.verificationInfo.dateOfMounted)
                    ? new Date($scope.verificationInfo.dateOfMounted).toLocaleDateString() : $filter('translate')('NO_TIME');
                $scope.counterInfo.comment = $scope.verificationInfo.comment;
                $scope.counterInfo.numberCounter = $scope.verificationInfo.numberCounter;
                $scope.counterInfo.sealPresence = ($scope.verificationInfo.sealPresence) ? $filter('translate')('YES') : $filter('translate')('NO');
                $scope.counterInfo.counterSymbol = $scope.verificationInfo.symbol;
                $scope.counterInfo.counterStandardSize = $scope.verificationInfo.standardSize;
                $scope.counterInfo.releaseYear = $scope.verificationInfo.releaseYear;

            };

            $scope.convertInfoForView = function() {
                //ADDITION INFO
                $scope.additionalInfo.entrance = $scope.verificationInfo.entrance;
                $scope.additionalInfo.doorCode = $scope.verificationInfo.doorCode;
                $scope.additionalInfo.floor = $scope.verificationInfo.floor;
                $scope.additionalInfo.dateOfVerif = ($scope.verificationInfo.dateOfVerif)
                    ? new Date($scope.verificationInfo.dateOfVerif).toLocaleDateString() :  $filter('translate')('NO_TIME');
                $scope.additionalInfo.time = ($scope.verificationInfo.timeFrom && $scope.verificationInfo.timeTo)
                    ? ($scope.verificationInfo.timeFrom + " - " + $scope.verificationInfo.timeTo) : $filter('translate')('NO_TIME');
                $scope.additionalInfo.serviceability = ($scope.verificationInfo.serviceability) ? $filter('translate')('YES') : $filter('translate')('NO');
                $scope.additionalInfo.noWaterToDate = ($scope.verificationInfo.noWaterToDate)
                    ? new Date($scope.verificationInfo.noWaterToDate).toLocaleDateString() : $filter('translate')('NO_TIME');
                $scope.additionalInfo.notes = $scope.verificationInfo.notes;

            };

            $scope.fillClientInfoForEdit = function() {

                //CLIENT INFO
                $scope.formData.lastName = $scope.verificationData.lastName;
                $scope.formData.firstName = $scope.verificationData.firstName;
                $scope.formData.middleName = $scope.verificationData.middleName;
                $scope.formData.email = $scope.verificationData.email;
                $scope.formData.phone = $scope.verificationData.phone;

                $scope.selectedData.selectedBuilding = $scope.verificationData.building;
                $scope.formData.flat = $scope.verificationData.flat;

                dataReceivingService.findAllRegions().then(function (respRegions) {
                    $scope.regions = respRegions.data;
                    var index = arrayObjectIndexOf($scope.regions, $scope.verificationData.region, "designation");
                    $scope.selectedData.region = $scope.regions[index];

                    dataReceivingService.findDistrictsByRegionId($scope.selectedData.region.id)
                        .then(function (districts) {
                            $scope.districts = districts.data;
                            var index = arrayObjectIndexOf($scope.districts, $scope.verificationData.district, "designation");
                            $scope.selectedData.district = $scope.districts[index];

                            dataReceivingService.findLocalitiesByDistrictId($scope.selectedData.district.id)
                                .then(function (localities) {
                                    $scope.localities = localities.data;
                                    var index = arrayObjectIndexOf($scope.localities, $scope.verificationData.locality, "designation");
                                    $scope.selectedData.locality = $scope.localities[index];

                                    dataReceivingService.findStreetsByLocalityId($scope.selectedData.locality.id)
                                        .then(function(streets) {
                                            $scope.streets = streets.data;
                                            var index = arrayObjectIndexOf($scope.streets, $scope.verificationData.street, "designation");
                                            $scope.selectedData.selectedStreet = $scope.streets[index];

                                        });

                                    dataReceivingService.findMailIndexByLocality($scope.selectedData.locality.designation,
                                        $scope.selectedData.district.id)
                                        .success(function (indexes) {
                                            $scope.indexes = indexes;
                                            $scope.selectedData.index = $scope.indexes[0];
                                            $scope.blockSearchFunctions = false;
                                        });
                                });
                        });
                });
            };

            $scope.fillCounterForEdit = function() {
                //COUNTER
                $scope.counterData.dismantled = $scope.verificationInfo.dismantled;
                $scope.counterData.dateOfDismantled = $scope.verificationInfo.dateOfDismantled;
                $scope.counterData.dateOfMounted = $scope.verificationInfo.dateOfMounted;
                $scope.counterData.comment = $scope.verificationInfo.comment;
                $scope.counterData.numberCounter = $scope.verificationInfo.numberCounter;
                $scope.counterData.sealPresence = $scope.verificationInfo.sealPresence;
                $scope.counterData.releaseYear = $scope.verificationInfo.releaseYear;

                if ($scope.verificationInfo.deviceName) {
                    dataReceivingService.findAllDevices().then(function (devices) {
                        $scope.devices = devices.data;
                        var index = arrayObjectIndexOf($scope.devices, $scope.verificationInfo.deviceName, "designation");
                        $scope.counterData.selectedDevice = $scope.devices[index];

                        if ($scope.verificationInfo.symbol) {

                            dataReceivingService.findAllSymbols($scope.verificationInfo.deviceId).then(function (respSymbols) {
                                $scope.symbols = respSymbols.data;
                                var index = arrayObjectIndexOf($scope.symbols, $scope.verificationInfo.symbol);
                                $scope.counterData.counterSymbol = $scope.symbols[index];

                                dataReceivingService.findStandardSizesBySymbol($scope.counterData.counterSymbol, $scope.verificationInfo.deviceId)
                                    .then(function (standardSizes) {
                                        $scope.standardSizes = standardSizes.data;
                                        var index = arrayObjectIndexOf($scope.standardSizes, $scope.verificationInfo.standardSize);
                                        $scope.counterData.counterStandardSize = $scope.standardSizes[index];
                                    });
                            });
                        }
                    });
                }

            };

            $scope.fillAddInfoForEdit = function() {

                //ADDITION INFO
                $scope.addInfo.entrance = $scope.verificationInfo.entrance;
                $scope.addInfo.doorCode = $scope.verificationInfo.doorCode;
                $scope.addInfo.floor = $scope.verificationInfo.floor;
                $scope.addInfo.dateOfVerif = $scope.verificationInfo.dateOfVerif;
                if($scope.verificationInfo.timeFrom && $scope.verificationInfo.timeTo) {
                    $scope.addInfo.timeFrom = moment($scope.verificationInfo.timeFrom, "HH:mm");
                    $scope.fillTimeToForEdit()
                } else {
                    $scope.updateTimepicker();
                }
                $scope.addInfo.serviceability = $scope.verificationInfo.serviceability;
                $scope.addInfo.noWaterToDate = $scope.verificationInfo.noWaterToDate;
                $scope.addInfo.notes = $scope.verificationInfo.notes;
            };

            /**
             * Convert date to long to sent it to backend
             * @param date
             * @returns {number}
             */
            $scope.convertDateToLong = function(date) {
                return (new Date(date)).getTime();
            };

            /**
             * Initializing the addInfo
             * */
            $scope.addInfo = {};

            /**
            * Toggle button (additional info) functionality
            */
            $scope.showStatus = {
                opened: false
            };

            $scope.openAdditionalInformation = function () {
                $scope.showStatus.opened = !$scope.showStatus.opened;
            };

            $scope.showCounter = {
                opened: false
            };

            $scope.openCounterInfo = function() {
                $scope.showCounter.opened = !$scope.showCounter.opened;
            };

            /**
             *  Date picker and formatter setup
             *
             */
            $scope.firstCalendar = {};
            $scope.firstCalendar.isOpen = false;
            $scope.secondCalendar = {};
            $scope.secondCalendar.isOpen = false;
            $scope.thirdCalendar = {};
            $scope.thirdCalendar.isOpen = false;
            $scope.fourthCalendar = {};
            $scope.fourthCalendar.isOpen = false;

            $scope.open1 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.firstCalendar.isOpen = true;
            };

            $scope.open2 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.secondCalendar.isOpen = true;
            };

            $scope.open3 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.thirdCalendar.isOpen = true;
            };

            $scope.open4 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.fourthCalendar.isOpen = true;
            };

            moment.locale('uk');
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'

            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy-MM-dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

            $scope.clear = function () {
                $scope.addInfo.pickerDate = null;
            };

            // Disable weekend selection
            $scope.disabled = function(date, mode) {
                return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
            };

            $scope.toggleMin = function() {
                $scope.minDate = $scope.minDate ? null : new Date();
            };

            $scope.toggleMin();
            $scope.maxDate = new Date(2100, 5, 22);
            $scope.minDateDismantled = new Date(2015, 1, 1);
            $scope.maxDateDismantled = new Date();

            $scope.clearDate1 = function () {
                $scope.addInfo.dateOfVerif = null;
            };

            $scope.clearDate2 = function () {
                $scope.addInfo.noWaterToDate = null;
            };

            $scope.clearDateOfDismantled = function() {
                $scope.counterData.dateOfDismantled = null;
            };

            $scope.clearDateOfMounted = function() {
                $scope.counterData.dateOfMounted = null;
            };

            /**
             * additonal info form validation
             *
             * @param caseForValidation
             */
            $scope.checkAll = function (caseForValidation) {
                switch (caseForValidation) {
                    case ('installationNumber'):
                        var installationNumber = $scope.calibrationTask.installationNumber;
                        if (/^[0-9]{5,20}$/.test(installationNumber)) {
                            validator('installationNumber', false);
                        } else {
                            validator('installationNumber', true);
                        }
                        break;
                    case ('entrance'):
                        var entrance = $scope.addInfo.entrance;
                        if (/^[0-9]{1,2}$/.test(entrance)) {
                            validator('entrance', false);
                        } else {
                            validator('entrance', true);
                        }
                        break;
                    case ('doorCode'):
                        var doorCode = $scope.addInfo.doorCode;
                        if (/^[0-9]{1,4}$/.test(doorCode)) {
                            validator('doorCode', false);
                        } else {
                            validator('doorCode', true);
                        }
                        break;
                    case ('floor'):
                        var floor = $scope.addInfo.floor;
                        if (floor == null) {

                        } else if (/^\d{1,2}$/.test(floor)) {
                            validator('floor', false);
                        } else {
                            validator('floor', true);
                        }
                        break;
                    case ('counterNumber'):
                        var counterNumber = $scope.addInfo.counterNumber;
                        if (/^[0-9]{5,20}$/.test(counterNumber)) {
                            validator('counterNumber', false);
                        } else {
                            validator('counterNumber', true);
                        }
                        break;
                    case ('time'):
                        var time = $scope.addInfo.time;
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
                    case ('entrance'):
                        $scope.entranceValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        };
                        break;
                    case ('doorCode'):
                        $scope.doorCodeValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        };
                        break;
                    case ('floor'):
                        $scope.floorValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        };
                        break;
                    case ('counterNumber'):
                        $scope.counterNumberValidation = {
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

            /**
             * reset additional info form
             */
            $scope.resetAddInfoForm = function(){
                $scope.$broadcast('show-errors-reset');
                $scope.fillAddInfoForEdit();
            };

            /**
             * reset counter form
             */
            $scope.resetCounterForm = function() {
                $scope.$broadcast('show-errors-reset');
                $scope.fillCounterForEdit();
            };

            /**
             * reset client form
             */
            $scope.resetClientForm = function() {
                $scope.$broadcast('show-errors-reset');
                $scope.fillClientInfoForEdit();
            };

            $scope.showMessage = {
                status: false
            };

            /**
             * send clientInfo to server for updating
             */
            $scope.editClientForm = function() {

                var clientInfo = {
                    "verificationId": $scope.verificationData.id,
                    "lastName": $scope.formData.lastName,
                    "firstName": $scope.formData.firstName,
                    "middleName": $scope.formData.middleName,
                    "email": $scope.formData.email,
                    "phone": $scope.formData.phone,
                    "secondPhone": $scope.formData.secondPhone,
                    "region": $scope.selectedData.region.designation,
                    "district": $scope.selectedData.district.designation,
                    "locality": $scope.selectedData.locality.designation,
                    "street": $scope.selectedData.selectedStreet.designation || $scope.selectedData.selectedStreet,
                    "building": $scope.selectedData.selectedBuilding.designation || $scope.selectedData.selectedBuilding,
                    "flat": $scope.formData.flat
                };
                verificationService.editClientInfo(clientInfo)
                    .then(function(response) {
                        if (response.status == 200) {
                            verificationService.getVerificationById($scope.verificationData.id)
                                .success(function(info) {
                                    $scope.verificationData = info;
                                    $scope.toEditClientInfo = !$scope.toEditClientInfo;
                                });
                        } else {
                            $scope.incorrectValue = true;
                        }
                    })

            };

            /**
             * sent form data about counter to the server for updating
             */
            $scope.editCounter = function() {

                var counter = {
                    "verificationId": $scope.verificationData.id,
                    "deviceId": $scope.counterData.selectedDevice.id,
                    "deviceName": $scope.counterData.selectedDevice.designation,
                    "dismantled": $scope.counterData.dismantled,
                    "dateOfDismantled": ($scope.convertDateToLong($scope.counterData.dateOfDismantled) !== 0)
                        ? $scope.convertDateToLong($scope.counterData.dateOfDismantled) : null,
                    "dateOfMounted": ($scope.convertDateToLong($scope.counterData.dateOfMounted) !== 0)
                        ? $scope.convertDateToLong($scope.counterData.dateOfMounted) : null,
                    "comment": $scope.counterData.comment,
                    "numberCounter": $scope.counterData.numberCounter,
                    "sealPresence": $scope.counterData.sealPresence,
                    "symbol": $scope.counterData.counterSymbol,
                    "standardSize": $scope.counterData.counterStandardSize,
                    "releaseYear": $scope.counterData.releaseYear
                };
                verificationService.editCounterInfo(counter)
                    .then(function(response) {
                        if (response.status == 200) {
                            verificationService.getVerificationById($scope.verificationData.id)
                                .success(function(info) {
                                    $scope.verificationInfo = info;
                                    $scope.convertCounterForView();
                                    $scope.toEditCounter = !$scope.toEditCounter;
                                });
                        } else {
                            $scope.incorrectValue = true;
                        }
                    })
            };

            /**
             * send form data about addition information to the server for updating
             */
            $scope.editAdditionalInfo = function(){

                if (!$scope.addInfo.entrance && !$scope.addInfo.doorCode && !$scope.addInfo.floor
                    && !$scope.addInfo.dateOfVerif && !$scope.addInfo.time &&
                    !$scope.addInfo.noWaterToDate && !$scope.addInfo.notes){
                    $scope.showMessage.status = true;
                } else {
                    if (!$scope.addInfo.serviceability){
                        $scope.addInfo.serviceability = true;
                    }
                    $scope.showMessage.status = false;
                    var info = {
                        "entrance": $scope.addInfo.entrance,
                        "doorCode": $scope.addInfo.doorCode,
                        "floor": $scope.addInfo.floor,
                        "dateOfVerif": $scope.addInfo.dateOfVerif,
                        "timeFrom": moment($scope.convertDateToLong($scope.addInfo.timeFrom)).format("HH:mm"),
                        "timeTo": $scope.addInfo.timeTo,
                        "serviceability": $scope.addInfo.serviceability,
                        "noWaterToDate": $scope.addInfo.noWaterToDate,
                        "notes": $scope.addInfo.notes,
                        "verificationId": $scope.verificationData.id
                    };
                    verificationService.saveAdditionalInfo(info)
                        .then(function (response) {
                            if (response.status == 200) {
                                verificationService.getVerificationById($scope.verificationData.id)
                                    .success(function(info) {
                                        $scope.verificationInfo = info;
                                        $scope.convertInfoForView();
                                        $scope.toEditInfo = !$scope.toEditInfo;
                                    });
                            } else {
                             $scope.incorrectValue = true;
                            }
                        });
                }

            }

        }]);
