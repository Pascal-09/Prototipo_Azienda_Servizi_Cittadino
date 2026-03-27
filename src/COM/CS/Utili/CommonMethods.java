package COM.CS.Utili;

import COM.CS.Commons.InterfacciaDB;
import COM.CS.GestioneProfilo.Interface.HomepageAmministratore;
import COM.CS.GestioneProfilo.Interface.HomepageDatoreDiLavoro;
import COM.CS.GestioneProfilo.Interface.HomepageImpiegato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommonMethods {


    //sicurezza -> cifrario di cesare
    public static String decriptaPsw(String psw){
        StringBuilder cifrata = new StringBuilder();
        for(int i = 0; i<psw.length(); i++){
            char carattere = psw.charAt(i);
            if(Character.isLetter(carattere)){
                char base = Character.isUpperCase(carattere) ? 'A' : 'a';
                cifrata.append((char) (base + (carattere - base + 5) % 26));
            }
            else{
                cifrata.append(carattere);
            }
        }
        return cifrata.toString();
    }

    public static String encriptaPsw(String pswCifrata) {
        StringBuilder decifrata = new StringBuilder();

        for (int i = 0; i < pswCifrata.length(); i++) {
            char carattere = pswCifrata.charAt(i);

            if (Character.isLetter(carattere)) {
                char base = Character.isUpperCase(carattere) ? 'A' : 'a';
                decifrata.append((char) (base + (carattere - base - 5 + 26) % 26));
            } else {
                decifrata.append(carattere);
            }
        }

        return decifrata.toString();
    }

    //scelta homepage
    public static void mostraHomepage(int ruolo){
        switch(ruolo){
            case 1, 2, 3, 4:
                HomepageImpiegato HI = new HomepageImpiegato();
                HI.setVisible(true);
                break;
            case -1:
                HomepageAmministratore HA = new HomepageAmministratore();
                HA.setVisible(true);
                break;
            case 0:
                HomepageDatoreDiLavoro HDDL = new HomepageDatoreDiLavoro();
                HDDL.setVisible(true);
                break;
        }
    }


    public static String ottieniStringaRuolo(int ruolo){
        switch (ruolo){
            case 1: return "impiegato tipo 1";
            case 2: return "impiegato tipo 2";
            case 3 : return "impiegato tipo 3";
            case 4 : return "impiegato tipo 4";
            case -1: return "amministratore";
            case 0 : return "datore di lavoro";
        }
        return "errore";
    }

    public static String formattaDataAggiungi0(String data){
        String[] dataSplit = data.split("-");
        String giorno = dataSplit[0];
        char d1 = giorno.charAt(0);
        if(d1 == '0'){
            return giorno.charAt(1) + "-" + dataSplit[1] + "-" + dataSplit[2];
        }
        else{
            return data;
        }
    }

     /* Il metodo formattaDataTogli0 prende una stringa di input, controlla se inizia con '0', e se lo fa, restituisce una nuova stringa senza il '0' iniziale.
    Altrimenti, restituirà la stessa stringa di input senza apportare modifiche. Questo può essere utile per formattare date o numeri in modo da rimuovere zeri non significativi all'inizio.
    */


    public static String formattaDataTogli0(String data){
        StringBuilder sb = new StringBuilder(data);
        if(data.charAt(0) == '0'){
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }
}
