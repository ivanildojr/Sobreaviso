<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'usuarioPerfil.label', default: 'UsuarioPerfil')}" />
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
        <div id="list-usuarioPerfil" class="content scaffold-list" role="main">

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

                <table class="table table-hover table-responsive">
                    <thead class="table-header">
                    <tr>
                        <th>Usuário</th>
                        <th>Perfil</th>
                    </tr>
                    </thead>
                    <tbody class="table-row-cell">
                    <g:each status="i" in="${usuarioPerfilList}" var="item">
                        <tr>
                            <td><g:link action="usuario.edit" id="${item.id}">${item.usuario?.encodeAsHTML()}</g:link></td>
                            <td><g:link action="edit" id="${item.id}">${item.perfil?.encodeAsHTML()}</g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                    <tfoot class="table-footer">
                    <tr>
                        <td colspan="6">
                            <div class="pagination">
                                <g:paginate total="${usuarioPerfilCount ?: 0}" />
                            </div>
                        </td>

                    </tr>
                    </tfoot>
                </table>




        </div>
            </div>
        </div>

    </div>
    <!-- Fim do conteúdo da página ================================================== -->
    </body>
</html>