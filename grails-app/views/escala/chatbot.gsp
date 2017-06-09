<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    %{--<asset:javascript src="prf/jquery.js"/>   Removido pois já está inserido no sistema  --}%
    <asset:javascript src="sobreaviso.js"/>
    <asset:javascript src="rivescript.min.js"/>
    <asset:javascript src="riverscript-prf.js"/>
    <asset:javascript src="rivescript-contrib-coffeescript.min.js"/>
    <asset:stylesheet src="bot.css"/>


</head>
<body>
<g:set var="nomeusuario" value="${sec.username()}"></g:set>



    <!-- Conteúdo da página    ================================================== -->
    <div id="page-content" class="clearfix fixed">
        ﻿
        <div class="page-header">
            <h1> Sobreaviso
                <small><i class="icon-double-angle-right"></i> Sistema de Abertura e Acompanhamento de Chamados - NUTEL-RN</small>
            </h1>
        </div>
        <div class="box">
            %{--<button id="iniciar" class="btn-primary " href="#modalContato" data-toggle="modal">Iniciar Chat</button> Quantidade de usuários on-line: !--}%
            <button id="iniciar" class="btn-primary " data-toggle="modal">Iniciar Chat</button> Quantidade de usuários on-line: !
            <div class="modal fade" id="modalContato" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Chat com NUTEL-RN</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">

                            <div class="form-group">
                                %{--<label for="historico">Chat:</label>--}%
                                %{--<textarea style="width:100%;" class="form-control" rows="5" id="historico"></textarea>--}%
                                <div class="try-dialogue" id="historico"></div>
                            </div>
                            %{--<label for="pergunta"></label><input style="width:100%;" id="pergunta" type="text" value="${nomeusuario}: "/>--}%
                            <label for="pergunta"></label><input style="width:100%;" id="pergunta" type="text" />
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>

                        </div>
                    </div>
                </div>
            </div>



        </div>

    </div>
    <!-- Fim do conteúdo da página ================================================== -->


<g:javascript>
    // $(document).ready(function() {
    //     $('#pergunta').keypress(function(e) {
    //         if(e.which == 13) {
    //             var indexDoubleDot = $('#pergunta').val().indexOf(':');
    //             var nomeUsuario = $('#pergunta').val().substring(0,indexDoubleDot) + ": ";
    //             var textarea = $('#historico');
    //             var historico = textarea.val();
    //
    //             var pergunta = $('#pergunta').val();
    //
    //             historico = historico + pergunta + '\n';
    //             pergunta = nomeUsuario;
    //             textarea.val(historico);
    //
    //             $('#pergunta').val(pergunta);
    //             textarea.scrollTop(textarea[0].scrollHeight);
    //
    //
    //
    //         }
    //     });
    // });
</g:javascript>




</body>
</html>