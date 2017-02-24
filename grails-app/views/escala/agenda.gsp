<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <asset:javascript src="jquery-2.2.0.min.js"/>
</head>
<body>
<g:form action="sobreaviso">

    <fieldset class="form">
        <div align="center">
                <table id="tabelaSobreaviso" class=".table-condensed">
                    <tr>
                        <th>Horas/Dias</th>
                        <g:each var="dia" status="i" in="${diasListNum}">
                            <th>
                                <g:if test="${dia == '1'}">
                                    Domingo<br>
                                    I: <g:checkBox name="allI1" value="I" checked="false"/>
                                    T: <g:checkBox name="allT1" value="T" checked="false"/>
                                    R: <g:checkBox name="allR1" value="R" checked="false"/>
                                </g:if>
                                <g:elseif test="${dia == '2'}">
                                    Segunda-feira
                                    <br>
                                    I: <g:checkBox name="allI2" value="I" checked="false"/>
                                    T: <g:checkBox name="allT2" value="T" checked="false"/>
                                    R: <g:checkBox name="allR2" value="R" checked="false"/>
                                </g:elseif>
                                <g:elseif test="${dia == '3'}">
                                    Terça-feira
                                    <br>
                                    I: <g:checkBox name="allI3" value="I" checked="false"/>
                                    T: <g:checkBox name="allT3" value="T" checked="false"/>
                                    R: <g:checkBox name="allR3" value="R" checked="false"/>
                                </g:elseif>
                                <g:elseif test="${dia == '4'}">
                                    Quarta-feira
                                    <br>
                                    I: <g:checkBox name="allI4" value="I" checked="false"/>
                                    T: <g:checkBox name="allT4" value="T" checked="false"/>
                                    R: <g:checkBox name="allR4" value="R" checked="false"/>
                                </g:elseif>
                                <g:elseif test="${dia == '5'}">
                                    Quinta-feira
                                    <br>
                                    I: <g:checkBox name="allI5" value="I" checked="false"/>
                                    T: <g:checkBox name="allT5" value="T" checked="false"/>
                                    R: <g:checkBox name="allR5" value="R" checked="false"/>
                                </g:elseif>
                                <g:elseif test="${dia == '6'}">
                                    Sexta-feira
                                    <br>
                                    I: <g:checkBox name="allI6" value="I" checked="false"/>
                                    T: <g:checkBox name="allT6" value="T" checked="false"/>
                                    R: <g:checkBox name="allR6" value="R" checked="false"/>
                                </g:elseif>
                                <g:elseif test="${dia == '7'}">
                                    Sábado
                                    <br>
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
                                <td>
                                    I: <g:checkBox name="checkList" value="I-${dia}-${hora}" checked="${escalaLista.contains('I-'+dia+'-'+hora)}"/>
                                    T: <g:checkBox name="checkList" value="T-${dia}-${hora}" checked="${escalaLista.contains('T-'+dia+'-'+hora)}"/>
                                    R: <g:checkBox name="checkList" value="R-${dia}-${hora}" checked="${escalaLista.contains('R-'+dia+'-'+hora)}"/>
                                </td>
                            </g:each>
                        </tr>
                    </g:each>
                </table>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <div align="center"><g:submitButton name="create" class="save" value="${message(code: 'default.button.update.label', default: 'update')}" /></div>
    </fieldset>

</g:form>
<g:javascript>
    $(document).ready(function() {
        $("#allI1").on('click', function () {
            if (this.checked) {
                $("input[value|='I-1']").prop('checked', true)
            }else{
                $("input[value|='I-1']").prop('checked', false)
            }

        });
    });
</g:javascript>
</body>
</html>