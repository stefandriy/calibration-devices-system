angular
    .module('providerModule')
    .controller('EmployeeController', ['$scope', '$log', '$modal', 'UserService',

        function ($scope, $log, $modal, userService) {

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

                    if (address) {
                        $scope.addressMessage =
                            address.selectedRegion.designation + " область, " +
                            address.selectedDistrict.designation + " район, " +
                            address.selectedLocality.designation + ", " +
                            address.selectedStreet.designation + " " +
                            address.selectedBuilding.designation || address.selectedBuilding + "/" +
                            address.selectedFlat || ""
                    }
                });
            };

            $scope.checkUsername = function (username) {
                userService
                    .isUsernameAvailable(username)
                    .success(function (result) {
                        $scope.usernameError = result;
                    })
            };

            $scope.addEmployee = function () {

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
                    });
            };
        }]);
