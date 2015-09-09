angular.module('employeeModule')
    .controller('CalibrationTestEditModalController',
    ['$rootScope', '$scope', '$modalInstance', 'CalibrationTestServiceCalibrator',
        function ($rootScope, $scope, $modalInstance, calibrationTestServiceCalibrator) {
    	
	    	 /**
	         * Closes modal window on browser's back/forward button click.
	         */     
	    	$rootScope.$on('$locationChangeStart', function() {
			    $modalInstance.close();
			});
    	
            /**
             * Edit test. If everything is ok then
             * resets the test form and closes modal
             * window.
             */

            $scope.editTest = function () {

                var testForm = {
                    name: $scope.calibrationTest.name,
                    temperature: $scope.calibrationTest.temperature,
                    settingNumber: $scope.calibrationTest.settingNumber,
                    latitude: $scope.calibrationTest.latitude,
                    longitude: $scope.calibrationTest.longitude,
                    consumptionStatus: $scope.calibrationTest.consumptionStatus,
                    testResult: $scope.calibrationTest.testResult
                }

               console.log(testForm);

                if (!$scope.calibrationTestFormEdit.isValid) {
                    calibrationTestServiceCalibrator.editCalibrationTest(testForm, $rootScope.testId).then(
                        function (data) {
                            if (data == 200) {
                                $scope.closeModal();
                                $scope.resetTestForm();
                                $rootScope.onTableHandling();
                            }
                        });
                } else {
                    $scope.incorrectValue = true;
                }
            }


            /**
             * Resets test form
             */
            $scope.resetTestForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.calibrationTest = null;
                $scope.nameValidation = null;
                $scope.temperatureValidation = null;
                $scope.settingNumberValidation = null;
                $scope.latitudeValidation = null;
                $scope.longitudeValidation = null;
                $scope.consumptionStatusValidation = null;
                $scope.testResultValidation = null;
                $scope.incorrectValue = false;
            };

            /**
             * Closes edit modal window.
             */
            $scope.closeModal = function () {
                $modalInstance.close();
            };

            $scope.checkAll = function (caseForValidation) {
                switch (caseForValidation) {
                    case ('name'):
                        var name = $scope.calibrationTest.name;
                        if (name == null) {

                        } else if (/^[a-zA-Z0-9]{5,20}$/.test(name)) {
                            validator('name', false);
                        } else {
                            validator('name', true);
                        }
                        break;
                    case ('temperature'):
                        var temperature = $scope.calibrationTest.temperature;
                        if (temperature == null) {

                        } else if (/^\d{1,3}$/.test(temperature)) {
                            validator('temperature', false);
                        } else {
                            validator('temperature', true);
                        }
                        break;
                    case ('settingNumber'):
                        var settingNumber = $scope.calibrationTest.settingNumber;
                        if (settingNumber == null) {
                        }
                        else if (/^\d{1,5}$/.test(settingNumber)) {
                            validator('settingNumber', false);
                        } else {
                            validator('settingNumber', true);
                        }
                        break;
                    case ('latitude'):
                        var latitude = $scope.calibrationTest.latitude;
                        if (latitude == null) {
                        }
                        else if (/^\d{2,7}$/.test(latitude)) {
                            validator('latitude', false);
                        } else {
                            validator('latitude', true);
                        }
                        break;
                    case ('longitude'):
                        var longitude = $scope.calibrationTest.longitude;
                        if (longitude == null) {
                        }
                        else if (/^\d{2,7}$/.test(longitude)) {
                            validator('longitude', false);
                        } else {
                            validator('longitude', true);
                        }
                        break;
                    case ('consumptionStatus'):
                        var consumptionStatus = $scope.calibrationTest.consumptionStatus;
                        if (consumptionStatus == null) {
                        }
                        else if (/^[a-zA-Z0-9]{5,20}$/.test(consumptionStatus)) {
                            validator('consumptionStatus', false);
                        } else {
                            validator('consumptionStatus', true);
                        }
                        break;
                    case ('testResult'):
                        var testResult = $scope.calibrationTest.testResult;
                        if (testResult == null) {
                        }
                        else if (/^[a-zA-Z0-9]{5,20}$/.test(testResult)) {
                            validator('testResult', false);
                        } else {
                            validator('testResult', true);
                        }
                        break;
                }

            }

            function validator(caseForValidation, isValid) {
                switch (caseForValidation) {
                    case ('name'):
                        $scope.nameValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('temperature'):
                        $scope.temperatureValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('settingNumber'):
                        $scope.settingNumberValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('latitude'):
                        $scope.latitudeValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('longitude'):
                        $scope.longitudeValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('consumptionStatus'):
                        $scope.consumptionStatusValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('testResult'):
                        $scope.testResultValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                }
            }

        }]);

