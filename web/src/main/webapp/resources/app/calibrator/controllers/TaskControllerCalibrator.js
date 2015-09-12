angular
    .module('employeeModule')
    .controller('TaskControllerCalibrator', ['$rootScope', '$scope', '$modal', '$modalInstance',
        '$log', '$filter', '$rootScope', '$timeout', '$translate',
        function ($rootScope, $scope, $modalInstance, $log, $filter, $rootScope, $timeout, $translate) {


            /**
             * Closes modal window on browser's back/forward button click.
             */
            /*$rootScope.$on('$locationChangeStart', function() {
                $modalInstance.destroy();
            });*/

            /**
             * Closes edit modal window.
             */
           /* $scope.closeModal = function () {
                $modalInstance.close();
            };*/

            /**
             *  Date picker and formatter setup
             *
             */
            $scope.openState = {};
            $scope.openState.isOpen = false;

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openState.isOpen = true;
            };


            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'
            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

        }]);
