package sobreavisonutel

import grails.plugin.springsecurity.annotation.Secured
import groovy.time.TimeCategory
import groovy.time.TimeDuration

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class TopPontoREPController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def topPontoRepService

    def pegaMarcacoes(){
        /*Está somando os minutos além de 60.*/
//        Date partida = Date.parse("yyyy-MM-dd","2017-05-09")
//
//        while(partida.before(Date.parse("yyyy-MM-dd","2017-05-10"))){
//            pegaHorarios(partida, "Ivanildo")
//            partida = partida.plus(1)
//            println partida
//        }

        topPontoRepService.pegaJornadaFunc("Ivanildo")



        render(view:'pegaMarcacoes',model:[])
    }

    private void pegaHorarios(Date dataPartida, String nomeFuncioario) {

        def PAR = 1
        def IMPAR = 2

        List horarios = topPontoRepService.pegaHorario(dataPartida.format("yyyy-MM-dd"), nomeFuncioario)
        TopPontoREP registro = new TopPontoREP()

        if (horarios.size() > 0) {
            if (horarios.size() % 2 == 0) {
                /*PAR*/
                def qtdeHorariosIndex = horarios.size() - PAR

                def horasDia = 0
                def minutosDia = 0

                switch (horarios.size()) {
                    case 2:
                        registro.marcacao1 = horarios.get(0).replace(":00.0000000", "")
                        registro.marcacao2 = horarios.get(1).replace(":00.0000000", "")
                        break;
                    case 4:
                        registro.marcacao1 = horarios.get(0).replace(":00.0000000", "")
                        registro.marcacao2 = horarios.get(1).replace(":00.0000000", "")
                        registro.marcacao3 = horarios.get(2).replace(":00.0000000", "")
                        registro.marcacao4 = horarios.get(3).replace(":00.0000000", "")
                        break;
                    case 6:
                        registro.marcacao1 = horarios.get(0).replace(":00.0000000", "")
                        registro.marcacao2 = horarios.get(1).replace(":00.0000000", "")
                        registro.marcacao3 = horarios.get(2).replace(":00.0000000", "")
                        registro.marcacao4 = horarios.get(3).replace(":00.0000000", "")
                        registro.marcacao5 = horarios.get(4).replace(":00.0000000", "")
                        registro.marcacao6 = horarios.get(5).replace(":00.0000000", "")
                        break;

                }

                TimeDuration duracao
                TimeDuration cargaHoraria = TimeCategory.minus(Date.parse("HH:mm", "00:00"), Date.parse("HH:mm", "00:00"))
                for (int j = 0; j <= qtdeHorariosIndex; j += 2) {


                    def start = Date.parse("HH:mm", horarios.get(j).replace(":00.0000000", ""))
                    def end = Date.parse("HH:mm", horarios.get(j + 1).replace(":00.0000000", ""))
                    duracao = TimeCategory.minus(end, start)
                    horasDia += duracao.getHours()
                    minutosDia += duracao.getMinutes()

                    cargaHoraria = cargaHoraria.plus(duracao)
                }

                Double cargaHoras = cargaHoraria.toMilliseconds() / 1000
                cargaHoras = cargaHoras / 60 / 60

                registro.cargaHorariaDia = cargaHoras
                registro.nomeFuncionario = nomeFuncioario
                registro.dataMarcacao = dataPartida


                registro.save flush: true
            } else {
                /*IMPAR*/
                /*Se for impar a quantidade de registros, despreza-se o último*/
                println "IMPAR"
                def qtdeHorariosIndex = horarios.size() - IMPAR

                def horasDia = 0
                def minutosDia = 0

                switch (horarios.size()) {
                    case 2:
                        registro.marcacao1 = horarios.get(0).replace(":00.0000000", "")
                        registro.marcacao2 = horarios.get(1).replace(":00.0000000", "")
                        break;
                    case 4:
                        registro.marcacao1 = horarios.get(0).replace(":00.0000000", "")
                        registro.marcacao2 = horarios.get(1).replace(":00.0000000", "")
                        registro.marcacao3 = horarios.get(2).replace(":00.0000000", "")
                        registro.marcacao4 = horarios.get(3).replace(":00.0000000", "")
                        break;
                    case 6:
                        registro.marcacao1 = horarios.get(0).replace(":00.0000000", "")
                        registro.marcacao2 = horarios.get(1).replace(":00.0000000", "")
                        registro.marcacao3 = horarios.get(2).replace(":00.0000000", "")
                        registro.marcacao4 = horarios.get(3).replace(":00.0000000", "")
                        registro.marcacao5 = horarios.get(4).replace(":00.0000000", "")
                        registro.marcacao6 = horarios.get(5).replace(":00.0000000", "")
                        break;

                }

                TimeDuration duracao
                TimeDuration cargaHoraria = TimeCategory.minus(Date.parse("HH:mm", "00:00"), Date.parse("HH:mm", "00:00"))
                for (int j = 0; j <= qtdeHorariosIndex; j += 2) {


                    def start = Date.parse("HH:mm", horarios.get(j).replace(":00.0000000", ""))
                    def end = Date.parse("HH:mm", horarios.get(j + 1).replace(":00.0000000", ""))
                    duracao = TimeCategory.minus(end, start)
                    horasDia += duracao.getHours()
                    minutosDia += duracao.getMinutes()

                    cargaHoraria = cargaHoraria.plus(duracao)
                }

                Double cargaHoras = cargaHoraria.toMilliseconds() / 1000
                cargaHoras = cargaHoras / 60 / 60

                registro.cargaHorariaDia = cargaHoras
                registro.nomeFuncionario = nomeFuncioario
                registro.dataMarcacao = dataPartida


                registro.save flush: true
            }
        }
    }


    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TopPontoREP.list(params), model:[topPontoREPCount: TopPontoREP.count()]
    }

    def show(TopPontoREP topPontoREP) {
        respond topPontoREP
    }

    def create() {
        respond new TopPontoREP(params)
    }

    @Transactional
    def save(TopPontoREP topPontoREP) {
        if (topPontoREP == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (topPontoREP.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond topPontoREP.errors, view:'create'
            return
        }

        topPontoREP.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'topPontoREP.label', default: 'TopPontoREP'), topPontoREP.id])
                redirect topPontoREP
            }
            '*' { respond topPontoREP, [status: CREATED] }
        }
    }

    def edit(TopPontoREP topPontoREP) {
        respond topPontoREP
    }

    @Transactional
    def update(TopPontoREP topPontoREP) {
        if (topPontoREP == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (topPontoREP.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond topPontoREP.errors, view:'edit'
            return
        }

        topPontoREP.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'topPontoREP.label', default: 'TopPontoREP'), topPontoREP.id])
                redirect topPontoREP
            }
            '*'{ respond topPontoREP, [status: OK] }
        }
    }

    @Transactional
    def delete(TopPontoREP topPontoREP) {

        if (topPontoREP == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        topPontoREP.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'topPontoREP.label', default: 'TopPontoREP'), topPontoREP.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'topPontoREP.label', default: 'TopPontoREP'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
