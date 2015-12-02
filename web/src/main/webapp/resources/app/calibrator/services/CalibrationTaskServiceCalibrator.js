angular
    .module('employeeModule')
    .factory('CalibrationTaskServiceCalibrator', ['$http', function ($http) {

        return {
            getPage: function (pageNumber, itemsPerPage, search, sortCriteria, sortOrder) {
                return getDataWithParams(pageNumber + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            }
        };

        function sendData(url, data) {
            return $http.post('task/' + url, data)
                .then(function (result) {
                    return result.status;
                });
        }

        function getData(url) {
            return $http.get('task/' + url).success(function (result) {
                return result;
            }).error(function (err) {
                return err;
            });
        }

        function getDataWithParams(url, params) {
            return $http.get('task/' + url, {
                params: params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }

    }]);
