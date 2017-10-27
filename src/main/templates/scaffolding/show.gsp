<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="\${message(code: '${propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
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


                    <div id="show-${propertyName}" class="content scaffold-show" role="main">

                        <g:if test="\${flash.message}">
                        <div class="message" role="status">\${flash.message}</div>
                        </g:if>
                        <f:display bean="${propertyName}" />
                        <g:form resource="\${this.${propertyName}}" method="DELETE">
                            <fieldset class="buttons">
                                <g:link class="edit" action="edit" resource="\${this.${propertyName}}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                                <input class="delete" type="submit" value="\${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('\${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
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
