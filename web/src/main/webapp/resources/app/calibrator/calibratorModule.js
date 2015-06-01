(function () {
    angular.module('calibratorModule', ['spring-security-csrf-token-interceptor',
        'ui.bootstrap', 'ui.router', 'ui.bootstrap.showErrors'])

        .config(['$stateProvider', '$urlRouterProvider', 'showErrorsConfigProvider',

            function ($stateProvider, $urlRouterProvider, showErrorsConfigProvider) {

                showErrorsConfigProvider.showSuccess(true);

                $urlRouterProvider.otherwise('/');

                $stateProvider
                    .state('main-panel', {
                        url: '/',
                        templateUrl: '/resources/app/calibrator/views/main-panel.html'
                    })
                    .state("new-verifications", {
                        url: '/verifications/new',
                        templateUrl: '/resources/app/calibrator/views/new-verifications.html',
                        controller: 'NewVerificationsController'
                    })

                    .state("verifications-archive", {
                        url: '/verifications/archive',
                        templateUrl: '/resources/app/calibrator/views/archival-verifications.html',
                        controller: 'ArchivalVerificationsController'
                    });

            }]);

    angular.module('calibratorModule').run(function (paginationConfig) {
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
        'controllers/AddingVerificationsController',
        'controllers/DetailsModalController',
        'controllers/SendingModalController',
        'services/VerificationService'
    ], function () {});
})();
