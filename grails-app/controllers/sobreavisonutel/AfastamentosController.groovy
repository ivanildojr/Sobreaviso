package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class AfastamentosController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Afastamentos.list(params), model:[afastamentosCount: Afastamentos.count()]
    }

    def show(Afastamentos afastamentos) {
        respond afastamentos
    }

    def create() {
        respond new Afastamentos(params)
    }

    @Transactional
    def save(Afastamentos afastamentos) {
        if (afastamentos == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (afastamentos.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond afastamentos.errors, view:'create'
            return
        }

        afastamentos.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'afastamentos.label', default: 'Afastamentos'), afastamentos.id])
                redirect afastamentos
            }
            '*' { respond afastamentos, [status: CREATED] }
        }
    }

    def edit(Afastamentos afastamentos) {
        respond afastamentos
    }

    @Transactional
    def update(Afastamentos afastamentos) {
        if (afastamentos == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (afastamentos.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond afastamentos.errors, view:'edit'
            return
        }

        afastamentos.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'afastamentos.label', default: 'Afastamentos'), afastamentos.id])
                redirect afastamentos
            }
            '*'{ respond afastamentos, [status: OK] }
        }
    }

    @Transactional
    def delete(Afastamentos afastamentos) {

        if (afastamentos == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        afastamentos.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'afastamentos.label', default: 'Afastamentos'), afastamentos.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'afastamentos.label', default: 'Afastamentos'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
