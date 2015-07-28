(function () {
    angular.module('employeeModule', ['spring-security-csrf-token-interceptor',
        'ui.bootstrap', 'ui.router', 'ui.bootstrap.showErrors', 'ngTable', 'pascalprecht.translate', 'ngCookies', 'localytics.directives', 'highcharts-ng', 'ngFileUpload'])

        .config(['$translateProvider', '$stateProvider', '$urlRouterProvider', 'showErrorsConfigProvider',

            function ($translateProvider, $stateProvider, $urlRouterProvider, showErrorsConfigProvider) {

                showErrorsConfigProvider.showSuccess(true);
               
                /**
                 *  i18n configuration.
                 */
                $translateProvider.useStaticFilesLoader({
                    prefix: '/resources/assets/i18n/welcome-',
                    suffix: '.json'
                });
                $translateProvider.useLocalStorage();
                $translateProvider.useSanitizeValueStrategy('escaped');
                $translateProvider.preferredLanguage('ukr');
                /**
                 * Routing configuration.
                 */

                $urlRouterProvider.otherwise('/');

                $stateProvider
                .state('main-panel-provider', {
                    url: '/',
                    templateUrl: '/resources/app/provider/views/main-panel.html',
                    controller: 'MainPanelControllerProvider'
                })
                .state("new-verifications-provider", {
                    url: '/provider/verifications/new',
                    templateUrl: '/resources/app/provider/views/new-verifications.html',
                    controller: 'NewVerificationsControllerProvider'
                })
                .state("adding-verifications-provider", {
                    url: '/provider/verifications/add',
                    templateUrl: '/resources/app/provider/views/adding-verifications.html',
                    controller: 'AddingVerificationsControllerProvider'
                })
                .state("employee-show-provider", {
                    url: '/provider/employee-show',
                    templateUrl: '/resources/app/provider/views/employee/show-employee.html',
                    controller: 'UsersController'
                })
                .state("verifications-archive-provider", {
                    url: '/provider/verifications/archive',
                    templateUrl: '/resources/app/provider/views/archival-verifications.html',
                    controller: 'ArchivalVerificationsControllerProvider'
                })
                .state("settings-provider", {
                    url: '/provider/verificator/settings',
                    templateUrl: '/resources/app/provider/views/settings-panel.html'

                })
                    .state("statistic-show-providerEmployee", {
                        url: '/provider/statistic/employee',
                        templateUrl: '/resources/app/provider/views/employee/calendar-providerEmployee.html',
                        controller: 'CalendarEmployeeProvider'
                    })
                	.state('main-panel-calibrator', {
                        url: '/calibrator/',
                        templateUrl: '/resources/app/calibrator/views/main-panel.html'
                    })
                    .state("new-verifications-calibrator", {
                        url: '/calibrator/verifications/new',
                        templateUrl: '/resources/app/calibrator/views/new-verifications.html',
                        controller: 'NewVerificationsControllerCalibrator'
                    })
                   
                    .state("calibration-test-calibrator", {
                        url: '/calibrator/verifications/calibration-test',
                        templateUrl: '/resources/app/calibrator/views/calibration-test-add-modal.html',
                        controller: 'CalibrationTestAddModalControllerCalibrator'
                    })
                    .state("verifications-archive-calibrator", {
                        url: '/calibrator/verifications/archive',
                        templateUrl: '/resources/app/calibrator/views/archival-verifications.html',
                        controller: 'ArchivalVerificationsControllerCalibrator'
                    })
                      .state("measuring-equipment-calibrator", {
                        url: '/calibrator/mEquipment/',
                        templateUrl: '/resources/app/calibrator/views/measurement-equipments.html',
                        controller: 'MeasuringEquipmentControllerCalibrator'
                    })
                    
                    
                    .state('main-panel-verificator', {
                        url: '/',
                        templateUrl: '/resources/app/verificator/views/main-panel.html'
                    })
                    .state("new-verifications-verificator", {
                        url: '/verifications/new',
                        templateUrl: '/resources/app/verificator/views/new-verifications.html',
                        controller: 'NewVerificationsControllerVerificator'
                    })
                    .state("verifications-archive-verificator", {
                        url: '/verifications/archive',
                        templateUrl: '/resources/app/verificator/views/archival-verifications.html',
                        controller: 'ArchivalVerificationsControllerProvider'
                    });


            }]);

    angular.module('employeeModule').run(function (paginationConfig) {
        paginationConfig.firstText = 'Перша';
        paginationConfig.previousText = 'Попередня';
        paginationConfig.nextText = 'Наступна';
        paginationConfig.lastText = 'Остання';
    });

    define([
            'provider/controllers/InternationalizationController',
            'provider/controllers/TopNavBarControllerProvider',
            'provider/controllers/MainPanelControllerProvider',
            'provider/controllers/ArchivalVerificationsControllerProvider',
            'provider/controllers/NewVerificationsControllerProvider',
            'provider/controllers/AddingVerificationsControllerProvider',
            'provider/controllers/DetailsModalControllerProvider',
            'provider/controllers/SendingModalControllerProvider',
            'provider/controllers/AddEmployeeController',
            'provider/controllers/AddressModalControllerProvider',
            'provider/controllers/UsersController',
            'provider/controllers/SettingsControllerProvider',
            'provider/controllers/NotificationsControllerProvider',
            'provider/controllers/ProviderEmployeeControllerProvider',
            'provider/controllers/MailSendingModalControllerProvider',
            'provider/services/VerificationServiceProvider',
            'provider/services/AddressServiceProvider',
            'provider/services/SettingsServiceProvider',
            'provider/services/UserService',
            'provider/controllers/CapacityEmployeeControllerProvider',
            'provider/controllers/GraficEmployeeProvider',
            'provider/controllers/CalendarEmployeeProvider',
            'provider/controllers/ArchivalDetailsModalController',

            'calibrator/controllers/TopNavBarControllerCalibrator',
            'calibrator/controllers/MainPanelControllerCalibrator',
            'calibrator/controllers/NewVerificationsControllerCalibrator',
            'calibrator/controllers/DetailsModalControllerCalibrator',
            'calibrator/controllers/SendingModalControllerCalibrator',
            'calibrator/controllers/CalibrationTestAddModalControllerCalibrator',
            'calibrator/controllers/EmployeeControllerCalibrator',
            'calibrator/controllers/AddressModalControllerCalibrator',
            'calibrator/controllers/ArchivalVerificationsControllerCalibrator',
            'calibrator/controllers/NotificationsControllerCalibrator',
            'calibrator/controllers/MeasuringEquipmentControllerCalibrator',
           'calibrator/controllers/MeasuringEquipmentAddModalControllerCalibrator',
            'calibrator/controllers/MeasuringEquipmentEditModalControllerCalibrator',   
            'calibrator/controllers/UploadBbiFileController',
            'calibrator/services/CalibrationTestServiceCalibrator',
            'calibrator/services/AddressServiceCalibrator',
            'calibrator/services/UserServiceCalibrator',
            'calibrator/services/VerificationServiceCalibrator',
            'calibrator/services/MeasuringEquipmentServiceCalibrator',
            
            'verificator/controllers/TopNavBarControllerVerificator',
            'verificator/controllers/MainPanelControllerVerificator',
            'verificator/controllers/NewVerificationsControllerVerificator',
            'verificator/controllers/DetailsModalControllerVerificator',
            'verificator/controllers/SendingModalControllerVerificator',
            'verificator/controllers/EmployeeControllerVerificator',
            'verificator/controllers/AddressModalControllerVerificator',
            'verificator/controllers/NotificationsControllerVerificator',
            'verificator/controllers/CalibrationTestReviewControllerVerificator',    
            'verificator/services/AddressServiceVerificator',
            'verificator/services/UserServiceVerificator',
            'verificator/services/VerificationServiceVerificator'


    ], function () {});
})();
