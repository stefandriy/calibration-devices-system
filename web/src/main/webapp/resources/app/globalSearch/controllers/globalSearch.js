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
                        $scope.selectedParams.forEach(function(entry){
                            if(!entry.hasOwnProperty('params')) {
                                $scope.selectedParams.splice($scope.selectedParams.indexOf(entry), 1);
                            }
                        });
                        for(var param in $scope.selectedParams){
                            if(!param.hasOwnProperty('params')){
                                $scope.selectedParams.splice($scope.selectedParams.indexOf($scope.selectedParams[index]), 1);
                            }
                        }
                        //$scope.paramsAddition.$setPristine();
                    };
                    $scope.newSearchParam = function () {
                        $scope.selectedParams.push({});
                        $scope.selectedKey = '';
                        $scope.newSearchParamAvailable=($scope.params.length - $scope.selectedParams.length > 0);
                        //$scope.paramsAddition.paramSelect.$render();
                        $scope.myDatePicker={};
                        $scope.myDatePicker.pickerDate = {
                            startDate: moment(),
                            endDate: moment()
                        };
                    };
                    $scope.setParamsToModel = function () {
                        for (var i = 0; i < $scope.selectedParams.length; ++i) {
                            //param.params={};
                            if ($scope.selectedParams[i].type == "Date") {
                                $scope.setDateToSelectedParam(i);
                            }
                            $scope.model.push({
                                key: $scope.selectedParams[i].params.key,
                                value: $scope.selectedParams[i].params.value,
                                type: $scope.selectedParams[i].params.type
                            });
                        }
                    };
                    $scope.addSearchParam = function (key, value, type) {
                        $scope.model.push({
                            key: key,
                            value: value,
                            type: type
                        });
                    };
                    $scope.deleteSearchParam = function (index) {
                        $scope.model.splice($scope.model.indexOf($scope.selectedParams[index]), 1);
                        $scope.selectedParams.splice(index, 1);
                        $scope.newSearchParamAvailable=($scope.params.length - $scope.selectedParams.length > 0);
                    };
                    $scope.clearAllSearchParams = function () {
                        while ($scope.selectedParams.length>0){
                            $scope.deleteSearchParam(0);
                        }
                    };
                    $scope.isFiltered = function (filterList) {
                        return function (param) {
                                var i = (filterList.map(function (e) {
                                    if(e.hasOwnProperty("params")) {
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
                    $scope.getParams = function () {
                        return $scope.params;
                    };
                    $scope.getFilteredParamsList = function () {
                        var res = [];
                        //for(var param in $scope.params){
                        //    var a= $scope.selectedParams.map(function (e){return e.key}).indexOf(param.key);
                        //    if(a<0){
                        //     res.push(param);
                        //    }
                        //}
                        for (var i = 0; i < $scope.params.length; ++i) {
                            var a = $scope.selectedParams.map(function (e) {
                                return e.key
                            }).indexOf($scope.params[i].key);
                            if (a < 0) {
                                res.push($scope.params[i]);
                            }
                        }
                        return res;
                    };
                    $scope.setParamsToSelectedParams = function (index) {
                        $scope.markParamsAsSelected();
                        var paramsIndex = $scope.params.map(function (e) {
                            return e.key
                        }).indexOf($scope.selectedParams[index].key);
                        //if(selectedParamsIndex<0){
                        //    $scope.selectedParams.push($scope.params[paramsIndex]);
                        //} else {
                        //    $scope.selectedParams.splice()
                        //}
                        $scope.selectedParams[index] = $scope.params[paramsIndex];
                        // $scope.selectedParams[index].params=angular.$scope.params[paramsIndex];
                    };


                    //$scope.$watch('selectedValue', function(newValue, oldValue) {
                    //    //for(var param in selectedParams){
                    //    //    $scope.model.splice($scope.model.indexOf(param),1);
                    //    //}
                    //    $scope.submit();
                    //});
//datepickerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
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
                    $('input[name="datefilter"]').on('apply.daterangepicker', function (ev, picker) {
                        $(this).val(picker.startDate.format($scope.formats[2]) + ' - ' + picker.endDate.format($scope.formats[2]));
                    });
                    $('input[name="datefilter"]').on('cancel.daterangepicker', function (ev, picker) {
                        $(this).val('');
                    });
//datepickerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr

                }

            };
        });
})();