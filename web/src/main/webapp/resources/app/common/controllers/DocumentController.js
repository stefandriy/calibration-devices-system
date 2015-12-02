angular
    .module('employeeModule')
    .controller('DocumentController', ['$rootScope', '$scope', '$modal', '$http', 'DocumentService', function ($rootScope, $scope, $http, $modal, documentService) {
        $scope.downloadDocument = function (documentType, verificationId, fileFormat) {
            var url = "doc/" + documentType + "/" + verificationId + "/" + fileFormat;
            location.href = url;
        }

        $scope.printDocument = function (verification) {
            var documentType = verification.status == 'TEST_OK' ? 'VERIFICATION_CERTIFICATE' : 'UNFITNESS_CERTIFICATE';
            var url = "doc/" + documentType + "/" + verification.id + "/pdf";
            //var url = "https://cs7052.vk.me/c612420/u32810625/docs/7ed84187fc53/merged.pdf?extra=5my7LEiXYJzBsv8Q2gsL6pyvGbaXYSyuAPxS7fRdofFCwczzWbHznaJnKB8gORPMYL_tw4ChTZJpyu5q8iMVCnN1Gx9_mJUu";
            //var doc =  documentService.getDocument(documentType, verification.id, "pdf");
            var frame = document.getElementById("frame");
            //frame.src = url;
            //console.log(frame);
            var innerDoc = frame.contentWindow.document;
            innerDoc.open();
            innerDoc.write("<embed type='application/pdf' width='100%' src='" + url + "'><p>bla-bla-bla...</p></embed>");
            innerDoc.close();
            frame.contentWindow.document.execCommand('print', false, null);
            //frame.contentWindow.postMessage("message", "*");

            //var xhttp = new XMLHttpRequest();
            //xhttp.onreadystatechange = function() {
            //    if (xhttp.readyState == 4 && xhttp.status == 200) {
            //        var toPrint = xhttp.response;
            //        //var origin = document.body.innerHTML;
            //        //var iframe = document.getElementById("pdf");
            //        //var content = "bla-bla-bla";
            //        frame.srcdoc = toPrint;
            //        console.dir(toPrint);
            //        //window.print();
            //        //document.body.innerHTML = origin;
            //        //frame.contentWindow.document.execCommand('print', false, null);
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
