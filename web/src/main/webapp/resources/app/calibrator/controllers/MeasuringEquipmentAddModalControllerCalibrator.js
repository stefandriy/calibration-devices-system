angular.module('employeeModule')
		.controller('MeasuringEquipmentAddModalControllerCalibrator',
				   ['$rootScope', '$scope', '$modalInstance', 'MeasuringEquipmentServiceCalibrator',
						function($rootScope, $scope, $modalInstance, MeasuringEquipmentServiceCalibrator) {

							$scope.name = null;
							$scope.deviceType = null;
							$scope.manufacturer = null;
							$scope.verificationInterval = null;

							/**
							 * Resets Equipment form
							 */
							$scope.resetEquipmentForm = function() {
								$scope.$broadcast('show-errors-reset');
								$scope.nameValidation = null;
								$scope.equipmentFormData = null;
							};

							/**
							 * Calls resetEquipmentForm after the view loaded
							 */
							$scope.resetEquipmentForm();

							/**
							 * Validates name
							 */
							$scope.checkName = function() {
								var name = $scope.equipmentFormData.name;
								if (name == null) {
								} else if (/^[a-z0-9_-]{3,40}$/.test(name)) {
									isNameAvailable(name)
								} else {
									validateName(false,
											'К-сть символів не повинна бути меншою за 3\n і більшою за 40 ');
								}
							};

							/**
							 * Custom Name field validation. Shows error
							 * message in view if Name isn't validated.
							 * 
							 * @param isValid
							 * @param message
							 */
							function validateName(isValid, message) {
								$scope.nameValidation = {
									isValid : isValid,
									css : isValid ? 'has-success' : 'has-error',
									message : isValid ? undefined : message
								}
							}

							/**
							 * Checks whereas given Name is available to use
							 * for new equipment
							 * 
							 * @param name
							 */
							function isNameAvailable(name) {
								MeasuringEquipmentServiceCalibrator.isEquipmentNameAvailable(name).then(
										function(data) {
											validateName(data,
													'Такий пристрій вже існує');
										})
							}

							/**
							 * Validates equipment form before saving
							 */
							$scope.onEquipmentFormSubmit = function() {
								$scope.$broadcast('show-errors-check-validity');
//								if ($scope.equipmentForm.$valid
//										&& $scope.nameValidation.isValid) {
//								}
								saveEquipment();
							};

							/**
							 * Saves new equipment from the form in database.
							 * If everything is ok then resets the equipment
							 * form and updates table with equipments.
							 */
							function saveEquipment() {
								MeasuringEquipmentServiceCalibrator.saveEquipment(
										$scope.equipmentFormData).then(
										function(data) {
											if (data == 201) {
												$scope.closeModal();
												$scope.resetEquipmentForm();
												$rootScope.onTableHandling();
											}
										});
							}

							/**
							 * Closes the modal window for adding new
							 * equipment.
							 */
							$rootScope.closeModal = function() {
								$modalInstance.close();
							};

						} ]);