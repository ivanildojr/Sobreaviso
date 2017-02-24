//= require jquery-2.2.0.min
//= require bootstrap

$(document).ready(function(event) {

    $("#tabelaSobreaviso").on('click', function (event) {
        var allCheckBox = event.target.id
        var pessoa = allCheckBox.charAt(3)
        var dia = allCheckBox.charAt(4)
        var elemento = '#'+allCheckBox

        if($(elemento).prop('checked')){
            $("input[value|="+pessoa+'-'+dia+']').prop('checked', true)
        }else{
            $("input[value|="+pessoa+'-'+dia+']').prop('checked', false)
        }
        console.log(elemento + '-' + $(elemento).prop('checked'))
    });

});