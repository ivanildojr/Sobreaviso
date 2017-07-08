package sobreavisonutel

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AbonoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Abono.list(params), model:[abonoCount: Abono.count()]
    }

    def show(Abono abono) {
        respond abono
    }

    def create() {
        respond new Abono(params)
    }

    @Transactional
    def save(Abono abono) {
        if (abono == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (abono.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond abono.errors, view:'create'
            return
        }

        abono.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'abono.label', default: 'Abono'), abono.id])
                redirect abono
            }
            '*' { respond abono, [status: CREATED] }
        }
    }

    def edit(Abono abono) {
        respond abono
    }

    @Transactional
    def update(Abono abono) {
        if (abono == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (abono.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond abono.errors, view:'edit'
            return
        }

        abono.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'abono.label', default: 'Abono'), abono.id])
                redirect abono
            }
            '*'{ respond abono, [status: OK] }
        }
    }

    @Transactional
    def delete(Abono abono) {

        if (abono == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        abono.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'abono.label', default: 'Abono'), abono.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'abono.label', default: 'Abono'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
