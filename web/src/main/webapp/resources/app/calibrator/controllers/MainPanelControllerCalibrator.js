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
                	if (dateValidate()){                	
	                    var dataToSearch = {
	                        fromDate: $scope.changeDateToSend($scope.dataToSearch.fromDate),
	                        toDate: $scope.changeDateToSend($scope.dataToSearch.toDate)
	                    };
	                    userServiceCalibrator.getGraficDataMainPanel(dataToSearch)
	                        .success(function (data) {
	                            return me.displayGrafic(data);
	                        });
                	}
                };
                
                var dateValidate = function () {
                	var dateRangeError = false;
                	
                	if ($scope.dataToSearch.fromDate > new Date()){
                		dateRangeError = true;
                		$scope.fromDateInTheFuture = true;
                	} else {
                		$scope.fromDateInTheFuture = false;
                	}
                	
                	if ($scope.dataToSearch.toDate > new Date()){
                		dateRangeError = true;
                		$scope.toDateInTheFuture = true;
                	} else {
                		$scope.toDateInTheFuture = false;
                	}
                	
                	if ($scope.dataToSearch.toDate < $scope.dataToSearch.fromDate && !$scope.fromDateInTheFuture){
                		dateRangeError = true;
                		$scope.toDateInvalid = true;
                	} else {
                		$scope.toDateInvalid = false;
                	}
                	
                	return !dateRangeError;
                	
                };

                /**
                 *  Date picker and formatter setup
                 *
                 */
                $scope.firstCalendar = {};
                $scope.firstCalendar.isOpen = false;
                $scope.secondCalendar = {};
                $scope.secondCalendar.isOpen = false;

                $scope.open1 = function ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.firstCalendar.isOpen = true;
                };
                $scope.open2 = function ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.secondCalendar.isOpen = true;
                };

                $scope.dateOptions = {
                    formatYear: 'yyyy',
                    startingDay: 1,
                    showWeeks: 'false'
                };

                $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
                $scope.format = $scope.formats[2];

                $scope.changeDateToSend = function (value) {

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

                $scope.addProviderEmployee = function (verifId, providerEmployee) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: '/resources/app/provider/views/modals/adding-providerEmployee.html',
                        controller: 'ProviderEmployeeControllerProvider',
                        size: 'md',
                        windowClass: 'xx-dialog',
                        resolve: {
                            providerEmploy: function () {
                                return verificationServiceCalibrator.getProviders()
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
                        verificationServiceCalibrator
                            .sendEmployeeProvider(dataToSend)
                            .success(function () {
                                $scope.tableParamsVerifications.reload();
                                $scope.tableParamsEmployee.reload();
                                $scope.showGraficTwo();
                            });
                    });
                };
    }]);