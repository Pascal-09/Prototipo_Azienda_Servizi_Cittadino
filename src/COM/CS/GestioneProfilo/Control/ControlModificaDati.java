package COM.CS.GestioneProfilo.Control;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.AvvisoSuccesso;
import COM.CS.Utili.CommonMethods;
import COM.CS.Commons.InterfacciaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ControlModificaDati {


    /* il metodo controlloPsw prende due argomenti di tipo String: nuovaPsw e confermaNuovaPsw.
     Il metodo confronta se queste due stringhe sono uguali utilizzando il metodo equals di Java.
     Restituirà true se le due stringhe sono identiche e false in caso contrario.
     Questo metodo verifica se la stringa nuovaPsw è uguale alla stringa confermaNuovaPsw per confermare che la modifica della password possa avvenire senza ambiguità.*/

    public boolean controlloPsw(String nuovaPsw, String confermaNuovaPsw){
        return nuovaPsw.equals(confermaNuovaPsw);
    }


    /* Il metodo controlloModifiche accetta tre argomenti di tipo stringa: mail, IBAN e telefono.
     Inizialmente, il codice verifica se le stringhe mail, IBAN e telefono non sono vuote. Successivamente, effettua tre controlli separati.
     Il primo controllo mailCheck utilizza un'espressione regolare per verificare se le tre parti della stringa mail seguono degli specifici pattern prestabiliti.
     Il secondo controllo IBANCheck verifica se la stringa IBAN è valida secondo il pattern prestabilito.
     Il terzo controllo telefonoCheck verifica se la stringa telefono è composta solo da cifre.
     Il metodo controlloModifiche restituisce true solo se tutti questi controlli risultano positivi, altrimenti restituirà false. */

    public boolean controlloModifiche(String mail, String IBAN, String telefono){
        boolean firstCheck = !mail.isBlank() && !IBAN.isBlank() && !telefono.isBlank();
        boolean mailCheck = mail.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
        boolean IBANCheck = IBAN.matches("^[A-Z]{2}[0-9]{2}[A-Z0-9]{4}[0-9]{7}([A-Z0-9]?){0,16}$");
        boolean telefonoCheck = true;
        for(char c : telefono.toCharArray()){
            if(!Character.isDigit(c)){
                telefonoCheck = false;
            }
        }
        return firstCheck && mailCheck && IBANCheck && telefonoCheck;
    }

    /* Il metodo modificaPsw ha lo scopo di modificare la password di un utente registrato nel dbms.
    Il metodo utilizza un'istruzione SQL per eseguire l'aggiornamento della password.
    La funzione encriptaPsw è chiamata per cifrare la nuova password prima di essere inserita nella query SQL.
    Se almeno una riga nel database viene aggiornata con successo, viene visualizzato un messaggio di successo tramite una finestra di avviso AvvisoSuccesso.
    In caso contrario, viene mostrato un messaggio di errore tramite AvvisoErrore.*/

    public void modificaPsw(String matricola, String nuovaPsw) {
        String query = "UPDATE citizenservices.utenti SET utenti.password = ? WHERE utenti.matricola = ? ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, CommonMethods.encriptaPsw(nuovaPsw));
            pst.setString(2, matricola);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                AvvisoSuccesso AS = new AvvisoSuccesso("i dati sono stati aggiornati correttamente");
                AS.pack();
                AS.setVisible(true);
            } else {
                AvvisoErrore AE = new AvvisoErrore("non è stata apportata alcuna modifica");
                AE.pack();
                AE.setVisible(true);
            }
        } catch (SQLException e) {
            System.out.println("ERROREEEE QUERY");
        }
    }

    /* Il metodo modificaDatiUtente prende in input la "matricola" dell'utente, nonché nuovi valori per l'indirizzo email ("mail"), l'IBAN e il numero di telefono.
    Viene utilizzata un'istruzione SQL per eseguire l'aggiornamento dei dati dell'utente nel database. Se l'operazione di aggiornamento ha successo (cioè se almeno una riga viene modificata), visualizza un messaggio di successo, altrimenti mostra un messaggio di errore.
    In caso di problemi con la query SQL, viene stampata l'istruzione SQL sulla console per scopi di debugging.*/
    public void modificaDatiUtente(String matricola, String mail, String IBAN, String telefono){
        String query = "UPDATE citizenservices.utenti SET utenti.mail = ?, utenti.IBAN = ?, utenti.telefono = ? WHERE utenti.matricola = ?;";
        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query);){
            pst.setString(1, mail);
            pst.setString(2, IBAN);
            pst.setString(3, telefono);
            pst.setString(4, matricola);
            int rowsUpdated = pst.executeUpdate();
            if(rowsUpdated>0){
                AvvisoSuccesso AS = new AvvisoSuccesso("i dati sono stati aggiornati correttamente");
                AS.pack();
                AS.setVisible(true);
            }
            else{
                AvvisoErrore AE = new AvvisoErrore("non è stata apportata alcuna modifica");
                AE.pack();
                AE.setVisible(true);
            }
        }
        catch (SQLException ex){
            System.out.println(query);
        }
    }
}
