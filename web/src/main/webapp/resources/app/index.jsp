<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Language" content="uk,en,ru">
    <title>Виконавець послуг</title>
    <link href="/resources/assets/bower_components/ng-table/ng-table.css" rel="stylesheet">
    <link href="/resources/assets/bower_components/angular-loading-bar/build/loading-bar.css" rel="stylesheet">
    <link href="/resources/assets/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/assets/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link href="/resources/assets/bower_components/chosen/chosen.min.css" rel="stylesheet">
    <link href="/resources/assets/bower_components/bootstrap-daterangepicker/daterangepicker-bs3.css" rel="stylesheet">

    <link href="/resources/assets/bower_components/ui-select/dist/select.min.css" rel="stylesheet">
    <link href="/resources/assets/bower_components/select2/select2.css" rel="stylesheet">
    <link href="/resources/assets/bower_components/select2-bootstrap-css/select2-bootstrap.min.css" rel="stylesheet">

    <link href="/resources/assets/bower_components/angularjs-toaster/toaster.min.css" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="/resources/assets/bower_components/semantic/dist/semantic.min.css">

    <link href="/resources/assets/css/application-form-organization.css" rel="stylesheet">
    <link href="/resources/assets/css/provider.css" rel="stylesheet">
    <link href="/resources/assets/css/calibrator.css" rel="stylesheet">
    <%--<link href="/resources/assets/css/sb-admin-2.css" rel="stylesheet">--%>
    <%--<link href="/resources/assets/css/timeline.css" rel="stylesheet">--%>


    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
    <link href="/resources/assets/bower_components/bootstrap-switch/dist/css/bootstrap3/bootstrap-switch.css"
          rel="stylesheet">
    <link rel="stylesheet"
          href="/resources/assets/winmarkltd-BootstrapFormHelpers-0d89ab4/dist/css/bootstrap-formhelpers.min.css">
    <%--<link rel="stylesheet" href="/resources/assets/css/loader.css">--%>

    <style>
        [ng\:cloak]
		[ng-cloak],
        [data-ng-cloak],
        [x-ng-cloak],
        .ng-cloak,
        .x-ng-cloak {
            display: none !important;
        }
    </style>

</head>

<body id="employeeModule">

<div id="content" class="wrapper ng-cloak">

    <!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-static-top" role="navigation"
         ng-controller="TopNavBarControllerProvider">
        <div class="navbar-header">
            <a class="navbar-brand" style="color:honeydew">Централізована система повірки лічильників</a>
        </div>

        <div ng-controller="InternationalizationController">
            <ul class="nav navbar-nav">
                <li class="dropdown" dropdown on-toggle="toggled(open)">
                    <a class="dropdown-toggle" dropdown-toggle translate="LANG">
                        <span class="caret"></span>
                    </a>

                    <ul class="dropdown-menu">
                        <li ng-repeat="lang in languages">
                            <a ng-click="changeLanguage(lang.key)">{{ lang.name }}</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>

        <!-- Nav bar top right links -->
        <ul class="nav navbar-top-links navbar-right">
            <li class="dropdown" dropdown>
                <a class="dropdown-toggle" style="width:250px; padding: 10px 15px; text-align: right" dropdown-toggle>
                    <div>
                        <label  translate="{{employee.firstName}} {{employee.lastName}}"></label>
                        <i class="fa fa-user fa-fw"></i><i class="fa fa-caret-down"></i>
                    </div>
                </a>

                <ul class="dropdown-menu dropdown-user">
                    <div class="ui card" style="width:250px">
                        <div class="ui image center aligned">
                            <img src="/resources/assets/AdminLTE-master/img/User_icon.png"
                                 style="width: 250px;"
                                 alt="User Image"/>
                        </div>
                        <div class="content" style="padding:0;">
                            <div class="ui top attached secondary segment" style="padding:10px;">
                                <label class="userlabel" translate="{{employee.firstName}} {{employee.lastName}} {{employee.middleName}} <br/> ({{employee.username}})"></label>
                            </div>
                            <div class="ui vertical menu" style="margin-top:0">
                                <div class="link item middle center aligned">
                                    <a ui-sref="profile-info" translate="PROFILE"></a>
                                </div>
                                <div class="link item middle center aligned">
                                    <a translate="SETTINGS"></a>
                                </div>
                                <div class="link item middle center aligned">
                                    <a ng-click="logout()" translate="LOG_OUT"> </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </ul>
            </li>
        </ul>
    </nav>
    <!-- Sidebar -->

    <div class="row ng-cloak">
        <div class="col-md-3 menu_column">
            <div class="ui primary button" ng-class="{'toggle_button' : menuState}" ng-init="menuState = true"
                 ng-click="menuState = !menuState">
                <div ng-show="menuState"><i class="fa fa-chevron-left"
                                            style="font-size: 30px; vertical-align: middle"></i><span>Сховати</span>
                </div>
                <div ng-show="!menuState"><i class="fa fa-chevron-right"
                                             style="font-size: 30px;vertical-align: middle"></i><span>Показати</span>
                </div>
            </div>
        </div>
    </div>

    <div class="row ng-cloak">
        <div ng-class="{'col-md-3 menu_column' : menuState, '': !menuState}">
            <div class="ui visible sticky visible very wide sidebar" ng-hide="!menuState" role="navigation">
                <ul class="nav ui vertical menu">
                    <sec:authorize url="/provider">
                        <li ui-sref-active="active">
                            <a ui-sref="main-panel-provider"><i class="fa fa-home fa-fw"></i> Головна панель
                                (постачальник послуг)</a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/calibrator">
                        <li ui-sref-active="active">
                            <a ui-sref="main-panel-calibrator"><i class="fa fa-home fa-fw"></i> Головна панель
                                (вимірювальна лабораторія)</a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/verificator">
                        <li ui-sref-active="active">
                            <a ui-sref="main-panel-verificator"><i class="fa fa-home fa-fw"></i> Головна панель
                                (уповноважена повірочна лабораторія)</a>
                        </li>
                    </sec:authorize>


                    <sec:authorize url="/provider">
                        <li ui-sref-active="active" ng-controller="NotificationsControllerProvider">
                            <a ui-sref="new-verifications-provider" ng-click="reloadVerifications()">
                                <i class="fa fa-list-alt fa-fw"></i> Нові заявки (постачальник послуг)
                              <span class="ui teal label" ng-bind="countOfUnreadVerifications"
                                    ng-show="countOfUnreadVerifications>0" ng-cloak>
                              </span>
                            </a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/calibrator">
                        <li ui-sref-active="active" ng-controller="NotificationsControllerCalibrator">
                            <a ui-sref="new-verifications-calibrator" ng-click="reloadVerifications()"><i
                                    class="fa fa-list-alt fa-fw"></i> Нові заявки (вимірювальна лабораторія)
                           		<span class="ui teal label" ng-bind="countOfUnreadVerifications"
                                      ng-show="countOfUnreadVerifications>0" ng-cloak>
                              	</span>
                            </a>
                        </li>
                        <li ui-sref-active="active" ng-controller="MeasuringEquipmentControllerCalibrator">
                            <a ui-sref="measuring-equipment-calibrator" ng-click="onTableHandling()"><i
                                    class="fa fa-desktop"></i> Довідник засобів вимірювальної техніки (вимірювальна
                                лабораторія)
                            </a>
                        </li>
                        <li ui-sref-active="active">
                            <a ui-sref="planning-task-calibrator"><i class="fa fa-tasks"></i> Планування завдання
                            </a>
                        </li>
                        <li ui-sref-active="active">
                            <a><i class="fa fa-wrench"></i> Переносний модуль
                            </a>
                        </li>
                        <li ui-sref-active="active">
                            <a><i class="fa fa-wrench"></i> Стаціонарний модуль
                            </a>
                        </li>


                    </sec:authorize>


                    <sec:authorize url="/verificator">
                        <li ui-sref-active="active" ng-controller="NotificationsControllerVerificator">
                            <a ui-sref="new-verifications-verificator" ng-click="reloadVerifications()"><i
                                    class="fa fa-list-alt fa-fw"></i> Нові заявки (уповноважена повірочна
                                лабораторія)
                                <span class="ui teal label" ng-bind="countOfUnreadVerifications"
                                      ng-show="countOfUnreadVerifications>0" ng-cloak>
                              	</span>
                            </a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/provider/admin/">
                        <li ui-sref-active="active">
                            <a ui-sref="employee-show-provider"><i class="fa fa-users"></i> <span
                                    translate="EMPLOYEE"></span></a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/calibrator/admin/">
                        <li ui-sref-active="active">
                            <a ui-sref="employee-show-calibrator"><i class="fa fa-users"></i> <span
                                    translate="EMPLOYEE"></span></a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/verificator/admin/">
                        <li ui-sref-active="active">
                            <a ui-sref="employee-show-verificator"><i class="fa fa-users"></i> <span
                                    translate="EMPLOYEE"></span></a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/provider">
                        <li ui-sref-active="active">
                            <a ui-sref="verifications-archive-provider"><i class="fa fa-archive fa-fw"></i> Архів
                                повірок</a>
                        </li>
                    </sec:authorize>
                    <sec:authorize url="/calibrator">
                        <li ui-sref-active="active">
                            <a ui-sref="verifications-archive-calibrator"><i class="fa fa-archive fa-fw"></i> Архів
                                повірок</a>
                        </li>
                    </sec:authorize>
                    <sec:authorize url="/verificator">
                        <li ui-sref-active="active">
                            <a ui-sref="verifications-archive-verificator"><i class="fa fa-archive fa-fw"></i> Архів
                                повірок</a>
                        </li>
                    </sec:authorize>
                    <sec:authorize url="/provider/admin/">
                        <li ui-sref-active="active">
                            <a ui-sref="statistic-show-providerEmployee"><i class="fa fa-bar-chart"></i>
                                <span translate="STATISTIC_OF_EMPLOYEE_CAPACITY"> </span>
                            </a>
                        </li>
                    </sec:authorize>
                </ul>
            </div>
        </div>
        <div ng-class="{'col-md-12' : !menuState, 'col-md-9 main_column' : menuState}">
            <div ui-view></div>
        </div>
    </div>

</div>
<div id="loader-wrapper">
    <div id="loader" class="ui large loader"></div>
</div>
<toaster-container
        toaster-options="{'time-out': 3000, 'close-button':true, 'animation-class': 'toast-top-center'}"></toaster-container>
<script type="text/javascript" data-main="/resources/app/runApp"
        src="/resources/assets/bower_components/requirejs/require.js"></script>
<script src="/resources/assets/bower_components/jquery/dist/jquery.js" type="text/javascript"></script>
<script src="/resources/assets/bower_components/chosen/chosen.jquery.min.js"
        type="text/javascript"></script>
<script type="text/javascript">
    $(".chzn-select").chosen();
    $(".chzn-select-deselect").chosen({
        allow_single_deselect: true
    });
    $("#states").chosen();
</script>

<script src="/resources/assets/js/loader-employee.js"></script>


</body>
