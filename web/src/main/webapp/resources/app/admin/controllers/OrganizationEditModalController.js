angular
	.module('adminModule')
	.filter('organizationFilter', function () {
		return function (allTypes, currentTypes) {
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
	'OrganizationEditModalController',
	[
		'$rootScope',
		'$scope',
		'$translate',
		'$modalInstance',
		'$filter',
		'AddressService',
		'UserService',
		'DevicesService',
		'OrganizationService','$log',
		function ($rootScope, $scope, $translate, $modalInstance, $filter,
				 addressService,
				  userService, devicesService, organizationService, $log) {


			function arrayObjectIndexOf(myArray, searchTerm, property) {
				for(var i = 0, len = myArray.length; i < len; i++) {
					if (myArray[i][property] === searchTerm) return i;
				}
				var elem = {
					id: length,
					designation: searchTerm
				};
				myArray.push(elem);
				return (myArray.length-1);
			}

			$scope.organization.address.region = $rootScope.organization.address.region;

			$scope.typeData = [
				{
					id: 'PROVIDER',
					label: null
				},
				{
					id: 'CALIBRATOR',
					label: null
				},
				{
					id: 'STATE_VERIFICATOR',
					label: null
				}
			];

			//$rootScope.organization.lastName;

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
			
			 /**
             * Closes modal window on browser's back/forward button click.
             */
			$rootScope.$on('$locationChangeStart', function() {
			    $modalInstance.close();
			});
			
			$scope.setTypeDataLanguage();

			$scope.regions = null;
			$scope.districts = [];
			$scope.localities = [];
			$scope.streets = [];
			$scope.buildings = [];


			$scope.isUsernameAvailable = true;

			$scope.checkIfUsernameIsAvailable = function() {
				var username = $rootScope.organization.username;
				userService.isUsernameAvailable(username).then(
					function(data) {
						$scope.isUsernameAvailable = data;
					})
			}


			$scope.checkField = function (caseForValidation) {
				switch (caseForValidation) {
					case ('organizationName') :
						var organizationName = $rootScope.organization.name;
						if (organizationName == null) {
						} else if ($scope.ORGANIZATION_NAME_REGEX.test(organizationName)) {
							validator('organizationName', false);
						} else {
							validator('organizationName', true);
						}
						break;
					case ('firstName') :
						var firstName = $rootScope.organization.firstName;
						if (firstName == null) {
						} else if ($scope.FIRST_LAST_NAME_REGEX.test(firstName)) {
							validator('firstName', false);
						} else {
							validator('firstName', true);
						}
						break;
					case ('lastName') :
						var lastName = $rootScope.organization.lastName;
						if (lastName == null) {

						} else if ($scope.FIRST_LAST_NAME_REGEX.test(lastName)) {

							validator('lastName', false);
						} else {
							validator('lastName', true);
						}
						break;
					case ('middleName') :
						var middleName = $rootScope.organization.middleName;
						if (middleName == null) {
						} else if ($scope.MIDDLE_NAME_REGEX.test(middleName)) {
							validator('middleName', false);
						} else {
							validator('middleName', true);
						}
						break;
					case ('phone') :
						var phone = $rootScope.organization.phone;
						if (phone == null) {
						} else if ($scope.PHONE_REGEX.test(phone)) {
							validator('phone', false);
						} else {
							validator('phone', true);
						}
						break;
					case ('email') :
						var email = $rootScope.organization.email;
						if (email == null) {
						} else if ($scope.EMAIL_REGEX.test(email)) {
							validator('email', false);
						} else {
							validator('email', true);
						}
						break;
					case ('login') :
						var username = $rootScope.organization.username;
						if (username == null) {
						} else if ($scope.USERNAME_REGEX.test(username)) {
							isUsernameAvailable(username);
						} else {
							validator('loginValid', false);
						}
						break;
				}
			}


			function validator(caseForValidation, isValid) {

				switch (caseForValidation) {
					case 'organizationName':
						$scope.organizationNameValidation = {
							isValid: isValid,
							css: isValid ? 'has-error' : 'has-success'
						}
						break;
					case 'firstName':
						$scope.firstNameValidation = {
							isValid: isValid,
							css: isValid ? 'has-error' : 'has-success'
						}
						break;
					case 'lastName':
						$scope.lastNameValidation = {
							isValid: isValid,
							css: isValid ? 'has-error' : 'has-success'
						}
						break;
					case 'middleName':
						$scope.middleNameValidation = {
							isValid: isValid,
							css: isValid ? 'has-error' : 'has-success'
						}
						break;
					case 'phone':
						$scope.phoneNumberValidation = {
							isValid: isValid,
							css: isValid ? 'has-error' : 'has-success'
						}
						break;
					case 'email':
						$scope.emailValidation = {
							isValid: isValid,
							css: isValid ? 'has-error' : 'has-success'
						}
						break;
					case 'loginValid':
						$scope.usernameValidation = {
							isValid: isValid,
							css: isValid? 'has-success' : 'has-error',
							message: isValid ? undefined : 'К-сть символів не повинна бути меншою за 3\n і більшою за 16 '
						}
						break;
					case 'existLogin':
						$scope.usernameValidation = {
							isValid: isValid,
							css: isValid ? 'has-success' : 'has-error',
							message: isValid ? undefined : 'Такий логін вже існує'
						}
						break;
				}
			}
			/**
			 * Finds all regions
			 */
			function initFormData() {
				if (!$scope.regions) {
					addressService.findAllRegions().then(
						function(respRegions) {
							$log.debug($rootScope.organization);
							$scope.regions = respRegions;
						/*	var index = arrayObjectIndexOf($scope.regions,  $rootScope.organization.address.region.designation, "designation");
							$rootScope.organization.address.region = $scope.regions[index];
							*//*$scope.onRegionSelected($scope.regions[index].id);*/
						});
				}
			}
			/*var index = arrayObjectIndexOf($scope.regions,  $scope.user.address.region, "designation");
			$rootScope.organization.region = $scope.regions[index];
			$scope.onRegionSelected($scope.regions[index].id);*/
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
			 * Convert address data to string
			 */
			function addressFormToOrganizationForm() {
				if (typeof $rootScope.organization.address.region == 'object') {
					$rootScope.organization.address.region = $rootScope.organization.address.region.designation;
				}
				if (typeof $rootScope.organization.address.district == 'object') {
					$rootScope.organization.address.district = $rootScope.organization.address.district.designation;
				}
				if (typeof $rootScope.organization.address.locality == 'object') {
					$rootScope.organization.address.locality = $rootScope.organization.address.locality.designation;
				}
				if (typeof $rootScope.organization.address.street == 'object') {
					$rootScope.organization.address.street = $rootScope.organization.address.street.designation;
				}
				if (typeof $rootScope.organization.address.building == 'object') {
					$rootScope.organization.address.building = $rootScope.organization.address.building.designation;
				}
			}

			function objectTypesToStringTypes() {
				for (var i in $rootScope.organization.types) {
					$rootScope.organization.types[i] = $rootScope.organization.types[i].id;
				}
			}


			/**
			 * Edit organization. If everything is ok then
			 * resets the organization form and closes modal
			 * window.
			 */
			$scope.editOrganization = function() {
				addressFormToOrganizationForm();
				objectTypesToStringTypes();
				var organizationForm = {
					name : $rootScope.organization.name,
					email : $rootScope.organization.email,
					phone : $rootScope.organization.phone,
					types: $rootScope.organization.types,
					employeesCapacity : $rootScope.organization.employeesCapacity,
					maxProcessTime: $rootScope.organization.maxProcessTime,
					region : $rootScope.organization.address.region,
					locality : $rootScope.organization.address.locality,
					district : $rootScope.organization.address.district,
					street : $rootScope.organization.address.street,
					building : $rootScope.organization.address.building,
					flat : $rootScope.organization.address.flat
				};

				organizationService.editOrganization(
					organizationForm,
					$rootScope.organizationId).then(
					function(data) {
						if (data == 200) {
							$scope.closeModal();
							$scope.resetOrganizationForm();
							console.log(data);
							$rootScope.onTableHandling();
						}
						else (console.log(data));
					});
			};

			/**
			 * Closes edit modal window.
			 */
			$scope.closeModal = function() {
				$modalInstance.close();
			};

			$scope.ORGANIZATION_NAME_REGEX = /^(?=.{5,50}$).*/;
			$scope.PHONE_REGEX = /^[1-9]\d{8}$/;
			$scope.EMAIL_REGEX = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
			$scope.FIRST_LAST_NAME_REGEX = /^([A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20})$/;
			$scope.MIDDLE_NAME_REGEX = /^[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}$/;
			$scope.USERNAME_REGEX = /^[a-z0-9_-]{3,16}$/;
			$scope.PASSWORD_REGEX = /^(?=.{4,20}$).*/;
			$scope.BUILDING_REGEX = /^[1-9]{1}[0-9]{0,3}([A-Za-z]|[\u0410-\u042f\u0407\u0406\u0430-\u044f\u0456\u0457]){0,1}$/;
			$scope.FLAT_REGEX=/^([1-9]{1}[0-9]{0,3}|0)$/;

		}

	]);