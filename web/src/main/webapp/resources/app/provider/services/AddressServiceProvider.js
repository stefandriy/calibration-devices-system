angular
    .module('employeeModule')
    .factory('AddressServiceProvider', ['$http', '$log', function ($http, $log) {
        return {
            findAllRegions: function () {
                return getProviderData('region');
            },
            findAllDevices : function() {
                return getProviderData('devices');
            },
            findDistrictsByRegionId: function (id) {
                return getProviderData('districts/' + id);
            },
            findLocalitiesByDistrictId: function (id) {
                return getProviderData('localities/' + id);
            },
            findStreetsByLocalityId: function (id) {
                return getData('streets/' + id);
            },
            findBuildingsByStreetId: function (id) {
                return getData('buildings/' + id);
            },

            findCalibratorsForProviderByType : function(type) {
				return getData("calibrators/" + type);
			},
			checkOrganizationType : function() {
				return getProviderData('organizationType');
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
        
        function getProviderData(url) {
            return $http.get('provider/applications/' + url)
                .success(function (data) {
                    return data;
                })
                .error(function (err) {
                    return err;
                });
        }
    }]);
