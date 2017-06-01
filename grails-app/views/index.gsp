<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Sobreaviso NUTEL-RN</title>

    %{--<asset:link rel="icon" href="favicon.ico" type="image/x-ico" />--}%
</head>
<body>



<div class="container-fluid" id="main-container">
    <!-- Menú principal lateral ================================================== -->
    <div id="sidebar" class="fixed">
        <ul class="nav nav-list nav-open">
            <li><a href="#" class="fechar-sidebar"><i class="icon-reorder"></i> Fechar menu </a></li>

            <li id="menuInicio" class="ativo"><a href="/sobreaviso"><i class="icon-home"></i> Início</a></li>

            <li id="menuAgenda"><g:link controller="escala" action="agenda"><i class="icon-tasks"></i>Agenda</g:link></li>

            <li id="menuHistorico"><g:link controller="escala" action="historico"><i class="icon-calendar"></i>Histórico Escala</g:link></li>

            <li id="menuTopPontoREP"><g:link controller="topPontoREP" action="pegaMarcacoes"><i class="icon-calendar"></i>Pegar Marcações</g:link></li>

            <li id="nutelRobot"><g:link controller="escala" action="chatBot"><i class="icon-user-md"></i>Suporte On-Line</g:link></li>


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

    <div class="modal fade" id="modalContato" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Digite aqui sua mensagem</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <label for="email">E-mail:</label><input id="email" type="email"/>
                    <div class="form-group">
                        <label for="mensagem">Mensagem:</label>
                        <textarea class="form-control" rows="5" id="mensagem"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
                    <button type="button" class="btn btn-primary">Enviar</button>
                </div>
            </div>
        </div>
    </div>


    <!-- Conteúdo da página    ================================================== -->
    <div id="page-content" class="clearfix fixed">
        ﻿
        <div class="page-header">
            <h1> Sobreaviso
                <small><i class="icon-double-angle-right"></i> Sistema de Abertura e Acompanhamento de Chamados - NUTEL-RN</small>
            </h1>
        </div>
        <div class="box">
            <div class="box-header">
                <span class="title" style="font-size: 18px;">
                    <div class="text-center">
                        <strong>
                            <span class="text-error">
                                <i class="icon-pushpin"></i>
                            </span>
                            <span class="text-success">
                                Avisos
                            </span>
                        </strong>
                    </div>
                </span>
            </div>
            <div class="box-content padded">
                <table class="table">
                    <tbody>
                    <tr>
                        <td>
                            Nenhuma Mensagem Cadastrada.

                        </td>
                    </tr>
                    </tbody>
                </table>

            </div>
        </div>

    </div>
    <!-- Fim do conteúdo da página ================================================== -->

</div>

</div>












</body>
</html>
