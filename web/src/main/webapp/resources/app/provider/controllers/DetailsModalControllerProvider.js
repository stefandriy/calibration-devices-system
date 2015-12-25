                                      angular
    .module('employeeModule')
    .controller('DetailsModalControllerProvider', ['$scope', '$modalInstance', '$log', 'response', '$rootScope',
											  'VerificationServiceProvider', 'AddressServiceProvider',
        function ($scope, $modalInstance, $log, response, $rootScope, verificationServiceProvider, addressServiceProvider) {


			$scope.counterData = {};
			$scope.counterData.selectedCount = '1';
			$scope.deviceCountOptions = [1, 2, 3, 4];

			$scope.counterData.dismantled = true;
			$scope.counterData.sealPresence = true;

			$scope.addInfo = {};
			$scope.addInfo.serviceability = true;

			$scope.symbols = [];
			$scope.standardSizes = [];

			$scope.options = {
				hstep: [0.5, 1.0, 1.5, 2.0, 2.5, 3.0]
			};

			$scope.moments = [];


			/**
			 * For timepicker
			 */
			$scope.updateTimepicker = function() {
				$scope.addInfo.timeFrom = new Date();
				$scope.addInfo.timeFrom.setHours( 8 );
				$scope.addInfo.timeFrom.setMinutes( 0 );

				$scope.updateTimeTo();
			};

			$scope.updateTimeTo = function() {
				$scope.moments = [];
				var time = undefined;
				var plusTime;
				angular.forEach($scope.options.hstep, function(value) {
					time = moment((new Date($scope.addInfo.timeFrom)).getTime());
					plusTime = 60 * value;
					$scope.moments.push(time.add(plusTime, 'minutes').format("HH:mm"));
				});
				$scope.addInfo.timeTo = $scope.moments[3];
			};

			function arrayObjectIndexOf(myArray, searchTerm, property) {
				for (var i = 0, len = myArray.length; i < len; i++) {
					if (myArray[i][property] === searchTerm) return i;
				}
				return 0;
			}

			function arrayObjectIndexOfMoments(myArray, searchTerm) {
				for (var i = 0, len = myArray.length; i < len; i++) {
					if (myArray[i].isSame(searchTerm)) return i;
				}
				return 0;
			}

			$scope.fillTimeToForEdit = function() {
				$scope.updateTimeTo();
				var index = arrayObjectIndexOfMoments($scope.moments, moment($scope.verificationInfo.timeTo, "HH:mm"));
				$scope.addInfo.timeTo = $scope.moments[index];
			};

			/**
			 * Receives all possible devices.
			 */
			addressServiceProvider.findAllDevices()
				.success(function (devices) {
					$scope.devices = devices;
					$log.debug('device');
					$log.debug(devices);
					$scope.counterData.selectedDevice = [];
					$log.debug($scope.counterData.selectedCount);
				});

			/**
			 * Receives list of all symbols from table counter_type
			 */
			$scope.receiveAllSymbols = function(device) {
				$scope.symbols = [];
				addressServiceProvider.findAllSymbols(device.id)
					.success(function(symbols) {
						$scope.symbols = symbols;
						$scope.counterData.counterSymbol = undefined;
						$scope.counterData.counterStandardSize = undefined;
					});
			};

			/**
			 * Receive list of standardSizes from table counter_type by symbol
			 */
			$scope.recieveStandardSizesBySymbol = function (symbol, device) {
				$scope.standardSizes = [];
				addressServiceProvider.findStandardSizesBySymbol(symbol, device.id)
					.success(function(standardSizes) {
						$scope.standardSizes = standardSizes;
						$scope.counterData.counterStandardSize = undefined;
					});
			};

	    	/**
	         * Closes modal window on browser's back/forward button click.
	         */ 
	    	$rootScope.$on('$locationChangeStart', function() {
			    $modalInstance.close();
			});
    	
    		$scope.verificationData = response.data;

		    $scope.acceptVerification = function () {
		    	 var dataToSend = {
							verificationId: $rootScope.verificationID,
							status: 'ACCEPTED'
				};
		    verificationServiceProvider.acceptVerification(dataToSend).success(function () {
			$rootScope.$broadcast('refresh-table');
		    		 $modalInstance.close();
		    	});
		    };
		  
		    $scope.rejectVerification = function () {
		    	$rootScope.$broadcast("verification_rejected", { verifID: $rootScope.verificationID });
		    	$modalInstance.close();
		    };

			/**
			 * Convert date to long to sent it to backend
			 * @param date
			 * @returns {number}
			 */
			$scope.convertDateToLong = function(date) {
				return (new Date(date)).getTime();
			};
		  
		    $scope.close = function () {
		    	$modalInstance.close();
		    };

			$scope.showAddInfoTable = {
				status: false
			};

			$scope.toEdit = false;
			$scope.additionalInfo = {};
			$scope.counterInfo = {};

			verificationServiceProvider.getVerificationById($scope.verificationData.id)
				.success(function(info) {
					$scope.verificationInfo = info;
					$scope.convertCounterForView();
					$scope.convertInfoForView();
					$scope.fillCounterForEdit();
					$scope.fillAddInfoForEdit();
				});

			$scope.convertCounterForView = function() {

				// COUNTER
				$scope.counterInfo.deviceName = $scope.verificationInfo.deviceName;
				$scope.counterInfo.counterStatus = ($scope.verificationInfo.dismantled) ? "так" : "ні";
				$scope.counterInfo.dateOfDismantled = ($scope.verificationInfo.dateOfDismantled)
					? new Date($scope.verificationInfo.dateOfDismantled).toLocaleDateString() : "час відсутній";
				$scope.counterInfo.dateOfMounted = ($scope.verificationInfo.dateOfMounted)
					? new Date($scope.verificationInfo.dateOfMounted).toLocaleDateString() : "час відсутній";
				$scope.counterInfo.comment = $scope.verificationInfo.comment;
				$scope.counterInfo.numberCounter = $scope.verificationInfo.numberCounter;
				$scope.counterInfo.sealPresence = ($scope.verificationInfo.sealPresence) ? "так" : "ні";
				$scope.counterInfo.counterSymbol = $scope.verificationInfo.symbol;
				$scope.counterInfo.counterStandardSize = $scope.verificationInfo.standardSize;
				$scope.counterInfo.releaseYear = $scope.verificationInfo.releaseYear;
			};

			$scope.convertInfoForView = function() {

				//ADDITION INFO
				$scope.additionalInfo.entrance = $scope.verificationInfo.entrance;
				$scope.additionalInfo.doorCode = $scope.verificationInfo.doorCode;
				$scope.additionalInfo.floor = $scope.verificationInfo.floor;
				$scope.additionalInfo.dateOfVerif = ($scope.verificationInfo.dateOfVerif)
					? new Date($scope.verificationInfo.dateOfVerif).toLocaleDateString() :  "час відсутній";
				$scope.additionalInfo.time = ($scope.verificationInfo.timeFrom && $scope.verificationInfo.timeTo)
					? ($scope.verificationInfo.timeFrom + " - " + $scope.verificationInfo.timeTo) : "час відсутній";
				$scope.additionalInfo.serviceability = ($scope.verificationInfo.serviceability) ? "так" : "ні" ;
				$scope.additionalInfo.noWaterToDate = ($scope.verificationInfo.noWaterToDate)
					? new Date($scope.verificationInfo.noWaterToDate).toLocaleDateString() : "час відсутній";
				$scope.additionalInfo.notes = $scope.verificationInfo.notes;

			};

			$scope.fillCounterForEdit = function() {

				//COUNTER
				$scope.counterData.dismantled = $scope.verificationInfo.dismantled;
				$scope.counterData.dateOfDismantled = $scope.verificationInfo.dateOfDismantled;
				$scope.counterData.dateOfMounted = $scope.verificationInfo.dateOfMounted;
				$scope.counterData.comment = $scope.verificationInfo.comment;
				$scope.counterData.numberCounter = $scope.verificationInfo.numberCounter;
				$scope.counterData.sealPresence = $scope.verificationInfo.sealPresence;
				$scope.counterData.releaseYear = $scope.verificationInfo.releaseYear;

				if ($scope.verificationInfo.deviceName) {
					addressServiceProvider.findAllDevices().then(function (devices) {
						$scope.devices = devices.data;
						var index = arrayObjectIndexOf($scope.devices, $scope.verificationInfo.deviceName, "designation");
						$scope.counterData.selectedDevice = $scope.devices[index];

						if ($scope.verificationInfo.symbol) {

							addressServiceProvider.findAllSymbols($scope.verificationInfo.deviceId).then(function (respSymbols) {
								$scope.symbols = respSymbols.data;
								var index = arrayObjectIndexOf($scope.symbols, $scope.verificationInfo.symbol);
								$scope.counterData.counterSymbol = $scope.symbols[index];

								addressServiceProvider.findStandardSizesBySymbol($scope.counterData.counterSymbol, $scope.verificationInfo.deviceId)
									.then(function (standardSizes) {
										$scope.standardSizes = standardSizes.data;
										var index = arrayObjectIndexOf($scope.standardSizes, $scope.verificationInfo.standardSize);
										$scope.counterData.counterStandardSize = $scope.standardSizes[index];
									});
							});
						}
					});
				}
			};

			$scope.fillAddInfoForEdit = function() {

				//ADDITION INFO
				$scope.addInfo.entrance = $scope.verificationInfo.entrance;
				$scope.addInfo.doorCode = $scope.verificationInfo.doorCode;
				$scope.addInfo.floor = $scope.verificationInfo.floor;
				$scope.addInfo.dateOfVerif = $scope.verificationInfo.dateOfVerif;
				if($scope.verificationInfo.timeFrom && $scope.verificationInfo.timeTo) {
					$scope.addInfo.timeFrom = moment($scope.verificationInfo.timeFrom, "HH:mm");
					$scope.fillTimeToForEdit()
				} else {
					$scope.updateTimepicker();
				}
				$scope.addInfo.serviceability = $scope.verificationInfo.serviceability;
				$scope.addInfo.noWaterToDate = $scope.verificationInfo.noWaterToDate;
				$scope.addInfo.notes = $scope.verificationInfo.notes;
			};

			/**
			 * Toggle button (additional info) functionality
			 */
			$scope.showStatus = {
				opened: false
			};

			$scope.openAdditionalInformation = function () {
				if($scope.showStatus.opened === false){
					$scope.showStatus.opened = true;
				} else {
					$scope.showStatus.opened = false;
				}
			};

			$scope.showCounter = {
				opened: false
			};

			$scope.openCounterInfo = function() {
				$scope.showCounter.opened = !$scope.showCounter.opened;
			};

			/**
			 *  Date picker and formatter setup
			 *
			 */
			$scope.firstCalendar = {};
			$scope.firstCalendar.isOpen = false;
			$scope.secondCalendar = {};
			$scope.secondCalendar.isOpen = false;
			$scope.thirdCalendar = {};
			$scope.thirdCalendar.isOpen = false;
			$scope.fourthCalendar = {};
			$scope.fourthCalendar.isOpen = false;

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

			$scope.open3 = function ($event) {
				$event.preventDefault();
				$event.stopPropagation();
				$scope.thirdCalendar.isOpen = true;
			};

			$scope.open4 = function ($event) {
				$event.preventDefault();
				$event.stopPropagation();
				$scope.fourthCalendar.isOpen = true;
			};

			moment.locale('uk');
			$scope.dateOptions = {
				formatYear: 'yyyy',
				startingDay: 1,
				showWeeks: 'false'

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
			$scope.minDateDismantled = new Date(2015, 1, 1);
			$scope.maxDateDismantled = new Date();


			$scope.clearDate1 = function () {
				$scope.addInfo.dateOfVerif = null;
			};

			$scope.clearDate2 = function () {
				$scope.addInfo.noWaterToDate = null;
			};

			$scope.clearDateOfDismantled = function() {
				$scope.counterData.dateOfDismantled = null;
			};

			$scope.clearDateOfMounted = function() {
				$scope.counterData.dateOfMounted = null;
			};

			/**
			 * additonal info form validation
			 *
			 * @param caseForValidation
			 */
			$scope.checkAll = function (caseForValidation) {
				switch (caseForValidation) {
					case ('installationNumber'):
						var installationNumber = $scope.calibrationTask.installationNumber;
						if (/^[0-9]{5,20}$/.test(installationNumber)) {
							validator('installationNumber', false);
						} else {
							validator('installationNumber', true);
						}
						break;
					case ('entrance'):
						var entrance = $scope.addInfo.entrance;
						if (/^[0-9]{1,2}$/.test(entrance)) {
							validator('entrance', false);
						} else {
							validator('entrance', true);
						}
						break;
					case ('doorCode'):
						var doorCode = $scope.addInfo.doorCode;
						if (/^[0-9]{1,4}$/.test(doorCode)) {
							validator('doorCode', false);
						} else {
							validator('doorCode', true);
						}
						break;
					case ('floor'):
						var floor = $scope.addInfo.floor;
						if (floor == null) {

						} else if (/^\d{1,2}$/.test(floor)) {
							validator('floor', false);
						} else {
							validator('floor', true);
						}
						break;
					case ('counterNumber'):
						var counterNumber = $scope.addInfo.counterNumber;
						if (/^[0-9]{5,20}$/.test(counterNumber)) {
							validator('counterNumber', false);
						} else {
							validator('counterNumber', true);
						}
						break;
					case ('time'):
						var time = $scope.addInfo.time;
						if (/^[0-1]{1}[0-9]{1}(\:)[0-9]{2}(\-)[0-2]{1}[0-9]{1}(\:)[0-9]{2}$/.test(time)) {
							validator('time', false);
						} else {
							validator('time', true);
						}
						break;
				}

			};

			function validator(caseForValidation, isValid) {
				switch (caseForValidation) {
					case ('entrance'):
						$scope.entranceValidation = {
							isValid: isValid,
							css: isValid ? 'has-error' : 'has-success'
						};
						break;
					case ('doorCode'):
						$scope.doorCodeValidation = {
							isValid: isValid,
							css: isValid ? 'has-error' : 'has-success'
						};
						break;
					case ('floor'):
						$scope.floorValidation = {
							isValid: isValid,
							css: isValid ? 'has-error' : 'has-success'
						};
						break;
					case ('counterNumber'):
						$scope.counterNumberValidation = {
							isValid: isValid,
							css: isValid ? 'has-error' : 'has-success'
						};
						break;
					case ('time'):
						$scope.timeValidation = {
							isValid: isValid,
							css: isValid ? 'has-error' : 'has-success'
						};
						break;

				}
			}

			/**
			 * reset additional info form
			 */
			$scope.resetAddInfoForm = function(){
				$scope.$broadcast('show-errors-reset');
				$scope.fillAddInfoForEdit();
			};

			/**
			 * reset counter form
			 */
			$scope.resetCounterForm = function() {
				$scope.$broadcast('show-errors-reset');
				$scope.fillCounterForEdit();
			};

			$scope.showMessage = {
				status: false
			};

			/**
			 * sent form data about counter to the server for updating
			 */
			$scope.editCounter = function() {

				var counter = {
					"verificationId": $scope.verificationData.id,
					"deviceId": $scope.counterData.selectedDevice.id,
					"deviceName": $scope.counterData.selectedDevice.designation,
					"dismantled": $scope.counterData.dismantled,
					"dateOfDismantled": ($scope.convertDateToLong($scope.counterData.dateOfDismantled) !== 0)
						? $scope.convertDateToLong($scope.counterData.dateOfDismantled) : null,
					"dateOfMounted": ($scope.convertDateToLong($scope.counterData.dateOfMounted) !== 0)
						? $scope.convertDateToLong($scope.counterData.dateOfMounted) : null,
					"comment": $scope.counterData.comment,
					"numberCounter": $scope.counterData.numberCounter,
					"sealPresence": $scope.counterData.sealPresence,
					"symbol": $scope.counterData.counterSymbol,
					"standardSize": $scope.counterData.counterStandardSize,
					"releaseYear": $scope.counterData.releaseYear
				};
				verificationServiceProvider.editCounterInfo(counter)
					.then(function(response) {
						if (response.status == 200) {
							verificationServiceProvider.getVerificationById($scope.verificationData.id)
								.success(function(info) {
									$scope.verificationInfo = info;
									$scope.convertCounterForView();
									$scope.toEditCounter = !$scope.toEditCounter;
								});
						} else {
							$scope.incorrectValue = true;
						}
					})
			};

			/**
			 * send form data to the server
			 */
			$scope.editAdditionalInfo = function(){
				if ($scope.addInfo.entrance==undefined && $scope.addInfo.doorCode==undefined && $scope.addInfo.floor == undefined
					&& $scope.addInfo.dateOfVerif==undefined && $scope.addInfo.time == undefined &&
					$scope.addInfo.noWaterToDate == undefined && $scope.addInfo.notes == undefined){
					$scope.showMessage.status = true;
				} else {
					if ($scope.addInfo.serviceability == undefined){
						$scope.addInfo.serviceability = true;
					}
					$scope.showMessage.status = false;
					var info = {
						"entrance": $scope.addInfo.entrance,
						"doorCode": $scope.addInfo.doorCode,
						"floor": $scope.addInfo.floor,
						"dateOfVerif": $scope.addInfo.dateOfVerif,
						"timeFrom": moment($scope.convertDateToLong($scope.addInfo.timeFrom)).format("HH:mm"),
						"timeTo": $scope.addInfo.timeTo,
						"serviceability": $scope.addInfo.serviceability,
						"noWaterToDate": $scope.addInfo.noWaterToDate,
						"notes": $scope.addInfo.notes,
						"verificationId": $scope.verificationData.id
					};
					verificationServiceProvider.saveAdditionalInfo(info)
						.then(function (response) {
							if (response.status == 200) {
								verificationServiceProvider.getVerificationById($scope.verificationData.id)
									.success(function(info) {
										$scope.verificationInfo = info;
										$scope.convertInfoForView();
										$scope.toEditInfo = !$scope.toEditInfo;
									});

							} else {
								$scope.incorrectValue = true;
							}
						});
				}

			}

		}]);
