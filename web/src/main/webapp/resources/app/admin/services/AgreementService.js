angular
    .module('adminModule')
    .factory('AgreementService', function ($http) {
        return {
            getPage: function (pageNumber, itemsPerPage, search, sortCriteria, sortOrder) {
                return getDataWithParams(pageNumber + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },

            saveAgreement: function (formData) {
                return sendData('add', formData);
            },
            getAgreementById: function (id) {
                return getData("get/" + id);
            },
            editAgreement: function (formData, id) {
                return sendData('edit/' + id, formData);
            },
            disableAgreement: function (id) {
                return getData("disable/" + id);
            }
        };

        function sendData(url, data) {
            return $http.post('/admin/agreement/' + url, data)
                .then(function (result) {
                    return result.status;
                });
        }

        function getData(url) {
            return $http.get('/admin/agreement/' + url).success(function (result) {
                return result;
            }).error(function (err) {
                return err;
            });
        }

        function getDataWithParams(url, params) {
            return $http.get('/admin/agreement/' + url, {
                params: params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }
    });