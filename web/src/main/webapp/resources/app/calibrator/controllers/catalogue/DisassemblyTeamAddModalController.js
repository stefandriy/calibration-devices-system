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
                $log.debug($scope.teamFormData.effectiveTo);
                $scope.teamFormData.effectiveTo = null;
            };

            $scope.clearDate2 = function () {
                $log.debug($scope.teamFormData.dateOfVerif);
                $scope.teamFormData.dateOfVerif = null;
            };

            $scope.clearDate3 = function () {
                $log.debug($scope.teamFormData.noWaterToDate);
                $scope.teamFormData.noWaterToDate = null;
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
                //$scope.teamFormData.pickerDate = null;
                $scope.teamFormData = null;
            };

            $scope.resetTeamForm();


            function retranslater() {
                teamData = {
                    teamNumber: $scope.teamFormData.teamNumber,
                    teamName: $scope.teamFormData.teamName,
                    effectiveTo: $scope.teamFormData.effectiveTo,
                    specialization: $scope.teamFormData.specialization,
                    leaderFullName: $scope.teamFormData.leaderFullName,
                    leaderPhone: $scope.teamFormData.leaderPhone,
                    leaderEmail: $scope.teamFormData.leaderEmail
                }
            }


            function isDisassemblyTeamAvailable(teamUsername) {
                DisassemblyTeamServiceCalibrator.isDisassemblyTeamNameAvailable(teamUsername)
                    .then(function (data) {
                        validator('availableTeamLogin', data);
                        //validator('availableTeamLogin', true);
                    })
            }

            $scope.checkAll = function (caseForValidation) {
                switch (caseForValidation) {
                    case ('teamNumber'):
                        var teamNumber = $scope.teamFormData.teamNumber;
                        if (teamNumber == null) {
                        } else if ($scope.TEAM_USERNAME_REGEX.test(teamNumber)) {
                            isDisassemblyTeamAvailable(teamNumber);
                        } else {
                            validator('loginTeamValid', false);
                        }
                        break;
                    case ('time'):
                        var time = $scope.teamFormData.time;
                        if (/^[0-1]{1}[0-9]{1}(\.)[0-9]{2}(\-)[0-2]{1}[0-9]{1}(\.)[0-9]{2}$/.test(time)) {
                            validator('time', false);
                        } else {
                            validator('time', true);
                        }
                        break;
                }

            };

            function validator(caseForValidation, isValid) {
                switch (caseForValidation) {
                    case 'availableTeamLogin' :
                        $scope.teamNumberValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-success' : 'has-error',
                            message: isValid ? undefined : 'Такий логін вже існує'
                };
                        break;
                    case 'loginTeamValid' :
                        $scope.teamNumberValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-success' : 'has-error',
                            message: isValid ? undefined : 'К-сть символів не повинна бути меншою за 3\n і більшою за 16 '
                        };
                        break;
                    case ('time'):
                    $scope.timeValidation = {
                        isValid: isValid,
                        css: isValid ? 'has-error' : 'has-success'
                    }
                    break;
                }
            }


            bValidation = function () {
                if ( $scope.teamNumberValidation === undefined || $scope.teamNumberValidation.isValid === false) {
                    $scope.incorrectValue = true;
                    return false;
                } else {
                    return true;
                }
            }

            /**
             * Validates team form before saving
             */
            $scope.onTeamFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if (bValidation()) {
                        retranslater();
                        saveDisassemblyTeam();
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
                        }
                        if (data == 409) {
                            $scope.closeModal();
                            $scope.resetTeamForm();
                            $rootScope.onTableHandling();
                            $modal.open({
                                animation: true,
                                templateUrl: '/resources/app/calibrator/views/modals/disassembly-team-adding-denied.html',
                                controller: function ($modalInstance) {
                                    this.ok = function () {
                                        $modalInstance.close();
                                    }
                                },
                                controllerAs: 'deniedController',
                                size: 'md'
                            });
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



            //TODO fix regex
            $scope.TEAM_USERNAME_REGEX = /^[a-z0-9_-]{3,16}$/;
            $scope.TEAM_NAME_REGEX = /^([A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20})$/;
            $scope.TEAM_LEADER_FULL_NAME_REGEX = /^([A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20})$/;
            $scope.PHONE_REGEX = /^[1-9]\d{8}$/;
            $scope.EMAIL_REGEX = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;

        }]);
