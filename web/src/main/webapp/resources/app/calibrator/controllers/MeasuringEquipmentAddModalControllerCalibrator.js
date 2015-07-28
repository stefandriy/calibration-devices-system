angular.module('employeeModule')
		.controller('MeasuringEquipmentAddModalControllerCalibrator',
				   ['$rootScope', '$scope', '$modalInstance', 'MeasuringEquipmentServiceCalibrator',
						function($rootScope, $scope, $modalInstance, MeasuringEquipmentServiceCalibrator) {

							/**
							 * Resets Equipment form
							 */
							$scope.resetEquipmentForm = function() {
								$scope.$broadcast('show-errors-reset');
								//$scope.equipmentFormData = null;
								$scope.equipmentFormData.name = null;
								$scope.equipmentFormData.deviceType = null;
								$scope.equipmentFormData.manufacturer = null;
								$scope.equipmentFormData.verificationInterval = null;
								$scope.EQUIPMENT_NAME_REGEX = null;
								$scope.EQUIPMENT_TYPE_REGEX = null;
								$scope.EQUIPMENT_MANUFACTURER_REGEX = null;
								$scope.EQUIPMENT_INTERVAL_REGEX = null;
							};


							/**
							 * Validates equipment form before saving
							 */
							$scope.onEquipmentFormSubmit = function() {
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.eqipmentForm.$valid) {
									saveEquipment();
								}
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

							$scope.EQUIPMENT_NAME_REGEX = /^[a-zA-Z0-9]{5,20}$/;
							$scope.EQUIPMENT_TYPE_REGEX = /^[A-Z]{4,16}$/;
							$scope.EQUIPMENT_MANUFACTURER_REGEX = /^[a-zA-Z0-9]{5,20}$/;
							$scope.EQUIPMENT_INTERVAL_REGEX = /^\d{2,5}$/;

						} ]);