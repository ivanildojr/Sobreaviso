package sobreavisonutel

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import groovy.time.Duration
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import sobreavisonutel.seguranca.Usuario

import static java.time.LocalDate.now

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class BoletimController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def springSecurityService

    def index() {
        //render "some text"
    }

    def boletim() {

        println "Parâmetros da view: " + params.list()  //imprime tudo que foi retornado do formulario da view
        def stringDataInicio = params.list("dataInicio").get(0)
        String mes = stringDataInicio.substring(3,5)
        String ano = stringDataInicio.substring(6,10)
        println "mes/ano: " + mes + "/" + ano
        Date dataInicio = Date.parse("dd/MM/yyyy", stringDataInicio)
        //passa a string datainicio pro formato de data, depois coloca na formatacao do banco

        Calendar calend = Calendar.getInstance();
        calend.setTime(dataInicio);
        Integer ultimoDia = calend.getActualMaximum(calend.DAY_OF_MONTH);
        String stringDataFim = ultimoDia.toString() + "/" + mes + "/" + ano

        println "dataInicio: " + stringDataInicio
        println "stringDataFim:  " + stringDataFim
        Date dataFim = Date.parse("dd/MM/yyyy", stringDataFim)
        println "dataFim: " + dataFim
        Date dataIni = dataInicio

        List listAtendentesId = Atendentes.findAll()
        println "listAtendentesId: " + listAtendentesId

        def atendente = "Rudsom"
        def atendenteId = Atendentes.findByNome(atendente)
        atendenteId = atendenteId.id
        println "atendenteId: " + atendenteId
//        def atendenteNomeCompleto = sobreavisonutel.seguranca.Usuario.executeQuery("select nome from Usuario where nome LIKE '%$atendente%'").get(0)
//        println "atendenteNomeCompleto: " + atendenteNomeCompleto
        List listBusca = []
        def busca
        def stringDataInicioFixa = dataInicio.format("yyyy-MM-dd").toString()

        while (dataFim >= dataInicio) {
            stringDataInicio = dataInicio.format("yyyy-MM-dd").toString()
//            println "stringDataInicio: " + stringDataInicio

            def dataInicial = Date.parse('yyyy-MM-dd', stringDataInicio)

            Calendar cal = Calendar.getInstance();
            cal.setTime(dataInicial)
            cal.setFirstDayOfWeek(Calendar.SUNDAY);
            int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - diaSemana)
            dataInicial = cal.getTime()

            cal.add(Calendar.DAY_OF_MONTH, 6)

            def dataFinal = cal.getTime()
            def dataMaisRecente = Historico.executeQuery("select max(dataModificacao) from Historico where dataEscala between :data1 and :data2",
                    [data1: dataInicial, data2: dataFinal]).get(0)
            def dataMaisRecenteString = ''
            if (dataMaisRecente != null) {
                dataMaisRecenteString = dataMaisRecente.format("yyyy-MM-dd HH:mm:ss")
//                println "dataMaisRecente: " + dataMaisRecenteString
            }
            busca = Historico.executeQuery("select dataEscala, hora, atendentes.nome from Historico where dataEscala='$stringDataInicio' and dataModificacao>='$dataMaisRecenteString' ) order by dataEscala")
            if(busca == []) {
                Date dataNaoExistente = dataInicio
//                println "dataNaoExistente: " + dataNaoExistente
                busca = [[dataNaoExistente, "", ""]]
            }
//            println "busca: " + busca

            listBusca << busca
//            println "dataInico: " + dataInicio
            dataInicio = dataInicio.plus(1)
        }

        /////////////////////////////////TRATANDO AS HORAS EM SOBREAVISO/////////////////////////////////////////
//        println "listBusca: " + listBusca
        def listData = []
        def listHora = []
        def listSemana = []
        def listAtendentes = []
        List semana = ["Domingo","Segunda","Terça","Quarta","Quinta","Sexta","Sábado"]
        List list_i = []
        Date data
        def horas = 0
        def horasTotal = 0
        def periodoInicio
        def periodoFim
        def listPeriodo = []
        boolean flagData = 0
        def relatorio
        listBusca.each { i ->
//            println "i: " + i
            data = i[0][0]
            if (i[0][1] == "") {
                print "i: " + i
                println " Vazio"
                listData << data
                Calendar calen = Calendar.getInstance();
                calen.setTime(data);
                int day = calen.get(Calendar.DAY_OF_WEEK);
                String diaDaSemana = semana[day - 1]
                listSemana << diaDaSemana
                listPeriodo << "---"
                listAtendentes << "---"
            }

            else {
                data = i[0][0]
                print "i: " + i
                println " Cheio"
                list_i = i
                horas = list_i.size()                      //pega todas as horas do dia
                horasTotal = horasTotal + horas
                periodoInicio = list_i.get(0)
                periodoInicio = periodoInicio[1]
                atendente = i[0][2]
                periodoFim = list_i.get(horas - 1)
                periodoFim = (periodoFim[1] as Integer) + 1
            }

                if (!listData.contains(data)) {              //se a lista de datas ainda nao tem a data
                    println "Contem data"
                    listData << data                        //inclui data na listData
                    listHora << horas
                    listAtendentes << atendente
                    if(periodoInicio.toInteger() < 12) {
                        listPeriodo << "0" + periodoInicio + " - " + periodoFim + "h"
                    }
                    else {
                        listPeriodo << periodoInicio + " - " + periodoFim + "h"
                    }

                    Calendar calen = Calendar.getInstance();
                    calen.setTime(data);
                    int day = calen.get(Calendar.DAY_OF_WEEK);
                    String diaDaSemana = semana[day - 1]
                    listSemana << diaDaSemana
                    //                println "diaDaSemana: " + diaDaSemana
                }

        }
        listHora << horas
//        println "listData: " + listData

        List<Relatorio> relatorioList = new ArrayList<Relatorio>()

        listData.eachWithIndex {dia, index->
            data = listData.getAt(index)
//            println "data: " + data
            relatorio = new Relatorio()
            relatorio.data = data
            relatorio.diaSemana = listSemana.getAt(index)
            relatorio.hora = listHora.getAt(index)
            relatorio.periodo = listPeriodo.getAt(index)
            relatorio.atendente = listAtendentes.getAt(index)
            relatorioList.add(relatorio)
        }

        List meses = ["JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO", "JULHO", "AGOSTO", "SETEMBRO", "OUTUBRO", "NOVEMBRO", "DEZEMBRO"]

        String mesAno = meses.get(mes.toInteger() - 1) + "/" + ano

        render(view: "index", model: [atendente: atendente, dataInicio: dataIni, mesAno: mesAno, listaBusca: relatorioList])

    }
}