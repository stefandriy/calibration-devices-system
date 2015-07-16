angular
    .module('welcomeModule')
    .controller('ApplicationSendingController', ['$scope', '$state', '$http', '$log',
        'DataReceivingService', 'DataSendingService',

        function ($scope, $state, $http, $log, dataReceivingService, dataSendingService) {

            $scope.isShownForm = true;

            /**
             * Receives all possible regions.
             */
            $scope.regions = [];
            dataReceivingService.findAllRegions()
                .success(function (regions) {
                    $scope.regions = regions;
                });
            /**
             * Receives all possible devices.
             */
            $scope.devices = [];
            dataReceivingService.findAllDevices()
                .success(function (devices) {
                    $scope.devices = devices;
                });
            /**
             * Receives all possible districts.
             * On-select handler in region input form element.
             */
            $scope.receiveDistricts = function (selectedRegion) {
                $scope.districts = [];
                dataReceivingService.findDistrictsByRegionId(selectedRegion.id)
                    .success(function (districts) {
                        $scope.districts = districts;
                        $scope.selectedDistrict = "";
                        $scope.selectedLocality = "";
                        $scope.selectedStreet = "";
                    });
            };
            /**
             * Receives all possible localities.
             * On-select handler in district input form element.
             */
            $scope.receiveLocalitiesAndProviders = function (selectedDistrict) {
                $scope.localities = [];
                dataReceivingService.findLocalitiesByDistrictId(selectedDistrict.id)
                    .success(function (localities) {
                        $scope.localities = localities;
                        $scope.selectedLocality = "";
                        $scope.selectedStreet = "";
                    });

                //Receives providers corresponding this district
                dataReceivingService.findProvidersByDistrictDesignation(selectedDistrict.designation)
                    .success(function (providers) {
                        $scope.providers = providers;
                    });
            };
            /**
             * Receives all possible streets.
             * On-select handler in locality input form element
             */
            $scope.receiveStreets = function (selectedLocality) {
                $scope.streets = [];
                dataReceivingService.findStreetsByLocalityId(selectedLocality.id)
                    .success(function (streets) {
                        $scope.streets = streets;
                        $scope.selectedStreet = "";
                    });
            };
            /**
             * Receives all possible buildings.
             * On-select handler in street input form element.
             */
            $scope.receiveBuildings = function (selectedStreet) {
                $scope.buildings = [];
                dataReceivingService.findBuildingsByStreetId(selectedStreet.id)
                    .success(function (buildings) {
                        $scope.buildings = buildings;
                    });
            };

            /**
             * Sends data to the server where Verification entity will be created.
             * On-click handler in send button.
             */
            $scope.sendApplicationData = function () {
                $scope.$broadcast('show-errors-check-validity');

                if ($scope.clientForm.$valid) {
                    $scope.formData.region = $scope.selectedRegion.designation;
                    $scope.formData.device = $scope.selectedDevice;
                    $scope.formData.district = $scope.selectedDistrict.designation;
                    $scope.formData.locality = $scope.selectedLocality.designation;
                    $scope.formData.street = $scope.selectedStreet.designation;
                    $scope.formData.building = $scope.selectedBuilding.designation || $scope.selectedBuilding;
                    $scope.formData.providerId = $scope.selectedProvider.id;

                    dataSendingService.sendApplication($scope.formData)
                        .success(function (applicationCode) {
                            $scope.applicationCode = applicationCode;
                        });

                    //hide form because application status is shown
                    $scope.isShownForm = false;
                }
            };

            $scope.closeAlert = function () {
                $state.go($state.current, {}, {reload: true});
            }
            
            /**
             * Receives all regex for input fields
             * 
             * 
             */
            $scope.FIRST_LAST_NAME_REGEX=/^([A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}|[A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20})$/;
            $scope.MIDDLE_NAME_REGEX=/^[A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}$/;
            $scope.FLAT_REGEX=/^([1-9]{1}[0-9]{0,3}|0)$/;
            $scope.BUILDING_REGEX=/^[1-9]{1}[0-9]{0,3}([A-Za-z])|([\u0410-\u042f\u0407\u0406\u0430-\u044f\u0456\u0457]){0,1}$/;
            $scope.PHONE_REGEX=/^0[1-9]\d{8}$/;
            $scope.EMAIL_REGEX=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
          
            $scope.checkboxModel = false;
        }]);
