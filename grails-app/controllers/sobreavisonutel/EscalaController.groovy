package sobreavisonutel

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import sobreavisonutel.seguranca.Usuario

import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class EscalaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def springSecurityService

    def chatBot(){

        def usuario = "Ivanildo"
        render(view:'chatbot',model:[chatUser:usuario])
    }


    def sobreavisoHistorico(){


        def sobreavisoNutel = params.getProperty("checkList")//params.getList()
        println "Domingo: " + params.getProperty("domingo")
        println "Marcado: " + params.getProperty("checkList")
        println "Sabado: " + params.getProperty("sabado")
        println "Sobreaviso marcações: " + sobreavisoNutel



        /*Avaliar necessidade de limpar para o Historico, para Escala ainda persiste*/
        List limpaEscala = Historico.findAllByDataEscalaBetween(Date.parse("dd/MM/yyyy", params.getProperty("domingo")),Date.parse("dd/MM/yyyy", params.getProperty("sabado")))

        /*limpaEscala.each{
            Historico historico = it
             historico.atendentes = null
             historico.save flush:true
        }*/


        Date dataModificacao = new Date()

        sobreavisoNutel.each{ s->

            switch (s.toString().charAt(0)/*Atendente*/){
                case 'I':/*Ivanildo*/
                    Atendentes atendente = Atendentes.findByNome("Ivanildo")
                    String dia = s.toString().charAt(2)/*Dia*/
                    String hora = s.toString().substring(4,6).toInteger()/*Hora*/
                    Date dataDia
                    dataDia = dataByDia(dia, dataDia)

                    Historico historico = new Historico()

                    historico.dia = dia
                    historico.hora = hora
                    historico.dataEscala = dataDia
                    historico.dataModificacao = dataModificacao
                    historico.atendentes = atendente
                    historico.login = springSecurityService.currentUser
                    historico.save flush:true

                    break;
                case 'T':/*Torres*/
                    Atendentes atendente = Atendentes.findByNome("Torres")
                    String dia = s.toString().charAt(2)/*Dia*/
                    String hora = s.toString().substring(4,6).toInteger()/*Hora*/
                    Date dataDia
                    dataDia = dataByDia(dia, dataDia)


                    Historico historico = new Historico()

                    historico.dia = dia
                    historico.hora = hora
                    historico.dataEscala = dataDia
                    historico.dataModificacao = dataModificacao
                    historico.atendentes = atendente
                    historico.login = springSecurityService.currentUser
                    historico.save flush:true

                    break;
                case 'R':/*Rudsom*/
                    Atendentes atendente = Atendentes.findByNome("Rudsom")
                    String dia = s.toString().charAt(2)/*Dia*/
                    String hora = s.toString().substring(4,6).toInteger()/*Hora*/
                    Date dataDia
                    dataDia = dataByDia(dia, dataDia)

                    Historico historico = new Historico()

                    historico.dia = dia
                    historico.hora = hora
                    historico.dataEscala = dataDia
                    historico.dataModificacao = dataModificacao
                    historico.atendentes = atendente
                    historico.login = springSecurityService.currentUser
                    historico.save flush:true

                    break;
            }
        }


        String[] diasNum = ["1","2","3","4","5","6","7"]
        String[] horasNum = ["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"]
        /*List escala = Escala.findAll()
        List escalaSobreaviso = new ArrayList()


        String letraInicial = ""
        escala.each{ e->

            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+e.hora)


            }
        }
        render(view:'agenda',model:[diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreaviso,usuarioLogado:springSecurityService.currentUser])*/

        def dataMaisRecente = Historico.executeQuery("select max(dataModificacao) from Historico where dataEscala between :data1 and :data2",
                [data1:Date.parse("dd/MM/yyyy", params.getProperty("domingo")),data2:Date.parse("dd/MM/yyyy", params.getProperty("sabado"))]).get(0)
        List historico = Historico.executeQuery("""
            from Historico where dataModificacao = :dataModificacao and dataEscala between :data1 and :data2 order by dataModificacao desc""",
                [dataModificacao:dataMaisRecente,data1:Date.parse("dd/MM/yyyy", params.getProperty("domingo")),data2:Date.parse("dd/MM/yyyy", params.getProperty("sabado"))])



        List escalaSobreavisoHistorico = new ArrayList()

        String letraInicial = ""
        historico.each{ e->

            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+e.hora)
            }
        }




        render(view:'historico',model:[diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreavisoHistorico])



    }

    private Date dataByDia(String dia, Date dataDia) {
        switch (dia) {
            case '1': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("domingo"))
                break;
            case '2': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("segunda"))
                break;
            case '3': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("terca"))
                break;
            case '4': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("quarta"))
                break;
            case '5': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("quinta"))
                break;
            case '6': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("sexta"))
                break;
            case '7': dataDia = Date.parse("dd/MM/yyyy", params.getProperty("sabado"))
                break;
        }
        dataDia
    }


    def sobreaviso(){


        def sobreavisoNutel = params.list('checkList')

        List limpaEscala = Escala.findAll()

        limpaEscala.each{
            Escala escala = it
            escala.atendentes = null
            escala.save flush:true
        }


        sobreavisoNutel.each{ s->

            switch (s.toString().charAt(0)/*Atendente*/){
                case 'I':/*Ivanildo*/
                    Atendentes atendente = Atendentes.findByNome("Ivanildo")
                    Escala escala = Escala.findByDiaAndHora(s.toString().charAt(2)/*Dia*/,s.toString().substring(4,6).toInteger()/*Hora*/)
                    escala.atendentes = atendente

                    escala.validate()
                    if(!escala.hasErrors()) {
                        escala.save flush:true
                        println "Salvou com sucesso"
                    }
                    else {
                        println escala.errors
                        println "Não salvou"
                    }

//                    escala.save flush:true

                    break;
                case 'T':/*Torres*/
                    Atendentes atendente = Atendentes.findByNome("Torres")
                    Escala escala = Escala.findByDiaAndHora(s.toString().charAt(2)/*Dia*/,s.toString().substring(4,6).toInteger()/*Hora*/)
                    escala.atendentes = atendente
                    escala.save flush:true
                    break;
                case 'R':/*Rudsom*/
                    Atendentes atendente = Atendentes.findByNome("Rudsom")
                    Escala escala = Escala.findByDiaAndHora(s.toString().charAt(2)/*Dia*/,s.toString().substring(4,6).toInteger()/*Hora*/)
                    escala.atendentes = atendente
                    escala.save flush:true
                    break;
            }
        }

        String[] diasNum = ["1","2","3","4","5","6","7"]
        String[] horasNum = ["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"]
        List escala = Escala.findAll()
        List escalaSobreaviso = new ArrayList()


        String letraInicial = ""
        escala.each{ e->

            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+e.hora)


            }
        }
//        String usuario = "IVANILDO DE OLIVEIRA DA SILVA JR"
        render(view:'agenda',model:[diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreaviso/*,usuarioLogado:usuario*/])




    }

    def agenda(){

        String[] diasNum = ["1","2","3","4","5","6","7"]
        String[] horasNum = ["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"]
        List escala = Escala.findAll()
        List escalaSobreaviso = new ArrayList()




        String letraInicial = ""
        escala.each{ e->

            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreaviso.add(letraInicial+"-"+e.dia+"-"+e.hora)
            }
        }
        //println escalaSobreaviso
        render(view:'agenda',model:[diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreaviso])
    }

    def historico(String dtInicial, String dtFinal){

        println "HISTORICO"

        String[] diasNum = ["1","2","3","4","5","6","7"]
        String[] horasNum = ["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"]
        String dataHistorico = params.get('dataHistorico')
        Date dataInicial
        Date dataFinal
        println "dataInicial: " + dataInicial
        if(dataHistorico) {
            dataInicial = Date.parse('dd/MM/yyyy', dataHistorico)

            Calendar cal = Calendar.getInstance();
            cal.setTime(dataInicial)
            cal.setFirstDayOfWeek (Calendar.SUNDAY);
            int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
            cal.add (Calendar.DAY_OF_MONTH, Calendar.SUNDAY - diaSemana)
            dataInicial = cal.getTime()

            cal.add (Calendar.DAY_OF_MONTH, 6)

            dataFinal = cal.getTime()
        }
        else {
            dataInicial = new Date()
            dataFinal = new Date()
        }

        println "Primeiro dia: " + dataInicial + " Data: " + dataHistorico + " Ultimo dia: " + dataFinal
        List historico = Historico.findAllByDataEscalaBetween(dataInicial,dataFinal)
        List escalaSobreavisoHistorico = new ArrayList()

//        println historico


        String letraInicial = ""
        historico.each{ e->

            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+e.hora)
            }
        }

        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");



        render(view:'historico',model:[inicial:df.format(dataInicial),final:dataFinal,diasListNum:diasNum,horasListNum:horasNum,escalaLista:escalaSobreavisoHistorico])

    }

    List feriados = ["01/01/2017","Confraternização Universal"
                    ,"06/01/2017","Santos Reis"
                    ,"27/02/2017","Carnaval"
                    ,"28/02/2017","Carnaval"
                    ,"14/04/2017","Paixão de Cristo"
                    ,"21/04/2017","Tiradentes"
                    ,"01/05/2017","Dia do Trabalho"
                    ,"15/06/2017","Corpus Christi"
                    ,"07/09/2017","Independência do Brasil"
                    ,"03/10/2017","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2017","Nossa Sra Aparecida"
                    ,"02/11/2017","Finados"
                    ,"15/11/2017","Proclamação da República"
                    ,"21/11/2017","Nossa Sra da Apresentação"
                    ,"25/12/2017","Natal"
                    ,"01/01/2018","Confraternização Universal"
                    ,"06/01/2018","Santos Reis"
                    ,"12/02/2018","Carnaval"
                    ,"13/02/2018","Carnaval"
                    ,"30/03/2018","Paixão de Cristo"
                    ,"21/04/2018","Tiradentes"
                    ,"01/05/2018","Dia do Trabalho"
                    ,"31/05/2018","Corpus Christi"
                    ,"07/09/2018","Independência do Brasil"
                    ,"03/10/2018","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2018","Nossa Sr.a Aparecida"
                    ,"02/11/2018","Finados"
                    ,"15/11/2018","Proclamação da República"
                    ,"21/11/2018","Nossa Sra da Apresentação"
                    ,"25/12/2018","Natal"
                    ,"01/01/2019","Confraternização Universal"
                    ,"06/01/2019","Santos Reis"
                    ,"04/03/2019","Carnaval"
                    ,"05/03/2019","Carnaval"
                    ,"19/04/2019","Paixão de Cristo"
                    ,"21/04/2019","Tiradentes"
                    ,"01/05/2019","Dia do Trabalho"
                    ,"20/06/2019","Corpus Christi"
                    ,"07/09/2019","Independência do Brasil"
                    ,"03/10/2019","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2019","Nossa Sr.a Aparecida"
                    ,"02/11/2019","Finados"
                    ,"15/11/2019","Proclamação da República"
                    ,"21/11/2019","Nossa Sra da Apresentação"
                    ,"25/12/2019","Natal"
                    ,"01/01/2020","Confraternização Universal"
                    ,"06/01/2020","Santos Reis"
                    ,"24/02/2020","Carnaval"
                    ,"25/02/2020","Carnaval"
                    ,"10/04/2020","Paixão de Cristo"
                    ,"21/04/2020","Tiradentes"
                    ,"01/05/2020","Dia do Trabalho"
                    ,"11/06/2020","Corpus Christi"
                    ,"07/09/2020","Independência do Brasil"
                    ,"03/10/2020","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2020","Nossa Sr.a Aparecida"
                    ,"02/11/2020","Finados"
                    ,"15/11/2020","Proclamação da República"
                    ,"21/11/2020","Nossa Sra da Apresentação"
                    ,"25/12/2020","Natal"
                    ,"01/01/2021","Confraternização Universal"
                    ,"06/01/2021","Santos Reis"
                    ,"15/02/2021","Carnaval"
                    ,"16/02/2021","Carnaval"
                    ,"02/04/2021","Paixão de Cristo"
                    ,"21/04/2021","Tiradentes"
                    ,"01/05/2021","Dia do Trabalho"
                    ,"03/06/2021","Corpus Christi"
                    ,"07/09/2021","Independência do Brasil"
                    ,"03/10/2021","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2021","Nossa Sr.a Aparecida"
                    ,"02/11/2021","Finados"
                    ,"15/11/2021","Proclamação da República"
                    ,"21/11/2021","Nossa Sra da Apresentação"
                    ,"25/12/2021","Natal"
                    ,"01/01/2022","Confraternização Universal"
                    ,"06/01/2022","Santos Reis"
                    ,"28/02/2022","Carnaval"
                    ,"01/03/2022","Carnaval"
                    ,"15/04/2022","Paixão de Cristo"
                    ,"21/04/2022","Tiradentes"
                    ,"01/05/2022","Dia do Trabalho"
                    ,"16/06/2022","Corpus Christi"
                    ,"07/09/2022","Independência do Brasil"
                    ,"03/10/2022","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2022","Nossa Sr.a Aparecida"
                    ,"02/11/2022","Finados"
                    ,"15/11/2022","Proclamação da República"
                    ,"21/11/2022","Nossa Sra da Apresentação"
                    ,"25/12/2022","Natal"
                    ,"01/01/2023","Confraternização Universal"
                    ,"06/01/2023","Santos Reis"
                    ,"20/02/2023","Carnaval"
                    ,"21/02/2023","Carnaval"
                    ,"07/04/2023","Paixão de Cristo"
                    ,"21/04/2023","Tiradentes"
                    ,"01/05/2023","Dia do Trabalho"
                    ,"08/06/2023","Corpus Christi"
                    ,"07/09/2023","Independência do Brasil"
                    ,"03/10/2023","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2023","Nossa Sr.a Aparecida"
                    ,"02/11/2023","Finados"
                    ,"15/11/2023","Proclamação da República"
                    ,"21/11/2023","Nossa Sra da Apresentação"
                    ,"25/12/2023","Natal"
                    ,"01/01/2024","Confraternização Universal"
                    ,"06/01/2024","Santos Reis"
                    ,"12/02/2024","Carnaval"
                    ,"13/02/2024","Carnaval"
                    ,"29/03/2024","Paixão de Cristo"
                    ,"21/04/2024","Tiradentes"
                    ,"01/05/2024","Dia do Trabalho"
                    ,"30/05/2024","Corpus Christi"
                    ,"07/09/2024","Independência do Brasil"
                    ,"03/10/2024","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2024","Nossa Sr.a Aparecida"
                    ,"02/11/2024","Finados"
                    ,"15/11/2024","Proclamação da República"
                    ,"21/11/2024","Nossa Sra da Apresentação"
                    ,"25/12/2024","Natal"
                    ,"01/01/2025","Confraternização Universal"
                    ,"06/01/2025","Santos Reis"
                    ,"03/03/2025","Carnaval"
                    ,"04/03/2025","Carnaval"
                    ,"18/04/2025","Paixão de Cristo"
                    ,"21/04/2025","Tiradentes"
                    ,"01/05/2025","Dia do Trabalho"
                    ,"19/06/2025","Corpus Christi"
                    ,"07/09/2025","Independência do Brasil"
                    ,"03/10/2025","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2025","Nossa Sr.a Aparecida"
                    ,"02/11/2025","Finados"
                    ,"15/11/2025","Proclamação da República"
                    ,"21/11/2025","Nossa Sra da Apresentação"
                    ,"25/12/2025","Natal"
                    ,"01/01/2026","Confraternização Universal"
                    ,"06/01/2026","Santos Reis"
                    ,"16/02/2026","Carnaval"
                    ,"17/02/2026","Carnaval"
                    ,"03/04/2026","Paixão de Cristo"
                    ,"21/04/2026","Tiradentes"
                    ,"01/05/2026","Dia do Trabalho"
                    ,"04/06/2026","Corpus Christi"
                    ,"07/09/2026","Independência do Brasil"
                    ,"03/10/2026","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2026","Nossa Sr.a Aparecida"
                    ,"02/11/2026","Finados"
                    ,"15/11/2026","Proclamação da República"
                    ,"21/11/2026","Nossa Sra da Apresentação"
                    ,"25/12/2026","Natal"
                    ,"01/01/2027","Confraternização Universal"
                    ,"06/01/2027","Santos Reis"
                    ,"08/02/2027","Carnaval"
                    ,"09/02/2027","Carnaval"
                    ,"26/03/2027","Paixão de Cristo"
                    ,"21/04/2027","Tiradentes"
                    ,"01/05/2027","Dia do Trabalho"
                    ,"27/05/2027","Corpus Christi"
                    ,"07/09/2027","Independência do Brasil"
                    ,"03/10/2027","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2027","Nossa Sr.a Aparecida"
                    ,"02/11/2027","Finados"
                    ,"15/11/2027","Proclamação da República"
                    ,"21/11/2027","Nossa Sra da Apresentação"
                    ,"25/12/2027","Natal"
                    ,"01/01/2028","Confraternização Universal"
                    ,"06/01/2028","Santos Reis"
                    ,"28/02/2028","Carnaval"
                    ,"29/02/2028","Carnaval"
                    ,"14/04/2028","Paixão de Cristo"
                    ,"21/04/2028","Tiradentes"
                    ,"01/05/2028","Dia do Trabalho"
                    ,"15/06/2028","Corpus Christi"
                    ,"07/09/2028","Independência do Brasil"
                    ,"03/10/2028","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2028","Nossa Sr.a Aparecida"
                    ,"02/11/2028","Finados"
                    ,"15/11/2028","Proclamação da República"
                    ,"21/11/2028","Nossa Sra da Apresentação"
                    ,"25/12/2028","Natal"
                    ,"01/01/2029","Confraternização Universal"
                    ,"06/01/2029","Santos Reis"
                    ,"12/02/2029","Carnaval"
                    ,"13/02/2029","Carnaval"
                    ,"30/03/2029","Paixão de Cristo"
                    ,"21/04/2029","Tiradentes"
                    ,"01/05/2029","Dia do Trabalho"
                    ,"31/05/2029","Corpus Christi"
                    ,"07/09/2029","Independência do Brasil"
                    ,"03/10/2029","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2029","Nossa Sr.a Aparecida"
                    ,"02/11/2029","Finados"
                    ,"15/11/2029","Proclamação da República"
                    ,"21/11/2029","Nossa Sra da Apresentação"
                    ,"25/12/2029","Natal"
                    ,"01/01/2030","Confraternização Universal"
                    ,"06/01/2030","Santos Reis"
                    ,"04/03/2030","Carnaval"
                    ,"05/03/2030","Carnaval"
                    ,"19/04/2030","Paixão de Cristo"
                    ,"21/04/2030","Tiradentes"
                    ,"01/05/2030","Dia do Trabalho"
                    ,"20/06/2030","Corpus Christi"
                    ,"07/09/2030","Independência do Brasil"
                    ,"03/10/2030","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2030","Nossa Sr.a Aparecida"
                    ,"02/11/2030","Finados"
                    ,"15/11/2030","Proclamação da República"
                    ,"21/11/2030","Nossa Sra da Apresentação"
                    ,"25/12/2030","Natal"
                    ,"01/01/2031","Confraternização Universal"
                    ,"06/01/2031","Santos Reis"
                    ,"24/02/2031","Carnaval"
                    ,"25/02/2031","Carnaval"
                    ,"11/04/2031","Paixão de Cristo"
                    ,"21/04/2031","Tiradentes"
                    ,"01/05/2031","Dia do Trabalho"
                    ,"12/06/2031","Corpus Christi"
                    ,"07/09/2031","Independência do Brasil"
                    ,"03/10/2031","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2031","Nossa Sr.a Aparecida"
                    ,"02/11/2031","Finados"
                    ,"15/11/2031","Proclamação da República"
                    ,"21/11/2031","Nossa Sra da Apresentação"
                    ,"25/12/2031","Natal"
                    ,"01/01/2032","Confraternização Universal"
                    ,"06/01/2032","Santos Reis"
                    ,"09/02/2032","Carnaval"
                    ,"10/02/2032","Carnaval"
                    ,"26/03/2032","Paixão de Cristo"
                    ,"21/04/2032","Tiradentes"
                    ,"01/05/2032","Dia do Trabalho"
                    ,"27/05/2032","Corpus Christi"
                    ,"07/09/2032","Independência do Brasil"
                    ,"03/10/2032","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2032","Nossa Sr.a Aparecida"
                    ,"02/11/2032","Finados"
                    ,"15/11/2032","Proclamação da República"
                    ,"21/11/2032","Nossa Sra da Apresentação"
                    ,"25/12/2032","Natal"
                    ,"01/01/2033","Confraternização Universal"
                    ,"06/01/2033","Santos Reis"
                    ,"28/02/2033","Carnaval"
                    ,"01/03/2033","Carnaval"
                    ,"15/04/2033","Paixão de Cristo"
                    ,"21/04/2033","Tiradentes"
                    ,"01/05/2033","Dia do Trabalho"
                    ,"16/06/2033","Corpus Christi"
                    ,"07/09/2033","Independência do Brasil"
                    ,"03/10/2033","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2033","Nossa Sr.a Aparecida"
                    ,"02/11/2033","Finados"
                    ,"15/11/2033","Proclamação da República"
                    ,"21/11/2033","Nossa Sra da Apresentação"
                    ,"25/12/2033","Natal"
                    ,"01/01/2034","Confraternização Universal"
                    ,"06/01/2034","Santos Reis"
                    ,"20/02/2034","Carnaval"
                    ,"21/02/2034","Carnaval"
                    ,"07/04/2034","Paixão de Cristo"
                    ,"21/04/2034","Tiradentes"
                    ,"01/05/2034","Dia do Trabalho"
                    ,"08/06/2034","Corpus Christi"
                    ,"07/09/2034","Independência do Brasil"
                    ,"03/10/2034","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2034","Nossa Sr.a Aparecida"
                    ,"02/11/2034","Finados"
                    ,"15/11/2034","Proclamação da República"
                    ,"21/11/2034","Nossa Sra da Apresentação"
                    ,"25/12/2034","Natal"
                    ,"01/01/2035","Confraternização Universal"
                    ,"06/01/2035","Santos Reis"
                    ,"05/02/2035","Carnaval"
                    ,"06/02/2035","Carnaval"
                    ,"23/03/2035","Paixão de Cristo"
                    ,"21/04/2035","Tiradentes"
                    ,"01/05/2035","Dia do Trabalho"
                    ,"24/05/2035","Corpus Christi"
                    ,"07/09/2035","Independência do Brasil"
                    ,"03/10/2035","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2035","Nossa Sr.a Aparecida"
                    ,"02/11/2035","Finados"
                    ,"15/11/2035","Proclamação da República"
                    ,"21/11/2035","Nossa Sra da Apresentação"
                    ,"25/12/2035","Natal"
                    ,"01/01/2036","Confraternização Universal"
                    ,"06/01/2036","Santos Reis"
                    ,"25/02/2036","Carnaval"
                    ,"26/02/2036","Carnaval"
                    ,"11/04/2036","Paixão de Cristo"
                    ,"21/04/2036","Tiradentes"
                    ,"01/05/2036","Dia do Trabalho"
                    ,"12/06/2036","Corpus Christi"
                    ,"07/09/2036","Independência do Brasil"
                    ,"03/10/2036","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2036","Nossa Sr.a Aparecida"
                    ,"02/11/2036","Finados"
                    ,"15/11/2036","Proclamação da República"
                    ,"21/11/2036","Nossa Sra da Apresentação"
                    ,"25/12/2036","Natal"
                    ,"01/01/2037","Confraternização Universal"
                    ,"06/01/2037","Santos Reis"
                    ,"16/02/2037","Carnaval"
                    ,"17/02/2037","Carnaval"
                    ,"03/04/2037","Paixão de Cristo"
                    ,"21/04/2037","Tiradentes"
                    ,"01/05/2037","Dia do Trabalho"
                    ,"04/06/2037","Corpus Christi"
                    ,"07/09/2037","Independência do Brasil"
                    ,"03/10/2037","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2037","Nossa Sr.a Aparecida"
                    ,"02/11/2037","Finados"
                    ,"15/11/2037","Proclamação da República"
                    ,"21/11/2037","Nossa Sra da Apresentação"
                    ,"25/12/2037","Natal"
                    ,"01/01/2038","Confraternização Universal"
                    ,"06/01/2038","Santos Reis"
                    ,"08/03/2038","Carnaval"
                    ,"09/03/2038","Carnaval"
                    ,"21/04/2038","Tiradentes"
                    ,"23/04/2038","Paixão de Cristo"
                    ,"01/05/2038","Dia do Trabalho"
                    ,"24/06/2038","Corpus Christi"
                    ,"07/09/2038","Independência do Brasil"
                    ,"03/10/2038","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2038","Nossa Sr.a Aparecida"
                    ,"02/11/2038","Finados"
                    ,"15/11/2038","Proclamação da República"
                    ,"21/11/2038","Nossa Sra da Apresentação"
                    ,"25/12/2038","Natal"
                    ,"01/01/2039","Confraternização Universal"
                    ,"06/01/2039","Santos Reis"
                    ,"21/02/2039","Carnaval"
                    ,"22/02/2039","Carnaval"
                    ,"08/04/2039","Paixão de Cristo"
                    ,"21/04/2039","Tiradentes"
                    ,"01/05/2039","Dia do Trabalho"
                    ,"09/06/2039","Corpus Christi"
                    ,"07/09/2039","Independência do Brasil"
                    ,"03/10/2039","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2039","Nossa Sr.a Aparecida"
                    ,"02/11/2039","Finados"
                    ,"15/11/2039","Proclamação da República"
                    ,"21/11/2039","Nossa Sra da Apresentação"
                    ,"25/12/2039","Natal"
                    ,"01/01/2040","Confraternização Universal"
                    ,"06/01/2040","Santos Reis"
                    ,"13/02/2040","Carnaval"
                    ,"14/02/2040","Carnaval"
                    ,"30/03/2040","Paixão de Cristo"
                    ,"21/04/2040","Tiradentes"
                    ,"01/05/2040","Dia do Trabalho"
                    ,"31/05/2040","Corpus Christi"
                    ,"07/09/2040","Independência do Brasil"
                    ,"03/10/2040","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2040","Nossa Sr.a Aparecida"
                    ,"02/11/2040","Finados"
                    ,"15/11/2040","Proclamação da República"
                    ,"21/11/2040","Nossa Sra da Apresentação"
                    ,"25/12/2040","Natal"
                    ,"01/01/2041","Confraternização Universal"
                    ,"06/01/2041","Santos Reis"
                    ,"04/03/2041","Carnaval"
                    ,"05/03/2041","Carnaval"
                    ,"19/04/2041","Paixão de Cristo"
                    ,"21/04/2041","Tiradentes"
                    ,"01/05/2041","Dia do Trabalho"
                    ,"20/06/2041","Corpus Christi"
                    ,"07/09/2041","Independência do Brasil"
                    ,"03/10/2041","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2041","Nossa Sr.a Aparecida"
                    ,"02/11/2041","Finados"
                    ,"15/11/2041","Proclamação da República"
                    ,"21/11/2041","Nossa Sra da Apresentação"
                    ,"25/12/2041","Natal"
                    ,"01/01/2042","Confraternização Universal"
                    ,"06/01/2042","Santos Reis"
                    ,"17/02/2042","Carnaval"
                    ,"18/02/2042","Carnaval"
                    ,"04/04/2042","Paixão de Cristo"
                    ,"21/04/2042","Tiradentes"
                    ,"01/05/2042","Dia do Trabalho"
                    ,"05/06/2042","Corpus Christi"
                    ,"07/09/2042","Independência do Brasil"
                    ,"03/10/2042","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2042","Nossa Sr.a Aparecida"
                    ,"02/11/2042","Finados"
                    ,"15/11/2042","Proclamação da República"
                    ,"21/11/2042","Nossa Sra da Apresentação"
                    ,"25/12/2042","Natal"
                    ,"01/01/2043","Confraternização Universal"
                    ,"06/01/2043","Santos Reis"
                    ,"09/02/2043","Carnaval"
                    ,"10/02/2043","Carnaval"
                    ,"27/03/2043","Paixão de Cristo"
                    ,"21/04/2043","Tiradentes"
                    ,"01/05/2043","Dia do Trabalho"
                    ,"28/05/2043","Corpus Christi"
                    ,"07/09/2043","Independência do Brasil"
                    ,"03/10/2043","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2043","Nossa Sr.a Aparecida"
                    ,"02/11/2043","Finados"
                    ,"15/11/2043","Proclamação da República"
                    ,"21/11/2043","Nossa Sra da Apresentação"
                    ,"25/12/2043","Natal"
                    ,"01/01/2044","Confraternização Universal"
                    ,"06/01/2044","Santos Reis"
                    ,"29/02/2044","Carnaval"
                    ,"01/03/2044","Carnaval"
                    ,"15/04/2044","Paixão de Cristo"
                    ,"21/04/2044","Tiradentes"
                    ,"01/05/2044","Dia do Trabalho"
                    ,"16/06/2044","Corpus Christi"
                    ,"07/09/2044","Independência do Brasil"
                    ,"03/10/2044","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2044","Nossa Sr.a Aparecida"
                    ,"02/11/2044","Finados"
                    ,"15/11/2044","Proclamação da República"
                    ,"21/11/2044","Nossa Sra da Apresentação"
                    ,"25/12/2044","Natal"
                    ,"01/01/2045","Confraternização Universal"
                    ,"06/01/2045","Santos Reis"
                    ,"20/02/2045","Carnaval"
                    ,"21/02/2045","Carnaval"
                    ,"07/04/2045","Paixão de Cristo"
                    ,"21/04/2045","Tiradentes"
                    ,"01/05/2045","Dia do Trabalho"
                    ,"08/06/2045","Corpus Christi"
                    ,"07/09/2045","Independência do Brasil"
                    ,"03/10/2045","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2045","Nossa Sr.a Aparecida"
                    ,"02/11/2045","Finados"
                    ,"15/11/2045","Proclamação da República"
                    ,"21/11/2045","Nossa Sra da Apresentação"
                    ,"25/12/2045","Natal"
                    ,"01/01/2046","Confraternização Universal"
                    ,"06/01/2046","Santos Reis"
                    ,"05/02/2046","Carnaval"
                    ,"06/02/2046","Carnaval"
                    ,"23/03/2046","Paixão de Cristo"
                    ,"21/04/2046","Tiradentes"
                    ,"01/05/2046","Dia do Trabalho"
                    ,"24/05/2046","Corpus Christi"
                    ,"07/09/2046","Independência do Brasil"
                    ,"03/10/2046","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2046","Nossa Sr.a Aparecida"
                    ,"02/11/2046","Finados"
                    ,"15/11/2046","Proclamação da República"
                    ,"21/11/2046","Nossa Sra da Apresentação"
                    ,"25/12/2046","Natal"
                    ,"01/01/2047","Confraternização Universal"
                    ,"06/01/2047","Santos Reis"
                    ,"25/02/2047","Carnaval"
                    ,"26/02/2047","Carnaval"
                    ,"12/04/2047","Paixão de Cristo"
                    ,"21/04/2047","Tiradentes"
                    ,"01/05/2047","Dia do Trabalho"
                    ,"13/06/2047","Corpus Christi"
                    ,"07/09/2047","Independência do Brasil"
                    ,"03/10/2047","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2047","Nossa Sr.a Aparecida"
                    ,"02/11/2047","Finados"
                    ,"15/11/2047","Proclamação da República"
                    ,"21/11/2047","Nossa Sra da Apresentação"
                    ,"25/12/2047","Natal"
                    ,"01/01/2048","Confraternização Universal"
                    ,"06/01/2048","Santos Reis"
                    ,"17/02/2048","Carnaval"
                    ,"18/02/2048","Carnaval"
                    ,"03/04/2048","Paixão de Cristo"
                    ,"21/04/2048","Tiradentes"
                    ,"01/05/2048","Dia do Trabalho"
                    ,"04/06/2048","Corpus Christi"
                    ,"07/09/2048","Independência do Brasil"
                    ,"03/10/2048","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2048","Nossa Sr.a Aparecida"
                    ,"02/11/2048","Finados"
                    ,"15/11/2048","Proclamação da República"
                    ,"21/11/2048","Nossa Sra da Apresentação"
                    ,"25/12/2048","Natal"
                    ,"01/01/2049","Confraternização Universal"
                    ,"06/01/2049","Santos Reis"
                    ,"01/03/2049","Carnaval"
                    ,"02/03/2049","Carnaval"
                    ,"16/04/2049","Paixão de Cristo"
                    ,"21/04/2049","Tiradentes"
                    ,"01/05/2049","Dia do Trabalho"
                    ,"17/06/2049","Corpus Christi"
                    ,"07/09/2049","Independência do Brasil"
                    ,"03/10/2049","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2049","Nossa Sr.a Aparecida"
                    ,"02/11/2049","Finados"
                    ,"15/11/2049","Proclamação da República"
                    ,"21/11/2049","Nossa Sra da Apresentação"
                    ,"25/12/2049","Natal"
                    ,"01/01/2050","Confraternização Universal"
                    ,"06/01/2050","Santos Reis"
                    ,"21/02/2050","Carnaval"
                    ,"22/02/2050","Carnaval"
                    ,"08/04/2050","Paixão de Cristo"
                    ,"21/04/2050","Tiradentes"
                    ,"01/05/2050","Dia do Trabalho"
                    ,"09/06/2050","Corpus Christi"
                    ,"07/09/2050","Independência do Brasil"
                    ,"03/10/2050","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2050","Nossa Sr.a Aparecida"
                    ,"02/11/2050","Finados"
                    ,"15/11/2050","Proclamação da República"
                    ,"21/11/2050","Nossa Sra da Apresentação"
                    ,"25/12/2050","Natal"
                    ,"01/01/2051","Confraternização Universal"
                    ,"06/01/2051","Santos Reis"
                    ,"13/02/2051","Carnaval"
                    ,"14/02/2051","Carnaval"
                    ,"31/03/2051","Paixão de Cristo"
                    ,"21/04/2051","Tiradentes"
                    ,"01/05/2051","Dia do Trabalho"
                    ,"01/06/2051","Corpus Christi"
                    ,"07/09/2051","Independência do Brasil"
                    ,"03/10/2051","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2051","Nossa Sr.a Aparecida"
                    ,"02/11/2051","Finados"
                    ,"15/11/2051","Proclamação da República"
                    ,"21/11/2051","Nossa Sra da Apresentação"
                    ,"25/12/2051","Natal"
                    ,"01/01/2052","Confraternização Universal"
                    ,"06/01/2052","Santos Reis"
                    ,"04/03/2052","Carnaval"
                    ,"05/03/2052","Carnaval"
                    ,"19/04/2052","Paixão de Cristo"
                    ,"21/04/2052","Tiradentes"
                    ,"01/05/2052","Dia do Trabalho"
                    ,"20/06/2052","Corpus Christi"
                    ,"07/09/2052","Independência do Brasil"
                    ,"03/10/2052","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2052","Nossa Sr.a Aparecida"
                    ,"02/11/2052","Finados"
                    ,"15/11/2052","Proclamação da República"
                    ,"21/11/2052","Nossa Sra da Apresentação"
                    ,"25/12/2052","Natal"
                    ,"01/01/2053","Confraternização Universal"
                    ,"06/01/2053","Santos Reis"
                    ,"17/02/2053","Carnaval"
                    ,"18/02/2053","Carnaval"
                    ,"04/04/2053","Paixão de Cristo"
                    ,"21/04/2053","Tiradentes"
                    ,"01/05/2053","Dia do Trabalho"
                    ,"05/06/2053","Corpus Christi"
                    ,"07/09/2053","Independência do Brasil"
                    ,"03/10/2053","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2053","Nossa Sr.a Aparecida"
                    ,"02/11/2053","Finados"
                    ,"15/11/2053","Proclamação da República"
                    ,"21/11/2053","Nossa Sra da Apresentação"
                    ,"25/12/2053","Natal"
                    ,"01/01/2054","Confraternização Universal"
                    ,"06/01/2054","Santos Reis"
                    ,"09/02/2054","Carnaval"
                    ,"10/02/2054","Carnaval"
                    ,"27/03/2054","Paixão de Cristo"
                    ,"21/04/2054","Tiradentes"
                    ,"01/05/2054","Dia do Trabalho"
                    ,"28/05/2054","Corpus Christi"
                    ,"07/09/2054","Independência do Brasil"
                    ,"03/10/2054","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2054","Nossa Sr.a Aparecida"
                    ,"02/11/2054","Finados"
                    ,"15/11/2054","Proclamação da República"
                    ,"21/11/2054","Nossa Sra da Apresentação"
                    ,"25/12/2054","Natal"
                    ,"01/01/2055","Confraternização Universal"
                    ,"06/01/2055","Santos Reis"
                    ,"01/03/2055","Carnaval"
                    ,"02/03/2055","Carnaval"
                    ,"16/04/2055","Paixão de Cristo"
                    ,"21/04/2055","Tiradentes"
                    ,"01/05/2055","Dia do Trabalho"
                    ,"17/06/2055","Corpus Christi"
                    ,"07/09/2055","Independência do Brasil"
                    ,"03/10/2055","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2055","Nossa Sr.a Aparecida"
                    ,"02/11/2055","Finados"
                    ,"15/11/2055","Proclamação da República"
                    ,"21/11/2055","Nossa Sra da Apresentação"
                    ,"25/12/2055","Natal"
                    ,"01/01/2056","Confraternização Universal"
                    ,"06/01/2056","Santos Reis"
                    ,"14/02/2056","Carnaval"
                    ,"15/02/2056","Carnaval"
                    ,"31/03/2056","Paixão de Cristo"
                    ,"21/04/2056","Tiradentes"
                    ,"01/05/2056","Dia do Trabalho"
                    ,"01/06/2056","Corpus Christi"
                    ,"07/09/2056","Independência do Brasil"
                    ,"03/10/2056","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2056","Nossa Sr.a Aparecida"
                    ,"02/11/2056","Finados"
                    ,"15/11/2056","Proclamação da República"
                    ,"21/11/2056","Nossa Sra da Apresentação"
                    ,"25/12/2056","Natal"
                    ,"01/01/2057","Confraternização Universal"
                    ,"06/01/2057","Santos Reis"
                    ,"05/03/2057","Carnaval"
                    ,"06/03/2057","Carnaval"
                    ,"20/04/2057","Paixão de Cristo"
                    ,"21/04/2057","Tiradentes"
                    ,"01/05/2057","Dia do Trabalho"
                    ,"21/06/2057","Corpus Christi"
                    ,"07/09/2057","Independência do Brasil"
                    ,"03/10/2057","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2057","Nossa Sr.a Aparecida"
                    ,"02/11/2057","Finados"
                    ,"15/11/2057","Proclamação da República"
                    ,"21/11/2057","Nossa Sra da Apresentação"
                    ,"25/12/2057","Natal"
                    ,"01/01/2058","Confraternização Universal"
                    ,"06/01/2058","Santos Reis"
                    ,"25/02/2058","Carnaval"
                    ,"26/02/2058","Carnaval"
                    ,"12/04/2058","Paixão de Cristo"
                    ,"21/04/2058","Tiradentes"
                    ,"01/05/2058","Dia do Trabalho"
                    ,"13/06/2058","Corpus Christi"
                    ,"07/09/2058","Independência do Brasil"
                    ,"03/10/2058","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2058","Nossa Sr.a Aparecida"
                    ,"02/11/2058","Finados"
                    ,"15/11/2058","Proclamação da República"
                    ,"21/11/2058","Nossa Sra da Apresentação"
                    ,"25/12/2058","Natal"
                    ,"01/01/2059","Confraternização Universal"
                    ,"06/01/2059","Santos Reis"
                    ,"10/02/2059","Carnaval"
                    ,"11/02/2059","Carnaval"
                    ,"28/03/2059","Paixão de Cristo"
                    ,"21/04/2059","Tiradentes"
                    ,"01/05/2059","Dia do Trabalho"
                    ,"29/05/2059","Corpus Christi"
                    ,"07/09/2059","Independência do Brasil"
                    ,"03/10/2059","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2059","Nossa Sr.a Aparecida"
                    ,"02/11/2059","Finados"
                    ,"15/11/2059","Proclamação da República"
                    ,"21/11/2059","Nossa Sra da Apresentação"
                    ,"25/12/2059","Natal"
                    ,"01/01/2060","Confraternização Universal"
                    ,"06/01/2060","Santos Reis"
                    ,"01/03/2060","Carnaval"
                    ,"02/03/2060","Carnaval"
                    ,"16/04/2060","Paixão de Cristo"
                    ,"21/04/2060","Tiradentes"
                    ,"01/05/2060","Dia do Trabalho"
                    ,"17/06/2060","Corpus Christi"
                    ,"07/09/2060","Independência do Brasil"
                    ,"03/10/2060","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2060","Nossa Sr.a Aparecida"
                    ,"02/11/2060","Finados"
                    ,"15/11/2060","Proclamação da República"
                    ,"21/11/2060","Nossa Sra da Apresentação"
                    ,"25/12/2060","Natal"
                    ,"01/01/2061","Confraternização Universal"
                    ,"06/01/2061","Santos Reis"
                    ,"21/02/2061","Carnaval"
                    ,"22/02/2061","Carnaval"
                    ,"08/04/2061","Paixão de Cristo"
                    ,"21/04/2061","Tiradentes"
                    ,"01/05/2061","Dia do Trabalho"
                    ,"09/06/2061","Corpus Christi"
                    ,"07/09/2061","Independência do Brasil"
                    ,"03/10/2061","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2061","Nossa Sr.a Aparecida"
                    ,"02/11/2061","Finados"
                    ,"15/11/2061","Proclamação da República"
                    ,"21/11/2061","Nossa Sra da Apresentação"
                    ,"25/12/2061","Natal"
                    ,"01/01/2062","Confraternização Universal"
                    ,"06/01/2062","Santos Reis"
                    ,"06/02/2062","Carnaval"
                    ,"07/02/2062","Carnaval"
                    ,"24/03/2062","Paixão de Cristo"
                    ,"21/04/2062","Tiradentes"
                    ,"01/05/2062","Dia do Trabalho"
                    ,"25/05/2062","Corpus Christi"
                    ,"07/09/2062","Independência do Brasil"
                    ,"03/10/2062","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2062","Nossa Sr.a Aparecida"
                    ,"02/11/2062","Finados"
                    ,"15/11/2062","Proclamação da República"
                    ,"21/11/2062","Nossa Sra da Apresentação"
                    ,"25/12/2062","Natal"
                    ,"01/01/2063","Confraternização Universal"
                    ,"06/01/2063","Santos Reis"
                    ,"26/02/2063","Carnaval"
                    ,"27/02/2063","Carnaval"
                    ,"13/04/2063","Paixão de Cristo"
                    ,"21/04/2063","Tiradentes"
                    ,"01/05/2063","Dia do Trabalho"
                    ,"14/06/2063","Corpus Christi"
                    ,"07/09/2063","Independência do Brasil"
                    ,"03/10/2063","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2063","Nossa Sr.a Aparecida"
                    ,"02/11/2063","Finados"
                    ,"15/11/2063","Proclamação da República"
                    ,"21/11/2063","Nossa Sra da Apresentação"
                    ,"25/12/2063","Natal"
                    ,"01/01/2064","Confraternização Universal"
                    ,"06/01/2064","Santos Reis"
                    ,"18/02/2064","Carnaval"
                    ,"19/02/2064","Carnaval"
                    ,"04/04/2064","Paixão de Cristo"
                    ,"21/04/2064","Tiradentes"
                    ,"01/05/2064","Dia do Trabalho"
                    ,"05/06/2064","Corpus Christi"
                    ,"07/09/2064","Independência do Brasil"
                    ,"03/10/2064","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2064","Nossa Sr.a Aparecida"
                    ,"02/11/2064","Finados"
                    ,"15/11/2064","Proclamação da República"
                    ,"21/11/2064","Nossa Sra da Apresentação"
                    ,"25/12/2064","Natal"
                    ,"01/01/2065","Confraternização Universal"
                    ,"06/01/2065","Santos Reis"
                    ,"09/02/2065","Carnaval"
                    ,"10/02/2065","Carnaval"
                    ,"27/03/2065","Paixão de Cristo"
                    ,"21/04/2065","Tiradentes"
                    ,"01/05/2065","Dia do Trabalho"
                    ,"28/05/2065","Corpus Christi"
                    ,"07/09/2065","Independência do Brasil"
                    ,"03/10/2065","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2065","Nossa Sr.a Aparecida"
                    ,"02/11/2065","Finados"
                    ,"15/11/2065","Proclamação da República"
                    ,"21/11/2065","Nossa Sra da Apresentação"
                    ,"25/12/2065","Natal"
                    ,"01/01/2066","Confraternização Universal"
                    ,"06/01/2066","Santos Reis"
                    ,"22/02/2066","Carnaval"
                    ,"23/02/2066","Carnaval"
                    ,"09/04/2066","Paixão de Cristo"
                    ,"21/04/2066","Tiradentes"
                    ,"01/05/2066","Dia do Trabalho"
                    ,"10/06/2066","Corpus Christi"
                    ,"07/09/2066","Independência do Brasil"
                    ,"03/10/2066","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2066","Nossa Sr.a Aparecida"
                    ,"02/11/2066","Finados"
                    ,"15/11/2066","Proclamação da República"
                    ,"21/11/2066","Nossa Sra da Apresentação"
                    ,"25/12/2066","Natal"
                    ,"01/01/2067","Confraternização Universal"
                    ,"06/01/2067","Santos Reis"
                    ,"14/02/2067","Carnaval"
                    ,"15/02/2067","Carnaval"
                    ,"01/04/2067","Paixão de Cristo"
                    ,"21/04/2067","Tiradentes"
                    ,"01/05/2067","Dia do Trabalho"
                    ,"02/06/2067","Corpus Christi"
                    ,"07/09/2067","Independência do Brasil"
                    ,"03/10/2067","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2067","Nossa Sr.a Aparecida"
                    ,"02/11/2067","Finados"
                    ,"15/11/2067","Proclamação da República"
                    ,"21/11/2067","Nossa Sra da Apresentação"
                    ,"25/12/2067","Natal"
                    ,"01/01/2068","Confraternização Universal"
                    ,"06/01/2068","Santos Reis"
                    ,"05/03/2068","Carnaval"
                    ,"06/03/2068","Carnaval"
                    ,"20/04/2068","Paixão de Cristo"
                    ,"21/04/2068","Tiradentes"
                    ,"01/05/2068","Dia do Trabalho"
                    ,"21/06/2068","Corpus Christi"
                    ,"07/09/2068","Independência do Brasil"
                    ,"03/10/2068","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2068","Nossa Sr.a Aparecida"
                    ,"02/11/2068","Finados"
                    ,"15/11/2068","Proclamação da República"
                    ,"21/11/2068","Nossa Sra da Apresentação"
                    ,"25/12/2068","Natal"
                    ,"01/01/2069","Confraternização Universal"
                    ,"06/01/2069","Santos Reis"
                    ,"25/02/2069","Carnaval"
                    ,"26/02/2069","Carnaval"
                    ,"12/04/2069","Paixão de Cristo"
                    ,"21/04/2069","Tiradentes"
                    ,"01/05/2069","Dia do Trabalho"
                    ,"13/06/2069","Corpus Christi"
                    ,"07/09/2069","Independência do Brasil"
                    ,"03/10/2069","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2069","Nossa Sr.a Aparecida"
                    ,"02/11/2069","Finados"
                    ,"15/11/2069","Proclamação da República"
                    ,"21/11/2069","Nossa Sra da Apresentação"
                    ,"25/12/2069","Natal"
                    ,"01/01/2070","Confraternização Universal"
                    ,"06/01/2070","Santos Reis"
                    ,"10/02/2070","Carnaval"
                    ,"11/02/2070","Carnaval"
                    ,"28/03/2070","Paixão de Cristo"
                    ,"21/04/2070","Tiradentes"
                    ,"01/05/2070","Dia do Trabalho"
                    ,"29/05/2070","Corpus Christi"
                    ,"07/09/2070","Independência do Brasil"
                    ,"03/10/2070","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2070","Nossa Sr.a Aparecida"
                    ,"02/11/2070","Finados"
                    ,"15/11/2070","Proclamação da República"
                    ,"21/11/2070","Nossa Sra da Apresentação"
                    ,"25/12/2070","Natal"
                    ,"01/01/2071","Confraternização Universal"
                    ,"06/01/2071","Santos Reis"
                    ,"02/03/2071","Carnaval"
                    ,"03/03/2071","Carnaval"
                    ,"17/04/2071","Paixão de Cristo"
                    ,"21/04/2071","Tiradentes"
                    ,"01/05/2071","Dia do Trabalho"
                    ,"18/06/2071","Corpus Christi"
                    ,"07/09/2071","Independência do Brasil"
                    ,"03/10/2071","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2071","Nossa Sr.a Aparecida"
                    ,"02/11/2071","Finados"
                    ,"15/11/2071","Proclamação da República"
                    ,"21/11/2071","Nossa Sra da Apresentação"
                    ,"25/12/2071","Natal"
                    ,"01/01/2072","Confraternização Universal"
                    ,"06/01/2072","Santos Reis"
                    ,"22/02/2072","Carnaval"
                    ,"23/02/2072","Carnaval"
                    ,"08/04/2072","Paixão de Cristo"
                    ,"21/04/2072","Tiradentes"
                    ,"01/05/2072","Dia do Trabalho"
                    ,"09/06/2072","Corpus Christi"
                    ,"07/09/2072","Independência do Brasil"
                    ,"03/10/2072","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2072","Nossa Sr.a Aparecida"
                    ,"02/11/2072","Finados"
                    ,"15/11/2072","Proclamação da República"
                    ,"21/11/2072","Nossa Sra da Apresentação"
                    ,"25/12/2072","Natal"
                    ,"01/01/2073","Confraternização Universal"
                    ,"06/01/2073","Santos Reis"
                    ,"06/02/2073","Carnaval"
                    ,"07/02/2073","Carnaval"
                    ,"24/03/2073","Paixão de Cristo"
                    ,"21/04/2073","Tiradentes"
                    ,"01/05/2073","Dia do Trabalho"
                    ,"25/05/2073","Corpus Christi"
                    ,"07/09/2073","Independência do Brasil"
                    ,"03/10/2073","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2073","Nossa Sr.a Aparecida"
                    ,"02/11/2073","Finados"
                    ,"15/11/2073","Proclamação da República"
                    ,"21/11/2073","Nossa Sra da Apresentação"
                    ,"25/12/2073","Natal"
                    ,"01/01/2074","Confraternização Universal"
                    ,"06/01/2074","Santos Reis"
                    ,"26/02/2074","Carnaval"
                    ,"27/02/2074","Carnaval"
                    ,"13/04/2074","Paixão de Cristo"
                    ,"21/04/2074","Tiradentes"
                    ,"01/05/2074","Dia do Trabalho"
                    ,"14/06/2074","Corpus Christi"
                    ,"07/09/2074","Independência do Brasil"
                    ,"03/10/2074","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2074","Nossa Sr.a Aparecida"
                    ,"02/11/2074","Finados"
                    ,"15/11/2074","Proclamação da República"
                    ,"21/11/2074","Nossa Sra da Apresentação"
                    ,"25/12/2074","Natal"
                    ,"01/01/2075","Confraternização Universal"
                    ,"06/01/2075","Santos Reis"
                    ,"18/02/2075","Carnaval"
                    ,"19/02/2075","Carnaval"
                    ,"05/04/2075","Paixão de Cristo"
                    ,"21/04/2075","Tiradentes"
                    ,"01/05/2075","Dia do Trabalho"
                    ,"06/06/2075","Corpus Christi"
                    ,"07/09/2075","Independência do Brasil"
                    ,"03/10/2075","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2075","Nossa Sr.a Aparecida"
                    ,"02/11/2075","Finados"
                    ,"15/11/2075","Proclamação da República"
                    ,"21/11/2075","Nossa Sra da Apresentação"
                    ,"25/12/2075","Natal"
                    ,"01/01/2076","Confraternização Universal"
                    ,"06/01/2076","Santos Reis"
                    ,"02/03/2076","Carnaval"
                    ,"03/03/2076","Carnaval"
                    ,"17/04/2076","Paixão de Cristo"
                    ,"21/04/2076","Tiradentes"
                    ,"01/05/2076","Dia do Trabalho"
                    ,"18/06/2076","Corpus Christi"
                    ,"07/09/2076","Independência do Brasil"
                    ,"03/10/2076","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2076","Nossa Sr.a Aparecida"
                    ,"02/11/2076","Finados"
                    ,"15/11/2076","Proclamação da República"
                    ,"21/11/2076","Nossa Sra da Apresentação"
                    ,"25/12/2076","Natal"
                    ,"01/01/2077","Confraternização Universal"
                    ,"06/01/2077","Santos Reis"
                    ,"22/02/2077","Carnaval"
                    ,"23/02/2077","Carnaval"
                    ,"09/04/2077","Paixão de Cristo"
                    ,"21/04/2077","Tiradentes"
                    ,"01/05/2077","Dia do Trabalho"
                    ,"10/06/2077","Corpus Christi"
                    ,"07/09/2077","Independência do Brasil"
                    ,"03/10/2077","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2077","Nossa Sr.a Aparecida"
                    ,"02/11/2077","Finados"
                    ,"15/11/2077","Proclamação da República"
                    ,"21/11/2077","Nossa Sra da Apresentação"
                    ,"25/12/2077","Natal"
                    ,"01/01/2078","Confraternização Universal"
                    ,"06/01/2078","Santos Reis"
                    ,"14/02/2078","Carnaval"
                    ,"15/02/2078","Carnaval"
                    ,"01/04/2078","Paixão de Cristo"
                    ,"21/04/2078","Tiradentes"
                    ,"01/05/2078","Dia do Trabalho"
                    ,"02/06/2078","Corpus Christi"
                    ,"07/09/2078","Independência do Brasil"
                    ,"03/10/2078","Mártires Uruaçu e Cunhaú"
                    ,"12/10/2078","Nossa Sr.a Aparecida"
                    ,"02/11/2078","Finados"
                    ,"15/11/2078","Proclamação da República"
                    ,"21/11/2078","Nossa Sra da Apresentação"
                    ,"25/12/2078","Natal"
                    ]

    def datasAjax(String dataHistorico){

        println "dataHistorico: " + dataHistorico

        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");

        println "Data da pesquisa passada como parametro: " + dataHistorico

        Date dataInicial
        Date dataFinal
        if(dataHistorico) {
            dataInicial = Date.parse('dd/MM/yyyy', dataHistorico)

            Calendar cal = Calendar.getInstance();
            cal.setTime(dataInicial)
            cal.setFirstDayOfWeek (Calendar.SUNDAY);
            int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
            cal.add (Calendar.DAY_OF_MONTH, Calendar.SUNDAY - diaSemana)
            dataInicial = cal.getTime()

            cal.add (Calendar.DAY_OF_MONTH, 6)

            dataFinal = cal.getTime()
        }
        else {
            dataInicial = new Date()
            dataFinal = new Date()
        }


        println "Data Inicial: " + dataInicial
        println "Data Final: " + dataFinal

        //////////// LOCALIZA FERIADOS
        List listFeriados = []
        Date d = dataInicial
        dataInicial.upto(dataFinal) {
            def dString = d.format("dd/MM/yyyy")
            println "dString: " + dString
            if(feriados.contains(dString))  {
                def feriado = feriados.get(feriados.indexOf(dString)+1)
                listFeriados << feriado
                println "feriado: " + feriado
            }
            else listFeriados << ""
            d = d + 1
        }
        println "listFeriados: " + listFeriados
        ///////////////////////////////

        JsonUtil jsonUtil = new JsonUtil()
        jsonUtil.dataInicial = dataInicial
        jsonUtil.dataFinal = dataFinal
        jsonUtil.feriados = listFeriados
//        jsonUtil.feriado = "01/01/2017"

        /**************Busca os horários na tabela historico**********************/
        String[] diasNum = ["1","2","3","4","5","6","7"]
        String[] horasNum = ["00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"]

        def dataMaisRecente = Historico.executeQuery("select max(dataModificacao) from Historico where dataEscala between :data1 and :data2",
                [data1:dataInicial,data2:dataFinal]).get(0)
        List historico = Historico.executeQuery("""
            from Historico where dataModificacao = :dataModificacao and dataEscala between :data1 and :data2 order by dataModificacao desc""",
                [dataModificacao:dataMaisRecente,data1:dataInicial,data2:dataFinal])



        println "Data mais Recente: " + dataMaisRecente


        List escalaSobreavisoHistorico = new ArrayList()

        String letraInicial = ""
        historico.each{ e->
            if(e.atendentes){
                letraInicial = e.atendentes.nome.charAt(0)
                if(Integer.parseInt(e.hora) < 10)
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+'0'+e.hora)
                else
                    escalaSobreavisoHistorico.add(letraInicial+"-"+e.dia+"-"+e.hora)
            }
        }


        jsonUtil.escalaSobreavisoHistorico = escalaSobreavisoHistorico
//        println Historico.executeQuery("from Historico where ")
        println historico
        /*************************************/

        render jsonUtil as JSON
    }


    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Escala.list(params), model:[escalaCount: Escala.count()]
    }

    def show(Escala escala) {
        println "show!"
        respond escala
    }

    def create() {
        println "create!"
        respond new Escala(params)
    }

    @Transactional
    def save(Escala escala) {
        if (escala == null) {
            println "não houve erro no save!"
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (escala.hasErrors()) {
            println "houve erro no save!"
            transactionStatus.setRollbackOnly()
            respond escala.errors, view:'create'
            return
        }

        escala.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'escala.label', default: 'Escala'), escala.id])
                redirect escala
            }
            '*' { respond escala, [status: CREATED] }
        }
    }

    def edit(Escala escala) {
        println "edit escala!"
        respond escala
    }

    @Transactional
    def update(Escala escala) {

        if (escala == null) {
            println "não houve erro no update!"
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (escala.hasErrors()) {
            println "houve erro no update!"
            transactionStatus.setRollbackOnly()
            respond escala.errors, view:'edit'
            return
        }

        escala.validate()
        if(!escala.hasErrors()) {
            escala.save flush:true
            println "Salvou com sucesso"
        }
        else {
            println escala.errors
            println "Não salvou"
        }


//        escala.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'escala.label', default: 'Escala'), escala.id])
                redirect escala
            }
            '*'{ respond escala, [status: OK] }
        }
    }

    @Transactional
    def delete(Escala escala) {

        if (escala == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        escala.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'escala.label', default: 'Escala'), escala.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'escala.label', default: 'Escala'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
