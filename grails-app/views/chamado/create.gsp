<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'chamado.label', default: 'Chamado')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
        <asset:stylesheet href="prf/chamados.css"/>
    </head>
    <body>
    <!-- Conteúdo da página    ================================================== -->
    <div id="page-content" class="clearfix fixed">
        ﻿
        <div class="page-header">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
        </div>
        <div class="box">
            <div class="box-content padded">
                <div class="panel panel-default">

                    <div id="create-chamado" class="content scaffold-create" role="main">

                        <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                        </g:if>
                        <g:hasErrors bean="${this.chamado}">
                        <ul class="errors" role="alert">
                            <g:eachError bean="${this.chamado}" var="error">
                            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                            </g:eachError>
                        </ul>
                        </g:hasErrors>
                        <g:form action="save">
                            <fieldset class="form">
                                <div class="container-fluid">
                                    <div class="row">
                                        %{--<div class="col-md-4">Ordem de Atendimento: <g:field name="ordemAtendimento" type="text" value="Pegar no banco" readonly="true"/></div>--}%
                                        <div class="col-md-4">Status: <g:field name="status" type="text" value="aberto" readonly="true"/></div>
                                        <div class="col-md-4">Usuário: <g:field name="usuarioAbertura" type="text" value="${sec.username()}" readonly="true"/></div>
                                    </div>
                                    <g:set var="dateNow" value="${new Date()}" />
                                    <div class="row">
                                        <div class="col-md-4">Data: <g:field name="dataAbertura" type="text" value="${formatDate(format:'dd-MM-yyyy',date:dateNow)}" readonly="true"/></div>
                                        <div class="col-md-4">Hora: <g:field name="horaAbertura" type="text" value="${formatDate(format:'HH:mm',date:dateNow)}" readonly="true"/></div>
                                        <div class="col-md-4"><f:field bean="chamado" property="setor" /></div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">Descrição do Problema*: <br><g:textArea id="descricao" name="descricao" type="text" required="true"/></div>

                                    </div>
                                </div>



                                %{--<f:field property="usuarioAbertura" bean="chamado"><sec:loggedInUserInfo field='username'/></f:field>--}%
                                %{--<f:field property="dataAbertura" bean="chamado"><g:formatDate format="dd-MM-yyyy" date="${dateNow}"/></f:field>--}%
                                %{--<f:field property="horaAbertura" bean="chamado"><g:formatDate format="HH:mm" date="${dateNow}"/></f:field>--}%


                            </fieldset>
                            <fieldset class="buttons">
                                <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                            </fieldset>
                        </g:form>
                    </div>
    </div>
    </div>
    </div>
    </div>
    <!-- Fim do conteúdo da página ================================================== -->
    </body>
</html>
