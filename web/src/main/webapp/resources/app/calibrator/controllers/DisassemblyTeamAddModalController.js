angular.module('employeeModule')
    .controller('DisassemblyTeamAddModalControllerCalibrator',
    ['$rootScope', '$scope', '$modalInstance', 'DisassemblyTeamServiceCalibrator', '$modal',
        function ($rootScope, $scope, $modalInstance, DisassemblyTeamServiceCalibrator, $modal) {


            var teamData = {};
            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function() {
                $modalInstance.close();
            });

            /**
             * Resets Team form
             */
            $scope.resetTeamForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.teamNumber = null;
                $scope.teamFormData = null;
                //$scope.teamFormData = null;
                //$scope.nameValidation = null;
                //$scope.deviceTypeValidation = null;
                //$scope.manufacturerValidation = null;
                //$scope.verificationIntervalValidation = null;
                //$scope.incorrectValue = false;
            };
            $scope.resetTeamForm();

            function retranslater() {
                teamData = {
                    teamNumber: $scope.teamFormData.teamNumber
                    //firstName: $scope.employeeFormData.firstName,
                }
            }

            /**
             * Validates team form before saving
             */
            $scope.onTeamFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if (!$scope.teamNumberValidation.isValid) {
                    saveDisassemblyTeam();
                    $modal.open({
                        animation: true,
                        templateUrl: '/resources/app/calibrator/views/modals/disassembly-team-adding-success.html',
                        controller: function ($modalInstance) {
                            this.ok = function () {
                                $modalInstance.close();
                            }
                        },
                        controllerAs: 'successController',
                        size: 'md'
                    });
                } else {
                    $scope.incorrectValue = true;
                }
            };

            /**
             * Saves new team from the form in database.
             * If everything is ok then resets the team
             * form and updates table with teams.
             */
            function saveDisassemblyTeam() {
                DisassemblyTeamServiceCalibrator.saveDisassemblyTeam(
                    $scope.teamFormData).then(
                    function (data) {
                        if (data == 201) {
                            $scope.closeModal();
                            $scope.resetTeamForm();
                            $rootScope.onTableHandling();
                        }
                    });
            }


            /**
             * Closes the modal window for adding new
             * team.
             */
            $rootScope.closeModal = function () {
                $modalInstance.close();
            };

            $scope.checkAll = function (caseForValidation) {
                switch (caseForValidation) {
                    case ('teamNumber')://TODO should rewrite it (check if team is already exist)
                        var teamNumber = $scope.teamFormData.teamNumber;
                        if (teamNumber == null) {
                        } else if ($scope.TEAM_USERNAME_REGEX.test(teamNumber)) {
                            validator('teamNumber', false)
                        } else {
                            validator('teamNumber', true);
                        }
                        break;
                    case ('name'):
                        var name = $scope.teamFormData.name;
                        if (name == null) {

                        } else if (/^[a-zA-Z0-9]{5,20}$/.test(name)) {
                            validator('name', false);
                        } else {
                            validator('name', true);
                        }
                        break;
                    case ('effectiveTo'):
                        var effectiveTo = $scope.teamFormData.effectiveTo;
                            if (deviceType == null) {

                        } else if (/^[A-Z0-9]{4,10}$/.test(deviceType)) {
                            validator('deviceType', false);
                        } else {
                            validator('deviceType', true);
                        }
                        break;
                    case ('manufacturer'):
                        var manufacturer = $scope.teamFormData.manufacturer;
                        if (manufacturer == null) {
                        }
                        else if (/^[a-zA-Z0-9]{5,20}$/.test(manufacturer)) {
                            validator('manufacturer', false);
                        } else {
                            validator('manufacturer', true);
                        }
                        break;
                    case ('verificationInterval'):
                        var verificationInterval = $scope.teamFormData.verificationInterval;
                        if (verificationInterval == null) {
                        }
                        else if (/^\d{2,5}$/.test(verificationInterval)) {
                            validator('verificationInterval', false);
                        } else {
                            validator('verificationInterval', true);
                        }
                        break;
                }

            }

            function validator(caseForValidation, isValid) {
                switch (caseForValidation) {
                    case ('name'):
                        $scope.nameValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('deviceType'):
                        $scope.deviceTypeValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('manufacturer'):
                        $scope.manufacturerValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('verificationInterval'):
                        $scope.verificationIntervalValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                }
            }

        $scope.TEAM_USERNAME_REGEX = /^[a-z0-9_-]{3,16}$/;

        }]);
