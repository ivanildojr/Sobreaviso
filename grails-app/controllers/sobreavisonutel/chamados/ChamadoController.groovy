package sobreavisonutel.chamados

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ChamadoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
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
