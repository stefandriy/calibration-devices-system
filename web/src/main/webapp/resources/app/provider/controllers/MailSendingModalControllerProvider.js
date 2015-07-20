angular
    .module('employeeModule')

    .controller('MailSendingModalControllerProvider', ['$scope', '$log', '$modalInstance','$rootScope',
        function ($scope, $log, $modalInstance,  $rootScope) {
          $scope.formData={};
            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
            $scope.submit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.mailSendingForm.$valid){
                    $modalInstance.close($scope.formData);

                }
            }
        }]);