angular
    .module('adminModule')
    .controller(
    'CategoryDeviceEditModalController',
    [
        '$rootScope',
        '$scope',
        '$translate',
        '$modalInstance',
        '$timeout',
        'DevicesService',
        function ($rootScope, $scope, $translate, $modalInstance, $timeout,
                  devicesService) {

            $scope.defaultData = {};
            $scope.defaultData.deviceType = {
                type: $rootScope.countersCategory.deviceType,
                label: null
            };

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

            /*
            */

            var setCurrentTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {
                    switch ($scope.defaultData.deviceType.type) {
                        case "WATER":
                            console.log($scope.defaultData.deviceType);
                            $scope.defaultData.deviceType.label = 'Холодна вода';
                            break;
                        case "THERMAL":
                            console.log($scope.defaultData.deviceType);
                            $scope.defaultData.deviceType.label = 'Гаряча вода';
                            break;
                        default:
                            console.log($scope.defaultData.deviceType.type + " not device type");
                    }
                } else if (lang === 'eng') {
                    switch ($scope.defaultData.deviceType.type) {
                        case "WATER":
                            $scope.defaultData.deviceType.label = 'Cold water';
                            break;
                        case "THERMAL":
                            $scope.defaultData.deviceType.label = 'Hot water';
                            break;
                        default:
                            console.log($scope.defaultData.deviceType.type + " not device type");
                    }
                }
            };

            $scope.setTypeDataLanguage();
            setTimeout(setCurrentTypeDataLanguage(), 2000);


            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            /**
             * Closes the modal window for adding new
             * organization.
             */
            $rootScope.closeModal = function () {
                $modalInstance.close();
            };

            /**
             * Validates organization form before saving
             */
            $scope.onEditCategoryFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.editCategoryForm.$valid) {
                    var deviceCategoryForm = {
                        number: $rootScope.countersCategory.number,
                        deviceType: $scope.defaultData.deviceType.type,
                        deviceName: $rootScope.countersCategory.deviceName
                    };
                    console.log(deviceCategoryForm);
                    saveDeviceCategory(deviceCategoryForm);
                }
            };

            /**
             * Saves new organization from the form in database.
             * If everything is ok then resets the organization
             * form and updates table with organizations.
             */
            function saveDeviceCategory(deviceCategoryForm) {
                console.log(deviceCategoryForm);
                console.log($rootScope.countersCategory.id);
                devicesService.editDeviceCategory(
                    deviceCategoryForm,
                    $rootScope.countersCategory.id).then(
                    function (data) {
                        if (data == 200) {
                            $scope.closeModal();
                            console.log(data);
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