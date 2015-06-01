angular
    .module('welcomeModule')
    .factory('DataReceivingService', ['$http', '$log', function ($http, $log) {

        return {
            findAllRegions: function () {
                return getData('regions');
            },
            findDistrictsByRegionId: function (id) {
                return getData('districts/' + id);
            },
            findLocalitiesByDistrictId: function (id) {
                return getData('localities/' + id);
            },
            findProvidersByDistrictDesignation: function (designation) {
                return getData("providers/" + designation);
            },
            findStreetsByLocalityId: function (id) {
                return getData('streets/' + id);
            },
            findBuildingsByStreetId: function (id) {
                return getData('buildings/' + id);
            },
            getClientApplicationStatusByCode: function (code) {
                return getData('check/' + code);
            }
        };

        function getData(url) {
            return $http.get('application/' + url)
                .success(function (data) {
                    return data;
                })
                .error(function (err) {
                    return err;
                });
        }
    }]);
