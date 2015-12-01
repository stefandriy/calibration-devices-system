angular.module("employeeModule")
    .controller("checkboxController", function checkboxController($scope) {


        $scope.$data = [{
            Name: "Item one"
        }, {
            Name: "Item two"
        }, {
            Name: "Item three"
        }];
        $scope.checkAll = function () {
            if ($scope.selectedAll) {
                $scope.selectedAll = true;
            } else {
                $scope.selectedAll = false;
            }
            angular.forEach($scope.$data, function (verification) {
                verification.Selected = $scope.selectedAll;
            });

        };


    });