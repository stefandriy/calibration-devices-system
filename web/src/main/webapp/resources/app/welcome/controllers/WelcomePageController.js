angular
    .module('welcomeModule')
    .controller('WelcomePageController', ['$scope', function ($scope) {
        $scope.myInterval = 3000;
        $scope.slides = [];


        $scope.slides = [
            {
                image: 'resources/assets/images/main2.jpg'
            },
            {

                image: 'resources/assets/images/main5.jpg'
            },
            {
                image: 'resources/assets/images/main3.jpg'
            },
            {
                image: 'resources/assets/images/main6.jpg'
            },
            {
                image: 'resources/assets/images/main7.jpg'
            }
        ];
    }]
);