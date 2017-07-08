package sobreavisonutel

class Afastamentos {

    Date dtInicio
    Date dtFim
    boolean abonado
    boolean limitehoraExtra
    int codFunc
    int codMotivo

    static constraints = {
        dtInicio         nullable: false
        dtFim nullable: false
        abonado nullable: false
        limitehoraExtra nullable: false
        codFunc nullable: false
        codMotivo nullable: false
    }
}
