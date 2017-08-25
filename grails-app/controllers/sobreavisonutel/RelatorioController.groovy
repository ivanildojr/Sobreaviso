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
        def dataInicio = params.list("dataInicio").get(0)
        def dataFim = params.list("dataFim").get(0)

//        println "dataInicio: " + dataInicio
//        println "dataFim: $dataFim"
//        println "atendente: $atendente"
//        println "Parse: " + Date.parse("dd/MM/yyyy", dataInicio)
//        println "Format: " + Date.parse("dd/MM/yyyy", dataInicio).format("yyyy-MM-dd")
        dataInicio = Date.parse("dd/MM/yyyy", dataInicio).format("yyyy-MM-dd")              //passa a string datainicio pro formato de data, depois coloca na formatacao do banco
        dataFim = Date.parse("dd/MM/yyyy", dataFim).format("yyyy-MM-dd")

        def atendenteId = Atendentes.findByNome(atendente)
        atendenteId = atendenteId.id

        Calendar cal = Calendar.getInstance()
        println cal.setTime(dataInicio)
        cal.setFirstDayOfWeek (Calendar.SUNDAY);
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);                       //pega o numero do dia de semana de 1:domingo a 7:sabado
        cal.add (Calendar.DAY_OF_MONTH, Calendar.SUNDAY - diaSemana)
//        println "dia da semana: " + Calendar.DAY_OF_WEEK
        dataInicio = cal.getTime()                                          //pega a primeira semana da data inicial
        def listBusca
        while(dataFim >= dataInicio){
            cal.add (Calendar.DAY_OF_MONTH, 6)
            dataFim = cal.getTime()
            listBusca << Historico.executeQuery("select dataEscala, hora from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' and dataModificacao>=(select max(dataModificacao) from Historico where atendentes_id='$atendenteId') order by dataEscala")

            dataInicio = dataFim
        }

        println listBusca

        def listData = []
        def listHora = []
        def data
        def horas = 0
        boolean flagData = 0
        def relatorio
        listBusca.each {i->
//            println i
            data = i[0]
            if(!listData.contains(data)) {              //se a lista de datas ainda nao tem a data
                listData << data                        //inclui data na listData
                if(flagData) {                          //se passou por aqui pela 1 vez
                    listHora << horas
//                    relatorio.hora = horas
                }
                horas = 0
//                println "i_zero: " + data
                horas++                               //inclui a primeira hora no somatorio de horas
                flagData = 1;
            }
            else {                                    //se já contem a data na listData, incrementa as horas desta data
                horas++                              //busca quantas vezes a data aparece na lista, então essa é a soma de horas
//                println "horas: " + horas
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
