<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    %{--<asset:javascript src="prf/jquery.js"/>   Removido pois já está inserido no sistema  --}%
    <asset:javascript src="sobreaviso.js"/>
    <asset:javascript src="rivescript.min.js"/>
    <asset:javascript src="riverscript-prf.js"/>
    <asset:javascript src="rivescript-contrib-coffeescript.min.js"/>
    <asset:stylesheet src="bot.css"/>


</head>
<body>
<g:set var="nomeusuario" value="${sec.username()}"></g:set>
<div class="container-fluid" id="main-container">
    <!-- Menú principal lateral ================================================== -->
    <div id="sidebar" class="fixed">
        <ul class="nav nav-list nav-open">
            <li><a href="#" class="fechar-sidebar"><i class="icon-reorder"></i> Fechar menu </a></li>


            <li id="menuInicio" class="ativo"><a href="${createLink(uri: '/')}"><i class="icon-home"></i> Início</a></li>


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
            <h1> Sobreaviso
                <small><i class="icon-double-angle-right"></i> Sistema de Abertura e Acompanhamento de Chamados - NUTEL-RN</small>
            </h1>
        </div>
        <div class="box">





        </div>

    </div>
    <!-- Fim do conteúdo da página ================================================== -->

</div>

</div>
<g:javascript>
    // $(document).ready(function() {
    //     $('#pergunta').keypress(function(e) {
    //         if(e.which == 13) {
    //             var indexDoubleDot = $('#pergunta').val().indexOf(':');
    //             var nomeUsuario = $('#pergunta').val().substring(0,indexDoubleDot) + ": ";
    //             var textarea = $('#historico');
    //             var historico = textarea.val();
    //
    //             var pergunta = $('#pergunta').val();
    //
    //             historico = historico + pergunta + '\n';
    //             pergunta = nomeUsuario;
    //             textarea.val(historico);
    //
    //             $('#pergunta').val(pergunta);
    //             textarea.scrollTop(textarea[0].scrollHeight);
    //
    //
    //
    //         }
    //     });
    // });
</g:javascript>




</body>
</html>