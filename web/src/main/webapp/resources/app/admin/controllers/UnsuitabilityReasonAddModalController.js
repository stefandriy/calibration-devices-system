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
        'devices',
        function ($rootScope, $scope, $translate, $modalInstance,
                  devices) {

            $scope.addReasonFormData = [];
            $scope.addReasonFormData.deviceName = undefined;
            $scope.addReasonFormData.name = '';
            $scope.deviceNameData = devices.data;

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
                $scope.addReasonForm.$setPristine();
                $scope.addReasonForm.$setUntouched();
                $scope.addReasonFormData = {};
                $scope.addReasonFormData.deviceName = {};
                $scope.addReasonFormData.name = '';
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
              $scope.addReasonFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.addReasonForm.$valid) {
                         $modalInstance.close($scope.addReasonFormData);
                }
            };



        }
    ]);