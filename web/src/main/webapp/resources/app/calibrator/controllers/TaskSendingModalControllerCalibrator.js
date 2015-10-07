angular
    .module('employeeModule')
    .controller('TaskSendingModalControllerCalibrator', ['$rootScope', '$scope', '$modal', '$modalInstance', 'VerificationPlanningTaskService', '$log',
        function ($rootScope, $scope, $modal, $modalInstance, taskServiceCalibrator) {

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


            $scope.calibrationTask = {};
            $scope.calibrationTask.pickerDate = {};
            $scope.defaultDate = null;

            $scope.initDateRangePicker = function (date) {
                /**
                 *  Date picker and formatter setup
                 */
                $scope.calibrationTask.pickerDate = {
                    startDate: (date ? moment(date).format('YYYY-MM-DD') : moment().format('YYYY-MM-DD')),
                    endDate: moment().format('YYYY-MM-DD') // current day
                };

                if ($scope.defaultDate == null) {
                   $scope.defaultDate = angular.copy($scope.calibrationTask.pickerDate);
                }
                moment.locale('uk');
                $scope.opts = {
                    format: 'DD-MM-YYYY',
                    showDropdowns: true,
                    locale: {
                        firstDay: 1,
                        fromLabel: 'Від',
                        toLabel: 'До',
                        applyLabel: "Прийняти",
                        cancelLabel: "Закрити"
                    },
                    eventHandlers: {}
                };
            };

            $scope.showTaskPicker = function ($event) {
                angular.element("#datepicker").trigger("click");
            };

            $scope.clearDate = function () {
                $scope.calibrationTask.pickerDate = $scope.defaultDate;
            };

            $scope.resetTaskForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.calibrationTask = {};
                $scope.incorrectValue = false;
                $scope.calibrationTask.pickerDate = {};
                $scope.installationNumberValidation = null;
                $scope.floorValidation = null;
                $scope.counterNumberValidation = null;
                $scope.checkPlaceAndStatus();
                $scope.checkPlace();
            };


            $scope.checkPlace = function(){
                try{
                    if ($scope.calibrationTask.place==null) {
                        return true;
                    } else if ($scope.calibrationTask.place == 'fixed_station') {
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
                    } else if ($scope.calibrationTask.place == 'fixed_station' && $scope.calibrationTask.counterStatus == 'not_removed') {
                        return false;
                    } else if ($scope.calibrationTask.place == 'portable_station'){
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

                }
            }

            $scope.editTask = function (){
                    $scope.calibrationTask = {
                        place : $scope.calibrationTask.place,
                        counterStatus: $scope.calibrationTask.counterStatus,
                        installationNumber: $scope.calibrationTask.installationNumber,
                        startDate: $scope.calibrationTask.pickerDate.startDate,
                        endDate: $scope.calibrationTask.pickerDate.endDate,
                        floor: $scope.calibrationTask.floor,
                        counterNumber: $scope.calibrationTask.counterNumber,
                        notes: $scope.calibrationTask.notes
                    };
                    console.log($scope.calibrationTask);
                    taskServiceCalibrator.saveTask($rootScope.verifId, $scope.calibrationTask).then(
                        function (data) {
                            if (data == 200) {
                                $scope.closeModal();
                            } else {
                             $scope.incorrectValue = true;
                             console.log($scope.incorrectValue);
                            }
                        });
                console.log($rootScope.verifIds);
            }

        }]);
