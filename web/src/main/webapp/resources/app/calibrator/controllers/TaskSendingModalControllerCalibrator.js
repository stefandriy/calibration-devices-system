angular
    .module('employeeModule')
    .controller('TaskSendingModalControllerCalibrator', ['$rootScope', '$scope', '$modal', '$modalInstance', 'VerificationPlanningTaskService', '$log',
        function ($rootScope, $scope, $modal, $modalInstance, verificationPlanningTaskService, $log) {

            $scope.calibrationTask = {};
            $scope.incorrectValue = false;

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function() {
                $modalInstance.close();
            });

            /**
             * Closes edit modal window.
             */
            $scope.closeModal = function () {
                console.log("close modal window");
                $modalInstance.close();
            };

             /**
             *  Date picker and formatter setup
             *
             */

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.status.opened = true;
            };

            moment.locale('uk');
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false',

            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

            $scope.today = function() {
                $scope.calibrationTask.pickerDate = new Date();
            };
            $scope.today();

            $scope.clear = function () {
                $scope.calibrationTask.pickerDate = null;
            };

            // Disable weekend selection
            $scope.disabled = function(date, mode) {
                return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
            };

            $scope.toggleMin = function() {
                $scope.minDate = $scope.minDate ? null : new Date();
            };
            $scope.toggleMin();
            $scope.maxDate = new Date(2020, 5, 22);

            $scope.status = {
                opened: false
            };

            $scope.clearDate = function () {
                $scope.calibrationTask.pickerDate = $scope.defaultDate;
            };

            $scope.resetTaskForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.calibrationTask = {};
                $scope.incorrectValue = false;
                $scope.calibrationTask.pickerDate = null;
                $scope.installationNumberValidation = null;
                $scope.floorValidation = null;
                $scope.counterNumberValidation = null;
                $scope.showSendingMessage = false;
                $scope.checkPlaceAndStatus();
                $scope.checkPlace();
            };


            $scope.checkPlace = function(){
                try{
                    if ($scope.calibrationTask.place==null) {
                        return true;
                    } else if ($scope.calibrationTask.place == 'stationary_station') {
                        return false;
                    } else {
                        return true;
                    }
                }  catch (e) {
                    console.log("Got an error!",e);
                }
            }


            $scope.checkPlaceAndStatus = function(){
                try{
                    if (($scope.calibrationTask.place==null) && ($scope.calibrationTask.counterStatus==null)) {
                        return true;
                    } else if ($scope.calibrationTask.place == 'stationary_station' && $scope.calibrationTask.counterStatus == 'not_removed') {
                        return false;
                    } else if ($scope.calibrationTask.place == 'mobile_station'){
                        $scope.calibrationTask.counterStatus = '';
                        return false;
                    } else {
                        return true;
                    }
                } catch (e) {
                    console.log("Got an error!",e);
                }

            }

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
                        var entrance = $scope.calibrationTask.entrance;
                        if (/^[0-9]{1,2}$/.test(entrance)) {
                            validator('entrance', false);
                        } else {
                            validator('entrance', true);
                        }
                        break;
                    case ('doorCode'):
                        var doorCode = $scope.calibrationTask.doorCode;
                        if (/^[0-9]{1,4}$/.test(doorCode)) {
                            validator('doorCode', false);
                        } else {
                            validator('doorCode', true);
                        }
                        break;
                    case ('floor'):
                        var floor = $scope.calibrationTask.floor;
                        if (floor == null) {

                        } else if (/^\d{1,2}$/.test(floor)) {
                            validator('floor', false);
                        } else {
                            validator('floor', true);
                        }
                        break;
                    case ('counterNumber'):
                        var counterNumber = $scope.calibrationTask.counterNumber;
                        if (/^[0-9]{5,20}$/.test(counterNumber)) {
                            validator('counterNumber', false);
                        } else {
                            validator('counterNumber', true);
                        }
                        break;
                    case ('time'):
                        var time = $scope.calibrationTask.time;
                        if (/^[0-1]{1}[0-9]{1}(\.)[0-9]{2}(\-)[0-2]{1}[0-9]{1}(\.)[0-9]{2}$/.test(time)) {
                            validator('time', false);
                        } else {
                            validator('time', true);
                        }
                        break;
                }

            }

            function validator(caseForValidation, isValid) {
                switch (caseForValidation) {
                    case ('installationNumber'):
                        $scope.installationNumberValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
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

            $scope.modulesSerialNumbers = [];

            $scope.receiveModuleNumbers = function(){
                console.log($scope.calibrationTask.place + " " + $scope.calibrationTask.pickerDate);
                var place = $scope.calibrationTask.place;
                var pickerDate = $scope.calibrationTask.pickerDate;
                verificationPlanningTaskService.getModuls(place, pickerDate)
                    .then(function (result) {
                        $log.debug('result ', result);
                        $scope.modulesSerialNumbers.push(result.data);
                        $log.debug('my array ', $scope.modulesSerialNumbers);
                    }, function (result) {
                        $log.debug('error fetching data:', result);
                    });
            }


            $scope.showSendingMessage = false;

            $scope.editTask = function (){
                if ($rootScope.emptyStatus == true) {
                    $scope.showSendingMessage = true;
                    console.log("Ви не вибрали заявок для надсилання")
                } else {
                    console.log($rootScope.verifIds);
                }
                    //$scope.calibrationTask = {
                    //    place : $scope.calibrationTask.place,
                    //    counterStatus: $scope.calibrationTask.counterStatus,
                    //    installationNumber: $scope.calibrationTask.installationNumber,
                    //    startDate: $scope.calibrationTask.pickerDate.startDate,
                    //    endDate: $scope.calibrationTask.pickerDate.endDate,
                    //    floor: $scope.calibrationTask.floor,
                    //    counterNumber: $scope.calibrationTask.counterNumber,
                    //    notes: $scope.calibrationTask.notes
                    //};
                    //console.log($scope.calibrationTask);
                    //taskServiceCalibrator.saveTask($rootScope.verifId, $scope.calibrationTask).then(
                    //    function (data) {
                    //        if (data == 200) {
                    //            $scope.closeModal();
                    //        } else {
                    //         $scope.incorrectValue = true;
                    //         console.log($scope.incorrectValue);
                    //        }
                    //    });

            }

        }]);
