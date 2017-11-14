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
    /*input[type=text] {*/
        /*width: auto;*/
        /*padding: 1px 1px;*/
        /*box-sizing: border-box;*/
        /*border: none;*/
        /*background-color: #ffffff;*/
        /*box-shadow: none;*/
        /*text-align: center;*/
        /*font-weight: bold;*/
        /*font-size: xx-small;*/
    /*}*/
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

    #mesAno {
        font-size: large;
        text-align: center;
        /*vertical-align: middle;*/
        /*horiz-align: center;*/
    }

    #celOcorrencia{
         width: 70%;
    }

    #celFixo {
        width: 7%;
        vertical-align: middle;
    }

    #celFixo2 {
        width: 10%;
        vertical-align: middle;
    }

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
                ATENDENTE:
                <g:select name="atendente" optionKey="nome" optionValue="nome"
                          from="${sobreavisonutel.Atendentes.listOrderByNome()}"
                />
                <br><br>
                <div class="input-daterange" id="calendario" >
                    PERÍODO:
                    <input required=true type="text" class="input-medium" name="dataInicio" />
                </div>
                <br>
                <div align="center" name="gerarBtn">
                    <g:actionSubmit value="Gerar" action="gerador"/>
                </div>
            </g:form>
        </div>
        <br>
        <div>
            <g:if test="${horasTotal > 0}">
                <table align="center" border="1" cellpadding="5" cellspacing="0"  id="tabelaRelatorio" class="table table-condensed" style="width:30%">
                    <tr>
                        <th colspan="4"><b> ${atendenteNomeCompleto} </th>
                    </tr>
                    <tr>
                        <th colspan="4"> MÊS DE REFERÊNCIA:
                            <span id=mesAno class="label label-info"> ${mesAno} </span>
                        </th>
                    </tr>
                    <th class="col-md-1">Data</th>
                    <th class="col-md-1">Dia</th>
                    <th class="col-md-1">Período</th>
                    <th class="col-md-1">Horas em sobreaviso</th>
                    <g:each var="relatorio" status="j" in="${listaBusca}">
                        <tr align="center" >
                            <td> ${formatDate(format:'dd-MM-yyyy',date:relatorio.data)} </td>
                            <td> ${relatorio.diaSemana} </td>
                            <td> ${relatorio.periodo} </td>
                            <td> ${relatorio.hora} h</td>
                        </tr>
                    </g:each>
                    <tr align="center">
                        <td colspan="3">Sobreaviso</td>
                        <td>${horasTotal} h</td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">Acionamentos em escala</td>
                        <td>${stringacionamentoNaEscala}</td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">Sobreaviso - Acionamentos em escala</td>
                        <td>${tempoSobreavisoMenosAcionamento}</td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">Sobreaviso sem acionamentos &#247 3</td>
                        <td>${stringSobreAvisoDiv3}</td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">Acionamentos fora da escala</td>
                        <td>${stringHoraForaEscala}</td>
                    </tr>
                    <tr align="center">
                        <td colspan="3"><b>Lançamento no ponto</b></td>
                        <td><b>${tempoPonto}</b></td>
                    </tr>
                    </tr>
                </table>
                <div>
                <br><br>
                <g:if test="${!ocorrenciaList.empty}">
                    <table align="center" border="1" cellpadding="5" cellspacing="0" id="tabelaOcorrencia" class="table table-condensed" style="width:90%">
                        <th class="col-md-1" id="celFixo">Data</th>
                        <th class="col-md-1" id="celFixo">Início - Fim</th>
                        <th class="col-md-1" id="celFixo2">Duração</th>
                        <th class="col-md-1"id="celOcorrencia">Ocorrência</th>
                        <g:each var="ocorrencia" status="k" in="${ocorrenciaList}">
                            %{--<g:if test="${}">--}%
                                %{--<tr align="center" FONT COLOR="red">--}%
                                    %{--<td> ${ocorrencia.data} </td>--}%
                                    %{--<td> ${ocorrencia.horaInicio} - ${ocorrencia.horaFim} </td>--}%
                                    %{--<td> ${ocorrencia.duracao} </td>--}%
                                    %{--<td> ${ocorrencia.relato} </td>--}%
                                %{--</tr>--}%
                            %{--</g:if>--}%
                            %{--<g:else>--}%
                                <tr align="center">
                                    <td> ${ocorrencia.data} </td>
                                    <td> ${ocorrencia.horaInicio} - ${ocorrencia.horaFim} </td>
                                    <td> ${ocorrencia.duracao} </td>
                                    <td> ${ocorrencia.relato} </td>
                                </tr>
                            %{--</g:else>--}%
                        </g:each>
                            %{--<td colspan="2" align="center" valign="middle" ><b>Acionamentos</b></td>--}%
                            %{--<td align="center"><b>${tempoTrabTotal}</b></td>--}%
                            %{--<td></td>--}%
                    </table>
                </g:if>
                <g:else>
                    <div id="divMsg" align="center" style="width:30%">
                        <div class="alert alert-danger" role="alert">Não existe ocorrências registradas no período!</div>
                    </div>
                </g:else>
                    <br>
                </div>

            </g:if>
        </div>
        <br>

        <div id="alertaData" class="alert alert-danger" role="alert">
            <strong>Operação não permitida!</strong> O campo data deve ser preenchido antes de alterar.
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