angular
    .module('employeeModule')
    .factory('VerificationPlanningTaskService', ['$http', function ($http) {

        return {
           getVerificationsByCalibratorEmployeeAndTaskStatus: function (pageNumber, itemsPerPage) {
                return getData('task/findAll/' + pageNumber + '/' + itemsPerPage);
           },
           getModuls: function (place, pickerDate) {
                return getAvailableModules('task/findAllModules/' + place + '/' + pickerDate);
           }

        };

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


        //return{
        //     saveTask: function (verifId, task) {
        //         var url = '/add/' + verifId;
        //            return $http.post(url, task)
        //              .then(function (result) {
        //                  return result.status;
        //              });
        //     }
        //};


    }]);
