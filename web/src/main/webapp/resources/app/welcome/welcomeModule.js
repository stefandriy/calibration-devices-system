(function() {
	angular
			.module(
					'welcomeModule',
					[ 'spring-security-csrf-token-interceptor', 'ui.bootstrap',
							'pascalprecht.translate', 'ngCookies', 'ui.router',
							'ui.bootstrap.showErrors', 'ngTable',
							'localytics.directives',
							'frapontillo.bootstrap-switch', 'luegg.directives',
							'irontec.simpleChat' ])

			.config(
					[
							'$translateProvider',
							'$stateProvider',
							'$urlRouterProvider',
							'showErrorsConfigProvider',

							function($translateProvider, $stateProvider,
									$urlRouterProvider,
									showErrorsConfigProvider) {

								showErrorsConfigProvider.showSuccess(true);

								/**
								 * i18n configuration.
								 */
								$translateProvider.useStaticFilesLoader({
									prefix : '/resources/assets/i18n/welcome-',
									suffix : '.json'
								});
								$translateProvider.useLocalStorage();
								$translateProvider
										.useSanitizeValueStrategy('escaped');
								$translateProvider.preferredLanguage('ukr');
								/**
								 * Routing configuration.
								 */
								$urlRouterProvider.otherwise('/start');

								$stateProvider
										.state(
												'start',
												{
													url : '/start',
													templateUrl : '/resources/app/welcome/views/start.html',
													controller: 'WelcomePageController'

												})
										.state(
												'about',
												{
													url : '/about',
													templateUrl : '/resources/app/welcome/views/about.html'
												})
										.state(
												'login',
												{
													url : '/login',
													templateUrl : '/resources/app/welcome/views/login.html',
													controller : 'LoginController'
												})
										.state(
												'application-sending',
												{
													url : '/application-sending/{verificationId}',
													templateUrl : '/resources/app/welcome/views/application-sending.html',
													controller : 'ApplicationSendingController',
													resolve:{
												         verificationData:  function($stateParams, DataReceivingService){
												            return DataReceivingService.getVerificationById($stateParams.verificationId);
												         }
												      },
												})
										.state(
												'application-status',
												{
													url : '/application-status/{clientCode}',
													templateUrl : '/resources/app/welcome/views/application-status.html',
													controller : 'ApplicationStatusController'
												});
							} ]);

	define([ 'controllers/LoginController',
			'controllers/ApplicationSendingController',
			'controllers/ApplicationStatusController',
			'controllers/InternationalizationController',
			'controllers/NavigationController',
			'controllers/WelcomePageController',
			'controllers/DetailsController', 'controllers/FeedbackController',
			'controllers/ChatController', 'services/DataReceivingService',
			'services/DataSendingService', 'directives/OnStartupMessage',
			'directives/unique'],
			function() {
			});
})();
