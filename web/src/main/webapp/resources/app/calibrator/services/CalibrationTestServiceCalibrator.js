angular
    .module('employeeModule')
    .factory('CalibrationTestServiceCalibrator', function ($http) {
    	return {
            getCalibrationTests: function (testId) {
                var url = '/calibrationTests/' + testId;

                return $http.get(url)
                    .then(function(result) {
                        return result.data;
                    });
            },
            saveCalibrationTest: function(formData) {
                return $http.post("/calibrationTests/add", formData)
                    .then(function(result) {
                        return result.status;
                    });
            }
            
        }
    });