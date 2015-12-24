angular
    .module('employeeModule')
    .controller('NotStandardNotificationsControllerProvider', ['$scope', '$log', 'VerificationServiceProvider', '$interval', '$state', '$rootScope', '$timeout',
        function ($scope, $log, verificationServiceProvider, $interval, $state,  $rootScope, $timeout) {

            var promiseInterval;
            var promiseTimeOut;
            $scope.countOfUnreadVerifications = 0;

            $scope.initializeCounter = function () {
                verificationServiceProvider.getCountOfNewNotStandardVerifications().success(function (count) {
                    $scope.countOfUnreadVerifications = count;
                });
            }

            $scope.initializeCounter();

            $scope.reloadVerifications = function() {
                $rootScope.$broadcast('refresh-table');
            }

            $scope.startPolling = function(){
                $scope.stopPolling();
                if(angular.isDefined(promiseInterval)) return;
                promiseInterval = $interval(function () {
                    verificationServiceProvider.getCountOfNewNotStandardVerifications().success(function (count) {
                        $scope.countOfUnreadVerifications = count;
                    })
                }, 10000);
            }

            $scope.stopPolling = function() {
                $interval.cancel(promiseInterval);
            };

            $scope.startPolling();

            $rootScope.$on('verification-sent-to-calibrator', function(){
                verificationServiceProvider.getCountOfNewNotStandardVerifications().success(function (count) {
                    $scope.countOfUnreadVerifications = count;
                });
            });

            $rootScope.$on('verification-was-read', function(){
                verificationServiceProvider.getCountOfNewNotStandardVerifications().success(function (count) {
                    $scope.countOfUnreadVerifications = count;
                });
            });

            $scope.$on('$destroy', function () {
                $scope.stopPolling();
            });

        }]);