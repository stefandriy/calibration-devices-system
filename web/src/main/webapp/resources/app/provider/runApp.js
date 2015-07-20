require.config({
    paths: {
        angular: '../../assets/bower_components/angular/angular.min',
        csrfInterceptor: '../../assets/bower_components/spring-security-csrf-token-interceptor/dist/spring-security-csrf-token-interceptor.min',
        angularBootstrap: '../../assets/bower_components/angular-bootstrap/ui-bootstrap-tpls.min',
        angularUIRouter: '../../assets/bower_components/angular-ui-router/release/angular-ui-router.min',
        showErrors: '../../assets/bower_components/angular-bootstrap-show-errors/src/showErrors.min',
        ngTable: '../../assets/bower_components/ng-table/ng-table',
        angularTranslate: '../../assets/bower_components/angular-translate/angular-translate.min',
        angularTranslateLoaderStaticFiles: '../../assets/bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min',
        angularCookie: '../../assets/bower_components/angular-cookies/angular-cookies.min',
        angularTranslateStorageLocal: '../../assets/bower_components/angular-translate-storage-local/angular-translate-storage-local.min',
        angularTranslateStorageCookie: '../../assets/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.min',
		providerModule: 'providerModule'
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
        providerModule: {
            deps: ['angular', 'csrfInterceptor', 
                   'angularBootstrap', 'angularUIRouter',
                'showErrors', 'ngTable', 'angularTranslate',
                'angularCookie', 'angularTranslateStorageCookie',
                'angularTranslateStorageLocal', 'angularTranslateLoaderStaticFiles'

                ]
        }
    }
});

require(['providerModule'], function () {
    angular.bootstrap(document.getElementById('providerModule'), ['providerModule']);
});
