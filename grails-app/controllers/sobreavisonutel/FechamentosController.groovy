package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class FechamentosController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Fechamentos.list(params), model:[fechamentosCount: Fechamentos.count()]
    }

    def show(Fechamentos fechamentos) {
        respond fechamentos
    }

    def create() {
        respond new Fechamentos(params)
    }

    @Transactional
    def save(Fechamentos fechamentos) {
        if (fechamentos == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (fechamentos.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond fechamentos.errors, view:'create'
            return
        }

        fechamentos.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'fechamentos.label', default: 'Fechamentos'), fechamentos.id])
                redirect fechamentos
            }
            '*' { respond fechamentos, [status: CREATED] }
        }
    }

    def edit(Fechamentos fechamentos) {
        respond fechamentos
    }

    @Transactional
    def update(Fechamentos fechamentos) {
        if (fechamentos == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (fechamentos.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond fechamentos.errors, view:'edit'
            return
        }

        fechamentos.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'fechamentos.label', default: 'Fechamentos'), fechamentos.id])
                redirect fechamentos
            }
            '*'{ respond fechamentos, [status: OK] }
        }
    }

    @Transactional
    def delete(Fechamentos fechamentos) {

        if (fechamentos == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        fechamentos.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'fechamentos.label', default: 'Fechamentos'), fechamentos.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'fechamentos.label', default: 'Fechamentos'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
