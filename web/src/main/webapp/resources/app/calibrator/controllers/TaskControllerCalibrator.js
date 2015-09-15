angular
    .module('employeeModule')
    .controller('TaskControllerCalibrator', ['$rootScope', '$scope', '$modal', '$modalInstance', 'TaskServiceCalibrator',
        function ($rootScope, $scope, $modal, $modalInstance, taskServiceCalibrator) {

            $scope.calibrationTask = {};

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
            $scope.calibrationTask.pickerDate = null;
            $scope.defaultDate = null;

            $scope.initDateRangePicker = function (date) {
                /**
                 *  Date picker and formatter setup
                 */
                $scope.calibrationTask.pickerDate = {
                    startDate: (date ? moment(date, "YYYY-MM-DD") : moment()),
                    endDate: moment() // current day
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
                $scope.checkPlaceAndStatus(null);
                $scope.calibrationTask = null;
                $scope.installationNumberValidation = null;
                $scope.floorValidation = null;
                $scope.counterNumberValidation = null;
            };


            $scope.checkPlace = function(place){
                if (place==null) {
                    return true;
                } else if (place == 'fixed_installation') {
                    return false;
                } else {
                    return true;
                }
            }


            $scope.checkPlaceAndStatus = function(place, counterStatus){
                if ((place==null) && (counterStatus==null)) {
                    return true;
                } else if (place == 'fixed_installation' && counterStatus == 'not_removed') {
                    return false;
                } else if (place == 'portable_installation'){
                    return false;
                } else {
                    return true;
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

                        } else if (/^\d{1,3}$/.test(floor)) {
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

            $scope.editTask = function (formTask){
                    $scope.master = angular.copy($scope.calibrationTask);
                    console.log($scope.master);
                    console.log($rootScope.verifId);
                    console.log(formTask);
                    taskServiceCalibrator.saveTask($rootScope.verifId, $scope.master)
                        .success(function (result) {
                            $log.debug('saving result:', result);
                        }, function (result) {
                            $log.debug('saving result:', result);
                        });

            }

        }]);
