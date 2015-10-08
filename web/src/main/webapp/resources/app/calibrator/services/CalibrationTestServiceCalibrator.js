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
            saveCalibrationTest: function (formData, testId) {
                return $http.post("/calibrator/calibrationTests/add/" + testId, formData)
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
            deleteCalibrationTest: function (calibrationTestId) {
                var url = '/calibrator/calibrationTests/delete/' + calibrationTestId;
                return $http.post(url)
                    .then(function (result) {
                        return result.status;
                    });
            },
            getEmptyTest: function (verificationId) {
                var url = '/calibrator/calibrationTests/createEmptyTest/' + verificationId;
                return $http.get(url)
                    .then(function (result) {
                        return result.data;
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
            },

            parseBbiFile: function (fileName) {
                var dotIndex = fileName.lastIndexOf('.');
                var extension = fileName.substring(dotIndex + 1);
                fileName = fileName.substring(0, dotIndex);
                var url = '/calibrator/calibrationTestData/parseBbi/' + fileName + '/' + extension;
                return $http.get(url).then(function (result) {
                    return result;
                })
            }
        }
    });
