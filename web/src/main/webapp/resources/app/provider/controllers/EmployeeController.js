angular
    .module('providerModule')
    .controller('EmployeeController', ['$scope', '$log', '$modal', '$state', 'UserService',

        function ($scope, $log, $modal, $state, userService) {

            $scope.employeeData = {};

            $scope.openAddressModal = function () {
                var addressModal = $modal.open({
                    animation: true,
                    controller: 'AddressModalController',
                    templateUrl: '/resources/app/provider/views/modals/address.html',
                    size: 'lg',
                    resolve: {
                        address: function () {
                            return $scope.address;
                        }
                    }
                });

                addressModal.result.then(function (address) {
                    $log.info(address);
                    $scope.address = address;
                    $scope.addressMessage = null;

                    if (address) {
                        $scope.addressMessage =
                            address.selectedRegion.designation + " область, " +
                            address.selectedDistrict.designation + " район, " +
                            address.selectedLocality.designation + ", " +
                            address.selectedStreet.designation + " " +
                            (address.selectedBuilding.designation || address.selectedBuilding) + " " +
                            (address.selectedFlat || "");
                        $log.info($scope.addressMessage);
                    }
                });
            };

            $scope.checkUsername = function (username) {
                $log.info($scope.employeeForm.username.$valid);
                employeeForm.username.$setValidity("usernameError", true);
                userService
                    .isUsernameAvailable(username)
                    .success(function (result) {
                        $scope.usernameError = !result;
                    })
            };

            $scope.resetForm = function () {
                $state.go($state.current, {}, {reload: true});
            };

            $scope.addEmployee = function (employeeForm) {
                $scope.$broadcast('show-errors-check-validity');

                if (employeeForm.$valid) {

                    var employeeData = $scope.employeeData;
                    var address = $scope.address;

                    employeeData.address = {
                        region: address.selectedRegion.designation,
                        district: address.selectedDistrict.designation,
                        locality: address.selectedLocality.designation,
                        street: address.selectedStreet.designation,
                        building: address.selectedBuilding.designation || address.selectedBuilding,
                        flat: address.selectedFlat
                    };


                    $log.info(employeeData);

                    userService.saveUser(employeeData)
                        .success(function (response) {
                            $log.info(response);

                            $modal.open({
                                animation: true,
                                templateUrl: '/resources/app/provider/views/modals/employee-adding-success.html',
                                controller: function ($modalInstance) {
                                    this.ok = function () {
                                        $modalInstance.close();
                                    }
                                },
                                controllerAs: 'successController',
                                size: 'md'
                            });

                            $scope.resetForm();
                        });
                }
            };
        }]);
