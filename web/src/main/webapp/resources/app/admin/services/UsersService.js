angular
    .module('adminModule')
    .factory('UsersService', function ($http) {
        return {
            getPage: function (currentPage, itemsPerPage, searchObj, filterObj) {
                var field;
                var value;
                for (var key in filterObj) {
                    field = key;
                    value = filterObj[field];
                }
                value=='asc'?field=field:field="-"+field;
                return getData('admin/users/' + currentPage + '/' + itemsPerPage + '/' +
                    field, searchObj);
            }
        }

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