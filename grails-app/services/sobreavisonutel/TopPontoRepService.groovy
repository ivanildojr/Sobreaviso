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

            String sql = "select codFunc, data, debito from dbo.fechamentos WHERE CodFunc = " + funcionario

            ResultSet rs=stmt.executeQuery(sql);
            List horarios = new ArrayList();
            while(rs.next()){
                Fechamentos fch = new Fechamentos()
                fch.codFunc = rs.getInt(1)
                fch.dataLancamento = rs.getDate(2)
                fch.cargaHorariaLancada = rs.getTime(3)

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

            String sql = "select cast(DataHora as time) as 'marcacoes' from dbo.bilhetes " +
                    "where CodFunc = "+ funcionario +" and DataHora >= '" + data + "' and DataHora < '"+ LocalDate.parse(data).plusDays(1).toString() +"' " +
                    "order by DataHora asc"

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
