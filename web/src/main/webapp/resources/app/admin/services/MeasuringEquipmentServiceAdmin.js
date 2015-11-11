angular
    .module('adminModule')
    .factory('MeasuringEquipmentServiceAdmin',
    function ($http) {
        return {
            getPage: function (pageNumber, itemsPerPage, search) {
                var url = '/admin/calibration-module/' + pageNumber + '/' + itemsPerPage;
                if (search != null && search != undefined && search != "")
                    url += '/' + search;

                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },
            isEquipmentNameAvailable: function (Ename) {
                var url = '/admin/calibration-module/available/' + Ename;
                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },
            saveEquipment: function (formData) {
                return $http.post("/admin/calibration-module/add", formData)
                    .then(function (result) {
                        return result.status;
                    });
            },
            getEquipmentWithId: function (id) {
                var url = '/admin/calibration-module/getEquipment/' + id;
                return $http.get(url).then(function (result) {
                    return result.data;
                });
            },

            editEquipment: function (formData, id) {
                var url = '/calibration-module/edit/' + id;
                return $http.post(url, formData)
                    .then(function (result) {
                        return result.status;
                    });
            },

            deleteEquipment: function (mEquipmentId) {
                var url = '/admin/calibration-module/delete/' + mEquipmentId;
                return $http.post(url)
                    .then(function (result) {
                        return result.status;
                    });
            }

        }
    });