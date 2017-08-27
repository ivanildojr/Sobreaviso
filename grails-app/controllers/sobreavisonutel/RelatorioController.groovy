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

        println params  //imprime tudo que foi retornado do formulario da view

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

        while(dataFim > dataInicio){
            stringDataInicio = dataInicio.format("yyyy-MM-dd").toString()
            busca = Historico.executeQuery("select dataEscala, hora from Historico where dataEscala='$stringDataInicio' and dataModificacao>=(select max(dataModificacao) from Historico where atendentes_id='$atendenteId' and dataEscala='$stringDataInicio' ) order by dataEscala")

            println "busca: " + busca
            if(busca!=[]) listBusca << busca
//            println "dataInico: " + dataInicio
            dataInicio = dataInicio.plus(1)
        }

        println "listBusca: " + listBusca

        def listData = []
        def listHora = []
        List list_i = []
        Date data
        def horas = 0
        boolean flagData = 0
        def relatorio
        listBusca.each {i->
            println "i: "+ i
            data = i[0][0]
            list_i = i
            horas = list_i.size()
            println "listdata: " + listData

            if(!listData.contains(data)) {              //se a lista de datas ainda nao tem a data
                listData << data                        //inclui data na listData
//                if(flagData) {                          //se passou por aqui pela 1 vez
                    listHora << horas
////                    relatorio.hora = horas
//                }
//                horas = 0
////                println "i_zero: " + data
//                horas++                               //inclui a primeira hora no somatorio de horas
//                flagData = 1;
            }
            else {                                    //se já contem a data na listData, incrementa as horas desta data
                horas++                              //busca quantas vezes a data aparece na lista, então essa é a soma de horas
                println "horas: " + horas
            }
        }
        listHora << horas
        println listData
        println listHora

        List<Relatorio> relatorioList = new ArrayList<Relatorio>()
        listData.eachWithIndex {dia, index->
            data = listData.getAt(index)
            println data
            relatorio = new Relatorio()
            relatorio.data = data
            relatorio.hora = listHora.getAt(index)
            relatorioList.add(relatorio)
        }
//        List relatorioList = Relatorio.getCount()
//        println relatorioList

        render(view: "index", model: [listaBusca:relatorioList])
    }
}
