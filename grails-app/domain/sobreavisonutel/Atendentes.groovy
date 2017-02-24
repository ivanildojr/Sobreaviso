package sobreavisonutel

class Atendentes {

    String nome
    String telefone

    static constraints = {
        nome nullable: false
        telefone nullable: false

    }
    String toString() {
        nome
    }

}
