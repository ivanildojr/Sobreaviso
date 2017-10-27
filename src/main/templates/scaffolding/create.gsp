<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="\${message(code: '${propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
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

                    <div id="create-${propertyName}" class="content scaffold-create" role="main">

                        <g:if test="\${flash.message}">
                        <div class="message" role="status">\${flash.message}</div>
                        </g:if>
                        <g:hasErrors bean="\${this.${propertyName}}">
                        <ul class="errors" role="alert">
                            <g:eachError bean="\${this.${propertyName}}" var="error">
                            <li <g:if test="\${error in org.springframework.validation.FieldError}">data-field-id="\${error.field}"</g:if>><g:message error="\${error}"/></li>
                            </g:eachError>
                        </ul>
                        </g:hasErrors>
                        <g:form action="save">
                            <fieldset class="form">
                                <f:all bean="${propertyName}"/>
                            </fieldset>
                            <fieldset class="buttons">
                                <g:submitButton name="create" class="save" value="\${message(code: 'default.button.create.label', default: 'Create')}" />
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
