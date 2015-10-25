angular
    .module('adminModule')
    .factory('RoleService', function ($http) {
        return {
            isSuperAdmin: function () {
                var url = '/admin/is_super_admin/';
                return $http.get(url)
                    .then(function(result) {
                        return result;
                    });
            }
        };
    });

