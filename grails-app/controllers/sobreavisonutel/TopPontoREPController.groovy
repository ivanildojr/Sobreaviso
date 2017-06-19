package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import org.apache.commons.lang.time.DateUtils

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class TopPontoREPController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

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
        /* Remove todos os registros, busca novamente no banco do Top Ponto REP*/
        String resposta
        try {
            Thread.sleep(5000)
            throw new Exception("Erro produzido")




            TopPontoREP removerMarcacao = TopPontoREP.findAll()

            if (removerMarcacao != null) {
                removerMarcacao.delete flush: true
            }

            Jornada removerJornada = Jornada.findAll()
            if (removerJornada != null) {
                removerJornada.delete flush: true
            }

            Fechamentos removerFechamentos = Fechamentos.findAll()
            if (removerFechamentos != null) {
                removerFechamentos.delete flush: true
            }

            /*Pega os Fechamentos inseridos no Banco - Onde são lançados os sobreavisos e horas pela SRH*/
            List<Atendentes> atendentes = Atendentes.findAll()
            atendentes.each {
                println "Fechamentos: "
                List horarios = topPontoRepService.pegaFechamentos(it.nome)

                horarios.each {
                    println it.codFunc + "----" + it.dataLancamento + "----" + it.cargaHorariaLancada
                    Fechamentos fch = new Fechamentos()
                    fch = it
                    fch.save flush: true
                }
            }

            /*Pega as Jornadas e insere no banco*/
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
            atendentes.each {
                println "Horários: "
                Date partida = Date.parse("yyyy-MM-dd", "2013-01-01")

                while (partida.before(Date.parse("yyyy-MM-dd", new Date().format("yyyy-MM-dd")))) {
                    println it.nome + "----" + partida
                    pegaHorarios(partida, it.nome, false)

                    partida = partida.plus(1)
                }
            }

            /*Estou aqui!!!*/
            /*Remover pois sera feito visualmente
            * Talvez seja melhor calcular em tempo de execução
            * No momento que for calcular a quantidade de horas baseado no lancamento,
            * verificar o fechamento e se tiver hora a lancar nesse dia somar
            *
            * */

    //        List<Fechamentos> cargaHorariaFechamentos = Fechamentos.findAllByCodFunc(31)
    //        Double cargaHF = 0
    //        TimeDuration duracao
    //        TimeDuration cargaHorariaJornada = TimeCategory.minus(Date.parse("HH:mm", "00:00"), Date.parse("HH:mm", "00:00"))
    //        for (int j = 0; j < cargaHorariaFechamentos.size(); j ++) {
    //            def start = Date.parse("HH:mm", "00:00")
    //            def end = cargaHorariaFechamentos.get(j).cargaHorariaLancada
    //            duracao = TimeCategory.minus(end, start)
    //            cargaHorariaJornada = cargaHorariaJornada.plus(duracao)
    //
    //        }
    //
    //        cargaHF = cargaHorariaJornada.toMilliseconds() / 1000
    //        cargaHF = cargaHF / 60 / 60
    //        println "cargaHF: " + cargaHF
    //
    //        List<TopPontoREP> rep = TopPontoREP.findAllByDataMarcacao(Date.parse("yyyy-MM-dd","2017-05-31"))
    //
    //
    //        cargaHF += rep.get(0).cargaHorariaDia
    //        println "cargaHF + Hora do dia: " + cargaHF

                /*Remover*/

            resposta = "Banco atualizado com sucesso!!!"

            render(view:'atualizaTudo',model:[resposta:resposta])
        }catch(Exception e){
            resposta = "Houve um erro no processamento da atualização.<br>" +
                        e.toString()
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

        List<TopPontoREP> marcacoesIvanildo = TopPontoREP.findAllByNomeFuncionarioAndDataMarcacaoBetween(funcionarios[0],domingoCorrente,sabadoCorrente)
        List<Escala> sobreavisoIvanildo = Escala.findAllByAtendentesAndDiaLessThan(Atendentes.findAllByNome(funcionarios[0]),diaSemanaAtual)
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
        List<Escala> sobreavisoTorres = Escala.findAllByAtendentesAndDiaLessThan(Atendentes.findAllByNome(funcionarios[1]),diaSemanaAtual)
        Double horasTrabalhadasTorres = 0
        Double horasSobreavisoTorres = sobreavisoTorres.size() / 3
        marcacoesTorres.each{
            horasTrabalhadasTorres += it.cargaHorariaDia
        }
        horasTrabalhadas.add(horasTrabalhadasTorres)
        horasSobreaviso.add(horasSobreavisoTorres)

        List<TopPontoREP> marcacoesRudsom = TopPontoREP.findAllByNomeFuncionarioAndDataMarcacaoBetween(funcionarios[2],domingoCorrente,sabadoCorrente)
        List<Escala> sobreavisoRudsom = Escala.findAllByAtendentesAndDiaLessThan(Atendentes.findAllByNome(funcionarios[2]),diaSemanaAtual)
        Double horasTrabalhadasRudsom = 0
        Double horasSobreavisoRudsom = sobreavisoRudsom.size() / 3
        marcacoesRudsom.each{
            horasTrabalhadasRudsom += it.cargaHorariaDia
        }
        horasTrabalhadas.add(horasTrabalhadasRudsom)
        horasSobreaviso.add(horasSobreavisoRudsom)

        render(view:'pegaMarcacoes',model:[servidores:funcionarios,horasTrabalhadas:horasTrabalhadas,horasSobreaviso:horasSobreaviso,chSemana:chSemana])
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

        /*Remover a marcacao para re-inserir de forma atualizada*/
        if(apagaRegistros){
            TopPontoREP marcacaoRemover = TopPontoREP.findByDataMarcacaoAndNomeFuncionario(dataPartida,nomeFuncioario)
            if(marcacaoRemover!=null) {
                marcacaoRemover.delete flush: true
            }
        }

        List horarios = topPontoRepService.pegaHorario(dataPartida.format("yyyy-MM-dd"), nomeFuncioario)
        TopPontoREP registro = new TopPontoREP()

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

                registro.cargaHorariaDia = cargaHoras
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

                registro.save flush: true
            } else {
                /*IMPAR*/
                /*Se for impar a quantidade de registros, despreza-se o último*/

                def qtdeHorariosIndex = horarios.size() - IMPAR

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

                registro.cargaHorariaDia = cargaHoras
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


                registro.save flush: true
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
