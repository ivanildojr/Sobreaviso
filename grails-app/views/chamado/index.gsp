<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'chamado.label', default: 'Chamado')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <asset:stylesheet src="prf/chamados.css"/>
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <asset:javascript src="prf/chamados.js"/>
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



                <div class="panel panel-default">




                    <table class="table table-hover" id="tabelachamados">
                        <thead>
                            <tr>
                                <th>Ordem</th>
                                <th>Setor</th>
                                <th>Descrição</th>
                                <th>Hora</th>
                                <th>Data</th>
                                <th>Status</th>
                                <th>Ticket</th>
                                <th>Usuário</th>
                                <th>IP</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:set var="ordem" value="0"/>
                            <g:each status="i" in="${chamadoList}" var="item">
                                <g:if test="${item.status=='aberto'}">
                                    <tr class="table-aberto">
                                        <td class="ordem">${item.ordem}</td>
                                        <th scope="row">${item.setor}</th>

                                        <td><div>${item.descricao}</div></td>
                                        <td>${item.horaAbertura}</td>
                                        <td>${item.dataAbertura}</td>
                                        <td>${item.status}</td>
                                        <td class="ordemAtendimento">${item.ordemAtendimento}</td>
                                        <td>${item.usuarioAbertura}</td>
                                        <td>${item.ip}</td>
                                        <td>

                                            <a href="${g.createLink(controller: 'chamado', action: 'edit', id:item.id)}" title="Atender"><i class="icon-wrench"></i></a>
                                            %{--<a href="#" title="Mudar Prioridade"><i class="icon-random"></i></a>--}%
                                            <a href="#" title="Encerrar"><i class="icon-ban-circle"></i></a>
                                        </td>
                                    </tr>
                                </g:if>
                                <g:if test="${item.status=='executando'}">
                                    <tr class="table-executando">
                                        <td class="ordem">${item.ordem}</td>
                                        <th scope="row">${item.setor}</th>

                                        <td><div>${item.descricao}</div></td>
                                        <td>${item.horaAbertura}</td>
                                        <td>${item.dataAbertura}</td>
                                        <td>${item.status}</td>
                                        <td class="ordemAtendimento">${item.ordemAtendimento}</td>
                                        <td>${item.usuarioAbertura}</td>
                                        <td>${item.ip}</td>
                                        <td>
                                            <a href="${g.createLink(controller: 'chamado', action: 'edit', id:item.id)}" title="Atender"><i class="icon-wrench"></i></a>
                                            %{--<a href="#" title="Mudar Prioridade"><i class="icon-random"></i></a>--}%
                                            <a href="#" title="Encerrar"><i class="icon-ban-circle"></i></a>
                                        </td>
                                    </tr>
                                </g:if>
                                <g:if test="${item.status=='prioridade'}">
                                    <tr class="table-prioridade">
                                        <td class="ordem">${item.ordem}</td>
                                        <th scope="row">${item.setor}</th>

                                        <td><div>${item.descricao}</div></td>
                                        <td>${item.horaAbertura}</td>
                                        <td>${item.dataAbertura}</td>
                                        <td>${item.status}</td>
                                        <td class="ordemAtendimento">${item.ordemAtendimento}</td>
                                        <td>${item.usuarioAbertura}</td>
                                        <td>${item.ip}</td>
                                        <td>
                                            <a href="${g.createLink(controller: 'chamado', action: 'edit', id:item.id)}" title="Atender"><i class="icon-wrench"></i></a>
                                            %{--<a href="#" title="Mudar Prioridade"><i class="icon-random"></i></a>--}%
                                            <a href="#" title="Encerrar"><i class="icon-ban-circle"></i></a>
                                        </td>
                                    </tr>
                                </g:if>
                                <g:if test="${item.status=='aguardando'}">
                                    <tr class="table-aguardando">
                                        <td class="ordem">${item.ordem}</td>
                                        <th scope="row">${item.setor}</th>

                                        <td><div>${item.descricao}</div></td>
                                        <td>${item.horaAbertura}</td>
                                        <td>${item.dataAbertura}</td>
                                        <td>${item.status}</td>
                                        <td class="ordemAtendimento">${item.ordemAtendimento}</td>
                                        <td>${item.usuarioAbertura}</td>
                                        <td>${item.ip}</td>
                                        <td>
                                            <a href="${g.createLink(controller: 'chamado', action: 'edit', id:item.id)}" title="Atender"><i class="icon-wrench"></i></a>
                                            %{--<a href="#" title="Mudar Prioridade"><i class="icon-random"></i></a>--}%
                                            <a href="#" title="Encerrar"><i class="icon-ban-circle"></i></a>
                                        </td>
                                    </tr>
                                </g:if>
                            </g:each>
                        </tbody>
                    </table>
                    %{--<div class="pagination" align="right">--}%
                        %{--<g:paginate total="${chamadoCount ?: 0}" />--}%
                    %{--</div>--}%
                </div>


            </div>
        </div>
    </div>
    <!-- Fim do conteúdo da página ================================================== -->

    <g:javascript>

    function atualizaOrdemDB(ordemAtendimento, key, ordem){

        $.ajax({
            url:'${g.createLink(controller: 'chamado', action: 'atualizaOrdemDB')}',
            dataType: 'json',
            contentType: 'json',
            data: {ordemAtendimento: ordemAtendimento.eq(key).text(),ordem: ordem},
            success: function (data) {
                //console.log(data);



            },
            error: function (request, status, error) {
                //alert(error);
            }
        });

    };

    </g:javascript>

    </body>
</html>