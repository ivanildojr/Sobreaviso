<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main"/>
    %{--<asset:javascript src="prf/jquery.js"/>   Removido pois já está inserido no sistema--}%
    <asset:javascript src="sobreaviso.js"/>
    <asset:stylesheet src="datepicker/css/bootstrap-datepicker.css"/>
    %{--<asset:stylesheet src="grails-app/assets/stylesheets/rudsom.css"/>--}%
    <asset:javascript src="datepicker/js/bootstrap-datepicker.min.js"/>
    <asset:javascript src="datepicker/locales/bootstrap-datepicker.pt-BR.min.js"/>
    <style>
    input[id=datepicker] {
        font-size: 14px;
        line-height: 1.428571429
    }

    .table th, .table td {
        text-align: center;
    }

    </style>

    <script type="text/javascript">
        $(document).ready(function() {
//            alert("funcionou")
        });

            function alterarOcorrencia(id){
            $.ajax({
                method: "POST",
                url: "editar",
                data: { "id": id },
                dataType: 'json',
                success: function(data){
                    console.log(data)
                    jQuery('input[name=data]').val(data.data)
                    jQuery('input[name=ocorrencia]').val(data.detalhado)

                }
            })
        }

    </script>

</head>

<body>

<!-- Conteúdo da página    ================================================== -->
<div id="page-content" class="clearfix fixed">
    ﻿
    <div class="page-header">
        <h1>Sobreaviso
            <small><i class="icon-double-angle-right"></i> Sistema de Abertura e Acompanhamento de Chamados - NUTEL-RN
            </small>
        </h1>
    </div>

    <div class="box">
        <div align="center">
            <br>
            <g:form id="formOcorrencia" class="form-inline">
                <div class="form-group">
                ATENDENTE:
                <g:select name="atendente" optionKey="nome" optionValue="nome"
                          from="${sobreavisonutel.Atendentes.listOrderByNome()}"/>
                </div>
                <br>
                <div class="form-group input-group date" id="calendario">
                    DATA:
                    <input id="data" type="text" class="input-small form-control" name="data">
                    <span class="add-on" style="height:20px"></span>
                </div>
                <br>
                <div class="form-group">
                    INÍCIO: <input class="input-small" type="time" id="horaInicio" name="horaInicio">
                    FIM: <input class="input-small" type="time" id="horaFim" name="horaFim">
                </div>
                <br>
                OCORRÊNCIA: <input class="input-xxlarge" type="text" id="inputOcorrencia" name="ocorrencia">
                <br><br>

                <div align="center" name="registrarBtn">
                    <button id="teste" class="btn btn-large btn-primary" type="button">Teste</button>
                    <g:actionSubmit value="Registrar" action="ocorrencias"/>
                </div>
            </g:form>
            <br>
        </div>

        <div>
            %{--<g:if test="${horasTotal > 0}">--}%
                <table align="center" id="tabelaRelatorio" class="table table-condensed" style="width:100%">
                    <tr>
                        %{--<th colspan="3"><b>${atendente} - ${formatDate(format: 'dd-MM-yyyy', date: dataInicio)} à ${formatDate(format: 'dd-MM-yyyy', date: dataFim)}</b>--}%
                        %{--</th>--}%
                    </tr>
                    <th class="col-md-1">Id</th>
                    <th class="col-md-1">Atendente</th>
                    <th class="col-md-1">Data</th>
                    <th class="col-md-1">Hora Início</th>
                    <th class="col-md-1">Hora Fim</th>
                    <th class="col-md-8">Ocorrência</th>
                    <g:each var="ocorrencia" status="j" in="${listaOcorrencias}">
                        <tr align="center">
                            %{--<td> <g:link id="${ocorrencia.id}" action="editar">${ocorrencia.id} </g:link> </td>--}%
                            <td> <a href="javascript: alterarOcorrencia(${ocorrencia.id})"> ${ocorrencia.id} </a> </td>
                            <td>${ocorrencia.atendentes}</td>
                            <td>${formatDate(format: 'dd-MM-yyyy', date: ocorrencia.data)}</td>
                            <td> ${formatDate(format: 'HH:mm', date: ocorrencia.horaInicio)}</td>
                            <td>${formatDate(format: 'HH:mm', date: ocorrencia.horaFim)}</td>
                            <td>${ocorrencia.resumido}</td>
                        </tr>
                    </g:each>
                    <tr>
                        %{--<td colspan="2"><b>Total em sobreaviso</b></td>--}%
                        %{--<td><b>${horasTotal} horas</b></td>--}%
                    </tr>
                </table>
            %{--</g:if>--}%
        </div>
        <br>

        <div id="alertaData" class="alert alert-danger" role="alert">
            <strong>Operação não permitida!</strong> O campo data deve ser preenchido antes de alterar.
        </div>
    </div>

</div>
<!-- Fim do conteúdo da página ================================================== -->



<g:javascript>

    $(document).ready(function () {
        $('#alertaData').hide()
        $('#gerarBtn').on('click', function (e) {
            alert("O campo data deve ser preenchido!");
            if ($('#dataInicio').val().length == 0) {
                e.preventDefault()
                $('#alertaData').show()
                alert("O campo data deve ser preenchido!");
            }
        });

        $('#data').datepicker({
            language: "pt-BR",
            format: "dd/mm/yyyy",
            clearBtn: true,
            todayHighlight: true,
            orientation: "bottom left"
        });

        $('#data').on('change', function () {
            var periodo = $('#calendario').val();
            //alert(periodo);
        });



    });
</g:javascript>
</body>
</html>