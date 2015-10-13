angular.module('employeeModule').controller('AddingVerificationsControllerProvider', ['$scope', '$state', '$http', '$log',
    'AddressServiceProvider', 'VerificationServiceProvider', '$stateParams',
    '$rootScope', '$location', '$window', '$modalInstance','$filter',

    function ($scope, $state, $http, $log, addressServiceProvider, verificationServiceProvider, $stateParams, $rootScope, $location, $window, $modalInstance) {
        $scope.isShownForm = true;
        $scope.isCalibrator = -1;
        $scope.calibratorDefined = false;

        /**
         * Receives all regex for input fields
         */
        $scope.selectedCount = '0';
        $scope.FIRST_LAST_NAME_REGEX = /^([A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457']{1,20}\u002d[A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457']{1,20})$/;
        $scope.MIDDLE_NAME_REGEX = /^[A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457']{1,20}$/;
        $scope.FLAT_REGEX = /^([1-9][0-9]{0,3}|0)$/;
        $scope.BUILDING_REGEX = /^[1-9][0-9]{0,3}([A-Za-z]|[\u0410-\u042f\u0407\u0406\u0430-\u044f\u0456\u0457])?$/;
        $scope.PHONE_REGEX = /^[1-9]\d{8}$/;
        $scope.PHONE_REGEX_SECOND = /^[1-9]\d{8}$/;
        $scope.EMAIL_REGEX = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;

        $scope.checkboxModel = false;

        $scope.regions = [];
        $scope.devices = [];
        $scope.localities = [];
        $scope.providers = [];
        $scope.calibrators = [];
        $scope.streetsTypes = [];

        $scope.selectedData={};
        $scope.selectedStreetType = "";

        $scope.applicationCodes = [];
        $scope.codes = [];

        /**
         * Closes modal window on browser's back/forward button click.
         */
        $rootScope.$on('$locationChangeStart', function () {
            $modalInstance.close();
        });

        addressServiceProvider.checkOrganizationType().success(function (response) {
            $scope.isCalibrator = response;
        });

        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for (var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return 0;
        }

        /**
         * Receives all possible regions.
         */
        $scope.receiveRegions = function () {
            addressServiceProvider.findAllRegions()
                .success(function (regions) {
                    $scope.regions = regions;
                    $scope.selectedData.region = "";
                    $scope.selectedData.district = "";
                    $scope.selectedData.locality = "";
                    $scope.selectedStreet = "";
                });
        };

        $scope.receiveRegions();

        /**
         * Receives all possible devices.
         */
        addressServiceProvider.findAllDevices()
            .success(function (devices) {
                $scope.devices = devices;
                $log.debug('device');
                $log.debug(devices);
                $scope.selectedDevice = [];  //$scope.devices[0];
                $log.debug($scope.selectedCount);
            });
        /**
         * Receives all possible districts.
         * On-select handler in region input form element.
         */
        $scope.receiveDistricts = function (selectedRegion) {
            $scope.districts = [];
            addressServiceProvider.findDistrictsByRegionId(selectedRegion.id)
                .success(function (districts) {
                    $scope.districts = districts;
                    $scope.selectedData.district = "";
                    $scope.selectedData.locality = "";
                    $scope.selectedStreet = "";
                });
        };
        /**
         * Receives all possible localities.
         * On-select handler in district input form element.
         */
        $scope.receiveLocalitiesAndProviders = function (selectedDistrict) {
            addressServiceProvider.findLocalitiesByDistrictId(selectedDistrict.id)
                .success(function (localities) {
                    $scope.localities = localities;
                    $scope.selectedData.locality = "";
                    $scope.selectedStreet = "";
                });
            addressServiceProvider.findCalibratorsByDistrict(selectedDistrict.designation)
                .success(function (calibrators) {
                    $scope.calibrators = calibrators;
                    $scope.selectedCalibrator = "";
                    if ($scope.isCalibrator > 0) {
                        var index = arrayObjectIndexOf($scope.calibrators, $scope.isCalibrator, "id");
                        $scope.selectedCalibrator = $scope.calibrators[index];
                    }
                });
        };

        addressServiceProvider.findStreetsTypes().success(function (streetsTypes) {
            $scope.streetsTypes = streetsTypes;
            $scope.selectedStreetType = "";
            $log.debug("$scope.streetsTypes");
            $log.debug($scope.streetsTypes);
        });

        /**
         * Receives all possible streets.
         * On-select handler in locality input form element
         */
        $scope.receiveStreets = function (selectedLocality, selectedDistrict) {
            if (!$scope.blockSearchFunctions) {
                $scope.streets = [];
                addressServiceProvider.findStreetsByLocalityId(selectedLocality.id)
                    .success(function (streets) {
                        $scope.streets = streets;
                        $scope.selectedStreet = "";
                    });
                $scope.indexes = [];
                addressServiceProvider.findMailIndexByLocality(selectedLocality.designation, selectedDistrict.id)
                    .success(function (indexes) {
                        $scope.indexes = indexes;
                        $scope.selectedData.index = indexes[0];
                    });
            }
        };
        /**
         * Receives all possible buildings.
         * On-select handler in street input form element.
         */
        $scope.receiveBuildings = function (selectedStreet) {
            $scope.buildings = [];
            addressServiceProvider.findBuildingsByStreetId(selectedStreet.id)
                .success(function (buildings) {
                    $scope.buildings = buildings;
                });
        };

        /**
         * Sends data to the server where Verification entity will be created.
         * On-click handler in send button.
         */
        $scope.isMailValid = true;
        $scope.sendApplicationData = function () {
            $scope.$broadcast('show-errors-check-validity');
            if ($scope.clientForm.$valid) {
                $scope.formData.region = $scope.selectedData.region.designation;
                $scope.formData.district = $scope.selectedData.district.designation;
                $scope.formData.locality = $scope.selectedData.locality.designation;
                $scope.formData.street = $scope.selectedStreet.designation || $scope.selectedStreet;
                $scope.formData.building = $scope.selectedBuilding.designation || $scope.selectedBuilding;
                $scope.formData.calibratorId = $scope.selectedCalibrator.id;
                $scope.formData.deviceId = $scope.selectedDevice.id;

                verificationServiceProvider.sendInitiatedVerification($scope.formData)
                    .success(function (applicationCode) {
                        $scope.applicationCode = applicationCode;
                    });

                verificationServiceProvider.checkMailIsExist($scope.formData)
                                       .success(function (isMailValid) {
                                                $scope.isMailValid = isMailValid;
                                            });

                //hide form because application status is shown
                $scope.isShownForm = false;
            }
        };

        $scope.closeAlert = function () {
            $modalInstance.close();
        };
    }
]);
