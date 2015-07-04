angular
		.module('adminModule')
		.controller(
				'OrganizationController',
				[
						'$rootScope',
						'$scope',
						'$modal',
						'$http',
						'OrganizationService',
						function($rootScope, $scope, $modal, $http,
								organizationService) {

							$rootScope.organizationId = 0;
							$rootScope.organization = {};
							$scope.totalItems = 0;
							$scope.currentPage = 1;
							$scope.itemsPerPage = 5;
							$scope.pageContent = [];
							$scope.typeData = [ {
								type : 'PROVIDER',
								name : 'Постачальник послуг'
							}, {
								type : 'CALIBRATOR',
								name : 'Повірочна організація'
							}, {
								type : 'STATE_VERIFICATION',
								name : 'Державний повірник'
							} ];

							/**
							 * Opens modal window with address form.
							 */
							$scope.openOrganizationDTO = function(
									organizationId) {
								$rootScope.organizationId = organizationId;
								organizationService.getOrganizationWithId(
										$rootScope.organizationId).then(
										function(data) {
											$rootScope.organization = data;
										});

								var organizationDTOModal = $modal
										.open({
											animation : true,
											controller : 'OrganizationDTOController',
											templateUrl : '/resources/app/admin/views/organization-modal-dto.html',
										});
							};

							/**
							 * Updates the table with organization.
							 */
							$scope.onTableHandling = function() {
								organizationService
										.getPage($scope.currentPage,
												$scope.itemsPerPage,
												$scope.searchData)
										.then(
												function(data) {
													$scope.pageContent = data.content;
													$scope.totalItems = data.totalItems;
												});
							};

							$scope.onTableHandling();
						} ]);