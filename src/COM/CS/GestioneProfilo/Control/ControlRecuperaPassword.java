package COM.CS.GestioneProfilo.Control;

import COM.CS.Commons.InterfacciaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControlRecuperaPassword {


    protected String ottieniMailDaMatricola(String matricola) {
        return "";
    }

    /*Il metodo controlloMatricoolaNonVuota definisce un metodo pubblico che restituisce un valore booleano (vero o falso) e accetta un parametro di tipo String (matricola)
     L'istruzione di ritorno del metodo usa il metodo di stringa  isBlank per verificare se la matricola è vuota o contiene solo spazi bianchi.
     La funzione restituirà true se la condizione è soddisfatta, il che indica che la matricola fornita è corretta, altrimenti restituirà false, indicando che le matricola è incompleta o non valida.*/
    public boolean controlloMatricolaNonVuota(String matricola) {
        return !matricola.isBlank();
    }


    /* Il metodo controlloEsistenzaMatricola verifica se la matricola immessa dall'utente corrisponde alla matricola memorizzata nel dbms.
    Il metodo esegue una query SQL parametrizzata per cercare un utente nel database "citizenservices.utenti" in base alla matricola fornita come argomento.
    Se trova una corrispondenza, restituisce "true"; altrimenti, restituirà "false". Inoltre, il codice gestisce eccezioni SQL prima di confrontarla con quella nel database.*/

    public boolean controlloEsistenzaMatricola(String matricola) {
        InterfacciaDB DB = new InterfacciaDB();
        String query = "SELECT * FROM citizenservices.utenti WHERE utenti.matricola = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, matricola);
            ResultSet resultSet = pst.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            return false;
        }
    }
}
