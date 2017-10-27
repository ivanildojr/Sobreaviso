package sobreavisonutel

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import sobreavisonutel.seguranca.Usuario

import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class EscalaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def springSecurityService

    def chatBot(){

        def usuario = "Ivanildo"
        render(view:'chatbot',model:[chatUser:usuario])
    }


    def sobreavisoHistorico(){


        def sobreavisoNutel = params.getProperty("checkList")//params.getList()
        println "Domingo: " + params.getProperty("domingo")
        println "Marcado: " + params.getProperty("checkList")
        println "Sabado: " + params.getProperty("sabado")
        println "Sobreaviso marcações: " + sobreavisoNutel



        /*Avaliar necessidade de limpar para o Historico, para Escala ainda persiste*/
        List limpaEscala = Historico.findAllByDataEscalaBetween(Date.parse("dd/MM/yyyy", params.getProperty("domingo")),Date.parse("dd/MM/yyyy", params.getProperty("sabado")))

        /*limpaEscala.each{
            Historico historico = it
             historico.atendentes = null
             historico.save flush:true
        }*/


        Date dataModificacao = new Date()

        sobreavisoNutel.each{ s->

            switch (s.toString().charAt(0)/*Atendente*/){
                case 'I':/*Ivanildo*/
                    Atendentes atendente = Atendentes.findByNome("Ivanildo")
                    String dia = s.toString().charAt(2)/*Dia*/
                    String hora = s.toString().substring(4,6).toInteger()/*Hora*/
                    Date dataDia
                    dataDia = dataByDia(dia, dataDia)

                    Historico historico = new Historico()

                    historico.dia = dia
                    historico.hora = hora
                    historico.dataEscala = dataDia
                    historico.dataModificacao = dataModificacao
                    historico.atendentes = atendente
                    historico.login = springSecurityService.currentUser
                    historico.save flush:true

                    break;
                case 'T':/*Torres*/
                    Atendentes atendente = Atendentes.findByNome("Torres")
                    String dia = s.toString().charAt(2)/*Dia*/
                    String hora = s.toString().substring(4,6).toInteger()/*Hora*/
                    Date dataDia
                    dataDia = dataByDia(dia, dataDia)


                    Historico historico = new Historico()

                    historico.dia = dia
                    historico.hora = hora
                    historico.dataEscala = dataDia
                    historico.dataModificacao = dataModificacao
                    historico.atendentes = atendente
                    historico.login = springSecurityService.currentUser
                    historico.save flush:true

                    break;
                case 'R':/*Rudsom*/
                    Atendentes atendente = Atendentes.findByNome("Rudsom")
                    String dia = s.toString().charAt(2)/*Dia*/
                    String hora = s.toString().substring(4,6).toInteger()/*Hora*/
                    Date dataDia
                    dataDia = dataByDia(dia, dataDia)

                    Historico historico = new Historico()

                    historico.dia = dia
                    historico.hora = hora
                    historico.dataEscala = dataDia
                    historico.dataModificacao = dataModificacao
                    historico.atendentes = atendente
                    historico.login = springSecurityService.currentUser
                    historico.save flush:true

                    break;
            }
        }


        String[] diasNum = ["1","2","3","4","5","6","7"]
        String[] horasNum = ["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"]
        /*List escala = Escala.findAll()
        List escalaSobreaviso = new ArrayList()


        String letraInicial = ""
        escala.each{ e->

            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+e.hora)


            }
        }
        render(view:'agenda',model:[diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreaviso,usuarioLogado:springSecurityService.currentUser])*/

        def dataMaisRecente = Historico.executeQuery("select max(dataModificacao) from Historico where dataEscala between :data1 and :data2",
                [data1:Date.parse("dd/MM/yyyy", params.getProperty("domingo")),data2:Date.parse("dd/MM/yyyy", params.getProperty("sabado"))]).get(0)
        List historico = Historico.executeQuery("""
            from Historico where dataModificacao = :dataModificacao and dataEscala between :data1 and :data2 order by dataModificacao desc""",
                [dataModificacao:dataMaisRecente,data1:Date.parse("dd/MM/yyyy", params.getProperty("domingo")),data2:Date.parse("dd/MM/yyyy", params.getProperty("sabado"))])



        List escalaSobreavisoHistorico = new ArrayList()

        String letraInicial = ""
        historico.each{ e->

            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+e.hora)
            }
        }




        render(view:'historico',model:[diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreavisoHistorico])



    }

    private Date dataByDia(String dia, Date dataDia) {
        switch (dia) {
            case '1': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("domingo"))
                break;
            case '2': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("segunda"))
                break;
            case '3': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("terca"))
                break;
            case '4': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("quarta"))
                break;
            case '5': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("quinta"))
                break;
            case '6': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("sexta"))
                break;
            case '7': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("sabado"))
                break;
        }
        dataDia
    }


    def sobreaviso(){


        def sobreavisoNutel = params.list('checkList')

        List limpaEscala = Escala.findAll()

        limpaEscala.each{
            Escala escala = it
            escala.atendentes = null
            escala.save flush:true
        }


        sobreavisoNutel.each{ s->

            switch (s.toString().charAt(0)/*Atendente*/){
                case 'I':/*Ivanildo*/
                    Atendentes atendente = Atendentes.findByNome("Ivanildo")
                    Escala escala = Escala.findByDiaAndHora(s.toString().charAt(2)/*Dia*/,s.toString().substring(4,6).toInteger()/*Hora*/)
                    escala.atendentes = atendente

                    escala.validate()
                    if(!escala.hasErrors()) {
                        escala.save flush:true
                        println "Salvou com sucesso"
                    }
                    else {
                        println escala.errors
                        println "Não salvou"
                    }

//                    escala.save flush:true

                    break;
                case 'T':/*Torres*/
                    Atendentes atendente = Atendentes.findByNome("Torres")
                    Escala escala = Escala.findByDiaAndHora(s.toString().charAt(2)/*Dia*/,s.toString().substring(4,6).toInteger()/*Hora*/)
                    escala.atendentes = atendente
                    escala.save flush:true
                    break;
                case 'R':/*Rudsom*/
                    Atendentes atendente = Atendentes.findByNome("Rudsom")
                    Escala escala = Escala.findByDiaAndHora(s.toString().charAt(2)/*Dia*/,s.toString().substring(4,6).toInteger()/*Hora*/)
                    escala.atendentes = atendente
                    escala.save flush:true
                    break;
            }
        }

        String[] diasNum = ["1","2","3","4","5","6","7"]
        String[] horasNum = ["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"]
        List escala = Escala.findAll()
        List escalaSobreaviso = new ArrayList()


        String letraInicial = ""
        escala.each{ e->

            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+e.hora)


            }
        }
//        String usuario = "IVANILDO DE OLIVEIRA DA SILVA JR"
        render(view:'agenda',model:[diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreaviso/*,usuarioLogado:usuario*/])




    }

    def agenda(){

        String[] diasNum = ["1","2","3","4","5","6","7"]
        String[] horasNum = ["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"]
        List escala = Escala.findAll()
        List escalaSobreaviso = new ArrayList()




        String letraInicial = ""
        escala.each{ e->

            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+e.hora)
            }
        }
        //println escalaSobreaviso
        render(view:'agenda',model:[diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreaviso])
    }

    def historico(String dtInicial, String dtFinal){

        println "HISTORICO"

        String[] diasNum = ["1","2","3","4","5","6","7"]
        String[] horasNum = ["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"]
        String dataHistorico = params.get('dataHistorico')
        Date dataInicial
        Date dataFinal
        println "dataInicial: " + dataInicial
        if(dataHistorico) {
            dataInicial = Date.parse('dd/MM/yyyy', dataHistorico)

            Calendar cal = Calendar.getInstance();
            cal.setTime(dataInicial)
            cal.setFirstDayOfWeek (Calendar.SUNDAY);
            int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
            cal.add (Calendar.DAY_OF_MONTH, Calendar.SUNDAY - diaSemana)
            dataInicial = cal.getTime()

            cal.add (Calendar.DAY_OF_MONTH, 6)

            dataFinal = cal.getTime()
        }
        else {
            dataInicial = new Date()
            dataFinal = new Date()
        }

        println "Primeiro dia: " + dataInicial + " Data: " + dataHistorico + " Ultimo dia: " + dataFinal
        List historico = Historico.findAllByDataEscalaBetween(dataInicial,dataFinal)
        List escalaSobreavisoHistorico = new ArrayList()

//        println historico


        String letraInicial = ""
        historico.each{ e->

            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+e.hora)
            }
        }

        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");



        render(view:'historico',model:[inicial:df.format(dataInicial),final:dataFinal,diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreavisoHistorico])

    }

    def datasAjax(String dataHistorico){
        println "dataHistorico: " + dataHistorico

        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");

        println "Data da pesquisa passada como parametro: " + dataHistorico

        Date dataInicial
        Date dataFinal
        if(dataHistorico) {
            dataInicial = Date.parse('dd/MM/yyyy', dataHistorico)

            Calendar cal = Calendar.getInstance();
            cal.setTime(dataInicial)
            cal.setFirstDayOfWeek (Calendar.SUNDAY);
            int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
            cal.add (Calendar.DAY_OF_MONTH, Calendar.SUNDAY - diaSemana)
            dataInicial = cal.getTime()

            cal.add (Calendar.DAY_OF_MONTH, 6)

            dataFinal = cal.getTime()
        }
        else {
            dataInicial = new Date()
            dataFinal = new Date()
        }


        println "Data Inicial: " + dataInicial
        println "Data Final: " + dataFinal

        JsonUtil jsonUtil = new JsonUtil()
        jsonUtil.dataInicial = dataInicial
        jsonUtil.dataFinal = dataFinal

        /**************Busca os horários na tabela historico**********************/
        String[] diasNum = ["1","2","3","4","5","6","7"]
        String[] horasNum = ["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"]

        def dataMaisRecente = Historico.executeQuery("select max(dataModificacao) from Historico where dataEscala between :data1 and :data2",
                [data1:dataInicial,data2:dataFinal]).get(0)
        List historico = Historico.executeQuery("""
            from Historico where dataModificacao = :dataModificacao and dataEscala between :data1 and :data2 order by dataModificacao desc""",
                [dataModificacao:dataMaisRecente,data1:dataInicial,data2:dataFinal])



        println "Data mais Recente: " + dataMaisRecente


        List escalaSobreavisoHistorico = new ArrayList()

        String letraInicial = ""
        historico.each{ e->
            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+e.hora)
            }
        }


        jsonUtil.escalaSobreavisoHistorico = escalaSobreavisoHistorico
//        println Historico.executeQuery("from Historico where ")
        println historico
        /*************************************/

        render jsonUtil as JSON
    }


    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Escala.list(params), model:[escalaCount: Escala.count()]
    }

    def show(Escala escala) {
        println "show!"
        respond escala
    }

    def create() {
        println "create!"
        respond new Escala(params)
    }

    @Transactional
    def save(Escala escala) {
        if (escala == null) {
            println "não houve erro no save!"
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (escala.hasErrors()) {
            println "houve erro no save!"
            transactionStatus.setRollbackOnly()
            respond escala.errors, view:'create'
            return
        }

        escala.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'escala.label', default: 'Escala'), escala.id])
                redirect escala
            }
            '*' { respond escala, [status: CREATED] }
        }
    }

    def edit(Escala escala) {
        println "edit escala!"
        respond escala
    }

    @Transactional
    def update(Escala escala) {

        if (escala == null) {
            println "não houve erro no update!"
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (escala.hasErrors()) {
            println "houve erro no update!"
            transactionStatus.setRollbackOnly()
            respond escala.errors, view:'edit'
            return
        }

        escala.validate()
        if(!escala.hasErrors()) {
            escala.save flush:true
            println "Salvou com sucesso"
        }
        else {
            println escala.errors
            println "Não salvou"
        }


//        escala.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'escala.label', default: 'Escala'), escala.id])
                redirect escala
            }
            '*'{ respond escala, [status: OK] }
        }
    }

    @Transactional
    def delete(Escala escala) {

        if (escala == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        escala.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'escala.label', default: 'Escala'), escala.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'escala.label', default: 'Escala'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
