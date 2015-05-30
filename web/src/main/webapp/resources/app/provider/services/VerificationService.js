angular
    .module('providerModule')
    .factory('VerificationService', ['$http', '$log', function ($http, $log) {

        return {
            getArchivalVerifications: function (currentPage, itemsPerPage) {
                return getData('archive/' + currentPage + '/' + itemsPerPage);
            },
            getNewVerifications: function (currentPage, itemsPerPage) {
                return getData('new/' + currentPage + '/' + itemsPerPage);
            },
            getArchivalVerificationDetails: function (verificationId) {
                return getData('archive/' + verificationId);
            },
            getNewVerificationDetails: function (verificationId) {
                return getData('new/' + verificationId);
            },
            getCalibrators: function (url) {
                return getData('new/calibrators');
            },
            sendVerificationsToCalibrator: function (data) {
                return updateData('new/update', data);
            }
        };

        function getData(url) {

            $log.info(url);

            return $http.get('provider/verifications/' + url)
                .success(function (data) {
                    return data;
                })
                .error(function (err) {
                    return err;
                });
        }

        function saveData(url, data) {
            return $http.post('provider/verifications/' + url, data)
                .success(function (responseData) {
                    return responseData;
                })
                .error(function (err) {
                    return err;
                });
        }

        function updateData(url, data) {
            return $http.put('provider/verifications/' + url, data)
                .success(function (responseData) {
                    return responseData;
                })
                .error(function (err) {
                    return err;
                });
        }
    }]);
