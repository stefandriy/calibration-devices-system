angular
    .module('employeeModule')
    .controller('VerificationTeamController', ['$rootScope', '$scope', '$modal',
        'DisassemblyTeamServiceCalibrator', '$timeout', '$filter', 'toaster',
        function ($rootScope, $scope, $modal, disassemblyTeamServiceCalibrator, $timeout, $filter, toaster) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];
            alert("verification team");
            /**
             * Updates the table with DisassemblyTeams
             */
            $rootScope.onTableHandling = function () {
                disassemblyTeamServiceCalibrator
                    .getPage($scope.currentPage, $scope.itemsPerPage, $scope.searchData)
                    .then(function (data) {
                        $scope.pageContent = data.content;
                        $scope.totalItems = data.totalItems;
                    })
            };
            $rootScope.onTableHandling();


            /**
             * Opens modal window for adding new team
             */
            $scope.openAddDisassemblyTeamModal = function () {
                var addTeamModal = $modal
                    .open({
                        animation: true,
                        controller: 'VerificationTeamAddModalController',
                        templateUrl: 'resources/app/verificator/views/modals/verification-team-add-modal.html'
                    })
            };

            /**
             * Opens modal window for editing team
             */
            $scope.openEditDisassemblyTeamModal = function (teamId) {
                $rootScope.teamId = teamId;
                disassemblyTeamServiceCalibrator.getDisassemblyTeamWithId($rootScope.teamId)
                    .then(function (data) {
                            var teamDTOModal = $modal
                                .open({
                                    animation: true,
                                    controller: 'VerificationTeamEditModalController',
                                    templateUrl: 'resources/app/verificator/views/modals/verification-team-edit-modal.html',
                                    resolve: {
                                        team: function () {
                                            return data;
                                        }
                                    }
                                })
                        }
                    );
            };

            /**
             * delete team
             */
            $scope.deleteDisassemblyTeam = function (teamId) {
                $rootScope.teamId = teamId;
                disassemblyTeamServiceCalibrator.deleteDisassemblyTeam($rootScope.teamId)
                    .then(function () {
                        toaster.pop('success', $filter('translate')('INFORMATION'),
                            $filter('translate')('SUCCESSFUL_DELETE_TEAM'));
                    });
                $timeout(function () {
                    console.log('delete with timeout');
                    $rootScope.onTableHandling();
                }, 700);
            }

        }]);
