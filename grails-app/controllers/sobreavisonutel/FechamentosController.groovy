package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured
import groovy.time.TimeCategory
import groovy.time.TimeDuration

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class FechamentosController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def topPontoRepService

    def index(Integer max) {
//        params.max = Math.min(max ?: 10, 100)
//        println "ocorrencias: " + params
          respond Fechamentos.list(params), model:[fechamentosCount: Fechamentos.count()]
    }

    def recarregar() {

        /*Pega os Fechamentos inseridos no Banco - Onde são lançados os sobreavisos e horas pela SRH*/
        println "Removendo Fechamentos!!!"
        Fechamentos.executeUpdate('delete from Fechamentos')

        println "Inserindo Fechamentos!!!"
        List<Atendentes> atendentes = Atendentes.findAll()
//            List<Atendentes> atendentes = Atendentes.findAllByNome("Ivanildo")
        atendentes.each {
            println "Fechamentos: "
            List horarios = topPontoRepService.pegaFechamentos(it.nome)

            horarios.each {

                Fechamentos fch = new Fechamentos()
                fch = it

                /*Insere no banco o equivalente double das horas de fechamento*/
                TimeDuration duracao
                TimeDuration cargaHoraria = TimeCategory.minus(Date.parse("HH:mm", "00:00"), Date.parse("HH:mm", "00:00"))
                def start = fch.cargaHorariaCreditoTime
                def end = fch.cargaHorarioDebitoTime

                duracao = TimeCategory.minus(end, start)

                int dataDuration = fch.cargaHorariaDebito - fch.cargaHorariaCredito

                TimeDuration horasData = new TimeDuration(dataDuration*24,0,0,0)

                cargaHoraria = cargaHoraria.plus(duracao).plus(horasData)

                Double cargaHoras = cargaHoraria.toMilliseconds() / 1000
                cargaHoras = cargaHoras / 60 / 60
                fch.cargaHorariaD = cargaHoras

                String cargaHorariaS = resultado(cargaHoras)
//                println "cargaHorariaS: " + cargaHorariaS
                fch.cargaHorariaS = cargaHorariaS

                if(!fch.hasErrors()) {
                    fch.save flush:true
//                    println "Salvou com sucesso"
                }
                else {
                    println fch.errors
//                    println "Não salvou"
                }

            }

        }
        redirect(action: "index")
    }

    //////////MÉTODO PARA CONVERTER FLOAT EM HORAS E MINUTOS
    static def resultado(def numero) {
        //numero = Math.round(numero)
        //println "numero: " + numero
        String resultado
        def sinal
        if(numero>=0) sinal=1  //sinal positivo, credito em horas
        else {
            sinal=0            //sinal negativo, débito de horas
            numero = numero * -1
        }
        Integer hNumero = numero
        Integer mNumero = Math.round((numero - hNumero)*60) //transformar decimal para minutos
        if(mNumero==60) {
            hNumero += 1
            mNumero = 0
        }
//      println "hNumero: " + hNumero
//      println "mNumero: " + mNumero
        if(hNumero==0) resultado = mNumero + " minutos"
        if(hNumero==1) resultado = mNumero + " minuto"
        if(mNumero==0) resultado = hNumero + " horas"
        if(mNumero==1) resultado = hNumero + " hora"
        if(hNumero>0 & mNumero>0) resultado = hNumero + " horas, " + mNumero + " minutos"
        if(hNumero>0 & mNumero==1) resultado = hNumero + " horas, " + mNumero + " minuto"
        if(hNumero==1 & mNumero>0) resultado = hNumero + " hora, " + mNumero + " minutos"
        if(hNumero==1 & mNumero==0) resultado = hNumero + " hora"
        if(hNumero==0 & mNumero==1) resultado = mNumero + " minuto"
        if(hNumero==0 & mNumero==0) resultado = "0"
//      println "resultado: " + resultado
        if(sinal==0) resultado = "- " + resultado
//        println "resultado: " + resultado
        return resultado
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
