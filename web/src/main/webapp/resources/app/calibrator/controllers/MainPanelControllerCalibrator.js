angular
    .module('employeeModule')
    .controller('MainPanelControllerCalibrator', ['$scope', '$log','VerificationServiceProvider','ngTableParams','$modal', 'UserServiceCalibrator', '$controller', '$filter',
        function ($scope, $log, verificationServiceProvider, ngTableParams, $modal, userServiceCalibrator, $controller, $filter) {
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
    }]);