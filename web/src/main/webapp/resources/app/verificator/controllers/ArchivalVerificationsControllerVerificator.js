angular
    .module('employeeModule')
    .controller('ArchivalVerificationsControllerVerificator', ['$scope', '$modal', '$log', 'VerificationServiceVerificator', 'ngTableParams', '$filter', '$rootScope', '$timeout', '$translate',

        function ($scope, $modal, $log, verificationServiceVerificator, ngTableParams, $filter, $rootScope, $timeout, $translate) {
    	
    	 $scope.resultsCount = 0;

    	 $scope.clearAll = function() {
      		$scope.selectedStatus.name=null;
      		$scope.tableParams.filter({});   		
      	}

      	$scope.doSearch = function() {
      		$scope.tableParams.reload();
      	}
      	
      	$scope.selectedStatus = {
      		name : null
      	}
  	
      	$scope.statusData = [ { id : 'SENT', label : null }, 
      	                      { id : 'ACCEPTED', label : null },
      	                      { id : 'REJECTED', label : null }, 
      	                      { id : 'IN_PROGRESS', label : null },
      	                      { id : 'TEST_PLACE_DETERMINED', label : null },
      	                      { id : 'SENT_TO_TEST_DEVICE', label : null },
      	                      { id : 'TEST_COMPLETED', label : null }, 
      	                      { id : 'SENT_TO_VERIFICATOR', label : null },
      	                      { id : 'TEST_OK', label : null }, 
      	                      { id : 'TEST_NOK', label : null }    	   				
      	];

      	   			$scope.setTypeDataLanguage = function () {
      	   				var lang = $translate.use();
      	   				if (lang === 'ukr') {
      	   					$scope.statusData[0].label = 'Надійшла';
      	   					$scope.statusData[1].label = 'Прийнята';
      	   					$scope.statusData[2].label = 'Відхилена';
      	   					$scope.statusData[3].label = 'В роботі';
      	   					$scope.statusData[4].label = 'Визначено спосіб повірки';
      	   					$scope.statusData[5].label = 'Відправлено на установку';
      	   					$scope.statusData[6].label = 'Проведено вимірювання';
      	   					$scope.statusData[7].label = 'Предявлено повірнику';
      	   					$scope.statusData[8].label = 'Перевірено придатний';
      	   					$scope.statusData[9].label = 'Перевірено непридатний';
      	   					
      	   				} else if (lang === 'eng') {
      	   					$scope.statusData[0].label = 'Sent';
      	   					$scope.statusData[1].label = 'Accepted';
      	   					$scope.statusData[2].label = 'Rejected';
      	   					$scope.statusData[3].label = 'In progress';
      	   					$scope.statusData[4].label = 'Test place determined';
      	   					$scope.statusData[5].label = 'Sent to test device';
      	   					$scope.statusData[6].label = 'Test completed';
      	   					$scope.statusData[7].label = 'Sent to verificator';
      	   					$scope.statusData[8].label = 'Tested OK';
      	   					$scope.statusData[9].label = 'Tested NOK';
      	   					
      	   				}
      	   			};

      	   			$scope.setTypeDataLanguage();
    	 
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
                	
                	if($scope.selectedStatus.name != null) {
                		params.filter().status = $scope.selectedStatus.name.id;
                	}
                	
                	verificationServiceVerificator.getArchiveVerifications(params.page(), params.count(), params.filter(), sortCriteria, sortOrder).success(function (result) {
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
                            return verificationServiceVerificator.getArchivalVerificationDetails(verifId)
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
