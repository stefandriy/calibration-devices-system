angular
    .module('adminModule')
    .factory('UserService', function ($http) {
        return {
            isAdmin: function () {
                return getData('employee/admin/users/verificator');
            },
            isUsernameAvailable: function(username){
                return getData('/admin/users/available/', username)
            },
            saveUser: function (userData) {
                return saveData('employee/admin/users/add', userData);
            }

          /*      function (username) {
                var url = '/admin/users/available/' + username;
                return $http.get(url)
                    .then(function(result) {
                        return result.data;
                    });
            }*/
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

        function saveData(url, data) {
            return $http.post(url, data)
                .success(function (response) {
                    return response;
                })
                .error(function (err) {
                    alert(err);
                    alert("in error");
                    return err;
                });
        }
    });
