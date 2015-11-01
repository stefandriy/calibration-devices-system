angular
    .module('employeeModule')
    .controller('CounterStatusControllerCalibrator', ['$scope', '$rootScope', '$modalInstance', '$log', 'VerificationPlanningTaskService', //'response',
        function ($scope, $rootScope, $modalInstance, $log, planningTaskService) {

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $scope.$on('$locationChangeStart', function() {
                $modalInstance.close();
            });

            $scope.close = function () {
                $modalInstance.close();
            };

            $scope.verifId = $rootScope.verificationId;
            /**
             *  Date picker and formatter setup
             *
             */
            $scope.firstCalendar = {};
            $scope.firstCalendar.isOpen = false;
            $scope.secondCalendar = {};
            $scope.secondCalendar.isOpen = false;

            $scope.open1 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.firstCalendar.isOpen = true;
            };

            $scope.open2 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.secondCalendar.isOpen = true;
            };

            moment.locale('uk');
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false',

            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy-MM-dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

            $scope.clear = function () {
                $scope.addInfo.pickerDate = null;
            };

            // Disable weekend selection
            $scope.disabled = function(date, mode) {
                return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
            };

            $scope.toggleMin = function() {
                $scope.minDate = $scope.minDate ? null : new Date();
            };

            $scope.toggleMin();
            $scope.maxDate = new Date(2100, 5, 22);


            $scope.clearDate1 = function () {
                $log.debug($scope.addInfo.dateOfVerif);
                $scope.addInfo.dateOfVerif = null;
            };

            $scope.clearDate2 = function () {
                $log.debug($scope.addInfo.noWaterToDate);
                $scope.addInfo.noWaterToDate = null;
            };

            $scope.counterInfo = {};
            $scope.counterInfo.counterStatus = false;
            $scope.resolveCounterStatus = function(){
                if($scope.counterInfo.counterStatus === false){
                    $scope.counterInfo.counterStatus = true;
                } else {
                    $scope.counterInfo.counterStatus = false;
                }
                if ($scope.counterInfo.counterStatus === true) {
                    findSymbolsAndStandartSizes();
                }
                if ($scope.counterInfo.counterStatus === false){
                    $scope.counterInfo.symbol = undefined;
                    $scope.counterInfo.standardSize = undefined;
                }
            }

            $scope.sizes = {};
            $scope.symbols = {};
            function findSymbolsAndStandartSizes(){
                planningTaskService.getSymbolsAndStandartSizes($scope.verifId)
                    .then(function (result) {
                        $log.debug('result ', result.data);
                        $scope.sizes = result.data.sizes;
                        $scope.symbols = result.data.symbols;
                    }, function (result) {
                        $log.debug('error fetching data:', result);
                    });
            }

            $scope.checkAll = function (caseForValidation) {
                switch (caseForValidation) {
                  case ('counterNumber'):
                        var counterNumber = $scope.counterInfo.counterNumber;
                        if (/^[0-9]{5,20}$/.test(counterNumber)) {
                            validator('counterNumber', false);
                        } else {
                            validator('counterNumber', true);
                        }
                        break;
                    case ('year'):
                        var year = $scope.counterInfo.year;
                        if (/^([12]{1}[09]{1}[\d]{2})$/.test(year)) {
                            validator('year', false);
                        } else {
                            validator('year', true);
                        }
                        break;
                }

            }

            function validator(caseForValidation, isValid) {
                switch (caseForValidation) {
                    case ('counterNumber'):
                        $scope.counterNumberValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('year'):
                        $scope.yearValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;

                }
            }

            $scope.resetCounterStatusForm = function(){
                $scope.$broadcast('show-errors-reset');
                $scope.yearValidation = {};
                $scope.counterNumberValidation = {};
                $scope.counterInfo = {};
            }

            $scope.showMessage = {
                status: false
            }

        }]);

