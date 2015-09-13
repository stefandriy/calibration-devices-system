angular
    .module('employeeModule')
    .controller('TaskControllerCalibrator', ['$rootScope', '$scope', '$modal', '$modalInstance',
        '$log', '$filter', '$rootScope', '$timeout', '$translate',
        function ($rootScope, $scope, $modalInstance, $log, $filter, $rootScope, $timeout, $translate) {


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
                console.log("Do you see meeee");
                angular.element($('#taskModel')).close();
            };

            $scope.calibrationTask = {};
            $scope.calibrationTask.pickerDate = null;
            $scope.defaultDate = null;

            $scope.initDatePicker = function (date) {
                /**
                 *  Date picker and formatter setup
                 *
                 */


                $scope.calibrationTask.pickerDate = {
                    startDate: (date ? moment(date, "YYYY-MM-DD") : moment()),
                    endDate: moment() // current day
                };

                if ($scope.defaultDate == null) {
                   $scope.defaultDate = angular.copy($scope.calibrationTask.pickerDate);
                }
                moment.locale('uk');
                $scope.myOptions = {
                    format: 'DD-MM-YYYY',
                    showDropdowns: true,
                    locale: {
                        firstDay: 1,
                        fromLabel: 'Від',
                        toLabel: 'До',
                        applyLabel: "Прийняти",
                        cancelLabel: "Закрити"
                    },
                    eventHandlers: {}
                };
            };

            $scope.showPicker = function ($event) {
                angular.element("#datepicker").trigger("click");
            };


        }]);
