<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    %{--<asset:javascript src="prf/jquery.js"/>   Removido pois já está inserido no sistema  --}%
    <asset:javascript src="sobreaviso.js"/>
    <asset:stylesheet src="datepicker/css/bootstrap-datepicker.css"/>
    %{--<asset:stylesheet src="grails-app/assets/stylesheets/rudsom.css"/>--}%
    <asset:javascript src="datepicker/js/bootstrap-datepicker.min.js"/>
    <asset:javascript src="datepicker/locales/bootstrap-datepicker.pt-BR.min.js"/>
    <style>

    input[id=dataHistorico]{
        width: auto;
        text-align: center;
        font-weight: bold;
        font-size: xx-small;
    }
    span[id=R] {
        color: #0000FF;
    }
    .col-md-6{
        text-align: center;
        /*color: red;*/
    }
    span[id=colunas] {
        color: red;
    }

    .table th, .table td {
        /*text-align: center;*/
        /*align-items: center;*/
        text-align: center;
        vertical-align: middle;
        horiz-align: center;
    }

    p.Texto_Centralizado {
        font-size:13pt;
        font-family:Times New Roman;
        text-align:center;
        text-transform:uppercase;
        margin:0pt;
        word-wrap:normal;
        vertical-align: middle;
        horiz-align: center;
    }

    #mesAno {
        font-size: medium;
        text-align: center;
        /*vertical-align: middle;*/
        /*horiz-align: center;*/
    }

    #celOcorrencia{
        width: 70%;
    }

    #celFixo {
        width: 85px;
        vertical-align: middle;
    }

    /*#celFixo2 {*/
    /*width: 10%;*/
    /*vertical-align: middle;*/
    /*}*/

    #divMsg{
        margin: 0 auto !important;
        text-align: center;
        font-weight: bold;
        font-size: medium;
    }

    /*input[name=checkListI] {*/
    /*color: #0000FF;*/
    /*outline: 2px solid green;*/
    /*}*/
    /*input[name=checkListR] {*/
    /*color: #0000FF;*/
    /*outline: 2px solid blue;*/
    /*}*/
    /*input[name=checkListT] {*/
    /*color: #0000FF;*/
    /*outline: 2px solid red;*/
    /*}*/

    input[id=datepicker] {
        font-size: 14px;
        line-height: 1.428571429
    }

    </style>

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
        <div align="center">
            <br>
                <g:form>
                    %{--ATENDENTE:--}%
                    %{--<g:select name="atendente" optionKey="nome" optionValue="nome"--}%
                              %{--from="${sobreavisonutel.Atendentes.listOrderByNome()}"--}%
                    %{--/>--}%
                    %{--<br><br>--}%
                    <div class="input-daterange" id="calendario" >
                        MÊS:
                        <input required=true type="text" class="input-medium" name="dataInicio" />
                    </div>
                    <div align="center" name="gerarBtn">
                        <g:actionSubmit value="Gerar" action="boletim"/>
                    </div>
                </g:form>
        </div>
        <br>
        <div>
                <table align="center" border="1" cellpadding="5" cellspacing="0"  id="tabelaRelatorio" class="table table-condensed" style="width:30%">
                <tr style="background-color: #ddd;">
                    <th colspan="4">
                        <span id=mesAno > ${mesAno} </span>
                    </th>
                </tr>
                <th colspan="2" class="col-md-1">Dia</th>
                <th class="col-md-1">Período</th>
                <th class="col-md-1">Atendente</th>
                <g:each var="relatorio" status="j" in="${listaBusca}">
                    <g:if test="${relatorio.diaSemana == 'Domingo' || relatorio.diaSemana == 'Sábado'}">
                        <tr align="center" class="success" style="font-weight: bold">
                    </g:if>
                    <g:else>
                        <tr align="center" >
                    </g:else>
                        <td> ${formatDate(format:'dd',date:relatorio.data)} </td>
                        <td> ${relatorio.diaSemana}
                            <g:if test="${relatorio.feriado}">
                                <span class="label label-warning">${relatorio.feriado}</span>
                            </g:if>
                        </td>
                        <td> ${relatorio.periodo} </td>
                        <td> ${relatorio.atendente}</td>
                    </tr>
                </g:each>
                </tr>
            </table>
            <div>
            <br><br>
        </div>
        </div>
    </div>
    <!-- Fim do conteúdo da página ================================================== -->



    <g:javascript>



        $(document).ready(function() {
            $('#alertaData').hide()
            $('#gerarBtn').on('click',function (e) {
                alert("O campo data deve ser preenchido!");
                if($('#dataInicio').val().length == 0){
                    e.preventDefault()
                    $('#alertaData').show()
                    alert("O campo data deve ser preenchido!");
                }
            });

            $('#calendario').datepicker({
                format: "dd/mm/yyyy",
                minViewMode: 1,
                maxViewMode: 2,
                language: "pt-BR",
                autoclose: true,
                todayHighlight: true,
                orientation: "bottom left"
            });

            $('#calendario').on('change',function(){
                var periodo = $('#calendario').val();
                //alert(periodo);
            });


        });
    </g:javascript>
</body>
</html>