package sobreavisonutel

class Feriados {

    /*
    *       Armazenará os feriados nos quais o expediente deve ser de zero horas
    *       Para os demais feriados nos quais há expediente, o RH inserira nos TOPPontoREP - Fechamentos
    */


    String nomeFeriado
    Date dataFeriado

    static constraints = {
        nomeFeriado nullable: true
        dataFeriado nullable: false
    }
}
