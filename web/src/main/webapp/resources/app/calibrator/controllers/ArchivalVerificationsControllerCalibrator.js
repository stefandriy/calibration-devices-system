angular
    .module('employeeModule')
    .controller('ArchivalVerificationsControllerCalibrator', ['$scope', '$modal', '$log', 'VerificationServiceCalibrator', 'ngTableParams', '$filter', '$rootScope', '$timeout',

        function ($scope, $modal, $log, verificationServiceCalibrator, ngTableParams, $filter, $rootScope, $timeout) {

    	 $scope.resultsCount = 0;    
    	
    	$scope.tableParams = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {
                    date: 'desc'     
                }
            			}, {
                total: 0,
                filterDelay: 1500,
                getData: function ($defer, params) {

                	var sortCriteria = Object.keys(params.sorting())[0];
                	var sortOrder = params.sorting()[sortCriteria];
                	
                	verificationServiceCalibrator.getArchiveVerifications(params.page(), params.count(), params.filter(), sortCriteria, sortOrder).success(function (result) {
                    					 $scope.resultsCount=result.totalItems;
                    					$defer.resolve(result.content);
                    					params.total(result.totalItems);
                    				}, function (result) {
                    					$log.debug('error fetching data:', result);
                    });
                }
            });
            
            $scope.checkFilters = function () {       	 
                var obj = $scope.tableParams.filter();
                for (var i in obj) {
                    if (obj.hasOwnProperty(i) && obj[i]) {
                        return true;
                    }
                }
                return false;         
            };
            
            $scope.openDetails = function (verifId, verifDate) {

                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/modals/archival-verification-details.html',
                    controller: 'ArchivalDetailsModalController',
                    size: 'lg',
                    resolve: {
                        response: function () {
                            return verificationServiceCalibrator.getArchivalVerificationDetails(verifId)
                                .success(function (verification) {
                                    verification.id = verifId;
                                    verification.initialDate = verifDate;                                
                                    return verification;
                                });
                        }
                    }
                });
            };
            
            /**
             *  Date picker and formatter setup
             * 
             */
            $scope.openState = {};
            $scope.openState.isOpen = false;

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openState.isOpen = true;
            };


            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'
              };

           $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
           $scope.format = $scope.formats[2];

        }]);
