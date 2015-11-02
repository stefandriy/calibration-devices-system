angular
    .module('employeeModule')
    .controller('CounterStatusControllerCalibrator', ['$scope', '$rootScope', '$modalInstance', '$log', 'VerificationPlanningTaskService',
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

            /**
             * open first date picker
             * on the modal
             *
             * @param $event
             */
            $scope.open1 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.firstCalendar.isOpen = true;
            };

            /**
             * open second date picker
             * on the modal window
             *
             * @param $event
             */
            $scope.open2 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.secondCalendar.isOpen = true;
            };


            /**
             * set date pickers options
             * @type {{formatYear: string, startingDay: number, showWeeks: string}}
             */
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false',
            };

            /**
             * set format of date picker date
             *
             */
            $scope.formats = ['dd-MMMM-yyyy', 'yyyy-MM-dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

            /**
             * clear date in the date picker
             */
            $scope.clear = function () {
                $scope.addInfo.pickerDate = null;
            };

            /**
             * Disable weekend selection
             *
             * @param date
             * @param mode
             * @returns {boolean}
             */
            $scope.disabled = function(date, mode) {
                return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
            };

            $scope.toggleMin = function() {
                $scope.minDate = $scope.minDate ? null : new Date();
            };

            $scope.toggleMin();
            $scope.maxDate = new Date(2100, 5, 22);


            $scope.clearDate1 = function () {
                $scope.counterInfo.dateOfVerif = null;
            };

            $scope.clearDate2 = function () {
                $scope.counterInfo.noWaterToDate = null;
            };


            $scope.counterInfo = {};
            $scope.counterInfo.counterStatus = false;

            /**
             *  work with toggle button.
             *  if status is true this method calls the
             *  findSymbolsAndStandartSizes() method which makes
             *  asynchronous request to the server and search
             *  the counter sizes and symbols and open on the frontend
             *  select fields
             */
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

            /**
             *  method which makes asynchronous request to the server and search
             * the counter sizes and symbols
             *
             */
            $scope.sizes = {};
            $scope.symbols = {};
            function findSymbolsAndStandartSizes(){
                planningTaskService.getSymbolsAndStandartSizes($scope.verifId)
                    .then(function (result) {
                        $scope.sizes = result.data.sizes;
                        $scope.symbols = result.data.symbols;
                    });
            }

            /**
             * validation for the counter number
             * and year field
             *
             * @param caseForValidation
             */
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

            /**
             * validation for the counter number
             * and year field
             *
             * @param caseForValidation
             * @param isValid
             */
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

            /**
             * reset counter info form
             */
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

