angular
    .module('employeeModule')
    .factory('EmployeeService', function ($http) {
        return {
            getAll: function (pageNumber, itemsPerPage) {
                console.log(pageNumber);
                return getData('employee/admin/users/' + pageNumber + "/" + itemsPerPage);
            }
        };

        function getData(url, params) {
            return $http.get(url, {
                params: params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }
    });
