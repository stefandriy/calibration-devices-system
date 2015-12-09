angular
    .module('adminModule')
    .controller(
    'UnsuitabilityReasonDeleteModalController',
    [
        '$rootScope',
        '$scope',
        '$modalInstance',
        'toaster',
        '$filter',
        'id',
        'UnsuitabilityReasonService',
        function ($rootScope, $scope, $modalInstance, toaster, $filter,
                  id, unsuitabilityReasonService) {
            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            /**
             * Closes the modal window
             */
            $rootScope.closeModal = function (close) {
                if (close === true) {
                    $modalInstance.close();
                }
                $modalInstance.dismiss();
            };
            /**
             * Remove unsuitability reason by id
             * @param id
             */
            $scope.deleteUnsuitabilityReason = function () {
                unsuitabilityReasonService.deleteUnsuitabilityReason(id).then(function () {
                    $scope.closeModal(true);
                    $rootScope.onTableHandling();
                })
            }

        }]);

