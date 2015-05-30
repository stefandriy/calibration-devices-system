angular
    .module('providerModule')
    .controller('SendingModalController', ['$scope', '$rootScope', '$modalInstance', 'response',
        function ($scope,$rootScope, $modalInstance, response) {

            $scope.calibrators = response.data;

            $scope.close = function () {
                $modalInstance.close();
            };
            $scope.ok = function (calibrator) {
                $modalInstance.close(calibrator);
            }
        }]);


