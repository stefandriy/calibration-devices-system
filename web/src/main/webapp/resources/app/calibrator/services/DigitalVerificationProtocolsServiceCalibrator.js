angular
    .module('employeeModule')
    .factory('DigitalVerificationProtocolsServiceCalibrator',['$http', '$log',
    function($http, $log) {
        return {

            getProtocols: function (currentPage, itemsPerPage, search, sortCriteria, sortOrder) {
                return getData('calibrator/protocols/' + currentPage + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },
            sendProtocols: function (protocol) {
                return send('/calibrator/protocols/send', protocol);
            },
            getVerificators: function (url) {
                return getData('/calibrator/protocols/verificators');
            },
            };


        function getData(url, params) {
            $log.info(url);
            return $http.get(url, {
                params : params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        };
        function send (url, protocol) {
            return $http.put(url, protocol)
                .success(function (data) {
                    return data;
                }).error(function (err) {
                    console.log(err);
                    return err;
                });
        }


    }]);