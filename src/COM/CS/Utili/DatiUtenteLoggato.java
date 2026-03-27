package COM.CS.Utili;
import COM.CS.Commons.InterfacciaDB;
import COM.CS.Entity.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.prefs.Preferences;

public class DatiUtenteLoggato {
    private Preferences userPreferences;

    public DatiUtenteLoggato(){
        userPreferences = Preferences.userRoot().node("com/CitizenServices/userdata");
    }

    public void salvaDatiUtente(Utente utente){
        userPreferences.put("matricola", utente.getMatricola());
        userPreferences.putInt("cod_contratto", utente.getCod_contratto());
        userPreferences.put("nome", utente.getNome());
        userPreferences.put("cognome", utente.getCognome());
        userPreferences.put("telefono", utente.getTelefono());
        userPreferences.put("IBAN", utente.getIBAN());
        userPreferences.put("mail", utente.getMail());
        userPreferences.putInt("ruolo",utente.getRuolo());
        userPreferences.putInt("stato_contratto", utente.getStato_contratto());
        userPreferences.putInt("ore_da_svolgere", utente.getOre_da_svolgere());
    }

    public void modificaDatiUtente(String mail, String IBAN, String telefono){
        userPreferences.put("mail", mail);
        userPreferences.put("IBAN", IBAN);
        userPreferences.put("telefono", telefono);
    }

    public Utente getUtente(){
        String matricola = userPreferences.get("matricola", "");
        int cod_contratto = userPreferences.getInt("cod_contratto", 0);
        String nome = userPreferences.get("nome", "");
        String cognome = userPreferences.get("cognome", "");
        String telefono = userPreferences.get("telefono", "");
        String IBAN = userPreferences.get("IBAN", "");
        String mail = userPreferences.get("mail", "");
        int ruolo = userPreferences.getInt("ruolo", 0);
        int stato_contratto = userPreferences.getInt("stato_contratto", 0);
        String cod_fiscale = userPreferences.get("cod_fiscale","");
        int ore_da_svolgere = userPreferences.getInt("ore_da_svolgere", 0);

        Utente utente = new Utente(matricola, cod_contratto, nome, cognome, telefono, IBAN, mail, ruolo, stato_contratto, cod_fiscale, ore_da_svolgere);

        return utente;
    }

    public void setTipoContratto(String matricola){
        String query = "SELECT tipo_contratto FROM citizenservices.contratti JOIN citizenservices.utenti ON utenti.cod_contratto = contratti.cod_contratto WHERE utenti.matricola = ? ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, matricola);
            ResultSet resultSet = pst.executeQuery();
            if(resultSet.next()) {
                userPreferences.put("tipo_contratto",resultSet.getString("tipo_contratto"));
            }
        }
        catch (SQLException e) {
            System.out.println("errore ottieni tipo contatto");
            DB.lostConnection();
        }
    }
    public String getTipoCotratto(){
        return userPreferences.get("tipo_contratto", "");
    }

    public void eliminaDatiUtente(){
        try {
            userPreferences.clear();
            userPreferences.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
