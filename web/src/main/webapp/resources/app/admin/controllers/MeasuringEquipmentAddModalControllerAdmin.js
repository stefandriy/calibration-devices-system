angular
    .module('adminModule')
    .controller(
    'MeasuringEquipmentAddModalControllerAdmin',
    [
        '$rootScope',
        '$scope',
        '$translate',
        '$modalInstance',
        '$filter',
        'MeasuringEquipmentServiceAdmin',
        'calibrationModule',
        function ($rootScope, $scope, $translate, $modalInstance, $filter,
                  measuringEquipmentServiceAdmin, calibrationModule) {

            /**
             * Initialize all variables
             */
            $scope.init = function () {

                $scope.addCalibrationModuleFormData = {};
                //$scope.data = {};

                $scope.addCalibrationModuleFormData.organizationCode = '';
                $scope.addCalibrationModuleFormData.condDesignation = '';
                $scope.addCalibrationModuleFormData.serialNumber = '';
                $scope.addCalibrationModuleFormData.employeeFullName = '';
                $scope.addCalibrationModuleFormData.telephone = '';
                $scope.addCalibrationModuleFormData.workDate = '';
                $scope.addCalibrationModuleFormData.moduleType = '';
                $scope.addCalibrationModuleFormData.email = '';
                $scope.addCalibrationModuleFormData.calibrationType = '';

                $scope.deviceTypeData = [
                    {
                        type: 'WATER',
                        label: $filter('translate')('WATER')
                    },
                    {
                        type: 'THERMAL',
                        label: $filter('translate')('THERMAL')
                    }
                ];

                if (calibrationModule !== undefined) {
                    $scope.addCalibrationModuleFormData.deviceType = {
                        type: calibrationModule.deviceType,
                        label: $filter('translate')(calibrationModule.deviceType)
                    };

                    $scope.addCalibrationModuleFormData.organizationCode = calibrationModule.organizationCode;
                    $scope.addCalibrationModuleFormData.condDesignation = calibrationModule.condDesignation;
                    $scope.addCalibrationModuleFormData.serialNumber = calibrationModule.serialNumber;
                    $scope.addCalibrationModuleFormData.employeeFullName = calibrationModule.employeeFullName;
                    $scope.addCalibrationModuleFormData.telephone = calibrationModule.telephone;
                    $scope.addCalibrationModuleFormData.workDate = calibrationModule.workDate;
                    $scope.addCalibrationModuleFormData.moduleType = calibrationModule.moduleType;
                    $scope.addCalibrationModuleFormData.email = calibrationModule.email;
                    $scope.addCalibrationModuleFormData.calibrationType = calibrationModule.calibrationType;
                } else {
                    $scope.addCalibrationModuleFormData.deviceType = undefined;
                    $scope.addCalibrationModuleFormData.organizationCode = '';
                    $scope.addCalibrationModuleFormData.condDesignation = '';
                    $scope.addCalibrationModuleFormData.serialNumber = '';
                    $scope.addCalibrationModuleFormData.employeeFullName = '';
                    $scope.addCalibrationModuleFormData.telephone = '';
                    $scope.addCalibrationModuleFormData.workDate = '';
                    $scope.addCalibrationModuleFormData.moduleType = '';
                    $scope.addCalibrationModuleFormData.email = '';
                    $scope.addCalibrationModuleFormData.calibrationType = '';
                }
            };

            $scope.init();

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            /**
             * Resets form
             */
            $scope.resetAddCalibrationModuleForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.addCalibrationModuleForm.$setPristine();
                $scope.addCalibrationModuleForm.$setUntouched();
                $scope.addCalibrationModuleFormData = {};
                //$scope.data = {};
            };

            /**
             * Closes the modal window
             */
            $rootScope.closeModal = function (close) {
                $scope.resetAddCalibrationModuleForm();
                if (close === true) {
                    $modalInstance.close();
                }
                $modalInstance.dismiss();
            };

            /**
             * Validates organization form before saving
             */
            $scope.onAddCalibrationModuleFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.addCalibrationModuleForm.$valid) {
                    $scope.addCalibrationModuleFormData.deviceType = $scope.addCalibrationModuleFormData.deviceType.type;
                    /*$scope.addCalibrationModuleFormData.organizationCode = $scope.addCalibrationModuleFormData.organizationCode;
                    $scope.addCalibrationModuleFormData.condDesignation = $scope.addCalibrationModuleFormData.condDesignation;
                    $scope.addCalibrationModuleFormData.serialNumber = $scope.addCalibrationModuleFormData.serialNumber;
                    $scope.addCalibrationModuleFormData.employeeFullName = $scope.addCalibrationModuleFormData.employeeFullName;
                    $scope.addCalibrationModuleFormData.telephone = $scope.addCalibrationModuleFormData.telephone;
                    $scope.addCalibrationModuleFormData.workDate = $scope.addCalibrationModuleFormData.workDate;
                    $scope.addCalibrationModuleFormData.moduleType = $scope.addCalibrationModuleFormData.moduleType;
                    $scope.addCalibrationModuleFormData.email = $scope.addCalibrationModuleFormData.email;
                    $scope.addCalibrationModuleFormData.calibrationType = $scope.addCalibrationModuleFormData.calibrationType;*/
                    saveCalibrationModule();
                }
            };

            /**
             * Saves calibrationModule
             */
            function saveCalibrationModule() {
                console.log($scope.addCalibrationModuleFormData);
                if (calibrationModule === undefined) {
                    measuringEquipmentServiceAdmin.saveCalibrationModule($scope.addCalibrationModuleFormData)
                        .then(function (result) {
                            if (result == 201) {
                                $scope.closeModal(true);
                                $scope.resetAddCalibrationModuleForm();
                                $rootScope.onTableHandling();
                            }
                        });
                } else {
                    measuringEquipmentServiceAdmin.editCalibrationModule($scope.addCalibrationModuleFormData, calibrationModule.id)
                        .then(function (result) {
                            if (result == 200) {
                                $scope.closeModal(true);
                                $scope.resetAddCalibrationModuleForm();
                                $rootScope.onTableHandling();
                            }
                        });
                }
            }


            $scope.CATEGORY_DEVICE_CODE = /^[\u0430-\u044f\u0456\u0457\u0454a-z\d]{13}$/;
            $scope.PHONE_REGEX = /^[1-9]\d{8}$/;
            $scope.EMAIL_REGEX = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
            $scope.FIRST_LAST_NAME_REGEX = /^([A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20})$/;
            $scope.MIDDLE_NAME_REGEX = /^[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}$/;
            $scope.USERNAME_REGEX = /^[a-z0-9_-]{3,16}$/;
            $scope.PASSWORD_REGEX = /^(?=.{4,20}$).*/;
            $scope.BUILDING_REGEX = /^[1-9]{1}[0-9]{0,3}([A-Za-z]|[\u0410-\u042f\u0407\u0406\u0430-\u044f\u0456\u0457]){0,1}$/;
            $scope.FLAT_REGEX = /^([1-9]{1}[0-9]{0,3}|0)$/;

            if (calibrationModule !== undefined) {


            }
        }
    ]);