angular.module('employeeModule')
    .controller('DisassemblyTeamEditModalControllerCalibrator',
    ['$rootScope', '$scope', '$modalInstance', 'DisassemblyTeamServiceCalibrator', '$log', '$filter', 'toaster', 'team',
        function ($rootScope, $scope, $modalInstance, DisassemblyTeamServiceCalibrator, $log, $filter, toaster, team) {

            $scope.team = team;
            console.log(team);

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            /**
             * init ui-select
             */
            $scope.deviceTypeData = [
                {
                    type: 'WATER',
                    label: $filter('translate')('WATER')
                },
                {
                    type: 'THERMAL',
                    label: $filter('translate')('THERMAL')
                }
            ];

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

            moment.locale('uk');
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'

            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

            // Disable weekend selection
            $scope.disabled = function (date, mode) {
                return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
            };

            $scope.toggleMin = function () {
                $scope.minDate = $scope.minDate ? null : new Date();
            };

            $scope.toggleMin();
            $scope.maxDate = new Date(2100, 5, 22);


            $scope.clearDate1 = function () {
                $log.debug($scope.team.effectiveTo);
                $scope.team.effectiveTo = null;
            };

            $scope.clearDate2 = function () {
                $log.debug($scope.team.dateOfVerif);
                $scope.team.dateOfVerif = null;
            };

            $scope.clearDate3 = function () {
                $log.debug($scope.team.noWaterToDate);
                $scope.team.noWaterToDate = null;
            };

            /**
             * Resets Team form
             */
            $scope.resetTeamForm = function () {
                $scope.$broadcast('show-errors-reset');
                if ($scope.teamForm) {
                    $scope.teamForm.$setPristine();
                    $scope.teamForm.$setUntouched();
                }
                $scope.teamNumber = null;
                $scope.team = null;
            };

            /**
             * Edit team. If everything is ok then
             * resets the team form and closes modal
             * window.
             */

            $scope.team.specialization = {
                type: team.specialization,
                label: $filter('translate')(team.specialization)
            };
            $scope.editDisassemblyTeam = function () {
                $scope.teamForm = {
                    teamNumber: $scope.team.teamNumber,
                    teamName: $scope.team.teamName,
                    effectiveTo: $scope.team.effectiveTo,
                    specialization: $scope.team.specialization.type,
                    leaderFullName: $scope.team.leaderFullName,
                    leaderPhone: $scope.team.leaderPhone,
                    leaderEmail: $scope.team.leaderEmail
                };
                DisassemblyTeamServiceCalibrator.editDisassemblyTeam(
                    $scope.teamForm,
                    $rootScope.teamId).then(
                    function (data) {
                        if (data == 200) {
                            $scope.closeModal();
                            $scope.resetTeamForm();
                            $rootScope.onTableHandling();
                            toaster.pop('success', $filter('translate')('INFORMATION'),
                                $filter('translate')('SUCCESSFUL_EDIT_TEAM'));
                        }
                    });
            };

            /**
             * Closes edit modal window.
             */
            $scope.closeModal = function () {
                $modalInstance.close();
            };
        }]);

