<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="\${message(code: '${propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
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



                        <div id="list-${propertyName}" class="content scaffold-list" role="main">

                            <g:if test="\${flash.message}">
                                <div class="message" role="status">\${flash.message}</div>
                            </g:if>
                            <f:table collection="\${${propertyName}List}" />

                            <div class="pagination">
                                <g:paginate total="\${${propertyName}Count ?: 0}" />
                            </div>
                        </div>


                    </div>
                </div>
            </div>
        </div>
    <!-- Fim do conteúdo da página ================================================== -->
    </body>
</html>