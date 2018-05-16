<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'fechamentos.label', default: 'Fechamentos')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>

        <asset:stylesheet src="grails-datatables.css"/>
        <asset:stylesheet src="grails-datatables-plain.css"/>
        <asset:javascript src="grails-datatables.js"/>

        <script type="text/javascript">
            $(document).ready(function() {
                $('#tabelaFechamentos').dataTable( {
                    "language": {
                        "url": "//cdn.datatables.net/plug-ins/1.10.16/i18n/Portuguese-Brasil.json"
                    },
                    "order": [[ 1, "desc" ]]
                });

            });
        </script>

        <style>
        .table th, .table td {
            /*text-align: center;*/
            /*align-items: center;*/
            text-align: center;
            vertical-align: middle;
            horiz-align: center;
        }
        </style>

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


        <div id="list-fechamentos" class="content scaffold-list" role="main">

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:form>
                <div align="center" name="recarregarBtn">
                    %{--<button id="teste" class="btn btn-large btn-primary" type="button">Teste</button>--}%
                    <g:actionSubmit value="Recarregar" action="recarregar"/>
                </div>
            </g:form>
                <table id="tabelaFechamentos" class="table table-hover" style="width:30%">
                    <thead class="table-header">
                    <tr>
                        <th>Funcionário</th>
                        <th>Data Lançamentos</th>
                        %{--<th>Carga Horária Lançada</th>--}%
                        <th>Carga Horária Lançada</th>
                    </tr>
                    </thead>
                    <tbody class="table-row-cell">
                    <g:each status="i" in="${fechamentosList}" var="item">
                        <tr>
                            <td>
                                <g:link action="edit" id="${item.id}">
                                        <g:if test="${item.codFunc == 3}">Torres</g:if>
                                        <g:if test="${item.codFunc == 31}">Ivanildo</g:if>
                                        <g:if test="${item.codFunc == 64}">Rudsom</g:if>
                                </g:link>
                            </td>
                            <td>${formatDate(format: 'yyyy-MM-dd', date: item.dataLancamento)}</td>
                            %{--<td>${item.cargaHorariaD}</td>--}%
                            <td>${item.cargaHorariaS}</td>
                        </tr>
                    </g:each>
                    </tbody>
                    <tfoot class="table-footer">
                    <tr>


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