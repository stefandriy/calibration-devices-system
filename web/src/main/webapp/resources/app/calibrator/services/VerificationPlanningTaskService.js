angular
    .module('employeeModule')
    .factory('VerificationPlanningTaskService', ['$http', function ($http) {

        return {
           saveTask: function (task) {
              return save('task/save', task);
           },
           getVerificationsByCalibratorEmployeeAndTaskStatus: function (pageNumber, itemsPerPage) {
                return getData('task/findAll/' + pageNumber + '/' + itemsPerPage);
           },
           getModuls: function (place, pickerDate, applicationFiled) {
                return getAvailableModules('task/findAllModules/' + place + '/' + pickerDate + '/' + applicationFiled);
           },
           //createExcelFileForVerifications: function (data) {
           //     return createExcelFile ('task/createExcelFile/', data);
           //}

        };

        function save (url, task) {
            return $http.post(url, task)
                .success(function (data) {
                    console.log(data);
                    return data;
                }).error(function (err) {
                    console.log(err);
                    return err;
                });
        }

        function getData (url) {
            return $http.get(url)
            .success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }

        function getAvailableModules (url) {
            return $http.get(url)
               .success(function (data) {
                    return data;
            }).error(function (err) {
                    return err;
            });
        }

        //function createExcelFile (url, data) {
        //    return $http.put (url, data)
        //        .success(function (data) {
        //            return data;
        //        }).error(function (err) {
        //            return err;
        //        });
        //}





    }]);
