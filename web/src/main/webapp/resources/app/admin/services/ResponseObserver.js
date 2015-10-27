angular
    .module('adminModule')
.factory('responseObserver', function responseObserver($q, $window) {
    return {
        'responseError': function(errorResponse) {
            switch (errorResponse.status) {
                case 403:
                    $window.location = '/admin#/403';
                    break;
                case 404:
                    $window.location = '/admin#/404';
                    break;
                case 500:
                    $window.location = '/admin#/500';
                    break;
            }
            return $q.reject(errorResponse);
        }
    };
});