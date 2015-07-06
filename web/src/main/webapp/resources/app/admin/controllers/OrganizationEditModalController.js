angular
		.module('adminModule')
		.controller(
				'OrganizationEditModalController',
				[
						'$rootScope',
						'$scope',
						'$modalInstance',
						'AddressService',
						'OrganizationService',
						function($rootScope, $scope, $modalInstance,
								addressService, organizationService) {

							$scope.typeData = [ {
								type : 'PROVIDER',
								name : 'Постачальник послуг'
							}, {
								type : 'CALIBRATOR',
								name : 'Повірочна організація'
							}, {
								type : 'STATE_VERIFICATION',
								name : 'Державний повірник'
							} ];
							$scope.regions = null;
							$scope.districts = [];
							$scope.localities = [];
							$scope.streets = [];
							$scope.buildings = [];

							/**
							 * Finds all regions
							 */
							function initFormData() {
								if (!$scope.regions) {
									addressService.findAllRegions().then(
											function(data) {
												$scope.regions = data;
											});
								}
							}

							initFormData();

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
										});
							};

							/**
							 * Resets organization form
							 */
							$scope.resetOrganizationForm = function() {
								$scope.$broadcast('show-errors-reset');
								$rootScope.organization = null;
							};

							/**
							 * Calls resetOrganizationForm after the view loaded
							 */
							$scope.resetOrganizationForm();

							/**
							 * Transfers address data from modal window to
							 * current scope
							 */
							function addressFormToOrganizationForm() {
								if (typeof $rootScope.organization.region == 'object') {
									$rootScope.organization.region = $rootScope.organization.region.designation;
								}
								if (typeof $rootScope.organization.district == 'object') {
									$rootScope.organization.district = $rootScope.organization.district.designation;
								}
								if (typeof $rootScope.organization.locality == 'object') {
									$rootScope.organization.locality = $rootScope.organization.locality.designation;
								}
								if (typeof $rootScope.organization.street == 'object') {
									$rootScope.organization.street = $rootScope.organization.street.designation;
								}
								if (typeof $rootScope.organization.building == 'object') {
									$rootScope.organization.building = $rootScope.organization.building.designation;
								}
							}

							/**
							 * Edit organization. If everything is ok then
							 * resets the organization form and closes modal
							 * window.
							 */
							$scope.editOrganization = function() {
								addressFormToOrganizationForm();
								organizationService.editOrganization(
										$rootScope.organization,
										$rootScope.organizationId).then(
										function(data) {
											if (data == 200) {
												$scope.closeModal();
												$scope.resetOrganizationForm();
												$rootScope.onTableHandling();
											}
										});
							}

							/**
							 * Closes edit modal window.
							 */
							$scope.closeModal = function() {
								$modalInstance.close();
							};

						} ]);
