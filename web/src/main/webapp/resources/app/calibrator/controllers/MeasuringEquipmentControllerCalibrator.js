angular
    .module('employeeModule')
    .controller('MeasuringEquipmentControllerCalibrator', ['$rootScope','$scope', '$modal','MeasuringEquipmentServiceCalibrator', '$timeout',
        function ($rootScope, $scope, $modal, equipmentServiceCalibrator, $timeout) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];
       
            /**
             * Updates the table with MeasuringEquipments.
             */
            $rootScope.onTableHandling = function () {
            	equipmentServiceCalibrator
					.getPage($scope.currentPage, $scope.itemsPerPage, $scope.searchData)
            		.then(function (data) {
						$scope.pageContent = data.content;
						$scope.totalItems = data.totalItems;
                });             
            };
            $rootScope.onTableHandling();
            
    		/**
			 * Opens modal window for adding new equipment.
			 */
			$scope.openAddEquipmentModal = function() {
				var addEquipmentModal = $modal
						.open({
							animation : true,
							controller : 'MeasuringEquipmentAddModalControllerCalibrator',
							templateUrl : '/resources/app/calibrator/views/modals/measuring-equipment-add-modal.html',
						});
			};

			/**
			 * Opens modal window for editing equipment.
			 */
			$scope.openEditEquipmentModal = function(
					equipmentId) {
				$rootScope.equipmentId = equipmentId;
				equipmentServiceCalibrator.getEquipmentWithId(
						$rootScope.equipmentId).then(
						function(data) {
							$rootScope.equipment = data;
						});
				var equipmentDTOModal = $modal
						.open({
							animation : true,
							controller : 'MeasuringEquipmentEditModalControllerCalibrator',
							templateUrl : '/resources/app/calibrator/views/modals/measuring-equipment-edit-modal.html',
						});
			};
			
			$scope.deleteEquipment = function (equipmentId) {
				$rootScope.equipmentId = equipmentId;
				equipmentServiceCalibrator.deleteEquipment(equipmentId);
                $timeout(function() {
                    console.log('delete with timeout');
                    $rootScope.onTableHandling();
                }, 700);

            };
			
			
    }]);