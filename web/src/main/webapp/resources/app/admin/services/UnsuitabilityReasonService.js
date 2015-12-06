angular
    .module('adminModule')
    .factory('UnsuitabilityReasonService', function ($http) {
        return {
            getPage: function (pageNumber, itemsPerPage) {
                var url = 'admin/unsuitability-reasons/' + pageNumber + '/' + itemsPerPage;
                return getDataWithParams(url);
            },
            saveUnsuitabilityReason: function (formData) {
                var url = 'admin/unsuitability-reasons/add';
                return $http.post(url, formData)
                    .success(function (formData) {
                        return formData;
                    }).error(function (err) {
                        return err;
                    });
            },
            getDevices: function () {
                var url = 'admin/unsuitability-reasons/devices';
                return $http.get(url)
                    .success(function (data) {
                        return data;
                    }).error(function (err) {
                        return err;
                    });
            },
            deleteUnsuitabilityReason: function (id) {
                var url = 'admin/unsuitability-reasons/delete/' + id;
                return $http.delete(url)
                    .then(function (result) {
                        return result.status;
                    });
            }
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
    });
