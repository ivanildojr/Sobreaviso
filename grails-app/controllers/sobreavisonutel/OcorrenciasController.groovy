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
        def atendentes = ocorrencia.atendentes.get(0)
        def dataF = ocorrencia.data.getAt(0)
        def horaInicioF = ocorrencia.horaInicio.get(0)
        def horaFimF = ocorrencia.horaFim.get(0)
        def relato = ocorrencia.resumido.get(0)
        dataF = dataF.format("dd/MM/yyyy")
        horaInicioF = horaInicioF.format("HH:mm")
        horaFimF = horaFimF.format("HH:mm")

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
//        println params.values()
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
        def atendente = params.list("atendente").get(0)  //recebe atendentes e dataInicio da view e tira da list
        def data = params.list("data").get(0)
        def horaInicio = params.list("horaInicio").get(0)
        def horaFim = params.list("horaFim").get(0)
        def relato = params.list("ocorrencia").get(0)

        data = Date.parse("dd/MM/yyyy", data)
        horaInicio = Date.parse("HH:ss", horaInicio)
        horaFim = Date.parse("HH:ss", horaFim)

        ////////////// VERIFICA SE PASSOU DE UM DIA PRO OUTRO
        if(horaFim<horaInicio) {
            horaFim = data+1
            println "horaFim: " + horaFim
        }

        Date dataModificacao = new Date()

        Ocorrencias ocorrencia = new Ocorrencias()
        ocorrencia.status = "Ativo"
        ocorrencia.dataModificacao = dataModificacao
        ocorrencia.atendentes = atendente
        ocorrencia.data = data
        ocorrencia.horaInicio = horaInicio
        ocorrencia.horaFim = horaFim
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
