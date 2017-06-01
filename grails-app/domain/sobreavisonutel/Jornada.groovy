package sobreavisonutel

import java.sql.Time

class Jornada {

    int codFunc
    Date dataInicio
    int codHorario
    Time marcacao
    int sequencia

    static constraints = {
        codFunc nullable: false
        dataInicio nullable: false
        codHorario nullable: false
        marcacao nullable: false
        sequencia nullable: false
    }
}
