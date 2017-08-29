package sobreavisonutel

import sobreavisonutel.Atendentes

class Ocorrencias {

    Date dataInicio //data da escala
    Date dataFim
    Date dataModificacao //data da modificação para fins de log
    Atendentes atendentes //atendente no dia
    String resumido
    String detalhado
    String login //Login de quem alterou a escala

    static constraints = {

        atendentes nullable: false
        dataInicio nullable: false
        dataFim nullable: false
        dataModificacao nullable: false
        resumido nullable: false
        login nullable: false;
    }
}
