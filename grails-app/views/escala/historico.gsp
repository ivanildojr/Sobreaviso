<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    %{--<asset:javascript src="prf/jquery.js"/>   Removido pois já está inserido no sistema  --}%
    <asset:javascript src="sobreaviso.js"/>
    <asset:stylesheet src="datepicker/css/bootstrap-datepicker.css"/>
    <asset:javascript src="datepicker/js/bootstrap-datepicker.min.js"/>
    <asset:javascript src="datepicker/locales/bootstrap-datepicker.pt-BR.min.js"/>
    <style>
        input[type=text] {
            width: auto;
            padding: 1px 1px;
            box-sizing: border-box;
            border: none;
            background-color: #ffffff;
            box-shadow: none;
            text-align: center;
            font-weight: bold;
            font-size: xx-small;
        }
        input[id=dataHistorico]{
            width: auto;
            text-align: center;
            font-weight: bold;
            font-size: xx-small;
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


            <table align="center">
                <tr>
                    <td>
                        <div>Informe a data de pesquisa:</div><input id="dataHistorico" data-provide="datepicker">
                    </td>
                    <td>
                        <g:form action="sobreavisoHistorico">

                            <fieldset class="form">
                                <div align="center">
                                    <table id="tabelaSobreaviso" class=".table-condensed" border="1">
                                        <tr>

                                            <th>Horas/Dias</th>
                                            <g:each var="dia" status="i" in="${diasListNum}">
                                                <th>
                                                    <g:if test="${dia == '1'}">
                                                        Domingo<br>
                                                        <input align="center" type="text" readonly name="domingo" value=""/><br>
                                                        I: <g:checkBox name="allI1" value="I" checked="false"/>
                                                        T: <g:checkBox name="allT1" value="T" checked="false"/>
                                                        R: <g:checkBox name="allR1" value="R" checked="false"/>
                                                    </g:if>
                                                    <g:elseif test="${dia == '2'}">
                                                        Segunda-feira
                                                        <br>
                                                        <input align="center" type="text" readonly name="segunda" value=""/><br>
                                                        I: <g:checkBox name="allI2" value="I" checked="false"/>
                                                        T: <g:checkBox name="allT2" value="T" checked="false"/>
                                                        R: <g:checkBox name="allR2" value="R" checked="false"/>
                                                    </g:elseif>
                                                    <g:elseif test="${dia == '3'}">
                                                        Terça-feira
                                                        <br>
                                                        <input align="center" type="text" readonly name="terca" value=""/><br>
                                                        I: <g:checkBox name="allI3" value="I" checked="false"/>
                                                        T: <g:checkBox name="allT3" value="T" checked="false"/>
                                                        R: <g:checkBox name="allR3" value="R" checked="false"/>
                                                    </g:elseif>
                                                    <g:elseif test="${dia == '4'}">
                                                        Quarta-feira
                                                        <br>
                                                        <input align="center" type="text" readonly name="quarta" value=""/><br>
                                                        I: <g:checkBox name="allI4" value="I" checked="false"/>
                                                        T: <g:checkBox name="allT4" value="T" checked="false"/>
                                                        R: <g:checkBox name="allR4" value="R" checked="false"/>
                                                    </g:elseif>
                                                    <g:elseif test="${dia == '5'}">
                                                        Quinta-feira
                                                        <br>
                                                        <input align="center" type="text" readonly name="quinta" value=""/><br>
                                                        I: <g:checkBox name="allI5" value="I" checked="false"/>
                                                        T: <g:checkBox name="allT5" value="T" checked="false"/>
                                                        R: <g:checkBox name="allR5" value="R" checked="false"/>
                                                    </g:elseif>
                                                    <g:elseif test="${dia == '6'}">
                                                        Sexta-feira
                                                        <br>
                                                        <input align="center" type="text" readonly name="sexta" value=""/><br>
                                                        I: <g:checkBox name="allI6" value="I" checked="false"/>
                                                        T: <g:checkBox name="allT6" value="T" checked="false"/>
                                                        R: <g:checkBox name="allR6" value="R" checked="false"/>
                                                    </g:elseif>
                                                    <g:elseif test="${dia == '7'}">
                                                        Sábado
                                                        <br>
                                                        <input align="center" type="text" readonly name="sabado" value=""/><br>
                                                        I: <g:checkBox name="allI7" value="I" checked="false"/>
                                                        T: <g:checkBox name="allT7" value="T" checked="false"/>
                                                        R: <g:checkBox name="allR7" value="R" checked="false"/>
                                                    </g:elseif>

                                                </th>
                                            </g:each>

                                        </tr>
                                        <g:each var="hora" status="j" in="${horasListNum}">
                                            <tr>
                                                <td>${hora}:00</td>
                                                <g:each var="dia" status="i" in="${diasListNum}">
                                                    <td align="center">
                                                        I: <g:checkBox name="checkList" id="I-${dia}-${hora}" value="I-${dia}-${hora}" checked="${escalaLista.contains('I-'+dia+'-'+hora)}"/>
                                                        T: <g:checkBox name="checkList" id="T-${dia}-${hora}" value="T-${dia}-${hora}" checked="${escalaLista.contains('T-'+dia+'-'+hora)}"/>
                                                        R: <g:checkBox name="checkList" id="R-${dia}-${hora}" value="R-${dia}-${hora}" checked="${escalaLista.contains('R-'+dia+'-'+hora)}"/>
                                                    </td>
                                                </g:each>
                                            </tr>
                                        </g:each>
                                    </table>
                                </div>
                            </fieldset>
                            <fieldset class="buttons">
                                <div align="center"><g:submitButton id="alterarBtn" name="create" class="save" value="${message(code: 'default.button.update.label', default: 'update')}" /></div>
                            </fieldset>

                        </g:form>
                    </td>
                </tr>
            </table>

            <div id="alertaData" class="alert alert-danger" role="alert">

                <strong>Operação não permitida!</strong> O campo data deve ser preenchido antes de alterar.
            </div>
        </div>

    </div>
    <!-- Fim do conteúdo da página ================================================== -->



<g:javascript>

   $(document).ready(function() {
       /*var dataAtual = new Date()
       var dataHistorico = $('#dataHistorico');

       dataHistorico.val(dataAtual.getDate() + '/' + (dataAtual.getMonth()+1) + '/' + dataAtual.getFullYear());
       dataHistorico.dispatchEvent(new Event('change'))
        */
       $('#alertaData').hide()


        $('#alterarBtn').on('click',function (e) {

            if($('#dataHistorico').val().length == 0){
                e.preventDefault()
                $('#alertaData').show()
                //alert("O campo data deve ser preenchido!");
            }
        });

       $('#dataHistorico').datepicker({
           format: "dd/mm/yyyy",
           clearBtn: true,
           language: "pt-BR",
           autoclose: true,
           todayHighlight: true
       });


       $('#dataHistorico').on('change',function(){

                var dados = $('#dataHistorico').val();

                /*Para apagar o preenchimento da tabela*/
                $.each(["1","2","3","4","5","6","7"], function( i, vi ) {
                        $.each(["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"], function( j, vj ) {
                            $.each(["I","T","R"], function( k, vk ) {
                                $("input[value|="+vk+'-'+vi+']').prop('checked', false)
                            });
                        });
                });


                /*Pega os dados do banco e preenche a tabela*/
                $.ajax({
                  url:'${g.createLink(controller: 'escala', action: 'datasAjax')}',
                  dataType: 'json',
                  data: {dataHistorico: $('#dataHistorico').val()}
                  ,
                  success: function (data) {
                    // console.log(data);
                    // console.log(data.dataInicial);

                    var domingo = new Date(data.dataInicial);
                    var segunda = new Date(data.dataInicial);
                    segunda.setDate(segunda.getDate() + 1);
                    var terca = new Date(data.dataInicial);
                    terca.setDate(terca.getDate() + 2);
                    var quarta = new Date(data.dataInicial);
                    quarta.setDate(quarta.getDate() + 3);
                    var quinta = new Date(data.dataInicial);
                    quinta.setDate(quinta.getDate() + 4);
                    var sexta = new Date(data.dataInicial);
                    sexta.setDate(sexta.getDate() + 5);
                    var sabado = new Date(data.dataFinal);


                    $("input[name=domingo]").html(domingo.toLocaleDateString());
                    $("input[name=domingo]").val(domingo.toLocaleDateString());
                    $("input[name=domingo]").prop('value',domingo.toLocaleDateString());
                    $("input[name=segunda]").html(segunda.toLocaleDateString());
                    $("input[name=segunda]").val(segunda.toLocaleDateString());
                    $("input[name=segunda]").prop('value',segunda.toLocaleDateString());

                    $("input[name=terca]").html(terca.toLocaleDateString());
                    $("input[name=terca]").val(terca.toLocaleDateString());
                    $("input[name=terca]").prop('value',terca.toLocaleDateString());

                    $("input[name=quarta]").html(quarta.toLocaleDateString());
                    $("input[name=quarta]").val(quarta.toLocaleDateString());
                    $("input[name=quarta]").prop('value',quarta.toLocaleDateString());

                    $("input[name=quinta]").html(quinta.toLocaleDateString());
                    $("input[name=quinta]").val(quinta.toLocaleDateString());
                    $("input[name=quinta]").prop('value',quinta.toLocaleDateString());

                    $("input[name=sexta]").html(sexta.toLocaleDateString());
                    $("input[name=sexta]").val(sexta.toLocaleDateString());
                    $("input[name=sexta]").prop('value',sexta.toLocaleDateString());

                    $("input[name=sabado]").html(sabado.toLocaleDateString());
                    $("input[name=sabado]").val(sabado.toLocaleDateString());
                    $("input[name=sabado]").prop('value',sabado.toLocaleDateString());


                    $.each(data.escalaSobreavisoHistorico, function( index, value ) {
                        var pessoa = value.charAt(0)
                        var dia = value.charAt(2)
                        var hora = value.substring(4,6)
                        $("input[value|="+pessoa+'-'+dia+'-'+hora+']').prop('checked', true)
                    });




                  },
                  error: function (request, status, error) {
                   console.log(request)
                   console.log(error)
                  }
                });
              });



   });
</g:javascript>
</body>
</html>