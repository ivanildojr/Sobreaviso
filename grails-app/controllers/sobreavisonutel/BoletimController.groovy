package sobreavisonutel

class BoletimController {

    def index() {

        println params.list()  //imprime tudo que foi retornado do formulario da view

        def atendente = params.list("atendente").get(0)  //recebe atendentes e dataInicio da view e tira da list
        def stringDataInicio = params.list("dataInicio").get(0)
        String mesAno = stringDataInicio.drop(3)
        println "mesAno: " + mesAno
        Date dataInicio = Date.parse("dd/MM/yyyy", stringDataInicio)           //passa a string datainicio pro formato de data, depois coloca na formatacao do banco

        Calendar calend = Calendar.getInstance();
        calend.setTime(dataInicio);
        Integer ultimoDia = calend.getActualMaximum(calend.DAY_OF_MONTH);
        String stringDataFim = ultimoDia.toString() + "/" + mesAno

        println "dataInicio: " + stringDataInicio
        println "stringDataFim:  " + stringDataFim
        println "atendente: $atendente"
//        println "Parse: " + Date.parse("dd/MM/yyyy", dataInicio)
//        println "Format: " + Date.parse("dd/MM/yyyy", dataInicio).format("yyyy-MM-dd")
//        Date dataInicio = Date.parse("dd/MM/yyyy", stringDataInicio)           //passa a string datainicio pro formato de data, depois coloca na formatacao do banco
        Date dataFim = Date.parse("dd/MM/yyyy", stringDataFim)
        println "dataFim: " + dataFim
        Date dataIni = dataInicio

        def atendenteId = Atendentes.findByNome(atendente)
        atendenteId = atendenteId.id
        def atendenteNomeCompleto = sobreavisonutel.seguranca.Usuario.executeQuery("select nome from Usuario where nome LIKE '%$atendente%'").get(0)
//        println "atendenteNomeCompleto: " + atendenteNomeCompleto
        List listBusca = []
        def busca
        def stringDataInicioFixa = dataInicio.format("yyyy-MM-dd").toString()

        while(dataFim >= dataInicio) {
            stringDataInicio = dataInicio.format("yyyy-MM-dd").toString()
//            println "stringDataInicio: " + stringDataInicio

            def dataInicial = Date.parse('yyyy-MM-dd', stringDataInicio)

            Calendar cal = Calendar.getInstance();
            cal.setTime(dataInicial)
            cal.setFirstDayOfWeek(Calendar.SUNDAY);
            int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - diaSemana)
            dataInicial = cal.getTime()

            cal.add(Calendar.DAY_OF_MONTH, 6)

            def dataFinal = cal.getTime()
//            println "dataInicial: " + dataInicial.format("yyyy-MM-dd")
//            println "dataFinal: " + dataFinal.format("yyyy-MM-dd")

            def dataMaisRecente = Historico.executeQuery("select max(dataModificacao) from Historico where dataEscala between :data1 and :data2",
                    [data1: dataInicial, data2: dataFinal]).get(0)

            def dataMaisRecenteString = ''
            if (dataMaisRecente != null) {
                dataMaisRecenteString = dataMaisRecente.format("yyyy-MM-dd HH:mm:ss")
//                println "dataMaisRecente: " + dataMaisRecenteString
            }
            busca = Historico.executeQuery("select dataEscala, hora from Historico where dataEscala='$stringDataInicio' and atendentes_id='$atendenteId' and dataModificacao>='$dataMaisRecenteString' ) order by dataEscala")
//            println "busca: " + busca

            if(busca!=[]) listBusca << busca
//            println "dataInico: " + dataInicio
            dataInicio = dataInicio.plus(1)
        }

    }
}
