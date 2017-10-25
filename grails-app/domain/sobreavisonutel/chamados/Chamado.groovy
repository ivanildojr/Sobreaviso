package sobreavisonutel.chamados


class Chamado {

    static hasOne = [setor: Setores]
    String descricao
    String horaAbertura
    String dataAbertura
    String status
    long ordemAtendimento
    String usuarioAbertura
    String ip
    int ordem


    static constraints = {
        setor nullable: false
        descricao nullable: false
        horaAbertura nullable: false
        dataAbertura nullable: false
        status nullable: false, inList: ["aberto","executando","aguardando","prioridade"]
        ordemAtendimento nullable: false
        usuarioAbertura nullable: false
        ip nullable: true
        ordem nullable: false
    }

    static mapping = {
        sort  status:"desc",ordem:"asc"
    }

    @Override
    public String toString() {
        return "Chamado{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", horaAbertura='" + horaAbertura + '\'' +
                ", dataAbertura='" + dataAbertura + '\'' +
                ", status='" + status + '\'' +
                ", ordemAtendimento='" + ordemAtendimento + '\'' +
                ", usuarioAbertura='" + usuarioAbertura + '\'' +
                '}';
    }
}
