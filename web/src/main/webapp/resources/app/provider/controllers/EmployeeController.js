angular
    .module('providerModule')
    .controller('EmployeeController', ['$scope', '$log', '$modal',

        function ($scope, $log, $modal) {


            $scope.openAddressModal = function () {
                var addressModal = $modal.open({
                    animation: true,
                    controller: 'AddressModalController',
                    templateUrl: '/resources/app/provider/views/modals/address.html',
                    size: 'lg'
                });

            };
        }]);
