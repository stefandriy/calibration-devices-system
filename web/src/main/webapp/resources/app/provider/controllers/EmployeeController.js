angular
    .module('providerModule')
    .controller('EmployeeController', ['$scope',
        function ($scope) {
            $scope.hello = "Hello";
        }]);
