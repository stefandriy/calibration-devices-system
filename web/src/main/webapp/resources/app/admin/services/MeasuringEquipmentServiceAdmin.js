angular
    .module('adminModule')
    .factory('MeasuringEquipmentServiceAdmin',
    function ($http) {
        return {
            getPage: function (pageNumber, itemsPerPage, search) {
                var url = '/admin/mEquipment/' + pageNumber + '/' + itemsPerPage;
                if (search != null && search != undefined && search != "")
                    url += '/' + search;

                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },
            isEquipmentNameAvailable: function (Ename) {
                var url = '/admin/mEquipment/available/' + Ename;
                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },
            saveEquipment: function (formData) {
                return $http.post("/admin/mEquipment/add", formData)
                    .then(function (result) {
                        return result.status;
                    });
            },
            getEquipmentWithId: function (id) {
                var url = '/admin/mEquipment/getEquipment/' + id;
                return $http.get(url).then(function (result) {
                    return result.data;
                });
            },

            editEquipment: function (formData, id) {
                var url = '/mEquipment/edit/' + id;
                return $http.post(url, formData)
                    .then(function (result) {
                        return result.status;
                    });
            },

            deleteEquipment: function (mEquipmentId) {
                var url = '/admin/mEquipment/delete/' + mEquipmentId;
                return $http.post(url)
                    .then(function (result) {
                        return result.status;
                    });
            }

        }
    });