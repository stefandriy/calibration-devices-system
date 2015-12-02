/**
 * Created by Pavlo on 26.11.2015.
 */
(function () {
    'use strict';
    angular.module('globalSearch', [])
        .directive('cdsGlobalSearch', function () {
            return {
                restrict: 'E',
                scope: {
                    params: '=',
                    model : '=',
                    placeholder: '@'
                },
                templateUrl: 'resources/app/globalSearch/views/cds-global-search.html',
                link: function ($scope, $element, $attrs) {
                    $scope.mainButton = false;
                    $scope.clickMainButton = function () {
                        $scope.mainButton = ! $scope.mainButton;
                        $scope.selected={};
                        $scope.selectedValue='';
                        $scope.paramsAddition.$setPristine();
                    };
                    $scope.addSearchParam = function (selectedParam, value) {
                        $scope.model.push({
                            key: selectedParam.key,
                            value: value,
                            type: selectedParam.type
                        });
                    };
                    $scope.submit = function () {
                        if($scope.selected.type=='Date'){
                            $scope.setDateToSelectedValue();
                        }
                        $scope.model.push({
                            key: $scope.selected.key,
                            value: $scope.selectedValue,
                            type: $scope.selected.type
                        });
                        $scope.selected={};
                        $scope.selectedValue='';
                        $scope.paramsAddition.$setPristine();
                        $scope.myDatePicker.pickerDate=null;
                    }
                    $scope.deleteSearchParam = function (searchParamToDelete) {
                        $scope.model.splice($scope.model.indexOf(searchParamToDelete), 1);
                    };
                    $scope.editSearchParam = function (selectedParam, newValue) {
                        scope.model.indexOf(selectedParam).value = newValue;
                    };

//datepickerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
                    $scope.myDatePicker = {};
                    $scope.myDatePicker.pickerDate = null;
                    $scope.opts = {
                        format: 'DD-MM-YYYY',
                        showDropdowns: true,
                        minDate:'01-01-2013'
                    };
                    $scope.isDateDefault=function(){
                        return $scope.myDatePicker.pickerDate==null;
                    };
                    $scope.clearDate=function(){
                        $scope.myDatePicker.pickerDate=null;
                        $scope.selectedValue='';
                    };
                    $scope.setDateToSelectedValue=function(){
                        $scope.selectedValue=[];
                        $scope.selectedValue.push($scope.myDatePicker.pickerDate.startDate,$scope.myDatePicker.pickerDate.endDate);
                    };

//datepickerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
                }
            };
        });
})();