angular.module('adminModule')
	.factory('OrganizationService', ['$http', '$log', function ($http, $log) {

			return {
				getPage : function(currentPage, itemsPerPage, search, sortCriteria, sortOrder) {
					return getDataWithParams('/admin/organization/' + currentPage + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
				},
				saveOrganization : function(formData) {
					return $http.post("/admin/organization/add", formData)
							.then(function(result) {
								return result.status;
								
							});
				},
				getOrganizationWithId : function(id) {
					var url = '/admin/organization/getOrganization/' + id;
					return $http.get(url).then(function(result) {
						return result.data;
					});
				},

				editOrganization : function(formData, id) {
					var url = '/admin/organization/edit/' + id;
					return $http.post(url, formData)
							.then(function(result) {
								return result.status;
							});
				}

			};
			function getDataWithParams(url, params) {
				return $http.get(url, {
					params : params
				}).success(function (data) {
					return data;
				}).error(function (err) {
					return err;
				});
			}

		}]);