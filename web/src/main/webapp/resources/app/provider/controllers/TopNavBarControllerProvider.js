angular
    .module('employeeModule')
    .controller('TopNavBarControllerProvider', ['$scope', '$http','UserService', function ($scope, $http, UserService) {
        $scope.logout = function () {
            $http({
                method: 'POST',
                url: '/logout'
            }).then(function () {
                window.location.replace("/");
            });
        };

        $scope.employee=null;
        UserService.loggedInUser().
            then(function (data) {
                $scope.employee = data;
            });
    }]);
