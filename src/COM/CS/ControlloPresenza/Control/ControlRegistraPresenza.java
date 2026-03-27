package COM.CS.ControlloPresenza.Control;

import COM.CS.Commons.InterfacciaDB;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.Entity.Turno;
import COM.CS.Utili.CommonMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ControlRegistraPresenza {

    /*riceve in input nome, cognome e matricola e restituisce un booleano (true/false) se trova una corrispondenza nel DBMS attraverso la query*/
    public boolean corrispondenzaDatiSPR(String nome, String cognome, String matricola){
        InterfacciaDB DB = new InterfacciaDB();
        String query = "SELECT * FROM citizenservices.utenti WHERE utenti.matricola = ? AND utenti.nome = ? AND utenti.cognome = ? ;";
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query);)
        {
            pst.setString(1, matricola);
            pst.setString(2, nome);
            pst.setString(3, cognome);
            ResultSet resultSet = pst.executeQuery();
            return resultSet.next();
        }
        catch(SQLException ex){
            return false;
        }
    }

    //preleva dal DBMS la lista dei turni dell'impiegato (contraddistinti dalla matricola),nella data corrente, che ancora devono iniziare( ovvero con codice presenza 0) e li immette nella lista 'turni'
    public void ottieniTurniGiornataDaIniziare(String matricola, String data, ArrayList<Turno> turni){
        turni.clear();
        String dataFormattata = CommonMethods.formattaDataTogli0(data);
        String query = "SELECT turni.* FROM citizenservices.turni WHERE turni.data = ? AND turni.ref_matricola = ? AND turni.cod_tipo_presenza = 0 ORDER BY turni.ora_inizio ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, dataFormattata);
            pst.setString(2, matricola);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                String ref_matricola = resultSet.getString("ref_matricola");
                int cod_servizio = resultSet.getInt("cod_servizio");
                int cod_tipo_presenza = resultSet.getInt("cod_tipo_presenza");
                String ora_inizio = resultSet.getString("ora_inizio");
                String ora_fine = resultSet.getString("ora_fine");
                Turno turno = new Turno(ref_matricola, cod_servizio, dataFormattata, cod_tipo_presenza, ora_inizio, ora_fine);
                turni.add(turno);
            }
        }
        catch(SQLException e){
            DB.lostConnection();
        }
    }

    //cerca il turno dell'impiegato che inizia nell'ora corrente nella lista dei turni e lo restituisce se lo trova
    //se non lo trova o la lista è vuota, restituisce null
    public Turno cercaTurnoImpiegato(String matricola, String ora, ArrayList<Turno> turni){
        if(turni.isEmpty()){
            return null;
        }
        else{
            String[] oraSplit = ora.split(":");
            String oraInizio = oraSplit[0] + ":0:0";
            for(Turno turno : turni){
                if(turno.getOra_inizio().equals(oraInizio)){
                    return turno;
                }
            }
            return null;
        }
    }

    //controlla se l'orario di registrazione dell'impiegato lo farebbe risultare in orario, ovvero, se sono passati massimo 10 minuti dall'inizio del turno
    public int controlloOrarioIngressoPresenza(String inizioTurno){
        String orarioAttuale = InterfacciaTMP.ottieniOrario();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try{
            Date inizioTurnoDate = sdf.parse(inizioTurno);
            Date orarioAttualeDate = sdf.parse(orarioAttuale);
            long differenzaInMillisecondi = orarioAttualeDate.getTime() - inizioTurnoDate.getTime();
            int differenzaInMinuti = (int) (differenzaInMillisecondi / (60 * 1000));
            return differenzaInMinuti;
        }
        catch (ParseException e){
            return -1;
        }
    }

    //registra sul DBMS l'ingresso in orario, ovvero, cambia il cod_presenza da 0 a 1
    public void registraPresenzaInOrario(String orarioIngresso, Turno turno){
         String query = "UPDATE citizenservices.turni SET turni.cod_tipo_presenza = 1, turni.ora_inizio = ? WHERE turni.ref_matricola = ? AND turni.data = ? AND turni.ora_inizio = ? AND turni.ora_fine = ?";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, orarioIngresso);
            pst.setString(2, turno.getRef_matricola());
            pst.setString(3, turno.getData());
            pst.setString(4, turno.getOra_inizio());
            pst.setString(5, turno.getOra_fine());
            int rowsAffected = pst.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("registra presenza in orario errore");
        }
    }

    //registra sul DBMS l'ingresso in ritardo, ovvero, cambia il cod_presenza da 0 a 2
    public void registraPresenzaInRitardo(String orarioIngresso, Turno turno){
        String query = "UPDATE citizenservices.turni SET turni.cod_tipo_presenza = 2, turni.ora_inizio = ? WHERE turni.ref_matricola = ? AND turni.data = ? AND turni.ora_inizio = ? AND turni.ora_fine = ?";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, orarioIngresso);
            pst.setString(2, turno.getRef_matricola());
            pst.setString(3, turno.getData());
            pst.setString(4, turno.getOra_inizio());
            pst.setString(5, turno.getOra_fine());
            pst.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("registra presenza in orario errore");
        }
    }

    //ottiene i turni dell'impiegato già iniziati, ovvero con cod_presenza diversa da 0(non iniziato) e da -1 (assente) e li restituisce in una lista
    public void ottieniTurniGiornataIniziati(String data, String matricola, ArrayList<Turno> turni) {
        turni.clear();
        String dataFormattata = CommonMethods.formattaDataTogli0(data);
        String query = "SELECT * FROM citizenservices.turni WHERE turni.data = ? AND turni.ref_matricola = ? AND turni.cod_tipo_presenza != -1 AND turni.cod_tipo_presenza != 0 ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, dataFormattata);
            pst.setString(2, matricola);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                String ref_matricola = resultSet.getString("ref_matricola");
                int cod_servizio = resultSet.getInt("cod_servizio");
                int cod_tipo_presenza = resultSet.getInt("cod_tipo_presenza");
                String ora_inizio = resultSet.getString("ora_inizio");
                String ora_fine = resultSet.getString("ora_fine");
                Turno turno = new Turno(ref_matricola, cod_servizio, dataFormattata, cod_tipo_presenza, ora_inizio, ora_fine);
                System.out.println(turno.toString());
                turni.add(turno);
            }
        }
        catch(SQLException e){
            DB.lostConnection();
        }
    }

    //controlla se un turno della lista di turni già iniziati appartiene all'impiegato.
    //In quel caso controlla se sono già passati 40 minuti dal suo inizio.
    //Restituisce pertanto i turni che bisogna segnare con una assenza
    public Turno cercaTurnoIniziatoImpiegato(String matricola, ArrayList<Turno> turni){
        String orarioAttuale = CommonMethods.formattaDataTogli0(InterfacciaTMP.ottieniOrario());
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
        try {
            Date orarioAttualeDate = sdf.parse(orarioAttuale);
            for(Turno turno: turni){
                Date orarioInizioTurnoDate = sdf.parse(turno.getOra_inizio());
                long differenza = (int) ((orarioAttualeDate.getTime()- orarioInizioTurnoDate.getTime()) / (60*1000));
                if(turno.getRef_matricola().equals(matricola) && differenza >= 40 ){
                    return turno;
                }
            }
            return null;
        }
        catch (ParseException ex){
            return null;
        }
    }

    //registra l'uscita dell'impiegato, cambiando l'ora di fine del turno
    public void registraUscita(String s, Turno turno) {
        String query = "UPDATE citizenservices.turni SET turni.ora_fine = ? WHERE turni.ref_matricola = ? AND turni.data = ? AND turni.ora_inizio = ? AND turni.ora_fine = ?";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, CommonMethods.formattaDataTogli0(InterfacciaTMP.ottieniOrario()));
            pst.setString(2, turno.getRef_matricola());
            pst.setString(3, turno.getData());
            pst.setString(4, turno.getOra_inizio());
            pst.setString(5, turno.getOra_fine());
            //int rowsAffected = pst.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("registra presenza in orario errore");
        }
    }
}
