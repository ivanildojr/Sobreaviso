package sobreavisonutel

import java.sql.Time

class TopPontoREP {

    String nomeFuncionario
    Date dataMarcacao
    Time marcacao1
    Time marcacao2
    Time marcacao3
    Time marcacao4
    Time marcacao5
    Time marcacao6
    /*Carga horaria das marcacoes no dia*/
    Double cargaHorariaDia
    /*Deve ser inserido o valor da jornada a ser considerado
     para o dia.*/
    Double jornadaHorariaDia
    Double codFunc
    Double bancoHoras



    static constraints = {
        nomeFuncionario nullable: false
        dataMarcacao nullable: false
        cargaHorariaDia nullable: false
        marcacao1 nullable: false
        marcacao2 nullable: false
        marcacao3 nullable: true
        marcacao4 nullable: true
        marcacao5 nullable: true
        marcacao6 nullable: true
        jornadaHorariaDia nullable: false
        codFunc nullable: false
        bancoHoras nullable: false
    }
}
