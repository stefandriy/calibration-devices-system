angular
    .module('employeeModule')
    .controller('DocumentController', ['$rootScope', '$scope', '$modal', 'DocumentService', function ($rootScope, $scope, $modal, documentService) {
        $scope.downloadDocument = function (documentType, verificationId, fileFormat) {
            var url = "doc/" + documentType + "/" + verificationId + "/" + fileFormat;
            location.href = url;
        }

        $scope.printDocument = function (verification) {
            var documentType = verification.status == 'TEST_OK' ? 'VERIFICATION_CERTIFICATE' : 'UNFITNESS_CERTIFICATE';
            //var url = "doc/" + documentType + "/" + verification.id + "/html";
            //var xhttp = new XMLHttpRequest();
            //xhttp.onreadystatechange = function() {
            //    if (xhttp.readyState == 4 && xhttp.status == 200) {
            //        var toPrint = xhttp.responseText;
            //        var origin = document.body.innerHTML;
            //        document.body.innerHTML = toPrint;
            //        console.dir(toPrint);
            //        window.print();
            //        document.body.innerHTML = origin;
            //    }
            //};
            //xhttp.open("GET", url, true);
            //xhttp.send();
        }

        $scope.editDocument = function (verification) {
            var documentType = verification.status == 'TEST_OK' ? 'VERIFICATION_CERTIFICATE' : 'UNFITNESS_CERTIFICATE';
            //var url = "doc/" + documentType + "/" + verification.id + "/html";
            //$modal.open({
            //    animation: true,
            //    templateUrl: url,
            //    size: 'lg'
            //});
            //var xhttp = new XMLHttpRequest();
            //xhttp.onreadystatechange = function() {
            //    if (xhttp.readyState == 4 && xhttp.status == 200) {
            //        var toPrint = xhttp.responseText;
            //        //var origin = document.body.innerHTML;
            //        //document.body.innerHTML = toPrint;
            //        //console.dir(toPrint);
            //        //window.print();
            //        //document.body.innerHTML = origin;
            //
            //        $modal.open({
            //            animation: true,
            //            templateUrl: toPrint,
            //            size: 'lg'
            //        });
            //    }
            //};
            //xhttp.open("GET", url, true);
            //xhttp.send();
        }
    }]);
