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
        String hql = "select distinct new map(hora as Hora, dataEscala as Dia) from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala, hora"
        def escala = Historico.executeQuery(hql)

//        def escala = Historico.executeQuery("select distinct hora, dataEscala from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala ASC")

        def horasTrabalhadas = escala.size()
        println "$horasTrabalhadas horas"
        //println escala
        def diaAnterior
        def mapEscala = [:]
        def mapEscalaString
        def escalaFormatada = []
        def countHora = 0
        boolean flagDataInicial = 1
        for(day in escala) {
            def hora = day.Hora
            def dia = day.Dia
            if(flagDataInicial) {  //so executa uma vez a data inicial
                diaAnterior = dia
                flagDataInicial = 0
            }
            //println "$hora - $dia"
            if(diaAnterior==dia){
                countHora+=1
            }
            else {
                println diaAnterior
                mapEscala['dia'] = diaAnterior
                mapEscala['hora'] = countHora
                mapEscalaString = mapEscala.values()
                mapEscalaString = mapEscalaString.toString()
                println mapEscalaString
                flagDataInicial = 1 //habilita a contagem de horas de uma nova data
                escalaFormatada.push(mapEscalaString)
//                println escalaFormatada
                countHora=0 //zera contagem de horas pra novas contagens
            }
        }
        println escalaFormatada
        render(view: "index", model: [escalaFormatada:escalaFormatada])
//        render(view: "index", model:[dia:dia, hora:hora])
    }
}
