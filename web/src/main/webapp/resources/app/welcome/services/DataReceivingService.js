angular.module('welcomeModule').factory('DataReceivingService',
		[ '$http', '$log', function($http, $log) {

			return {
				findAllRegions : function() {
					return getData('regions');
				},
				findAllDevices : function() {
					return getData('devices');
				},
				findDistrictsByRegionId : function(id) {
					return getData('districts/' + id);
				},
				findLocalitiesByDistrictId : function(id) {
					return getData('localities/' + id);
				},
				findProvidersByDistrict : function(district) {
					return getData("providers/" + district);
				},
				findStreetsByLocalityId : function(id) {
					return getData('streets/' + id);
				},
				findBuildingsByStreetId : function(id) {
					return getData('buildings/' + id);
				},
				getVerificationStatusById : function(code) {
					return getData('check/' + code);
				},
				getVerificationById : function(code) {
					return getData('verification/' + code);
				}
			};

			function getData(url) {
				return $http.get('application/' + url).success(function(data) {
					return data;
				}).error(function(err) {
					return err;
				});
			}
		} ]);
