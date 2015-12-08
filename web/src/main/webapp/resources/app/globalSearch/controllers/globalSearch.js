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
                    model: '=',
                    placeholder: '@'
                },
                templateUrl: 'resources/app/globalSearch/views/cds-global-search.html',
                link: function ($scope, $element, $attrs, $filter) {
                    $scope.selectedParams = [];
                    $scope.selectedValues = [];
                    $scope.mainButton = false;
                    $scope.newSearchParamAvailable = ($scope.params.length - $scope.selectedParams.length > 0);
                    $scope.selectedValues = [];
                    $scope.clickMainButton = function () {
                        $scope.mainButton = !$scope.mainButton;
                        $scope.selectedParams.forEach(function (entry) {
                            if (!entry.hasOwnProperty('params')) {
                                $scope.selectedParams.splice($scope.selectedParams.indexOf(entry), 1);
                            }
                        });
                        $scope.clearModel();
                        $scope.clearParams();
                    };
                    $scope.newSearchParam = function () {
                        $scope.selectedParams.push({});
                        $scope.selectedKey = '';
                        $scope.newSearchParamAvailable = ($scope.params.length - $scope.selectedParams.length > 0);
                        $scope.myDatePicker = {};
                        $scope.myDatePicker.pickerDate = {
                            startDate: moment(),
                            endDate: moment()
                        };
                    };
                    $scope.setParamsToModel = function () {
                        for (var i = 0; i < $scope.selectedParams.length; ++i) {
                            var modelMapIndex = $scope.model.map(function (e) {
                                return e.key
                            }).indexOf($scope.selectedParams[i].params.key);
                            if ($scope.selectedParams[i].type == "Date") {
                                $scope.setDateToSelectedParam(i);
                            }
                            if (modelMapIndex >= 0) {
                                $scope.model[modelMapIndex].value = $scope.selectedParams[i].params.value;

                            } else {
                                $scope.model.push({
                                    key: $scope.selectedParams[i].params.key,
                                    value: $scope.selectedParams[i].params.value,
                                    type: $scope.selectedParams[i].params.type,
                                    name: $scope.selectedParams[i].params.name
                                });
                            }
                        }
                        $scope.clearParams();
                        $scope.clearModel();
                        $scope.clearSelectedParams();
                    };
                    $scope.deleteSearchParam = function (index) {
                        $scope.model.splice($scope.model.indexOf($scope.selectedParams[index]), 1);
                        $scope.selectedParams.splice(index, 1);
                        $scope.newSearchParamAvailable = ($scope.params.length - $scope.selectedParams.length > 0);
                        $scope.clearParams();
                    };
                    $scope.clearAllSearchParams = function () {
                        while ($scope.selectedParams.length > 0) {
                            $scope.deleteSearchParam(0);
                        }
                        $scope.clearModel();
                    };
                    $scope.clearModel=function(){
                        $scope.model.forEach(function(model){
                            var paramIndex=$scope.params.map(function (e) {
                                return e.key
                            }).indexOf(model.key);
                            var selectedParamIndex=$scope.selectedParams.map(function (e) {
                                return e.params.key
                            }).indexOf(model.key);
                            if(paramIndex>0&&selectedParamIndex<0){
                                $scope.model.splice($scope.model.indexOf(model),1)
                            }
                        });
                    };
                    $scope.clearSelectedParams=function(){
                        $scope.selectedParams.forEach(function(param){
                            var modelIndex=$scope.model.map(function (e) {
                                return e.key
                            }).indexOf(param.params.key);
                            if(modelIndex<0){
                                param.params.value={};
                            }

                        });
                    };
                    $scope.clearParams=function(){
                        $scope.params.forEach(function(param){
                            var paramIndex=$scope.selectedParams.map(function (e) {
                                return e.params.key
                            }).indexOf(param.key);
                            if(paramIndex<0){
                                param.value='';
                            }
                        });

                    };
                    $scope.allIsSelected=function(status){
                        var isSelected=status;
                      $scope.selectedParams.forEach(function(param){
                            if(!param.hasOwnProperty('params')||!param.params.hasOwnProperty("key")){
                                isSelected=false;
                            }
                        });
                        return isSelected;
                    };
                    $scope.isFiltered = function (filterList) {
                        return function (param) {
                            var i = (filterList.map(function (e) {
                                if (e.hasOwnProperty("params")) {
                                    return e.params.key;
                                }
                            }).indexOf(param.key));
                            return i < 0;

                        }
                    };
                    $scope.markParamsAsSelected = function () {
                        for (var i = 0; i < $scope.selectedParams.length; i++) {
                            for (var j = 0; j < $scope.params.length; j++) {
                                scope.params[j].isSelected = $scope.selectedParams[i].key == $scope.params
                            }
                        }
                    };

                    $scope.formats = ['DD-MM-YYYY', 'YYYY/MM/DD', 'DD.MM.YYYY', 'shortDate'];
                    $scope.opts = {
                        autoUpdateInput: false,
                        format: $scope.formats[2],
                        showDropdowns: true,
                        minDate: '01-01-2013'
                    };
                    $scope.isDateDefault = function () {
                        return $scope.myDatePicker.pickerDate == null;
                    };
                    $scope.clearDate = function () {
                        $scope.myDatePicker.pickerDate = null;
                        $scope.selectedValue = '';
                    };
                    $scope.setDateToSelectedParam = function (index) {
                        $scope.selectedParams[index].value = [];
                        $scope.selectedParams[index].value.push($scope.myDatePicker.pickerDate.startDate.format($scope.formats[2]), $scope.myDatePicker.pickerDate.endDate.format($scope.formats[2]));
                    };
                    //$('input[name="datefilter"]').on('apply.daterangepicker', function (ev, picker) {
                    //    $(this).val(picker.startDate.format($scope.formats[2]) + ' - ' + picker.endDate.format($scope.formats[2]));
                    //});
                    //$('input[name="datefilter"]').on('cancel.daterangepicker', function (ev, picker) {
                    //    $(this).val('');
                    //});
                }

            };
        });
})();