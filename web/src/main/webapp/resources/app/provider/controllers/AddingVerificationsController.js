angular
    .module('providerModule')
    .controller('AddingVerificationsController', ['$scope', '$log', '$modal', 'VerificationService', '$state',
        function ($scope, $log, $modal, verificationService, $state) {
            $scope.calibrators = [];
            /**
             * Receives all possible calibrators.
             */
            verificationService.getCalibratorsCorrespondingProvider()
                .success(function (calibrators) {
                    $scope.calibrators = calibrators;
                });

            $scope.resetForm = function () {
                $state.go($state.current, {}, {reload: true});
            };

            /**
             * Updates the table with verifications.
             */
            $scope.saveVerification = function () {

                /* if ($scope.form.$valid) {*/
                $scope.form.locality = $scope.selectedLocality.designation;
                $scope.form.street = $scope.selectedStreet.designation;
                $scope.form.building = $scope.selectedBuilding.designation;
                $scope.form.calibrator = $scope.selectedCalibrator;
                verificationService.sendInitiatedVerification($scope.form)
                    .success(function () {
                        $modal.open({
                            animation: true,
                            templateUrl: '/resources/app/provider/views/modals/verification-adding-success.html',
                            controller: function ($modalInstance) {
                                this.ok = function () {
                                    $modalInstance.close();
                                }
                            },
                            controllerAs: 'successController',
                            size: 'md'
                        });
                            $state.go($state.current, {}, {reload: true});
                    });
            };
               /* else {
                    $scope.$broadcast('show-errors-check-validity');
                }*/

            /**
             * Receives all possible localities.
             */

            $scope.localities = [];
            verificationService.getLocalitiesCorrespondingProvider()
                .success(function (localities) {
                    $scope.localities = localities;
                });
            /**
             * Receives all possible streets.
             */
            $scope.receiveStreets = function (selectedLocality) {
                $scope.streets = [];
                verificationService.getStreetsCorrespondingLocality(selectedLocality)
                    .success(function (streets) {
                        $scope.streets = streets;
                    });
            };
            /**
             * Receives all possible buildings.
             */
            $scope.receiveBuildings = function (selectedStreet) {
                $scope.buildings = [];
                verificationService.getBuildingsCorrespondingStreet(selectedStreet)
                    .success(function (buildings) {
                        $scope.buildings = buildings;
                    });
            };
        }]);
