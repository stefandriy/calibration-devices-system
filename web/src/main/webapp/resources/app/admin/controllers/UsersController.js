angular
    .module('adminModule')
    .controller('UsersController', ['$scope', 'UsersService', '$modal', '$log', 'ngTableParams', '$timeout', '$filter','$rootScope',
        'toaster',
        function ($scope, userService, $modal, $log, ngTableParams, $timeout, $filter, $rootScope, toaster) {


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
                    userService.getPage(params.page(), params.count(), params.filter(),params.sorting())
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

            $scope.openAddUserModal = function() {
                var addEmployeeModal = $modal
                    .open({
                        animation : true,
                        controller : 'UserAddModalController',
                        templateUrl : '/resources/app/admin/views/modals/user-add-modal.html',
                    });

                /**
                 * executes when modal closing
                 */
                addEmployeeModal.result.then(function () {
                    $scope.popNotification($filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_CREATED_ADMIN'));
                });
            };






            $scope.cantAddNewEmployee = function() {
                userService.getOrganizationEmployeeCapacity().success(
                    function(data) {
                        $scope.organizationEmployeesCapacity = data;
                        if ($scope.totalEmployee < $scope.organizationEmployeesCapacity) {
                            $scope.cantAddEmployee = false;
                        } else {
                            $scope.cantAddEmployee = true;
                        }
                    });
            };

            $scope.popNotification = function (title, text) {
                toaster.pop('success', title, text);
            };

        }]);