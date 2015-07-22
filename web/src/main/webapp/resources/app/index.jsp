<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Виконавець послуг</title>
    <link href="/resources/assets/bower_components/ng-table/ng-table.css" rel="stylesheet">
	<link href="/resources/assets/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/assets/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet"
          href="/resources/assets/bower_components/chosen/chosen.min.css">
    <link href="/resources/assets/css/provider.css" rel="stylesheet">
 </head>

<body>

<div id="employeeModule" class="wrapper">

    <!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-static-top" role="navigation" style="margin-bottom: 0" ng-controller="TopNavBarControllerProvider">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand">Централізована система повірки лічильників</a>
        </div>
        
        <div ng-controller="InternationalizationController">
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
            <li class="dropdown" dropdown>
                <a class="dropdown-toggle" dropdown-toggle>
                    <i class="fa fa-user fa-fw"></i><i class="fa fa-caret-down"></i>
                </a>
                <ul class="dropdown-menu dropdown-user">
                    <li><a><i class="fa fa-user fa-fw"></i> Профіль</a>
                    </li>
                    <li><a><i class="fa fa-gear fa-fw"></i> Налаштування</a>
                    </li>
                    <li class="divider"></li>
                    <li><a ng-click="logout()"><i class="fa fa-sign-out fa-fw"></i> Вийти</a>
                    </li>
                </ul>
            </li>
        </ul>

        <!-- Sidebar --> 
        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu">
                <sec:authorize access="hasRole('ROLE_provider_admin')">
                    <li ui-sref-active="active">
                        <a ui-sref="main-panel-provider"><i class="fa fa-home fa-fw"></i> Головна панель провайдер</a>
                    </li>
                </sec:authorize>
                
                <sec:authorize url="/calibrator">    
                    <li ui-sref-active="active">
                        <a ui-sref="main-panel-calibrator"><i class="fa fa-home fa-fw"></i> Головна панель калібратор</a>
                    </li>
                </sec:authorize>  
                
                <sec:authorize url="/verificator">
                 <li ui-sref-active="active">
                        <a ui-sref="main-panel-verificator"><i class="fa fa-home fa-fw"></i> Головна панель веріфікатор</a>
                 </li>
                 </sec:authorize>   
                 
           

				 <sec:authorize url="/provider">	
                 <li ui-sref-active="active" ng-controller ="NotificationsControllerProvider">
                       <a ui-sref="new-verifications-provider" ng-click="reloadVerifications()" ><i class="fa fa-list-alt fa-fw"></i>Нові заявки (провайдер) 
       			 		<button  class="pull-right myCircleButton " ng-if="countOfUnreadVerifications > 0"
       			 			ng-bind="countOfUnreadVerifications" ng-cloak>
       			 		</button>
                      	</a>
                 </li>
                 </sec:authorize> 
                 
                 <sec:authorize url="/calibrator">
                 <li ui-sref-active="active" ng-controller ="NotificationsControllerCalibrator">
                         <a ui-sref="new-verifications-calibrator" ng-click="reloadVerifications()" ><i class="fa fa-list-alt fa-fw"></i>Нові заявки (калібратор)  
        			 		<button  class="pull-right myCircleButton " ng-if="countOfUnreadVerifications > 0"
        			 			ng-bind="countOfUnreadVerifications" ng-cloak>
        			 		</button>
                      	</a>
                    </li>
                     <li ui-sref-active="active" ng-controller ="MeasuringEquipmentControllerCalibrator">
                         <a ui-sref="measuring-equipment-calibrator" ng-click="onTableHandling()" ><i class="fa fa-desktop"></i>Довідник засобів вимірювальної техніки (калібратор)  
                      	</a>
                    </li>
                    
                    
                    </sec:authorize>
                    
                    
                    
                    <sec:authorize url="/verificator">
                    <li ui-sref-active="active" ng-controller ="NotificationsControllerVerificator"> 
                        <a ui-sref="new-verifications-verificator" ng-click="reloadVerifications()" ><i class="fa fa-list-alt fa-fw"></i>Нові заявки (веріфікатор)  
        			 		<button  class="pull-right myCircleButton " ng-if="countOfUnreadVerifications > 0"
        			 			ng-bind="countOfUnreadVerifications" ng-cloak>
        			 		</button>
                      	</a>
                    </li>   
                    </sec:authorize>
                    
	                <li ui-sref-active="active">
                        <a ui-sref="adding-verifications-provider"><i class="fa fa-file-text-o"></i>Ініціювати повірку</a>
                    </li>
				
                    <li ui-sref-active="active">
                        <a ui-sref="add-employees"><i class="fa fa-user-plus"></i>Додати працівника</a>
                    </li>
                    
                    
                    <sec:authorize url="/provider/admin/">
                    <li ui-sref-active="active">
                        <a ui-sref="employee-show-provider"><i class="fa fa-users"></i>Переглянути усіх працівників</a>
                    </li>
                    </sec:authorize>
                    
                    <sec:authorize url="/provider">
                    <li ui-sref-active="active">
                        <a ui-sref="verifications-archive-provider"><i class="fa fa-archive fa-fw"></i> Архів повірок</a>
                    </li>
               		</sec:authorize>

                    <sec:authorize url="/provider/admin/">
                        <li ui-sref-active="active">
                            <a ui-sref="statistic-show-providerEmployee"><i class="fa fa-bar-chart"></i>Статистика продуктивності працівників</a>
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
<br>
<script src="/resources/assets/bower_components/jquery/dist/jquery.js"
        type="text/javascript"></script>
<br>
<br>
<script
        src="/resources/assets/bower_components/chosen/chosen.jquery.min.js"
        type="text/javascript"></script>
<br>
<script type="text/javascript">
    $(".chzn-select").chosen();
    $(".chzn-select-deselect").chosen({
        allow_single_deselect : true
    });
    $("#states").chosen();
</script>
</body>
