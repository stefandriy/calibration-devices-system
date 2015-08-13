angular
    .module('employeeModule')
    .factory('CalibrationTestServiceCalibrator', function ($http) {
        return {
            getCalibrationTests: function (testId) {
                var url = '/calibrationTests/' + testId;

                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },

            getPage: function (pageNumber, itemsPerPage, search, id) {
                var url = '/calibrator/verifications/calibration-test/' + pageNumber + '/' + itemsPerPage + '/' + search + '/' + id;
                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },
            saveCalibrationTest: function (formData, verificationId) {
                return $http.post("/calibrator/calibrationTests/add/" + verificationId, formData)
                    .then(function (result) {
                        return result.status;
                    });
            },
            saveCalibrationTestData: function  (formdata, testId) {
              return $http.post("/calibrator/calibrationTestData/addTestData/" + testId, formdata)
                  .then(function(result) {
                    return result.status;
                });
            },
            deleteCalibrationTest: function (testId) {
                var url = '/calibrator/calibrationTests/delete/' + testId;
                return $http.post(url)
                    .then(function (result) {
                        return result.status;
                    });
            },

            editCalibrationTest: function (formData, testId) {
                var url = '/calibrator/calibrationTests/edit/' + testId;
                return $http.post(url, formData)
                    .then(function (result) {
                        return result.status;
                    });
            },

            getCalibrationTestWithId: function (testId) {
                var url = '/calibrator/calibrationTests/getTest/' + testId;
                return $http.get(url).then(function (result) {
                    return result.data;
                });
            }
        }
    });
