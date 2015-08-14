angular
    .module('employeeModule')
    .factory('AddressServiceProvider', ['$http', '$log', function ($http, $log) {
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
            findStreetsByLocalityId: function (id) {
                return getData('streets/' + id);
            },
            findBuildingsByStreetId: function (id) {
                return getData('buildings/' + id);
            },
            findAllDevices : function() {
				return getData('devices');
			},
			findProvidersByDistrict : function(district) {
				return getData("providers/" + district);
			},
			findCalibratorsByDistrict : function(district) {
				return getData("calibrators/" + district);
			},
			checkOrganizationType : function() {
				return getDataAboutOrganization('organizationType');
			},
			findMailIndexByLocality : function(localityDesignation ,districtId) {
				return getData('localities/' + localityDesignation+'/'+districtId);
			},
			findStreetsTypes : function() {
				return getData('streetsTypes/');
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
        
        function getDataAboutOrganization(url) {
            return $http.get('provider/applications/' + url)
                .success(function (data) {
                    return data;
                })
                .error(function (err) {
                    return err;
                });
        }
    }]);
