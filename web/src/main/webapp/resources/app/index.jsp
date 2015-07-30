<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xml:lang="uk,en,ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Виконавець послуг</title>
    <link href="/resources/assets/bower_components/ng-table/ng-table.css" rel="stylesheet">
    <link href="/resources/assets/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/assets/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/resources/assets/bower_components/chosen/chosen.min.css">

    <link href="/resources/assets/css/provider.css" rel="stylesheet">
    <link href="/resources/assets/css/calibrator.css" rel="stylesheet">


    <link href="/resources/assets/css/application-form.css" rel="stylesheet">
<link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
<link href="/resources/assets/bower_components/bootstrap-switch/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet">
<link rel="stylesheet"href="/resources/assets/winmarkltd-BootstrapFormHelpers-0d89ab4/dist/css/bootstrap-formhelpers.min.css">
<link rel="stylesheet" href="/resources/assets/css/loader.css">

    <!-- Ionicons -->
    <link href="/resources/assets/AdminLTE-master/css/ionicons.min.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="/resources/assets/AdminLTE-master/css/AdminLTE.css" rel="stylesheet" type="text/css" />

</head>

<body  class="skin-blue">

<div id="employeeModule" class="wrapper">

    <!-- Navigation -->
    <nav class="navbar  navbar-fixed-top" role="navigation" style="margin-bottom: -4px"
         ng-controller="TopNavBarControllerProvider">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand"  style="color:beige">Централізована система повірки лічильників</a>
        </div>

        <div ng-controller="InternationalizationController" >
            <ul class="nav navbar-nav">
                <li class="dropdown" dropdown on-toggle="toggled(open)">
                    <a class="dropdown-toggle" dropdown-toggle>
                        {{ 'LANG' | translate }} <span class="caret"></span>
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

            <li class="dropdown messages-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <i class="fa fa-envelope"></i>
                    <span class="label label-success">4</span>
                </a>
                <ul class="dropdown-menu" >
                    <li class="header">You have 4 messages</li>
                    <li>
                        <!-- inner menu: contains the actual data -->
                        <ul class="menu">
                            <li><!-- start message -->
                                <a href="#">
                                    <div class="pull-left">
                                        <img src="/resources/assets/AdminLTE-master/img/avatar3.png" class="img-circle" alt="User Image"/>
                                    </div>
                                    <h4>
                                        Support Team
                                        <small><i class="fa fa-clock-o"></i> 5 mins</small>
                                    </h4>
                                    <p>Why not buy a new awesome theme?</p>
                                </a>
                            </li><!-- end message -->
                            <li>
                                <a href="#">
                                    <div class="pull-left">
                                        <img src="img/avatar2.png" class="img-circle" alt="user image"/>
                                    </div>
                                    <h4>
                                        AdminLTE Design Team
                                        <small><i class="fa fa-clock-o"></i> 2 hours</small>
                                    </h4>
                                    <p>Why not buy a new awesome theme?</p>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="pull-left">
                                        <img src="img/avatar.png" class="img-circle" alt="user image"/>
                                    </div>
                                    <h4>
                                        Developers
                                        <small><i class="fa fa-clock-o"></i> Today</small>
                                    </h4>
                                    <p>Why not buy a new awesome theme?</p>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="pull-left">
                                        <img src="img/avatar2.png" class="img-circle" alt="user image"/>
                                    </div>
                                    <h4>
                                        Sales Department
                                        <small><i class="fa fa-clock-o"></i> Yesterday</small>
                                    </h4>
                                    <p>Why not buy a new awesome theme?</p>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="pull-left">
                                        <img src="img/avatar.png" class="img-circle" alt="user image"/>
                                    </div>
                                    <h4>
                                        Reviewers
                                        <small><i class="fa fa-clock-o"></i> 2 days</small>
                                    </h4>
                                    <p>Why not buy a new awesome theme?</p>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="footer"><a href="#">See All Messages</a></li>
                </ul>
            </li>
            <!-- Notifications: style can be found in dropdown.less -->
            <li class="dropdown notifications-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <i class="fa fa-warning"></i>
                    <span class="label label-warning">10</span>
                </a>
                <ul class="dropdown-menu">
                    <li class="header">You have 10 notifications</li>
                    <li>
                        <!-- inner menu: contains the actual data -->
                        <ul class="menu">
                            <li>
                                <a href="#">
                                    <i class="ion ion-ios7-people info"></i> 5 new members joined today
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-warning danger"></i> Very long description here that may not fit into the page and may cause design problems
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-users warning"></i> 5 new members joined
                                </a>
                            </li>

                            <li>
                                <a href="#">
                                    <i class="ion ion-ios7-cart success"></i> 25 sales made
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="ion ion-ios7-person danger"></i> You changed your username
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="footer"><a href="#">View all</a></li>
                </ul>
            </li>
            <!-- Tasks: style can be found in dropdown.less -->
            <li class="dropdown tasks-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <i class="fa fa-tasks"></i>
                    <span class="label label-danger">9</span>
                </a>
                <ul class="dropdown-menu">
                    <li class="header">You have 9 tasks</li>
                    <li>
                        <!-- inner menu: contains the actual data -->
                        <ul class="menu">
                            <li><!-- Task item -->
                                <a href="#">
                                    <h3>
                                        Design some buttons
                                        <small class="pull-right">20%</small>
                                    </h3>
                                    <div class="progress xs">
                                        <div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                            <span class="sr-only">20% Complete</span>
                                        </div>
                                    </div>
                                </a>
                            </li><!-- end task item -->
                            <li><!-- Task item -->
                                <a href="#">
                                    <h3>
                                        Create a nice theme
                                        <small class="pull-right">40%</small>
                                    </h3>
                                    <div class="progress xs">
                                        <div class="progress-bar progress-bar-green" style="width: 40%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                            <span class="sr-only">40% Complete</span>
                                        </div>
                                    </div>
                                </a>
                            </li><!-- end task item -->
                            <li><!-- Task item -->
                                <a href="#">
                                    <h3>
                                        Some task I need to do
                                        <small class="pull-right">60%</small>
                                    </h3>
                                    <div class="progress xs">
                                        <div class="progress-bar progress-bar-red" style="width: 60%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                            <span class="sr-only">60% Complete</span>
                                        </div>
                                    </div>
                                </a>
                            </li><!-- end task item -->
                            <li><!-- Task item -->
                                <a href="#">
                                    <h3>
                                        Make beautiful transitions
                                        <small class="pull-right">80%</small>
                                    </h3>
                                    <div class="progress xs">
                                        <div class="progress-bar progress-bar-yellow" style="width: 80%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                            <span class="sr-only">80% Complete</span>
                                        </div>
                                    </div>
                                </a>
                            </li><!-- end task item -->
                        </ul>
                    </li>
                    <li class="footer">
                        <a href="#">View all tasks</a>
                    </li>
                </ul>
            </li>
            <!-- User Account: style can be found in dropdown.less -->
            <li class="dropdown user user-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <i class="glyphicon glyphicon-user"></i>
                    <span>Jane Doe <i class="caret"></i></span>
                </a>
                <ul class="dropdown-menu">
                    <!-- User image -->
                    <li class="user-header bg-light-blue">
                        <img src="img/avatar3.png" class="img-circle" alt="User Image" />
                        <p>
                            Jane Doe - Web Developer
                            <small>Member since Nov. 2012</small>
                        </p>
                    </li>
                    <!-- Menu Body -->
                    <li class="user-body">
                        <div class="col-xs-4 text-center">
                            <a href="#">Followers</a>
                        </div>
                        <div class="col-xs-4 text-center">
                            <a href="#">Sales</a>
                        </div>
                        <div class="col-xs-4 text-center">
                            <a href="#">Friends</a>
                        </div>
                    </li>
                    <!-- Menu Footer-->
                    <li class="user-footer">
                        <div class="pull-left">
                            <a href="#" class="btn btn-default btn-flat">Profile</a>
                        </div>
                        <div class="pull-right">
                            <a ng-click="logout()" class="btn btn-default btn-flat">Sign out</a>
                        </div>
                    </li>
                </ul>
            </li>
            <li class="dropdown" dropdown>
                <a class="dropdown-toggle" dropdown-toggle>
                    User name   <i class="fa fa-user fa-fw"></i><i class="fa fa-caret-down"></i>
                </a>
                <ul class="dropdown-menu dropdown-user"  style="background-color: lightskyblue ">
                    <li class="user-header bg-light-blue">
                        <img src="/resources/assets/AdminLTE-master/img/avatar3.png" class="img-circle" alt="User Image" />
                        <p>
                           INPUT PARAM OF USER HERE
                        </p>
                    </li>
                    <li class="user-footer" >
                        <div  style="width: 100%">
                            <a href="#" class="btn btn-default" style="color: #2C1919 ">Profile</a>
                        </div>
                        <div  style="width: 100%">
                            <a class="btn btn-default" style="color: #2C1919"> {{ 'SETTINGS' | translate }}</a>
                        </div>
                        <div  style="width: 100%" >
                            <a ng-click="logout()" class="btn btn-default" style="color: #2C1919 ">{{ 'LOG_OUT' | translate
                                }} </a>
                        </div>
                    </li>
                </ul>
            </li>
        </ul>

        <!-- Sidebar -->
        <div class="sidebar" role="navigation">
            <div >
                <ul class="sidebar-menu" id="side-menu">
                    <sec:authorize url="/provider">
                        <li ui-sref-active="active">
                            <a ui-sref="main-panel-provider"><i class="fa fa-home fa-fw"></i>  Головна панель
                                провайдер</a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/calibrator">
                        <li ui-sref-active="active">
                            <a ui-sref="main-panel-calibrator"><i class="fa fa-home fa-fw"></i>  Головна панель
                                калібратор</a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/verificator">
                        <li ui-sref-active="active">
                            <a ui-sref="main-panel-verificator"><i class="fa fa-home fa-fw"></i>  Головна панель
                                веріфікатор</a>
                        </li>
                    </sec:authorize>


                    <sec:authorize url="/provider">
                        <li ui-sref-active="active" ng-controller="NotificationsControllerProvider">
                            <a ui-sref="new-verifications-provider" ng-click="reloadVerifications()"><i
                                    class="fa fa-list-alt fa-fw"></i>  Нові заявки (провайдер)
                              <span id="coloredBadge" class="badge pull-right" ng-bind="countOfUnreadVerifications"ng-cloak>
                              </span>
                            </a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/calibrator">
                        <li ui-sref-active="active" ng-controller="NotificationsControllerCalibrator">
                            <a ui-sref="new-verifications-calibrator" ng-click="reloadVerifications()"><i
                                    class="fa fa-list-alt fa-fw"></i>   Нові заявки (калібратор)
                           		<span id="coloredBadge" class="badge pull-right" ng-bind="countOfUnreadVerifications"ng-cloak>
                              	</span>
                            </a>
                        </li>
                        <li ui-sref-active="active" ng-controller="MeasuringEquipmentControllerCalibrator">
                            <a ui-sref="measuring-equipment-calibrator" ng-click="onTableHandling()"><i
                                    class="fa fa-desktop"></i>  Довідник засобів вимірювальної техніки (калібратор)
                            </a>
                        </li>


                    </sec:authorize>


                    <sec:authorize url="/verificator">
                        <li ui-sref-active="active" ng-controller="NotificationsControllerVerificator">
                            <a ui-sref="new-verifications-verificator" ng-click="reloadVerifications()"><i
                                    class="fa fa-list-alt fa-fw"></i> Нові заявки (веріфікатор)
                                <span id="coloredBadge" class="badge pull-right" ng-bind="countOfUnreadVerifications"ng-cloak>
                              	</span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize url="/employee/admin/">

                        <li ui-sref-active="active">
                            <a ui-sref="employee-show-provider"><i class="fa fa-users"></i> Переглянути усіх працівників</a>
                        </li>
                    </sec:authorize>

                    <sec:authorize url="/provider">
                        <li ui-sref-active="active">
                            <a ui-sref="verifications-archive-provider"><i class="fa fa-archive fa-fw"></i>   Архів
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
                            <a ui-sref="statistic-show-providerEmployee"><i class="fa fa-bar-chart"></i>   Статистика
                                продуктивності працівників</a>
                        </li>
                    </sec:authorize>
                </ul>
            </div>
        </div>
    </nav>
    <div ui-view></div>
</div>
<script type="text/javascript" data-main="/resources/app/runApp"
        src="/resources/assets/bower_components/requirejs/require.js"></script>
<script src="/resources/assets/bower_components/jquery/dist/jquery.js"
        type="text/javascript"></script>
<script
        src="/resources/assets/bower_components/chosen/chosen.jquery.min.js"
        type="text/javascript"></script>
<script type="text/javascript">
    $(".chzn-select").chosen();
    $(".chzn-select-deselect").chosen({
        allow_single_deselect: true
    });
    $("#states").chosen();
</script>
<!-- AdminLTE App -->
<script src="/resources/assets/AdminLTE-master/js/AdminLTE/app.js" type="text/javascript"></script>
<script src="/resources/assets/js/vendor/modernizr-2.6.2.min.js"></script>
<script src="/resources/assets/bower_components/bootstrap-switch/dist/js/bootstrap-switch.js"></script>
<script src="/resources/assets/winmarkltd-BootstrapFormHelpers-0d89ab4/dist/js/bootstrap-formhelpers.js"></script>
<script src="/resources/assets/js/main.js"></script>
</body>
</html>