angular
    .module('employeeModule')

    .controller('EditPhotoController', ['$scope', '$rootScope', '$route', '$log', '$modalInstance',
         '$timeout', 'photo', 'photoId',
        function ($scope, $rootScope, $route, $log, $modalInstance, $timeout, photo, photoId) {

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
                console.log(photoId);
                switch ($scope.rotateIndex) {
                    case 1: {
                        document.getElementById(photoId).className = "rotated90";
                        break;
                    }
                    case 2: {
                        document.getElementById(photoId).className = "rotated180";
                        break;
                    }
                    case 3: {
                        document.getElementById(photoId).className = "rotated270";
                        break;
                    }
                    case 4: {
                        document.getElementById(photoId).className = "rotated0";
                        break;
                    }
                }

                $modalInstance.close("saved");
            }
        }]);

