angular
    .module('employeeModule')
    .controller('TaskForStationModalControllerCalibrator', ['$rootScope', '$scope', '$modal', '$modalInstance', 'VerificationPlanningTaskService', '$log',
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
            $scope.firstCalendar = {};
            $scope.firstCalendar.isOpen = false;


            /**
             * open first date picker
             * on the modal
             *
             * @param $event
             */
            $scope.open1 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.firstCalendar.isOpen = true;
            };

            /**
             * set date pickers options
             * @type {{formatYear: string, startingDay: number, showWeeks: string}}
             */
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false',

            };

            /**
             * set format of date picker date
             */
            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

            /**
             * Disable weekend selection
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


            $scope.clearDate1 = function () {
                $log.debug($scope.calibrationTask.taskDate);
                $scope.calibrationTask.taskDate = null;
            };

            /**
             * reset task form
             */
            $scope.resetTaskForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.calibrationTask = {};
                $scope.incorrectValue = false;
                $scope.calibrationTask.pickerDate = null;
                $scope.installationNumberValidation = null;
                $scope.floorValidation = null;
                $scope.counterNumberValidation = null;
                $scope.showSendingMessage = false;
                $scope.modulesSerialNumbers = {};
            };

            $scope.modulesSerialNumbers = {};

            /**
             * make asynchronous request to the server
             * and receive the stations info
             */
            $scope.receiveModuleNumbers = function(){
                console.log($scope.calibrationTask.place + " " + $scope.calibrationTask.taskDate);
                var place = $scope.calibrationTask.place;
                var taskDate = $scope.calibrationTask.taskDate;
                var applicationFiled = $scope.calibrationTask.applicationFiled;
                verificationPlanningTaskService.getModuls(place, taskDate, applicationFiled)
                    .then(function (result) {
                        $log.debug(result);
                        $scope.modulesSerialNumbers = result.data;
                    }, function (result) {
                        $log.debug('error fetching data:', result);
                    });
            }

            /**
             * send the task for station data
             * to the server to be saved in the database
             * if response status 200 opens success modal,
             * else open error modal
             */
            $scope.showSendingMessage = false;
            $scope.save = function (){
                if ($rootScope.emptyStatus == true) {
                    $scope.showSendingMessage = true;
                } else {
                    var calibrationTask = {
                        "taskDate": $scope.calibrationTask.taskDate,
                        "serialNumber": $scope.calibrationTask.installationNumber,
                        "verificationsId": $rootScope.verifIds
                    };
                    console.log(calibrationTask);
                    verificationPlanningTaskService.saveTask(calibrationTask).
                        then(function (data) {
                            if (data.status == 200) {
                                $scope.closeModal();
                                $rootScope.verifIds = [];
                                $modal.open({
                                    animation: true,
                                    templateUrl: '/resources/app/calibrator/views/modals/task-add-success.html',
                                    controllerAs: 'successController',
                                    size: 'md'
                               });
                            } else if (data.status == 409) {
                                $scope.incorrectValue = true;
                                console.log($scope.incorrectValue);
                                $scope.closeModal();
                                $modal.open({
                                    animation: true,
                                    templateUrl: '/resources/app/calibrator/views/modals/task-adding-error.html',
                                    size: 'md'
                                });
                            }
                        });
                }

            }

        }]);
