angular
    .module('employeeModule')
    .controller('VerificationPlanningTaskController', ['$scope', '$log',
        '$modal', 'VerificationPlanningTaskService',
        '$rootScope', 'ngTableParams', '$timeout', '$filter', '$window', '$location', '$translate',
        function ($scope, $log, $modal, verificationPlanningTaskService, $rootScope, ngTableParams) {

            $scope.resultsCount = 0;
            $scope.verifications = [];

            /**
             * fills the planning task table
             */
            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                filterDelay: 1500,
                getData: function ($defer, params) {
                    verificationPlanningTaskService.getVerificationsByCalibratorEmployeeAndTaskStatus(params.page(), params.count())
                            .then(function (result) {
                                $scope.resultsCount = result.data.totalItems;
                                $defer.resolve(result.data.content);
                                params.total(result.data.totalItems);
                            });
                }

            });

            $scope.idsOfVerifications = [];
            $scope.checkedItems = [];
            $scope.allIsEmpty = true;
            /**
             * adds selected verificationId to the array
             * or delete it if it when it is not selected
             * but it it is still in the array
             *
             * @param id
             */
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
            }

            /**
             * opens task for station modal
             * if task saved successfully reloads
             * table data
             */
            $scope.openTaskForStation = function(){
                $rootScope.verifIds = [];
                for (var i = 0; i < $scope.idsOfVerifications.length; i++) {
                    $rootScope.verifIds[i] = $scope.idsOfVerifications[i];
                }
                $rootScope.emptyStatus = $scope.allIsEmpty;
                $scope.$modalInstance  = $modal.open({
                    animation: true,
                    controller: 'TaskForStationModalControllerCalibrator',
                    templateUrl: 'resources/app/calibrator/views/modals/addTaskForStationModal.html'
                });
                $scope.$modalInstance.result.then(function () {
                    $scope.tableParams.reload();
                    $scope.checkedItems = [];
                    $scope.idsOfVerifications = [];
                    $scope.allIsEmpty = true;
                });
            };

            /**
             * opens task for team modal
             * if task saved successfully reloads
             * table data
             */
            $scope.openTaskForTeam = function(){
                $rootScope.verifIds = [];
                for (var i = 0; i < $scope.idsOfVerifications.length; i++) {
                    $rootScope.verifIds[i] = $scope.idsOfVerifications[i];
                }
                $rootScope.emptyStatus = $scope.allIsEmpty;
                $scope.$modalInstance  = $modal.open({
                    animation: true,
                    controller: 'TaskForTeamModalControllerCalibrator',
                    templateUrl: 'resources/app/calibrator/views/modals/eddTaskForTeamModal.html'
                });
                $scope.$modalInstance.result.then(function () {
                    $scope.tableParams.reload();
                    $scope.checkedItems = [];
                    $scope.idsOfVerifications = [];
                    $scope.allIsEmpty = true;
                });
            };

            /**
             * opens counter info modal
             * if task saved successfully reloads
             * table data
             */
            $rootScope.verificationId = null;
            $scope.openCounterInfoModal = function(id){
                $rootScope.verificationId = id;
                $log.debug($rootScope.verificationId);
                $scope.$modalInstance  = $modal.open({
                    animation: true,
                    controller: 'CounterStatusControllerCalibrator',
                    templateUrl: 'resources/app/calibrator/views/modals/counterStatusModal.html'
                });
                $scope.$modalInstance.result.then(function () {
                    $scope.tableParams.reload();
                });
            };

        }]);


