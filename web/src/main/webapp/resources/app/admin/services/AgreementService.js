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
            //========
            getDeviceCategoryById: function (id) {
                var url = '/admin/device-category/get/' + id;
                return $http.get(url).then(function (result) {
                    return result.data;
                });
            },
            editDeviceCategory: function (formData, id) {
                var url = '/admin/device-category/edit/' + id;
                return $http.post(url, formData)
                    .then(function (result) {
                        return result.status;
                    });
            },
            deleteDeviceCategory: function (id) {
                var url = '/admin/device-category/delete/' + id;
                return $http.delete(url)
                    .then(function (result) {
                        return result.status;
                    });
            }
        };

        function sendData(url, data) {
            return $http.post('/admin/agreement/' + url, data)
                .then(function (result) {
                    return result.status;
                });
        }

        function getData(url) {
            return $http.get('/admin/agreement/' + url).success(function (data) {
                return data;
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