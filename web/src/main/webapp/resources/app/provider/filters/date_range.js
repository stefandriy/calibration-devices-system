angular
    .module('employeeModule')
    .filter('daterange', function () {
        return function (items, date) {
            var result = [];
            $log.debug(items);
            // date filters
            var startDate = (date.startDate && !isNaN(Date.parse(date.startDate))) ? Date.parse(date.startDate) : 0;
            var end_date = (date.endDate && !isNaN(Date.parse(date.endDate))) ? Date.parse(date.endDate) : new Date().getTime();

            // if the conversations are loaded
            if (items && items.length > 0) {
                angular.each(items, function (index, item) {
                    var certainDate = new Date(item.initialDate); //add exception

                    if (certainDate >= startDate && certainDate <= end_date) {
                        result.push(item);
                    }
                });

                return result;
            }
        };
    });