package sobreavisonutel

import sobreavisonutel.Atendentes

class Ocorrencias {

    String status
    Date data //data da escala
    Date horaInicio
    Date horaFim
    Date dataModificacao //data da modificação para fins de log
    String atendentes //atendente no dia
    String resumido
    String detalhado
    String login //Login de quem alterou a escala

//    static mapping = {
//        detalhado sqlType: 'longtext', length: 10000
//        resumido sqlType: 'longtext', length: 10000
//    }


    static constraints = {

        status nullable: false
        data nullable: false
        atendentes nullable: false
        horaInicio nullable: false
        horaFim nullable: false
        dataModificacao nullable: false
        resumido (nullable: false, maxSize: 100000)
        detalhado (nullable: true, maxSize: 100000)
        login nullable: false
    }
}
