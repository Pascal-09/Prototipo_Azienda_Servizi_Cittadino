package COM.CS.GestioneTurnazione.Control;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.AvvisoSuccesso;
import COM.CS.Commons.InterfacciaDB;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.Entity.Richiesta;
import COM.CS.Utili.CommonMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ControlGestioneRichieste {

       /* Il metodo controlloDateValide  verifica se le date fornite sono valide.
    - Per essere valide, ovvero per far sì che controlloDateValide=true)
      -> Entrambe le date devono essere non vuote.
       -> La data di inizio deve essere precedente alla data di fine.
       -> La data di inizio deve essere successiva alla data odierna.
    - Se una qualsiasi di queste condizioni non è soddisfatta, il metodo restituirà false.
    */

    public boolean controlloDateValide(String dataInizio, String dataFine){
        String dataOggi = InterfacciaTMP.ottieniData();
        if(!dataInizio.isBlank() && !dataFine.isBlank()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date inizio = dateFormat.parse(dataInizio);
                Date fine = dateFormat.parse(dataFine);
                Date dataToday = dateFormat.parse(dataOggi);
                return  inizio.before(fine) && inizio.after(dataToday);
            }
            catch (ParseException e){
                return false;
            }
        }
        else {
            return false;
        }
    }

    /* Il metodo controlloRientraNelNumeroOreRichiedibili controlla se l'intervallo di tempo tra dataInizio e dataFine rientra nel limite delle ore richieste (ore_richiedibili).Se rientra, restituisce true, altrimenti restituisce false.
     - SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"): crea un oggetto SimpleDateFormat che viene utilizzato per formattare le date nel formato "yyyy-MM-dd".
     - Date inizio = dateFormat.parse(dataInizio): converte le stringa dataInizio in un oggetto Date utilizzando il formato specificato.
     - Date fine = dateFormat.parse(dataFine): converte la stringa dataFine in un oggetto Date utilizzando il formato specificato.
     - Questo è utile per poter calcolare la differenza tra le due date.
     - long durationInMillis = fine.getTime() - inizio.getTime();: calcola la differenza in millisecondi tra le due date (Viene sottratto il tempo in millisecondi della data di inizio dalla data di fine)
     - int durationInHours = (int) (durationInMillis / (60*60*1000)): converte la differenza di millisecondi calcolata in precedenza in ore. Poiché ci sono 60 minuti in un'ora e 60 secondi in un minuto, il valore viene diviso per (60 * 60 * 1000) per ottenere il numero di ore.
     - return durationInHours <= ore_richiedibili;: restituisce:
                                                    -> (true) se durationInHours (la differenza tra le date in ore) è minore o uguale a ore_richiedibili.
                                                    -> (false.) altrimenti.
     - QUINDI: il metodo verifica se la differenza tra dataInizio e dataFine è inferiore o uguale al numero di ore richieste (ore_richiedibili) e restituisce true se è vero, altrimenti false.
    */

    public boolean controlloRientraNelNumeroOreRichiedibili(String dataInizio, String dataFine, int ore_richiedibili){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date inizio = dateFormat.parse(dataInizio);
            Date fine = dateFormat.parse(dataFine);
            long durationInMillis = fine.getTime() - inizio.getTime();
            int durationInHours = (int) (durationInMillis / (60*60*1000));
            return durationInHours <= ore_richiedibili;
        }
        catch (ParseException e){
            return false;
        }
    }

        /* Il metodo ottieniNumeroOreRichiedibili calcola e restituisce il numero di ore richiedibili in base a tre argomenti:
        - tipo_contratto: determina il numero totale di ore richiedibili.
        - numero_ore_da_svolgere_: non utilizzato nel calcolo.
        - tipoAstensione: che specifica il tipo di astensione. Il metodo gestisce tre casi di astensione diversi:
        - Viene dichiarata una variabile intera n_tot che verrà utilizzata per memorizzare il numero totale di ore richiedibili. Il suo valore verrà determinato in base al tipo di contratto.
        - (blocco if): verifica se il tipo_contratto è uguale a "part-time". Se la condizione è vera, il blocco di codice successivo verrà eseguito.
                       -> n_tot = 18: La variabile n_tot viene impostata su 18.
                       -> Questo valore rappresenta il numero totale di ore richiedibili in un contratto part-time.
        - (blocco else): Se la condizione dell'istruzione condizionale precedente non è vera (cioè il tipo_contratto non è "part-time"), allora sicuramente sarà "full-time" e verrà eseguitoblocco di codice successivo
                        -> n_tot = 36:  la variabile n_tot viene impostata su 36.
                        -> Questo valore rappresenta il numero totale di ore richiedibili in un contratto full-time.
        - (blocc switch):  In base al valore di tipoAstensione, verrà selezionato un caso specifico e verrà restituito un valore corrispondente:
                        -> case 0: Se tipoAstensione=0, significa che si tratta di un "congedo parentale".
                                   - return n_tot * 3 * 3: In questo caso, vengono calcolate e restituite le ore richiedibili.
                                   - Il congedo parentale dura 3 mesi, quindi n_tot (numero totale di ore richiedibili) viene moltiplicato per 3 mesi e ancora per 3 per ottenere il numero di ore richiedibili per il congedo parentale.
                        -> case 1: Se tipoAstensione=1, significa che si tratta di uno "sciopero":
                                   - return (int) n_tot / 7 * 5: In questo caso, vengono calcolate e restituite le ore richiedibili.
                                   - Si assume che ci siano 5 giorni di sciopero, quindi n_tot viene diviso per 7 (giorni in una settimana) e moltiplicato per 5 per ottenere il numero di ore richiedibili per lo sciopero.
                        -> case 2: Se tipoAstensione è uguale a 2, significa che si tratta di "ferie".
                                   - return n_tot *: In questo caso, vengono calcolate e restituite le ore richiedibili.
                                   - Si assume che ci siano 2 settimane di ferie, quindi n_tot viene moltiplicato per 2 per ottenere il numero di ore richiedibili per le ferie.
                        -> return 0: Se tipoAstensione non corrisponde a nessuno dei casi precedenti, il che significa che non ci sono ore richiedibili
    */

    public int ottieniNumeroOreRichiedibili(String tipo_contratto, int numero_ore_da_svolgere, int tipoAstensione){
        int n_tot;
        if(tipo_contratto.equals("part-time")){
            n_tot = 18;
        }
        else{
            n_tot = 36;
        }
        switch (tipoAstensione){
            case 0: //congedo parentale
                return 90 * 24; // il congedo parentale è di 3 mesi circa
            case 1: //sciopero
                return 5 * 24; //i giorni di sciopero sono circa 5
            case 2: //ferie
                return 28 * 24; //il numero di giorni di ferie è di 4 settimane circa
            case 4: return 4 * 24;
        }
        return 0;
    }



        /* Il metodo mandaRichiestaPeriodoAstensione accetta un oggetto Richiesta, inserisce i dati di questa richiesta in una tabella di un database utilizzando una query SQL di inserimento.
       Se l'inserimento ha successo, mostra un messaggio di successo tramite un'interfaccia AvvisoSuccesso.
     - Viene definita una query che inserisce una nuova riga (record) nella tabella richieste del database, con i dati relativi a una "Richiesta".
     - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
     - Vengono impostati i valori dei parametri nella query utilizzando i metodi setString e setInt.
     - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto righeInserite (conterrà la riga da inserire specificata dalla query).
     - (blocco if):  Verifica se almeno una riga è stata inserita con successo nel database:
                    -> viene creata un oggetto AvvisoSuccesso che visualizza un messaggio di successo.
     - In caso di eccezioni, viene invocato il metodo lostConnection() dell'oggetto DB.
    */


    public void mandaRichiestaPeriodoAstensione(Richiesta richiesta){
        String query = "INSERT INTO citizenservices.richieste (cod_richiesta, ref_utente, cod_motivo, nome, cognome, statoRichiesta, inizioPeriodo, finePeriodo) VALUES (?,?,?,?,?,?,?,?) ;";
        InterfacciaDB DB= new InterfacciaDB();

        try (Connection con=DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)){
            pst.setInt(1,richiesta.getCod_richiesta());
            pst.setString(2, richiesta.getRef_utente());
            pst.setInt(3, richiesta.getCod_motivo());
            pst.setString(4, richiesta.getNome());
            pst.setString(5, richiesta.getCognome());
            pst.setInt(6, richiesta.getStatoRichiesta());
            pst.setString(7, richiesta.getInizioPeriodo());
            pst.setString(8, richiesta.getFinePeriodo());

            int righeInserite = pst.executeUpdate();
            if(righeInserite > 0){
                AvvisoSuccesso AS = new AvvisoSuccesso("<html>la richiesta è stata mandata con successo.<br>Devi attendere che il Datore di Lavoro la approvi.<br>Controlla le tue notifiche per monitorare l’esito della richiesta!</html>");
                AS.pack();
                AS.setVisible(true);
            }
        }
        catch (SQLException e) {
            DB.lostConnection();
        }
    }



    /* Il metodo controlloListaRichiesteVuota serve a determinare se l'ArrayList listaRichieste passato come parametro è vuoto o contiene elementi:
        - Restituisce true se la lista è vuota.
        - Restituisce false se contiene almeno un elemento.
     */

    public boolean controlloListaRichiesteVuota(ArrayList<Richiesta> listaRichieste){
        return listaRichieste.isEmpty();
    }


       /* Il metodo registraApprovazioneRichiesta esegue un'operazione di aggiornamento (UPDATE) dei dati di una "Richiesta" nella tabella "richieste" del dbms impostando il campo statoRichiesta della corrispondente riga nel database a 1.
     - Viene definita una query che imposta il valore della colonna statoRichiesta nella tabella citizenservices.richieste a 1.
     - L'aggiornamento viene applicato solo alle righe in cui il valore della colonna cod_richiesta è uguale al valore specificato mediante il segnaposto ?.
     - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
     - Viene impostato il valore del parametro nella query utilizzando il metodo setInt.
     - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto righeInserite (conterrà la riga da inserire specificata dalla query).
     - In caso di eccezioni, viene invocato il metodo lostConnection() dell'oggetto DB.
    */


    public void registraApprovazioneRichiesta(Richiesta richiesta){
        String query = "UPDATE citizenservices.richieste SET statoRichiesta = 1 WHERE richieste.cod_richiesta = ? ;";
        InterfacciaDB DB= new InterfacciaDB();

        try (Connection con=DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)){
            pst.setInt(1, richiesta.getCod_richiesta());
            int righeInserite = pst.executeUpdate();
        }
        catch (SQLException e) {
            DB.lostConnection();
        }
    }

   /* Il metodo registraRifiutoRichiesta prende un oggetto Richiesta e registra il rifiuto di questa richiesta nel database aggiornando il campo statoRichiesta a -1 per la richiesta specifica.
    - Viene definita una query che imposta il valore della colonna statoRichiesta nella tabella citizenservices.richieste a -1. L'aggiornamento viene applicato solo alle righe in cui il valore della colonna cod_richiesta è uguale al valore specificato mediante il segnaposto ?.
    - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
    - Viene impostato il valore del parametro nella query utilizzando il metodo setInt.
    - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto righeInserite.
    - In caso di eccezioni, viene invocato il metodo lostConnection() dell'oggetto DB.
     */

    public void registraRifiutoRichiesta(Richiesta richiesta){
        String query = "UPDATE citizenservices.richieste SET statoRichiesta = -1 WHERE richieste.cod_richiesta = ? ;";
        InterfacciaDB DB= new InterfacciaDB();

        try (Connection con=DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)){
            pst.setInt(1, richiesta.getCod_richiesta());
            int righeInserite = pst.executeUpdate();
        }
        catch (SQLException e) {
            DB.lostConnection();
        }
    }

    public void aggiornaOreRichiedili(int tipo, String matricola){

    }
    public void creaNotificaApprovazioneRichiesta(Richiesta r){

    }

    public void creaNotificaRifiutoRichiesta(Richiesta r){

    }

     /* Il metodo ottieniListaRichiesteInSospeso ecupera le richieste in sospeso (cioè con "statoRichiesta" uguale a 0) da una tabella del database e le memorizza in un'ArrayList di oggetti Richiesta
    - listaRichieste.clear();: Questa riga svuota l'ArrayList listaRichieste per assicurarsi che sia vuoto prima di iniziare a riempirlo con nuovi dati.
    - Viene definita una query che seleziona tutti i campi dalla tabella "richieste", ma solo per le righe in cui il valore della colonna statoRichiesta=0.
    - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
    - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto resultSet (conterrà le righe selezionate dalla query).
    - (blocco while): utilizzato per iterare attraverso le righe dei risultati estratti dalla query SQL e per estrarre i dati da ciascuna riga; All'interno del ciclo while, viene valutata la condizione resultSet.next() (controlla se ci sono ulteriori righe nel ResultSet.)
		-> resultSet.next()=true, esiste una riga successiva nel ResultSet da leggere e viene eseguito il corpo del ciclo:
		   - All'interno del corpo del ciclo, vengono estratti i dati da ciascuna colonna della riga corrente del ResultSet utilizzando i metodi getString() o getInt() in base al tipo di dato.
		   - I dati estratti vengono utilizzati per creare oggetti "Richiesta" corrispondenti.
	       - Gli oggetti creati vengono quindi aggiunti all'ArrayList listaRichieste utilizzando il metodo add().
		   - Il ciclo continua ad iterare attraverso le righe del ResultSet fino a quando non ci sono più righe da elaborare.
		-> Se resultSet.next()=false, significa che tutte le righe sono state lette e non ci sono più righe successive nel ResultSet.
    - Se si verifica un'eccezione SQL durante l'esecuzione della query o il recupero dei dati, il blocco catch viene eseguito.
      In questo caso, il blocco catch invoca il metodo lostConnection() dell'oggetto DB.
    */

    public void ottieniListaRichiesteInSospeso(ArrayList<Richiesta> listaRichieste){
        listaRichieste.clear();
        String query = "SELECT * FROM citizenservices.richieste WHERE statoRichiesta = 0 ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con= DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                int cod_richiesta = resultSet.getInt("cod_richiesta");
                String ref_utente = resultSet.getString("ref_utente");
                int cod_motivo = resultSet.getInt("cod_motivo");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                int statoRichiesta = resultSet.getInt("statoRichiesta");
                String inizioPeriodo = resultSet.getString("inizioPeriodo");
                String finePeriodo = resultSet.getString("finePeriodo");

                Richiesta richiesta = new Richiesta(cod_richiesta, ref_utente, cod_motivo, nome, cognome, statoRichiesta, inizioPeriodo, finePeriodo);
                listaRichieste.add(richiesta);
            }
        }
        catch (SQLException e){
            DB.lostConnection();
        }

    }


        /* Il metodo controlloEsistenzaImpiegato verifica se esiste un impiegato nei dati del database in base alla matricola, al nome e al cognome specificati come parametri. Restituirà true se una corrispondenza è trovata e false se non viene trovata alcuna corrispondenza.
    - Viene definita una query che seleziona i dati nella tabella citizenservices.utenti dove i valori dei campi matricola, nome, e cognome corrispondono ai valori specificati attraverso i segnaposto ?.
    - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
    - Vengono impostati i valori dei parametri nella query utilizzando i metodi setString e setInt.
    - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto resultSet (conterrà le righe selezionate dalla query).
    - return resultSet.next();: Questa riga restituisce true se il ResultSet contiene almeno una riga di dati (cioè, se resultSet.next() restituisce true).
                                In caso contrario, restituirà false. Questo significa che se la query trova una corrispondenza nei dati del database, il metodo restituirà true, altrimenti restituirà false.
    -  Nel caso di eventuali eccezioni SQL che possono verificarsi durante l'esecuzione della query, il metodo restituirà false, indicando che non è stata trovata una corrispondenza.
     */


    public boolean controlloEsistenzaImpiegato(String matricolaImp, String nome, String cognome){
        InterfacciaDB DB = new InterfacciaDB();
        String query = "SELECT * FROM citizenservices.utenti WHERE utenti.matricola = ? AND utenti.nome = ? AND utenti.cognome = ? ;";
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query);)
        {
            pst.setString(1, matricolaImp);
            pst.setString(2, nome);
            pst.setString(3, cognome);
            ResultSet resultSet = pst.executeQuery();
            return resultSet.next();
        }
        catch(SQLException ex){
            return false;
        }
    }

    public int calcoloOreAssenzaDaTurni(String data_inizio, String data_fine){
        return 0;
    }
}
