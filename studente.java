import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class studente {


    public String  matr ;
    public String  nome;
    public String citta;
    public int anno;
    public ArrayList<Esame> listaEsami;
    public boolean studenteEsiste;
    public float mediaEsami;

    public studente() {

        matr = "";
        nome = "";
        citta = "";
        anno = 0;
        listaEsami = null;
        studenteEsiste = false;
        mediaEsami = 0;
    }


    public studente(Connection cn, String matricola) throws Exception {

        Statement stmt = null;

        stmt = cn.createStatement();

        String sql;
        sql = "SELECT s.Matr, " +
                " s.SNome, " +
                " s.citta, " +
                " s.ACorso " +
                " FROM s  " +
                " WHERE s.Matr = '" + matricola + "'";

        ResultSet rs = stmt.executeQuery(sql);

        if(rs.next()){
            //Retrieve by column name
            this.matr  = rs.getString("Matr");
            this.nome = rs.getString("SNome");
            this.citta = rs.getString("citta");
            this.anno = rs.getInt("ACorso");
            this.studenteEsiste = true;

            sql = "SELECT AVG(e.Voto) AS media" +
                    " FROM e  " +
                    " WHERE e.Matr = '" + matricola + "'";

            rs.close();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                this.mediaEsami = rs.getFloat("media");

            }
            else
                this.mediaEsami = 0;

            sql = "SELECT e.Matr, " +
                    " e.CC, " +
                    " e.Data, " +
                    " e.Voto, " +
                    " c.CNome " +
                    " FROM e, c  " +
                    " WHERE c.CC = e.CC AND " +
                    " e.Matr = '" + matricola + "'";

            rs.close();
            rs = stmt.executeQuery(sql);

            this.listaEsami = new ArrayList<Esame>();
            while(rs.next()){

                Esame e = new Esame(rs.getString("Matr"),rs.getString("CC"), rs.getString("Data"),rs.getInt("Voto"),rs.getString("CNome") );
                this.listaEsami.add(e);
            }

            int somma=0;
            int cont=0;

            for(Esame e : this.listaEsami){
                somma += e.voto;
                cont++;
            }
            this.mediaEsami = somma/cont;


        }
        else
        {

            this.matr = "";
            this.nome = "";
            this.citta = "";
            this.anno = 0;
            this.listaEsami = null;
            this.studenteEsiste = false;
            mediaEsami = 0;
        }

        rs.close();

        stmt.close();

    }

    public void aggiornaStudente(Connection cn) throws Exception{

        Statement stmt = cn.createStatement();
        ResultSet rs;
        String query = "SELECT * FROM s ";

        if(this.matr.length()>0) {


            query = query + " WHERE Matr = '" + this.matr + "'";

            rs = stmt.executeQuery(query);

            if(rs.next()){
                // lo studente esiste
                query = "UPDATE s SET SNome = '" + this.nome + "', citta = '" + this.citta + "', ACorso = "
                        + this.anno + "  WHERE Matr = '" + this.matr + "';";

            }
            else
            {
                // lo studente non esiste
                query = "INSERT INTO s (Matr, SNome, citta, ACorso) VALUES " +
                        "('" + this.matr + "', '" + this.nome + "', '" + this.citta + "', " + this.anno + ");";
            }
            //System.out.println(query);
            stmt.executeUpdate(query);
        }
        else
            System.out.println("Matricola non valida per l'aggiornamento...");
    }


    public void eliminaStudente(Connection cn) throws Exception {

        Statement stmt = null;
        stmt = cn.createStatement();
        String sql;
        sql = "SELECT COUNT(*) AS NumEsami " +
                " FROM e  " +
                " WHERE e.Matr = '" + this.matr + "'";

        ResultSet rs = stmt.executeQuery(sql);

        if(rs.next()){

            Integer contaEsami = rs.getInt("NumEsami");

            if (contaEsami == 0) {
                sql = "DELETE FROM s  " +
                        " WHERE s.Matr = '" + this.matr + "'";

                stmt.executeUpdate(sql);
                System.out.println("Studente eliminato con successo");
            }
            else {
                System.out.println("Lo studente ha sostenuto " + contaEsami  + " esami, non lo posso eliminare");

            }
        }

        rs.close();
        stmt.close();

    }



    @Override
    public String toString() {

        String out="";

        out = "Studente{ " +
                "Matricola= " + matr +
                ", Nome= " + nome+ " " +
                ", Citt�= " + citta + " " +
                ", Anno di corso=" + anno +
                ", Media esami=" + mediaEsami +
                " }\n";

        for(Esame e : this.listaEsami){
            out += e.toString() + "\n";
        }
        return out;
    }

}
