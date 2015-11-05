angular
    .module('employeeModule')
    .factory('DigitalVerificationProtocolsServiceCalibrator', ['$http', function ($http) {
        return {
            getPage: function (pageNumber, itemsPerPage /*, search*/) {
                var url = '/calibrator/protocols/' + pageNumber + '/' + itemsPerPage;
                /*  if (search != null && search != undefined && search != "")
                 url += '/' + search;*/

                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },
            sendDataProtocol: function (url, data) {
                return $http.put('calibrator/protocols/' + url, data)
                    .success(function (responseData) {
                        return responseData;
                    })
                    .error(function (err) {
                        return err;
                    });
            }
        };


    }]);