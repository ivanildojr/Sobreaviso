package sobreavisonutel

class TopPontoREP {

    String nomeFuncionario
    Date dataMarcacao
    String marcacao1
    String marcacao2
    String marcacao3
    String marcacao4
    String marcacao5
    String marcacao6
    /*Carga horaria das marcacoes no dia*/
    Double cargaHorariaDia
    /*Deve ser inserido o valor da jornada a ser considerado
     para o dia.*/
    Double jornadaHorariaDia


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
    }
}
