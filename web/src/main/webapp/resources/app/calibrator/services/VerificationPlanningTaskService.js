angular
    .module('employeeModule')
    .factory('VerificationPlanningTaskService', ['$http', '$log', function ($http, $log) {

    return{
        saveTask: function (verifId, task) {
            var url = 'calibrator/verifications/task/add/' + verifId;
            return $http.post(url, task)
                .then(function (result) {
                    return result.status;
                });
        },

        getVerificationsByCalibratorEmplAndTaskStatus: function (pageNumber, itemsPerPage){
            var url = 'calibrator/verifications/task/findAll/' + pageNumber + '/' + itemsPerPage;
            return $http.get(url)
                .then(function (result) {
                    return result;
                });
        }
    }





    }]);
