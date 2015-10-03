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
        function($rootScope, $scope, $modal, organizationService) {

            $scope.noChanges = true;

            if ($rootScope.organization[1]){
                $scope.noChanges = false;
            }
            console.log($scope.noChanges);
            console.log($rootScope.organization[1]);
        } ]);