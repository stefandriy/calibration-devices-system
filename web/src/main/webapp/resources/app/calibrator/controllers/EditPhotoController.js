angular
    .module('employeeModule')

    .controller('EditPhotoController', ['$scope', '$rootScope', '$route', '$log', '$modalInstance',
         '$timeout', 'photo', 'setMainPhoto',
        function ($scope, $rootScope, $route, $log, $modalInstance, $timeout, photo, setMainPhoto) {

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $scope.$on('$locationChangeStart', function() {
                $modalInstance.close();
            });

            $scope.cancel = function () {
                $modalInstance.close("cancel");
            };

            $scope.mainPhoto = photo;

            $scope.rotateIndex = 4;

            $scope.rotateLeft = function() {
                $scope.rotateIndex--;
                if ($scope.rotateIndex == 0) {
                    $scope.rotateIndex = 4;
                }
            }

            $scope.rotateRight = function() {
                $scope.rotateIndex++;
                if ($scope.rotateIndex == 5) {
                    $scope.rotateIndex = 1;
                }
            }

            $scope.rotate180 = function() {
                $scope.rotateIndex += 2;
                if ($scope.rotateIndex > 4) {
                    $scope.rotateIndex -= 4;
                }
            }

            $scope.saveOnExit = function () {
                setMainPhoto($scope.mainPhoto);
                $modalInstance.close("saved");
            }
        }]);

