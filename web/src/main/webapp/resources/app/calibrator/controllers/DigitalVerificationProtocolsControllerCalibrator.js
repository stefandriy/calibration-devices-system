angular
    .module('employeeModule')
    .controller('DigitalVerificationProtocolsControllerCalibrator', ['$rootScope', '$scope', '$modal', 'DigitalVerificationProtocolsServiceCalibrator',
        function ($rootScope, $scope, $modal, digitalVerificationProtocolsServiceCalibrator) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];

            /**
             * Updates the table with digitalVerificationProtocols.
             */
            $rootScope.onTableHandling = function () {
                digitalVerificationProtocolsServiceCalibrator
                    .getPage($scope.currentPage, $scope.itemsPerPage, $scope.searchData)
                    .then(function (data) {
                        $scope.pageContent = data.content;
                        $scope.totalItems = data.totalItems;
                    });
            };
            $rootScope.onTableHandling();
        }]);

        $scope.sentProtocols = function () {
        var sentProtocols = $modal
        .open({
            animation: true,
            controller: 'DigitalVerificationProtocolsControllerCalibrator',
            templateUrl: '/resources/app/calibrator/views/modals/send-protocols.html',
        });
};


