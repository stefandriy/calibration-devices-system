require.config({
    paths: {
        angular: '../assets/bower_components/angular/angular.min',
        csrfInterceptor: '../assets/bower_components/spring-security-csrf-token-interceptor/dist/spring-security-csrf-token-interceptor.min',
        angularBootstrap: '../assets/bower_components/angular-bootstrap/ui-bootstrap-tpls.min',
        angularUIRouter: '../assets/bower_components/angular-ui-router/release/angular-ui-router.min',
        showErrors: '../assets/bower_components/angular-bootstrap-show-errors/src/showErrors.min',
        ngTable: '../assets/bower_components/ng-table/ng-table',
        angularTranslate: '../assets/bower_components/angular-translate/angular-translate.min',
        angularTranslateLoaderStaticFiles: '../assets/bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min',
        angularCookie: '../assets/bower_components/angular-cookies/angular-cookies.min',
        angularTranslateStorageLocal: '../assets/bower_components/angular-translate-storage-local/angular-translate-storage-local.min',
        angularTranslateStorageCookie: '../assets/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.min',
        employeeModule: 'employeeModule',
        highchartsAngular:  '../assets/bower_components/highcharts-ng/src/highcharts-ng',
        highcharts:  '../assets/bower_components/highcharts-release/highcharts.src',
        chosen : '../assets/bower_components/angular-chosen-localytics/chosen',

        standalone:  '../assets/bower_components/highcharts-release/adapters/standalone-framework.src'
    },
    shim: {
        angular: {
            exports: "angular"
        },
        csrfInterceptor: {
            deps: ['angular']
        },
        angularBootstrap: {
            deps: ['angular']
        },
        angularTranslate: {
            deps: ['angular']
        },
        angularCookie: {
            deps: ['angular']
        },
        angularTranslateStorageCookie: {
            deps: ['angular', 'angularTranslate', 'angularCookie']
        },
        angularTranslateStorageLocal: {
            deps: ['angular', 'angularTranslate', 'angularTranslateStorageCookie']
        },
        angularTranslateLoaderStaticFiles: {
            deps: ['angular', 'angularTranslate']
        },
        angularUIRouter: {
            deps: ['angular']
        },
        showErrors: {
            deps: ['angular']
        },
        ngTable: {
        	exports:"ngTable",
        	 deps: [ 'angular' ]
        },
        highcharts:{
            deps: [ 'angular','standalone' ]
        },
        highchartsAngular:{
            deps: [ 'angular','highcharts']

        },
        chosen : {
            exports : "chosen",
            deps : [ 'angular' ]
        },
        employeeModule: {
            deps: ['angular', 'csrfInterceptor', 'angularBootstrap', 'angularTranslate', 'angularCookie', 'angularTranslateStorageCookie',
                   'angularTranslateStorageLocal', 'angularTranslateLoaderStaticFiles', 'angularUIRouter',
                'showErrors', 'ngTable', 'highchartsAngular', 'chosen']
        }
    }
});

require(['employeeModule'], function () {
    angular.bootstrap(document.getElementById('employeeModule'), ['employeeModule']);
});
