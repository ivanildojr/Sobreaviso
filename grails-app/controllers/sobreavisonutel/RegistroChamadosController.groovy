package sobreavisonutel

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class RegistroChamadosController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond RegistroChamados.list(params), model:[registroChamadosCount: RegistroChamados.count()]
    }

    def show(RegistroChamados registroChamados) {
        respond registroChamados
    }

    def create() {
        respond new RegistroChamados(params)
    }

    @Transactional
    def save(RegistroChamados registroChamados) {
        if (registroChamados == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (registroChamados.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond registroChamados.errors, view:'create'
            return
        }

        registroChamados.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'registroChamados.label', default: 'RegistroChamados'), registroChamados.id])
                redirect registroChamados
            }
            '*' { respond registroChamados, [status: CREATED] }
        }
    }

    def edit(RegistroChamados registroChamados) {
        respond registroChamados
    }

    @Transactional
    def update(RegistroChamados registroChamados) {
        if (registroChamados == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (registroChamados.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond registroChamados.errors, view:'edit'
            return
        }

        registroChamados.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'registroChamados.label', default: 'RegistroChamados'), registroChamados.id])
                redirect registroChamados
            }
            '*'{ respond registroChamados, [status: OK] }
        }
    }

    @Transactional
    def delete(RegistroChamados registroChamados) {

        if (registroChamados == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        registroChamados.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'registroChamados.label', default: 'RegistroChamados'), registroChamados.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'registroChamados.label', default: 'RegistroChamados'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
