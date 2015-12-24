/**
 * Created by Pavlo on 26.11.2015.
 */

(function () {
    'use strict';
    angular.module('globalSearchModule')
        .factory('globalSearchService', function ($http) {
            return {
                getAllFilters: function (locationUrl) {
                    return getData(locationUrl);
                },
                saveFilter: function (locationUrl, filter) {
                    return sendData("add/" + locationUrl, filter);
                },
                deleteFilter: function (locationUrl, filterName) {
                    return sendData("delete/" + locationUrl, filterName);
                },
                updateFilter: function (locationUrl, filter) {
                    return sendData("update/" + locationUrl, filter);
                }
            };
            function getData(url) {
                return $http.get('globalSearch/' + url).success(function (result) {
                    return result;
                }).error(function (err) {
                    return err;
                });
            }

            function sendData(url, data) {
                return $http.post('globalSearch/' + url, data)
                    .then(function (result) {
                        return result.status;
                    });
            }
        })
        .directive('cdsGlobalSearch', function ($location, globalSearchService) {
            return {
                restrict: 'E',
                scope: {
                    params: '=',
                    model: '=',
                    placeholder: '@'
                },
                templateUrl: 'resources/app/globalSearch/views/cds-global-search.html',
                link: function ($scope, $element, $attrs, $filter) {
                    //var locationUrl=$location.path().slice(1);
                    var locationUrl = $location.path().replace(/\//g, '-').slice(1);
                    //globalSearchService.getAllFilters();
                    $scope.selected = {};
                    $scope.selectedParams = [];
                    $scope.selectedValues = [];
                    $scope.savedFilters = [];
                    $scope.selectedSavedFilter = {};
                    //$scope.getSavedFilters();
                    $scope.mainButton = false;
                    $scope.newSearchParamAvailable = ($scope.params.length - $scope.selectedParams.length > 0);
                    //$scope.selectedValues = [];
                    $scope.clickMainButton = function () {
                        $scope.mainButton = !$scope.mainButton;
                        $scope.reloadSelectedParams();
                        $scope.newSearchParamAvailable = ($scope.params.length - $scope.selectedParams.length > 0);
                        $scope.getAllSavedFilters();
                        //$scope.clearSelectedParams();
                        ////$scope.clearModel();
                        //$scope.clearParams();
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
                        //$scope.model.splice($scope.model.indexOf($scope.selectedParams[index]), 1);
                        $scope.selectedParams.splice(index, 1);
                        $scope.newSearchParamAvailable = ($scope.params.length - $scope.selectedParams.length > 0);
                        $scope.clearParams();
                    };
                    $scope.clearAllSearchParams = function () {
                        $scope.selectedParams = [];
                        $scope.selected = {};
                        $scope.newSearchParamAvailable = ($scope.params.length - $scope.selectedParams.length > 0);
                        $scope.clearModel();
                    };
                    $scope.clearModel = function () {
                        for (var i = $scope.model.length - 1; i >= 0; i--) {
                            var paramIndex = $scope.params.map(function (e) {
                                return e.key
                            }).indexOf($scope.model[i].key);
                            var selectedParamIndex = $scope.selectedParams.map(function (e) {
                                return e.params.key
                            }).indexOf($scope.model[i].key);
                            if (paramIndex >= 0 && selectedParamIndex < 0) {
                                $scope.model.splice(i, 1)
                            }
                        }
                    };
                    $scope.clearSelectedParams = function () {
                        for (var i = $scope.selectedParams.length - 1; i >= 0; i--) {
                            if (!$scope.selectedParams[i].hasOwnProperty('params')) {
                                $scope.selectedParams.splice(i, 1);
                            }
                        }
                        $scope.selectedParams.forEach(function (param) {
                            var modelIndex = $scope.model.map(function (e) {
                                return e.key
                            }).indexOf(param.params.key);
                            if (modelIndex < 0) {
                                param.params.value = '';
                            }

                        });
                    };
                    $scope.clearParams = function () {
                        $scope.params.forEach(function (param) {
                            var paramIndex = $scope.selectedParams.map(function (e) {
                                if (e.hasOwnProperty('params')) {
                                    return e.params.key
                                } else {
                                    return -1;
                                }
                            }).indexOf(param.key);
                            if (paramIndex < 0) {
                                param.value = '';
                            }
                        });

                    };
                    $scope.reloadSelectedParams = function () {
                        $scope.selectedParams = [];
                        $scope.model.forEach(function (param) {
                            var paramIndex = $scope.params.map(function (e) {
                                return e.key;
                            }).indexOf(param.key);
                            if (paramIndex >= 0) {
                                if ($scope.params[paramIndex].hasOwnProperty('options')) {
                                    $scope.selectedParams.push({
                                        params: {
                                            key: param.key,
                                            name: param.name,
                                            type: param.type,
                                            value: param.value,
                                            options: $scope.params[paramIndex].options
                                        }
                                    });
                                }
                                else {
                                    $scope.selectedParams.push({
                                        params: {
                                            key: param.key,
                                            name: param.name,
                                            type: param.type,
                                            value: param.value
                                        }
                                    });
                                }
                            }
                        });
                        $scope.clearModel();
                        $scope.clearSelectedParams();
                        $scope.clearParams();
                    };
                    $scope.allIsSelected = function (status) {
                        var isSelected = status;
                        $scope.selectedParams.forEach(function (param) {
                            if (!param.hasOwnProperty('params') || !param.params.hasOwnProperty("key")) {
                                isSelected = false;
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
                    $scope.tagTransform = function (newTag){
                        $scope.savedFilters.push({
                            name: newTag,
                            filter: $scope.selectedParams
                        });
                    };
                    $scope.getAllSavedFilters = function () {
                        globalSearchService.getAllFilters(locationUrl)
                            .success(function (result) {
                                $scope.savedFilters = [];
                                for (var i = 0; i < result.length; i++) {
                                    $scope.savedFilters.push({
                                        name: result[i].name,
                                        filter: JSON.parse([result[i].filter])
                                    })
                                }
                            }, function (result) {
                                $log.debug('error fetching data:', result);
                            });
                    };
                    $scope.getAllSavedFilters();
                    $scope.saveFilter = function () {
                        if ($scope.selectedParams.length > 0) {
                            var filter = JSON.stringify($scope.selectedParams);
                            var newFilter = {
                                name: $scope.selected.SavedFilter.name,
                                filter: filter
                            };
                        }
                        if ($scope.savedFilters.map(function (e) {
                                return e.name
                            }).indexOf(selected.name) < 0) {
                            globalSearchService.saveFilter(locationUrl, newFilter);
                        }
                        else {
                            globalSearchService.updateFilter(locationUrl, newFilter);
                        }

                        $scope.getAllSavedFilters();
                    };
                    $scope.deleteFilter = function () {
                        globalSearchService.deleteFilter(locationUrl,$scope.selected.SavedFilter.name);
                    };
                    $scope.$watch('selected', function (newParam, oldParam) {
                        if ($scope.selected.hasOwnProperty('savedFilter')) {
                            //      $scope.model = $scope.selected.savedFilter.filter;
                            $scope.selectedParams=[];
                            for (var i = 0; i < $scope.selected.savedFilter.filter.length; i++) {
                                $scope.selectedParams.push({
                                    key: $scope.selected.savedFilter.filter[i].params.key,
                                    value: $scope.selected.savedFilter.filter[i].params.value,
                                    type: $scope.selected.savedFilter.filter[i].params.type,
                                    name: $scope.selected.savedFilter.filter[i].params.name
                                });
                            }
                        }
                    }, true);

                }


            };
        });
    //define(['services/globalSearchService'],function(){}
    //);
})();