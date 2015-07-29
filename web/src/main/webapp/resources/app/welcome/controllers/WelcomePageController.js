angular
    .module('welcomeModule')
    .controller('WelcomePageController', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
        $scope.myInterval = 5000;
        $scope.slides = [];


        $scope.slides = [
            {
                image: 'resources/assets/images/main.jpg'
            },
            {

                image: 'resources/assets/images/main2.jpg'
            },
            {
                image: 'resources/assets/images/main3.jpg'
            },
            {
                image: 'resources/assets/images/main4.jpg'
            }
        ];
    }]
);