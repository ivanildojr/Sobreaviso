package sobreavisonutel

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AtendentesController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Atendentes.list(params), model:[atendentesCount: Atendentes.count()]
    }

    def show(Atendentes atendentes) {
        respond atendentes
    }

    def create() {
        respond new Atendentes(params)
    }

    @Transactional
    def save(Atendentes atendentes) {
        if (atendentes == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (atendentes.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond atendentes.errors, view:'create'
            return
        }

        atendentes.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'atendentes.label', default: 'Atendentes'), atendentes.id])
                redirect atendentes
            }
            '*' { respond atendentes, [status: CREATED] }
        }
    }

    def edit(Atendentes atendentes) {
        respond atendentes
    }

    @Transactional
    def update(Atendentes atendentes) {
        if (atendentes == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (atendentes.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond atendentes.errors, view:'edit'
            return
        }

        atendentes.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'atendentes.label', default: 'Atendentes'), atendentes.id])
                redirect atendentes
            }
            '*'{ respond atendentes, [status: OK] }
        }
    }

    @Transactional
    def delete(Atendentes atendentes) {

        if (atendentes == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        atendentes.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'atendentes.label', default: 'Atendentes'), atendentes.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'atendentes.label', default: 'Atendentes'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
