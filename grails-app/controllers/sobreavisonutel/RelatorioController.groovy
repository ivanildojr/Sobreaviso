package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import groovyjarjarantlr.collections.List

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
        def atendente = params.list("atendente")  //recebe atendentes e dataInicio da view
        def dataInicio = params.list("dataInicio")
        def dataFim = params.list("dataFim")

        dataInicio = dataInicio[1] //pega só a data
        dataFim = dataFim[1] //pega só a data
        println "dataInicio: $dataInicio"
        println "dataFim: $dataFim"
        println "atendente: $atendente"
        dataInicio = Date.parse("dd/MM/yyyy", dataInicio).format("yyyy-MM-dd") //passa a string datainicio pro formato de data, depois coloca na formatacao do banco
        dataFim = Date.parse("dd/MM/yyyy", dataFim).format("yyyy-MM-dd")

        def atendenteId = Atendentes.findByNome(atendente)
        atendenteId = atendenteId.id
//        String hql = "select distinct new map(hora as Hora, dataEscala as Dia) from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala, hora"
//        def escala = Historico.executeQuery(hql)

//        escala = Historico.executeQuery("select distinct hora, dataEscala from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala ASC")

        def listHora = []
        def listData = []
        def listBusca = Historico.executeQuery("select distinct hora, dataEscala from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala ASC")
//        def listDia = Historico.executeQuery("select distinct dataEscala from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala ASC")

        listBusca.each {i->
            def listH = i[0]
            listHora << listH
            def listD = i[1]
            listData << listD
        }

        println listHora
        println listData

        def horasTrabalhadas = listHora.size()
        println "$horasTrabalhadas horas"

        render(view: "index", model: [listHora:listHora, listData:listData])
    }
}
