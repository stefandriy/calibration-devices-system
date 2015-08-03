angular.module('employeeModule')
		.controller('MeasuringEquipmentEditModalControllerCalibrator',
				   ['$rootScope', '$scope', '$modalInstance', 'MeasuringEquipmentServiceCalibrator',
						function($rootScope, $scope, $modalInstance, MeasuringEquipmentServiceCalibrator) {

						/**
						 * Resets Equipment form
						 */
						$scope.resetEquipmentForm = function() {
							$scope.$broadcast('show-errors-reset');
							$rootScope.equipment = null;
						};

						/**
						 * Calls resetEquipmentForm after the view loaded
						 */
						$scope.resetEquipmentForm();

							/**
							 * Edit equipment. If everything is ok then
							 * resets the equipment form and closes modal
							 * window.
							 */
							$scope.editEquipment = function() {
								var equipmentForm = {
									name : $scope.equipment.name,
									deviceType : $scope.equipment.deviceType,
									manufacturer : $scope.equipment.manufacturer,
									verificationInterval : $scope.equipment.verificationInterval
									
								}
								MeasuringEquipmentServiceCalibrator.editEquipment(
										equipmentForm,
										$rootScope.equipmentId).then(
										function(data) {
											if (data == 200) {
												$scope.closeModal();
												$scope.resetEquipmentForm();
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
