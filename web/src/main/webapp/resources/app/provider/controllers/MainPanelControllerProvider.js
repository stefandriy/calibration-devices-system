angular
    .module('employeeModule')
    .controller('MainPanelControllerProvider', ['$scope', '$log','VerificationServiceProvider','ngTableParams','$modal', 'UserService', '$controller', '$filter',
        function ($scope, $log, verificationServiceProvider, ngTableParams, $modal, userService, $controller, $filter) {
        	$log.debug('inside main pan ctrl provider');

            /**
             * Role
             */

                var organizationTypeProvider = false;
                var organizationTypeCalibrator = false;
                var organizationTypeVerificator = false;
                var thereIsProvider = 0;
                var thereIsCalibrator = 0;
                var thereIsStateVerificator = 0;

                userService.isAdmin()
                    .success(function (response) {
                        var roles = response + '';
                        var role = roles.split(',');
                        for (var i = 0; i < role.length; i++) {
                            if (role[i] === 'PROVIDER_ADMIN' || role[i] === 'PROVIDER_EMPLOYEE')
                                thereIsProvider++;
                            if (role[i] === 'CALIBRATOR_ADMIN' || role[i] === 'CALIBRATOR_EMPLOYEE')
                                thereIsCalibrator++;
                            if (role[i] === 'STATE_VERIFICATOR_ADMIN' || role[i] === 'STATE_VERIFICATOR_EMPLOYEE')
                                thereIsStateVerificator++;
                            if (thereIsProvider > 0) {
                                $scope.providerViews = true;
                                provider();
                            }
                            if (thereIsCalibrator > 0)
                                $scope.calibratorViews = true;
                            if (thereIsStateVerificator > 0)
                                $scope.stateVerificatorViews = true;
                        }
                    });


                /**
                 * Graph of verifications
                 */
                var me = $scope;
                $controller('GraphicEmployeeProviderMainPanel', {
                    $scope: $scope
                });

                $scope.formattedDate = null;
                $scope.fcalendar = null;
                $scope.acalendar = null;
                var date1 = new Date(new Date().getFullYear(), 0, 1);
                var date2 = new Date();
                $scope.dataToSearch = {
                    fromDate: date1,
                    toDate: date2
                };


                $scope.cancel = function () {
                    $modal.dismiss();
                };

                $scope.showGrafic = function () {         	
	                    var dataToSearch = {
	                        fromDate: $scope.changeDateToSend($scope.dataToSearch.fromDate),
	                        toDate: $scope.changeDateToSend($scope.dataToSearch.toDate)
	                    };
	                    userService.getGraficDataMainPanel(dataToSearch)
	                        .success(function (data) {
	                            return me.displayGrafic(data);
	                        });
                };
                

                /**
                 *  Date picker and formatter setup
                 *
                 */
                $scope.toMaxDate = new Date();
                
                $scope.firstCalendar = {};
                $scope.firstCalendar.isOpen = false;
                $scope.secondCalendar = {};
                $scope.secondCalendar.isOpen = false;
                

                $scope.open1 = function ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.firstCalendar.isOpen = true;
                    $scope.secondCalendar.isOpen = true;
                };
                $scope.open2 = function ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.secondCalendar.isOpen = true;
                    $scope.firstCalendar.isOpen = true;
                };

                $scope.dateOptions = {
                    formatYear: 'yyyy',
                    startingDay: 1,
                    showWeeks: 'false'
                };

                $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
                $scope.format = $scope.formats[2];

                $scope.changeDateToSend = function (value) {
                	if ($scope.dataToSearch.toDate != null) {
                		$scope.fromMaxDate = $scope.dataToSearch.toDate;
                	} else {
                		$scope.fromMaxDate = new Date();
                	}
                	
                	$scope.toMinDate = $scope.dataToSearch.fromDate;
                	
                    if (angular.isUndefined(value)) {
                        return null;

                    } else {

                        return $filter('date')(value, 'dd-MM-yyyy');
                    }
                };
                var provider = function() {
                    $scope.showGrafic();
                    $scope.showGraficTwo();
                };

                /**
                 * Pie of sent and accepted
                 */
                var mo = $scope;
                $controller('PieProviderEmployee', {
                    $scope: $scope
                });


                $scope.showGraficTwo = function () {
                    userService.getPieDataMainPanel()
                        .success(function (data) {
                            return mo.displayGraficPipe(data);
                        });
                };


            $scope.checkIfNewVerificationsAvailable = function () {
                return $scope.resultsCount != 0;

            };
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
                                $scope.resultsCount = result.totalItems;
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
                    });
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
                                $scope.showGraficTwo();
                            });
                    });
                };

                /**
                 * Table of employee
                 */
                $scope.tableParamsEmployee = new ngTableParams({
                    page: 1,
                    count: 5,
                    sorting: {
                        lastName: 'asc'     // initial sorting
                    },
                }, {
                    total: 0,
                    getData: function ($defer, params) {
                        userService.getPage(params.page(), params.count(), params.filter(), params.sorting())
                            .success(function (result) {
                                $scope.totalEmployee = result.totalItems;
                                $defer.resolve(result.content);
                                params.total(result.totalItems);
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
       //     }
	}]);
