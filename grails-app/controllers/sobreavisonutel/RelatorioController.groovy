package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class RelatorioController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def springSecurityService

    def index() {
        //render "some text"
    }

    def gerador() {

        def atendente = params.list("atendentes")
        def dataInicio = params.list("dataInicio")
        dataInicio = Date.parse("yyyy-MM-dd", params.getProperty("dataInicio"))
        println atendente
        println dataInicio

        def dataFim = "2017-08-31 00:00:00"

        def atendenteId = Atendentes.findByNome(atendente)
        atendenteId = atendenteId.id
        println "atendenteID:" + atendenteId

        //def escala = Historico.executeQuery("select count(dataEscala) from Historico where atendentes_id=$atendenteId and dataEscala=$dataInicio")

        println escala



        render(view: "index")
    }
}
