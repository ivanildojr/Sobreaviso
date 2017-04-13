<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'usuario.label', default: 'Usuario')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
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
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li class="active"><a href="#modalContato" data-toggle="modal">Fale Conosco</a></li>
            </ul>
        </div>


        <!-- Conteúdo da página    ================================================== -->
        <div id="page-content" class="clearfix fixed">
            ﻿
            <div class="page-header">
                <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            </div>
            <div class="box">
                <div class="box-content padded">
                    <!-- Inicio da página -->
                    <div id="list-usuario" class="content scaffold-list" role="main">

                        <g:if test="${flash.message}">
                            <div class="message" role="status">${flash.message}</div>
                        </g:if>
                        <g:link class="btn" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link>
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
                            <g:each status="i" in="${usuarioList}" var="item">
                                <tr>
                                    <td><g:link action="edit" id="${item.id}">${item.nome?.encodeAsHTML()}</g:link></td>
                                    <td><g:link action="edit" id="${item.id}">${item.username?.encodeAsHTML()}</g:link></td>
                                    <td><g:link action="edit" id="${item.id}">${item.enabled?.encodeAsHTML()}</g:link></td>
                                    <td><g:link action="edit" id="${item.id}">${item.accountExpired?.encodeAsHTML()}</g:link></td>
                                    <td><g:link action="edit" id="${item.id}">${item.accountLocked?.encodeAsHTML()}</g:link></td>
                                    <td><g:link action="edit" id="${item.id}">${item.passwordExpired?.encodeAsHTML()}</g:link></td>


                                </tr>
                            </g:each>
                            </tbody>
                            <tfoot class="table-footer">
                            <tr>
                                <td colspan="6">
                                    <div class="pagination">
                                        <g:paginate total="${usuarioCount ?: 0}" />
                                    </div>
                                </td>

                            </tr>
                            </tfoot>
                        </table>

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