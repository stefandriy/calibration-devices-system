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
                return getData('employee/admin/users/capacityOfEmployee'+ '/'+username);            },
            getPage: function (currentPage, itemsPerPage, searchObj) {
                return getDataWithParam('employee/admin/users/' + currentPage + '/' + itemsPerPage, searchObj);
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
    });
