/**
 * Created by Sonka on 23.11.2015.
 */
angular
    .module('adminModule')
    .controller(
    'UnsuitabilityReasonController',
    [
        '$rootScope',
        '$scope',
        '$modal',
        '$http',
        'UnsuitabilityReasonService',
        'ngTableParams',
        '$timeout',
        '$filter',
        'toaster',
        function ($rootScope, $scope, $modal, $http, unsuitabilityReasonService, ngTableParams, $timeout, $filter, toaster) {
            /**
             * init of page params
             */
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];

            /**
             * Clear filtering fields
             */
            $scope.clearAll = function () {
                $scope.tableParams.filter({});
            };
            $scope.doSearch = function () {
                $scope.tableParams.reload();
            }
            /**
             * Sorting and filtering of table
             * @type {ngTableParams|*}
             */
            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {
                    id: 'desc'
                }
            }, {
                total: 0,
                filterDelay: 10000,
                getData: function ($defer, params) {

                    var sortCriteria = Object.keys(params.sorting())[0];
                    var sortOrder = params.sorting()[sortCriteria];

                    unsuitabilityReasonService.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
                        .success(function (result) {
                            $scope.resultsCount = result.totalItems;
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                }
            });
            /**
             * Updates the table with counter type params.
             */
            $rootScope.onTableHandling = function () {
                $scope.tableParams.reload();
            };
            /**
             * initializing table params
             */
            $rootScope.onTableHandling();

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
             * Opens modal window for adding new counter type.
             */
            $scope.openAddUnsuitabilityReasonModal = function() {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/admin/views/modals/unsuitability-reason-add-modal.html',
                    controller: 'UnsuitabilityReasonAddModalController',
                    size: 'md',
                    /*resolve: {
                    /*    counters: function () {
                            console.log(unsuitabilityReasonService.getCounterTypes());
                            return unsuitabilityReasonService.getCounterTypes();
                        }
                    }*/
                });
                /**
                 * executes when modal closing
                 */
                modalInstance.result.then(function () {
                    var dataToAdd = {
                        reasonName: $scope.reasonName,
                        counterId: formData.counters.id
                    };
                    unsuitabilityReasonService.saveUnsuitabilityReason(dataToAdd)
                        .success(function () {
                            $log.debug('success sending');
                            $scope.tableParams.reload();
                            $rootScope.$broadcast('verification-sent-to-verificator');

                    toaster.pop('success',$filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_ADDED_NEW_REASON'));
                        });
                });
            };

            /**
             * Remove unsuitability reason by id
             * @param id
             */
         $scope.deleteUnsuitabilityReason = function (id) {
                $rootScope.unsuitabilityReasonId = id;
                console.log($rootScope.unsuitabilityReasonId);
                unsuitabilityReasonService.deleteUnsuitabilityReason(id).then(function (status) {
                    if (status == 409){
                        toaster.pop('info', $filter('translate')('INFORMATION'), $filter('translate')('ERROR_DELETED_REASON'));
                    } else {
                        toaster.pop('info', $filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_DELETED_REASON'));
                    }
                });
                $timeout(function() {
                    console.log('delete with timeout');
                    $rootScope.onTableHandling();
                }, 700);
            };

}]);