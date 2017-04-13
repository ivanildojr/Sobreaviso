<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'usuario.label', default: 'Usuario')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
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
                <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            </div>
            <div class="box">
                <div class="box-content padded">

                    <div id="edit-usuario" class="content scaffold-edit" role="main">
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
                        <div id="show-usuario" class="content scaffold-show" role="main">
                            %{--<h1><g:message code="default.show.label" args="[entityName]" /></h1>--}%
                            <g:if test="${flash.message}">
                                <div class="message" role="status">${flash.message}</div>
                            </g:if>
                            <table class="table table-hover table-responsive">
                                <thead class="table-header">
                                <tr>
                                    <th>Nome</th>
                                    <th>Login</th>
                                    <th>Status</th>
                                    <th>Expirado</th>
                                    <th>Conta Fechada</th>
                                    <th>Senha Expirada</th>
                                </tr>
                                </thead>
                                <tbody class="table-row-cell">
                                    <tr>
                                        <td>${usuario.nome?.encodeAsHTML()}</td>
                                        <td>${usuario.username?.encodeAsHTML()}</td>
                                        <td>${usuario.enabled?.encodeAsHTML()}</td>
                                        <td>${usuario.accountExpired?.encodeAsHTML()}</td>
                                        <td>${usuario.accountLocked?.encodeAsHTML()}</td>
                                        <td>${usuario.passwordExpired?.encodeAsHTML()}</td>


                                    </tr>
                                </tbody>
                            </table>
                            <g:form resource="${this.usuario}" method="DELETE">
                                <fieldset class="buttons">
                                    <g:link class="btn" action="edit" resource="${this.usuario}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                                    <input class="delete" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                                </fieldset>
                            </g:form>
                        </div>
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
