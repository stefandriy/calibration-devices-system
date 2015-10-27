angular
    .module('adminModule')
    .controller(
    'SysAdminDeleteModalController',
    [
        '$rootScope',
        '$scope',
        '$translate',
        '$modalInstance',
        '$modal',
        '$timeout',
        'UsersService',
        function ($rootScope, $scope, $translate, $modalInstance, $modal, $timeout, userService) {


            console.log($rootScope.username);
            $scope.submitDelete = function () {
                userService.deleteSysAdmin($rootScope.username)
                    .then(function(data) {
                        console.log(data);
                        if(data == 200){
                            $timeout(function() {
                                  console.log('delete with timeout');
                                  $modalInstance.close();
                                  $rootScope.onTableHandling();
                      }, 700);
                }else {
                    console.log(data.status);
                }
            });
            };

            $rootScope.cancel = function () {
                $modalInstance.close();
            };
        }]);
