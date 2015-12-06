angular
    .module('employeeModule')
    .factory('DigitalVerificationProtocolsServiceCalibrator', ['$http',
        function ($http) {
            return {
                getProtocols: function (currentPage, itemsPerPage) {
                    var url = 'calibrator/protocols/' + currentPage + '/' + itemsPerPage;
                    return getDataWithParams(url);
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
            function getDataWithParams(url, params) {
                return $http.get(url, {
                    params: params
                }).success(function (data) {
                    return data;
                }).error(function (err) {
                    return err;
                });
            }
        }]);