package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import java.sql.Date

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class RelatorioController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def springSecurityService

    def index() {
        //render "some text"
    }

    def gerador() {
//        def dataInicio = new Date().format("yyyy-MM-dd")
        //def dataFim = new Date().format("yyyy-MM-dd")
        def atendente = params.list("atendentes")  //recebe atendentes e dataInicio da view
        def dataInicio = params.list("dataInicio")
        def dataFim = params.list("dataFim")
        dataInicio = dataInicio[1] //pega só a data
        dataFim = dataFim[1] //pega só a data
        dataInicio = Date.parse("dd/MM/yyyy", dataInicio).format("yyyy-MM-dd")
        dataFim = Date.parse("dd/MM/yyyy", dataFim).format("yyyy-MM-dd")
        println dataInicio
        println dataFim

        def atendenteId = Atendentes.findByNome(atendente)
        atendenteId = atendenteId.id
        def escalaCount = Historico.executeQuery("select count(dataEscala) from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim'")
        println "$escalaCount horas"
        def escala = Historico.executeQuery("select dataEscala, hora from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala ASC")
        println escala
        //while(escala)



        render(view: "index")
    }
}
