angular
    .module('employeeModule')
    .controller('VerificationPlanningTaskController', ['$scope', '$log',
        '$modal', 'VerificationPlanningTaskService',
        '$rootScope', 'ngTableParams', '$timeout', '$filter', '$window', '$location', '$translate', 'toaster',
        function ($scope, $log, $modal, verificationPlanningTaskService, $rootScope, ngTableParams,
                $timeout, $filter, $window, $location, $translate, toaster) {

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
                    $scope.idsOfVerifications = [];
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

            /**
             * adds selected verificationId to the array
             * or delete it if it when it is not selected
             * but it it is still in the array
             *
             * @param id
             */
            $scope.resolveVerificationId = function (id){
                var index = $scope.idsOfVerifications.indexOf(id);
                if (index > -1) {
                    $scope.idsOfVerifications.splice(index, 1);
                } else {
                    $scope.idsOfVerifications.push(id);
                }
            };

            /**
             * opens task for station modal
             * if task saved successfully reloads
             * table data
             */
            $scope.openTaskForStation = function() {
                if ($scope.idsOfVerifications.length === 0) {
                    toaster.pop('error', $filter('translate')('INFORMATION'),
                        $filter('translate')('NO_VERIFICATIONS_CHECKED'));
                } else {
                    $scope.$modalInstance = $modal.open({
                        animation: true,
                        controller: 'TaskForStationModalControllerCalibrator',
                        templateUrl: 'resources/app/calibrator/views/modals/addTaskForStationModal.html',
                        resolve: {
                            verificationIDs: function () {
                                return $scope.idsOfVerifications;
                            },
                            moduleType: function() {
                                return 'INSTALLATION_PORT';
                            }
                        }
                    });
                    $scope.$modalInstance.result.then(function () {
                        $scope.tableParams.reload();
                        toaster.pop('success', $filter('translate')('INFORMATION'),
                            $filter('translate')('TASK_FOR_STATION_CREATED'));
                    });
                }
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
                // $rootScope.emptyStatus = $scope.allIsEmpty;
                $scope.$modalInstance  = $modal.open({
                    animation: true,
                    controller: 'TaskForTeamModalControllerCalibrator',
                    templateUrl: 'resources/app/calibrator/views/modals/addTaskForTeamModal.html'
                });
                $scope.$modalInstance.result.then(function () {
                    $scope.tableParams.reload();
                    $scope.checkedItems = [];
                    $scope.idsOfVerifications = [];
                    // $scope.allIsEmpty = true;
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


