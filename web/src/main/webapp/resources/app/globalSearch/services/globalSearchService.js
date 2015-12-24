/**
 * Created by Pavlo on 16.12.2015.
 */
(function () {
        'use strict';
        angular.module('globalSearchModule')
            .factory('globalSearchService', function ($http) {
                return {
                    getAllFilters: function (locationUrl) {
                        return getData(locationUrl);
                    },
                    saveFilter: function (locationUrl, filter,name) {
                        return sendData("add/"+locationUrl,filter,name);
                    },
                    deleteFilter:function(locationUrl,filterName){
                        return sendData("delete/"+locationUrl,filterName);
                    }
                };
                function getData(url) {
                    return $http.get('globalSearch' + url).success(function (result) {
                        return result;
                    }).error(function (err) {
                        return err;
                    });
                }
                function sendData(url, data,name) {
                    return $http.post('globalSearch' + url, data,name)
                        .then(function (result) {
                            return result.status;
                        });
                }
            });
    }
);