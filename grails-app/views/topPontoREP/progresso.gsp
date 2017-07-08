<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    %{--<asset:javascript src="prf/jquery.js"/>   Removido pois já está inserido no sistema  --}%
    <asset:javascript src="sobreaviso.js"/>

</head>
<body>

    <!-- Conteúdo da página    ================================================== -->
    <div id="page-content" class="clearfix fixed">
        ﻿
        <div class="page-header">
            <h1> Sobreaviso
                <small><i class="icon-double-angle-right"></i> Sistema de Abertura e Acompanhamento de Chamados - NUTEL-RN</small>
            </h1>
        </div>
        <div class="box">


        <div>

            <div align="center">
                <g:form>
                    <g:submitToRemote action="progresso"  name="progressButton" value="start...."/>
                    <g:jprogress progressId="123" trigger="progressButton"/>
                </g:form>
            </div>
        </div>


        </div>

    </div>
    <!-- Fim do conteúdo da página ================================================== -->

</body>
</html>