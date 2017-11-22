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
<g:set var="nomeusuario" value="${sec.username()}"></g:set>



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
            <br>
            <div align="center">Saldo horário da semana até <g:formatDate format="dd-MM-yyyy" date="${new Date()}"/></div>
            <table align="center" border="1" cellpadding="5" cellspacing="0" class="table table-condensed" style="width:30%">
                <tr>
                    <th>Nome Servidor</th>
                    <th>Horas Trabalhadas</th>
                    <th>Horas Efetivas <br> Sobreaviso</th>
                    <th>CH Semana</th>
                    <th>Saldo</th>
                </tr>
                <tr align="center">
                    <td>${servidores[0]}</td>
                    <td>${horasTrabalhadas[0].trunc(4)}</td>
                    <td>${horasSobreaviso[0].trunc(4)}</td>
                    <td>${chSemana}</td>
                    <td>${(horasTrabalhadas[0] + horasSobreaviso[0] - chSemana).trunc(4)}</td>
                </tr>
                <tr align="center">
                    <td>${servidores[1]}</td>
                    <td>${horasTrabalhadas[1].trunc(4)}</td>
                    <td>${horasSobreaviso[1].trunc(4)}</td>
                    <td>${chSemana}</td>
                    <td>${(horasTrabalhadas[1] + horasSobreaviso[1] - chSemana).trunc(4)}</td>
                </tr>
                <tr align="center">
                    <td>${servidores[2]}</td>
                    <td>${horasTrabalhadas[2].trunc(4)}</td>
                    <td>${horasSobreaviso[2].trunc(4)}</td>
                    <td>${chSemana}</td>
                    <td>${(horasTrabalhadas[2] + horasSobreaviso[2] - chSemana).trunc(4)}</td>
                </tr>
            </table>

            %{--<g:each var="servidor" in="${servidores}">--}%
                %{--${servidor}--}%
            %{--</g:each>--}%
        </div>
        <br>
            <div align="center">Banco de Horas mês de <g:formatDate format="dd-MM-yyyy" date="${new Date()}"/></div>
            <table align="center" border="1" cellpadding="5" cellspacing="0" class="table table-condensed" style="width:30%">
                <tr>
                    <th>Nome Servidor</th>
                    <th>Saldo Banco Horas</th>
                </tr>
                <tr align="center">
                    <td>${servidores[0]}</td>
                    <td>${bh[0].intValue()} Horas e ${((bh[0]-(bh[0].intValue()))*60).round()} Minutos</td>

                </tr>
                <tr align="center">
                    <td>${servidores[1]}</td>
                    <td>${bh[1].intValue()} Horas e ${((bh[1]-(bh[1].intValue()))*60).round()} Minutos</td>
                </tr>
                <tr align="center">
                    <td>${servidores[2]}</td>
                    <td>${bh[2].intValue()} Horas e ${((bh[2]-(bh[2].intValue()))*60).round()} Minutos</td>
                </tr>
            </table>
            <br>

        </div>

    </div>
    <!-- Fim do conteúdo da página ================================================== -->


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