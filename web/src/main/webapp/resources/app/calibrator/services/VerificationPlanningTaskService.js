angular
    .module('employeeModule')
    .factory('VerificationPlanningTaskService', ['$http', function ($http) {

        return {
           getVerificationsByCalibratorEmployeeAndTaskStatus: function (pageNumber, itemsPerPage) {
                var url  = 'task/findAll/' + pageNumber + '/' + itemsPerPage;
                console.log(url);
                return getData('task/findAll/' + pageNumber + '/' + itemsPerPage);
           }

        };

        return{
             saveTask: function (verifId, task) {
                 var url = '/add/' + verifId;
                    return $http.post(url, task)
                      .then(function (result) {
                          return result.status;
                      });
             }
        };

        function getData (url) {
            console.log(url);
            return $http.get(url)
            .success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }




    }]);
