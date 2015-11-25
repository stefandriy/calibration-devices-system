/**
 * Created by Sonka on 23.11.2015.
 */
angular
    .module('adminModule')
    .factory('UnsuitabilityReasonService', function ($http) {
        return {
            getPage: function (pageNumber, itemsPerPage) {
                return getDataWithParams('admin/unsuitability-reasons/' + pageNumber + '/' + itemsPerPage);
            },
            saveUnsuitabilityReason: function (formData) {
                return send('admin/unsuitability-reasons/add', formData);

            },
            getDevices: function () {
                return getData('admin/unsuitability-reasons/devices');
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

        function send(url, data) {
            return $http.post(url, data)
                .success(function (data) {
                    return data;
                }).error(function (err) {
                    console.log(err);
                    return err;
                });
        }

        function getData(url) {
            return $http.get(url).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }
    });


/* getUnsuitabilityReasonById: function (id) {
 var url = 'admin/unsuitability-reasons/get/' + id;
 return $http.get(url).then(function (result) {
 return result.data;
 });
 }
 /* editUnsuitabilityReason: function (formData, id) {
 var url = 'admin/unsuitability-reasons/edit/' + id;
 return $http.post(url, formData)
 .then(function (result) {
 return result.status;
 });
 }, */
/*
 },*/
