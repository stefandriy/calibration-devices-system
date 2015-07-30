angular
    .module('employeeModule')
    .controller('NewVerificationsControllerCalibrator', ['$scope', '$log',
        '$modal', 'VerificationServiceCalibrator',
        '$rootScope', 'ngTableParams','$timeout', '$filter',
        function ($scope, $log, $modal, verificationServiceCalibrator, $rootScope, ngTableParams, $timeout, $filter) {

    	 $scope.resultsCount= 0;
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

            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function ($defer, params) {

                    verificationServiceCalibrator.getNewVerifications(params.page(), params.count(), $scope.search)
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
                verificationServiceCalibrator.markVerificationAsRead(dataToSend).success(function () {
                    $rootScope.$broadcast('verification-was-read');
                    $scope.tableParams.reload();
                });
            };


            $scope.openDetails = function (verifId, verifDate, verifReadStatus) {
                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/new-verification-details.html',
                    controller: 'DetailsModalControllerCalibrator',
                    size: 'lg',
                    resolve: {
                        response: function () {
                            return verificationServiceCalibrator.getNewVerificationDetails(verifId)
                                .success(function (verification) {
                                    verification.id = verifId;
                                    verification.initialDate = verifDate;
                                    if (verifReadStatus == 'UNREAD') {
                                        $scope.markAsRead(verifId);
                                    }
                                    return verification;
                                });
                        }
                    }
                });
            };

            /**
             * Opens modal window for adding new calibration-test.
             */
            $scope.openAddCalibrationTestModal = function () {
                var addTestModal = $modal
                    .open({
                        animation: true,
                        controller: 'CalibrationTestAddModalControllerCalibrator',
                        templateUrl: '/resources/app/calibrator/views/modals/calibration-test-add-modal.html',
                    });
            };

            $scope.idsOfVerifications = [];
            $scope.checkedItems = [];
            $scope.allIsEmpty = true;

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


            $scope.openSendingModal = function () {
                if (!$scope.allIsEmpty) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: '/resources/app/calibrator/views/modals/verification-sending.html',
                        controller: 'SendingModalControllerCalibrator',
                        size: 'md',
                        resolve: {
                            response: function () {
                                return verificationServiceCalibrator.getVerificators()
                                    .success(function (verificators) {
                                        $log.debug(verificators);
                                        return verificators;
                                    });
                            }
                        }
                    });

                    //executes when modal closing
                    modalInstance.result.then(function (verificator) {

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            verificatorId: verificator.id
                        };

                        $log.debug(dataToSend);
                        $log.debug(verificator);

                        verificationServiceCalibrator
                            .sendVerificationsToCalibrator(dataToSend)
                            .success(function () {
                                $scope.tableParams.reload();
                                $rootScope.$broadcast('verification-sent-to-verifikator');
                            });
                        $scope.idsOfVerifications = [];
                        $scope.checkedItems = [];
                    });
                } else {
                    $scope.isClicked = true;
                }
            };

            var checkForEmpty = function () {
                $scope.allIsEmpty = $scope.idsOfVerifications.length === 0;
            };


            $scope.uploadBbiFile = function (idVerification) {

                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/upload-bbiFile.html',
                    controller: 'UploadBbiFileController',
                    size: 'lg',
                    resolve: {
                        verification: function () {
                            return idVerification;

                        }
                    }
                });
            };

                      $scope.cancelBbiFile=function (verification) {
                          var idVerification=verification;
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/calibrator/views/modals/cancel-bbiFile.html',
                    controller: 'CancelBbiProtocolCalibrator',
                    size: 'md',
                     resolve: {
                         verificationId: function () {
                            return verificationServiceCalibrator.cancelUploadFile(idVerification)
                                .success(function (bbiName) {
                                    return bbiName;
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
              };
              
              $scope.initiateVerification = function () {
             	  
       	        var modalInstance = $modal.open({
       	            animation: true,
       	            templateUrl: '/resources/app/provider/views/modals/initiate-verification.html',
       	            controller: 'AddingVerificationsControllerCalibrator',
       	            size: 'lg',

       	        });      
         	};

        }]);
