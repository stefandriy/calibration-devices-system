angular
    .module('adminModule')
    .controller('TopNavBarController', ['$scope', '$http', 'StatisticService', function ($scope, $http, StatisticService) {
        $scope.logout = function () {
            $http({
                method: 'POST',
                url: '/logout'
            }).then(function () {
                window.location.replace("/");
            });

        };
        $scope.employee=null;
        StatisticService.employee().then(function (data) {
            $scope.employee = data;
        });

    }]);