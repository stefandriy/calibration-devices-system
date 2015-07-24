/**
 * Created by MAX on 25.06.2015.
 */
angular
    .module('employeeModule')
    .controller('UsersController', ['$scope', 'UserService', '$modal', '$log', 'ngTableParams', '$timeout', '$filter',
        function ($scope, userService, $modal, $log, ngTableParams, $timeout, $filter) {
            $scope.totalEmployee=0;
            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 5
            }, {
                total: 0,
                getData: function ($defer, params) {
                    userService.getPage(params.page(), params.count(), params.filter())
                        .success(function (result) {
                            $scope.totalEmployee=result.totalItems;
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                }
            });

            $scope.isFilter = function () {
                var obj = $scope.tableParams.filter();
                for (var i in obj) {
                    if (obj.hasOwnProperty(i) && obj[i]) {
                        return true;
                    }
                }
                return false;
            };

            $scope.showCapacity = function (username) {

                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/employee/capacity-providerEmployee.html',
                    controller: 'CapacityEmployeeControllerProvider',
                    size: 'lg',
                    resolve: {

                        capacity: function () {
                            return userService.getCapacityOfWork(username)
                                .success(function (verifications) {
                                    return verifications;
                                });
                        }
                    }
                });
            };



            $scope.onTableHandling = function () {

                userService.isAdmin()
    			.success(function (response) {
    				var roles = response + '';
    				var role = roles.split(',');
    				var thereIsAdmin = 0;
    				for (var i = 0; i<role.length; i++){
    					if(role[i]==='PROVIDER_ADMIN') {
    						thereIsAdmin++;
    					}
    				}
    				if (thereIsAdmin > 0){
    					$scope.verificator = true;
    				}else{
    					$scope.accessLable = true;	
    				}
    			});
            };
            $scope.onTableHandling();


        }]);

