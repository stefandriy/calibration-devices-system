angular
    .module('employeeModule')
    .controller('MainPanelControllerCalibrator', ['$scope', '$log','VerificationServiceCalibrator','ngTableParams','$modal', 'UserServiceCalibrator', '$controller', '$filter',
        function ($scope, $log, verificationServiceCalibrator, ngTableParams, $modal, userServiceCalibrator, $controller, $filter) {
    		$log.debug('inside main panel contr calibr');
    		
    		
    		/**
             * Role
             */

                var organizationTypeProvider = false;
                var organizationTypeCalibrator = false;
                var organizationTypeVerificator = false;
                var thereIsProvider = 0;
                var thereIsCalibrator = 0;
                var thereIsStateVerificator = 0;

                userServiceCalibrator.isAdmin()
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
                            if (thereIsProvider > 0)
                                $scope.providerViews = true;
                            if (thereIsCalibrator > 0) {
                            	$scope.calibratorViews = true;
                            	calibrator();
                            }                               
                            if (thereIsStateVerificator > 0)
                                $scope.stateVerificatorViews = true;
                        }
                    });


                /**
                 * Graph of verifications
                 */
                var me = $scope;
                $controller('GraphicEmployeeCalibratorMainPanel', {
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
	                    userServiceCalibrator.getGraficDataMainPanel(dataToSearch)
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
                var calibrator = function() {
                    $scope.showGrafic();
                    $scope.showGraficTwo();
                };
                
                
                /**
                 * Pie of sent and accepted
                 */
                var mo = $scope;
                $controller('PieCalibratorEmployee', {
                    $scope: $scope
                });


                $scope.showGraficTwo = function () {
                    userServiceCalibrator.getPieDataMainPanel()
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

                        verificationServiceCalibrator.getNewVerificationsForMainPanel(params.page(), params.count(), $scope.search)
                            .success(function (result) {
                                $scope.resultsCount = result.totalItems;
                                $defer.resolve(result.content);
                                params.total(result.totalItems);
                            }, function (result) {
                                $log.debug('error fetching data:', result);
                            });
                    }
                });

                $scope.addCalibratorEmployee = function (verifId, calibratorEmployee) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: '/resources/app/calibrator/views/employee/assigning-calibratorEmployee.html',
                        controller: 'CalibratorEmployeeControllerCalibrator',
                        size: 'md',
                        windowClass: 'xx-dialog',
                        resolve: {
                            calibratorEmploy: function () {
                                return verificationServiceCalibrator.getCalibrators()
                                    .success(function (calibrators) {
                                        return calibrators;
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
                            employeeCalibrator: formData.provider
                        };
                        $log.info(dataToSend);
                        verificationServiceCalibrator
                            .sendEmployeeCalibrator(dataToSend)
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
                        userServiceCalibrator.getPage(params.page(), params.count(), params.filter(), params.sorting())
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
                        templateUrl: '/resources/app/calibrator/views/employee/capacity-calibratorEmployee.html',
                        controller: 'CapacityEmployeeControllerCalibrator',
                        size: 'lg',
                        resolve: {

                            capacity: function () {
                                return userServiceCalibrator.getCapacityOfWork(username)
                                    .success(function (verifications) {
                                        return verifications;
                                    });
                            }
                        }
                    });
                };
    }]);