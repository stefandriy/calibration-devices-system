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
                        options: {
                            chart: {
                                type: 'column'
                            },
                            title: {
                                text: 'Продуктивність працівників',
                                x: -20 //center
                            },
                            subtitle: {
                                text: 'Графік з кількості відпрацьованих заявок працівниками',
                                x: -20
                            },

                            xAxis: {
                                categories: graficData[0].listMonths,
                                crosshair: true
                            },
                            yAxis: {
                                min: 0,
                                title: {
                                    text: 'Кількість виконаних заявок '
                                }
                            },
                            tooltip: {
                                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                                '<td style="padding:0"><b>{point.y:.1f} опрацьовано заявок</b></td></tr>',
                                footerFormat: '</table>',
                                shared: true,
                                useHTML: true
                            },
                            plotOptions: {
                                column: {
                                    pointPadding: 0.2,
                                    borderWidth: 0
                                }
                            },
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


