<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'setores.label', default: 'Setores')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
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
                        <div id="edit-setores" class="content scaffold-edit" role="main">

                        <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                        </g:if>
                        <g:hasErrors bean="${this.setores}">
                        <ul class="errors" role="alert">
                            <g:eachError bean="${this.setores}" var="error">
                            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                            </g:eachError>
                        </ul>
                        </g:hasErrors>
                        <g:form resource="${this.setores}" method="PUT">
                            <g:hiddenField name="version" value="${this.setores?.version}" />
                            <fieldset class="form">
                                <f:all bean="setores"/>
                            </fieldset>
                            <fieldset class="buttons">
                                <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
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
