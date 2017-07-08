package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class FeriadosController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Feriados.list(params), model:[feriadosCount: Feriados.count()]
    }

    def show(Feriados feriados) {
        respond feriados
    }

    def create() {
        respond new Feriados(params)
    }

    @Transactional
    def save(Feriados feriados) {
        if (feriados == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (feriados.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond feriados.errors, view:'create'
            return
        }

        feriados.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'feriados.label', default: 'Feriados'), feriados.id])
                redirect feriados
            }
            '*' { respond feriados, [status: CREATED] }
        }
    }

    def edit(Feriados feriados) {
        respond feriados
    }

    @Transactional
    def update(Feriados feriados) {
        if (feriados == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (feriados.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond feriados.errors, view:'edit'
            return
        }

        feriados.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'feriados.label', default: 'Feriados'), feriados.id])
                redirect feriados
            }
            '*'{ respond feriados, [status: OK] }
        }
    }

    @Transactional
    def delete(Feriados feriados) {

        if (feriados == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        feriados.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'feriados.label', default: 'Feriados'), feriados.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'feriados.label', default: 'Feriados'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
