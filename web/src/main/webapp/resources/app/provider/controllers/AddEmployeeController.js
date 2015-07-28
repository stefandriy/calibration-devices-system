angular
    .module('employeeModule')
    .controller('AddEmployeeController', [ '$rootScope', '$scope', '$modalInstance', '$log',  '$state', '$http', 'UserService', 'AddressServiceProvider',

        function ($rootScope, $scope, $modalInstance,  $log, $state, $http, userService, addressServiceProvider) {
            var organizationTypeProvider = false;
            var organizationTypeCalibrator = false;
            var organizationTypeVerificator = false;
            var employeeData = {};



            userService.isAdmin()
                .success(function (response) {
                    var includeCheckBox = false;
                    var thereIsAdmin = 0;
                    var roles = response + '';
                    var role = roles.split(',');
                    for (var i = 0; i < role.length; i++) {
                        if (role[i] === 'PROVIDER_ADMIN' || role[i] === 'CALIBRATOR_ADMIN' || role[i] === 'STATE_VERIFICATOR_ADMIN')
                            thereIsAdmin++;
                    }
                    if (thereIsAdmin === 0) {
                        $scope.accessLable = true;
                    } else {
                        $scope.verificator = true;
                    }
                    if (thereIsAdmin === 1) {
                        if (role[0] === 'PROVIDER_ADMIN')
                            organizationTypeProvider = true;
                        if (role[0] === 'CALIBRATOR_ADMIN')
                            organizationTypeCalibrator = true;
                        if (role[0] === 'STATE_VERIFICATOR_ADMIN')
                            organizationTypeVerificator = true;
                    }
                    if (thereIsAdmin > 1) {
                        $scope.showListOfOrganization = true;
                        for (var i = 0; i < role.length; i++) {
                            if ((role[0] === 'PROVIDER_ADMIN' && role[1] === 'CALIBRATOR_ADMIN') ||
                                (role[0] === 'CALIBRATOR_ADMIN' && role[1] === 'PROVIDER_ADMIN'))
                                $scope.showListOfOrganizationChousenOne = true;
                            if ((role[0] === 'STATE_VERIFICATOR_ADMIN' && role[1] === 'CALIBRATOR_ADMIN') ||
                                (role[0] === 'CALIBRATOR_ADMIN' && role[1] === 'STATE_VERIFICATOR_ADMIN'))
                                $scope.showListOfOrganizationChousenTwo = true;
                            }
                    }
                });
            $scope.chouse = function (selectedEmployee) {
                var employee = selectedEmployee + '';
                var resaultEmployee = employee.split(',');
                for (var i = 0; i < resaultEmployee.length; i++) {
                    if (resaultEmployee[i] === 'provider') {
                        organizationTypeProvider = true;
                    }
                    if (resaultEmployee[i] === 'calibrator') {
                        organizationTypeCalibrator = true;
                    }
                    if (resaultEmployee[i] === 'verificatot') {
                        organizationTypeVerificator = true
                    }
                }
            }

            $scope.regions = null;
            $scope.districts = [];
            $scope.localities = [];
            $scope.streets = [];
            $scope.buildings = [];

            /**
             * Resets employee form
             */

            $scope.resetEmployeeForm = function() {
                $scope.$broadcast('show-errors-reset');
                $scope.usernameValidation = null;
                $scope.employeeFormData = null;
            };

                /**
                 * Calls resetOrganizationForm after the view loaded
                 */
                   $scope.resetEmployeeForm();


                /**
                 * Validates
                 */

                $scope.checkFirstName = function(caseForValidation) {
                    switch (caseForValidation) {
                        case ('firstName') :
                            var firstName = $scope.employeeFormData.firstName;
                            if (firstName == null) {
                            } else if (/^[А-Я]{1}[а-я]{3,10}/.test(firstName)) {
                                validator('firstName', false);
                            } else {
                                validator('firstName', true);
                            }
                            break;
                        case ('lastName') :
                            var lastName = $scope.employeeFormData.lastName;
                            if (lastName == null) {
                            } else if (/^[А-Я]{1}[а-я]{3,10}/.test(lastName)) {
                                validator('lastName', false);
                            } else {
                                validator('lastName', true);
                            }
                            break;
                        case ('middleName') :
                            var middleName = $scope.employeeFormData.middleName;
                            if (middleName == null) {
                            } else if (/^[А-Я]{1}[а-я]{3,10}/.test(middleName)) {
                                validator('middleName', false);
                            } else {
                                validator('middleName', true);
                            }
                            break;
                        case ('phone') :
                            var phone = $scope.employeeFormData.phone;
                            if (phone == null) {
                            } else if (/[0-9]{4,11}/.test(phone)) {
                                validator('phone', false);
                            } else {
                                validator('phone', true);
                            }
                            break;
                        case ('email') :
                            var email = $scope.employeeFormData.email;
                            if (email == null) {
                            } else if (/[0-9a-z_]+@[0-9a-z_]+\.[a-z]{2,5}/i.test(email)) {
                                validator('email', false);
                            } else {
                                validator('email', true);
                            }
                            break;
                        case ('login') :
                            var username = $scope.employeeFormData.username;
                            if (username == null) {
                            } else if (/^[a-z0-9_-]{3,16}$/.test(username)) {
                                isUsernameAvailable(username)
                            } else {
                                validateUsername(false,
                                    'К-сть символів не повинна бути меншою за 3\n і більшою за 16 ');
                            }
                            break;
                    }
                }

                function validator(caseForValidation, isValid) {

                    switch (caseForValidation) {
                        case 'firstName':
                            $scope.firstNameValidation = {
                                isValid: isValid,
                                css: isValid ? 'has-error' : 'has-success'
                            }
                            break;
                        case 'lastName':
                            $scope.lastNameValidation = {
                                isValid: isValid,
                                css: isValid ? 'has-error' : 'has-success'
                            }
                            break;
                        case 'middleName':
                            $scope.middleNameValidation = {
                                isValid: isValid,
                                css: isValid ? 'has-error' : 'has-success'
                            }
                            break;
                        case 'phone':
                            $scope.phoneNumberValidation = {
                                isValid: isValid,
                                css: isValid ? 'has-error' : 'has-success'
                            }
                            break;
                        case 'email':
                            $scope.emailValidation = {
                                isValid: isValid,
                                css: isValid ? 'has-error' : 'has-success'
                            }
                            break;
                    }
                }

                /**
                 * Custom username field validation. Shows error
                 * message in view if username isn't validated.
                 *
                 * @param isValid
                 * @param message
                 */
                function validateUsername(isValid, message) {

                    $scope.usernameValidation = {
                        isValid: isValid,
                        css: isValid ? 'has-success' : 'has-error',
                        message: isValid ? undefined : message
                    }
                }

                /**
                 * Checks whereas given username is available to use
                 * for new user
                 *
                 * @param username
                 */
                function isUsernameAvailable(username) {
                    userService.isUsernameAvailable(username).then(
                        function (data) {
                            validateUsername(data.data,
                                'Такий логін вже існує');
                        })
                }

                /**
                 * Finds all regions
                 */
                function initFormData() {
                    if (!$scope.regions) {
                        addressServiceProvider.findAllRegions().then(
                            function (data) {
                                $scope.regions = data.data;
                            });
                    }
                }

                initFormData();

                /**
                 * Finds districts in a given region.
                 *
                 * @param regionId
                 *            to identify region
                 */
                $scope.onRegionSelected = function (regionId) {
                    addressServiceProvider
                        .findDistrictsByRegionId(regionId)
                        .then(function (data) {
                            $scope.districts = data.data;
                        });
                };

                /**
                 * Finds localities in a given district.
                 *
                 * @param districtId
                 *            to identify district
                 */
                $scope.onDistrictSelected = function (districtId) {
                    addressServiceProvider.findLocalitiesByDistrictId(
                        districtId).then(function (data) {
                            $scope.localities = data.data;
                        });
                };

                /**
                 * There are no DB records for this methods.
                 *
                 * Finds streets in a given locality.
                 *
                 * @param localityId
                 *            to identify locality
                 */
                //$scope.onLocalitySelected = function (localityId) {
                //    addressServiceProvider.findStreetsByLocalityId(
                //        localityId).then(function (data) {
                //            $scope.streets = data.data;
                //        });
                //};

                /**
                 * Finds buildings in a given street.
                 *
                 * @param streetId
                 *            to identify street
                 */
                //$scope.onStreetSelected = function (streetId) {
                //    addressServiceProvider
                //        .findBuildingsByStreetId(streetId)
                //        .then(function (data) {
                //            $scope.buildings = data.data;
                //        });
                //};


                function addressFormToOrganizationForm() {
                  $scope.employeeFormData.region = $scope.employeeFormData.region.designation;
                   $scope.employeeFormData.district = $scope.employeeFormData.district.designation;
                    $scope.employeeFormData.locality = $scope.employeeFormData.locality.designation;
                    $scope.employeeFormData.street = $scope.employeeFormData.street;
                    $scope.employeeFormData.building = $scope.employeeFormData.building;
                   $scope.employeeFormData.flat = $scope.employeeFormData.flat;
                }

                /**
                 * Validates organization form before saving
                 */
                function retranslater(){
                    employeeData = {
                        firstName : $scope.employeeFormData.firstName,
                        lastName: $scope.employeeFormData.lastName,
                        middleName: $scope.employeeFormData.middleName,
                        phone : $scope.employeeFormData.phone,
                        email : $scope.employeeFormData.email,
                        username : $scope.employeeFormData.username,
                        password : $scope.employeeFormData.password,
                        userRoles : [],
                    }


                    employeeData.address = {
                        region: $scope.employeeFormData.region,
                        district: $scope.employeeFormData.district,
                        locality: $scope.employeeFormData.locality,
                        street: $scope.employeeFormData.street,
                        building: $scope.employeeFormData.building,
                        flat: $scope.employeeFormData.flat,
                    }

                    if (organizationTypeProvider === true) {
                        employeeData.userRoles.push('PROVIDER_EMPLOYEE');
                    }
                    if (organizationTypeCalibrator === true) {
                        employeeData.userRoles.push('CALIBRATOR_EMPLOYEE');
                    }
                    if (organizationTypeVerificator === true) {
                        employeeData.userRoles.push('STATE_VERIFICATOR_EMPLOYEE');
                    }

                }

                $scope.onEmployeeFormSubmit = function () {

                    $scope.$broadcast('show-errors-check-validity');
                    if( !$scope.firstNameValidation.isValid && !$scope.lastNameValidation.isValid
                    && !$scope.middleNameValidation.isValid && !$scope.emailValidation.isValid) {
                        addressFormToOrganizationForm();
                        retranslater();
                        saveEmployee();
                    } else {
                        $scope.incorrectValue =true;
                    }
                        };


                        /**
                         * Saves new organization from the form in database.
                         * If everything is ok then resets the organization
                         * form and updates table with organizations.
                         */
                        function saveEmployee() {
                            userService.saveUser(
                                employeeData).then(
                                function (data) {
                                    if (data.status == 201) {
                                        $scope.closeModal();
                                        $scope.resetEmployeeForm();

                                    }else{
                                        alert('Error');
                                    }
                                });
                        };


                     /* Closes the modal window for adding new
                     * organization.
                     */
                    $rootScope.closeModal = function () {
                        $modalInstance.close();
                    };


                        $scope.checkPasswords = function () {
                            var first = $scope.employeeFormData.password;
                            var second = $scope.employeeFormData.rePassword;
                            $log.info(first);
                            $log.info(second);
                            var isValid = false;
                            if (first != second) {
                                isValid = true;
                                }
                            $scope.passwordValidation = {
                                isValid: isValid,
                                css: isValid ? 'has-error' : 'has-success'
                            }
                        };


                    //   $log.info(employeeFormData);



        }]);
