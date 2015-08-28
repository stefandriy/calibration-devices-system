 angular
    .module('employeeModule')
    .factory('MeasuringEquipmentServiceCalibrator', 
    function ($http) {
        return {
        		getPage: function (pageNumber, itemsPerPage, search) {
        		var url = '/calibrator/mEquipment/' + pageNumber + '/' + itemsPerPage;
        		if (search != null && search != undefined && search != "")
        			url += '/' + search;
        		
        		return $http.get(url)
        		.then(function (result) {
        			return result.data;
        			});
        		},
            	isEquipmentNameAvailable: function (Ename) {
                var url = '/calibrator/mEquipment/available/' + Ename;
                return $http.get(url)
                    .then(function(result) {
                        return result.data;
                    });
            	},
                saveEquipment: function(formData){
                	return $http.post("/calibrator/mEquipment/add", formData)
                	.then(function(result){
                		return result.status;
                	});
                },
        		getEquipmentWithId : function(id) {
					var url = '/calibrator/mEquipment/getEquipment/' + id;
					return $http.get(url).then(function(result) {
						return result.data;
					});
				},

				editEquipment : function(formData, id) {
					var url = '/calibrator/mEquipment/edit/' + id;
					return $http.post(url, formData)
							.then(function(result) {
								return result.status;
							});
				},
				
				deleteEquipment : function(mEquipmentId){
					var url = '/calibrator/mEquipment/delete/' + mEquipmentId;
					return $http.post(url)
					.then(function(result) {
						return result.status;
					});
				}
            
        }
    });