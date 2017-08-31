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
        def ocorrencia = Ocorrencias.findAllById(params.id)
        def atendentes = ocorrencia.atendentes.get(0)
        def dataF = ocorrencia.data.getAt(0)
        def horaInicioF = ocorrencia.horaInicio.get(0)
        def horaFimF = ocorrencia.horaFim.get(0)
        def relato = ocorrencia.resumido.get(0)
        dataF = dataF.format("dd-MM-yyyy")
        horaInicioF = horaInicioF.format("HH:mm")
        horaFimF = horaFimF.format("HH:mm")
//        mapRetorno = ocorrencia
        println dataF
        mapRetorno << [atendentes: atendentes]
        mapRetorno << [diaF: dataF]
        mapRetorno << [horaInicioF: horaInicioF]
        mapRetorno << [horaFimF: horaFimF]
        mapRetorno << [detalhado: relato]
//        render data as JSON
        println mapRetorno as JSON
        render mapRetorno as JSON
    }

    def apagar() {

    }

    def index() {
        def listaOcorrencias = Ocorrencias.findAll()
        render(view: "index", model: [listaOcorrencias:listaOcorrencias])
    }

    def ocorrencias() {

//        println params.values() //imprime tudo que foi retornado do formulario da view
        def atendente = params.list("atendente").get(0)  //recebe atendentes e dataInicio da view e tira da list
        def data = params.list("data").get(0)
        def horaInicio = params.list("horaInicio").get(0)
        def horaFim = params.list("horaFim").get(0)
        def relato = params.list("ocorrencia").get(0)
        Date dataModificacao = new Date()

        Ocorrencias ocorrencia = new Ocorrencias()
        ocorrencia.dataModificacao = dataModificacao
        ocorrencia.atendentes = atendente
        ocorrencia.data = Date.parse("dd/MM/yyyy", data)
        ocorrencia.horaInicio = Date.parse("HH:mm", horaInicio)
        ocorrencia.horaFim = Date.parse("HH:mm", horaFim)
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


        def listaOcorrencias = Ocorrencias.findAll()

        render(view: "index", model: [listaOcorrencias:listaOcorrencias])

    }
}
