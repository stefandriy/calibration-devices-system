angular
    .module('employeeModule')
    .factory('DigitalVerificationProtocolsServiceCalibrator',['$http', '$log',
    function($http, $log) {
        return {

            getProtocols: function (currentPage, itemsPerPage, search, sortCriteria, sortOrder) {
                return getData('calibrator/protocols/' + currentPage + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },
        };

        function getData(url, params) {
            return $http.get(url, {
                params : params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }

    }]);