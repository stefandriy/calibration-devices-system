angular
    .module('providerModule')
    .controller('SendingModalController', ['$scope', '$rootScope', '$modalInstance', 'calibrators',
        function ($scope,$rootScope, $modalInstance, calibrators) {

            $scope.calibrators = calibrators;
            $scope.close = function () {
                $modalInstance.close();
            };
            $scope.ok = function (calibrator) {
                $modalInstance.close(calibrator);
            }
        }]);


