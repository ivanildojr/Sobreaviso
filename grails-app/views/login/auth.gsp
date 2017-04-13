<html>
<head>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title><g:message code='springSecurity.login.title'/></title>
    <style type="text/css" media="screen">
    #login {
        margin: 15px 0px;
        padding: 0px;
        text-align: center;
    }

    #login .inner {
        width: 340px;
        padding-bottom: 6px;
        margin: 60px auto;
        text-align: left;
        border: 1px solid #aab;
        background-color: #f0f0fa;
        -moz-box-shadow: 2px 2px 2px #eee;
        -webkit-box-shadow: 2px 2px 2px #eee;
        -khtml-box-shadow: 2px 2px 2px #eee;
        box-shadow: 2px 2px 2px #eee;
    }

    #login .inner .fheader {
        padding: 18px 26px 14px 26px;
        background-color: #f7f7ff;
        margin: 0px 0 14px 0;
        color: #2e3741;
        font-size: 18px;
        font-weight: bold;
    }

    #login .inner .cssform p {
        clear: left;
        margin: 0;
        padding: 4px 0 3px 0;
        padding-left: 105px;
        margin-bottom: 20px;
        height: 1%;
    }

    #login .inner .cssform input[type="text"] {
        width: 120px;
    }

    #login .inner .cssform label {
        font-weight: bold;
        float: left;
        text-align: right;
        margin-left: -105px;
        width: 110px;
        padding-top: 3px;
        padding-right: 10px;
    }

    #login #remember_me_holder {
        padding-left: 120px;
    }

    #login #submit {
        margin-left: 15px;
    }

    #login #remember_me_holder label {
        float: none;
        margin-left: 0;
        text-align: left;
        width: 200px
    }

    #login .inner .login_message {
        padding: 6px 25px 20px 25px;
        color: #c33;
    }

    #login .inner .text_ {
        width: 120px;
    }

    #login .inner .chk {
        height: 12px;
    }
    </style>
</head>

<body>
<div id="main-container" class="container-fluid">
    <div class="fixed" id="sidebar"></div>
    <div id="main-content" class="clearfix" style="margin-left: 0px !important;">
        <div class="menu-nav fixed">
            <ul class="nav inline">
                <li class="active"><a href="#modalContato" data-toggle="modal">Fale Conosco</a></li>
            </ul>
        </div>
        <div id="page-content" class="clearfix fixed">

            %{-- Início pagina login--}%
            <div class="row-fluid">
                <div class="span7">
                    <div class="span2 text-center">
                        <img width="70" height="84" src="../assets/prf-brasao-login.png">
                    </div>

                    <div class="page-header position-relative clearfix span12">
                        <h1>PRF Segurança </h1>
                    </div>

                    <div class="position-relative clearfix span12">
                        <h5>Usuário NUTEL-RN</h5>
                    </div>
                </div>

                <div class="span4">
                    <div class="well">
                        <span class="span12 page-header">
                            <h4>Faça o seu login
                            </h4>
                        </span>

                        <div class="fheader"><g:message code='springSecurity.login.header'/></div>

                        <g:if test='${flash.message}'>
                            <div class="login_message">${flash.message}</div>
                        </g:if>

                        <form action="${postUrl ?: '/login/authenticate'}" method="POST" id="loginForm" class="cssform" autocomplete="off">
                            <br><label for="username"><g:message code='springSecurity.login.username.label'/>:</label>
                            <input type="text" class="ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all span12" name="${usernameParameter ?: 'username'}" id="username"/>
                            <br><label for="password"><g:message code='springSecurity.login.password.label'/>:</label>
                            <input type="password" class="ui-inputfield ui-password ui-widget ui-state-default ui-corner-all span12" name="${passwordParameter ?: 'password'}" id="password"/>

                            <input class="ui-button-text ui-c" type="submit" id="submit" value="${message(code: 'springSecurity.login.button')}"/>


                            <br><input type="checkbox" class="chk" name="${rememberMeParameter ?: 'remember-me'}" id="remember_me"/> <g:if test='${hasCookie}'>checked="checked"</g:if>
                            <label for="remember_me"><g:message code='springSecurity.login.remember.me.label'/></label>
                            <br>
                            <br>
                            <a href="#">Esqueceu a senha?</a><input type="hidden" name="javax.faces.ViewState" id="javax.faces.ViewState" value="75378323976833339:-2268184326119953370" autocomplete="off">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    (function() {
        document.forms['loginForm'].elements['${usernameParameter ?: 'username'}'].focus();
    })();
</script>



%{--Fim página login--}%

</body>
</html>
