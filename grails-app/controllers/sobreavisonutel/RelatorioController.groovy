package sobreavisonutel

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import groovy.time.Duration
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import sobreavisonutel.seguranca.Usuario

import java.text.DateFormat

import static java.time.LocalDate.now

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class RelatorioController {

    TimeDuration horasTrabDentroSobreaviso, horasTrabForaSobreaviso

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def springSecurityService

    def index() {
        //render "some text"
    }

    def gerador() {

        println params.list()  //imprime tudo que foi retornado do formulario da view

        def atendente = params.list("atendente").get(0)  //recebe atendentes e dataInicio da view e tira da list
        def stringDataInicio = params.list("dataInicio").get(0)
        String mesAno = stringDataInicio.drop(3)
        println "mesAno: " + mesAno
        Date dataInicio = Date.parse("dd/MM/yyyy", stringDataInicio)           //passa a string datainicio pro formato de data, depois coloca na formatacao do banco

        Calendar calend = Calendar.getInstance();
        calend.setTime(dataInicio);
        Integer ultimoDia = calend.getActualMaximum(calend.DAY_OF_MONTH);
        String stringDataFim = ultimoDia.toString() + "/" + mesAno

        println "dataInicio: " + stringDataInicio
        println "stringDataFim:  " + stringDataFim
        println "atendente: $atendente"
//        println "Parse: " + Date.parse("dd/MM/yyyy", dataInicio)
//        println "Format: " + Date.parse("dd/MM/yyyy", dataInicio).format("yyyy-MM-dd")
//        Date dataInicio = Date.parse("dd/MM/yyyy", stringDataInicio)           //passa a string datainicio pro formato de data, depois coloca na formatacao do banco
        Date dataFim = Date.parse("dd/MM/yyyy", stringDataFim)
        println "dataFim: " + dataFim
        Date dataIni = dataInicio

        def atendenteId = Atendentes.findByNome(atendente)
        atendenteId = atendenteId.id
        def atendenteNomeCompleto = sobreavisonutel.seguranca.Usuario.executeQuery("select nome from Usuario where nome LIKE '%$atendente%'").get(0)
//        println "atendenteNomeCompleto: " + atendenteNomeCompleto
        List listBusca = []
        def busca
        def stringDataInicioFixa = dataInicio.format("yyyy-MM-dd").toString()

        while(dataFim >= dataInicio) {
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
//            println "dataInicial: " + dataInicial.format("yyyy-MM-dd")
//            println "dataFinal: " + dataFinal.format("yyyy-MM-dd")

            def dataMaisRecente = Historico.executeQuery("select max(dataModificacao) from Historico where dataEscala between :data1 and :data2",
                    [data1: dataInicial, data2: dataFinal]).get(0)

            def dataMaisRecenteString = ''
            if (dataMaisRecente != null) {
                dataMaisRecenteString = dataMaisRecente.format("yyyy-MM-dd HH:mm:ss")
//                println "dataMaisRecente: " + dataMaisRecenteString
            }
            busca = Historico.executeQuery("select dataEscala, hora from Historico where dataEscala='$stringDataInicio' and atendentes_id='$atendenteId' and dataModificacao>='$dataMaisRecenteString' ) order by dataEscala")
            println "busca: " + busca

            if(busca!=[]) listBusca << busca
//            println "dataInico: " + dataInicio
            dataInicio = dataInicio.plus(1)
        }

        /////////////////////////////////TRATANDO AS HORAS TRABALHADAS   /////////////////////////////////////////
        def listDiasTrabalhados
        String diaTrabalhado, diaSemana
        List listHorasTrabalhadas = [], listHoraInicio = [], listHoraFim = [], listDia = [], listResumido = [], listfloatTempoTrab = []
        String stringHoraInicio, stringHoraFim, resumido
        Date horaInicio, horaFim
        TimeDuration horasTrab
        TimeDuration horasTrabTotal = new TimeDuration(0,0,0,0)
        stringDataFim = dataFim.format("yyyy-MM-dd").toString()

        listDiasTrabalhados = Ocorrencias.executeQuery("select data, horaInicio, horaFim, resumido from Ocorrencias where atendentes='$atendente' and data>='$stringDataInicioFixa' and status='Ativo' and data<='$stringDataFim'")
//        println "diasTrabalhados: " + listDiasTrabalhados
        listDiasTrabalhados.each {i->
            diaTrabalhado = i[0]
            diaTrabalhado = Date.parse("yyyy-MM-dd HH:mm:ss", diaTrabalhado).format("dd-MM-yyyy")
//            println "diaTrabalhado: " + diaTrabalhado
            listDia << diaTrabalhado
            println "listDia: " + listDia



            stringHoraInicio = i[1]
            println "stringHoraInicio: " + stringHoraInicio
            stringHoraFim = i[2]
            horaInicio = Date.parse("yyyy-MM-dd HH:mm:ss", stringHoraInicio)
            stringHoraInicio = horaInicio.format("HH:mm")
            listHoraInicio << stringHoraInicio
            horaFim = Date.parse('yyyy-MM-dd HH:mm:ss', stringHoraFim)
            stringHoraFim = horaFim.format("HH:mm")
            listHoraFim << stringHoraFim
            resumido = i[3]
            listResumido << resumido

            horasTrab = TimeCategory.minus(horaFim, horaInicio)
//            println "horasTrab: " + horasTrab
            def hTrab = horasTrab.getHours()
            def mTrab = horasTrab.getMinutes()
            def floatTempoTrab = hTrab + mTrab/60
            println "floatTempoTrab: " + floatTempoTrab
            String tempoTrab = resultado(floatTempoTrab)
            println "tempoTrab: " + tempoTrab
            println "horasTrab: " + horasTrab
            horasTrabTotal = horasTrabTotal.plus(horasTrab)
            listHorasTrabalhadas << tempoTrab
            listfloatTempoTrab << floatTempoTrab
        }
        println "horasTrabTotal: " + horasTrabTotal
        Integer hTrabTotal = horasTrabTotal.getHours()
        Integer mTrabTotal = horasTrabTotal.getMinutes()
        def floatHTrabTotal = hTrabTotal + mTrabTotal/60  //tempo acionamento total
        String stringHTrabTotal = resultado(floatHTrabTotal)
        println "stringHTrabTotal: " + stringHTrabTotal


//        println "tempoTrabTotal: " + tempoTrabTotal
//        println "listHoraInicio: " + listHoraInicio
//        println "listHoraFim: " + listHoraFim
//        println "diaTrabalhado: " + diaTrabalhado
        listDia.sort()

        List<RelatorioOcorrencia> ocorrenciaList = new ArrayList<RelatorioOcorrencia>()
        def relatorioOcorrencia

        listDia.eachWithIndex {dia, index->
            relatorioOcorrencia = new RelatorioOcorrencia()
            relatorioOcorrencia.data = listDia.getAt(index)
            relatorioOcorrencia.horaInicio = listHoraInicio.getAt(index)
            relatorioOcorrencia.horaFim = listHoraFim.getAt(index)
            relatorioOcorrencia.duracao = listHorasTrabalhadas.getAt(index)
            relatorioOcorrencia.floatDuracao = listfloatTempoTrab.getAt(index)
            relatorioOcorrencia.relato = listResumido.getAt(index)
//            relatorioOcorrencia.acionamentoExtra = listResumido.getAt(index)
            ocorrenciaList.add(relatorioOcorrencia)
        }

        println "relatorioOcorrencia: " + relatorioOcorrencia.data
        println "relatorioOcorrencia.horaFim: " + relatorioOcorrencia.horaFim

        /////////////////////////////////TRATANDO AS HORAS EM SOBREAVISO/////////////////////////////////////////
//        println "listBusca: " + listBusca
        def listData = []
        def listHora = []
        def listSemana = []
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

                Calendar calen = Calendar.getInstance();
                calen.setTime(data);
                int day = calen.get(Calendar.DAY_OF_WEEK);
                String diaDaSemana = semana[day-1]
                listSemana << diaDaSemana
//                println "diaDaSemana: " + diaDaSemana
            }
        }
        listHora << horas
        println "listData: " + listData

        List<Relatorio> relatorioList = new ArrayList<Relatorio>()

        listData.eachWithIndex {dia, index->
            data = listData.getAt(index)
            println "data: " + data
            relatorio = new Relatorio()
            relatorio.data = data
            relatorio.diaSemana = listSemana.getAt(index)
            relatorio.hora = listHora.getAt(index)
            relatorio.periodo = listPeriodo.getAt(index)
            relatorioList.add(relatorio)
        }

        ///////////////////////////verifica se a ocorrência não está em dia da escala /////////////////////////////
        Float horaForaEscala=0
        Float horaDentroEscala=0
        def nDiasdeOcorrencia=0
        def horasTrabDentroSobreaviso = 0
        def horasTrabForaSobreaviso = 0

        listDia.each {dia->                //cada dia de ocorrencia
//            println "tipo dia: " + dia.getClass().getName()
//            println "tipo listDia: " + listDia[0].getClass().getName()

            println "dia: " + dia
            def diaF = Date.parse("dd-MM-yyyy", dia)
            diaF.format("dd-MM-yyyy HH:mm:ss.S")
//            println "tipo diaF: " + diaF.getClass().getName()

//            ////////////////////////////////////// CALCULO DAS HORAS EFETIVAS E SOBREAVISO
//
            def diaFF = diaF.format("yyyy-MM-dd HH:mm:ss")
            println "diaFF: " + diaFF
            def dataMaisRecente = Historico.executeQuery("select max(dataModificacao) from Historico where dataEscala='$diaFF'").get(0)

            def dataMaisRecenteString = ''
            if (dataMaisRecente != null) {
                dataMaisRecenteString = dataMaisRecente.format("yyyy-MM-dd HH:mm:ss")
//                println "dataMaisRecente: " + dataMaisRecenteString
            }
            def buscaDia = Historico.executeQuery("select dataEscala, hora from Historico where dataEscala='$diaFF' and atendentes_id='$atendenteId' and dataModificacao>='$dataMaisRecenteString' ) order by dataEscala")
            println "buscaDia: " + buscaDia

            Date dataFimEscala, dataInicioEscala
            def diaOcorrenciaInicio, horaOcorrenciaInicio, horaOcorrenciaFim
            diaOcorrenciaInicio =  ocorrenciaList.getAt(nDiasdeOcorrencia).data
            horaOcorrenciaInicio = ocorrenciaList.getAt(nDiasdeOcorrencia).horaInicio
            horaOcorrenciaFim = ocorrenciaList.getAt(nDiasdeOcorrencia).horaFim

            if(!buscaDia.empty) {   //se tiver escala no dia   else
                def horaEscalaInicio = buscaDia[0].getAt(1)
                println "horaEscalaInicio: " + horaEscalaInicio
                def horaEscalaFim = buscaDia[buscaDia.lastIndexOf()].getAt(1)
                println "horaEscalaFim: " + horaEscalaFim

                Calendar calInicioEscala = Calendar.getInstance();
                calInicioEscala.setTime(diaF)
                calInicioEscala.set(Calendar.HOUR, horaEscalaInicio as Integer)
                dataInicioEscala = calInicioEscala.getTime()
                println "dataInicioEscala: " + dataInicioEscala.getTime()

                Calendar calFimEscala = Calendar.getInstance();
                calFimEscala.setTime(diaF)
                calFimEscala.set(Calendar.HOUR, (horaEscalaFim as Integer) + 1)  //+1 para considerar escala ate as 00h
                if(calFimEscala<calInicioEscala) {  //se passar da meia noite considere como o dia seguinte
                    calFimEscala.add(calFimEscala.DATE,1)
                }
                dataFimEscala = calFimEscala.getTime()
                println "dataFimEscala: " + dataFimEscala
                println "ocorrenciaList.horaInicio: " + ocorrenciaList.horaInicio
                println "dataFimEscala: " + dataFimEscala
                List<TimeDuration> rudsom = calculaDiaComEscala(diaOcorrenciaInicio,horaOcorrenciaInicio,horaOcorrenciaFim,dataFimEscala)
//                println "horasTrab[0]: " + rudsom.get(0)

                horasTrabDentroSobreaviso += rudsom.get(0).plus(horasTrabDentroSobreaviso)
                horasTrabForaSobreaviso += rudsom.get(1).plus(horasTrabForaSobreaviso)
            }
            else {  //se não tem escala no dia
                horasTrabForaSobreaviso = calculaDiaSemEscala(diaOcorrenciaInicio,horaOcorrenciaInicio,horaOcorrenciaFim)
                horasTrabForaSobreaviso += horasTrabForaSobreaviso
                println "horasTrabForaSobreaviso: " + horasTrabForaSobreaviso
            }
            nDiasdeOcorrencia++

            /////////////////////////////

            println "listData: " + listData
//            diaF = diaF.format("yyyy-MM-dd HH:mm:ss.S")
            println "diaF: " + diaF
            if(!listData.contains(diaF)) {
                println "A data não existe na escala: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + diaF
                ocorrenciaList.findAll().each { oc->                       //busca todas as ocorrencias
                    def dateOcData = Date.parse("dd-MM-yyyy", oc.data)
//                    println "listData: " + listData
                    println "dateOcData: " + dateOcData
                    println "tipo dOcData: " + dateOcData.getClass().getName()
                    println "diaF: " + diaF
                    println "tipo diaF: " + diaF.getClass().getName()
                    if(dateOcData==diaF) {
                        println "Datas Iguais: " + oc.data + " ** " + diaF
                        println "oc.floatDuracao: " + oc.floatDuracao
                        horaForaEscala += oc.floatDuracao
                        println "horaForaEscala: " + horaForaEscala
                    }
                }
            }
        }
        String stringHoraForaEscala = resultado(horaForaEscala)

        def acionamentoNaEscala = floatHTrabTotal - horaForaEscala
        String stringacionamentoNaEscala = resultado(acionamentoNaEscala)
        println "stringacionamentoNaEscala: " + stringacionamentoNaEscala

                ////////////////////// Lançamento no ponto
        println "horasTrabTotal: " + horasTrabTotal
        def floatHorasTrabTotal = hTrabTotal + mTrabTotal/60
        println "floatHorasTrabTotal: " + floatHorasTrabTotal
        def floatHorasTrabEscala = floatHorasTrabTotal - horaForaEscala
        def horasSobreavisoMenosAcionamento = horasTotal - floatHorasTrabEscala
        println "horasSobreavisoMenosAcionamento: " + horasSobreavisoMenosAcionamento
        def horasPonto = horasSobreavisoMenosAcionamento/3 + horaForaEscala   //Fator de sobreaviso mais as horas efetivemente trabalhadas
        String tempoPonto = resultado(horasPonto)
        String tempoSobreavisoMenosAcionamento = resultado(horasSobreavisoMenosAcionamento)

        ////////////////////// Total em acionamento
        String stringSobreAvisoDiv3
//        def floatTempoTrab = hTrab + mTrab/60
//        println "floatTempoTrab: " + floatTempoTrab
//        String tempoTrab = resultado(floatTempoTrab)
//        mTrabTotal = mTrabTotal % 60
//        hTrabTotal = mTrabTotal/60 + hTrabTotal
//        def sobreAvisoDiv3 = horasTotal/3
        stringSobreAvisoDiv3 = resultado(horasSobreavisoMenosAcionamento/3)

        println "atendenteNomeCompleto: " + atendenteNomeCompleto

//        println "usuarios: " + Usuario.list()

        render(view: "index", model: [atendente:atendente, dataInicio:dataIni, mesAno:mesAno, listaBusca:relatorioList, horasTotal:horasTotal,
        ocorrenciaList: ocorrenciaList, stringacionamentoNaEscala:stringacionamentoNaEscala, stringSobreAvisoDiv3: stringSobreAvisoDiv3,
        tempoPonto: tempoPonto, tempoSobreavisoMenosAcionamento: tempoSobreavisoMenosAcionamento, stringHoraForaEscala: stringHoraForaEscala,
        atendenteNomeCompleto: atendenteNomeCompleto])
//        respond model: [listaBusca:relatorioList, horasTotal:horasTotal]
    }

    //////////MÉTODO PARA CONVERTER FLOAT EM HORAS E MINUTOS
    static def resultado(def numero) {
//        numero = Math.round(numero)
//        println "numero: " + numero
        String resultado
        Integer hNumero = numero
        Integer mNumero = Math.round((numero - hNumero)*60) //transformar decimal para minutos
        if(mNumero==60) {
            hNumero += 1
            mNumero = 0
        }
//        println "hNumero: " + hNumero
//        println "mNumero: " + mNumero
        if(hNumero==0) resultado = mNumero + " minutos"
        if(hNumero==1) resultado = mNumero + " minuto"
        if(mNumero==0) resultado = hNumero + " horas"
        if(mNumero==1) resultado = hNumero + " hora"
        if(hNumero>0 & mNumero>0) resultado = hNumero + " horas, " + mNumero + " minutos"
        if(hNumero>0 & mNumero==1) resultado = hNumero + " horas, " + mNumero + " minuto"
        if(hNumero==1 & mNumero>0) resultado = hNumero + " hora, " + mNumero + " minutos"
        if(hNumero==1 & mNumero==0) resultado = hNumero + " hora"
        if(hNumero==0 & mNumero==1) resultado = mNumero + " minuto"
        if(hNumero==0 & mNumero==0) resultado = "---"
//        println "resultado: " + resultado
        return resultado
    }

    //////////MÉTODO PARA HORAS TRABALHADAS QUE HÁ SOBREAVISO
    static def calculaDiaComEscala (def data, def horaInicio, def horaFim, def dataFimEscala) {
        def dataF = data + " " + horaInicio
        Date dataOcorrenciaInicio = Date.parse("dd-MM-yyyy HH:mm", dataF)
        println "dataOcorrenciaInicio: " + dataOcorrenciaInicio
        dataF = data + " " + horaFim
        Date dataOcorrenciaFim = Date.parse("dd-MM-yyyy HH:mm", dataF)
        println "dataOcorrenciaFim: " + dataOcorrenciaFim
        if(dataOcorrenciaFim<dataOcorrenciaInicio) {  //se passar da meia noite considere como o dia seguinte
            Calendar calOcorrenciaFim = Calendar.getInstance();
            calOcorrenciaFim.setTime(dataOcorrenciaFim)
            calOcorrenciaFim.add(calOcorrenciaFim.DATE,1)
            dataOcorrenciaFim = calOcorrenciaFim.getTime()
        }

        TimeDuration horasTrabDentroSobreaviso, horasTrabForaSobreaviso
        horasTrabDentroSobreaviso = TimeCategory.minus(dataFimEscala, dataOcorrenciaInicio)
        println "horasTrabDentroSobreaviso: " + horasTrabDentroSobreaviso
        horasTrabForaSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataOcorrenciaInicio) - horasTrabDentroSobreaviso
        println "horasTrabForaSobreaviso: " + horasTrabForaSobreaviso
        List<TimeDuration> horasTrab = new ArrayList<TimeDuration>()
        horasTrab.add(horasTrabDentroSobreaviso)
        horasTrab.add(horasTrabForaSobreaviso)
        return horasTrab


//        Calendar calOcorrenciaInicio = Calendar.getInstance();
//        calOcorrenciaInicio.setTime(dataOcorrenciaInicio)
//        calOcorrenciaInicio.set(Calendar.HOUR, horaInicio as Integer)
//        println "calOcorrenciaInicio: " + calOcorrenciaInicio.getTime()
    }

    //////////MÉTODO PARA HORAS TRABALHADAS QUE NÃO HÁ SOBREAVISO
    static def calculaDiaSemEscala (def data, def horaInicio, def horaFim) {
        def dataF = data + " " + horaInicio
        Date dataOcorrenciaInicio = Date.parse("dd-MM-yyyy HH:mm", dataF)
        println "dataOcorrenciaInicio: " + dataOcorrenciaInicio
        dataF = data + " " + horaFim
        Date dataOcorrenciaFim = Date.parse("dd-MM-yyyy HH:mm", dataF)
        println "dataOcorrenciaFim: " + dataOcorrenciaFim
        if (dataOcorrenciaFim < dataOcorrenciaInicio) {  //se passar da meia noite considere como o dia seguinte
            Calendar calOcorrenciaFim = Calendar.getInstance();
            calOcorrenciaFim.setTime(dataOcorrenciaFim)
            calOcorrenciaFim.add(calOcorrenciaFim.DATE, 1)
            dataOcorrenciaFim = calOcorrenciaFim.getTime()
        }
        TimeDuration horasTrabForaSobreaviso
        horasTrabForaSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataOcorrenciaInicio)
        return horasTrabForaSobreaviso
//        println "horasTrabForaSobreaviso: " + horasTrabForaSobreaviso
    }

}