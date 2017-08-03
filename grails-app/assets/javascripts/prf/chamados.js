var fixHelperModified = function(e, tr) {
    console.log("Teste");
        var $originals = tr.children();
        var $helper = tr.clone();
        $helper.children().each(function(index) {
            $(this).width($originals.eq(index).width())
        });
        return $helper;
    },
    updateIndex = function(e, ui) {
        $('td.index', ui.item.parent()).each(function (i) {
            $(this).html(i + 1);
        });
    };

$( function() {
    $("#tabelachamados tbody").sortable({
        helper: fixHelperModified,
        stop: updateIndex
    }).disableSelection();
});
