/**
 * Created by vova on 23.09.15.
 */
angular
    .module('adminModule')
    .controller(
    'OrganizationEditHistoryModalController',
    [
        '$rootScope',
        '$scope',
        '$modal',
        'OrganizationService',
        'ngTableParams',
        function($rootScope, $scope, $modal, organizationService,
                  ngTableParams) {

            //$scope.tableParams = new ngTableParams({
            //    page: 1,
            //    count: 10,
            //    sorting: {
            //        id: 'desc'
            //    }
            //}, {
            //    total: 0,
            //    filterDelay: 1500,
            //    getData: function ($defer, params) {
            //
            //        var sortCriteria = Object.keys(params.sorting())[0];
            //        var sortOrder = params.sorting()[sortCriteria];
            //    }
            //});

        } ]);