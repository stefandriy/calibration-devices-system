angular
    .module('employeeModule')
    .factory('TaskServiceCalibrator', ['$http', '$log', function ($http, $log) {

    return{
        saveTask: function (verifId, task) {
            var url = 'calibrator/verifications/task/add/' + verifId;
            return $http.post(url, task)
                .then(function (result) {
                    return result.status;
                });
        },
    }





    }]);
