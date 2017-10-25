<g:if test="${horasTotal>0}">
    <table align="center" id="tabelaRelatorio" class="table table-condensed" style="width:30%">
        <th class="col-md-4">Data</th>
        <th class="col-md-4">Período</th>
        <th class="col-md-4">Horas em sobreaviso</th>
        <g:each var="relatorio" status="j" in="${listaBusca}">
            <tr>
                <td> ${formatDate(format:'dd-MM-yyyy',date:relatorio.data)} </td>
                <td> ${relatorio.periodo} </td>
                <td> ${relatorio.hora} </td>
            </tr>
        </g:each>
        <tr>
            <td colspan="2"><b>Total em sobreaviso</b></td>
            <td><b>${horasTotal} horas</b></td>
        </tr>
    </table>
</g:if>