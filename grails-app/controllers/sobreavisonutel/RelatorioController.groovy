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
        println dataInicio
        println dataFim
        dataInicio = Date.parse("dd/MM/yyyy", dataInicio).format("yyyy-MM-dd") //passa a string datainicio pro formato de data, depois coloca na formatacao do banco
        dataFim = Date.parse("dd/MM/yyyy", dataFim).format("yyyy-MM-dd")
//        println dataInicio
//        println dataFim

        def atendenteId = Atendentes.findByNome(atendente)
        atendenteId = atendenteId.id
//        def escalaCount = Historico.executeQuery("select count(dataEscala) from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim'")
//        println "$escalaCount horas"
        def escala = Historico.executeQuery("select distinct hora, dataEscala from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala ASC")

//        def escala = Historico.executeQuery("select new map(distinct hora, dataEscala) from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala ASC")
        def horasTrabalhadas = escala.size()
        println horasTrabalhadas
        println escala
        for(hora in escala) {
            escala[0]
        }



        render(view: "index")
    }
}
