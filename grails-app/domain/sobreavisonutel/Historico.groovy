package sobreavisonutel

import sobreavisonutel.seguranca.Usuario

class Historico {

    String dia //dia da semana que corresponde a data
    String hora //hora do dia
    Date dataEscala //data da escala
    Date dataModificacao //data da modificação para fins de log
    Atendentes atendentes //atendente no dia
    String login //Login de quem alterou a escala

    static constraints = {
        dia nullable: false, inList: ["1","2","3","4","5","6","7"]
        hora nullable: false, inList: ["0","1","2","3","4","5","6","7","8","9",
                                       "10","11","12","13","14","15","16","17","18","19",
                                       "20","21","22","23"]
        atendentes nullable: true
        dataEscala nullable: false
        dataModificacao nullable: false
        login nullable: false;
    }
}
