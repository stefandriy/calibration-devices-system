angular
    .module('adminModule')
    .factory('CounterTypeService', function ($http) {
        return {
            getPage: function (pageNumber, itemsPerPage, search, sortCriteria, sortOrder) {
                return getDataWithParams('/admin/counter-type/' + pageNumber + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },
            saveCounterType: function (formData) {
                return $http.post("/admin/counter-type/add", formData)
                    .then(function (result) {
                        return result.status;

                    });
            },
            getCounterTypeById: function (id) {
                var url = '/admin/counter-type/get/' + id;
                return $http.get(url).then(function (result) {
                    return result.data;
                });
            },
            editCounterType: function (formData, id) {
                var url = '/admin/counter-type/edit/' + id;
                return $http.post(url, formData)
                    .then(function (result) {
                        return result.status;
                    });
            },
            deleteCounterType: function (id) {
                var url = '/admin/counter-type/delete/' + id;
                return $http.delete(url)
                    .then(function (result) {
                        return result.status;
                    });
            },
            findAllDevices : function() {
                return getData('devices');
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

        function getData(url) {
            return $http.get('application/' + url).success(function(data) {
                return data;
            }).error(function(err) {
                return err;
            });
        }
    });