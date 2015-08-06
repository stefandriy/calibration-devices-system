angular
    .module('employeeModule')
    .factory('UserService', function ($http) {
        return {
            isUsernameAvailable: function (username) {
                return getData('employee/admin/users/available/' + username);
            },
            saveUser: function (userData) {
                return saveData('employee/admin/users/add', userData);
            },
            isAdmin: function () {
                return getData('employee/admin/users/verificator');
            },
            getPage: function (currentPage, itemsPerPage, searchObj,filterObj) {
                var lastName=0;
                filterObj.lastName=='asc'?lastName=1:lastName=-1;
                return getData('employee/admin/users/' + currentPage + '/' + itemsPerPage + '/' + lastName,  searchObj);
            },
            getCapacityOfWork: function(username){
                return getData('employee/admin/users/capacityOfEmployee'+ '/'+username);
            },
            getGraficData: function(dataToSearch){
                return getData('provider/admin/users/graphic',dataToSearch);
            },
            getUser: function(username){
                return getData('employee/admin/users/getUser/' + username);
            },
            updateUser: function(userData) {
                return saveData('employee/admin/users/update', userData);
            },
            getOrganizationEmployeeCapacity : function() {
                return getData('employee/admin/users/organizationCapacity');
            },

            loggedInUser: function() {
                return getLoginUser();
            }
        };

        function getLoginUser() {
            return $http.get('/loginuser')
                .then(function(result) {
                    return result.data;
                });
        }

        function getData(url, params) {
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
                })
                .error(function (err) {
                    alert(err);
                    alert("in error");
                    return err;
                });
        }


    });
