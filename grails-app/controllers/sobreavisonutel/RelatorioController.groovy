package sobreavisonutel

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import groovy.time.TimeCategory
import groovy.time.TimeDuration

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class RelatorioController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def springSecurityService

    def index() {
        //render "some text"
    }

    def gerador() {

        println params.list()  //imprime tudo que foi retornado do formulario da view

        def atendente = params.list("atendente").get(0)  //recebe atendentes e dataInicio da view e tira da list
        def stringDataInicio = params.list("dataInicio").get(0)
        def stringDataFim = params.list("dataFim").get(0)

//        println "dataInicio: " + dataInicio
//        println "dataFim: $dataFim"
//        println "atendente: $atendente"
//        println "Parse: " + Date.parse("dd/MM/yyyy", dataInicio)
//        println "Format: " + Date.parse("dd/MM/yyyy", dataInicio).format("yyyy-MM-dd")
        Date dataInicio = Date.parse("dd/MM/yyyy", stringDataInicio)           //passa a string datainicio pro formato de data, depois coloca na formatacao do banco
        Date dataFim = Date.parse("dd/MM/yyyy", stringDataFim)
        Date dataIni = dataInicio

        def atendenteId = Atendentes.findByNome(atendente)
        atendenteId = atendenteId.id

//        Calendar cal = Calendar.getInstance()
//        cal.setTime(dataInicio)
//        cal.setFirstDayOfWeek (Calendar.SUNDAY);
//        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);                       //pega o numero do dia de semana de 1:domingo a 7:sabado
//        cal.add (Calendar.DAY_OF_MONTH, Calendar.SUNDAY - diaSemana)
////        println "dia da semana: " + Calendar.SUNDAY - diaSemana
//        dataInicio = cal.getTime()                                          //pega a primeira semana da data inicial
//        dataInicio = cal.getTime()                                          //pega a primeira semana da data inicial
        List listBusca = []
        def busca



        while(dataFim >= dataInicio){
            stringDataInicio = dataInicio.format("yyyy-MM-dd").toString()
            busca = Historico.executeQuery("select dataEscala, hora from Historico where dataEscala='$stringDataInicio' and dataModificacao>=(select max(dataModificacao) from Historico where atendentes_id='$atendenteId' and dataEscala='$stringDataInicio' ) order by dataEscala")

            if(busca!=[]) listBusca << busca
//            println "dataInico: " + dataInicio
            dataInicio = dataInicio.plus(1)
        }

        /////////////////////////////////TRATANDO AS HORAS TRABALHADAS   /////////////////////////////////////////
        def listDiasTrabalhados
        def diaTrabalhado
        List listHorasTrabalhadas = [], listDuracaoTrabalhadas = [], listHoraInicio = [], listHoraFim = []
        String stringHoraInicio, stringHoraFim
        Date horaInicio, horaFim
        TimeDuration horasTrab
        TimeDuration horasTrabTotal = new TimeDuration(0,0,0,0)

        listDiasTrabalhados = Ocorrencias.executeQuery("select data, horaInicio, horaFim from Ocorrencias")
//        println "diasTrabalhados: " + listDiasTrabalhados
        listDiasTrabalhados.each {i->
//            diaTrabalhado = i[0]
//            println "diaTrabalhado: " + diaTrabalhado
            stringHoraInicio = i[1]
//            println "stringHoraInicio: " + stringHoraInicio
            stringHoraFim = i[2]
            horaInicio = Date.parse("yyyy-MM-dd HH:mm:ss", stringHoraInicio)
            stringHoraInicio = horaInicio.getTimeString()
            listHoraInicio << stringHoraInicio
            horaFim = Date.parse('yyyy-MM-dd HH:mm:ss', stringHoraFim)
            stringHoraFim = horaFim.getTimeString()
            listHoraFim << stringHoraFim

            horasTrab = TimeCategory.minus(horaFim, horaInicio)
//            println "horasTrabalhadas: " + horasTrab
            horasTrabTotal = horasTrab + horasTrabTotal
            listHorasTrabalhadas << horasTrab
        }
        println "horasTrabTotal: " + horasTrabTotal
        println "listHoraInicio: " + listHoraInicio
        println "listHoraFim: " + listHoraFim

        /////////////////////////////////TRATANDO AS HORAS EM SOBREAVISO/////////////////////////////////////////
        println "listBusca: " + listBusca

        def listData = []
        def listHora = []
        List list_i = []
        Date data
        def horas = 0
        def horasTotal = 0
        def periodoInicio
        def periodoFim
        def listPeriodo = []
        boolean flagData = 0
        def relatorio
        listBusca.each {i->
//            println "i: "+ i
            data = i[0][0]
            list_i = i
            horas = list_i.size()                      //pega todas as horas do dia
            horasTotal = horasTotal + horas
            periodoInicio = list_i.get(0)
            periodoInicio = periodoInicio[1]
            periodoFim = list_i.get(horas-1)
            periodoFim = (periodoFim[1] as Integer) + 1
//            print "periodo: " + periodoInicio + " - "
//            println periodoFim

//            println "listdata: " + listData

            if(!listData.contains(data)) {              //se a lista de datas ainda nao tem a data
                listData << data                        //inclui data na listData
                listHora << horas
                listPeriodo << periodoInicio + " - " + periodoFim + "h"
            }
        }
        listHora << horas
//        println listData
//        println listHora

        List<Relatorio> relatorioList = new ArrayList<Relatorio>()
        listData.eachWithIndex {dia, index->
            data = listData.getAt(index)
//            println data
            relatorio = new Relatorio()
            relatorio.data = data
            relatorio.hora = listHora.getAt(index)
            relatorio.periodo = listPeriodo.getAt(index)
            relatorioList.add(relatorio)
        }

//        println "horasTotal: " + horasTotal



        render(view: "index", model: [atendente:atendente, dataInicio:dataIni, dataFim:dataFim, listaBusca:relatorioList, horasTotal:horasTotal, listDiasTrabalhados: listDiasTrabalhados,
                                      listDuracaoTrabalhadas: listDuracaoTrabalhadas])
//        respond model: [listaBusca:relatorioList, horasTotal:horasTotal]
    }
}
