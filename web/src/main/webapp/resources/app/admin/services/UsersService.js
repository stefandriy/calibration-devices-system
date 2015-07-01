angular
    .module('adminModule')
    .factory('UsersService', function ($http) {
        return {
            getPage: function (pageNumber, itemsPerPage, search) {
                var url = '/admin/users/' + pageNumber + '/' + itemsPerPage;

                if (search != null && search != undefined && search != "")
                    url += '/' + search;

                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            }
        }

        var data = [{name: "Moroni", age: 50, role: 'Administrator'},
            {name: "Tiancum", age: 43, role: 'Administrator'},
            {name: "Jacob", age: 27, role: 'Administrator'},
            {name: "Nephi", age: 29, role: 'Moderator'},
            {name: "Enos", age: 34, role: 'User'},
            {name: "Nephi", age: 29, role: 'User'},
            {name: "Enos", age: 34, role: 'User'}];

        $scope.tableParams = new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            groupBy: 'role',
            total: data.length,
            getData: function($defer, params) {
                var orderedData = params.sorting() ?
                    $filter('orderBy')(data, $scope.tableParams.orderBy()) :
                    data;

                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });

    });