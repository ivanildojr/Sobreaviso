package sobreavisonutel.chamados

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovy.json.JsonBuilder

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class ChamadoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def atualizaOrdemDB(long ordemAtendimento, int ordem){
        println "OrdemAtendimento: " + ordemAtendimento + " - " + ordem

        Chamado chamado = Chamado.findAllByOrdemAtendimento(ordemAtendimento).get(0);

        chamado.ordem = ordem

        chamado.save flush:true

        render ordem

    }



    def index(Integer max) {
        params.max = Math.min(max ?: 0, 100)

        respond Chamado.list(params), model:[chamadoCount: Chamado.count()]
    }

    def show(Chamado chamado) {
        respond chamado
    }

    def create() {
        respond new Chamado(params)
    }

    @Transactional
    def save(Chamado chamado) {
        if (chamado == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (chamado.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond chamado.errors, view:'create'
            return
        }

        /*Insere o endereço IP do solicitante*/
        chamado.ip = request.getRemoteAddr()

        /*Insere o numero de chamado como ordem de atendimento*/
        def dataAtendimtento = chamado.dataAbertura
        def horaAtendimento = chamado.horaAbertura
        def currentTime = System.currentTimeSeconds() % 100
        def ordem =  dataAtendimtento.substring(6,10)+dataAtendimtento.substring(3,5)+dataAtendimtento.substring(0,2)+horaAtendimento.substring(3,5)+horaAtendimento.substring(0,2)+currentTime
        chamado.ordemAtendimento = ordem.toLong()

        chamado.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'chamado.label', default: 'Chamado'), chamado.id])
                redirect chamado
            }
            '*' { respond chamado, [status: CREATED] }
        }
    }

    def edit(Chamado chamado) {
        respond chamado
    }

    @Transactional
    def update(Chamado chamado) {
        if (chamado == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (chamado.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond chamado.errors, view:'edit'
            return
        }

        chamado.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'chamado.label', default: 'Chamado'), chamado.id])
                redirect chamado
            }
            '*'{ respond chamado, [status: OK] }
        }
    }

    @Transactional
    def delete(Chamado chamado) {

        if (chamado == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        chamado.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'chamado.label', default: 'Chamado'), chamado.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'chamado.label', default: 'Chamado'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
