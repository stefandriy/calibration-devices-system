/**
 * Created by Sonka on 23.11.2015.
 */
angular
    .module('adminModule')
    .controller(
    'UnsuitabilityReasonAddModalController',
    [
        '$rootScope',
        '$scope',
        '$translate',
        '$modalInstance',
        'UnsuitabilityReasonService',
        function ($rootScope, $scope, $translate, $modalInstance,
                  unsuitabilityReasonService) {

            $scope.addCategoryFormData = {};
            $scope.addCategoryFormData.deviceType = undefined;
            $scope.addCategoryFormData.deviceName = '';

            $scope.deviceTypeData = [
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
             * Localization of select for counters kind
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
             * Resets category form
             */
            $scope.resetAddCategoryForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.addCategoryForm.$setPristine();
                $scope.addCategoryForm.$setUntouched();
                $scope.addCategoryFormData = {};
            };

            /**
             * Closes the modal window for adding new
             * category.
             */
            $rootScope.closeModal = function (close) {
                $scope.resetAddCategoryForm();
                if(close === true) {
                    $modalInstance.close();
                }
                $modalInstance.dismiss();
            };

            /**
             * Validates category form before saving
             */
            $scope.onAddReasonFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.addCategoryForm.$valid) {
                    $scope.addCategoryFormData.deviceType = $scope.addCategoryFormData.deviceType.type;
                    saveDeviceCategory();
                }
            };

            /**
             * Saves new category from the form in database.
             * If everything is ok then resets the category
             * form and updates table with reasons categories.
             */
            function saveDeviceCategory() {
                console.log($scope.addCategoryFormData);
                unsuitabilityReasonService.saveDeviceCategory($scope.addCategoryFormData)
                    .then(function (data) {
                        if (data == 201) {
                            $scope.closeModal(true);
                            $scope.resetAddCategoryForm();
                            $rootScope.onTableHandling();
                        }
                    });
            }

            $scope.COUNTER_NAME = /^([A-ZА-ЯЇІЄ']{1}[a-zа-яіїє']{1,20}|[A-ZА-ЯЇІЄ']{1}[a-zа-яіїє']{1,20}([ ]{1}[a-zа-яіїє']{1,20})+)$/;
        }
    ]);