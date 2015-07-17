angular
    .module('adminModule')
    .controller('DeviceController', ['$rootScope','$scope', '$http', 'DevicesService',
        function ($rootScope, $scope, $http, devicesService) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];
       
            /**
             * Updates the table with device.
             */
            $rootScope.onTableHandling = function () {
                devicesService
                    .getPage($scope.currentPage, $scope.itemsPerPage, $scope.searchData)
                    .then(function (data) {
                        $scope.pageContent = data.content;
                        $scope.totalItems = data.totalItems;
                    });
            };

            $rootScope.onTableHandling();
    }]);