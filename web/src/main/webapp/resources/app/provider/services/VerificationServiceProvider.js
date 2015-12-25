angular
    .module('employeeModule')
    .factory('VerificationServiceProvider', ['$http', '$log', function ($http, $log) {

        return {
            getNewVerifications: function (currentPage, itemsPerPage, search, sortCriteria, sortOrder) {
            	
            	return getDataWithParams('provider/verifications/new/' + currentPage + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },
            getNewVerificationsForMainPanel: function (currentPage, itemsPerPage, search) {
                return getDataWithParams('provider/verifications/new/mainpanel/' + currentPage + '/' + itemsPerPage, search);
            },
            getArchiveVerifications: function (currentPage, itemsPerPage, search, sortCriteria, sortOrder) {
            	return getDataWithParams('provider/verifications/archive/' + currentPage + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
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
            checkMailIsExist: function(form) {
                return sendData('mailExist', form);
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
            },
            getNewVerificationEarliestDate: function () {
                return getData('verifications/new/earliest_date/provider');
            },
            getArchivalVerificationEarliestDate: function () {
                return getData('verifications/archive/earliest_date/provider');
            },
            getIfEmployeeProvider: function(url) {
                return getData('verifications/provider/role');
            },
            saveVerification: function(data) {
                return sendData('save', data);
            },
            saveAdditionalInfo: function(data) {
                $log.debug("from service " +  data)
                return updateData('saveInfo', data);
            },
            editCounterInfo: function(data) {
                return updateData('editCounterInfo', data);
            },
            checkIfAdditionalInfoExists: function(verifId) {
                return checkInfo('calibrator/verifications/checkInfo/' +  verifId);
            },
            getVerificationById: function (code) {
                return getData('applications/verification/' + code);
            },
            getCountOfNewNotStandardVerifications : function(url) {
                return getData('not-standard-verifications/new/count');
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

        function getMailExistValidation(url) {
            return $http.get('provider/applications/' + url)
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

        function saveInfo(url, data) {
            //console.log("from service" + data);
            return $http.post(url, data)
                .success(function (response) {
                    console.log(response);
                    return response;
                })
                .error(function (err) {
                    console.log(err);
                    return err;
                });
        }
        function checkInfo(url) {
            return $http.get(url)
                .success(function (response) {
                    return response;
                })
                .error(function (err) {
                    return err;
                });
        }
        function findInfo(url) {
            return $http.get(url)
                .success(function (response) {
                    return response;
                })
                .error(function (err) {
                    return err;
                });
        }
    }]);
