angular
    .module('employeeModule')
    .controller('VerificationPlanningTaskController', ['$scope', '$log',
        '$modal', 'VerificationPlanningTaskService',
        '$rootScope', 'ngTableParams', '$timeout', '$filter', '$window', '$location', '$translate',
        function ($scope, $log, $modal, verificationPlanningTaskService, $rootScope, ngTableParams, $timeout, $filter, $window, $location, $translate) {

            $scope.resultsCount = 0;
            $scope.verifications = [];

            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                filterDelay: 1500,
                getData: function ($defer, params) {
                    verificationPlanningTaskService.getVerificationsByCalibratorEmployeeAndTaskStatus(params.page(), params.count())
                            .then(function (result) {
                                $log.debug('result ', result);
                                $scope.resultsCount = result.data.totalItems;
                                $defer.resolve(result.data.content);
                                params.total(result.data.totalItems);
                            }, function (result) {
                                $log.debug('error fetching data:', result);
                            });
                }

            });

            $scope.idsOfVerifications = [];
            $scope.checkedItems = [];
            $scope.allIsEmpty = true;
            $scope.resolveVerificationId = function (id){
                var index = $scope.idsOfVerifications.indexOf(id);
                if (index === -1) {
                    $scope.idsOfVerifications.push(id);
                    index = $scope.idsOfVerifications.indexOf(id);
                }
                if (!$scope.checkedItems[index]) {
                    $scope.idsOfVerifications.splice(index, 1, id);
                    $scope.checkedItems.splice(index, 1, true);
                } else {
                    $scope.idsOfVerifications.splice(index, 1);
                    $scope.checkedItems.splice(index, 1);
                }
                $scope.allIsEmpty = false;
                console.log($scope.idsOfVerifications);
            }


            $scope.openTaskForStation = function(){
                $rootScope.verifIds = [];
                for (var i = 0; i < $scope.idsOfVerifications.length; i++) {
                    $rootScope.verifIds[i] = $scope.idsOfVerifications[i];
                }
                $rootScope.emptyStatus = $scope.allIsEmpty;
                $scope.$modalInstance  = $modal.open({
                    animation: true,
                    controller: 'TaskForStationModalControllerCalibrator',
                    templateUrl: '/resources/app/calibrator/views/modals/eddTaskForStationModal.html'
                });
                $scope.$modalInstance.result.then(function () {
                    $scope.tableParams.reload();
                });
            };

            $scope.openTaskForTeam = function(){
                $rootScope.verifIds = [];
                for (var i = 0; i < $scope.idsOfVerifications.length; i++) {
                    $rootScope.verifIds[i] = $scope.idsOfVerifications[i];
                }
                $rootScope.emptyStatus = $scope.allIsEmpty;
                $scope.$modalInstance  = $modal.open({
                    animation: true,
                    controller: 'TaskForTeamModalControllerCalibrator',
                    templateUrl: '/resources/app/calibrator/views/modals/eddTaskForTeamModal.html'
                });
                $scope.$modalInstance.result.then(function () {
                    $scope.tableParams.reload();
                });
            };

            $rootScope.verificationId = null;
            $scope.openCounterInfoModal = function(id){
                $rootScope.verificationId = id;
                $log.debug($rootScope.verificationId);
                $scope.$modalInstance  = $modal.open({
                    animation: true,
                    controller: 'CounterStatusControllerCalibrator',
                    templateUrl: '/resources/app/calibrator/views/modals/counterStatusModal.html'
                });
                $scope.$modalInstance.result.then(function () {
                    $scope.tableParams.reload();
                });
            };

        }]);


