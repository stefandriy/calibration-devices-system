angular
    .module('adminModule')
    .controller(
    'MeasuringEquipmentControllerAdmin',
    [
        '$rootScope',
        '$scope',
        '$log',
        '$modal',
        'MeasuringEquipmentServiceAdmin',
        'ngTableParams',
        '$filter',
        '$translate',
        function ($rootScope, $scope, $log, $modal, measuringEquipmentServiceAdmin,
                  ngTableParams, $filter, $translate) {

            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];

            /*$scope.organizationTypeData = [
                {id: 'CALIBRATOR', label: null},
                {id: 'PROVIDER', label: null},
                {id: 'STATE_VERIFICATOR', label: null},
                {id: 'NO_TYPE', label: null},
            ];*/

            /*$scope.selectedOrganizationType = {
                name: null
            };*/

            /*$scope.setTypeDataLanguage();*/

            /*$scope.clearAll = function () {
                $scope.tableParams.filter({});
                $scope.selectedOrganizationType.name = null;
            };*/

            $scope.doSearch = function () {
                $scope.tableParams.reload();
            };

            /*$scope.setTypeDataLanguage = function () {
            };*/

            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 5,
                sorting: {
                    id: 'desc'
                }
            }, {
                total: 0,
                filterDelay: 1500,
                getData: function ($defer, params) {
                    /*if ($scope.selectedOrganizationType.name != null) {
                        params.filter().type = $scope.selectedOrganizationType.name.id;
                    }
                    else {
                        params.filter().type = null;//case when the filter is cleared with a button on the select
                    }*/

                    var sortCriteria = Object.keys(params.sorting())[0];
                    var sortOrder = params.sorting()[sortCriteria];


                    measuringEquipmentServiceAdmin.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
                        .success(function (result) {
                            $scope.totalItems = result.totalItems; /*$scope.resultsCount = result.totalItems;*/
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                }
            });


            $rootScope.onTableHandling = function () {
                $scope.tableParams.reload();
            };
            $rootScope.onTableHandling();

            /**
             * Opens modal window for adding new organization.
             */
            /*$scope.openAddOrganizationModal = function () {
                var addOrganizationModal = $modal.open({
                    animation: true,
                    controller: 'OrganizationAddModalController',
                    templateUrl: '/resources/app/admin/views/modals/organization-add-modal.html',
                    size: 'lg',
                    resolve: {
                        regions: function () {
                            return addressService.findAllRegions();
                        }
                    }
                });*/

                /**
                 * executes when modal closing
                 */
                /*addOrganizationModal.result.then(function () {
                    toaster.pop('success', $filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_CREATED_ORGANIZATION'));
                });
            };*/

            /**
             * Function for ng-show. When filtering fields are not empty show button for
             * clear this fields
             * @returns {boolean}
             */
            $scope.isFilter = function () {
                var obj = $scope.tableParams.filter();
                for (var i in obj) {
                    if (obj.hasOwnProperty(i) && obj[i]) {
                        return true;
                    }
                }
                return false;
            };

            /**
             * Opens modal window for editing organization.
             */
            /*$scope.openEditOrganizationModal = function (organizationId) {
                $rootScope.organizationId = organizationId;
                organizationService.getOrganizationWithId(
                    $rootScope.organizationId).then(
                    function (data) {
                        $rootScope.organization = data;
                        var organizationDTOModal = $modal
                            .open({
                                animation: true,
                                controller: 'OrganizationEditModalController',
                                templateUrl: '/resources/app/admin/views/modals/organization-edit-modal.html',
                                size: 'lg',
                                resolve: {
                                    regions: function () {
                                        return addressService.findAllRegions();
                                    }
                                }
                            });

                        /!**
                         * executes when modal closing
                         *!/
                        organizationDTOModal.result.then(function () {
                            toaster.pop('info', $filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_EDITED_ORGANIZATION'));
                        });
                    });

            };*/

            /**
             * Opens modal window for show history for all organization changes.
             */
            /*$scope.openOrganizationEditHistoryModal = function (organizationId) {
                $rootScope.organizationId = organizationId;
                organizationService.getHistoryOrganizationWithId(
                    organizationId).then(
                    function (data) {
                        $rootScope.organization = data.content;
                        var organizationDTOModal = $modal
                            .open({
                                animation: true,
                                controller: 'OrganizationEditHistoryModalController',
                                templateUrl: '/resources/app/admin/views/modals/organization-edit-history-modal.html',
                                size: 'lg'
                            });
                    });

            };*/

        }]);