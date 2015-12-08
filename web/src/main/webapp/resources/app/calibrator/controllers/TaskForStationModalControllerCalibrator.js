angular
    .module('employeeModule')
    .controller(
    'TaskForStationModalControllerCalibrator',
    [
        '$rootScope',
        '$scope',
        '$modal',
        '$modalInstance',
        'VerificationPlanningTaskService',
        '$log',
        '$filter',
        'verificationIDs',
        'moduleType',
        'toaster',
        function ($rootScope, $scope, $modal, $modalInstance, verificationPlanningTaskService, $log, $filter,
                verificationIDs, moduleType, toaster) {

            $scope.calibrationTask = {};
            $scope.moduleNumbers = [];
            $scope.noModulesAvailable = false;
            $scope.calibrationTask.moduleType = moduleType;

            /**
             * Device types (application field) for the select dropdown
             */
            $scope.deviceTypeData = [
                {id: 'WATER', label: $filter('translate')('WATER')},
                {id: 'THERMAL', label: $filter('translate')('THERMAL')},
                {id: 'ELECTRICAL', label: $filter('translate')('ELECTRICAL')},
                {id: 'GASEOUS', label: $filter('translate')('GASEOUS')}
            ];

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function() {
                $scope.closeModal();
            });

            /**
             * Closes edit modal window.
             */
            $scope.closeModal = function (close) {
                $scope.resetTaskForm();
                if (close === true) {
                    $modalInstance.close();
                } else {
                    $modalInstance.dismiss();
                }
            };

            /**
             * resets task form
             */
            $scope.resetTaskForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.noModulesAvailable = false;
                $scope.formTask.$submitted = false;
                $scope.calibrationTask.dateOfTask = null;
                $scope.calibrationTask.applicationField = null;
                $scope.moduleNumbers = [];
            };

            /**
             *  Date picker and formatter setup
             *
             */
            $scope.firstCalendar = {};
            $scope.firstCalendar.isOpen = false;

            /**
             * sets date pickers options
             * @type {{formatYear: string, startingDay: number, showWeeks: string}}
             */
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'
            };

            /**
             * opens date picker
             * on the modal
             *
             * @param $event
             */
            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.firstCalendar.isOpen = true;
            };

            /**
             * sets format of date picker date
             */
            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

            /**
             * Disables weekend selection
             *
             * @param date
             * @param mode
             * @returns {boolean}
             */
            $scope.disabled = function(date, mode) {
                return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
            };

            $scope.toggleMin = function() {
                $scope.minDate = $scope.minDate ? null : new Date();
            };

            $scope.toggleMin();
            $scope.maxDate = new Date(2100, 5, 22);

            $scope.clearDate = function () {
                $log.debug($scope.calibrationTask.dateOfTask);
                $scope.noModulesAvailable = false;
                $scope.calibrationTask.dateOfTask = null;
                $scope.moduleNumbers = [];
            };

            /**
             * makes asynchronous request to the server
             * and receives the calibration modules info
             */
            $scope.receiveModuleNumbers = function() {
                if ($scope.calibrationTask.dateOfTask && $scope.calibrationTask.applicationField) {
                    var dateOfTask = $scope.calibrationTask.dateOfTask;
                    var deviceType = $scope.calibrationTask.applicationField;
                    var moduleType = $scope.calibrationTask.moduleType;
                    verificationPlanningTaskService.getModules(moduleType, dateOfTask, deviceType)
                        .then(function (result) {
                            $log.debug(result);
                            $scope.moduleNumbers = result.data;
                            $scope.noModulesAvailable = $scope.moduleNumbers.length === 0;
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                } else {
                    $scope.noModulesAvailable = false;
                    $scope.moduleNumbers = [];
                }
            };

            /**
             * sends the task for calibration module data
             * to the server to be saved in the database
             * if response status 200 opens success modal,
             * else opens error modal
             */
            $scope.save = function () {
                if ($scope.formTask.$valid) {
                    var calibrationTask = {
                        "dateOfTask": $scope.calibrationTask.dateOfTask,
                        "moduleNumber": $scope.calibrationTask.installationNumber,
                        "verificationsId": verificationIDs
                    };
                    verificationPlanningTaskService.saveTask(calibrationTask).then(function (data) {
                        if (data.status == 200) {
                            $scope.closeModal(true);
                        } else {
                            toaster.pop('error', $filter('translate')('INFORMATION'),
                                $filter('translate')('ERROR_WHILE_CREATING_TASK'));
                            $scope.closeModal();
                        }
                    });
                }
            }
        }]);
