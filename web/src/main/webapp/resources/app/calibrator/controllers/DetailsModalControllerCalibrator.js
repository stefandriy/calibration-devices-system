angular
    .module('employeeModule')
    .controller('DetailsModalControllerCalibrator', ['$scope', '$modalInstance', '$log', 'response', 'VerificationServiceCalibrator',
        function ($scope, $modalInstance, $log, response, verificationService) {

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
            /**
             * this method send request to the server
             * and check if additional info exists, if true -
             * opens additional info table and sends request to the server
             * to receive the additional info and fill the table
             *
             */
            //verificationService.checkIfAdditionalInfoExists($scope.verificationData.id)
            //    .then(function (response) {
            //        $log.debug(response);
            //        if (response.data == true) {
            //            $scope.showAddInfoTable.status = true;
            //            verificationService.findAdditionalInfoByVerifId($scope.verificationData.id)
            //                .success(function (info) {
            //                    $scope.additionalInfo = info;
            //                    if($scope.additionalInfo.serviceability=true){
            //                        $scope.additionalInfo.serviceability = "так";
            //                    } else {
            //                        $scope.additionalInfo.serviceability = "ні";
            //                    }
            //            });
            //        } else {
            //            $scope.showAddInfoTable.status = false;
            //        }
            //    });

            verificationService.getVerificationById($scope.verificationData.id)
                .success(function(info) {
                    $scope.verificationInfo = info;
                    $scope.convertForView();
                });

            $scope.convertForView = function() {

                // COUNTER
                $scope.counterInfo.deviceName = $scope.verificationInfo.deviceName;
                $scope.counterInfo.counterStatus = ($scope.verificationInfo.dismantled) ? "так" : "ні";
                $scope.counterInfo.dateOfDismantled = ($scope.verificationInfo.dateOfDismantled)
                    ? new Date($scope.verificationInfo.dateOfDismantled).toLocaleDateString() : "час відсутній";
                $scope.counterInfo.dateOfMounted = ($scope.verificationInfo.dateOfMounted)
                    ? new Date($scope.verificationInfo.dateOfMounted).toLocaleDateString() : "час відсутній";
                $scope.counterInfo.comment = $scope.verificationInfo.comment;
                $scope.counterInfo.numberCounter = $scope.verificationInfo.numberCounter;
                $scope.counterInfo.sealPresence = ($scope.verificationInfo.sealPresence) ? "так" : "ні" ;
                $scope.counterInfo.counterSymbol = $scope.verificationInfo.symbol;
                $scope.counterInfo.counterStandardSize = $scope.verificationInfo.standardSize;
                $scope.counterInfo.releaseYear = $scope.verificationInfo.releaseYear;

                //ADDITION INFO
                $scope.additionalInfo.entrance = $scope.verificationInfo.entrance;
                $scope.additionalInfo.doorCode = $scope.verificationInfo.doorCode;
                $scope.additionalInfo.floor = $scope.verificationInfo.floor;
                $scope.additionalInfo.dateOfVerif = ($scope.verificationInfo.dateOfVerif)
                    ? new Date($scope.verificationInfo.dateOfVerif).toLocaleDateString() :  "час відсутній";
                $scope.additionalInfo.time = $scope.verificationInfo.timeFrom;
                $scope.additionalInfo.serviceability = ($scope.verificationInfo.serviceability) ? "так" : "ні" ;
                $scope.additionalInfo.noWaterToDate = ($scope.verificationInfo.noWaterToDate)
                    ? new Date($scope.verificationInfo.noWaterToDate).toLocaleDateString() : "час відсутній";
                $scope.additionalInfo.notes = $scope.verificationInfo.notes;

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
                if($scope.showStatus.opened === false){
                    $scope.showStatus.opened = true;
                } else {
                    $scope.showStatus.opened = false;
                }
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

            moment.locale('uk');
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false',

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


            $scope.clearDate1 = function () {
                $scope.addInfo.dateOfVerif = null;
            };

            $scope.clearDate2 = function () {
                $scope.addInfo.noWaterToDate = null;
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

            }

            function validator(caseForValidation, isValid) {
                switch (caseForValidation) {
                    case ('entrance'):
                        $scope.entranceValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('doorCode'):
                        $scope.doorCodeValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('floor'):
                        $scope.floorValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('counterNumber'):
                        $scope.counterNumberValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('time'):
                        $scope.timeValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;

                }
            }

            /**
             * reset additional info form
             */
            $scope.resetForm = function(){
                $scope.$broadcast('show-errors-reset');
                $scope.addInfo.entrance = undefined;
                $scope.addInfo.doorCode = undefined;
                $scope.addInfo.floor = undefined;
                $scope.addInfo.dateOfVerif;
                $scope.addInfo.time = undefined;
                $scope.addInfo.serviceability = undefined;
                $scope.addInfo.noWaterToDate = undefined;
                $scope.addInfo.notes  = undefined;
                $scope.entranceValidation = {};
                $scope.doorCodeValidation = {};
                $scope.floorValidation = {};
                $scope.counterNumberValidation = {};
                $scope.timeValidation = {};
            }

            $scope.showMessage = {
                status: false
            }

            /**
             * send form data to the server
             */
            $scope.editAdditionalInfo = function(){
                if ($scope.addInfo.entrance==undefined && $scope.addInfo.doorCode==undefined && $scope.addInfo.floor == undefined
                    && $scope.addInfo.dateOfVerif==undefined && $scope.addInfo.time == undefined &&
                    $scope.addInfo.noWaterToDate == undefined && $scope.addInfo.notes == undefined){
                    $scope.showMessage.status = true;
                } else {
                    if ($scope.addInfo.serviceability == undefined){
                        $scope.addInfo.serviceability = true;
                    }
                    $scope.showMessage.status = false;
                    var info = {
                        "entrance": $scope.addInfo.entrance,
                        "doorCode": $scope.addInfo.doorCode,
                        "floor": $scope.addInfo.floor,
                        "dateOfVerif": $scope.addInfo.dateOfVerif,
                        "time": $scope.addInfo.time,
                        "serviceability": $scope.addInfo.serviceability,
                        "noWaterToDate": $scope.addInfo.noWaterToDate,
                        "notes": $scope.addInfo.notes,
                        "verificationId": $scope.verificationData.id
                    }
                    verificationService.saveAdditionalInfo(info)
                        .then(function (response) {
                            if (response.status == 200) {
                                $scope.close();
                            } else {
                             $scope.incorrectValue = true;
                            }
                        });
                }

            }


        }]);
