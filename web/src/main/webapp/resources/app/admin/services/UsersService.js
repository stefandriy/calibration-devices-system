angular
    .module('adminModule')
    .factory('UsersService', function ($http) {
        return {
            getPage: function (currentPage, itemsPerPage, searchObj, sortCriteria, sortOrder) {
                return getData('admin/users/' + currentPage + '/' + itemsPerPage + '/'
                   + sortCriteria +  '/' + sortOrder, searchObj);
            },
            getSysAdminsPage : function (currentPage, itemsPerPage, searchObj, sortCriteria, sortOrder) {

                return getData('admin/sysadmins/' + currentPage + '/' + itemsPerPage + '/'
                    + sortCriteria +  '/' + sortOrder, searchObj);
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

        function getDataWithParam(url, params) {
            return $http.get(url, {
                params : params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }


    });