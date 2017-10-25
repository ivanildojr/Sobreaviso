var fixHelperModified = function(e, tr) {
        var $originals = tr.children();
        var $helper = tr.clone();
        $helper.children().each(function(index) {
            $(this).width($originals.eq(index).width())
        });
        return $helper;
    };
    updateIndex = function(e, ui) {
        var stopIndex = ui.item.index();
        var indiceInicial =  ui.item.data("startindex");
        var indiceFinal = stopIndex;
        // console.log("Indice Inicial: " + indiceInicial);
        // console.log("Indice Final: " + indiceFinal);


        /*Responsável por re-ordenar a lista visível*/
        var tdOrdem = $("#tabelachamados tbody").children().find("td.ordem");
        var tdOrdemAtendimento = $("#tabelachamados tbody").children().find("td.ordemAtendimento");
        var ordem = 0;
        $.each(tdOrdem, function (key,value) {
            tdOrdem.eq(key).text(++ordem);
            atualizaOrdemDB(tdOrdemAtendimento,key,ordem);
        });




    };
    updateIndexStart = function(e, ui) {
        $(ui.item).data("startindex", ui.item.index()+1);
    };
$( function() {
    $("#tabelachamados tbody").sortable({
        helper: fixHelperModified,
        start: updateIndexStart,
        stop: updateIndex,
        axis: "y"

    }).disableSelection();
});



