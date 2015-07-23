angular
    .module('employeeModule')
    .controller('NewVerificationsControllerVerificator', ['$scope', '$log', '$modal', 'VerificationServiceVerificator', '$rootScope',
        function ($scope, $log, $modal, VerificationServiceVerificator,  $rootScope) {

            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 10;
            $scope.pageData = [];

            $scope.onTableHandling = function () {
            	VerificationServiceVerificator
                    .getNewVerifications($scope.currentPage, $scope.itemsPerPage)
                    .success(function (verifications) {
                        $scope.pageData = verifications.content;
                        $scope.totalItems = verifications.totalItems;
                    });
            };

            $scope.onTableHandling();
            
            $scope.markAsRead = function (id) {
				 var dataToSend = {
							verificationId: id,
							readStatus: 'READ'
						};
				
				 VerificationServiceVerificator.markVerificationAsRead(dataToSend).success(function () {
		         	$rootScope.$broadcast('verification-was-read');
		         		$scope.onTableHandling();
		            });
	         };

            $scope.openDetails = function ( verifId, verifDate, verifReadStatus ) {
                $modal.open({
                    animation: true,
                    templateUrl: '/resources/app/verificator/views/modals/new-verification-details.html',
                    controller: 'DetailsModalControllerVerificator',
                    size: 'lg',
                    resolve: {
                        response: function () {
                        	   return VerificationServiceVerificator.getNewVerificationDetails(verifId)
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

            $scope.testReview = function(verifId){
            	$log.debug('VerifID');
                $log.debug(verifId);
                $modal.open({

            		animation: true,
            		templateUrl: '/resources/app/verificator/views/modals/testReview.html',
            		controller: 'CalibrationTestReviewControllerVerificator',
            		size: 'lg',
            		resolve: {
            			response: function () {
            				return VerificationServiceVerificator.getCalibraionTestDetails(verifId)
            				.success(function(calibrationTest){
            					//calibrationTest.id = verifId;
                                    $log.debug('CalibrationTest');
                                    $log.debug(calibrationTest);
            					return calibrationTest;
            				})
            				.error(function(){
            					console.log('ERROR');
            				});
            			}
            		}
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
                        templateUrl: '/resources/app/verificator/views/modals/verification-sending.html',
                        controller: 'SendingModalControllerVerificator',
                        size: 'md',
                        resolve: {
                            response: function () {
                                return VerificationServiceVerificator.getProviders()
                                    .success(function (providers) {
                                        return providers;
                                    });
                            }
                        }
                    });

                    //executes when modal closing
                    modalInstance.result.then(function (formData) {

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            idsOfProviders: formData.provider.id
                        };

                        $log.info(dataToSend);
                            VerificationServiceVerificator
                                .sendVerificationsToProvider(dataToSend)
                                .success(function () {
                                    $scope.onTableHandling();
                                });
      
                        $scope.idsOfVerifications = [];
                        $scope.checkedItems = [];
                    });
                } else {
                    $scope.isClicked = true;
                }
            };
            //For NOT_OK!!!
            $scope.openSendingModalNotOK = function () {
                if (!$scope.allIsEmpty) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: '/resources/app/verificator/views/modals/verification-sending.html',
                        controller: 'SendingModalControllerVerificator',
                        size: 'md',
                        resolve: {
                            response: function () {
                                return VerificationServiceVerificator.getProviders()
                                    .success(function (providers) {
                                        return providers;
                                    });
                            }
                        }
                    });

                    //executes when modal closing
                    modalInstance.result.then(function (formData) {

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            idsOfProviders: formData.provider.id
                        };

                        $log.info(dataToSend);
                            VerificationServiceVerificator
                                .sendVerificationNotOkStatus(dataToSend)
                                .success(function () {
                                    $scope.onTableHandling();
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
        }]);
