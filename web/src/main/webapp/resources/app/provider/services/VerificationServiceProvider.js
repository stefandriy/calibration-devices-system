angular
    .module('employeeModule')
    .factory('VerificationServiceProvider', ['$http', '$log', function ($http, $log) {

        return {
            getNewVerifications: function (currentPage, itemsPerPage, search) {
            	return getDataWithParams('provider/verifications/new/' + currentPage + '/' + itemsPerPage, search);
            },
            getNewVerificationsForMainPanel: function (currentPage, itemsPerPage, search) {
                return getDataWithParams('provider/verifications/new/mainpanel/' + currentPage + '/' + itemsPerPage, search);
            },
            getArchiveVerifications: function (currentPage, itemsPerPage, search) {
            	return getDataWithParams('provider/verifications/archive/' + currentPage + '/' + itemsPerPage, search);
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
            getProviders: function (url) {
                return getData('verifications/new/providerEmployees');
            },
            sendVerificationsToCalibrator: function (data) {
               return updateData('new/update', data);
            },
            sendEmployeeProvider: function (data) {
                return updateData('assign/providerEmployee', data);
            },
            cleanProviderEmployeeField:function (data) {
                return updateData('remove/providerEmployee', data);
            },
            sendInitiatedVerification:function(form){
                return sendData('send',form);
            },
            getLocalitiesCorrespondingProvider:function(url){
                return getData('applications/localities');
            },
            getStreetsCorrespondingLocality:function(selectedLocality){
                return getDataFromCatalog('streets/' + selectedLocality.id);
            },
            getBuildingsCorrespondingStreet:function(selectedBuilding){
                    return getDataFromCatalog('buildings/' + selectedBuilding.id);
            },
            getCountOfNewVerifications: function(url) {
            	return getData('verifications/new/count/provider');
            },
            markVerificationAsRead : function(data) {
            	return updateData('new/read', data);
            },
            acceptVerification : function(data) {
            	return updateData('new/accept', data);
            },
            rejectVerification : function(data) {
            	return updateData('new/reject', data);
            },
            sendMail : function(data) {
            	return sendData ('new/mail', data);
            }
        };

        function getData(url) {

            return $http.get('provider/' + url)
                .success(function (data) {
                	return data;
                })
                .error(function (err) {
                    return err;
                });
        }
        
        function getDataWithParams(url, params) {
            return $http.get(url, {
                params : params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }

        function updateData(url, data) {
            return $http.put('provider/verifications/' + url, data)
                .success(function (responseData) {
                   $log.info('response'  + responseData);
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
