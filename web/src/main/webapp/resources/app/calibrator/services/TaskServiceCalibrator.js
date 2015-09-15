angular
    .module('employeeModule')
    .factory('TaskServiceCalibrator', ['$http', '$log', function ($http, $log) {

    return{
        saveTask: function (verifId) {
            return saveData('calibrator/verifications/task/add/' + verifId);
        }
    }

        function saveData(url) {
            return $http.post(url)
                .success(function (result) {
                    return result;
                });
        }

    }]);
