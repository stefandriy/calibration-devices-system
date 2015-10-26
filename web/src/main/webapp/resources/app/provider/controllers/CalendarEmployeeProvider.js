/**
 * Created by MAX on 20.07.2015.
 */
angular
    .module('employeeModule')
    .controller('CalendarEmployeeProvider', ['$scope', '$controller', 'UserService', '$modal', '$log', 'ngTableParams', '$timeout', '$filter',
        function ($scope, $controller, userService, $modal, $log, ngTableParams, $timeout, $filter) {

            var me = $scope;
            $controller('GraficEmployeeProvider', {
                $scope: $scope
            });

            $scope.formattedDate = null;
            $scope.fcalendar = null;
            $scope.acalendar = null;
            var date1 = new Date(new Date().getFullYear(), 0, 1);
            var date2 = new Date();
            $scope.dataToSearch = {
                fromDate: date1,
                toDate: date2
            }


            $scope.cancel = function () {
                $modal.dismiss();
            };

            $scope.showGrafic = function () {
            	if ($scope.dataToSearch.toDate < $scope.dataToSearch.fromDate) {
            		$scope.dataToSearch.toDate.setTime($scope.dataToSearch.fromDate.getTime() + 24*60*60*1000);
            	}
            	
            	if ($scope.dataToSearch.toDate > new Date()) {
            		$scope.dataToSearch.toDate.setTime(new Date().getTime());
            	}
            	
                var dataToSearch = {
                		
                    fromDate: $scope.changeDateToSend($scope.dataToSearch.fromDate),
                    toDate: $scope.changeDateToSend($scope.dataToSearch.toDate)
                };
                userService.getGraficData(dataToSearch)
                    .success(function (data) {
                        return me.displayGrafic(data);
                    });
            };

            /**
             *  Date picker and formatter setup
             *
             */
            $scope.firstCalendar = {};
            $scope.firstCalendar.isOpen = false;
            $scope.secondCalendar = {};
            $scope.secondCalendar.isOpen = false;

            $scope.open1 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.firstCalendar.isOpen = true;
            };
            $scope.open2 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.secondCalendar.isOpen = true;
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
            $scope.showGrafic();


        }
    ]
)