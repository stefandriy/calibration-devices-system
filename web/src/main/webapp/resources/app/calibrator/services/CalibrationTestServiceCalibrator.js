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

            getPage: function (pageNumber, itemsPerPage, search) {
                var url = '/calibrationTests/' + pageNumber + '/' + itemsPerPage;
                if (search != null && search != undefined && search != "")
                    url += '/' + search;

                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },
            saveCalibrationTest: function(formData) {
                return $http.post("/calibrationTests/add", formData)
                    .then(function(result) {
                        return result.status;
                    });
            }
            ////IN PROGRESS!
            //saveCalibrationTest: function(formData, verificationId) {
            //    return $http.post("/calibrationTests/add", formData, verificationId)
            //        .then(function(result) {
            //            return result.status;
            //        });
            //}
        }
    });
