angular
    .module('welcomeModule')
    .controller('ApplicationSendingController', ['$scope', '$q', '$state', '$http', '$log',
        'DataReceivingService', 'DataSendingService', '$stateParams', '$window', '$rootScope','$location','$modal','$filter',

        function ($scope, $q, $state, $http, $log, dataReceivingService, dataSendingService, $stateParams, $window, $rootScope, $location,$modal,$filter) {
            $scope.isShownForm = true;
            $scope.blockSearchFunctions = false;
            
            $scope.createNew = function (ID) {
				$location.path('/application-sending/' + ID);
			}
            
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
            
            if ($stateParams.verificationId) {
            dataReceivingService.getVerificationById($stateParams.verificationId).then(function (verification) {
            			
            $scope.verification = verification;
            $scope.formData = {};
            $scope.formData.lastName = $scope.verification.data.lastName;
            $scope.formData.firstName = $scope.verification.data.firstName;
            $scope.formData.middleName = $scope.verification.data.middleName;
            $scope.formData.email = $scope.verification.data.email;
            $scope.formData.phone =  $scope.verification.data.phone;
            $scope.formData.flat = $scope.verification.data.flat;
     		 
            $scope.blockSearchFunctions = true;
            dataReceivingService.findAllRegions().then(function(respRegions) {	
            	$scope.regions = respRegions.data;
            	var index = arrayObjectIndexOf($scope.regions,  $scope.verification.data.region, "designation");
                $scope.selectedRegion = $scope.regions[index];
             
                dataReceivingService.findDistrictsByRegionId( $scope.selectedRegion.id)
                .then(function (districts) {
                	$scope.districts = districts.data;
                	var index = arrayObjectIndexOf($scope.districts,  $scope.verification.data.district, "designation");
                	$scope.selectedDistrict = $scope.districts[index];
                	
                	dataReceivingService.findLocalitiesByDistrictId($scope.selectedDistrict.id)
                     .then(function (localities) {
                         $scope.localities = localities.data;
                         var index = arrayObjectIndexOf($scope.localities,  $scope.verification.data.locality, "designation");
                         $scope.selectedLocality = $scope.localities[index];

                         dataReceivingService.findProvidersByDistrict($scope.selectedDistrict.designation)
                         .then(function (providers) {
                             $scope.providers = providers.data;
                             var index = arrayObjectIndexOf($scope.providers,  $scope.verification.data.provider, "designation");
                              $scope.selectedProvider = $scope.providers[index];
                              
                              dataReceivingService.findStreetsByLocalityId( $scope.selectedLocality.id)
                              .then(function (streets) {
                                  $scope.streets = streets.data;
                                	 var index = arrayObjectIndexOf($scope.streets,  $scope.verification.data.street, "designation");
                                	 $scope.selectedStreet = $scope.streets[index];
                                	 
                                	 dataReceivingService.findBuildingsByStreetId( $scope.selectedStreet.id)
                                     .then(function (buildings) {
                                        $scope.buildings = buildings.data;
                                        var index = arrayObjectIndexOf($scope.buildings,  $scope.verification.data.building, "designation");                                         
                                        $scope.selectedBuilding = $scope.buildings[index].designation;

                                         dataReceivingService.findMailIndexByLocality($scope.selectedLocality.designation, $scope.selectedDistrict.id)
                                         .success(function (indexes) {
                                             $scope.indexes = indexes;
                                             $scope.selectedIndex = $scope.indexes[0];
                                             $scope.blockSearchFunctions = false;
                                         });
                                         
                                     });
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
                    $scope.selectedLocality = "";
                   
                });
             }
            if( !$stateParams.verificationId) {  
            	$scope.receiveRegions();
            }
           
           
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

                /**
                 *  Receives providers corresponding this district
                 */
               
                dataReceivingService.findProvidersByDistrict(selectedDistrict.designation)
                    .success(function (providers) {
                    
                        $scope.providers = providers;
                   $scope.selectedProvider=providers[0];


                    });
            	}
            };


            /**
             * Receives all possible streets.
             * On-select handler in locality input form element
             */
            $scope.streetsTypes = [];
            $scope.selectedStreetType="";

                dataReceivingService.findStreetsTypes()
                    .success(function (streetsTypes) {
                        $scope.streetsTypes = streetsTypes;
                        $scope.selectedStreetType = "";
                        $log.debug("$scope.streetsTypes");
                        $log.debug($scope.streetsTypes);

                    });



            $scope.receiveStreets = function (selectedLocality ,selectedDistrict) {
            	if(!$scope.blockSearchFunctions) {
            	$scope.streets = [];
                dataReceivingService.findStreetsByLocalityId(selectedLocality.id)
                    .success(function (streets) {
                        $scope.streets = streets;
                        $scope.selectedStreet = "";
                        $log.debug("$scope.streets");
                        $log.debug($scope.streets);

                    });

                    $scope.indexes = [];
                    dataReceivingService.findMailIndexByLocality(selectedLocality.designation,selectedDistrict.id)
                        .success(function (indexes) {
                            $scope.indexes = indexes;
                            if (indexes.length>0){
                            $scope.selectedIndex=indexes[0];}
                            $log.debug("$scope.indexes");
                            $log.debug($scope.indexes);

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
             * Receives all possible devices.
             */
            $scope.devices = [];
            $scope.selectedDevice1=[];
            $scope.selectedDevice2=[];
            $scope.selectedCount="";
            $scope.devicesByType = [];
            $scope.selectedType="";
            $scope.devicesType=[];


            dataReceivingService.findAllDevices()
                .success(function (devices) {
                      $scope.devices = devices;
                      $scope.selectedDevice =[];
                
                });

            /**
             * Sends data to the server where Verification entity will be created.
             * On-click handler in send button.
             */
            $log.debug( "$scope.devices");
            $log.debug( $scope.devices);
            $scope.applicationCodes=[];

            $scope.allSelectedDevices=[];

            $scope.sendApplicationData = function () {
                $scope.codes=[];
                $log.debug( " $scope.codeslength");
                $log.debug( $scope.codes.length);
                $scope.allSelectedDevices=$scope.selectedDevice.concat($scope.selectedDevice1,$scope.selectedDevice2);
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.clientForm.$valid) {
                    $scope.isShownForm = false;
                    $log.debug( "$scope.selectedDevice");
                    $log.debug( $scope.selectedDevice);
                    $log.debug( "$scope.allSelectedDevices");
                    $log.debug( $scope.allSelectedDevices);
                    for ( var i=0; i< $scope.allSelectedDevices.length;i++){
                    $scope.formData.region = $scope.selectedRegion.designation;
                    $scope.formData.district = $scope.selectedDistrict.designation;
                    $scope.formData.locality = $scope.selectedLocality.designation;
                    $scope.formData.street = ($scope.selectedStreet.designation+" "+$scope.selectedStreetType.designation) ||($scope.selectedStreet + " "+$scope.selectedStreetType.designation) ;
                    $scope.formData.building = $scope.selectedBuilding.designation || $scope.selectedBuilding;
                    $scope.formData.providerId = $scope.selectedProvider.id;
                    $scope.formData.deviceId = $scope.allSelectedDevices[i].id;
                    $scope.applicationCodes.push(dataSendingService.sendApplication($scope.formData))
                          }
                    $q.all($scope.applicationCodes).then(function(values){
                        for ( var i=0; i< $scope.allSelectedDevices.length;i++) {
                            $scope.codes[i]=values[i].data;
                        }
                     });
                    $log.debug( " $scope.codeslength");
                    $log.debug( $scope.codes.length);
                 }
            };
                $scope.closeAlert = function () {
               	$location.path('/resources/app/welcome/views/start.html');
            }
            
            /**
             * Receives all regex for input fields
             * 
             * 
             */

            $scope.STREET_REGEX=/^[a-z\u0430-\u044f\u0456\u0457]{1,20}\s([A-Z\u0410-\u042f\u0407\u0406]{1}[a-z\u0430-\u044f\u0456\u0457\u0454]{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0454]{1}[a-z\u0430-\u044f\u0456\u0457\u0454]{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0454]{1}[a-z\u0430-\u044f\u0456\u0457\u0454]{1,20}){1}$/;
            $scope.FIRST_LAST_NAME_REGEX=/^([A-Z\u0410-\u042f\u0407\u0406\u0454]{1}[a-z\u0430-\u044f\u0456\u0457\u0454]{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0454]{1}[a-z\u0430-\u044f\u0456\u0457\u0454]{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0454]{1}[a-z\u0430-\u044f\u0456\u0457\u0454]{1,20})$/;
            $scope.MIDDLE_NAME_REGEX=/^[A-Z\u0410-\u042f\u0407\u0406\u0454]{1}[a-z\u0430-\u044f\u0456\u0457\u0454]{1,20}$/;
            $scope.FLAT_REGEX=/^([1-9]{1}[0-9]{0,3}|0)$/;
            $scope.BUILDING_REGEX=/^[1-9]{1}[0-9]{0,3}([A-Za-z]|[\u0410-\u042f\u0407\u0406\u0430-\u044f\u0456\u0457]){0,1}$/;
            $scope.PHONE_REGEX=/^0[1-9]\d{8}$/;
            $scope.PHONE_REGEX_SECOND=/^0[1-9]\d{8}$/;
            $scope.EMAIL_REGEX=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
            $scope.checkboxModel = false;
            /**
             * Modal window used to send questions to administration
             */
            $scope.responseSuccess = false;
            $scope.showSendingAlert = false;
            $scope.feedbackModalNoProvider = function () {
               var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/welcome/views/modals/feedBackWindow.html',
                    controller: 'FeedbackController',
                    size: 'md'

                });

                /**
                 * executes when modal closing
                 */
                modalInstance.result.then(function (formData, sendingStarted) {
                    var messageToSend = {
                        verifID :  $filter('translate')('NOTFOUND_TRANSLATION'),
                        msg : formData.message,
                        name:formData.firstName,
                        surname:formData.lastName,
                        email:formData.email
                    };
                    var idInfo=function(){
                            return $filter('translate')('PHONE')
                    };
                    $scope.showSendingAlert = true;
                    dataSendingService.sendMailNoProvider (messageToSend)
                        .success(function () {
                            $scope.responseSuccess = true;
                            $scope.showSendingAlert = false;
                            $log.debug('response val');
                            $log.debug($scope.responseSuccess);
                        });
                });

            };

            $scope.closeAlertW = function () {
                $scope.responseSuccess = false;
                $scope.showSendingAlert = false;
            }


        }]);
