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

            %{--<li id="nutelRobot"><g:link controller="escala" action="agenda"><i class="icon-user-md"></i>Suporte On-Line</g:link></li>--}%


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
