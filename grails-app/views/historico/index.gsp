<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'historico.label', default: 'Historico')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <!-- Conteúdo da página    ================================================== -->
    <div id="page-content" class="clearfix fixed">
        ﻿

        <div class="box">
            <div class="box-content padded">
                <!-- Inicio da página -->

        <div id="list-historico" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
                <table class="table table-hover table-responsive">
                    <thead class="table-header">
                    <tr>
                        <th>Dia</th>
                        <th>Hora</th>
                        <th>Data Escala</th>
                        <th>Data Modificação</th>
                        <th>Usuário que alterou</th>
                    </tr>
                    </thead>
                    <tbody class="table-row-cell">
                    <g:each status="i" in="${historicoList}" var="item">
                        <tr>
                            <td><g:link action="edit" id="${item.id}">${item.dia}</g:link></td>
                            <td><g:link action="edit" id="${item.id}">${item.hora}</g:link></td>
                            <td>
                                <g:link action="edit" id="${item.id}"><g:formatDate format="yyyy-MM-dd" date="${item.dataEscala}"/></g:link>

                            </td>
                            <td>
                                <g:link action="edit" id="${item.id}"><g:formatDate format="yyyy-MM-dd" date="${item.dataModificacao}"/></g:link>

                            </td>
                            <td><g:link action="edit" id="${item.id}">${item.login.replace('Usuario(username:','').replace(')','')}</g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                    <tfoot class="table-footer">
                    <tr>
                        <td colspan="6">
                            <div class="pagination">
                                <g:paginate total="${fechamentosCount ?: 0}" />
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