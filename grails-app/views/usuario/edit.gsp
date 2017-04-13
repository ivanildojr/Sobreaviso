<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'usuario.label', default: 'Usuario')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>

    <div class="container-fluid" id="main-container">
        <!-- Menú principal lateral ================================================== -->
        <div id="sidebar" class="fixed">
            <ul class="nav nav-list nav-open">
                <li><a href="#" class="fechar-sidebar"><i class="icon-reorder"></i> Fechar menu </a></li>


                <li id="menuInicio" class="ativo"><a href="/sobreaviso"><i class="icon-home"></i> Início</a></li>


                <li id="menuAgenda"><g:link controller="escala" action="agenda"><i class="icon-tasks"></i>Agenda</g:link></li>




            </ul>
            <ul class="nav nav-list nav-close" style="display:none">
                <li><a href="#" class="fechar-sidebar"><i class="icon-reorder"></i> Abrir menu</a></li>
            </ul>
        </div>        <div id="main-content" class="clearfix">
        <!-- Breadcrumb    ================================================== -->
        <!-- Navegação secundária    ================================================== -->
        <div class="menu-nav fixed">
            <ul class="nav inline">
                <li class="active"><a href="#modalContato" data-toggle="modal">Fale Conosco</a></li>
            </ul>
        </div>


        <!-- Conteúdo da página    ================================================== -->
        <div id="page-content" class="clearfix fixed">
            ﻿
            <div class="page-header">
                  <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            </div>
            <div class="box">
                <div class="box-content padded">
                   <!-- Inicio da página -->
                    %{--<a href="#edit-usuario" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>--}%
                    %{--<div class="nav" role="navigation">--}%
                        %{--<ul>--}%
                            %{--<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>--}%
                            %{--<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>--}%
                            %{--<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>--}%
                        %{--</ul>--}%
                    %{--</div>--}%
                    <div id="edit-usuario" class="content scaffold-edit" role="main">
                        %{--<h1><g:message code="default.edit.label" args="[entityName]" /></h1>--}%
                        <g:if test="${flash.message}">
                            <div class="message" role="status">${flash.message}</div>
                        </g:if>
                        <g:hasErrors bean="${this.usuario}">
                            <ul class="errors" role="alert">
                                <g:eachError bean="${this.usuario}" var="error">
                                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                                </g:eachError>
                            </ul>
                        </g:hasErrors>
                        <g:form resource="${this.usuario}" method="PUT">
                            <g:hiddenField name="version" value="${this.usuario?.version}" />
                            <fieldset class="form">
                                <f:all bean="usuario"/>
                            </fieldset>
                            <fieldset class="buttons">
                                <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                                <g:link class="btn" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link>
                            </fieldset>
                        </g:form>
                    </div>
                    <!-- Fim do caixa -->


                </div>
            </div>

        </div>
        <!-- Fim do conteúdo da página ================================================== -->

    </div>

    </div>











    </body>
</html>
