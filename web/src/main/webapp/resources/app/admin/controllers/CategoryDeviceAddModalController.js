angular
    .module('adminModule')
    .controller(
    'CategoryDeviceAddModalController',
    [
        '$rootScope',
        '$scope',
        '$translate',
        '$modalInstance',
        'DevicesService',
        function ($rootScope, $scope, $translate, $modalInstance,
                  devicesService) {

            $scope.addCategoryFormData = {};
            $scope.addCategoryFormData.number = '';
            $scope.addCategoryFormData.deviceType = undefined;
            $scope.addCategoryFormData.deviceName = '';

            $scope.deviceTypeData = [
                /*{
                    type: 'ELECTRICAL',
                    label: null
                 },
                {
                    type: 'GASEOUS',
                    label: null
                },*/
                {
                    type: 'WATER',
                    label: null
                },
                {
                    type: 'THERMAL',
                    label: null
                }
            ];

            /**
             * Localization of multiselect for type of organization
             */
            $scope.setTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {
                   // $scope.deviceTypeData[0].label = 'Електричний';
                    //$scope.deviceTypeData[1].label = 'Газовий';
                    $scope.deviceTypeData[0].label = 'Холодна вода';
                    $scope.deviceTypeData[1].label = 'Гаряча вода';
                } else if (lang === 'eng') {
                    //$scope.deviceTypeData[0].label = 'Electrical';
                   // $scope.deviceTypeData[1].label = 'Gaseous';
                    $scope.deviceTypeData[0].label = 'Cold water';
                    $scope.deviceTypeData[1].label = 'Hot water';
                }
            };
            $scope.setTypeDataLanguage();

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            /**
             * Resets organization form
             */
            $scope.resetAddCategoryForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.addCategoryForm.$setPristine();
                $scope.addCategoryForm.$setUntouched();
                $scope.addCategoryFormData = {};
            };

            /**
             * Closes the modal window for adding new
             * organization.
             */
            $rootScope.closeModal = function () {
                $scope.resetAddCategoryForm();
                $modalInstance.close();
            };

            /**
             * Validates organization form before saving
             */
            $scope.onAddCategoryFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.addCategoryForm.$valid) {
                    $scope.addCategoryFormData.deviceType = $scope.addCategoryFormData.deviceType.type;
                    saveDeviceCategory();
                }
            };

            /**
             * Saves new organization from the form in database.
             * If everything is ok then resets the organization
             * form and updates table with organizations.
             */
            function saveDeviceCategory() {
                console.log($scope.addCategoryFormData);
                devicesService.saveDeviceCategory($scope.addCategoryFormData)
                    .then(function (data) {
                        if (data == 201) {
                            $scope.closeModal();
                            $scope.resetAddCategoryForm();
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