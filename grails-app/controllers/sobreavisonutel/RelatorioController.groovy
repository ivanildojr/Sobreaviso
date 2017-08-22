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

        println params  //imprime tudo que foi retornado do formulario da view

        def atendente = params.list("atendente")  //recebe atendentes e dataInicio da view
        def dataInicio = params.list("dataInicio")
        def dataFim = params.list("dataFim")

        println "dataInicio: " + dataInicio
        println "dataFim: $dataFim"
        println "atendente: $atendente"
        println "Parse: " + Date.parse("dd/MM/yyyy", dataInicio[0])
        println "Format: " + Date.parse("dd/MM/yyyy", dataInicio[0]).format("yyyy-MM-dd")
        dataInicio = Date.parse("dd/MM/yyyy", dataInicio[0]).format("yyyy-MM-dd") //passa a string datainicio pro formato de data, depois coloca na formatacao do banco
        dataFim = Date.parse("dd/MM/yyyy", dataFim[0]).format("yyyy-MM-dd")

        def atendenteId = Atendentes.findByNome(atendente)
        atendenteId = atendenteId.id
//        String hql = "select distinct new map(hora as Hora, dataEscala as Dia) from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala, hora"
//        def escala = Historico.executeQuery(hql)

//        escala = Historico.executeQuery("select distinct (hora, dataEscala) from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala ASC")


        //def dataModificacao = Historico.executeQuery("select max(data_modificacao) from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala ASC")
        //def listBusca = Historico.executeQuery("select hora, dataEscala from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala ASC")
//        def listDia = Historico.executeQuery("select distinct dataEscala from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' order by dataEscala ASC")

        def listBusca = Historico.executeQuery("select dataEscala, hora from Historico where atendentes_id='$atendenteId' and dataEscala between '$dataInicio' and '$dataFim' and dataModificacao>=(select max(dataModificacao) from Historico where atendentes_id='$atendenteId') order by dataEscala")

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

//        def horasTrabalhadas = listBusca.size()
//        println "$horasTrabalhadas horas"
        listData.eachWithIndex {dia, index->
            println "index: " + index
            relatorio = new Relatorio()
            relatorio.data = listData.getAt(index)
            relatorio.hora = listHora.getAt(index)
        }

        List relatorioList = Relatorio.findAll()
        println relatorioList

        render(view: "index", model: [listHora:listHora, listData:listData, listaBusca:listBusca])
    }
}
