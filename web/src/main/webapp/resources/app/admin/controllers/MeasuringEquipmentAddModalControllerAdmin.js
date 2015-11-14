angular.module('adminModule')
    .controller('MeasuringEquipmentAddModalControllerAdmin',
    ['$rootScope', '$scope', '$modalInstance', 'MeasuringEquipmentServiceAdmin', '$modal',
        function ($rootScope, $scope, $modalInstance, MeasuringEquipmentServiceAdmin, $modal) {

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            /**
             * Resets Equipment form
             */
            $scope.resetEquipmentForm = function () {
                $scope.$broadcast('show-errors-reset');
                /*$scope.equipmentFormData = null;
                 $scope.nameValidation = null;
                 $scope.deviceTypeValidation = null;
                 $scope.manufacturerValidation = null;
                 $scope.verificationIntervalValidation = null;
                 $scope.incorrectValue = false;*/

                $scope.equipmentFormData = null;

                $scope.sphereOfApplicationValidation = null;
                $scope.moduleIdValidation = null;
                $scope.symbolValidation = null;
                $scope.manufacturerNumberValidation = null;
                $scope.contactPersonValidation = null;
                $scope.phoneNumberValidation = null;
                $scope.validUntilValidation = null;
                $scope.installationTypeValidation = null;
                $scope.emailValidation = null;
                $scope.methodsOfVerificationValidation = null;

                $scope.incorrectValue = false;

                $scope.sphereOfApplication = undefined;
                $scope.moduleId = "";
                $scope.symbol = "";
                $scope.manufacturerNumber = "";
                $scope.contactPerson = "";
                $scope.phoneNumber = "";
                $scope.validUntil = "";
                $scope.installationType = undefined;
                $scope.email = "";
                $scope.methodsOfVerification = "";
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
                        templateUrl: 'resources/app/admin/views/modals/measuring-equipment-adding-success.html',
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
                MeasuringEquipmentServiceAdmin.saveEquipment(
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
             * New method added. It was did not to dublicate views.
             */
            $scope.editEquipment = function () {
                var equipmentForm = {
                    sphereOfApplication: $scope.equipment.sphereOfApplication,

                    installationNumber: $scope.equipment.installationNumber,

                    moduleId: $scope.equipment.moduleId,
                    symbol: $scope.equipment.symbol,
                    contactPerson: $scope.equipment.contactPerson,
                    phoneNumber: $scope.equipment.phoneNumber,
                    validUntil: $scope.equipment.validUntil,
                    installationType: $scope.equipment.installationType,
                    email: $scope.equipment.email,
                    methodsOfVerification: $scope.equipment.methodsOfVerification

                }
                if (!$scope.nameValidation.isValid && !$scope.deviceTypeValidation.isValid
                    && !$scope.manufacturerValidation.isValid && !$scope.verificationIntervalValidation.isValid) {
                    MeasuringEquipmentServiceAdmin.editEquipment(
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

            /**
             * Closes the modal window for adding new
             * equipment.
             */
            $rootScope.closeModal = function () {
                $modalInstance.close();
            };

            $scope.checkAll = function (caseForValidation) {
                /*switch (caseForValidation) {
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
                 }*/

                switch (caseForValidation) {
                    case ('sphereOfApplication'):
                        var sphereOfApplication = $scope.equipmentFormData.sphereOfApplication;
                        if (sphereOfApplication == null) {
                        } else if (sphereOfApplication == '') {
                            validator('sphereOfApplication', false);
                        }
                        else {
                            validator('sphereOfApplication', true);
                        }
                        break;
                    case ('moduleId'):
                        var moduleId = $scope.equipmentFormData.moduleId;
                        if (moduleId == null) {
                            validator('moduleId', false);
                        } else if (moduleId.length >= 8 && moduleId.length <= 10) {
                            validator('moduleId', true);
                        }
                        break;
                    case ('symbol'):
                        var symbol = $scope.equipmentFormData.symbol;
                        if (symbol == null) {
                            validator('symbol', false);
                        } else {
                            validator('symbol', true);
                        }
                        break;
                    case ('manufacturerNumber'):
                        var manufacturerNumber = $scope.equipmentFormData.manufacturerNumber;
                        if (/r/.test(manufacturerNumber) == null) {
                            validator('manufacturerNumber', false);
                        } else {
                            validator('manufacturerNumber', true);
                        }
                        break;
                    case ('contactPerson'):
                        break;
                    case ('phoneNumber'):
                        var phoneNumber = $scope.equipmentFormData.phoneNumber;
                        if ($scope.PHONE_REGEX.test(phoneNumber)) {
                            validator('phoneNumber', true);
                        } else {
                            validator('phoneNumber', false);
                        }
                        break;
                    case ('validUntil'):
                        break;
                    case ('installationType'):
                        break;
                    case ('email'):
                        break;
                    case ('methodsOfVerification'):
                        break;
                }
            }

            function validator(caseForValidation, isValid) {
                switch (caseForValidation) {
                    case ('sphereOfApplication'):
                        $scope.sphereOfApplicationValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('moduleId'):
                        $scope.moduleIdValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('symbol'):
                        $scope.symbolValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('manufacturerNumber'):
                        $scope.manufacturerNumberValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('contactPerson'):
                        $scope.contactPersonValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('phoneNumber'):
                        $scope.phoneNumberValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('validUntil'):
                        $scope.validUntilValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('installationType'):
                        $scope.installationTypeValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('email'):
                        $scope.emailValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('methodsOfVerification'):
                        $scope.methodsOfVerificationValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                }

                /*switch (caseForValidation) {
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
                 }*/
            }

            /*$scope.ORGANIZATION_NAME_REGEX = /^[A-Za-zА-ЯЄІЇҐ"'а-яєіїґ ]+$/;*/
            $scope.INSTALLATION_CODE_REGEX = /^[A-Za-zА-ЯЄІЇҐ"'а-яєіїґ ]+$/;
            $scope.INSTALLATION_CODE_REGEX_ERROR = "Regex error";
            $scope.PHONE_REGEX = /^[1-9]\d{8}$/;
            $scope.EMAIL_REGEX = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;

            /*$scope.FIRST_LAST_NAME_REGEX = /^([A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20})$/;
             $scope.MIDDLE_NAME_REGEX = /^[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}$/;
             $scope.USERNAME_REGEX = /^[a-z0-9_-]{3,16}$/;
             $scope.PASSWORD_REGEX = /^(?=.{4,20}$).*!/;
             $scope.BUILDING_REGEX = /^[1-9]{1}[0-9]{0,3}([A-Za-z]|[\u0410-\u042f\u0407\u0406\u0430-\u044f\u0456\u0457]){0,1}$/;
             $scope.FLAT_REGEX = /^([1-9]{1}[0-9]{0,3}|0)$/;*/
        }]);