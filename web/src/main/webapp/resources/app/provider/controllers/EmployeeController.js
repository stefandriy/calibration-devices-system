angular
    .module('providerModule')
    .controller('EmployeeController', ['$scope', '$log', '$modal',

        function ($scope, $log, $modal) {


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
                            address.flat || ""
                    }
                });
            };
        }]);
