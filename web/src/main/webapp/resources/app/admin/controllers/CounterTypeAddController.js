angular
    .module('adminModule')
    .controller(
    'CounterTypeAddController',
    [
        '$rootScope',
        '$scope',
        '$translate',
        'devices',
        '$modalInstance',
        'CounterTypeService',
        function ($rootScope, $scope, $translate, devices, $modalInstance,
                  counterTypeService) {

            $scope.addCounterTypeFormData = {};
            $scope.names = devices.data;
            $scope.standardSizes = ['DN 10','DN 15','DN 20','DN 25','DN 32',
                                    'DN 40','DN 50','DN 65','DN 80','DN 100',
                                    'DN 125','DN 150', 'DN 200', 'DN 250'];
            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            /**
             * Resets organization form
             */
            $scope.resetAddCounterTypeForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.addCounterTypeForm.$setPristine();
                $scope.addCounterTypeForm.$setUntouched();
                $scope.addCounterTypeFormData = {};
            };

            /**
             * Receives all possible districts.
             * On-select handler in region input form element.
             */
            $scope.setDeviceId = function (selectedDevice) {
                $scope.addCounterTypeForm.name = selectedDevice.designation;
                $scope.addCounterTypeForm.deviceId = selectedDevice.id;
            };

            /**
             * Closes the modal window for adding new
             * organization.
             */
            $rootScope.closeModal = function () {
                $scope.resetAddCounterTypeForm();
                $modalInstance.close();
            };

            /**
             * Validates organization form before saving
             */
            $scope.onAddCounterTypeFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.addCounterTypeForm.$valid) {
                    $scope.addCounterTypeFormData.name = $scope.addCounterTypeForm.name;
                    $scope.addCounterTypeFormData.deviceId = $scope.addCounterTypeForm.deviceId;
                    saveCounterType();
                }
            };

            /**
             * Saves new organization from the form in database.
             * If everything is ok then resets the organization
             * form and updates table with organizations.
             */
            function saveCounterType() {
                console.log($scope.addCounterTypeFormData);
                counterTypeService.saveCounterType($scope.addCounterTypeFormData)
                    .then(function (data) {
                        if (data == 201) {
                            $scope.closeModal();
                            $scope.resetAddCounterTypeForm();
                            $rootScope.onTableHandling();
                        }
                    });
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
        }
    ]);