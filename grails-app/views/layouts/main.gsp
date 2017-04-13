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


    <g:layoutBody/>


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
