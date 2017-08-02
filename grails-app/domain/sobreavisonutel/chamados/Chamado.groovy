package sobreavisonutel.chamados

class Chamado {

    String setor
    String descricao
    String horaAbertura
    String dataAbertura
    String status
    String ordemAtendimento
    String usuarioAbertura


    static constraints = {
        setor nullable: false
        descricao nullable: false
        horaAbertura nullable: false
        dataAbertura nullable: false
        status nullable: false
        ordemAtendimento nullable: false
        usuarioAbertura nullable: false
    }
}
