<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'usuario.label', default: 'Usuario')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
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












































    </body>
</html>