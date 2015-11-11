angular.module('adminModule')
    .factory('MeasuringEquipmentServiceAdmin', ['$http', '$log', function ($http, $log) {

        return {
            getPage: function (currentPage, itemsPerPage, search, sortCriteria, sortOrder) {
                return getDataWithParams(currentPage + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },
            saveEquipmentr: function (formData) {
                return $http.post("/admin/calibration-module/add", formData)
                    .then(function (result) {
                        return result.status;

                    });
            },

            editEquipment: function (formData, id) {
                var url = '/admin/calibration-module/edit/' + id;
                return $http.post(url, formData)
                    .then(function (result) {
                        return result.status;
                    });
            },

            getOrganizationAdmin: function (id) {
                var url = '/admin/calibration-module/getCalibrationModuleAdmin/' + id;
                return $http.get(url).then(function (result) {
                    return result.data;
                });
            },

            getHistoryOrganizationWithId: function (id) {
                var url = '/admin/calibration-module/edit/history/' + id;
                return $http.get(url).then(function (result) {
                    return result.data;
                });
            },
            getServiceAreaLocalities: function (organizationId) {
                return getData('serviceArea/localities/' + organizationId);
            },
            getServiceAreaRegion: function (districtId) {
                return getData('serviceArea/region/' + districtId);
            },
            getOrganizationByOrganizationTypeAndDeviceType: function (organizationType, deviceType) {
                return getData('getOrganization/' + organizationType + '/' + deviceType);
            }

        };
        function getDataWithParams(url, params) {
            return $http.get('/admin/calibration-module/' + url, {
                params: params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }

        function getData(url) {
            return $http.get('/admin/calibration-module/' + url)
                .success(function (data) {
                    return data;
                }).error(function (err) {
                    return err;
                });
        }

    }]);