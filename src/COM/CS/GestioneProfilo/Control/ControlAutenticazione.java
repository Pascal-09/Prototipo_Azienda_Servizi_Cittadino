package COM.CS.GestioneProfilo.Control;

import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;
import COM.CS.Commons.InterfacciaDB;
import COM.CS.Entity.Utente;
import COM.CS.GestioneProfilo.Interface.Autenticazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControlAutenticazione {


    /*Il metodo controlloCredenziali definisce un metodo pubblico che restituisce un valore booleano (vero o falso) e accetta due parametri di tipo String (matricola,password)
    L'istruzione di ritorno del metodo usa due volte il metodo di stringa  isBlank per verificare se  sia la matricola che la password non sono stringhe vuote o che contengono solo spazi bianchi.
    La funzione restituirà true se entrambe le condizioni sono soddisfatte, il che indica che le credenziali fornite sono corrette, altrimenti restituirà false, indicando che una o entrambe le credenziali sono incomplete o non valide.*/
    public boolean controlloCredenziali(String matricola, String password){
        return !matricola.isBlank() && !password.isBlank();
    }

    /*Il metodo "ottieniDatiUtente" riceve una matricola come parametro e recupera le informazioni di un utente corrispondente dal database "citizenservices.utenti".
     Queste informazioni vengono estratte dalla query SQL e utilizzate per creare un oggetto "Utente", che viene quindi restituito come risultato del metodo.
     In caso di errore o se l'utente non è presente nel database, il metodo restituirà null.*/
    public Utente ottieniDatiUtente(String matricola) {
        String query = "SELECT * FROM citizenservices.utenti WHERE utenti.matricola = ?;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
        ) {
            pst.setString(1, matricola);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                String matr = resultSet.getString("matricola");
                int cod_contratto = resultSet.getInt("cod_contratto");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                String telefono = resultSet.getString("telefono");
                String IBAN = resultSet.getString("IBAN");
                String mail = resultSet.getString("mail");
                int ruolo = resultSet.getInt("ruolo");
                int stato_contratto = resultSet.getInt("stato_contratto");
                int ore_da_svolgere = resultSet.getInt("ore_da_svolgere");
                String cod_fiscale = resultSet.getString("cod_fiscale");
                Utente utente = new Utente(matr, cod_contratto, nome, cognome, telefono, IBAN, mail, ruolo, stato_contratto, cod_fiscale, ore_da_svolgere);
                return utente;
            }
        }
        catch (SQLException ex) {
            return null;
        }
        return null;
    }

/* Il metodo corrispondenzaCredenziali verifica se le credenziali immesse dall'utente, ovvero matricola e una password, corrispondono alle informazioni memorizzate nel dbms.
Il metodo esegue una query SQL parametrizzata per cercare un utente nel database "citizenservices.utenti" in base alla matricola e alla password fornite come argomenti.
Se trova una corrispondenza, restituisce "true"; altrimenti, restituirà "false". Inoltre, il codice gestisce eccezioni SQL e protegge la password utilizzando la funzione "CommonMethods.encriptaPsw" prima di confrontarla con quella nel database.*/
    public boolean corrispondenzaCredenziali(String matricola, String password){
        InterfacciaDB DB = new InterfacciaDB();
        String query = "SELECT * FROM citizenservices.utenti WHERE utenti.matricola = ? AND utenti.password = ?";
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query);)
        {
                pst.setString(1, matricola);
                pst.setString(2, CommonMethods.encriptaPsw(password));
                ResultSet resultSet = pst.executeQuery();
                return resultSet.next();
        }
        catch(SQLException ex){
            return false;
        }
    }
    /* Questo metodo è utilizzato per gestire l'uscita dell'utente dalla homepage.
    Inizialmente viene creato un oggetto della classe "DatiUtenteLoggato" e viene utilizzato il suo metodo "eliminaDatiUtente" per rimuovere i dati dell'utente correntemente loggato (chiudere la sessione)
    Successivamente viene creato un oggetto della classe "Autenticazione" che viene reso visibile per consentire ad un altro utente di effettuare l'accesso alla homepage dalla schermata autenticazione.*/
    public static void uscita(){
        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        DUL.eliminaDatiUtente();
        Autenticazione A = new Autenticazione();
        A.setVisible(true);
    }
}
