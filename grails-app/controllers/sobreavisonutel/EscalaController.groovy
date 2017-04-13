package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class EscalaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

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
                    escala.save flush:true

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
        String usuario = "IVANILDO DE OLIVEIRA DA SILVA JR"
        render(view:'agenda',model:[diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreaviso,usuarioLogado:usuario])




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
//        println escalaSobreaviso
        render(view:'agenda',model:[diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreaviso])
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Escala.list(params), model:[escalaCount: Escala.count()]
    }

    def show(Escala escala) {
        respond escala
    }

    def create() {
        respond new Escala(params)
    }

    @Transactional
    def save(Escala escala) {
        if (escala == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (escala.hasErrors()) {
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
        respond escala
    }

    @Transactional
    def update(Escala escala) {
        if (escala == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (escala.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond escala.errors, view:'edit'
            return
        }

        escala.save flush:true

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
