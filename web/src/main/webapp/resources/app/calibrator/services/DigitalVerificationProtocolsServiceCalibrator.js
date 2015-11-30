angular
    .module('employeeModule')
    .factory('DigitalVerificationProtocolsServiceCalibrator', ['$http',
        function ($http) {
            return {
                getProtocols: function (currentPage, itemsPerPage) {
                    var url = 'calibrator/protocols/' + currentPage + '/' + itemsPerPage;
                    return $http.get(url)
                        .success(function (data) {
                            return data;
                        }).error(function (err) {
                            return err;
                        });
                },
                sendProtocols: function (protocol) {
                    var url = '/calibrator/protocols/send';
                    return $http.put(url, protocol)
                        .success(function (data) {
                            return data;
                        }).error(function (err) {
                            return err;
                        });
                },
                getVerificators: function (url) {
                    var url = '/calibrator/protocols/verificators';
                    return $http.get(url)
                        .success(function (data) {
                            return data;
                        }).error(function (err) {
                            return err;
                        });
                },
            };

        }]);