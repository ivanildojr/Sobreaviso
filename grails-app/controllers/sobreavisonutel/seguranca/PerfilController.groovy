package sobreavisonutel.seguranca

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class PerfilController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Perfil.list(params), model:[perfilCount: Perfil.count()]
    }

    def show(Perfil perfil) {
        respond perfil
    }

    def create() {
        respond new Perfil(params)
    }

    @Transactional
    def save(Perfil perfil) {
        if (perfil == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (perfil.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond perfil.errors, view:'create'
            return
        }

        perfil.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'perfil.label', default: 'Perfil'), perfil.id])
                redirect perfil
            }
            '*' { respond perfil, [status: CREATED] }
        }
    }

    def edit(Perfil perfil) {
        respond perfil
    }

    @Transactional
    def update(Perfil perfil) {
        if (perfil == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (perfil.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond perfil.errors, view:'edit'
            return
        }

        perfil.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'perfil.label', default: 'Perfil'), perfil.id])
                redirect perfil
            }
            '*'{ respond perfil, [status: OK] }
        }
    }

    @Transactional
    def delete(Perfil perfil) {

        if (perfil == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        perfil.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'perfil.label', default: 'Perfil'), perfil.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'perfil.label', default: 'Perfil'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
