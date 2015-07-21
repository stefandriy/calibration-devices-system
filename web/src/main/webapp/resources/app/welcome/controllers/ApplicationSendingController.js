angular
    .module('welcomeModule')
    .controller('ApplicationSendingController', ['$scope', '$q', '$state', '$http', '$log',
        'DataReceivingService', 'DataSendingService', '$stateParams', '$window', '$rootScope', 'verificationData','$location',

        function ($scope, $q, $state, $http, $log, dataReceivingService, dataSendingService, $stateParams, $window, $rootScope, verificationData, $location) {
            $scope.isShownForm = true;
            $scope.blockSearchFunctions = false;
            $scope.showEditButton = false;
            $scope.errorEdit = false;
            if (( $stateParams.verificationId)&&(!angular.equals(verificationData.data.status, 'REJECTED'))) {
            	$scope.isShownForm = false;
            	 $scope.errorEdit = true;
            	 $scope.verificationId = $stateParams.verificationId;
            }
            	
            
            if (( $stateParams.verificationId)&&(angular.equals(verificationData.data.status, 'REJECTED'))) {
            	 $scope.showEditButton =  true;

            function arrayObjectIndexOf(myArray, searchTerm, property) {
                for(var i = 0, len = myArray.length; i < len; i++) {
                    if (myArray[i][property] === searchTerm) return i;
                }
                var elem = {
                		id: length,
                		designation: searchTerm
                }
                myArray.push(elem);
                return (myArray.length-1);
            }
            
            $scope.formData = {};
            $scope.formData.lastName = verificationData.data.lastName;
            $scope.formData.firstName = verificationData.data.firstName;
            $scope.formData.middleName = verificationData.data.middleName;
            $scope.formData.email = verificationData.data.email;
            $scope.formData.phone =  verificationData.data.phone;
            $scope.formData.flat = verificationData.data.flat;
     		 
            $scope.blockSearchFunctions = true;
            dataReceivingService.findAllRegions().then(function(respRegions) {	
            	$scope.regions = respRegions.data;
            	var index = arrayObjectIndexOf($scope.regions, verificationData.data.region, "designation");
                $scope.selectedRegion = $scope.regions[index];
             
                dataReceivingService.findDistrictsByRegionId( $scope.selectedRegion.id)
                .then(function (districts) {
                	$scope.districts = districts.data;
                	var index = arrayObjectIndexOf($scope.districts, verificationData.data.district, "designation");
                	$scope.selectedDistrict = $scope.districts[index];
                	
                	dataReceivingService.findLocalitiesByDistrictId($scope.selectedDistrict.id)
                     .then(function (localities) {
                         $scope.localities = localities.data;
                         var index = arrayObjectIndexOf($scope.localities, verificationData.data.locality, "designation");
                         $scope.selectedLocality = $scope.localities[index];
                         
                         dataReceivingService.findProvidersByDistrictDesignation($scope.selectedDistrict.designation)
                         .then(function (providers) {
                             $scope.providers = providers.data;
                            var index = arrayObjectIndexOf($scope.providers, verificationData.data.provider, "designation");
                              $scope.selectedProvider = $scope.providers[index];
                              
                              dataReceivingService.findStreetsByLocalityId( $scope.selectedLocality.id)
                              .then(function (streets) {
                                  $scope.streets = streets.data;
                                	 var index = arrayObjectIndexOf($scope.streets, verificationData.data.street, "designation");
                                	 $scope.selectedStreet = $scope.streets[index];
                                	 
                                	 dataReceivingService.findBuildingsByStreetId( $scope.selectedStreet.id)
                                     .then(function (buildings) {
                                         $scope.buildings = buildings.data;
                                        var index = arrayObjectIndexOf($scope.buildings, verificationData.data.building, "designation");
                                         $scope.selectedBuilding = $scope.buildings[index];
                                         $scope.blockSearchFunctions = false;
                                     });
                              });
                         });
                     });
               });  	
         });
      }    
         
            /**
             * Receives all possible regions.
             */

            $scope.regions = [];
            $scope.receiveRegions = function () {
            dataReceivingService.findAllRegions()
                .success(function (regions) {
                    $scope.regions = regions;
                    $scope.selectedRegion = "";
                    $scope.selectedDistrict = "";
                    $scope.selectedLocality = "";
                    $scope.selectedStreet = "";
                });
             }
            if( !$stateParams.verificationId) {  
            	$scope.receiveRegions();
            }
           
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
            	if(!$scope.blockSearchFunctions) {	
            		$scope.districts = [];
                dataReceivingService.findDistrictsByRegionId(selectedRegion.id)
                    .success(function (districts) {
                        $scope.districts = districts;
                        $scope.selectedDistrict = "";
                        $scope.selectedLocality = "";
                        $scope.selectedStreet = "";
                    });
            	}
            };
            /**
             * Receives all possible localities.
             * On-select handler in district input form element.
             */
            $scope.receiveLocalitiesAndProviders = function (selectedDistrict) {
            	if(!$scope.blockSearchFunctions) {
            	$scope.localities = [];
                dataReceivingService.findLocalitiesByDistrictId(selectedDistrict.id)
                    .success(function (localities) {
                        $scope.localities = localities;
                        $scope.selectedLocality = "";
                        $scope.selectedStreet = "";
                    });

                //Receives providers corresponding this district
                dataReceivingService.findProvidersByDistrict(selectedDistrict.designation)
                    .success(function (providers) {
                    	$log.debug(providers);
                        $scope.providers = providers;
                   $scope.selectedProvider=providers;
                    });
            	}
            };
            /**
             * Receives all possible streets.
             * On-select handler in locality input form element
             */
            $scope.receiveStreets = function (selectedLocality) {
            	if(!$scope.blockSearchFunctions) {
            	$scope.streets = [];
                dataReceivingService.findStreetsByLocalityId(selectedLocality.id)
                    .success(function (streets) {
                        $scope.streets = streets;
                        $scope.selectedStreet = "";
                    });
            	}
            };
            /**
             * Receives all possible buildings.
             * On-select handler in street input form element.
             */
            $scope.receiveBuildings = function (selectedStreet) {
            	if(!$scope.blockSearchFunctions) {
            	$scope.buildings = [];
                dataReceivingService.findBuildingsByStreetId(selectedStreet.id)
                    .success(function (buildings) {
                        $scope.buildings = buildings;
                    });
            	}
            };

            /**
             * Sends data to the server where Verification entity will be created.
             * On-click handler in send button.
             */
            $scope.sendApplicationData = function () {
            	 if( !$stateParams.verificationId) {  
                $scope.$broadcast('show-errors-check-validity');

                if ($scope.clientForm.$valid) {
                    $scope.formData.region = $scope.selectedRegion.designation;
              /*      $scope.formData.device = $scope.selectedDevice;*/
                    $scope.formData.district = $scope.selectedDistrict.designation;
                    $scope.formData.locality = $scope.selectedLocality.designation;
                    $scope.formData.street = $scope.selectedStreet.designation;
                    $scope.formData.building = $scope.selectedBuilding.designation || $scope.selectedBuilding;
                    $scope.formData.providerId = $scope.selectedProvider.id;
                    $scope.formData.deviceId = $scope.selectedDevice.id;
                    

                    dataSendingService.sendApplication($scope.formData)
                        .success(function (applicationCode) {
                            $scope.applicationCode = applicationCode;
                        });

                    //hide form because application status is shown
                    $scope.isShownForm = false;
                }
            }
            };

            $scope.editApplicationData = function () {
            	 if(( $stateParams.verificationId)&&(verificationData.data.status === 'REJECTED')) {  
            		 $scope.$broadcast('show-errors-check-validity');

	                if ($scope.clientForm.$valid) {
	                    $scope.formData.region = $scope.selectedRegion.designation;
	                    $scope.formData.district = $scope.selectedDistrict.designation;
	                    $scope.formData.locality = $scope.selectedLocality.designation;
	                    $scope.formData.street = $scope.selectedStreet.designation;
	                    $scope.formData.building = $scope.selectedBuilding.designation || $scope.selectedBuilding;
	                    $scope.formData.providerId = $scope.selectedProvider.id;
	                    $scope.formData.verificationId = $stateParams.verificationId;
	                    dataSendingService.editApplication($scope.formData)
	                        .success(function (applicationCode) {
	                            $scope.applicationCode = applicationCode;
	                        });
	
	                    //hide form because application status is shown
	                    $scope.isShownForm = false;
	                }
             }
            };
            
            $scope.closeAlert = function () {
               	$location.path('/resources/app/welcome/views/start.html');
            }
       
            $scope.closeError = function() {
               	$location.path('/resources/app/welcome/views/start.html');
            }
            
            /**
             * Receives all regex for input fields
             * 
             * 
             */
            $scope.FIRST_LAST_NAME_REGEX=/^([A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}|[A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20})$/;
            $scope.MIDDLE_NAME_REGEX=/^[A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457]{1,20}$/;
            $scope.FLAT_REGEX=/^([1-9]{1}[0-9]{0,3}|0)$/;
            $scope.BUILDING_REGEX=/^[1-9]{1}[0-9]{0,3}([A-Za-z]|[\u0410-\u042f\u0407\u0406\u0430-\u044f\u0456\u0457]){0,1}$/;
            $scope.PHONE_REGEX=/^0[1-9]\d{8}$/;
            $scope.PHONE_REGEX_SECOND=/^0[1-9]\d{8}$/;
            $scope.EMAIL_REGEX=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
          
            $scope.checkboxModel = false;
        }]);
