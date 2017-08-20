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
                <div>
                    <td>
                        DATA INICIAL:
                        <g:datePicker name="dataInicio" precision="day" noSelection="['':'-Choose-']"/>
                    </td>
                </div>
                <br>
                <div>
                    <td>
                        DATA FINAL:
                        <g:datePicker name="dataFim" precision="day" noSelection="['':'-Choose-']"/>
                    </td>
                </div>
                <br>
                ATENDENTE:
                <g:select name="atendente" optionKey="nome" optionValue="nome"
                          from="${sobreavisonutel.Atendentes.listOrderByNome()}"
                />
                <br><br>
                <div class="input-daterange input-group" id="calendario">
                    PERÍODO:
                    <g:field type="text" class="form-control" name="dataInicio" />
                    <span class="input-group-addon">ATÉ</span>
                    <g:field type="text" class="form-control" name="dataFim" />
                </div>
                <br>
                <div align="center" name="gerarBtn">
                    <g:actionSubmit value="Gerar" action="gerador" />
                </div>
            </g:form>
        </div>

        <div>
            <table id="tabelaRelatorio1" class="table" border="1" text-align="center">
              <tr>
                <td>
                <table id="tabelaRelatorio2" class="table" border="1" text-align="center">
                    <g:each var="dia" status="i" in="${listDia}">
                        <tr>
                            <td>${dia}</td>
                        </tr>
                    </g:each>
                </table>
                <td>
                <table id="tabelaRelatorio3" class="table" border="0" text-align="center">
                    <g:each var="hora" status="j" in="${listHora}">
                        <tr>
                        <td>${hora}</td>
                        </tr>
                    </g:each>
                </table>
                </td>
              </td>
              </tr>
            </table>
        </div>






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
           clearBtn: true,
           language: "pt-BR",
           todayHighlight: true
       });

       $('#calendario').on('change',function(){
           var periodo = $('#calendario').val();
           //alert(periodo);
       });


   });
</g:javascript>
</body>
</html>