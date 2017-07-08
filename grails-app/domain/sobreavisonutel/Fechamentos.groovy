package sobreavisonutel

import java.sql.Time

class Fechamentos {

    int codFunc
    Date dataLancamento
    Time cargaHorariaLancada
    Double cargaHorariaLancadaD
    Time cargaHorariaCredito
    Double cargaHorariaCreditoD

    static constraints = {
        codFunc nullable: false
        dataLancamento nullable: false
        cargaHorariaLancada nullable: false
        cargaHorariaLancadaD nullable: false
        cargaHorariaCredito nullable: false
        cargaHorariaCreditoD nullable: false
    }
}
