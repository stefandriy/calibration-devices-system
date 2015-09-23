angular
		.module('adminModule')
		.controller(
				'OrganizationPanelController',
				[
						'$rootScope',
						'$scope',
						'$modal',
						'OrganizationService',
						'AddressService',
					    'ngTableParams',
						function($rootScope, $scope, $modal, organizationService,
								 addressService, ngTableParams) {

							$scope.totalItems = 0;
							$scope.currentPage = 1;
							$scope.itemsPerPage = 5;
							$scope.pageContent = [];

							$scope.clearAll = function () {
								$scope.tableParams.filter({});
							};

							$scope.doSearch = function () {
								$scope.tableParams.reload();
							};

							$scope.tableParams = new ngTableParams({
								page: 1,
								count: 10,
								sorting: {
									id: 'desc'
								}
							}, {
								total: 0,
								filterDelay: 1500,
								getData: function ($defer, params) {

									var sortCriteria = Object.keys(params.sorting())[0];
									var sortOrder = params.sorting()[sortCriteria];


									organizationService.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
										.success(function (result) {
											$scope.resultsCount = result.totalItems;
											$defer.resolve(result.content);
											params.total(result.totalItems);
											console.log(result.totalItems);
											console.log(result.content[3]);
										}, function (result) {
											$log.debug('error fetching data:', result);
										});
								}
							});


							$rootScope.onTableHandling = function() {
								organizationService
										.getPage($scope.currentPage,
												$scope.itemsPerPage,
												$scope.searchData
								              )
										.then(
												function(data) {
													$scope.pageContent = data.content;
													$scope.totalItems = data.totalItems;
												});
							};
							$rootScope.onTableHandling();

							/**
							 * Opens modal window for adding new organization.
							 */
							$scope.openAddOrganizationModal = function() {
								var addOrganizationModal = $modal.open({
											animation : true,
											controller : 'OrganizationAddModalController',
											templateUrl : '/resources/app/admin/views/modals/organization-add-modal.html',
											size: 'lg',
											resolve: {
												regions: function () {
													return addressService.findAllRegions();
												}
											}
										});
							};

							/**
							 * Opens modal window for editing organization.
							 */
							$scope.openEditOrganizationModal = function(
									organizationId) {
								$rootScope.organizationId = organizationId;
								organizationService.getOrganizationWithId(
										$rootScope.organizationId).then(
										function(data) {
											$rootScope.organization = data;
											console.log($rootScope.organization);

								var organizationDTOModal = $modal
										.open({
											animation : true,
											controller : 'OrganizationEditModalController',
											templateUrl : '/resources/app/admin/views/modals/organization-edit-modal.html',
											resolve: {
												regions: function () {
													return addressService.findAllRegions();
												}
											}
										});
										});

							};

							/**
							 * Opens modal window for show history editing organization.
							 */
							$scope.openOrganizationEditHistoryModal = function(
								organizationId) {
								$rootScope.organizationId = organizationId;
								organizationService.getHistoryOrganizationWithId(
									organizationId).then(
									function(data) {
										$rootScope.organization = data.content;
										console.log($rootScope.organization);

										var organizationDTOModal = $modal
											.open({
												animation : true,
												controller : 'OrganizationEditHistoryModalController',
												templateUrl : '/resources/app/admin/views/modals/organization-edit-history-modal.html'
											});
									});

							};



						} ]);