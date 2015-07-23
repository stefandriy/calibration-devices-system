angular
    .module('employeeModule')
    .controller('MainPanelControllerProvider', ['$scope', '$log',
        function ($scope, $log) {
        	$log.debug('inside main pan ctrl provider');
	}]);
