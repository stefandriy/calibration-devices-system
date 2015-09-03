angular.module('employeeModule')
    .controller('MeasuringEquipmentAddModalControllerCalibrator',
    ['$rootScope', '$scope', '$modalInstance', 'MeasuringEquipmentServiceCalibrator', '$modal',
        function ($rootScope, $scope, $modalInstance, MeasuringEquipmentServiceCalibrator, $modal) {

	    	 /**
	         * Closes modal window on browser's back/forward button click.
	         */
	    	$rootScope.$on('$locationChangeStart', function() {
			    $modalInstance.close();
			});
    	
            /**
             * Resets Equipment form
             */
            $scope.resetEquipmentForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.equipmentFormData = null;
                $scope.nameValidation = null;
                $scope.deviceTypeValidation = null;
                $scope.manufacturerValidation = null;
                $scope.verificationIntervalValidation = null;
                $scope.incorrectValue = false;
            };


            /**
             * Validates equipment form before saving
             */
            $scope.onEquipmentFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if (!$scope.nameValidation.isValid && !$scope.deviceTypeValidation.isValid
                    && !$scope.manufacturerValidation.isValid && !$scope.verificationIntervalValidation.isValid) {
                    saveEquipment();
                    $modal.open({
                        animation: true,
                        templateUrl: '/resources/app/calibrator/views/modals/measuring-equipment-adding-success.html',
                        controller: function ($modalInstance) {
                            this.ok = function () {
                                $modalInstance.close();
                            }
                        },
                        controllerAs: 'successController',
                        size: 'md'
                    });
                } else {
                    $scope.incorrectValue = true;
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
                    function (data) {
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
            $rootScope.closeModal = function () {
                $modalInstance.close();
            };

            $scope.checkAll = function (caseForValidation) {
                switch (caseForValidation) {
                    case ('name'):
                        var name = $scope.equipmentFormData.name;
                        if (name == null) {

                        } else if (/^[a-zA-Z0-9]{5,20}$/.test(name)) {
                            validator('name', false);
                        } else {
                            validator('name', true);
                        }
                        break;
                    case ('deviceType'):
                        var deviceType = $scope.equipmentFormData.deviceType;
                        if (deviceType == null) {

                        } else if (/^[A-Z0-9]{4,10}$/.test(deviceType)) {
                            validator('deviceType', false);
                        } else {
                            validator('deviceType', true);
                        }
                        break;
                    case ('manufacturer'):
                        var manufacturer = $scope.equipmentFormData.manufacturer;
                        if (manufacturer == null) {
                        }
                        else if (/^[a-zA-Z0-9]{5,20}$/.test(manufacturer)) {
                            validator('manufacturer', false);
                        } else {
                            validator('manufacturer', true);
                        }
                        break;
                    case ('verificationInterval'):
                        var verificationInterval = $scope.equipmentFormData.verificationInterval;
                        if (verificationInterval == null) {
                        }
                        else if (/^\d{2,5}$/.test(verificationInterval)) {
                            validator('verificationInterval', false);
                        } else {
                            validator('verificationInterval', true);
                        }
                        break;
                }

            }

            function validator(caseForValidation, isValid) {
                switch (caseForValidation) {
                    case ('name'):
                        $scope.nameValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('deviceType'):
                        $scope.deviceTypeValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('manufacturer'):
                        $scope.manufacturerValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('verificationInterval'):
                        $scope.verificationIntervalValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                }
            }

        }]);