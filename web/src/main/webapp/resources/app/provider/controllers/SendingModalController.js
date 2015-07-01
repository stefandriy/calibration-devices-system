angular
    .module('providerModule')

    .controller('SendingModalController', ['$scope', '$log', '$modalInstance', 'response','providerEmploy', '$rootScope',
        function ($scope, $log, $modalInstance, response, providerEmploy, $rootScope) {

            $scope.calibrators = response.data;
            $scope.providers = providerEmploy.data;
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


                if ($scope.calibratorSelectionForm.$valid){
                    $modalInstance.close($scope.formData);

                }
            }
        }]);


