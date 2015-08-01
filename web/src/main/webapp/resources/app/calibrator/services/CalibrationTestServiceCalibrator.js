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

            getPage: function (pageNumber, itemsPerPage, search, id) {
                var url = '/calibrator/verifications/calibration-test/' + pageNumber + '/' + itemsPerPage +'/'+ search +'/'+ id;
                //if (search != null && search != undefined && search != "")
                //    url += '/' + search;
                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },
            ////IN PROGRESS!
            saveCalibrationTest: function(formData, verificationId) {
                return $http.post("/calibrator/calibrationTests/add/" + verificationId, formData)
                    .then(function(result) {
                        return result.status;
                    });
            }
        }
    });
