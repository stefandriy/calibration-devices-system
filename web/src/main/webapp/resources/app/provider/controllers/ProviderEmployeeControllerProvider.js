angular
    .module('employeeModule')

    .controller('ProviderEmployeeControllerProvider', ['$scope', '$log', '$modalInstance','providerEmployProvider', '$rootScope',
        function ($scope, $log, $modalInstance, providerEmployProvider, $rootScope) {

            $scope.providers = providerEmployProvider.data;
            $scope.formData={};
            $scope.formData.provider= $scope.providers[0];

            $scope.providerFullName=function(provider){
                var firstName = provider.firstName ? provider.firstName : "";
                var lastName = provider.lastName ? provider.lastName : "";
                var middleName = provider.middleName ? provider.middleName : "";
                return lastName + ' ' + firstName + ' ' + middleName;
            };
            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
            $scope.submit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.providerEmployeeAdd.$valid){
                    $modalInstance.close($scope.formData);
                }
            }
        }]);



