package sobreavisonutel

import grails.transaction.Transactional


import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Time
import java.time.LocalDate

@Transactional
class TopPontoRepService {


    /*
    * Pega os Abonos no banco do RH
    * */
    def pegaAbonos(String servidor){
        try{

            String funcionario

            switch (servidor) {
                case "Ivanildo": funcionario = "31"
                    break;
                case "Torres": funcionario = "3"
                    break;
                case "Rudsom": funcionario = "64"
                    break;
                default: throw new Exception("Funcionario inexistente");
            }

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String connectionUrl = "jdbc:sqlserver://10.15.0.97:1433;" +
                    "databaseName=TopPontoREP;user=sa;password=pol1c1@;";

            Connection con=DriverManager.getConnection(connectionUrl);

            Statement stmt=con.createStatement();

            String sql = "select codFunc, data, Ausencia_D, codMotivo from dbo.abonos where codFunc = " + funcionario

            ResultSet rs=stmt.executeQuery(sql);
            List abonos = new ArrayList();
            while(rs.next()){
                Abono a = new Abono()
                a.codFunc = rs.getInt(1)
                a.dataLancamento = rs.getDate(2)
                a.ausencia = rs.getTime(3)
                a.codMotivo = rs.getInt(4)

                abonos.add(a)
            }

            System.out.println(abonos)

            con.close();
            return abonos
        }catch(Exception e){
            System.out.println(e);
        }
    }


    /*
    * Pega os Feriados lancados pelo RH
    * */
    def pegaFeriadosFunc(){
        try{

           Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String connectionUrl = "jdbc:sqlserver://10.15.0.97:1433;" +
                    "databaseName=TopPontoREP;user=sa;password=pol1c1@;";

            Connection con=DriverManager.getConnection(connectionUrl);

            Statement stmt=con.createStatement();

            String sql = "select data, descricao from dbo.feriados" 

            ResultSet rs=stmt.executeQuery(sql);
            List feriados = new ArrayList();
            while(rs.next()){
                Feriados f = new Feriados()
                f.dataFeriado = rs.getDate(1)
                f.nomeFeriado = rs.getString(2)

                feriados.add(f)
            }

            System.out.println(feriados)

            con.close();
            return feriados
        }catch(Exception e){
            System.out.println(e);
        }
    }


    /*
    * Pega os lançamentos feitos pelo RH devidos ao sobreaviso
    * */
    def pegaFechamentos(String servidor){
        try{

            String funcionario

            switch (servidor) {
                case "Ivanildo": funcionario = "31"
                    break;
                case "Torres": funcionario = "3"
                    break;
                case "Rudsom": funcionario = "64"
                    break;
                default: throw new Exception("Funcionario inexistente");
            }

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String connectionUrl = "jdbc:sqlserver://10.15.0.97:1433;" +
                    "databaseName=TopPontoREP;user=sa;password=pol1c1@;";

            Connection con=DriverManager.getConnection(connectionUrl);

            Statement stmt=con.createStatement();

            String sql = "select codFunc, data, debito, credito from dbo.fechamentos WHERE CodFunc = " + funcionario

            ResultSet rs=stmt.executeQuery(sql);
            List horarios = new ArrayList();
            while(rs.next()){
                Fechamentos fch = new Fechamentos()
                fch.codFunc = rs.getInt(1)
                fch.dataLancamento = rs.getDate(2)
                fch.cargaHorariaLancada = rs.getTime(3)
                fch.cargaHorariaCredito = rs.getTime(4)

                horarios.add(fch)
            }

            println horarios

            con.close();
            return horarios
        }catch(Exception e){
            System.out.println(e);
        }
    }


    /**
     * Pega a jornarda a ser considerada no dia para o Funcionario
     */
    def pegaJornadaFunc(String servidor){
        try{

            String funcionario

            switch (servidor) {
                case "Ivanildo": funcionario = "31"
                    break;
                case "Torres": funcionario = "3"
                    break;
                case "Rudsom": funcionario = "64"
                    break;
                default: throw new Exception("Funcionario inexistente");
            }

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String connectionUrl = "jdbc:sqlserver://10.15.0.97:1433;" +
                    "databaseName=TopPontoREP;user=sa;password=pol1c1@;";

            Connection con=DriverManager.getConnection(connectionUrl);

            Statement stmt=con.createStatement();
            String sql =  """ select distinct jornadas_func.codFunc, jornadas_func.DtInicio, horarios_jornada.codHorario, marcacoes.marcacao, marcacoes.sequencia from dbo.jornadas_func 
                        inner join horarios_jornada on jornadas_func.codJornada = horarios_jornada.codJornada 
                        inner join marcacoes on horarios_jornada.codHorario = marcacoes.codHorario 
                        where jornadas_func.CodFunc = """ + funcionario +
                        """ order by dtInicio desc """

            ResultSet rs=stmt.executeQuery(sql)
            List horarios = new ArrayList();
            while(rs.next()){
                Jornada jornada = new Jornada();
                jornada.codFunc = rs.getInt(1);
                jornada.dataInicio = rs.getDate(2);
                jornada.codHorario = rs.getInt(3);
                jornada.marcacao = rs.getTime(4);
                jornada.sequencia = rs.getInt(5);

                horarios.add(jornada)
            }
            con.close();
            return horarios
        }catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * Pega a afastamentos a ser considerada no dia para o Funcionario
     */
    def pegaAfastamentosFunc(String servidor){
        try{

            String funcionario

            switch (servidor) {
                case "Ivanildo": funcionario = "31"
                    break;
                case "Torres": funcionario = "3"
                    break;
                case "Rudsom": funcionario = "64"
                    break;
                default: throw new Exception("Funcionario inexistente");
            }

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String connectionUrl = "jdbc:sqlserver://10.15.0.97:1433;" +
                    "databaseName=TopPontoREP;user=sa;password=pol1c1@;";

            Connection con=DriverManager.getConnection(connectionUrl);

            Statement stmt=con.createStatement();
            String sql =  """ select dtInicio, dtFim, abonado, codFunc, codMotivo, LimitaHoraExtra from dbo.afastamentos where CodFunc = """ + funcionario

            ResultSet rs=stmt.executeQuery(sql)
            List aft = new ArrayList();
            while(rs.next()){
                Afastamentos afastamento = new Afastamentos();
                afastamento.dtInicio = rs.getDate(1);
                afastamento.dtFim = rs.getDate(2);
                afastamento.abonado = rs.getBoolean(3);
                afastamento.codFunc = rs.getInt(4);
                afastamento.codMotivo = rs.getInt(5);
                afastamento.limitehoraExtra = rs.getBoolean(6)

                aft.add(afastamento)
            }
            con.close();
            return aft
        }catch(Exception e){
            System.out.println(e);
        }
    }


    /*
    * data deve estar no formato yy/MM/dd
    * servidor deve ser o nome dos servidores do NUTEL
    * */
    def pegaHorario(String data, String servidor) {

        try{

            String funcionario

            switch (servidor) {
                case "Ivanildo": funcionario = "31"
                    break;
                case "Torres": funcionario = "3"
                    break;
                case "Rudsom": funcionario = "64"
                    break;
                default: throw new Exception("Funcionario inexistente");
            }

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String connectionUrl = "jdbc:sqlserver://10.15.0.97:1433;" +
                    "databaseName=TopPontoREP;user=sa;password=pol1c1@;";

            Connection con=DriverManager.getConnection(connectionUrl);

            Statement stmt=con.createStatement();

            //Inserido o CodOcorrencia pois de 3 para cima e normalmente desconsiderado
            String sql = "select cast(DataHora as time) as 'marcacoes' from dbo.bilhetes " +
                    "where CodFunc = "+ funcionario +" and DataHora >= '" + data + "' and DataHora < '"+ LocalDate.parse(data).plusDays(1).toString() +"' " +
                    " and CodOcorrencia < 3 order by DataHora asc"

            ResultSet rs=stmt.executeQuery(sql);
            List horarios = new ArrayList();
            while(rs.next()){
                horarios.add(rs.getTime(1))

            }
            con.close();
            return horarios
        }catch(Exception e){
            System.out.println(e);
        }
    }






}
