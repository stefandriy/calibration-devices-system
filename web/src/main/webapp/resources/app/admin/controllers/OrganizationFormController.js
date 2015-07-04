angular
		.module('adminModule')
		.controller(
				'OrganizationFormController',
				[
						'$rootScope',
						'$scope',
						'$modal',
						'OrganizationService',
						'UserService',
						function($rootScope, $scope, $modal,
								organizationService, userService) {
							
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

							/**
							 * Resets organization form
							 */
							$scope.resetOrganizationForm = function() {
								$scope.$broadcast('show-errors-reset');
								$scope.addressMessage = '';
								$scope.usernameValidation = null;
								$scope.organizationFormData = null;
								$rootScope.address = {
									region : null,
									district : null,
									locality : null,
									street : null,
									building : null,
									flat : null
								};
								$rootScope.organization = null;
							};

							/**
							 * Calls resetOrganizationForm after the view loaded
							 */
							$scope.resetOrganizationForm();

							/**
							 * Updates address message in organization form
							 * according to data in modal window with address
							 * form.
							 */
							$rootScope.updateAddressMessage = function() {
								$scope.addressMessage = addressToString($rootScope.address);
							};

							/**
							 * Validates username
							 */
							$scope.checkUsername = function() {
								var username = $scope.organizationFormData.username;
								if (username == null) {
								} else if (/^[a-z0-9_-]{3,16}$/.test(username)) {
									isUsernameAvailable(username)
								} else {
									validateUsername(false,
											'К-сть символів не повинна бути меншою за 3\n і більшою за 16 ');
								}
							};

							/**
							 * Validates organization form before saving
							 */
							$scope.onOrganizationFormSubmit = function() {
								$scope.$broadcast('show-errors-check-validity');
								// TODO: add password match checking
								if ($scope.organizationForm.$valid
										&& $scope.usernameValidation.isValid) {
									addressFormToOrganizationForm();
									saveOrganization();
								}
							};
							

							/**
							 * Opens modal window with address form.
							 */
							$scope.openAddressModal = function() {
								var addressModal = $modal
										.open({
											animation : true,
											controller : 'OrganizationModalAddressController',
											templateUrl : '/resources/app/admin/views/organization-modal-address.html',
										});

								addressModal.result.then(function() {
									$rootScope.updateAddressMessage();
								});
							};

							/**
							 * Saves new organization from the form in database.
							 * If everything is ok then resets the organization
							 * form and updates table with organizations.
							 */
							function saveOrganization() {
								organizationService.saveOrganization(
										$scope.organizationFormData).then(
										function(data) {
											if (data == 201) {
												$scope.resetOrganizationForm();
												$scope.onTableHandling();
											}
										});
							}
							
							
							/**
							 * Edit organization.
							 * If everything is ok then resets the organization
							 * form and updates table with organizations.
							 */
							function editOrganization() {
								organizationService.editOrganization(
										$scope.organization, $rootScope.organizationId).then(
										function(data) {
											if (data == 201) {
												$scope.resetOrganizationForm();
												$scope.onTableHandling();
											}
										});
							}

							/**
							 * Custom username field validation. Shows error
							 * message in view if username isn't validated.
							 * 
							 * @param isValid
							 * @param message
							 */
							function validateUsername(isValid, message) {
								$scope.usernameValidation = {
									isValid : isValid,
									css : isValid ? 'has-success' : 'has-error',
									message : isValid ? undefined : message
								}
							}

							/**
							 * Checks whereas given username is available to use
							 * for new user
							 * 
							 * @param username
							 */
							function isUsernameAvailable(username) {
								userService.isUsernameAvailable(username).then(
										function(data) {
											validateUsername(data,
													'Такий логін вже існує');
										})
							}

							/**
							 * Transfers address data from modal window to
							 * current scope
							 */
							function addressFormToOrganizationForm() {
								$scope.organizationFormData.region = $rootScope.address.region.designation;
								$scope.organizationFormData.district = $rootScope.address.district.designation;
								$scope.organizationFormData.locality = $rootScope.address.locality.designation;
								$scope.organizationFormData.street = $rootScope.address.street.designation;
								$scope.organizationFormData.building = $rootScope.address.building.designation;
								$scope.organizationFormData.flat = $rootScope.address.flat;
							}

							/**
							 * Translates address form data to string format
							 * 
							 * @param address
							 *            form data
							 * @returns {string} address in string format
							 */
							function addressToString(address) {
								var message = '';
								if (address.region != null) {
									message += address.region.designation
											+ ' область';
								}
								if (address.district != null) {
									message += ', '
											+ address.district.designation
											+ ' район';
								}
								if (address.locality != null) {
									message += ', '
											+ address.locality.designation;
								}
								if (address.street != null) {
									message += ', '
											+ address.street.designation;
								}
								if (address.building != null) {
									message += ', буд. '
											+ address.building.designation;
								}
								if (address.flat != null) {
									message += ', кв. ' + address.flat;
								}
								return message
							}
						
							
							
						} ]);