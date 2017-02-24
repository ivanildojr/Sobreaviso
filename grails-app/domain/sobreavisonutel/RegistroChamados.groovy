package sobreavisonutel

class RegistroChamados {

    String data
    String cpf
    String telefoneOrigem
    String statusLigacao

    static constraints = {
        data nullable: false
        cpf nullable: false
        telefoneOrigem nullable: true
        statusLigacao nullable: true
    }
}
