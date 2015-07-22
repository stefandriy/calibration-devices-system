/**
 * Created by MAX on 18.07.2015.
 */
angular
    .module('employeeModule')
       .controller('GraficEmployeeProvider', ['$scope', '$log',
        function ($scope, $log) {
            $scope.displayGrafic = function (graficData) {
                $log.debug(graficData)
                if (graficData.length > 0) {
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
                            categories: graficData[0].listMonths
                        },
                        yAxis: {
                            title: {
                                text: 'Кількість виконаних заявок'
                            },
                            plotLines: [{
                                value: 0,
                                width: 1,
                                color: '#808080'
                            }]
                        },
                        tooltip: {
                            valueSuffix: ''
                        },
                        legend: {
                            layout: 'vertical',
                            align: 'right',
                            verticalAlign: 'middle',
                            borderWidth: 0
                        },
                        series: graficData
                    }

                } else {
                    $scope.chartConfig = {
                        title: {
                            text: 'Наразі немає інформації про працівників',
                            x: -20 //center
                        }
                    }
                }
            }
        }
 ] );