package sobreavisonutel.chamados

class Setores {

    String sigla
    static hasMany = [chamados: Chamado]

    static constraints = {
        sigla nullable: false, inList: ["NUTEL","NUSEG","NURAM","SAÚDE","NUAP","SRH","NMP","NUCOM","NUDOC","CADA","CORREGEDORIA","NUAI","SAF","NUOFI","SPF","GABINETE","NUAT","NUPAT","CIOP","NOE","1ªDEL","2ªDEL","3ªDEL","4ªDEL","POSTO SÃO SOJÉ MIPIBÚ","POSTO MACAÍBA","POSTO LAJES","POSTO CAMPO REDONDO","POSTO CAICÓ","POSTO MOSSORÓ 1","POSTO MOSSORÓ 2","POSTO SÃO GONÇALO DO AMARANTE"]
        chamados nullable: true
    }


    @Override
    public String toString() {
        return sigla
    }
}
