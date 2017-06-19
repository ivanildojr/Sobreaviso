<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    %{--<asset:stylesheet src="application.css"/>--}%

    <asset:stylesheet src="prf/bootstrap.css"/>
    <asset:stylesheet src="prf/prf-sistemas-internos.css"/>
    <asset:stylesheet src="prf/animacaoprogresso.css"/>
    <asset:javascript src="prf/jquery.js"/>
    <asset:javascript src="prf/bootstrap.js"/>
    <asset:javascript src="prf/bootstrap-inputmask.min.js"/>
    <asset:javascript src="prf/alertify.min.js"/>
    <asset:javascript src="prf/prf.js"/>


    <g:layoutHead/>
</head>
<body>
<div class="navbar navbar-inverse fixed">
    <div class="navbar-inner">
        <div class="container">
            <div class="brand-content"><a class="brand" href="https://10.15.0.8:8080/sobreaviso"> </a></div>
            <span class="nome-sistema">Sobreaviso NUTEL-RN</span>


            <sec:ifLoggedIn>
                <div class="login-sistema">
                    %{--<form id="formLogout" name="formLogout" method="post" action="/dprfseguranca/admin/principal" enctype="application/x-www-form-urlencoded" style="margin: 0; padding: 0;">--}%
                        <div class="btn-group">
                            <a id="ivanildo" href="#" class="btn btn-user dropdown-toggle" data-toggle="dropdown">
                                <i class="icon-user"></i><sec:loggedInUserInfo field='username'/>
                                <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu">
                                <li>
                                    <g:link controller="usuario" action="meusDados"><i class="icon-user"></i> Meus dados</g:link>
                                </li>
                                <li>

                                    <g:link controller="logout"><i class="icon-off"></i> Sair</g:link>
                                </li>
                            </ul>
                        </div>
                    %{--</form>--}%
                </div>
            </sec:ifLoggedIn>

        </div>
    </div>
</div>

<div class="container-fluid" id="main-container">
    <!-- Menú principal lateral ================================================== -->
    <div id="sidebar" class="fixed">
        <ul class="nav nav-list nav-open">
            <li><a href="#" class="fechar-sidebar"><i class="icon-reorder"></i> Fechar menu </a></li>

            <li id="menuInicio" class="ativo"><a href="/sobreaviso"><i class="icon-home"></i> Início</a></li>

            <li id="menuAgenda"><g:link controller="escala" action="agenda"><i class="icon-tasks"></i>Agenda</g:link></li>

            <li id="menuHistorico"><g:link controller="escala" action="historico"><i class="icon-calendar"></i>Histórico Escala</g:link></li>

            <li id="menuTopPontoREP"><g:link controller="topPontoREP" action="pegaMarcacoes"><i class="icon-calendar"></i>Saldo Horário Semana</g:link></li>

            <li id="menuAtualizaTodasMarcacoes"><a href="#modalAtualizaTudo" data-toggle="modal"><i class="icon-calendar"></i>Atualizar Todo Histórico</a></li>

            <li id="nutelRobot"><g:link controller="escala" action="chatBot"><i class="icon-user-md"></i>Suporte On-Line</g:link></li>


        </ul>
        <ul class="nav nav-list nav-close" style="display:none">
            <li><a href="#" class="fechar-sidebar"><i class="icon-reorder"></i> Abrir menu</a></li>
        </ul>
    </div>        <div id="main-content" class="clearfix">
    <!-- Breadcrumb    ================================================== -->
    <!-- Navegação secundária    ================================================== -->
    <div class="menu-nav fixed">
        <ul class="nav inline">
            <li class="active"><a href="#modalContato" data-toggle="modal">Fale Conosco</a></li>
        </ul>
    </div>

    <div class="modal fade" id="modalContato" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Digite aqui sua mensagem</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <label for="email">E-mail:</label><input id="email" type="email"/>
                    <div class="form-group">
                        <label for="mensagem">Mensagem:</label>
                        <textarea class="form-control" rows="5" id="mensagem"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
                    <button type="button" class="btn btn-primary">Enviar</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="modalAtualizaTudo" tabindex="-1" role="dialog" aria-labelledby="atualizaTudo" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <div align="center">
                    <h5 class="modal-title" id="atualizaTudo">Atualizar Dados</h5>
                    <svg class="progress-circle indefinite" width="100" height="100">
                        <g transform="rotate(-90,50,50)">
                            <circle class="bg" r="20" cx="25" cy="25" fill="none"></circle>
                            <circle class="progress" r="20" cx="25" cy="25" fill="none"></circle>
                        </g>
                    </svg>
                    </div>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    Esse procedimento removerá todos os registros de ponto do banco Sobreaviso e buscará novamente no banco do TopPontoREP.
                    <br>Não realizar esse procedimento muitas vezes pois pode impactar na performance do banco do TopPontoREP.
                    <br>Procedimento busca todas as marcações de ponto desde o ano de 2013 para os servidores em sobreaviso.

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
                    <button type="button" class="btn"><g:link controller="topPontoREP" action="atualizaTudo">Atualizar Todo Histórico</g:link></button>
                </div>
            </div>
        </div>
    </div>


    <g:layoutBody/>


    </div>

    </div>

    <footer class="footer">
        <div class="container">

            <ul class="footer-links"><li><a href="#">Topo</a></li><li><a href="#modalContato" data-toggle="modal"> Fale Conosco</a></li></ul>
            <p class="inline">Sobreaviso NUTEL-RN
                <small>1.0</small>
            </p>
        </div>
    </footer>



</body>
</html>
