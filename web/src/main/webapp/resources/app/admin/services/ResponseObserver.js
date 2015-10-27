angular
    .module('adminModule')
.factory('responseObserver', function responseObserver($q, $window) {
    return {
        'responseError': function(errorResponse) {
            switch (errorResponse.status) {
                case 403:
                    $window.location = './403.html';
                    break;
                case 404:
                    $window.location = './404.html';
                    break;
                case 500:
                    $window.location = './500.html';
                    break;
            }
            return $q.reject(errorResponse);
        }
    };
});