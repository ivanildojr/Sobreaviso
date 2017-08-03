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


                    <!-- Table Ajustar o padding de .table .td e margin-bottom do div-->
                    <table class="table" id="tabelachamados">
                        <thead>
                            <tr>
                                <th>Setor</th>
                                <th>Descrição</th>
                                <th>Hora</th>
                                <th>Data</th>
                                <th>Status</th>
                                <th>Ordem</th>
                                <th>Usuário</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr class="table-aberto">
                                <th scope="row">
                                    NURAM
                                <td><div>Sem rede na máquina de Ivanelson.</div></td>
                                <td>12:54</td>
                                <td>02/08/2017</td>
                                <td>Aberto</td>
                                <td>1</td>
                                <td>Aldo Maia</td>
                                <td>
                                    <a href="#" title="Atender"><i class="icon-wrench"></i></a>
                                    <a href="#" title="Mudar Prioridade"><i class="icon-random"></i></a>
                                    <a href="#" title="Encerrar"><i class="icon-ban-circle"></i></a>
                                </td>
                            </tr>
                            <tr class="table-executando">
                                <th scope="row">
                                    NURAM
                                <td><div>Sem rede na máquina de Ivanelson.</div></td>
                                <td>12:54</td>
                                <td>02/08/2017</td>
                                <td>Atendendo</td>
                                <td>1</td>
                                <td>Aldo Maia</td>
                                <td>
                                    <a href="#" title="Atender"><i class="icon-wrench"></i></a>
                                    <a href="#" title="Mudar Prioridade"><i class="icon-random"></i></a>
                                    <a href="#" title="Encerrar"><i class="icon-ban-circle"></i></a>
                                </td>
                            </tr>
                            <tr class="table-prioridade">
                                <th scope="row">
                                    NURAM
                                <td><div>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                            Mauris ac lorem ante. Vestibulum quis magna pretium, lacinia arcu at, condimentum odio.
                            Ut ultrices tempor metus, sit amet tristique nibh vestibulum in.
                            Pellentesque vel velit eget purus mollis placerat sed sit amet enim. Sed efficitur orci sapien, ac laoreet erat fringilla sodales.</div></td>
                                <td>12:54</td>
                                <td>02/08/2017</td>
                                <td>Prioridade</td>
                                <td>1</td>
                                <td>Aldo Maia</td>
                                <td>
                                    <a href="#" title="Atender"><i class="icon-wrench"></i></a>
                                    <a href="#" title="Mudar Prioridade"><i class="icon-random"></i></a>
                                    <a href="#" title="Encerrar"><i class="icon-ban-circle"></i></a>
                                </td>
                            </tr>
                            <tr class="table-aguardando">
                                <th scope="row">
                                    NURAM
                                <td><div>Sem rede na máquina de Ivanelson.</div></td>
                                <td>12:54</td>
                                <td>02/08/2017</td>
                                <td>Aguardando Usuário</td>
                                <td>1</td>
                                <td>Aldo Maia</td>
                                <td>
                                    <a href="#" title="Atender"><i class="icon-wrench"></i></a>
                                    <a href="#" title="Mudar Prioridade"><i class="icon-random"></i></a>
                                    <a href="#" title="Encerrar"><i class="icon-ban-circle"></i></a>
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>



                %{--<div class="alert alert-info" role="alert">--}%
                    %{--<a href="#" class="alert-link">...</a>--}%
                %{--</div>--}%
                %{--<div class="alert alert-warning" role="alert">--}%
                    %{--<a href="#" class="alert-link">...</a>--}%
                %{--</div>--}%
                %{--<div class="alert alert-danger" role="alert">--}%
                    %{--<a href="#" class="alert-link">...</a>--}%
                %{--</div>--}%






            </div>
        </div>
    </div>
    <!-- Fim do conteúdo da página ================================================== -->
    </body>
</html>