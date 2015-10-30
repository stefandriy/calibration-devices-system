angular
    .module('employeeModule')
    .controller('TaskForTeamModalControllerCalibrator', ['$rootScope', '$scope', '$modal', '$modalInstance', 'VerificationPlanningTaskService', '$log',
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


            $scope.open1 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.firstCalendar.isOpen = true;
            };

            moment.locale('uk');
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false',

            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

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
                $log.debug($scope.calibrationTask.taskDate);
                $scope.calibrationTask.taskDate = null;
            };

            $scope.resetTaskForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.calibrationTask = {};
                $scope.incorrectValue = false;
                $scope.calibrationTask.pickerDate = null;
                $scope.installationNumberValidation = null;
                $scope.showSendingMessage = false;
                $scope.teams = {};
            };

            $scope.teams = [];
            function createTeamArray(data) {
              for (var i = 0; i < data.length; i++) {
                    $scope.teams[i] = {
                        teamName: data[i].teamName,
                        teamNumber: data[i].teamNumber
                    }
              }
            }

            $scope.receiveTeams = function(){
                console.log($scope.calibrationTask.taskDate + " " + $scope.calibrationTask.applicationFiled);
                var taskDate = $scope.calibrationTask.taskDate;
                var applicationFiled = $scope.calibrationTask.applicationFiled;
                verificationPlanningTaskService.getTeams(taskDate, applicationFiled)
                    .then(function (result) {
                        $log.debug(result);
                        $log.debug($scope.teams);
                        //$scope.teams = result.data;
                        createTeamArray(result.data);
                    }, function (result) {
                        $log.debug('error fetching data:', result);
                    });
            }

            $scope.showSendingMessage = false;

            $scope.save = function (){
                if ($rootScope.emptyStatus == true) {
                    $scope.showSendingMessage = true;
                } else {
                    var calibrationTask = {
                        "taskDate": $scope.calibrationTask.taskDate,
                        "serialNumber": $scope.calibrationTask.mountingCrew.teamNumber,
                        "verificationsId": $rootScope.verifIds
                    };
                    console.log(calibrationTask);
                    verificationPlanningTaskService.saveTaskForTeam(calibrationTask).
                        then(function (data) {
                            if (data.status == 200) {
                                $scope.closeModal();
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

