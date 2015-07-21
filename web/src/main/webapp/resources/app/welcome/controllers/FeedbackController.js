angular
    .module('welcomeModule')
    .controller('FeedbackController', ['$scope', '$modalInstance', '$log', 
        function ($scope, $modalInstance, $log) {
                       $scope.close = function () {
                $modalInstance.close();
            };
        }]);
