angular
    .module('welcomeModule')/**
 * Created by Loc on 26.07.2015.
 */
    .directive('chosen', function ($timeout) {
    var linker = function (scope, element, attrs) {

        scope.$watch('chosenModelList', function (value) {
            element.trigger('chosen:updated');
        });

        scope.$watch('chosenModel', function (value) {
            element.trigger('chosen:updated');
        });

       scope.$watch(function(){
            return element[0].length;
        }, function(newvalue, oldvalue){
            if (newvalue !== oldvalue) {
                element.trigger("chosen:updated");
            }
        });

        scope.$watch(attr.ngModel, function(newVal, oldVal) {
            if (newVal !== oldVal) {
                stopLoading();
            }
        });
        element.chosen({
            display_selected_options: false,
            max_selected_options: scope.maxSelectedOptions || 3,
            disable_search_threshold: 1000
        });
    };

    return {
        restrict: 'A',
        scope: {
            'chosenModelList': '=',
            'chosenModel': '=',
            'maxSelectedOptions': '='
        },
        link: linker
    };
})