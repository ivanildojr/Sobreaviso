package sobreavisonutel.chamados

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class SetoresController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Setores.list(params), model:[setoresCount: Setores.count()]
    }

    def show(Setores setores) {
        respond setores
    }

    def create() {
        respond new Setores(params)
    }

    @Transactional
    def save(Setores setores) {
        if (setores == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (setores.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond setores.errors, view:'create'
            return
        }

        setores.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'setores.label', default: 'Setores'), setores.id])
                redirect setores
            }
            '*' { respond setores, [status: CREATED] }
        }
    }

    def edit(Setores setores) {
        respond setores
    }

    @Transactional
    def update(Setores setores) {
        if (setores == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (setores.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond setores.errors, view:'edit'
            return
        }

        setores.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'setores.label', default: 'Setores'), setores.id])
                redirect setores
            }
            '*'{ respond setores, [status: OK] }
        }
    }

    @Transactional
    def delete(Setores setores) {

        if (setores == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        setores.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'setores.label', default: 'Setores'), setores.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'setores.label', default: 'Setores'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
