angular
    .module('employeeModule')
    .factory('DocumentService', function ($http) {
        return {
            getDocument: function (documentType, verificationId, fileFormat) {
                console.log(documentType);
                return getData('doc/' + documentType + "/" + verificationId + "/" + fileFormat);
            }
        };

        function getData(url) {
            return $http.get(url, {responseType: "document"})
                .success(function (data) {
                return data;})
                .error(function (err) {
                return err;
                });
        }
    });
