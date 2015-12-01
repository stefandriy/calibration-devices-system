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
        function ($rootScope, $scope, $modal, $modalInstance, verificationPlanningTaskService, $log, $filter,
                verificationIDs, moduleType) {

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
                $modalInstance.close();
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
                $scope.calibrationTask.taskDate = null;
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
                $log.debug($scope.calibrationTask.taskDate);
                $scope.noModulesAvailable = false;
                $scope.calibrationTask.taskDate = null;
                $scope.moduleNumbers = [];
            };

            /**
             * makes asynchronous request to the server
             * and receives the calibration modules info
             */
            $scope.receiveModuleNumbers = function() {
                if ($scope.calibrationTask.taskDate && $scope.calibrationTask.applicationField) {
                    var taskDate = $scope.calibrationTask.taskDate;
                    var deviceType = $scope.calibrationTask.applicationField;
                    var moduleType = $scope.calibrationTask.moduleType;
                    verificationPlanningTaskService.getModules(moduleType, taskDate, deviceType)
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
                        "taskDate": $scope.calibrationTask.taskDate,
                        "moduleNumber": $scope.calibrationTask.installationNumber,
                        "verificationsId": verificationIDs
                    };
                    verificationPlanningTaskService.saveTask(calibrationTask).then(function (data) {
                        if (data.status == 200) {
                            $scope.closeModal(true);
                        }
                    });
                }
            }
        }]);
