<div class="container">
    <td>
        <table id="tabelaRelatorio" class="table table-condensed" style="width:30%">
            <th class="col-md-2">Data</th>
            <th class="col-md-1">Horas trabalhadas</th>
            <g:each var="relatorio" status="j" in="${listaBusca}">
                <tr>
                    <td>${formatDate(format:'dd-MM-yyyy',date:relatorio.data)}</td>
                    <td>${relatorio.hora}</td>
                </tr>
            </g:each>
        </table>
    </td>
</div>