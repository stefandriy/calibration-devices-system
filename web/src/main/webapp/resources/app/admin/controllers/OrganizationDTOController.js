angular
		.module('adminModule')
		.controller(
				'OrganizationDTOController',
				[
						'$rootScope',
						'$scope',
						'$modalInstance',
						'AddressService',
						'OrganizationService',
						function($rootScope, $scope, $modalInstance,
								addressService, organizationService) {

							$scope.regions = [];
							$scope.districts = [];
							$scope.localities = [];
							$scope.streets = [];
							$scope.buildings = [];

							/**
							 * Finds districts in a given region.
							 * 
							 * @param regionId
							 *            to identify region
							 */
							$scope.onRegionSelected = function(regionId) {
								addressService
										.findDistrictsByRegionId(regionId)
										.then(function(data) {
											$scope.districts = data;
											$rootScope.updateAddressMessage();
										});
							};

							/**
							 * Finds localities in a given district.
							 * 
							 * @param districtId
							 *            to identify district
							 */
							$scope.onDistrictSelected = function(districtId) {
								addressService.findLocalitiesByDistrictId(
										districtId).then(function(data) {
									$scope.localities = data;
									$rootScope.updateAddressMessage();
								});
							};

							/**
							 * Finds streets in a given locality.
							 * 
							 * @param localityId
							 *            to identify locality
							 */
							$scope.onLocalitySelected = function(localityId) {
								addressService.findStreetsByLocalityId(
										localityId).then(function(data) {
									$scope.streets = data;
									$rootScope.updateAddressMessage();
								});
							};

							/**
							 * Finds buildings in a given street.
							 * 
							 * @param streetId
							 *            to identify street
							 */
							$scope.onStreetSelected = function(streetId) {
								addressService
										.findBuildingsByStreetId(streetId)
										.then(function(data) {
											$scope.buildings = data;
											$rootScope.updateAddressMessage();
										});
							};

							$scope.onBuildingSelected = function() {
								$rootScope.updateAddressMessage();
							};

							/**
							 * Init form data from $rootScope and loads needed
							 * dependency
							 */
							function initFormData() {
								if (!$rootScope.address.region) {
									addressService.findAllRegions().then(
											function(data) {
												$scope.regions = data;
											});
								} else if (!$rootScope.address.district) {
									$scope
											.onRegionSelected($rootScope.address.region.id);
								} else if (!$rootScope.address.locality) {
									$scope
											.onDistrictSelected($rootScope.address.district.id);
								} else if (!$rootScope.address.street) {
									$scope
											.onLocalitySelected($rootScope.address.locality.id);
								} else if (!$rootScope.address.building) {
									$scope
											.onStreetSelected($rootScope.address.street.id);
								}
							}

							initFormData();

							/**
							 * Closes the modal window.
							 */
							$scope.closeModal = function() {
								$rootScope.updateAddressMessage();
								$modalInstance.close();
							};

						} ]);