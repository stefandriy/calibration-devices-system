angular
    .module('providerModule')
    .factory('VerificationService', ['$http', '$log', function ($http, $log) {

        return {
            getArchivalVerifications: function (currentPage, itemsPerPage) {
                return getData('verifications/archive/' + currentPage + '/' + itemsPerPage);
            },
            getNewVerifications: function (currentPage, itemsPerPage) {
                return getData('verifications/new/' + currentPage + '/' + itemsPerPage);
            },
            getArchivalVerificationDetails: function (verificationId) {
                return getData('verifications/archive/' + verificationId);
            },
            getNewVerificationDetails: function (verificationId) {
                return getData('verifications/new/' + verificationId);
            },
            getCalibrators: function (url) {
                return getData('verifications/new/calibrators');
            },
            sendVerificationsToCalibrator: function (data) {
                return updateData('new/update', data);
            },
            sendInitiatedVerification:function(form){
                return sendData("send",form);
            },
            getLocalitiesCorrespondingProvider:function(url){
                return getData("applications/localities");
            },
            getStreetsCorrespondingLocality:function(selectedLocality){
                return getDataFromCatalog("streets/" + selectedLocality.id);
            },
            getBuildingsCorrespondingStreet:function(selectedBuilding){
                    return getDataFromCatalog("buildings/" + selectedBuilding.id);
                }


        };

        function getData(url) {

            $log.info(url);

            return $http.get('provider/' + url)
                .success(function (data) {
                    return data;
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

        function sendData(url, data) {
            return $http.post('provider/applications/' + url, data)
                .success(function (responseData) {
                    return responseData;
                })
                .error(function (err) {
                    return err;
                });
        }
        function getDataFromCatalog(url) {
            return $http.get('application/' + url)
                .success(function (data) {
                    return data;
                })
                .error(function (err) {
                    return err;
                });
        }
    }]);
