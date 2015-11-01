angular
    .module('employeeModule')
    .factory('DigitalVerificationProtocolsServiceCalibrator',
            function($http) {
                return {
                    getPage: function (pageNumber, itemsPerPage, search) {
                        var url = '/calibrator/protocols/' + pageNumber + '/' + itemsPerPage;
                        if (search != null && search != undefined && search != "")
                            url += '/' + search;

                        return $http.get(url)
                            .then(function (result) {
                                return result.data;
                            });
                    },
                }
            });