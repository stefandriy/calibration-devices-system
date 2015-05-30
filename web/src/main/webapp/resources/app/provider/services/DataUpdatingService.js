angular
    .module('providerModule')
    .factory('DataUpdatingService', ['$http', function ($http) {
        this.updateData = function (url, data) {
            return $http.put(url, data)
                .success(function (responseData) {
                    return responseData;
                })
                .error(function (err) {
                    return err;
                });
        };
    }]);
