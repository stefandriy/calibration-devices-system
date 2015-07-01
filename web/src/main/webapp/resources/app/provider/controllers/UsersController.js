/**
 * Created by MAX on 25.06.2015.
 */
angular
    .module('providerModule')
    .controller('UsersController', ['$scope', 'UserService' , '$log',
        function ($scope, userService,$log) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];
            $scope.idOrganization=0;



            $scope.onTableHandling = function () {
                userService
                    .getPage($scope.currentPage, $scope.itemsPerPage,$scope.idOrganization,$scope.searchData)
                    .then(function (data) {
                        $scope.pageContent = data.content;
                        $scope.totalItems = data.totalItems;
                        $scope.idOrganization=data.idOrganization;

                        $log.info(data.content + "resp Content");
                        $log.info(data.totalItems + "resp items");
                    });

            };

           $scope.onTableHandling();
        }
    ]);

