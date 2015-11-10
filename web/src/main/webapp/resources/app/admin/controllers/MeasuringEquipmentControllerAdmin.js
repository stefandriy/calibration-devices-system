angular
    .module('adminModule')
    .controller('MeasuringEquipmentControllerAdmin', ['$rootScope','$scope', '$modal','MeasuringEquipmentServiceAdmin', '$timeout',
        function ($rootScope, $scope, $modal, equipmentServiceAdmin, $timeout) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];

            /**
             * Updates the table with MeasuringEquipments.
             */
            $rootScope.onTableHandling = function () {
				equipmentServiceAdmin
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
							controller : 'MeasuringEquipmentAddModalControllerAdmin',
							templateUrl : '/resources/app/admin/views/modals/measuring-equipment-add-modal.html',
						});
			};

			/**
			 * Opens modal window for editing equipment.
			 */
			$scope.openEditEquipmentModal = function(
					equipmentId) {
				$rootScope.equipmentId = equipmentId;
				equipmentServiceAdmin.getEquipmentWithId(
						$rootScope.equipmentId).then(
						function(data) {
							$rootScope.equipment = data;
						});
				var equipmentDTOModal = $modal
						.open({
							animation : true,
							controller : 'MeasuringEquipmentEditModalControllerAdmin',
							templateUrl : '/resources/app/admin/views/modals/measuring-equipment-edit-modal.html',
						});
			};

			$scope.deleteEquipment = function (equipmentId) {
				$rootScope.equipmentId = equipmentId;
				equipmentServiceAdmin.deleteEquipment(equipmentId);
                $timeout(function() {
                    console.log('delete with timeout');
                    $rootScope.onTableHandling();
                }, 700);

            };

			$scope.setSphereOfApplicationLanguage = function () {
				var lang = $translate.use();
				if (lang === 'ukr') {
					$scope.sphereOfApplication[0].label = 'Вода';
					$scope.sphereOfApplication[1].label = 'Газ';
					$scope.sphereOfApplication[2].label = 'Електроенергія';


				} else if (lang === 'eng') {
					$scope.sphereOfApplication[0].label = 'Water';
					$scope.sphereOfApplication[1].label = 'Gas';
					$scope.sphereOfApplication[2].label = 'Electricity';
				} else {
					$log.debug(lang);
				}
			};
    }]);