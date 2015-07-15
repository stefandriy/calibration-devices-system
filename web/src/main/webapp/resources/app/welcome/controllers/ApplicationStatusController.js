angular
		.module('welcomeModule')
		.controller(
				'ApplicationStatusController',
				[
						'$scope',
						'$http',
						'$translate',
						'$state',
						'$log',
						'$modal',
						'$stateParams',
						'DataReceivingService',

						function($scope, $http, $translate, $state, $log,
								$modal, $stateParams, dataReceivingService) {

							$scope.isShownForm = true;

							$log.info($stateParams);

							$scope.code = $stateParams.clientCode;

							$scope.findCode = function() {
								dataReceivingService.getVerificationStatusById(
										$scope.code).success(function(status) {
									$log.debug(status);
									$scope.status = resolveStatus(status);

								});

								$scope.isShownForm = false;
							};
							$scope.findVerification = function() {
								dataReceivingService
										.getVerificationById($scope.code)
										.success(
												function(verification) {
													$log
															.debug('verif from func :'
																	+ verification.status);
													$scope.verification = verification;

													if ($scope.verification.status == ('SENT'
															|| 'ACCEPTED' || 'REJECTED')) {
														$scope.progress = '25';
													}
													if ($scope.verification.status == 'IN_PROGRESS') {
														$scope.progress = '50';
													}
													if ($scope.verification.status == ('SENT_TO_VERIFICATOR' || 'TEST_COMPLETED')) {
														$scope.progress = '75';
													}
													if ($scope.verification.status == ('TEST_OK' || 'TEST_NOK')) {
														$scope.progress = '100';
													}

												});

								$scope.isShownForm = false;
							};
							$scope.getClientForm = function() {
								$scope.findCode();
								$scope.findVerification();
							}

							$scope.closeAlert = function() {
								$state.go($state.current, {}, {
									reload : true
								});
							}

							$scope.openDetails = function() {
								$modal
										.open({
											animation : true,
											templateUrl : '/resources/app/welcome/views/modals/archival-verification-details.html',
											controller : 'DetailsController',
											size : 'lg',
											resolve : {
												response : function() {
													return dataReceivingService
															.getVerificationById(
																	$scope.code)
															.success(
																	function(
																			verification) {
																		return verification;
																	});
												}
											}
										});
							};
						} ]);

var resolveStatus = function(status) {

	switch (status) {
	case 'NOT_FOUND':
		return 'NOTFOUND_TRANSLATION';
	case 'SENT':
		return 'SENT_TRANSLATION';
	case 'IN_PROGRESS':
		return 'IN_PROGRESS_TRANSLATION';
	case 'SENT_TO_VERIFICATOR':
		return 'SENT_TO_VERIFICATOR_TRANSLATION';
	case 'TEST_OK':
		return 'TEST_OK_TRANSLATION';
	case 'ACCEPTED':
		return 'ACCEPTED_TRASLATION';
	case 'REJECTED':
		return 'REJECTED_TRASLATION';
	case 'TEST_COMPLETED':
		return 'TEST_COMPLETED_TRASLATION';
	case 'TEST_NOK':
		return 'TEST_NOK_TRASLATION';
	}
};
