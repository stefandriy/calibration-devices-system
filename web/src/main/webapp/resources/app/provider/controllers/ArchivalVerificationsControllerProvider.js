angular
    .module('employeeModule')
    .controller('ArchivalVerificationsControllerProvider', ['$scope', '$modal', '$log', 'VerificationServiceProvider', 'ngTableParams', '$filter', '$rootScope',

        function ($scope, $modal, $log, verificationServiceProvider, ngTableParams, $filter, $rootScope) {

//            $scope.totalItems = 0;
//            $scope.currentPage = 1;
//            $scope.itemsPerPage = 10;
//            $scope.pageData = [];

//            $scope.onTableHandling = function () {
//                updatePage();
//            };

            $scope.search = {
            		idText:null,
            		formattedDate :null,
            		lastNameText:null,
            		streetText: null,
            		status: null
            }
            
            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            			}, {
                total: 0,
                getData: function ($defer, params) {

                    verificationServiceProvider.getArchiveVerifications(params.page(), params.count(), $scope.search)
                    				.success(function (result) {
                    					$defer.resolve(result.content);
                    					params.total(result.totalItems);
                    				}, function (result) {
                    					$log.debug('error fetching data:', result);
                    				});
                 }
            });
      
            
//            updatePage();
//
//            function updatePage() {
//                verificationServiceProvider
//                    .getArchivalVerifications($scope.currentPage, $scope.itemsPerPage)
//                    .success(function (verifications) {
//                        $scope.pageData = verifications.content;
//                        $scope.totalItems = verifications.totalItems;
//                    });
//            }

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
           
        }]);
