angular
    .module('employeeModule')
    .controller('TopNavBarControllerVerificator', ['$scope', '$http', function ($scope, $http) {
        $scope.logout = function () {
            $http({
                method: 'POST',
                url: '/logout'
            }).then(function () {
                window.location.replace("/");
            });
        };
    }]);
