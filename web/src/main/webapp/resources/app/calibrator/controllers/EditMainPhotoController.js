angular
    .module('employeeModule')

    .controller('EditMainPhotoController', ['$scope', '$rootScope', '$route', '$log', '$modalInstance',
         '$timeout', 'photo',
        function ($scope, $rootScope, $route, $log, $modalInstance, $timeout, photo) {

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $scope.$on('$locationChangeStart', function() {
                $modalInstance.close();
            });

            $scope.cancel = function () {
                $modalInstance.close("cancel");

            };

            $scope.mainPhoto = 'data:image/png;base64,' + photo;

            $scope.getRotatedRightPhoto = function () {
                var canvas = document.createElement("canvas");
                var img = new Image();
                img.src = $scope.mainPhoto;
                canvas.width = img.height;
                canvas.height = img.width;
                var context = canvas.getContext("2d");
                context.translate(img.width/2, img.height/2);
                context.rotate(90 * Math.PI / 180);
                context.drawImage(img, -img.width/2 + 40, -img.height/2 + 40);
                return canvas.toDataURL();
            }

            $scope.getRotatedLeftPhoto = function () {
                var canvas = document.createElement("canvas");
                var img = new Image();
                img.src = $scope.mainPhoto;
                canvas.width = img.height;
                canvas.height = img.width;
                var context = canvas.getContext("2d");
                context.translate(img.width/2, img.height/2);
                context.rotate(-90 * Math.PI / 180);
                context.drawImage(img, -img.width/2, -img.height/2 - 40);
                return canvas.toDataURL();
            }

            $scope.getRotated180Photo = function() {
                var canvas = document.createElement("canvas");
                var img = new Image();
                img.src = $scope.mainPhoto;
                canvas.width = img.width;
                canvas.height = img.height;
                var context = canvas.getContext("2d");
                context.translate(img.width, img.height);
                context.rotate(180 * Math.PI / 180);
                context.drawImage(img, 0, 0);
                return canvas.toDataURL();
            }

            $scope.rotated0 = $scope.mainPhoto;

            $scope.rotatedRight = $scope.getRotatedRightPhoto();

            $scope.rotatedLeft = $scope.getRotatedLeftPhoto();

            $scope.rotated180 = $scope.getRotated180Photo();

            $scope.rotateIndex = 4;

            $scope.rotateLeft = function() {
                $scope.rotateIndex--;
                if ($scope.rotateIndex == 0) {
                    $scope.rotateIndex = 4;
                }
                $scope.changeShownPhoto();
            }

            $scope.rotateRight = function() {
                $scope.rotateIndex++;
                if ($scope.rotateIndex == 5) {
                    $scope.rotateIndex = 1;
                }
                $scope.changeShownPhoto();
            }

            $scope.rotate180 = function() {
                $scope.rotateIndex += 2;
                if ($scope.rotateIndex > 4) {
                    $scope.rotateIndex -= 4;
                }
                $scope.changeShownPhoto();
            }

            $scope.changeShownPhoto = function () {
                switch ($scope.rotateIndex) {
                    case 1: {
                        $scope.mainPhoto = $scope.rotatedRight;
                        break;
                    }
                    case 2: {
                        $scope.mainPhoto = $scope.rotated180;
                        break;
                    }
                    case 3: {
                        $scope.mainPhoto = $scope.rotatedLeft;
                        break;
                    }
                    case 4: {
                        $scope.mainPhoto = $scope.rotated0;
                        break;
                    }
                }
            }
        }]);

