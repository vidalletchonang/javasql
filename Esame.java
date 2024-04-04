import java.sql.*;
import java.util.*;


public class Main {

    public static void main(String[] args) throws Exception {

        Connection conn = null;
        Statement stmt = null;

        String query = "SELECT * FROM s ";


        //11:38:15	SELECT * FROM universita.studente	7 row(s) returned	0.000 sec / 0.000 sec
        String URL = "jdbc:mysql://localhost/universita";
        Properties info = new Properties( );
        info.put( "user", "root" );
        info.put( "password", "Rout" );
        info.put( "autoReconnect", "true" );
        info.put( "useSSL", "false" );
        info.put( "serverTimezone", "Europe/Amsterdam" );

        Class.forName("com.mysql.cj.jdbc.Driver");

        //STEP 3: Open a connection
        System.out.println("Connecting to database...");
        //conn = DriverManager.getConnection("jdbc:mysql://localhost/universita?autoReconnect=true&useSSL=false&serverTimezone=Europe/Amsterdam","root","root");
        conn = DriverManager.getConnection(URL, info);

        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()){
            //Retrieve by column name

            //System.out.print(rs.getString("MATR"));

            String  matr  = rs.getString("Matr");
            String  nome = rs.getString("SNome");
            String citta = rs.getString("citta");
            int anno = rs.getInt("ACorso");

            if (anno == 1){
                //Display values
                System.out.print("Matr: " + matr);
                System.out.print(", SNOME: " + nome);
                System.out.print(", citta: " + citta);
                System.out.println(", ACorso: " + anno);
            }

//	             pw.println ("MATR: " + Matr + ", NOME: " + SNome + ", Citta: " + citta + ", Anno: " + ACorso);

        }


        query = query + " WHERE Matr = 'M11'";

        rs = stmt.executeQuery(query);

        if(rs.next()){
            // lo studente esiste
            query = "UPDATE s SET ACorso = 2 WHERE Matr = 'M11';";
            //query = "DELETE FROM s Matr = 'M11';";


        }
        else
        {
            // lo studente non esiste
            query = "INSERT INTO s (Matr, SNome, citta, ACorso) VALUES ('M11', 'Vidalle Newgep', 'MO', 1);";
        }
        int res = stmt.executeUpdate(query);

        System.out.println("Ho aggiornato M11 ! numero di tuple coivolte: " + res);


        studente s1 = new studente(conn, "M2");

        if(s1.studenteEsiste == true)
            System.out.println(s1.toString());
        else
            System.out.println("Non ho trovato lo studente !");

        //s1.matr = "M25";
        s1.nome = "luca bianchi";
        s1.citta = "Modena";
        s1.anno = 3;


        //s1.nome = "Lucia Quaranta";
        //s1.citta = "SA";
        //s1.anno = 1;

        s1.aggiornaStudente(conn);
        System.out.println(s1.toString());


        s1.eliminaStudente(conn);
        System.out.println(s1.toString());

        //System.out.println("Matr: " + s1.matr + ", Nome: "  + s1.nome);

        //   	for(Esame e : s1.listaEsami){
        //   		System.out.println("Esame: " + e.NomeCorso);
        //   	}

        conn.close();

        System.out.println("CIAO!");

    }

}
