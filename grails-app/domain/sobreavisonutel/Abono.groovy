package sobreavisonutel

import java.sql.Time

class Abono {

    int codFunc
    Date dataLancamento
    Time ausencia
    Double ausenciaD
    int codMotivo


    static constraints = {
        codFunc nullable: false
        dataLancamento nullable: false
        ausencia nullable: false
        codMotivo nullable:false
        ausenciaD nullable: false
    }
}
