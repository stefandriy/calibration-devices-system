angular
    .module('adminModule')
    .controller('MainPanelController', ['$scope', 'StatisticService',
        function ($scope, statisticService) {
            $scope.statistics = {
                organization: 0,
                users: 0,
                devices: 0,
                countertypes: 0,
                verifications: 0,
                sysAdmins : 0
            };
            statisticService.organizations().then(function (data) {
                $scope.statistics.organization = data.count;
            });
            statisticService.users().then(function (data) {
                $scope.statistics.users = data.count;
            });
            statisticService.devices().then(function (data) {
                $scope.statistics.devices = data.count;
            });
            statisticService.countertypes().then(function (data) {
                $scope.statistics.countertypes = data.count;
            });
            statisticService.verifications().then(function (data) {
                $scope.statistics.verifications = data.count;
            });
            statisticService.sysAdmins().then(function (data) {
                $scope.statistics.sysAdmins = data.count;
            });
    }]);
