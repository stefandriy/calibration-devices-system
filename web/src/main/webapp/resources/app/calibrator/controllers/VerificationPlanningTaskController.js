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
                                //params.total(result.totalItems);
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


            $scope.openTask = function(){
                $rootScope.verifIds = [];
                $rootScope.verifIds.push($scope.idsOfVerifications);
                $rootScope.emptyStatus = $scope.allIsEmpty;
                $scope.$modalInstance  = $modal.open({
                    animation: true,
                    controller: 'TaskSendingModalControllerCalibrator',
                    templateUrl: '/resources/app/calibrator/views/modals/eddTaskModal.html'
                });
            };

           // $scope.dawnloadFile = {
           //     url: null
           // }
           //$scope.createExcelFile = function (){
           //    verificationPlanningTaskService.createExcelFileForVerifications($scope.idsOfVerifications)
           //        .then(function (result) {
           //            $log.debug('result ', result);
           //            if (result.status == 200){
           //                $log.debug(result.data);
           //                $scope.dawnloadFile.url = "/"+ result.data;
           //                $("#downloadFile").attr("href", $scope.dawnloadFile.url);
           //                $log.debug($scope.dawnloadFile.url);
           //                $modal.open({
           //                    animation: true,
           //                    templateUrl: '/resources/app/calibrator/views/modals/create-excelfile-success.html',
           //                    controllerAs: 'successController',
           //                    size: 'md'
           //                });
           //            }
           //        }, function (result) {
           //            $log.debug('error fetching data:', result);
           //        });
           //}



        }]);


