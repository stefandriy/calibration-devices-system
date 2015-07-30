angular
    .module('employeeModule')
    .controller('NewVerificationsControllerProvider', ['$scope', '$log',
        '$modal', 'VerificationServiceProvider', '$rootScope', 'ngTableParams', '$filter', '$timeout',
        function ($scope, $log, $modal, verificationServiceProvider,
        		$rootScope, ngTableParams, $filter, $timeout) {
    	$scope.resultsCount = 0;
    	
    	
    	$scope.search = {
        		idText:null,
        		formattedDate :null,
        		lastNameText:null,
        		streetText: null,
        		status: null
        }
      
       
        $scope.clearAll = function(){
        	$scope.search.idText=null;
        	$scope.search.formattedDate=null;
        	$scope.dt = null;
        	$scope.search.lastNameText=null;
        	$scope.search.streetText=null;
        	$scope.search.status = null;
        	$scope.tableParams.reload();
        }
        
        $scope.clearId = function () {
        	$scope.search.idText = null;
        	$scope.tableParams.reload();
        }
        $scope.clearLastName = function () {
        	$scope.search.lastNameText = null;
        	$scope.tableParams.reload();
        }
        $scope.clearStreet = function () {
        	$scope.search.streetText = null;
        	$scope.tableParams.reload();
        }
        $scope.clearStatus = function () {
        	$scope.search.status = null;
        	$scope.tableParams.reload();
        }
      
        var promiseSearchTimeOut;
        $scope.doSearch = function() {       	
        	promiseTimeOut = $timeout(function() {
        		$scope.tableParams.reload();
        	}, 1500);
        }
        
        $scope.$on('refresh-table', function () {
        	 $scope.clearAll();
        }); 
       
        $scope.tableParams = new ngTableParams({
            page: 1,
            count: 10
        			}, {
            total: 0,
            getData: function ($defer, params) {

                verificationServiceProvider.getNewVerifications(params.page(), params.count(), $scope.search)
                				.success(function (result) {
                					 $scope.resultsCount=result.totalItems;
                					$defer.resolve(result.content);
                					params.total(result.totalItems);
                				}, function (result) {
                					$log.debug('error fetching data:', result);
                				});
             }
        });
  
	       $scope.markAsRead = function (id) {
				 var dataToSend = {
							verificationId: id,
							readStatus: 'READ'
						};
				
		         	verificationServiceProvider.markVerificationAsRead(dataToSend).success(function () {
		         		$rootScope.$broadcast('verification-was-read');
	                    $scope.tableParams.reload();
		            });
	         };


            /**
             * open modal
             */
            $scope.openDetails = function (verifId, verifDate, verifReadStatus, verifStatus) {

                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/provider/views/modals/new-verification-details.html',
                    controller: 'DetailsModalControllerProvider',
                    size: 'lg',
                    resolve: {
                        response: function () {
                            return verificationServiceProvider.getNewVerificationDetails(verifId)
                                .success(function (verification) {
                                	 $rootScope.verificationID = verifId;
                                    verification.id =   $rootScope.verificationID;
                                    verification.initialDate = verifDate;
                                    verification.status = verifStatus;
                                    if (verifReadStatus == 'UNREAD') {
                                        $scope.markAsRead(verifId);
                                    }
                                    return verification;
                                });
                        }
                    }
                });
            };

            $scope.removeProviderEmployee = function (verifId) {
                var dataToSend = {
                    idVerification: verifId
                };
                $log.info(dataToSend);
                verificationServiceProvider.cleanProviderEmployeeField(dataToSend)
                    .success(function () {
                        $scope.tableParams.reload();
                    });
            };

$scope.addProviderEmployee = function (verifId, providerEmployee) {
    var modalInstance = $modal.open({
        animation: true,
        templateUrl: '/resources/app/provider/views/modals/adding-providerEmployee.html',
        controller: 'ProviderEmployeeControllerProvider',
        size: 'md',
        windowClass: "providerEmployee",
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
                $scope.tableParams.reload();
            });
    });
};

$scope.idsOfVerifications = [];
$scope.checkedItems = [];
$scope.allIsEmpty = true;
$scope.idsOfCalibrators = null;



/**
 * push verification id to array
 */
$scope.resolveVerificationId = function (id) {
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
    checkForEmpty();
};

/**
 * open modal
 */
$scope.openSendingModal = function () {
    if (!$scope.allIsEmpty) {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: '/resources/app/provider/views/modals/verification-sending.html',
            controller: 'SendingModalControllerProvider',
            size: 'md',
            resolve: {
                response: function () {
                    return verificationServiceProvider.getCalibrators()
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

            var dataToSend = {
                idsOfVerifications: $scope.idsOfVerifications,
                idsOfCalibrators: formData.calibrator.id
            };

            

            verificationServiceProvider
                .sendVerificationsToCalibrator(dataToSend)
                .success(function () {
                	$log.debug('success sending');
                    $scope.tableParams.reload();
                    $rootScope.$broadcast('verification-sent-to-calibrator');
                });
            $scope.idsOfVerifications = [];
            $scope.checkedItems = [];

        });
    } else {
        $scope.isClicked = true;
    }
};

/**
 * check if idsOfVerifications array is empty
 */
var checkForEmpty = function () {
    $scope.allIsEmpty = $scope.idsOfVerifications.length === 0;
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

           $scope.changeDateToSend = function(val){
            	
            	  if(angular.isUndefined(val)){
            		  $scope.search.formattedDate = null;
            		  $scope.tableParams.reload();
            	  } else {
            		  var datefilter = $filter('date');
                	  $scope.search.formattedDate = datefilter(val, 'dd-MM-yyyy');
                	  $scope.tableParams.reload();
            	  }
              }
           
           /**
            * Modal window used to explain the reason of verification rejection
            */
           $scope.openMailModal = function (ID) {
        	   $log.debug('ID');
        	   $log.debug(ID);
        	        var modalInstance = $modal.open({
        	            animation: true,
        	            templateUrl: '/resources/app/provider/views/modals/mailComment.html',
        	            controller: 'MailSendingModalControllerProvider',
        	            size: 'md',

        	        });

        	        /**
        	         * executes when modal closing
        	         */
        	        modalInstance.result.then(function (formData) {

        	            var messageToSend = {
        	         		   verifID : ID,
        	         		   msg : formData.message
        	         	   };
        	 
        	            var dataToSend = {
        	            		verificationId: ID,
        	            		status: 'REJECTED'
        	            };
        	            verificationServiceProvider.rejectVerification(dataToSend).success(function () {
        	            		$rootScope.$broadcast('refresh-table');
        	            		verificationServiceProvider.sendMail (messageToSend)
         	            		.success(function (responseVal) {});
        	         	   });
        	        });
          	};

          	$scope.$on('verification_rejected', function(event, args) {
          		$log.debug(args.verifID);
          		 $scope.openMailModal(args.verifID);
          	});
          	
            $scope.initiateVerification = function () {
         	  
         	        var modalInstance = $modal.open({
         	            animation: true,
         	            templateUrl: '/resources/app/provider/views/modals/initiate-verification.html',
         	            controller: 'AddingVerificationsControllerProvider',
         	            size: 'lg',

         	        });      
           	};

        }]);

