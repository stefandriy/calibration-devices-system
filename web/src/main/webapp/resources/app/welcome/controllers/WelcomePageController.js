angular
    .module('welcomeModule')
    .controller('WelcomePageController', ['$scope', function ($scope) {
        $scope.slides = [
                'resources/assets/images/main2.jpg',
                'resources/assets/images/main3.jpg',
                'resources/assets/images/main7.jpg'
        ];
    }]
);