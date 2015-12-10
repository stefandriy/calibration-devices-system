angular
    .module('employeeModule')

    .controller('EditPhotoController', ['$scope', '$rootScope', '$route', '$log', '$modalInstance',
         '$timeout', 'photoId', 'parentScope' , '$translate',
        function ($scope, $rootScope, $route, $log, $modalInstance, $timeout, photoId, parentScope ,$translate) {

             /**
             * Closes modal window on browser's back/forward button click.
             */
            $scope.$on('$locationChangeStart', function() {
                $modalInstance.close();
            });

            $scope.cancel = function () {
                $modalInstance.close("cancel");
            };

            $scope.photoId = photoId;
            $scope.parentScope = parentScope;
            $scope.newValues = {};
            $scope.newValues.counterNumber = null;
            $scope.newValues.counterYear = null;
            $scope.newValues.accumulatedVolume = null;
            $scope.newValues.counterValue = null;
            $scope.photoType = null;
            $scope.photoIndex = null;

            $scope.isChanged = false;

            $scope.changed= function (){
                $scope.isChanged = true;
            };

            $scope.updateValues = function (index) {
                var test = parentScope.TestDataFormData[index];
                if (test.endValue == 0 || test.initialValue > test.endValue || test.initialValue ==0) {
                    test.testResult = 'RAW';
                    parentScope.TestForm.testResult = 'RAW';
                    test.calculationError = null;
                } else if (test.initialValue == test.endValue) {
                    test.testResult = 'FAILED';
                    parentScope.TestForm.testResult = 'FAILED';
                    test.calculationError = null;
                } else if (test.acceptableError >= Math.abs($scope.calcError(test.initialValue, test.endValue, test.volumeOfStandard))) {
                    test.testResult = 'SUCCESS';
                    parentScope.TestForm.testResult = 'SUCCESS';
                    test.calculationError = $scope.calcError(test.initialValue, test.endValue, test.volumeOfStandard);
                } else {
                    test.testResult = 'FAILED';
                    parentScope.TestForm.testResult = 'FAILED';
                    test.calculationError = null;
                }
                parentScope.TestDataFormData[index] = test;

            };


            $scope.calcError = function (initialValue, endValue, volumeOfStandard) {
                return parseFloat(((endValue - initialValue - volumeOfStandard) / (volumeOfStandard ) * 100).toFixed(1));
            };

            //$scope.newValues.typeWater = undefined;

            $scope.selectedTypeWater = {
                type: null
            };

            $scope.statusTypeWater = [
                {type: 'WATER'},
                {type: 'THERMAL'}
            ];

            $scope.setStatusTypeWater = function (typeWater) {
                $scope.selectedTypeWater = {
                    type: typeWater
                };
            };

            if (photoId == "testMainPhoto") {
                $scope.newValues.counterNumber = parentScope.TestForm.counterNumber;
                $scope.newValues.counterYear = parentScope.TestForm.counterProductionYear;
                $scope.newValues.accumulatedVolume = parentScope.TestForm.accumulatedVolume;
                $scope.setStatusTypeWater(parentScope.TestForm.typeWater);
            } else {
                var idSplit = photoId.split("Photo");
                $scope.photoType = idSplit[0];
                $scope.photoIndex = parseInt(idSplit[1]);
                $scope.newValues.counterValue = $scope.photoType == 'begin'
                    ? parentScope.TestDataFormData[$scope.photoIndex].initialValue
                    : parentScope.TestDataFormData[$scope.photoIndex].endValue;
            }

            $scope.photo = document.getElementById(photoId).src;

            $scope.rotateIndex;

            switch (document.getElementById(photoId).className) {
                case "rotated90" : {
                    $scope.rotateIndex = 1;
                    break;
                }
                case "rotated180" : {
                    $scope.rotateIndex = 2;
                    break;
                }
                case "rotated270" : {
                    $scope.rotateIndex = 3;
                    break;
                }
                case "rotated0" : {
                    $scope.rotateIndex = 4;
                    break;
                }
            }

            $scope.rotateLeft = function() {
                $scope.rotateIndex--;
                if ($scope.rotateIndex == 0) {
                    $scope.rotateIndex = 4;
                }
            };

            $scope.rotateRight = function() {
                $scope.rotateIndex++;
                if ($scope.rotateIndex == 5) {
                    $scope.rotateIndex = 1;
                }
            };

            $scope.rotate180 = function() {
                $scope.rotateIndex += 2;
                if ($scope.rotateIndex > 4) {
                    $scope.rotateIndex -= 4;
                }
            };

            $scope.saveOnExit = function () {
                if (photoId == "testMainPhoto") {
                    parentScope.TestForm.counterNumber = $scope.newValues.counterNumber;
                    parentScope.TestForm.accumulatedVolume = $scope.newValues.accumulatedVolume;
                    parentScope.TestForm.counterProductionYear = $scope.newValues.counterYear;
                } else {
                    if ($scope.photoType == 'begin') {
                        parentScope.TestDataFormData[$scope.photoIndex].initialValue = $scope.newValues.counterValue;
                        $scope.updateValues($scope.photoIndex);
                    } else {
                        parentScope.TestDataFormData[$scope.photoIndex].endValue = $scope.newValues.counterValue;
                        $scope.updateValues($scope.photoIndex);
                    }
                    $scope.isChanged = false;
                }

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
            };


            function validator(isValid) {
                $scope.entranceValidation = {
                    isValid: isValid,
                    css: isValid ? 'has-error' : 'has-success'
                }
            }





            $scope.checkAll = function (caseForValidation) {
                switch (caseForValidation) {
                    case 'counterValue':
                        var counterValue = $scope.newValues.counterValue;
                        if (/^[0-9]*$/.test(counterValue)) {
                            validator(false);
                        } else {
                            validator(true);
                        }
                        break;
                    case ('accumulatedVolume'):
                        var accumulatedVolume = $scope.newValues.accumulatedVolume;
                        if (/^[0-9]{1,4}$/.test(entrance)) {
                            validator(false);
                        } else {
                            validator(true);
                        }
                        break;

                }
            };


        }]);

