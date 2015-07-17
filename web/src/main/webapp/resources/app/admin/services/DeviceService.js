angular
    .module('adminModule')
    .factory('DeviceService', function ($http) {
        return {
            isDeviceidAvailable: function (id) {
                var url = '/admin/devices/available/' + id;
                return $http.get(url)
                    .then(function(result) {
                        return result.data;
                    });
            }
        }
    });