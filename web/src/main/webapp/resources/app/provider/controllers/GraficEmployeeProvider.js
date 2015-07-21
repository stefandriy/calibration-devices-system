/**
 * Created by MAX on 18.07.2015.
 */
angular
    .module('employeeModule')
       .controller('GraficEmployeeProvider', ['$scope', '$log', '$modalInstance', 'grafic',
        function ($scope, $log, $modalInstance, grafic) {
            $log.debug(grafic.data)
            $scope.chartConfig = {

                title: {
                    text: 'Продуктивність працівників',
                    x: -20 //center
                },
                subtitle: {
                    text: 'Графік з кількості відпрацьованих заявок працівниками',
                    x: -20
                },
                xAxis: {
                    categories: grafic.data[0].listMonths
                },
                yAxis: {
                    title: {
                        text: 'Кількість виконаних заявок)'
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }]
                },
                tooltip: {
                    valueSuffix: '°C'
                },
                legend: {
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'middle',
                    borderWidth: 0
                },
                series: grafic.data
            }


        }
 ] );