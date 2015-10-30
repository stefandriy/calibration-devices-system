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
            $rootScope.closeModal = function (close) {
                if(close === true) {
                    $modalInstance.close();
                }
                $modalInstance.dismiss();
            };

            /**
             * Validates organization form before saving
             */
            $scope.onEditCategoryFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.editCategoryForm.$valid) {
                    var deviceCategoryForm = {
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

            $scope.COUNTER_NAME = /^([A-ZА-ЯЇІЄ']{1}[a-zа-яіїє']{1,20}|[A-ZА-ЯЇІЄ']{1}[a-zа-яіїє']{1,20}([ ]{1}[a-zа-яіїє']{1,20})+)$/;
        }
    ]);