package sobreavisonutel

import java.sql.Time

class Fechamentos {

    int codFunc
    Date dataLancamento
    Date cargaHorariaDebito
    Double cargaHorariaD
    String cargaHorariaS
    Date cargaHorariaCredito
    Time cargaHorarioDebitoTime
    Time cargaHorariaCreditoTime

    static constraints = {
        codFunc nullable: false
        dataLancamento nullable: false
        cargaHorariaDebito nullable: false
        cargaHorariaD nullable: false
        cargaHorariaS (nullable: true, maxSize: 200)
        cargaHorariaCredito nullable: false
        cargaHorariaCreditoTime nullable: false
        cargaHorarioDebitoTime nullable: false
    }
}
