package sobreavisonutel

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import java.sql.Time

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')


class OcorrenciasController {

    static scaffold = Ocorrencias

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def springSecurityService

    def editar() {
        def mapRetorno = [:]
//        println "ocorrencias: " + params.id
//        def ocorrencia = Ocorrencias.findAllById(params.id)
        def ocorrencia = Ocorrencias.executeQuery("from Ocorrencias where id='$params.id'")
        def id = ocorrencia.id.get(0)
        def atendentes = ocorrencia.atendentes.get(0)
        def dataF = ocorrencia.data.getAt(0)
        def horaInicioF = ocorrencia.horaInicio.get(0)
        def horaFimF = ocorrencia.horaFim.get(0)
        def relato = ocorrencia.resumido.get(0)
        dataF = dataF.format("dd/MM/yyyy")
        horaInicioF = horaInicioF.format("HH:mm")
        horaFimF = horaFimF.format("HH:mm")

        mapRetorno << [id: id]
        mapRetorno << [atendentes: atendentes]
        mapRetorno << [diaF: dataF]
        mapRetorno << [horaInicioF: horaInicioF]
        mapRetorno << [horaFimF: horaFimF]
        mapRetorno << [detalhado: relato]
//        render data as JSON
        println mapRetorno as JSON
        render mapRetorno as JSON
    }

    def excluirOcorrencia() {
        println params.values()
        def id = params.values().getAt(3)
//        println id
        Ocorrencias ocorrencia = Ocorrencias.findById(id as Long)
        ocorrencia.status = "Inativo"
        ocorrencia.validate()
        if(!ocorrencia.hasErrors()) {
            ocorrencia.save flush: true
        }
        println "Inativou registro"
        redirect(action: "index")
    }

    def index() {
        def listaOcorrencias = Ocorrencias.executeQuery("from Ocorrencias where status='Ativo' order by data desc")
        render(view: "index", model: [listaOcorrencias:listaOcorrencias])
    }

    def ocorrencias() {

        println params.values() //imprime tudo que foi retornado do formulario da view

        def id = params.list("idN").get(0)
        if(!id=="") id as Long
        println "id: " + id
        def atendente = params.list("atendente").get(0)  //recebe atendentes e dataInicio da view e tira da list
        def data = params.list("data").get(0)
        def horaInicio = params.list("horaInicio").get(0)
        def horaFim = params.list("horaFim").get(0)
        def relato = params.list("ocorrencia").get(0)
        def hInicio = horaInicio.substring(0,2)
        def mInicio = horaInicio.substring(3,5)
        def hFim = horaFim.substring(0,2)
        def mFim = horaFim.substring(3,5)
//        println "HoraInicio e HoraFim: " + hInicio + mInicio + "-" + hFim + mFim

        data = Date.parse("dd/MM/yyyy", data)

        Calendar calenInicio = Calendar.getInstance();
        calenInicio.setTime(data);
        calenInicio.set(Calendar.HOUR, hInicio as Integer)
        calenInicio.set(Calendar.MINUTE, mInicio as Integer)

        Calendar calenFim = Calendar.getInstance();
        calenFim.setTime(data);
        calenFim.set(Calendar.HOUR, hFim as Integer)
        calenFim.set(Calendar.MINUTE, mFim as Integer)

        ////////////// VERIFICA SE PASSOU DE UM DIA PRO OUTRO
        if(calenFim<calenInicio) {
            calenFim.add(calenFim.DATE,1)
            println "calenFIm: " + calenFim
        }

        Date dataModificacao = new Date()

        if(id=="") {
            Ocorrencias ocorrencia = new Ocorrencias()
            ocorrencia.status = "Ativo"
            ocorrencia.dataModificacao = dataModificacao
            ocorrencia.atendentes = atendente
            ocorrencia.data = data
            ocorrencia.horaInicio = calenInicio.getTime()
            ocorrencia.horaFim = calenFim.getTime()
            ocorrencia.resumido = relato
            ocorrencia.detalhado = relato
            ocorrencia.login = springSecurityService.currentUser
            ocorrencia.validate()
            if(!ocorrencia.hasErrors()) {
                ocorrencia.save flush:true
                println "Salvou com sucesso"
            }
            else {
                println ocorrencia.errors
                println "Não salvou"
            }
            redirect(action: "index")
        }
        else {
            Ocorrencias ocorrencia = Ocorrencias.findById(id)
            ocorrencia.status = "Ativo"
            ocorrencia.dataModificacao = dataModificacao
            ocorrencia.atendentes = atendente
            ocorrencia.data = data
            ocorrencia.horaInicio = calenInicio.getTime()
            ocorrencia.horaFim = calenFim.getTime()
            ocorrencia.resumido = relato
            ocorrencia.detalhado = relato
            ocorrencia.login = springSecurityService.currentUser
            ocorrencia.validate()
            if(!ocorrencia.hasErrors()) {
                ocorrencia.save flush:true
                println "Salvou com sucesso"
            }
            else {
                println ocorrencia.errors
                println "Não salvou"
            }
            redirect(action: "index")
        }

    }
}
