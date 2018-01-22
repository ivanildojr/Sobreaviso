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

//        println "relatorioOcorrencia: " + relatorioOcorrencia.data
//        println "relatorioOcorrencia.horaFim: " + relatorioOcorrencia.horaFim

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
        def nDiasdeOcorrencia=0
        def floatTempoTrabForaSobreaviso=0, floatTempoTrabDentroSobreaviso=0

        listDia.each {dia->                //cada dia de ocorrencia
//            println "tipo dia: " + dia.getClass().getName()
//            println "tipo listDia: " + listDia[0].getClass().getName()

            println "dia: " + dia
            def diaF = Date.parse("dd-MM-yyyy", dia)
//            diaF.format("dd-MM-yyyy HH:mm:ss.S")
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

            if(!buscaDia.empty) {   //se tiver escala no dia
                def horaEscalaInicio = buscaDia[0].getAt(1)
                println "horaEscalaInicio: " + horaEscalaInicio
                def horaEscalaFim = buscaDia[buscaDia.lastIndexOf()].getAt(1)
                println "horaEscalaFim: " + horaEscalaFim

                Calendar calInicioEscala = Calendar.getInstance();
                calInicioEscala.setTime(diaF)
                calInicioEscala.set(Calendar.HOUR, horaEscalaInicio as Integer)
                dataInicioEscala = calInicioEscala.getTime()
                println "dataInicioEscala: " + dataInicioEscala

                Calendar calFimEscala = Calendar.getInstance();
                calFimEscala.setTime(diaF)
                calFimEscala.set(Calendar.HOUR, (horaEscalaFim as Integer) + 1)  //+1 para considerar escala ate as 00h
                if(calFimEscala<calInicioEscala) {  //se passar da meia noite considere como o dia seguinte
                    calFimEscala.add(calFimEscala.DATE,1)
                }
                dataFimEscala = calFimEscala.getTime()
                println "dataFimEscala: " + dataFimEscala
                println "ocorrenciaList.horaInicio: " + ocorrenciaList.horaInicio
                List<TimeDuration> rudsom = calculaDiaComEscala(atendenteId, diaOcorrenciaInicio,horaOcorrenciaInicio,horaOcorrenciaFim,dataInicioEscala,dataFimEscala)
                println ""
                println "COM ESCALA ******************"
                floatTempoTrabDentroSobreaviso += rudsom.get(0)
                floatTempoTrabForaSobreaviso += rudsom.get(1)
            }
            else {  //se não tem escala no dia
                println ""
                println "SEM ESCALA ******************"
                def dataF = diaOcorrenciaInicio + " " + horaOcorrenciaInicio
                Date dataOcorrenciaInicio = Date.parse("dd-MM-yyyy HH:mm", dataF)
                println "dataOcorrenciaInicio: " + dataOcorrenciaInicio
                dataF = diaOcorrenciaInicio + " " + horaOcorrenciaFim
                Date dataOcorrenciaFim = Date.parse("dd-MM-yyyy HH:mm", dataF)
                println "dataOcorrenciaFim: " + dataOcorrenciaFim
                List<TimeDuration> rudsom = calculaDiaComEscala(atendenteId, diaOcorrenciaInicio,horaOcorrenciaInicio,horaOcorrenciaFim,dataInicioEscala,dataFimEscala)
                floatTempoTrabDentroSobreaviso += rudsom.get(0)
                floatTempoTrabForaSobreaviso += rudsom.get(1)
//                if (dataOcorrenciaFim < dataOcorrenciaInicio) {  //se passar da meia noite considere como o dia seguinte
//                    Calendar calOcorrenciaFim = Calendar.getInstance();
//                    calOcorrenciaFim.setTime(dataOcorrenciaFim)
//                    calOcorrenciaFim.add(calOcorrenciaFim.DATE, 1)
//                    dataOcorrenciaFim = calOcorrenciaFim.getTime()
//                    def dataOcorrenciaFimSeguinte = dataOcorrenciaFim.format("yyyy-MM-dd")
//                    println "dataOcorrenciaFimSeguinte: " + dataOcorrenciaFimSeguinte
//
//                    def dataMaisRecenteDiaSeguinte = Historico.executeQuery("select max(dataModificacao) from Historico where dataEscala='$dataOcorrenciaFimSeguinte'").get(0)
//                    println "dataMaisRecenteDiaSeguinte: " + dataMaisRecenteDiaSeguinte
//
//                    def dataMaisRecenteStringDiaSeguinte = ''
//                    if (dataMaisRecenteDiaSeguinte != null) {
//                        dataMaisRecenteStringDiaSeguinte = dataMaisRecenteDiaSeguinte.format("yyyy-MM-dd HH:mm:ss")
//                        println "dataMaisRecenteStringDiaSeguinte: " + dataMaisRecenteStringDiaSeguinte
//                    }
//                    def buscaDiaSeguinte = Historico.executeQuery("select dataEscala, hora from Historico where dataEscala='$dataOcorrenciaFimSeguinte' and atendentes_id='$atendenteId' and dataModificacao>='$dataMaisRecenteStringDiaSeguinte' ) order by dataEscala")
//                    println "buscaDiaSeguinte: " + buscaDiaSeguinte
//                }
//
////                def floatTempoFora = calculaDiaSemEscala(dataOcorrenciaInicio,dataOcorrenciaFim)
////                println "floatTempoTrabForaSobreaviso: " + floatTempoFora
////                floatTempoTrabForaSobreaviso += floatTempoFora
////                println "******************"
            }
            nDiasdeOcorrencia++
            println "floatFora: " + floatTempoTrabForaSobreaviso
            println "floatDentro: " + floatTempoTrabDentroSobreaviso
            /////////////////////////////
//
        }
        String stringHoraForaEscala = resultado(floatTempoTrabForaSobreaviso)

        def acionamentoNaEscala = floatHTrabTotal - floatTempoTrabForaSobreaviso
        String stringacionamentoNaEscala = resultado(acionamentoNaEscala)
        println "stringacionamentoNaEscala: " + stringacionamentoNaEscala

        ////////////////////// Lançamento no ponto
        println "horasTrabTotal: " + horasTrabTotal
        def floatHorasTrabTotal = hTrabTotal + mTrabTotal/60
        println "floatHorasTrabTotal: " + floatHorasTrabTotal
        def floatHorasTrabEscala = floatHorasTrabTotal - floatTempoTrabForaSobreaviso
        float horasSobreavisoMenosAcionamento = horasTotal - floatHorasTrabEscala
        println "horasSobreavisoMenosAcionamento: " + horasSobreavisoMenosAcionamento
        def horasPonto = horasSobreavisoMenosAcionamento/3 + floatTempoTrabForaSobreaviso   //Fator de sobreaviso mais as horas efetivemente trabalhadas
        String tempoPonto = resultado(horasPonto)
        String tempoSobreavisoMenosAcionamento = resultado(horasSobreavisoMenosAcionamento)

        ////////////////////// Total em acionamento
        String stringSobreAvisoDiv3
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
    static def calculaDiaComEscala (def atendenteId, def data, def horaInicio, def horaFim, def dataInicioEscala, def dataFimEscala) {
        println "METODO COM SOBREAVISO ******************"
        def dataF = data + " " + horaInicio
        println "dataFimEscala: " + dataFimEscala
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
            def dataOcorrenciaFimSeguinte = dataOcorrenciaFim.format("yyyy-MM-dd")
            println "dataOcorrenciaFimSeguinte: " + dataOcorrenciaFimSeguinte

            def dataMaisRecenteDiaSeguinte = Historico.executeQuery("select max(dataModificacao) from Historico where dataEscala='$dataOcorrenciaFimSeguinte'").get(0)
            println "dataMaisRecenteDiaSeguinte: " + dataMaisRecenteDiaSeguinte

            def dataMaisRecenteStringDiaSeguinte = ''
            if (dataMaisRecenteDiaSeguinte != null) {
                dataMaisRecenteStringDiaSeguinte = dataMaisRecenteDiaSeguinte.format("yyyy-MM-dd HH:mm:ss")
                println "dataMaisRecenteStringDiaSeguinte: " + dataMaisRecenteStringDiaSeguinte

            def buscaDiaSeguinte = Historico.executeQuery("select dataEscala, hora from Historico where dataEscala='$dataOcorrenciaFimSeguinte' and atendentes_id='$atendenteId' and dataModificacao>='$dataMaisRecenteStringDiaSeguinte' ) order by dataEscala")
            println "buscaDiaSeguinte: " + buscaDiaSeguinte

                def horaEscalaInicio = buscaDiaSeguinte[0].getAt(1)
                println "horaEscalaInicio: " + horaEscalaInicio
                def horaEscalaFim = buscaDiaSeguinte[buscaDiaSeguinte.lastIndexOf()].getAt(1)
                println "horaEscalaFim: " + horaEscalaFim

                def diaF = buscaDiaSeguinte[0].getAt(0)
//                def diaF = Date.parse("dd-MM-yyyy", dia)

                Calendar calInicioEscala = Calendar.getInstance();
                calInicioEscala.setTime(diaF)
                calInicioEscala.set(Calendar.HOUR, horaEscalaInicio as Integer)
                dataInicioEscala = calInicioEscala.getTime()
                println "dataInicioEscala: " + dataInicioEscala

                Calendar calFimEscala = Calendar.getInstance();
                calFimEscala.setTime(diaF)
                calFimEscala.set(Calendar.HOUR, (horaEscalaFim as Integer) + 1)  //+1 para considerar escala ate as 00h
                if(calFimEscala<calInicioEscala) {  //se passar da meia noite considere como o dia seguinte
                    calFimEscala.add(calFimEscala.DATE,1)
                }
                dataFimEscala = calFimEscala.getTime()
                println "dataFimEscala: " + dataFimEscala
            }
        }

        TimeDuration horasTrabDentroSobreaviso, horasTrabForaSobreaviso
        if(dataFimEscala>dataOcorrenciaFim & dataOcorrenciaInicio>dataInicioEscala) {                 //    |--------------------------|    Escala
            horasTrabDentroSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataOcorrenciaInicio)   //              |-------|             Ocorrencia
            horasTrabForaSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataOcorrenciaFim) //forçar cast
            println "1 ******************"
        }
        if(dataOcorrenciaInicio>dataFimEscala & dataOcorrenciaInicio>dataInicioEscala) {              //    |--------------------------|                 Escala
            horasTrabForaSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataOcorrenciaInicio)     //                                   |-------|     Ocorrencia
            horasTrabDentroSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataOcorrenciaFim) //forçar cast
            println "2 ******************"
        }
        if(dataInicioEscala>dataOcorrenciaInicio & dataInicioEscala>dataOcorrenciaFim) {              //                |--------------------------|     Escala
            horasTrabForaSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataOcorrenciaInicio)     //    |-------|                                    Ocorrencia
            horasTrabDentroSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataOcorrenciaFim) //forçar cast
            println "3 ******************"
            TimeCategory.equals(0)
        }
        if(dataOcorrenciaFim>dataFimEscala & dataFimEscala>dataOcorrenciaInicio) {                    //    |--------------------------|         Escala
            horasTrabForaSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataFimEscala)            //                           |-------|     Ocorrencia
            horasTrabDentroSobreaviso = TimeCategory.minus(dataFimEscala, dataOcorrenciaInicio)
            println "4 ******************"
        }
        if(dataOcorrenciaFim>dataInicioEscala & dataInicioEscala>dataOcorrenciaInicio) {              //                |--------------------------|     Escala
            horasTrabForaSobreaviso = TimeCategory.minus(dataInicioEscala,dataOcorrenciaInicio)       //            |-------|                            Ocorrencia
            horasTrabDentroSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataInicioEscala)
            println "5 ******************"
        }
        println "horasTrabDentroSobreaviso: " + horasTrabDentroSobreaviso
        println "horasTrabForaSobreaviso: " + horasTrabForaSobreaviso
        def hTrab = horasTrabDentroSobreaviso.getHours()
        if(!hTrab==null) hTrab=0
        def mTrab = horasTrabDentroSobreaviso.getMinutes()
        if(!mTrab==null) mTrab=0
        def floatTempoTrabDentroSobreaviso = hTrab + mTrab/60
        println "floatTempoTrabDentroSobreaviso: " + floatTempoTrabDentroSobreaviso
        hTrab = horasTrabForaSobreaviso.getHours()
        if(!hTrab==null) hTrab=0
        mTrab = horasTrabForaSobreaviso.getMinutes()
        if(!mTrab==null) mTrab=0
        def floatTempoTrabForaSobreaviso = hTrab + mTrab/60
        println "floatTempoTrabForaSobreaviso: " + floatTempoTrabForaSobreaviso

        List<TimeDuration> horasTrab = new ArrayList<TimeDuration>()
        horasTrab.add(floatTempoTrabDentroSobreaviso)
        horasTrab.add(floatTempoTrabForaSobreaviso)
        return horasTrab
    }

//    //////////MÉTODO PARA HORAS TRABALHADAS QUE NÃO HÁ SOBREAVISO
//    static def calculaDiaSemEscala (def dataOcorrenciaInicio, def dataOcorrenciaFim) {
//        println "METODO SEM SOBREAVISO ******************"
////        def dataF = data + " " + horaInicio
////        Date dataOcorrenciaInicio = Date.parse("dd-MM-yyyy HH:mm", dataF)
////        println "dataOcorrenciaInicio: " + dataOcorrenciaInicio
////        dataF = data + " " + horaFim
////        Date dataOcorrenciaFim = Date.parse("dd-MM-yyyy HH:mm", dataF)
////        println "dataOcorrenciaFim: " + dataOcorrenciaFim
////        if (dataOcorrenciaFim < dataOcorrenciaInicio) {  //se passar da meia noite considere como o dia seguinte
////            Calendar calOcorrenciaFim = Calendar.getInstance();
////            calOcorrenciaFim.setTime(dataOcorrenciaFim)
////            calOcorrenciaFim.add(calOcorrenciaFim.DATE, 1)
////            dataOcorrenciaFim = calOcorrenciaFim.getTime()
////
////        }
//        if(dataOcorrenciaFim>dataInicioEscala & dataInicioEscala>dataOcorrenciaInicio) {              //                |--------------------------|     Escala
//            horasTrabForaSobreaviso = TimeCategory.minus(dataInicioEscala,dataOcorrenciaInicio)       //            |-------|                            Ocorrencia
//            horasTrabDentroSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataInicioEscala)
//            println "5 ******************"
//        }
//        TimeDuration horasTrabForaSobreaviso
//        horasTrabForaSobreaviso = TimeCategory.minus(dataOcorrenciaFim, dataOcorrenciaInicio)
//        println "horasTrabForaSobreaviso: " + horasTrabForaSobreaviso
//        def hTrab = horasTrabForaSobreaviso.getHours()
//        def mTrab = horasTrabForaSobreaviso.getMinutes()
//        def floatTempoTrabForaSobreaviso = hTrab + mTrab/60
////        println "floatTempoTrabForaSobreaviso: " + floatTempoTrabForaSobreaviso
//        return floatTempoTrabForaSobreaviso
////        println "horasTrabForaSobreaviso: " + horasTrabForaSobreaviso
//    }

}