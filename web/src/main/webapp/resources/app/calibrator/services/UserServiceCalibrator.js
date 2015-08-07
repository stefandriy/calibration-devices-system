angular
    .module('employeeModule')
    .factory('UserServiceCalibrator', function ($http) {
        return {
            isUsernameAvailable: function (username) {
                return getData('calibrator/admin/users/available/' + username);
            },
            saveUser: function (userData) {
                return saveData('calibrator/admin/users/add', userData);
            },
            isAdmin: function (){
            	return getData('calibrator/admin/users/verificator');
            },
            getCapacityOfWork: function(username){
                return getData('employee/admin/users/capacityOfEmployee'+ '/'+username);
            },
            getPage: function (currentPage, itemsPerPage,searchObj,filterObj) {
                var field;
                var value;
                for (var key in filterObj) {
                    field = key;
                    value = filterObj[field];
                }
                value=='asc'?field=field:field="-"+field;
                return getAllUsers('employee/admin/users/' + currentPage + '/' + itemsPerPage + '/' +
                    field, searchObj);
            },
            isAdmin: function () {
                return getData('employee/admin/users/verificator');
            }

        };

        function getData(url) {
            return $http.get(url)
                .success(function (result) {
                    return result;
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

        function saveData(url, data) {
            return $http.post(url, data)
                .success(function (response) {
                    return response;
                });
        }

        function getAllUsers(url, params) {
            return $http.get(url, {
                params: params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }
    });
