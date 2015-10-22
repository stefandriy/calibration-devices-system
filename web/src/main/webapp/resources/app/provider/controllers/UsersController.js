angular
    .module('employeeModule')
    .controller('UsersController', ['$scope', 'UserService', 'EmployeeService', '$modal', '$log', 'ngTableParams', '$timeout', '$filter', '$rootScope',
        function ($scope, userService, employeeService, $modal, $log, ngTableParams, $timeout, $filter, $rootScope) {
            $scope.totalEmployee = 0;
            $scope.cantAddEmployee;

            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 5,
                sorting: {
                    lastName: 'asc'     // initial sorting
                }
            }, {
                total: 0,
                getData: function ($defer, params) {
                    var pageNumber = params.page();
                    var itemsPerPage = params.count();
                    var sortCriteria = params.sorting();
                    var filterCriteria = params.filter();

                    if (!Object.keys(filterCriteria).length && sortCriteria.lastName == 'asc') {
                        employeeService
                            .getAll(pageNumber - 1, itemsPerPage + 1)
                            .success(function (result) {

                                $scope.totalEmployee = result.totalItems;
                                //$scope.totalEmployee = result.length;
                                params.total(result.totalItems);

                                $defer.resolve(result);

                            });
                        return;
                    }
                    //userService.getPage(pageNumber, itemsPerPage, filterCriteria, sortCriteria)
                    //    .success(function (result) {
                    //        $scope.totalEmployee = result.totalItems;
                    //        $defer.resolve(result.content);
                    //        params.total(result.totalItems);
                    //        $scope.cantAddNewEmployee();
                    //    }, function (result) {
                    //        $log.debug('error fetching data:', result);
                    //    });
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
                        for (var i = 0; i < role.length; i++) {
                            if (role[i] === 'PROVIDER_ADMIN') {
                                thereIsAdmin++;
                            }
                            if (role[i] === 'CALIBRATOR_ADMIN') {
                                thereIsAdmin++;
                            }
                            if (role[i] === 'STATE_VERIFICATOR_ADMIN') {
                                thereIsAdmin++;
                            }
                        }
                        if (thereIsAdmin > 0) {
                            $scope.verificator = true;
                        } else {
                            $scope.accessLable = true;
                        }
                    });
            };


            $scope.openAddUserModal = function () {
                var addEmployeeModal = $modal
                    .open({
                        animation: true,
                        controller: 'AddEmployeeController',
                        size: 'lg',
                        templateUrl: '/resources/app/provider/views/employee/employee-add-modal.html',
                    });
            };

            $scope.onTableHandling();

            $scope.openEditEmployeeModal = function (username) {
                userService.getUser(username)
                    .success(function (data) {
                        $rootScope.checkboxModel = false;
                        $rootScope.user = data;
                        $rootScope.$broadcast("info_about_editUser", {
                            roles: $rootScope.user.userRoles,
                            isAvaliable: $rootScope.user.isAvaliable
                        });
                        if (data.secondPhone != null) {
                            $rootScope.checkboxModel = true;
                        }
                    });
                var editEmployeeModal = $modal
                    .open({
                        animation: true,
                        controller: 'EditEmployeeController',
                        size: 'lg',
                        templateUrl: '/resources/app/provider/views/employee/employee-edit-modal.html'

                    });
            };


            $scope.cantAddNewEmployee = function () {
                userService.getOrganizationEmployeeCapacity().success(
                    function (data) {
                        $scope.organizationEmployeesCapacity = data;
                        if ($scope.totalEmployee < $scope.organizationEmployeesCapacity) {
                            $scope.cantAddEmployee = false;
                        } else {
                            $scope.cantAddEmployee = true;
                        }
                    });
            };


            $scope.$on('new-employee-added', function () {
                $scope.tableParams.reload();
            });


        }]);

