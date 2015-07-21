/**
 * Created by MAX on 20.07.2015.
 */
angular
    .module('employeeModule')
    .controller('CalendarEmployeeProvider', ['$scope', 'UserService', '$modal', '$modalInstance', '$log', 'ngTableParams', '$timeout', '$filter',
        function ($scope, userService, $modal, $modalInstance, $log, ngTableParams, $timeout, $filter) {

            $scope.formattedDate = null;
            $scope.fcalendar = null;
            $scope.acalendar = null;
            $scope.dataToSearch = {};

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };

            $scope.showGrafic = function () {
                var dataToSearch = {
                     fromDate: $scope.changeDateToSend($scope.dataToSearch.fromDate),
                     toDate: $scope.changeDateToSend($scope.dataToSearch.toDate)
                };

                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/employee/grafic-providerEmployee.html',
                    controller: 'GraficEmployeeProvider',
                    size: 'lg',
                    resolve: {

                        grafic: function () {
                            return userService.getGraficData(dataToSearch)
                                .success(function (data) {
                                    return data;
                                });
                        }
                    }
                });

            }

            /**
             *  Date picker and formatter setup
             *
             */
            $scope.firstCalendar = {};
            $scope.firstCalendar.isOpen = false;

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.firstCalendar.isOpen = true;
            };


            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'
            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

            $scope.changeDateToSend = function (value) {

                if (angular.isUndefined(value)) {
                    return null;

                } else {

                   return $filter('date')(value, 'dd-MM-yyyy');
                }
            }

        }
    ]
)