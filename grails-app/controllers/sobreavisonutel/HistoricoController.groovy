package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class HistoricoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Historico.list(params), model:[historicoCount: Historico.count()]
    }

    def show(Historico historico) {
        respond historico
    }

    def create() {
        respond new Historico(params)
    }

    @Transactional
    def save(Historico historico) {
        if (historico == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (historico.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond historico.errors, view:'create'
            return
        }

        historico.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'historico.label', default: 'Historico'), historico.id])
                redirect historico
            }
            '*' { respond historico, [status: CREATED] }
        }
    }

    def edit(Historico historico) {
        respond historico
    }

    @Transactional
    def update(Historico historico) {
        if (historico == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (historico.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond historico.errors, view:'edit'
            return
        }

        historico.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'historico.label', default: 'Historico'), historico.id])
                redirect historico
            }
            '*'{ respond historico, [status: OK] }
        }
    }

    @Transactional
    def delete(Historico historico) {

        if (historico == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        historico.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'historico.label', default: 'Historico'), historico.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'historico.label', default: 'Historico'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
