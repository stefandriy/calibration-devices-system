angular
    .module('employeeModule')
    .controller('MainPanelControllerProvider', ['$scope', '$log','VerificationServiceProvider','ngTableParams','$modal', 'UserService',
        function ($scope, $log, verificationServiceProvider, ngTableParams, $modal, userService) {
        	$log.debug('inside main pan ctrl provider');
            /**
             * Table of unread verifications
             */
            $scope.tableParamsVerifications = new ngTableParams({
                page: 1,
                count: 5
            }, {
                total: 0,
                getData: function ($defer, params) {

                    verificationServiceProvider.getNewVerificationsForMainPanel(params.page(), params.count(), $scope.search)
                        .success(function (result) {
                            $scope.resultsCount=result.totalItems;
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                }
            });

            $scope.addProviderEmployee = function (verifId, providerEmployee) {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/modals/adding-providerEmployee.html',
                    controller: 'ProviderEmployeeControllerProvider',
                    size: 'md',
                    windowClass: 'xx-dialog',
                    resolve: {
                        providerEmploy: function () {
                            return verificationServiceProvider.getProviders()
                                .success(function (providers) {
                                    return providers;
                                }
                            );
                        }
                    }
                })
                /**
                 * executes when modal closing
                 */
                modalInstance.result.then(function (formData) {
                    idVerification = 0;
                    var dataToSend = {
                        idVerification: verifId,
                        employeeProvider: formData.provider
                    };
                    $log.info(dataToSend);
                    verificationServiceProvider
                        .sendEmployeeProvider(dataToSend)
                        .success(function () {
                            $scope.tableParamsVerifications.reload();
                            $scope.tableParamsEmployee.reload();
                        });
                });
            }

            /**
             * Table of employee
             */
            $scope.tableParamsEmployee = new ngTableParams({
                page: 1,
                count: 5
            }, {
                total: 0,
                getData: function ($defer, params) {
                    userService.getPage(params.page(), params.count(), params.filter())
                        .success(function (result) {
                            $scope.totalEmployee=result.totalItems;
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                            $scope.cantAddNewEmployee();
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                }
            });

            $scope.showCapacity = function (username) {

                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/employee/capacity-providerEmployee.html',
                    controller: 'CapacityEmployeeControllerProvider',
                    size: 'lg',
                    resolve: {

                        capacity: function () {
                            return userService.getCapacityOfWork(username)
                                .success(function (verifications) {
                                    return verifications;
                                });
                        }
                    }
                });
            };
	}]);
