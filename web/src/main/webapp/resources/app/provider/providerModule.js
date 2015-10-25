angular
    .module('providerModule', ['spring-security-csrf-token-interceptor',
        'ui.bootstrap', 'ui.router', 'ui.bootstrap.showErrors', 'ngTable', 'pascalprecht.translate', 'ngCookies'])

    .config(['$stateProvider', '$urlRouterProvider', 'showErrorsConfigProvider','$translateProvider',

        function ($stateProvider, $urlRouterProvider, showErrorsConfigProvider, $translateProvider) {

            showErrorsConfigProvider.showSuccess(true);


        	/**
			 * i18n configuration.
			 */
			$translateProvider.useStaticFilesLoader({
				prefix : '/resources/assets/i18n/welcome-',
				suffix : '.json'
			});
			$translateProvider.useLocalStorage();
			$translateProvider.useSanitizeValueStrategy('escaped');
			$translateProvider.preferredLanguage('ukr');
			
			$urlRouterProvider.otherwise('/');
            $stateProvider
                .state('main-panel', {
                    url: '/',
                    templateUrl: '/resources/app/provider/views/main-panel.html'
                })
                .state("new-verifications", {
                    url: '/verifications/new',
                    templateUrl: '/resources/app/provider/views/new-verifications.html',
                    controller: 'NewVerificationsController'
                })
                .state("employees", {
                    url: '/employees',
                    templateUrl: '/resources/app/provider/views/employee/main-panel.html',
                    controller: 'EmployeeController'
                })
                .state("employee-show", {
                    url: '/employee-show',
                    templateUrl: '/resources/app/provider/views/employee/show-employee.html',
                    controller: 'UsersController'
                })
                .state("verifications-archive", {
                    url: '/verifications/archive',
                    templateUrl: '/resources/app/provider/views/archival-verifications.html',
                    controller: 'ArchivalVerificationsController'
                })
                .state('settings', {
                    url: '/settings',
                    templateUrl: '/resources/app/provider/views/settings-panel.html'

                });

        }]);

angular.module('providerModule').run(function (paginationConfig) {
    paginationConfig.firstText = 'Перша';
    paginationConfig.previousText = 'Попередня';
    paginationConfig.nextText = 'Наступна';
    paginationConfig.lastText = 'Остання';
        });

define([
    'controllers/TopNavBarController',
    'controllers/MainPanelController',
    'controllers/ArchivalVerificationsController',
    'controllers/NewVerificationsController',
    'controllers/DetailsModalController',
    'controllers/SendingModalController',
    'controllers/EmployeeController',
    'controllers/AddressModalController',
    'controllers/UsersController',
    'controllers/SettingsController',
    'controllers/NotificationsController',
    'controllers/ProviderEmployeeController',
    'controllers/CapacityEmployeeController',
    'controllers/MailSendingModalController',

    'services/VerificationService',
    'services/AddressService',
    'services/SettingsService',
    'services/UserService'
], function () {}
    );
