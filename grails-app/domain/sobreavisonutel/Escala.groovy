package sobreavisonutel

class Escala {

    String dia
    String hora
    Atendentes atendentes

    static constraints = {
        dia nullable: false, inList: ["1","2","3","4","5","6","7"]
        hora nullable: false, inList: ["0","1","2","3","4","5","6","7","8","9",
                                       "10","11","12","13","14","15","16","17","18","19",
                                       "20","21","22","23"]
        atendentes nullable: true
    }
}
