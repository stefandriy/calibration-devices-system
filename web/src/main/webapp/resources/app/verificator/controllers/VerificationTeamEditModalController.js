angular.module('employeeModule')
    .controller('VerificationTeamEditModalController',
    ['$rootScope', '$scope', '$modalInstance', 'DisassemblyTeamServiceCalibrator', '$log', '$filter', 'toaster', 'team',
        function ($rootScope, $scope, $modalInstance, DisassemblyTeamServiceCalibrator, $log, $filter, toaster, team) {

            $scope.team = team;

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

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


            $scope.editDisassemblyTeam = function () {
                var specializations = [];
                for (var i = 0; i < $scope.team.specialization.length; i++) {
                    specializations[i] = $scope.team.specialization[i].type;
                }
                $scope.teamFormData = {
                    teamNumber: $scope.team.teamNumber,
                    teamName: $scope.team.teamName,
                    effectiveTo: $scope.team.effectiveTo,
                    specialization: specializations,
                    leaderFullName: $scope.team.leaderFullName,
                    leaderPhone: $scope.team.leaderPhone,
                    leaderEmail: $scope.team.leaderEmail
                };
                DisassemblyTeamServiceCalibrator.editDisassemblyTeam(
                    $scope.teamFormData,
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

            //TODO fix regex
            $scope.TEAM_USERNAME_REGEX = /[a-z0-9_-]{3,16}/;
            $scope.TEAM_NAME_REGEX = /([A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20})/;
            $scope.TEAM_LEADER_FULL_NAME_REGEX = /([A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20})/;
            $scope.PHONE_REGEX = /^[1-9]\d{8}$/;
            $scope.EMAIL_REGEX = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;

        }]);

