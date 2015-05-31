angular
    .module('providerModule')
    .controller('AddingVerificationsController', ['$scope', '$log', '$modal', 'VerificationService',
        function ($scope, $log, $modal, verificationService) {
            $scope.calibrators = [];
            verificationService.getCalibratorsCorrespondingProvider()
                .success(function (calibrators) {
                    $scope.calibrators = calibrators;
                });

            $scope.saveVerification = function () {
                $scope.form.locality = $scope.selectedLocality.designation;
                $scope.form.street = $scope.selectedStreet.designation;
                $scope.form.building = $scope.selectedBuilding.designation;
                $scope.form.calibrator = $scope.selectedCalibrator;
                verificationService.sendInitiatedVerification($scope.form)
                    .success(function () {
                    });
            };
            $scope.localities = [];
            verificationService.getLocalitiesCorrespondingProvider()
                .success(function (localities) {
                    $scope.localities = localities;
                });


            $scope.receiveStreets = function (selectedLocality) {
                $scope.streets = [];
                verificationService.getStreetsCorrespondingLocality(selectedLocality)
                    .success(function (streets) {
                        $scope.streets = streets;
                    });
            };
            $scope.receiveBuildings = function (selectedStreet) {
                $scope.buildings = [];
                verificationService.getBuildingsCorrespondingStreet(selectedStreet)
                    .success(function (buildings) {
                        $scope.buildings = buildings;
                    });
            };
        }]);
