angular
    .module('adminModule')
    .controller(
    'AgreementAddController',
    [
        '$rootScope',
        '$scope',
        '$translate',
        '$modalInstance',
        '$filter',
        'AgreementService',
        'OrganizationService',
        function ($rootScope, $scope, $translate, $modalInstance, $filter,
                  agreementService, organizationService) {

            $scope.addAgreementFormData = {};
            $scope.addAgreementFormData.number = '';
            $scope.addAgreementFormData.deviceType = undefined;
            $scope.addAgreementFormData.customerId = '';
            $scope.addAgreementFormData.executorId = '';
            $scope.addAgreementFormData.deviceCount = '';

            $scope.data = {};
            $scope.data.customerType = "";
            $scope.data.customers = [];
            $scope.data.selectedCustomer = {};
            $scope.data.executorType = "";
            $scope.data.executors = [];
            $scope.data.selectedExecutors = {};

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

            $scope.organizationTypeData = [
                {
                    type: 'PROVIDER',
                    label: $filter('translate')('PROVIDER')
                },
                {
                    type: 'CALIBRATOR',
                    label: $filter('translate')('CALIBRATOR')
                }
            ];

            $scope.receiveCustomer = function (customerType, deviceType) {
                $scope.data.selectedCustomer = {};
                $scope.data.selectedExecutors = {};
                organizationService.getOrganizationByOrganizationTypeAndDeviceType(customerType, deviceType).then(function (customers) {
                    $scope.data.customers = customers.data;
                });
                $scope.data.executorType = customerType == 'PROVIDER' ? 'CALIBRATOR' : 'STATE_VERIFICATOR';
                organizationService.getOrganizationByOrganizationTypeAndDeviceType($scope.data.executorType, deviceType).then(function (customers) {
                    $scope.data.executors = customers.data;
                })

            };

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            /**
             * Resets form
             */
            $scope.resetAddAgreementForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.addAgreementForm.$setPristine();
                $scope.addAgreementForm.$setUntouched();
                $scope.addAgreementFormData = {};
            };

            /**
             * Closes the modal window
             */
            $rootScope.closeModal = function () {
                $scope.resetAddAgreementForm();
                $modalInstance.close();
            };

            /**
             * Validates organization form before saving
             */
            $scope.onAddAgreementFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.addAgreementForm.$valid) {
                    $scope.addAgreementFormData.deviceType = $scope.addAgreementFormData.deviceType.type;
                    $scope.addAgreementFormData.customerId = $scope.data.selectedCustomer.id;
                    $scope.addAgreementFormData.executorId = $scope.data.selectedExecutors.id;
                    saveAgreement();
                }
            };

            /**
             * Saves new organization from the form in database.
             * If everything is ok then resets the organization
             * form and updates table with organizations.
             */
            function saveAgreement() {
                console.log($scope.addAgreementFormData);
                agreementService.saveAgreement($scope.addAgreementFormData)
                    .then(function (data) {
                        if (data == 201) {
                            $scope.closeModal();
                            $scope.resetAddAgreementForm();
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