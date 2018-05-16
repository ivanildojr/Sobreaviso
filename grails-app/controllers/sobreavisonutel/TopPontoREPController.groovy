package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured
import groovy.time.TimeCategory
import groovy.time.TimeDuration

import java.sql.Time

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class TopPontoREPController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    public String dataInicio =  "2013-11-01"

    def topPontoRepService

    def pegaFuncionario(String funcionario){
        switch (funcionario) {
            case "Ivanildo": funcionario = "31"
                break;
            case "Torres": funcionario = "3"
                break;
            case "Rudsom": funcionario = "64"
                break;
            default: throw new Exception("Funcionario inexistente");
        }
        return funcionario
    }

    Date domingoCorrente
    Date sabadoCorrente
    def semanaDatas(){
        /*Pega as datas inicial e final da semana atual*/
        Date dataInicial = new Date()

        Calendar cal = Calendar.getInstance();
        cal.setTime(dataInicial)
        cal.setFirstDayOfWeek (Calendar.SUNDAY);
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        cal.add (Calendar.DAY_OF_MONTH, Calendar.SUNDAY - diaSemana)
        domingoCorrente = cal.getTime()

        cal.add (Calendar.DAY_OF_MONTH, 6)

        sabadoCorrente = cal.getTime()

    }

    def atualizaTudo(){
        /* Remove todos os registros de dois meses anteriores, busca novamente no banco do Top Ponto REP*/
        String resposta
        try {

            println "Removendo Maracações anteriores!!!"
            TopPontoREP.executeUpdate('delete from TopPontoREP')



            println "Removendo Jornadas!!!"
            Jornada.executeUpdate('delete from Jornada')


            println "Removendo Fechamentos!!!"
            Fechamentos.executeUpdate('delete from Fechamentos')

            println "Removendo Feriados!!!"
            Feriados.executeUpdate('delete from Feriados')


            /*Remove todos as ausencias/abonos*/
            println "Removendo Ausências/Abonos !!!"
            Abono.executeUpdate('delete from Abono')


            /*Remove todos os Afastamentos*/
            println "Removendo Afastamentos todos!!!"
            Afastamentos.executeUpdate('delete from Afastamentos')



            /*Pega os Fechamentos inseridos no Banco - Onde são lançados os sobreavisos e horas pela SRH*/
            println "Inserindo Fechamentos!!!"
            List<Atendentes> atendentes = Atendentes.findAll()
//            List<Atendentes> atendentes = Atendentes.findAllByNome("Ivanildo")
            atendentes.each {
                println "Fechamentos: "
                List horarios = topPontoRepService.pegaFechamentos(it.nome)

                horarios.each {

                        Fechamentos fch = new Fechamentos()
                        fch = it

                        /*Insere no banco o equivalente double das horas de fechamento*/
                        TimeDuration duracao
                        TimeDuration cargaHoraria = TimeCategory.minus(Date.parse("HH:mm", "00:00"), Date.parse("HH:mm", "00:00"))
                        def start = fch.cargaHorariaCreditoTime
                        def end = fch.cargaHorarioDebitoTime



                        duracao = TimeCategory.minus(end, start)

                        int dataDuration = fch.cargaHorariaDebito - fch.cargaHorariaCredito

                        TimeDuration horasData = new TimeDuration(dataDuration*24,0,0,0)

                        cargaHoraria = cargaHoraria.plus(duracao).plus(horasData)

                        println "Data Lancamento: " + fch.dataLancamento + "  Data Debito - Hora debito: " + fch.cargaHorariaDebito + ' ' + fch.cargaHorarioDebitoTime + " Data Credito - Hora Credito: " + fch.cargaHorariaCredito + ' ' + fch.cargaHorariaCreditoTime + " Duracao: " + cargaHoraria

                        Double cargaHoras = cargaHoraria.toMilliseconds() / 1000
                        cargaHoras = cargaHoras / 60 / 60
                        fch.cargaHorariaD = cargaHoras

                        String cargaHorariaS = resultado(cargaHoras)
                        println "cargaHorariaS: " + cargaHorariaS
                        fch.cargaHorariaS = cargaHorariaS



//                        cargaHoraria = TimeCategory.minus(Date.parse("HH:mm", "00:00"), Date.parse("HH:mm", "00:00"))
//                        start = fch.cargaHorariaCredito
//                        end = fch.cargaHorariaCredito
//                        duracao = TimeCategory.minus(end, start)
//                        cargaHoraria = cargaHoraria.plus(duracao)
//
//                        cargaHoras = cargaHoraria.toMilliseconds() / 1000
//                        cargaHoras = cargaHoras / 60 / 60
//
//                        fch.cargaHorariaCreditoD = (-1) * cargaHoras

//                        fch.save flush: true
                        if(!fch.hasErrors()) {
                            fch.save flush:true
                            println "Salvou com sucesso"
                        }
                        else {
                            println fch.errors
                            println "Não salvou"
                        }

                }
            }

            /*Pega os Abonos inseridos no Banco - Onde são lançados as ausencias e horas pelo servidor*/
            println "Inserindo Abonos!!!"
            atendentes.each {
                println "Abonos: "
                List horarios = topPontoRepService.pegaAbonos(it.nome)

                horarios.each {
                    println it.codFunc + "----" + it.dataLancamento + "----" + it.ausencia
                    Abono ab = new Abono()
                    ab = it

                    /*Insere no banco o equivalente double das horas de ausencia*/
                    TimeDuration duracao
                    TimeDuration cargaHoraria = TimeCategory.minus(Date.parse("HH:mm", "00:00"), Date.parse("HH:mm", "00:00"))
                    def start = new Time(0,0,0)
                    def end = ab.ausencia
                    duracao = TimeCategory.minus(end, start)
                    cargaHoraria = cargaHoraria.plus(duracao)

                    Double cargaHoras = cargaHoraria.toMilliseconds() / 1000
                    cargaHoras = cargaHoras / 60 / 60

                    ab.ausenciaD = cargaHoras



                    ab.save flush: true
                }
            }

            /*Pega os Feriados e insere no banco*/
            println "Inserindo Feriados!!!"
            List feriados = topPontoRepService.pegaFeriadosFunc()
            feriados.each {
                    println it.dataFeriado.toString() + "----" + it.nomeFeriado
                    Feriados f = new Feriados()
                    f = it
                    f.save flush: true
            }


            /*Pega os Afastamentos e insere no banco*/
            println "Inserindo Afastamentos!!!"
            atendentes.each {
                println "Afastamentos: "
                List horarios = topPontoRepService.pegaAfastamentosFunc(it.nome)
                horarios.each {
                    println it.codFunc + "----" + it.dtInicio + "----" + it.dtFim + "-----" + it.codMotivo + it.abonado
                    Afastamentos afa = new Afastamentos()
                    afa = it
                    afa.save flush: true
                }
            }

            /*Pega as Jornadas e insere no banco*/
            println "Inserindo Jornadas!!!"
            atendentes.each {
                println "Jornada: "
                List horarios = topPontoRepService.pegaJornadaFunc(it.nome)
                horarios.each {
                    println it.codFunc + "----" + it.dataInicio + "----" + it.marcacao
                    Jornada jor = new Jornada()
                    jor = it
                    jor.save flush: true
                }
            }

            /*Pega os horários na base TopPonto - desde a data informada até a data da execução*/
            println "Inserindo Marcações!!!"
            atendentes.each {
                println "Horários: "
                Date partida = Date.parse("yyyy-MM-dd", dataInicio)

                while (partida.before(Date.parse("yyyy-MM-dd", new Date().format("yyyy-MM-dd")))) {
                    println it.nome + "----" + partida
                    pegaHorarios(partida, it.nome, false)
                    partida = partida.plus(1)
                }
            }

            /*Por algum motivo ainda não identificado, quando usa-se o pegaAfastamentos dentro loop onde esta o pegaHorarios, a consulta fica aparentemente travado
            * Fazendo com o que não se retorne nada da consulta.
            * Somente colocando em outro loop é que a consulta passou a retornar.*/
            println "Ajustando Afastamentos nas Marcações !!!"
            atendentes.each {
                println "Afastamentos: "
                Date partida = Date.parse("yyyy-MM-dd", dataInicio)

                while (partida.before(Date.parse("yyyy-MM-dd", new Date().format("yyyy-MM-dd")))) {
                    println it.nome + "----" + partida
                    pegaAfastamentos(it.nome,partida)
                    partida = partida.plus(1)
                }
            }




            /*Ajustes nas folhas individuais de todos - Fatos encontratos na folha*/
            /*Ivanildo*/
            List<TopPontoREP> repIvanildo = TopPontoREP.findAllByCodFuncAndDataMarcacao(pegaFuncionario("Ivanildo"), Date.parse("yyyy-MM-dd","2015-12-29")) //Uma hora a mais no BH
            repIvanildo.each{
                it.bancoHoras = it.bancoHoras-1
                it.save flush:true
            }
            /*banco de horas não considerado pelo topponto*/
            repIvanildo = TopPontoREP.findAllByCodFuncAndDataMarcacaoBetween(pegaFuncionario("Ivanildo"), Date.parse("yyyy-MM-dd","2013-11-01"),Date.parse("yyyy-MM-dd","2013-11-30"))
            repIvanildo.each{
                it.bancoHoras = 0
                it.save flush:true
            }
//            /*Banco de Horas considerado errado*/
//            repIvanildo = TopPontoREP.findAllByCodFuncAndDataMarcacaoBetween(pegaFuncionario("Ivanildo"), Date.parse("yyyy-MM-dd","2013-11-01"),Date.parse("yyyy-MM-dd","2015-02-06"))
//            repIvanildo.each{
//                it.bancoHoras = 0
//                it.save flush:true
//            }
            List<Fechamentos> fchIvanildo = Fechamentos.findAllByCodFuncAndDataLancamento(pegaFuncionario("Ivanildo"), Date.parse("yyyy-MM-dd","2013-12-01")) //Credito errado
            fchIvanildo.each{
                it.cargaHorariaCredito = new Time(0,0,0)
                it.cargaHorariaD = 0
                it.save flush:true
            }


            /*Torres*/
            /*banco de horas não considerado pelo topponto*/
            List<TopPontoREP> repTorres = TopPontoREP.findAllByCodFuncAndDataMarcacaoBetween(pegaFuncionario("Torres"), Date.parse("yyyy-MM-dd","2013-11-01"),Date.parse("yyyy-MM-dd","2013-11-30"))
            repTorres.each{
                it.bancoHoras = 0
                it.save flush:true
            }
            /*Trabalhou nas férias e houve jornada no dia*/
            repTorres = TopPontoREP.findAllByCodFuncAndDataMarcacao(pegaFuncionario("Torres"), Date.parse("yyyy-MM-dd","2017-01-11"))
            repTorres.each{
                it.bancoHoras = 1+(5/60)
                it.save flush:true
            }

            resposta = "Banco atualizado com sucesso!!!"

            render(view:'atualizaTudo',model:[resposta:resposta])
        }catch(Exception e){
            resposta = "Houve um erro no processamento da atualização.<br>" +
                        e.toString() + e.printStackTrace()
            render(view:'atualizaTudo',model:[resposta:resposta])
        }

    }


    def pegaMarcacoes(){

        /*Atualizacao da base*/
        String[] funcionarios = Atendentes.findAllByNomeIsNotNull()
        semanaDatas()
        funcionarios.each{
            pegaHorariosEntreDuasDatas(domingoCorrente, it, sabadoCorrente)
        }



        List<Double> horasTrabalhadas = new ArrayList<Double>()
        List<Double> horasSobreaviso = new ArrayList<Double>()
        int diaSemanaAtual = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        def dataMaisRecente = Historico.executeQuery("select max(dataModificacao) from Historico where dataEscala between :dt1 and :dt2",[dt1:domingoCorrente,dt2:sabadoCorrente]).get(0)

        List<TopPontoREP> marcacoesIvanildo = TopPontoREP.findAllByNomeFuncionarioAndDataMarcacaoBetween(funcionarios[0],domingoCorrente,sabadoCorrente)
        List<Historico> sobreavisoIvanildo = Historico.findAllByAtendentesAndDiaLessThanAndDataModificacao(Atendentes.findAllByNome(funcionarios[0]),diaSemanaAtual,dataMaisRecente)
        Double horasTrabalhadasIvanildo = 0
        Double horasSobreavisoIvanildo = sobreavisoIvanildo.size() / 3
        Double chSemana = 0
        marcacoesIvanildo.each{
            horasTrabalhadasIvanildo += it.cargaHorariaDia
            chSemana += it.jornadaHorariaDia
        }
        horasTrabalhadas.add(horasTrabalhadasIvanildo)
        horasSobreaviso.add(horasSobreavisoIvanildo)

        List<TopPontoREP> marcacoesTorres = TopPontoREP.findAllByNomeFuncionarioAndDataMarcacaoBetween(funcionarios[1],domingoCorrente,sabadoCorrente)
        List<Historico> sobreavisoTorres = Historico.findAllByAtendentesAndDiaLessThanAndDataModificacao(Atendentes.findAllByNome(funcionarios[1]),diaSemanaAtual,dataMaisRecente)
        Double horasTrabalhadasTorres = 0
        Double horasSobreavisoTorres = sobreavisoTorres.size() / 3
        marcacoesTorres.each{
            horasTrabalhadasTorres += it.cargaHorariaDia
        }
        horasTrabalhadas.add(horasTrabalhadasTorres)
        horasSobreaviso.add(horasSobreavisoTorres)

        List<TopPontoREP> marcacoesRudsom = TopPontoREP.findAllByNomeFuncionarioAndDataMarcacaoBetween(funcionarios[2],domingoCorrente,sabadoCorrente)
        List<Historico> sobreavisoRudsom = Historico.findAllByAtendentesAndDiaLessThanAndDataModificacao(Atendentes.findAllByNome(funcionarios[2]),diaSemanaAtual,dataMaisRecente)
        Double horasTrabalhadasRudsom = 0
        Double horasSobreavisoRudsom = sobreavisoRudsom.size() / 3
        marcacoesRudsom.each{
            horasTrabalhadasRudsom += it.cargaHorariaDia
        }
        horasTrabalhadas.add(horasTrabalhadasRudsom)
        horasSobreaviso.add(horasSobreavisoRudsom)


        int mes = Calendar.getInstance().get(Calendar.MONTH)
        int ano = Calendar.getInstance().get(Calendar.YEAR)
        List<Double> bh = new ArrayList<Double>()
        Double bancoHorasAnterior = 0
        Double bancoHorasCorrente = 0
        funcionarios.each{
            int month = 11
            int year = 2013

            while(year <= ano){

//                Double fechamentosDebito = Fechamentos.executeQuery("select sum(f.cargaHorariaD) from Fechamentos f where cod_func = :func and month(data_lancamento) = :m and year(data_lancamento) = :y",
//                        [func: pegaFuncionario(it),m: month,y: year])[0]
//                Double fechamentosCredito = Fechamentos.executeQuery("select sum(f.cargaHorariaCreditoD) from Fechamentos f where cod_func = :func and month(data_lancamento) = :m and year(data_lancamento) = :y",
//                        [func: pegaFuncionario(it),m: month,y: year])[0]
                Double fechamentos = Fechamentos.executeQuery("select sum(f.cargaHorariaD) from Fechamentos f where cod_func = :func and month(data_lancamento) = :m and year(data_lancamento) = :y",
                        [func: pegaFuncionario(it),m: month,y: year])[0]
                Double abonos = Abono.executeQuery("select sum(a.ausenciaD) from Abono a where cod_func = ? and cod_motivo != 4 and month(data_lancamento) = ? and year(data_lancamento) = ?",
                        [pegaFuncionario(it),month,year])[0]
                Double bancoHoras = TopPontoREP.executeQuery("select sum(tp.bancoHoras) from TopPontoREP tp where cod_func = ? and month(data_marcacao) = ? and year(data_marcacao) = ?",
                        [pegaFuncionario(it),month,year])[0]

                if(abonos==null) abonos = 0
//                if(fechamentosDebito==null) fechamentosDebito = 0
//                if(fechamentosCredito==null) fechamentosCredito = 0
                if(fechamentos==null) fechamentos = 0
                if(bancoHoras==null) bancoHoras = 0


//                bancoHorasCorrente = bancoHoras - abonos + bancoHorasAnterior + fechamentosDebito + fechamentosCredito
                bancoHorasCorrente = bancoHoras - abonos + bancoHorasAnterior + fechamentos
                bancoHorasAnterior = bancoHorasCorrente
//                println "Mês/Ano: " + mes+"/"+ ano + " Month/Year: " + month+"/"+ year +" - Funcionario: " + it + " Fechamentos: " + fechamentosDebito + " Abonos: " + abonos + " bancoHoras: " + bancoHoras + " BancoHorasCorrente: " + bancoHorasCorrente
                //println " Month/Year: " + month+"/"+ year +" - Funcionario: " + it + " BancoHorasCorrente: " + bancoHorasCorrente



                month+=1
                if(month == 13){
                    year+=1
                    month = 1

                }
                if(year == ano && month == mes+2) break;
            }

            bh.add(bancoHorasCorrente)
            //println "Banco Horas: " + bancoHorasCorrente + " Funcionario: " + it
            bancoHorasAnterior = 0
            bancoHorasCorrente = 0


        }



        render(view:'pegaMarcacoes',model:[servidores:funcionarios,horasTrabalhadas:horasTrabalhadas,horasSobreaviso:horasSobreaviso,chSemana:chSemana,bh:bh])
    }

    private void pegaHorariosEntreDuasDatas(Date partida, String funcionario, Date termino) {
        partida.clearTime()
        /*Loop entre duas datas*/
        while (partida.before(termino)) {
//            println funcionario + ' ' + partida
            pegaHorarios(partida, funcionario, true)
            partida = partida.plus(1)


        }
    }

    private void pegaHorarios(Date dataPartida, String nomeFuncioario, Boolean apagaRegistros) {

        def PAR = 1
        def IMPAR = 2

        /*Remover a marcacao para re-inserir de forma atualizada - Está fazendo duas vezes no atualizaTudo?*/
        if(apagaRegistros){
            TopPontoREP marcacaoRemover = TopPontoREP.findByDataMarcacaoAndNomeFuncionario(dataPartida,nomeFuncioario)
            if(marcacaoRemover!=null) {
                marcacaoRemover.delete flush: true
            }
        }

        List horarios = topPontoRepService.pegaHorario(dataPartida.format("yyyy-MM-dd"), nomeFuncioario)
        TopPontoREP registro = new TopPontoREP()
        registro.codFunc = pegaFuncionario(nomeFuncioario).toInteger()

        if (horarios.size() > 0) {
            if (horarios.size() % 2 == 0) {
                /*PAR*/
                def qtdeHorariosIndex = horarios.size() - PAR

                def horasDia = 0
                def minutosDia = 0

                switch (horarios.size()) {
                    case 2:
                        registro.marcacao1 = horarios.get(0)
                        registro.marcacao2 = horarios.get(1)
                        break;
                    case 4:
                        registro.marcacao1 = horarios.get(0)
                        registro.marcacao2 = horarios.get(1)
                        registro.marcacao3 = horarios.get(2)
                        registro.marcacao4 = horarios.get(3)
                        break;
                    case 6:
                        registro.marcacao1 = horarios.get(0)
                        registro.marcacao2 = horarios.get(1)
                        registro.marcacao3 = horarios.get(2)
                        registro.marcacao4 = horarios.get(3)
                        registro.marcacao5 = horarios.get(4)
                        registro.marcacao6 = horarios.get(5)
                        break;

                }



                TimeDuration duracao
                TimeDuration cargaHoraria = TimeCategory.minus(Date.parse("HH:mm", "00:00"), Date.parse("HH:mm", "00:00"))
                for (int j = 0; j < qtdeHorariosIndex; j += 2) {


                    def start = horarios.get(j)
                    def end = horarios.get(j + 1)
                    duracao = TimeCategory.minus(end, start)
                    horasDia += duracao.getHours()
                    minutosDia += duracao.getMinutes()

                    cargaHoraria = cargaHoraria.plus(duracao)
                }

                Double cargaHoras = cargaHoraria.toMilliseconds() / 1000
                cargaHoras = cargaHoras / 60 / 60




                registro.nomeFuncionario = nomeFuncioario
                registro.dataMarcacao = dataPartida

                List<Jornada> jornadaDia = Jornada.findAllByCodFuncAndDataInicio(pegaFuncionario(nomeFuncioario),dataPartida)
                if(jornadaDia.size()>0){
                    /*O SRH inseriu um jornada para essa data. Mesmo se for feriado com meio expediente*/
                    Double cargaHorasJornada = pegaJornadaHoraria(jornadaDia, horasDia, minutosDia).toMilliseconds() / 1000
                    cargaHorasJornada = cargaHorasJornada / 60 / 60

                    registro.jornadaHorariaDia = cargaHorasJornada
                }else{
                    /*O SRH não inseriu uma jornada para esse dia, então devemos checar se é feriado o dia inteiro ou fim de semana
                    * Caso contrário deve ser de 8 horas a jornada*/
                    int diaDaSemana = fimDeSemanaCheck(dataPartida)
                    if((diaDaSemana == Calendar.SATURDAY) || (diaDaSemana == Calendar.SUNDAY)) {
                        registro.jornadaHorariaDia = 0
                    }else{

                        List<Feriados> feriados = Feriados.findAllByDataFeriado(dataPartida)

                        if(feriados.size()>0){
                            /*A data em questão é um feriado. Dessa forma a carga horaria deve ser zero*/
                            registro.jornadaHorariaDia = 0
                        }else{
                            registro.jornadaHorariaDia = 8
                        }
                    }
                }

                /*O sistema TOPPonto desconsidera a diferenca de ate 15 minutos */
                if(cargaHoras >= registro.jornadaHorariaDia - 0.25 && cargaHoras <= registro.jornadaHorariaDia){
                    cargaHoras = registro.jornadaHorariaDia
                }

                /*Ajuste para calcular corretamente o banco de horas*/
                if(cargaHoras > registro.jornadaHorariaDia){
                    registro.cargaHorariaDia = registro.jornadaHorariaDia
                    registro.bancoHoras = cargaHoras - registro.jornadaHorariaDia
                }else if(cargaHoras < registro.jornadaHorariaDia){
                    registro.cargaHorariaDia = cargaHoras
                    registro.bancoHoras = cargaHoras - registro.jornadaHorariaDia
                }else{
                    registro.cargaHorariaDia = cargaHoras
                    registro.bancoHoras = 0
                }


                ajusteAbonoBH(nomeFuncioario, dataPartida, registro)


                registro.save flush: true
            } else {
                /*IMPAR*/
                /*Se for impar a quantidade de registros, despreza-se o último*/

                def qtdeHorariosIndex = horarios.size() - IMPAR

                def horasDia = 0
                def minutosDia = 0

                switch (horarios.size()) {
                    case 3:
                        registro.marcacao1 = horarios.get(0)
                        registro.marcacao2 = horarios.get(1)
                        break;
                    case 5:
                        registro.marcacao1 = horarios.get(0)
                        registro.marcacao2 = horarios.get(1)
                        registro.marcacao3 = horarios.get(2)
                        registro.marcacao4 = horarios.get(3)
                        break;
                    case 1:
                        //Não faz nada pois não há registros de marcação suficientes
                        //Ajustar para não dar erro se não fizer nada.
                        break;

                }

                TimeDuration duracao
                TimeDuration cargaHoraria = TimeCategory.minus(Date.parse("HH:mm", "00:00"), Date.parse("HH:mm", "00:00"))
                for (int j = 0; j < qtdeHorariosIndex; j += 2) {


                    def start = horarios.get(j)
                    def end = horarios.get(j + 1)
                    duracao = TimeCategory.minus(end, start)
                    horasDia += duracao.getHours()
                    minutosDia += duracao.getMinutes()

                    cargaHoraria = cargaHoraria.plus(duracao)
                }

                Double cargaHoras = cargaHoraria.toMilliseconds() / 1000
                cargaHoras = cargaHoras / 60 / 60



                //registro.cargaHorariaDia = cargaHoras
                registro.nomeFuncionario = nomeFuncioario
                registro.dataMarcacao = dataPartida

                List<Jornada> jornadaDia = Jornada.findAllByCodFuncAndDataInicio(pegaFuncionario(nomeFuncioario),dataPartida)
                if(jornadaDia.size()>0){
                    /*O SRH inseriu um jornada para essa data. Mesmo se for feriado com meio expediente*/
                    Double cargaHorasJornada = pegaJornadaHoraria(jornadaDia, horasDia, minutosDia).toMilliseconds() / 1000
                    cargaHorasJornada = cargaHorasJornada / 60 / 60

                    registro.jornadaHorariaDia = cargaHorasJornada
                }else{
                    /*O SRH não inseriu uma jornada para esse dia, então devemos checar se é feriado o dia inteiro ou fim de semana
                    * Caso contrário deve ser de 8 horas a jornada*/
                    int diaDaSemana = fimDeSemanaCheck(dataPartida)
                    if((diaDaSemana == Calendar.SATURDAY) || (diaDaSemana == Calendar.SUNDAY)) {
                        registro.jornadaHorariaDia = 0
                    }else{

                        List<Feriados> feriados = Feriados.findAllByDataFeriado(dataPartida)

                        if(feriados.size()>0){
                            /*A data em questão é um feriado. Dessa forma a carga horaria deve ser zero*/
                            registro.jornadaHorariaDia = 0
                        }else{
                            registro.jornadaHorariaDia = 8
                        }
                    }
                }

                /*O sistema TOPPonto desconsidera a diferenca de ate 15 minutos */
                if(cargaHoras >= registro.jornadaHorariaDia - 0.25 && cargaHoras <= registro.jornadaHorariaDia){
                    cargaHoras = registro.jornadaHorariaDia
                }

                /*Ajuste para calcular corretamente o banco de horas*/
                if(cargaHoras > registro.jornadaHorariaDia){
                    registro.cargaHorariaDia = registro.jornadaHorariaDia
                    registro.bancoHoras = cargaHoras - registro.jornadaHorariaDia
                }else if(cargaHoras < registro.jornadaHorariaDia){
                    registro.cargaHorariaDia = cargaHoras
                    registro.bancoHoras = cargaHoras - registro.jornadaHorariaDia
                }else{
                    registro.cargaHorariaDia = cargaHoras
                    registro.bancoHoras = 0
                }


                ajusteAbonoBH(nomeFuncioario, dataPartida, registro)


                registro.save flush: true
            }
        }
    }



    def recalculaTudo(){/*Calculando ERRADO AINDA!*/


        List<Atendentes> nomeDosFuncionarios = Atendentes.findAll()
        Date dataPartida = Date.parse("yyyy-MM-dd", dataInicio)

        nomeDosFuncionarios.each{ nomeFuncioario->


                try {
                    while (dataPartida.before(Date.parse("yyyy-MM-dd", new Date().format("yyyy-MM-dd")))) {
                        //println nomeFuncioario.nome + "----" + dataPartida
                        List<TopPontoREP> horarios = TopPontoREP.findAllByDataMarcacaoAndNomeFuncionario(dataPartida, nomeFuncioario.nome)
                        if (horarios.size() > 0){

                            horarios[0].cargaHorariaDia = 0
                            horarios[0].jornadaHorariaDia = 0
                            horarios[0].bancoHoras = 0

                            TimeDuration cargaHoraria = TimeCategory.minus(Date.parse("HH:mm", "00:00"), Date.parse("HH:mm", "00:00"))

                            if (horarios[0].marcacao1 != null && horarios[0].marcacao2 != null) {
                                cargaHoraria = cargaHoraria.plus(TimeCategory.minus(horarios[0].marcacao2, horarios[0].marcacao1))
                            }
                            if (horarios[0].marcacao3 != null && horarios[0].marcacao4 != null) {
                                cargaHoraria = cargaHoraria.plus(TimeCategory.minus(horarios[0].marcacao4, horarios[0].marcacao3))
                            }
                            if (horarios[0].marcacao5 != null && horarios[0].marcacao6 != null) {
                                cargaHoraria = cargaHoraria.plus(TimeCategory.minus(horarios[0].marcacao6, horarios[0].marcacao5))
                            }

                            Double cargaHoras = cargaHoraria.toMilliseconds() / 1000
                            cargaHoras = cargaHoras / 60 / 60


                            List<Jornada> jornadaDia = Jornada.findAllByCodFuncAndDataInicio(pegaFuncionario(nomeFuncioario.nome), dataPartida)
                            if (jornadaDia.size() > 0) {
                                /*O SRH inseriu um jornada para essa data. Mesmo se for feriado com meio expediente*/
                                Double cargaHorasJornada = pegaJornadaHoraria(jornadaDia, 0, 0).toMilliseconds() / 1000
                                cargaHorasJornada = cargaHorasJornada / 60 / 60

                                horarios[0].jornadaHorariaDia = cargaHorasJornada
                            } else {
                                /*O SRH não inseriu uma jornada para esse dia, então devemos checar se é feriado o dia inteiro ou fim de semana
                                             * Caso contrário deve ser de 8 horas a jornada*/
                                int diaDaSemana = fimDeSemanaCheck(dataPartida)
                                if ((diaDaSemana == Calendar.SATURDAY) || (diaDaSemana == Calendar.SUNDAY)) {
                                    horarios[0].jornadaHorariaDia = 0
                                } else {

                                    List<Feriados> feriados = Feriados.findAllByDataFeriado(dataPartida)

                                    if (feriados.size() > 0) {
                                        /*A data em questão é um feriado. Dessa forma a carga horaria deve ser zero*/
                                        horarios[0].jornadaHorariaDia = 0
                                    } else {
                                        horarios[0].jornadaHorariaDia = 8
                                    }
                                }
                            }

                            /*O sistema TOPPonto desconsidera a diferenca de ate 15 minutos */
                            if (cargaHoras >= horarios[0].jornadaHorariaDia - 0.25 && cargaHoras <= horarios[0].jornadaHorariaDia) {
                                cargaHoras = horarios[0].jornadaHorariaDia
                            }

                            /*Ajuste para calcular corretamente o banco de horas*/
                            if (cargaHoras > horarios[0].jornadaHorariaDia) {
                                horarios[0].cargaHorariaDia = horarios[0].jornadaHorariaDia
                                horarios[0].bancoHoras = cargaHoras - horarios[0].jornadaHorariaDia
                            } else if (cargaHoras < horarios[0].jornadaHorariaDia) {
                                horarios[0].cargaHorariaDia = cargaHoras
                                horarios[0].bancoHoras = cargaHoras - horarios[0].jornadaHorariaDia
                            } else {
                                horarios[0].cargaHorariaDia = cargaHoras
                                horarios[0].bancoHoras = 0
                            }


                            /*Quando há marcação no ponto juntamente com compensação de horas, o sistema está descontando duas vezes
                            * Dessa forma, checar se existe na tabela abono e banco de horas < que zero. Nesse caso descontar o abono*/
                            List<Abono> abonos = Abono.findAllByCodFuncAndDataLancamento(pegaFuncionario(nomeFuncioario.nome), dataPartida)
                            if (abonos.size() > 0) {
                                if (horarios[0].bancoHoras < 0) {
                                    horarios[0].bancoHoras += abonos.get(0).ausenciaD
                                    //println "Ajuste no BH por meio do Abono: " + horarios[0].bancoHoras
                                }
                            }


                            horarios[0].save flush: true


                        }
                        dataPartida = dataPartida.plus(1)
                    }

                } catch (Exception e ) {
                    render(view: 'recalculaTudo', model: [resposta: "Erro na hora de recalcular!!!!" + e.printStackTrace()])

                }
                dataPartida = Date.parse("yyyy-MM-dd", dataInicio)

        }

        /*Por algum motivo ainda não identificado, quando usa-se o pegaAfastamentos dentro loop onde esta o pegaHorarios, a consulta fica aparentemente travado
            * Fazendo com o que não se retorne nada da consulta.
            * Somente colocando em outro loop é que a consulta passou a retornar.*/
        //println "Ajustando Afastamentos nas Marcações !!!"
        nomeDosFuncionarios.each {
            //println "Afastamentos: "
            Date partida = Date.parse("yyyy-MM-dd", dataInicio)

            while (partida.before(Date.parse("yyyy-MM-dd", new Date().format("yyyy-MM-dd")))) {
                //println it.nome + "----" + partida
                pegaAfastamentos(it.nome,partida) //Tentativa de que seja feita a consulta.
                partida = partida.plus(1)
            }
        }





        /*Ajustes nas folhas individuais de todos - Fatos encontratos na folha*/
        /*Ivanildo*/
        List<TopPontoREP> repIvanildo = TopPontoREP.findAllByCodFuncAndDataMarcacao(pegaFuncionario("Ivanildo"), Date.parse("yyyy-MM-dd","2015-12-29")) //Uma hora a mais no BH
        repIvanildo.each{
            it.bancoHoras = it.bancoHoras-1
            it.save flush:true
        }
        /*banco de horas não considerado pelo topponto*/
        repIvanildo = TopPontoREP.findAllByCodFuncAndDataMarcacaoBetween(pegaFuncionario("Ivanildo"), Date.parse("yyyy-MM-dd","2013-11-01"),Date.parse("yyyy-MM-dd","2013-11-30"))
        repIvanildo.each{
            it.bancoHoras = 0
            it.save flush:true
        }
        List<Fechamentos> fchIvanildo = Fechamentos.findAllByCodFuncAndDataLancamento(pegaFuncionario("Ivanildo"), Date.parse("yyyy-MM-dd","2013-12-01")) //Credito errado
        fchIvanildo.each{
            it.cargaHorariaCredito = new Time(0,0,0)
//            it.cargaHorariaCreditoD = 0
            it.save flush:true
        }


        /*Torres*/
        /*banco de horas não considerado pelo topponto*/
        List<TopPontoREP> repTorres = TopPontoREP.findAllByCodFuncAndDataMarcacaoBetween(pegaFuncionario("Torres"), Date.parse("yyyy-MM-dd","2013-11-01"),Date.parse("yyyy-MM-dd","2013-11-30"))
        repTorres.each{
            it.bancoHoras = 0
            it.save flush:true
        }
        /*Trabalhou nas férias e houve jornada no dia*/
        repTorres = TopPontoREP.findAllByCodFuncAndDataMarcacao(pegaFuncionario("Torres"), Date.parse("yyyy-MM-dd","2017-01-11"))
        repTorres.each{
            it.bancoHoras = 1+(5/60)
            it.save flush:true
        }



        render(view: 'recalculaTudo', model: [resposta: "Cálculo refeito!!!"])

    }

    //////////MÉTODO PARA CONVERTER FLOAT EM HORAS E MINUTOS
    static def resultado(def numero) {
        //numero = Math.round(numero)
        //println "numero: " + numero
        String resultado
        def sinal
        if(numero>=0) sinal=1  //sinal positivo, credito em horas
        else {
            sinal=0            //sinal negativo, débito de horas
            numero = numero * -1
        }
        Integer hNumero = numero
        Integer mNumero = Math.round((numero - hNumero)*60) //transformar decimal para minutos
        if(mNumero==60) {
            hNumero += 1
            mNumero = 0
        }
//      println "hNumero: " + hNumero
//      println "mNumero: " + mNumero
        if(hNumero==0) resultado = mNumero + " minutos"
        if(hNumero==1) resultado = mNumero + " minuto"
        if(mNumero==0) resultado = hNumero + " horas"
        if(mNumero==1) resultado = hNumero + " hora"
        if(hNumero>0 & mNumero>0) resultado = hNumero + " horas, " + mNumero + " minutos"
        if(hNumero>0 & mNumero==1) resultado = hNumero + " horas, " + mNumero + " minuto"
        if(hNumero==1 & mNumero>0) resultado = hNumero + " hora, " + mNumero + " minutos"
        if(hNumero==1 & mNumero==0) resultado = hNumero + " hora"
        if(hNumero==0 & mNumero==1) resultado = mNumero + " minuto"
        if(hNumero==0 & mNumero==0) resultado = "0"
//      println "resultado: " + resultado
        if(sinal==0) resultado = "- " + resultado
        println "resultado: " + resultado
        return resultado
    }

    private void ajusteAbonoBH(String nomeFuncioario, Date dataPartida, TopPontoREP registro) {
        /*Quando há marcação no ponto juntamente com compensação de horas, o sistema está descontando duas vezes
         * Dessa forma, checar se existe na tabela abono e banco de horas < que zero. Nesse caso descontar o abono*/
        List<Abono> abonos = Abono.findAllByCodFuncAndDataLancamento(pegaFuncionario(nomeFuncioario), dataPartida)
        if (abonos.size() > 0) {
            if (registro.bancoHoras < 0) {
                registro.bancoHoras += abonos.get(0).ausenciaD
                println "Ajuste no BH por meio do Abono: " + registro.bancoHoras
            }
        }
    }

    private void pegaAfastamentos(String nomeFuncioario, Date dataPartida) {



        List<Afastamentos> afastamentosDia = Afastamentos.findAllByCodFuncAndDtInicio(pegaFuncionario(nomeFuncioario), dataPartida.clearTime())

        if (afastamentosDia.size() > 0) {
            /*São os dias em que a jornada também pode ser zerada: alistamento eleitoral, ferias, viagem...
            * Verificar se todos os motivos geram a zerada da jornada do dia*/
            afastamentosDia.eachWithIndex { afs, it->
                List<TopPontoREP> listaMarcacoes = TopPontoREP.findAllByCodFuncAndDataMarcacaoBetween(pegaFuncionario(nomeFuncioario), afs.dtInicio, afs.dtFim)
                listaMarcacoes.eachWithIndex { mar,it2->

                    println "Jornada do dia antes: " + mar.dataMarcacao + " -- jornadaAntes: " + mar.jornadaHorariaDia
                    println "Banco H dia antes: " + mar.dataMarcacao + " -- bh-Antes: " + mar.bancoHoras

                    /*Aparentemente tanto a jornado do dia quanto o banco de horas do dia devem ser zerados
                    * Para Rudsom zerar funciona, mas para Ivanildo não.*/
                    if(afs.codMotivo == 8){
                            mar.bancoHoras = 0 //Férias não gera banco de horas
                    }else if(nomeFuncioario.equals("Ivanildo") && afs.codMotivo == 3){
                        if(mar.jornadaHorariaDia > 0){
                            println "BancoHoras Antes: " + mar.bancoHoras
                            mar.bancoHoras = mar.cargaHorariaDia
                            println "BancoHoras Depos: " + mar.bancoHoras
                        }
                    }else if(nomeFuncioario.equals("Torres") && afs.codMotivo == 3){
                        if(mar.jornadaHorariaDia > 0){
                            println "BancoHoras Antes: " + mar.bancoHoras
                            mar.bancoHoras = mar.cargaHorariaDia
                            println "BancoHoras Depos: " + mar.bancoHoras
                        }
                    }else{
                        mar.bancoHoras = 0
                    }

                    mar.jornadaHorariaDia = 0

                    println "Jornada do dia depois: " + mar.dataMarcacao + " -- jornadaDepois: " + mar.jornadaHorariaDia
                    println "Banco H dia depois: " + mar.dataMarcacao + " -- bh-depois: " + mar.bancoHoras
                    mar.save flush:true
                }
            }
        }
    }

    private int fimDeSemanaCheck(Date dataPartida) {
        Calendar date = Calendar.getInstance()
        date.setTime(dataPartida)
        int diaDaSemana = date.get(Calendar.DAY_OF_WEEK);
        diaDaSemana
    }

    private TimeDuration pegaJornadaHoraria(List<Jornada> jornadaDia, int horasDia, int minutosDia) {
        TimeDuration duracao
        TimeDuration cargaHorariaJornada = TimeCategory.minus(Date.parse("HH:mm", "00:00"), Date.parse("HH:mm", "00:00"))
        for (int j = 0; j < jornadaDia.size(); j += 2) {
            def start = jornadaDia.get(j).marcacao
            def end = jornadaDia.get(j + 1).marcacao
            duracao = TimeCategory.minus(end, start)
            horasDia += duracao.getHours()
            minutosDia += duracao.getMinutes()

            cargaHorariaJornada = cargaHorariaJornada.plus(duracao)

        }
        return cargaHorariaJornada
    }


    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TopPontoREP.list(params), model:[topPontoREPCount: TopPontoREP.count()]
    }

    def show(TopPontoREP topPontoREP) {
        respond topPontoREP
    }

    def create() {
        respond new TopPontoREP(params)
    }

    @Transactional
    def save(TopPontoREP topPontoREP) {
        if (topPontoREP == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (topPontoREP.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond topPontoREP.errors, view:'create'
            return
        }

        topPontoREP.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'topPontoREP.label', default: 'TopPontoREP'), topPontoREP.id])
                redirect topPontoREP
            }
            '*' { respond topPontoREP, [status: CREATED] }
        }
    }

    def edit(TopPontoREP topPontoREP) {
        respond topPontoREP
    }

    @Transactional
    def update(TopPontoREP topPontoREP) {
        if (topPontoREP == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (topPontoREP.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond topPontoREP.errors, view:'edit'
            return
        }

        topPontoREP.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'topPontoREP.label', default: 'TopPontoREP'), topPontoREP.id])
                redirect topPontoREP
            }
            '*'{ respond topPontoREP, [status: OK] }
        }
    }

    @Transactional
    def delete(TopPontoREP topPontoREP) {

        if (topPontoREP == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        topPontoREP.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'topPontoREP.label', default: 'TopPontoREP'), topPontoREP.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'topPontoREP.label', default: 'TopPontoREP'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
