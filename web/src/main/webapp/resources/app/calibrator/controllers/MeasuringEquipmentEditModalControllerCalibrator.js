angular.module('employeeModule')
    .controller('MeasuringEquipmentEditModalControllerCalibrator',
    ['$rootScope', '$scope', '$modalInstance', 'MeasuringEquipmentServiceCalibrator', '$log',
        function ($rootScope, $scope, $modalInstance, MeasuringEquipmentServiceCalibrator, $log) {

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
                $scope.equipment = null;
                $scope.nameValidation = null;
                $scope.deviceTypeValidation = null;
                $scope.manufacturerValidation = null;
                $scope.verificationIntervalValidation = null;
                $scope.incorrectValue = false;
            };

            /**
             * Edit equipment. If everything is ok then
             * resets the equipment form and closes modal
             * window.
             */
            $scope.editEquipment = function () {
                var equipmentForm = {
                    name: $scope.equipment.name,
                    deviceType: $scope.equipment.deviceType,
                    manufacturer: $scope.equipment.manufacturer,
                    verificationInterval: $scope.equipment.verificationInterval

                }
                if (!$scope.nameValidation.isValid && !$scope.deviceTypeValidation.isValid
                    && !$scope.manufacturerValidation.isValid && !$scope.verificationIntervalValidation.isValid) {
                    MeasuringEquipmentServiceCalibrator.editEquipment(
                        equipmentForm,
                        $rootScope.equipmentId).then(
                        function (data) {
                            if (data == 200) {
                                $scope.closeModal();
                                $scope.resetEquipmentForm();
                                $rootScope.onTableHandling();
                            }
                        });
                } else {
                    $scope.incorrectValue = true;
                }
            }
            $log.debug($rootScope.equipment);
            /**
             * Closes edit modal window.
             */
            $scope.closeModal = function () {
                $modalInstance.close();
            };

            $scope.checkAll = function (caseForValidation) {
                switch (caseForValidation) {
                    case ('name'):
                        var name = $scope.equipment.name;
                        if (name == null) {

                        } else if (/^[a-zA-Z0-9]{5,20}$/.test(name)) {
                            validator('name', false);
                        } else {
                            validator('name', true);
                        }
                        break;
                    case ('deviceType'):
                        var deviceType = $scope.equipment.deviceType;
                        if (deviceType == null) {

                        } else if (/^[A-Z]{4,16}$/.test(deviceType)) {
                            validator('deviceType', false);
                        } else {
                            validator('deviceType', true);
                        }
                        break;
                    case ('manufacturer'):
                        var manufacturer = $scope.equipment.manufacturer;
                        if (manufacturer == null) {
                        }
                        else if (/^[a-zA-Z0-9]{5,20}$/.test(manufacturer)) {
                            validator('manufacturer', false);
                        } else {
                            validator('manufacturer', true);
                        }
                        break;
                    case ('verificationInterval'):
                        var verificationInterval = $scope.equipment.verificationInterval;
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
