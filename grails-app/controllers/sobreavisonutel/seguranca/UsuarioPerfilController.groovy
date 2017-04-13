package sobreavisonutel.seguranca

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class UsuarioPerfilController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond UsuarioPerfil.list(params), model:[usuarioPerfilCount: UsuarioPerfil.count()]
    }

    def show(UsuarioPerfil usuarioPerfil) {
        respond usuarioPerfil
    }

    def create() {
        respond new UsuarioPerfil(params)
    }

    @Transactional
    def save(UsuarioPerfil usuarioPerfil) {
        if (usuarioPerfil == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (usuarioPerfil.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond usuarioPerfil.errors, view:'create'
            return
        }

        usuarioPerfil.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'usuarioPerfil.label', default: 'UsuarioPerfil'), usuarioPerfil.id])
                redirect usuarioPerfil
            }
            '*' { respond usuarioPerfil, [status: CREATED] }
        }
    }

    def edit(UsuarioPerfil usuarioPerfil) {
        respond usuarioPerfil
    }

    @Transactional
    def update(UsuarioPerfil usuarioPerfil) {
        if (usuarioPerfil == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (usuarioPerfil.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond usuarioPerfil.errors, view:'edit'
            return
        }

        usuarioPerfil.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'usuarioPerfil.label', default: 'UsuarioPerfil'), usuarioPerfil.id])
                redirect usuarioPerfil
            }
            '*'{ respond usuarioPerfil, [status: OK] }
        }
    }

    @Transactional
    def delete(UsuarioPerfil usuarioPerfil) {

        if (usuarioPerfil == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        usuarioPerfil.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'usuarioPerfil.label', default: 'UsuarioPerfil'), usuarioPerfil.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuarioPerfil.label', default: 'UsuarioPerfil'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
