(function () {
    angular.module('calibratorModule', ['spring-security-csrf-token-interceptor',
        'ui.bootstrap', 'ui.bootstrap.datepicker', 'ui.router', 'ui.bootstrap.showErrors', 'ngTable'])

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
                    .state("calibration-test", {
                        url: '/verifications/calibration-test',
                        templateUrl: '/resources/app/calibrator/views/calibration-test-add-modal.html',
                        controller: 'CalibrationTestController'
                    })
                    .state("employees", {
                        url: '/employees',
                        templateUrl: '/resources/app/calibrator/views/employee/main-panel.html',
                        controller: 'EmployeeController'
                    })
                    .state("planning-task-calibrator", {
                        url: '/calibrator/verifications/task',
                        templateUrl: '/resources/app/calibrator/views/task-for-verifications.html',
                        controller: 'VerificationPlanningTaskController'
                    })
                    .state("calibrator-task-add", {
                        url: '/calibrator/task/',
                        templateUrl: '/resources/app/calibrator/views/modals/eddTaskModal.html',
                        controller: 'TaskSendingModalControllerCalibrator'
                    })
                    .state("calibrator-add-info", {
                        url: '/calibrator/addInfo/',
                        templateUrl: '/resources/app/calibrator/views/modals/additionalInformation.html',
                        controller: 'AdditionalInfoController'
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
        'controllers/NewVerificationsController',
        'controllers/DetailsModalController',
        'controllers/SendingModalController',
        'controllers/CalibrationTestController',
        'controllers/EmployeeController',
        'controllers/AddressModalController',
        'controllers/NotificationsController',
        'controllers/VerificationPlanningTaskController',
        'controllers/TaskSendingModalControllerCalibrator',

        'services/CalibrationTestService',
        'services/AddressService',
        'services/UserService',
        'services/VerificationService',
        'services/VerificationPlanningTaskService'
    ], function () {});
})();
