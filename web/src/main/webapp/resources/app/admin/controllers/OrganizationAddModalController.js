angular
	.module('adminModule')
	.filter('organizationFilter', function() {
		return function(allTypes, currentTypes) {
			var filtered = allTypes;

			for (var i in currentTypes) {
				if (currentTypes[i].id != 'CALIBRATOR') {
					var filtered = [];
					filtered.push(allTypes[1]);
					filtered.push(currentTypes[i]);
				}
			}

			return filtered;
		}
	})
	.controller(
	'OrganizationAddModalController',
	[
		'$rootScope',
		'$scope',
		'$translate',
		'$modalInstance',
		'$filter',
		'AddressService',
		'OrganizationService',
		'UserService',
		function($rootScope, $scope, $translate, $modalInstance, $filter,
				 addressService, organizationService,
				 userService) {

			$scope.typeData = [
				{
					id : 'PROVIDER',
					label : null
				},
				{
					id : 'CALIBRATOR',
					label : null
				},
				{
					id : 'STATE_VERIFICATOR',
					label : null
				}
			];
			
			 /**
             * Closes modal window on browser's back/forward button click.
             */
			$rootScope.$on('$locationChangeStart', function() {
			    $modalInstance.close();
			});
			
			$scope.setTypeDataLanguage = function () {
				var lang = $translate.use();
				if (lang === 'ukr') {
					$scope.typeData[0].label = 'Постачальник послуг';
					$scope.typeData[1].label = 'Вимірювальна лабораторія';
					$scope.typeData[2].label = 'Уповноважена повірочна лабораторія';
				} else if (lang === 'eng') {
					$scope.typeData[0].label = 'Service provider';
					$scope.typeData[1].label = 'Measuring laboratory';
					$scope.typeData[2].label = 'Authorized calibration laboratory';
				} else {
					console.error(lang);
				}
			};

			$scope.setTypeDataLanguage();


			$scope.regions = null;
			$scope.districts = [];
			$scope.localities = [];
			$scope.streets = [];
			$scope.buildings = [];


			/**
			 * Resets organization form
			 */
			$scope.resetOrganizationForm = function() {
				$scope.$broadcast('show-errors-reset');
				$scope.organizationFormData = null;
			};

			/**
			 * Calls resetOrganizationForm after the view loaded
			 */
			//$scope.resetOrganizationForm();

			/**
			 * Checks whereas given username is available to use
			 * for new user
			 *
			 */

			$scope.isUsernameAvailable = true;

			$scope.checkIfUsernameIsAvailable = function() {
				var username = $scope.organizationFormData.username;
				userService.isUsernameAvailable(username).then(
					function(data) {
						$scope.isUsernameAvailable = data;
					})
			}

			$scope.isPasswordsEqual = true;

			$scope.checkRePassword = function() {
				var password = $scope.organizationFormData.password;
				var rePassword = $scope.organizationFormData.rePassword;
				if (password != rePassword) {
					$scope.isPasswordsEqual = false;
				} else {
					$scope.isPasswordsEqual = true;
				}
			}

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
			 * Convert address data to string
			 */
			function addressFormToOrganizationForm() {
				$scope.organizationFormData.region = $scope.organizationFormData.region.designation;
				$scope.organizationFormData.district = $scope.organizationFormData.district.designation;
				$scope.organizationFormData.locality = $scope.organizationFormData.locality.designation;
				$scope.organizationFormData.street = $scope.organizationFormData.street.designation ;
				$scope.organizationFormData.building = $scope.organizationFormData.building;
				$scope.organizationFormData.flat = $scope.organizationFormData.flat;
			}

			function objectTypesToStringTypes() {
				for (var i in $scope.organizationFormData.types) {
					$scope.organizationFormData.types[i] = $scope.organizationFormData.types[i].id;
				}


			}

			/**
			 * Validates organization form before saving
			 */
			$scope.onOrganizationFormSubmit = function() {
				$scope.$broadcast('show-errors-check-validity');
				if ($scope.organizationForm.$valid) {
					addressFormToOrganizationForm();
					objectTypesToStringTypes();
					saveOrganization();
				}
			};

			/**
			 * Saves new organization from the form in database.
			 * If everything is ok then resets the organization
			 * form and updates table with organizations.
			 */
			function saveOrganization() {
				console.log($scope.organizationFormData);
				organizationService.saveOrganization(
					$scope.organizationFormData).then(
					function(data) {
						if (data == 201) {
							$scope.closeModal();
							$scope.resetOrganizationForm();
							$rootScope.onTableHandling();
						}
					});
			}

			/**
			 * Closes the modal window for adding new
			 * organization.
			 */
			$rootScope.closeModal = function() {
				$modalInstance.close();
			};

			$scope.ORGANIZATION_NAME_REGEX = /^(?=.{5,50}$).*/;
			$scope.PHONE_REGEX = /^0[1-9]\d{8}$/;
			$scope.EMAIL_REGEX = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
			$scope.USERNAME_REGEX = /^[a-z0-9_-]{3,16}$/;
			$scope.PASSWORD_REGEX = /^(?=.{4,20}$).*/;
			$scope.BUILDING_REGEX = /^[1-9]{1}[0-9]{0,3}([A-Za-z]|[\u0410-\u042f\u0407\u0406\u0430-\u044f\u0456\u0457]){0,1}$/;
			$scope.FLAT_REGEX=/^([1-9]{1}[0-9]{0,3}|0)$/;

		} ]);
