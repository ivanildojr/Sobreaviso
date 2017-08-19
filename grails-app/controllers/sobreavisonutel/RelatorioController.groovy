package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured
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

        def atendente = params.list("atendentes")  //recebe atendentes e dataInicio da view
        def dataInicio = params.list("dataInicio")
        def dataFim = params.list("dataInicio")
        dataInicio = new Date().format("yyyy-MM-dd")
        dataFim = new Date().format("yyyy-MM-dd")

        def atendenteId = Atendentes.findByNome(atendente)
        atendenteId = atendenteId.id
        def escala = Historico.executeQuery("select count(dataEscala) from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim'")
        println escala

        render(view: "index")
    }
}
