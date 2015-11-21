angular
    .module('adminModule')
    .controller(
    'MeasuringEquipmentDisableModalControllerAdmin',
    [
        '$rootScope',
        '$scope',
        '$modalInstance',
        'MeasuringEquipmentServiceAdmin',
        function ($rootScope, $scope, $modalInstance, MeasuringEquipmentServiceAdmin) {

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function() {
                $modalInstance.close();
            });

            /**
             * Closes edit modal window.
             */
            $scope.closeModal = function () {
                $modalInstance.close();
            };

        }]);

