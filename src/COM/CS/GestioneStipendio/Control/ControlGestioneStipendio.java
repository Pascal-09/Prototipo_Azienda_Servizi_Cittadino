package COM.CS.GestioneStipendio.Control;

import COM.CS.Commons.InterfacciaDB;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.Entity.Stipendio;
import COM.CS.Entity.Turno;
import COM.CS.Entity.Utente;
import COM.CS.Utili.CommonMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class ControlGestioneStipendio {

    /*  Il metodo ottieniListaImpiegatiDaPagare accetta due parametri di tipo ArrayList: uno per gli oggetti di classe "Stipendio" chiamato "stipendi" e l'altro per gli oggetti di classe "Utente" chiamato "impiegati" ed effettua una query al database per ottenere dati dalle tabelle "utenti" e "stipendi" per popolare due liste di oggetti di classe "Stipendio" e "Utente" passate come input al metodo.
    - stipendi.clear() e impiegati.clear(): eliminano eventuali dati preesistenti nelle liste "stipendi" e "impiegati" per assicurarsi che siano vuote all'inizio dell'operazione al fine di preparare le liste a contenere i nuovi dati che verranno recuperati dal dbms.
    - Viene definita una query che seleziona tutti i dati da entrambe le tabelle "stipendi" e "utenti" in cui lo stato del pagamento ("stato" nella tabella "stipendi") è 'da accreditare' e in cui il campo "matricola" nella tabella "utenti" coincide con il campo "ref_matricola" nella tabella "stipendi".
    - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
    - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto ResultSet (conterrà tutte le righe selezionate dalla query).
    - (blocco while): utilizzato per iterare attraverso le righe dei risultati estratti dalla query SQL e per estrarre i dati da ciascuna riga; All'interno del ciclo while, viene valutata la condizione resultSet.next() (controlla se ci sono ulteriori righe nel ResultSet.)
		-> resultSet.next()=true, esiste una riga successiva nel ResultSet da leggere e viene eseguito il corpo del ciclo:
		   - All'interno del corpo del ciclo, vengono estratti i dati da ciascuna colonna della riga corrente del ResultSet utilizzando i metodi getString() o getInt() in base al tipo di dato.
		   -  dati estratti vengono utilizzati per creare oggetti "Stipendio" e tente" corrispondenti.
	       - Gli oggetti creati vengono quindi aggiunti agli array "stipendi" e "impiegati" rispettivamente utilizzando il metodo add().
    - Il ciclo continua ad iterare attraverso le righe del ResultSet fino a quando non ci sono più righe da elaborare.
		-> Se resultSet.next()=false, significa che tutte le righe sono state lette e non ci sono più righe successive nel `ResultSet.
    - All'interno di un blocco "try-catch", le eccezioni di tipo "SQLException" vengono gestite. In caso di errore durante la connessione al database o l'esecuzione della query, viene stampato un messaggio di errore sulla console.

    */

    //PER VISIONARE E ACCREDITARE GLI STIPENDI. NON PER IL CALCOLO
    public void ottieniListaImpiegatiDaPagare(ArrayList<Stipendio> stipendi, ArrayList<Utente> impiegati) {
        stipendi.clear();
        impiegati.clear();
        String query = "SELECT DISTINCT * FROM citizenservices.stipendi JOIN citizenservices.utenti ON utenti.matricola = stipendi.ref_matricola WHERE stipendi.stato = 'da accreditare' ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                String matricola = resultSet.getString("ref_matricola");
                String mese = resultSet.getString("mese");
                int stipendio_base = resultSet.getInt("stipendio_base");
                int ore_serv1 = resultSet.getInt("ore_serv_1");
                int ore_serv2 = resultSet.getInt("ore_serv_2");
                int ore_serv3 = resultSet.getInt("ore_serv_3");
                int ore_serv4 = resultSet.getInt("ore_serv_4");
                int ore_straordinario = resultSet.getInt("ore_straordinario");
                int tasse = resultSet.getInt("tasse");
                String stato = resultSet.getString("stato");
                Stipendio stipendio = new Stipendio(matricola, mese, stipendio_base, ore_serv1, ore_serv2, ore_serv3, ore_serv4, ore_straordinario, tasse, stato);
                stipendi.add(stipendio);
                int cod_contratto = resultSet.getInt("cod_contratto");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                String telefono = resultSet.getString("telefono");
                String IBAN = resultSet.getString("IBAN");
                String mail = resultSet.getString("mail");
                int ruolo = resultSet.getInt("ruolo");
                int stato_contratto = resultSet.getInt("stato_contratto");
                String cod_fiscale = resultSet.getString("cod_fiscale");
                int ore_da_svolgere = resultSet.getInt("ore_da_svolgere");
                Utente impiegato = new Utente(matricola, cod_contratto, nome, cognome, telefono, IBAN, mail, ruolo, stato_contratto, cod_fiscale, ore_da_svolgere);
                impiegati.add(impiegato);
            }
        } catch (SQLException e) {
            System.out.println("ottieni lista impiegati da pagare errore");
        }
    }


    /* Il metodo controlloListaVuota accetta due parametri di tipo ArrayList: uno per gli oggetti di classe "Stipendio" chiamato "stipendi" e l'altro per gli oggetti di classe "Utente" chiamato "impiegati" e restituisce un valore booleano, cioè true o false, che indica se entrambe le liste sono vuote o no:
    - stipendi.isEmpty() controlla se l'ArrayList "stipendi" è vuoto (true se l'ArrayList è privo di elementi, altrimenti false.)
    - impiegati.isEmpty() controlla se l'ArrayList "impiegati" è vuoto (true se l'ArrayList è privo di elementi, altrimenti false.)
    - return stipendi.isEmpty() && impiegati.isEmpty()= true se e solo se entrambi stipendi e impiegati sono vuoti, altrimenti restituirà false.
    */

    public boolean controlloListaVuota(ArrayList<Stipendio> stipendi, ArrayList<Utente> impiegati) {
        return stipendi.isEmpty() && impiegati.isEmpty();
    }

    /*Il metodo aggiornaStatoStipendio prende accetta due parametri di tipo String quali "matricola" ed "IBAN" ed ha lo scopo di aggiornare l'attributo "stato" della tabella stipendi da "da accreditare" ad "accreditato" tramite una specifica query.
    - Viene definita una query che aggiorna (UPDATE) il campo "stato" nella tabella "stipendi" a 'accreditato' per tutte le righe che corrispondono a una data "matricola".
    - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
    - Viene impostato il valore del parametro nella query utilizzando "pst.setString(1, matricola)".
    - Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare il valore specifico fornito attraverso i metodoisetString al segnaposto corrispondente.
    - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto rowsAffected (conterrà tutte le righe aggiornate con la query).
    - Se si verifica un'eccezione di tipo SQLException, viene stampato un messaggio di errore.
    */


    public void aggiornaStatoStipendio(String matricola, String IBAN) {
        String query = "UPDATE citizenservices.stipendi SET stipendi.stato = 'accreditato' WHERE stipendi.ref_matricola = ? ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, matricola);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("errore aggiorna stato stipendio");
        }
    }

    /* Il metodo ottieniStipendioImpiegato accetta due parametri di tipo String quali matricola e mese ed è utilizzato per ottenere le informazioni sullo stipendio di un impiegato in base alla matricola e al mese specificati.
    - Viene definita una query che seleziona tutte le righe della tabella "stipendi" del dbms "citizenservices" dove la colonna "ref_matricola" " della tabella "stipendi" deve essere uguale al valore fornito come argomento "matricola" in input al metodo e la colonna "mese" della tabella "stipendi" deve essere uguale al valore fornito come argomento "mese" in input al metodo.
    - Dopo la creazione di un oggetto interfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
    - Vengono impostati i valori dei parametri nella query utilizzando "pst.setString(1, matricola)" e pst.setString(2, mese);
    - Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare il valore specifico fornito attraverso i metodoisetString al segnaposto corrispondente.
    - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto resultSet (conterrà tutte le righe selezionate dalla la query).
    - Viene poi definito un blocco if-else per controllare se c'è almeno un risultato nella query grazie alla condizione resultSet.next():
        (blocco if): Il metodo next() di resultSet sposta il cursore al primo record restituito dalla query. Se ci sono risultati, verrà eseguito il blocco di codice all'interno di questo if
                -> resultSet.next()=true: esiste almeno una riga che soddisfa le condizioni specificate nella query e quindi viene eseguito il corpo dell'if.  All'interno del corpo del ciclo, vengono estratti i dati da ciascun colonna della riga corrente del ResultSet utilizzando i metodi getString() o getInt() in base al tipo di dato.
		    	- I dati estratti vengono utilizzati per creare oggetti "stato" e "stipendio" corrispondenti.
				- Infine, l'oggetto "Stipendio" appena creato viene restituito come risultato del metodo e conterrà  le informazioni sullo stipendio dell'impiegato per il mese specifico.
        (blocco else) Se resultSet.next()=false, significa che tutte le righe sono state lette e non ci sono più righe successive nel `ResultSet, quindi viene eseguito il corpo dell'else che restituirà "null" per indicare che nessuno stipendio è stato trovato per la matricola e il mese specificati.
     - Se si verifica un'eccezione di tipo SQLException durante l'esecuzione della query, viene catturata e gestita, stampando un messaggio di errore sulla console e restituendo null.
*/

    public Stipendio ottieniStipendioImpiegato(String matricola, String mese) {
        String query = "SELECT * FROM citizenservices.stipendi WHERE stipendi.ref_matricola = ? AND stipendi.mese = ? ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, matricola);
            pst.setString(2, mese);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                int stipendio_base = resultSet.getInt("stipendio_base");
                int ore_serv1 = resultSet.getInt("ore_serv_1");
                int ore_serv2 = resultSet.getInt("ore_serv_2");
                int ore_serv3 = resultSet.getInt("ore_serv_3");
                int ore_serv4 = resultSet.getInt("ore_serv_4");
                int ore_straordinario = resultSet.getInt("ore_straordinario");
                int tasse = resultSet.getInt("tasse");
                String stato = resultSet.getString("stato");
                Stipendio stipendio = new Stipendio(matricola, mese, stipendio_base, ore_serv1, ore_serv2, ore_serv3, ore_serv4, ore_straordinario, tasse, stato);
                return stipendio;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("errore ottieni stipendio impiegato");
            return null;
        }
    }

    /* Il metodo calcolaStipendioMensile permette di calcolare gli stipendi mensili degli impiegati.
    - Vengono create due liste vuote, "impiegatiDaPagare" di classe "Utente"e "turni" di classe "Turno", utilizzando la classe "ArrayList".
    - Viene chiamato il metodo "ottieniListaImpiegatiDaPagare" con il parametro "impiegatiDaPagare" per ottenere una lista di impiegati che devono essere pagati.
    - (ciclo for-each): scorre attraverso ciascun oggetto Utente nella lista impiegatiDaPagare e per ogni elemento della lista effettua le seguenti operazioni:
                        -> ottieniTurniMensili (turni): responsabile di ottenere una lista di turni mensili per ogni elemento della lista impiegatiDaPagare. Questo significa che la lista turni verrà sovrascritta ad ogni iterazione del ciclo.
                        ->Stipendio stipendio = calcoloStipendio(...): Questa istruzione chiama il metodo calcoloStipendio con tre parametri:
                                                                        - La matricola dell'impiegato, ottenuta da impiegato.getMatricola().
                                                                        - Una chiamata a InterfacciaTMP.OttieniStringaMese(), che restituisce una stringa rappresentante il mese corrente.
                                                                        - La lista turni ottenuta precedentemente dal metodo ottieniTurniMensili.
                        -> aggiungiStipendio(stipendio): responsabile dell'inserimento di uno stipendio nel dbms.
    */

    public void calcolaStipendioMensile() {
        ArrayList<Utente> impiegatiDaPagare = new ArrayList<>();
        ArrayList<Stipendio> stipendi = new ArrayList<>();
        ArrayList<Turno> turni = new ArrayList<>();
        ottieniListaImpiegatiStatoAttivo(impiegatiDaPagare);
        for(Utente impiegato : impiegatiDaPagare) {
            ottieniTurniMensili(impiegato, turni);
            if (!turni.isEmpty()){
                Stipendio stipendio = calcoloStipendio(impiegato.getMatricola(), InterfacciaTMP.OttieniStringaMese(),turni);
                stipendi.add(stipendio);
            }
        }
        for(Stipendio stipendio: stipendi){
            aggiungiStipendio(stipendio);
        }

    }

    /* Il metodo cancellaVecchiSalari elimina nel database alcune righe per rimuovere i record dalla tabella stipendi che soddisfano il criterio stato = 'accreditato'. Se l'operazione ha successo, i record vengono cancellati, altrimenti verrà gestita un'eccezione SQL.
        - Viene definita una query che permette di eliminare (DELETE) dalla tabella stipendi del dbms "citizenservices" tutti i record (righe) della tabella stipendi i cui lo stato corrisponde alla stringa "accreditato".
        - Dopo la creazione di un oggetto interfacciaDB e l'apertura di una connessione al dbms. viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
        - La query viene inviata al database per l'esecuzione (non c'è bisogno di memorizzare i risultati della query dal momento che stiamo eliminando delle righe dalla tabella "stipendi").
        - Se si verifica un'eccezione di tipo SQLException, viene stampato un messaggio di errore.
    */

    private void cancellaVecchiSalari(){
        String query = "DELETE * FROM citizenservices.stipendi WHERE stipendi.stato = 'accreditato' ;";
        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query);){
            pst.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("errore");
        }
    }

      /* Il metodo controlloGiornoCalcoloStipendio determina se la data corrente ha il giorno 25 o se è l'ultimo giorno dell'anno (31 dicembre). In caso affermativo, il metodo può effettuare operazioni specifiche come la cancellazione dei vecchi salari.
        - Il metodo "controlloGiornoCalcoloStipendio" accetta un parametro di input "dataOggi," che rappresenta una data nel formato "AAAA-MM-GG" (anno-mese-giorno).
        - String[] data = dataOggi.split("-"): la stringa "dataOggi" viene suddivisa utilizzando il metodo split("-"), che divide la stringa in un array di stringhe utilizzando il carattere "-". Questo serve per estrarre l'anno, il mese e il giorno dalla data fornit
        - String dataMese = data[2] + "-" + data[1]: a variabile dataMese viene creata combinando il giorno e il mese estratti dalla data originale. Questo risultato è una stringa nel formato "GG-MM," che rappresenta il giorno e il mese della data.
        - (blocco if): Viene verificato se la variabile dataMese è uguale alla stringa "31-12". Se questa condizione è vera, viene chiamato il metodo cancellaVecchiSalari(), altrimenti si passa alla condizione successiva.
        - (bloocco if-else): Viene confrontato l'elemento nella terza posizione (indice 2) dell'array data con la stringa "25":
                            - L'array data è stato creato dividendo la data fornita nel formato "AAAA-MM-GG", quindi data[2] rappresenta il giorno estratto dalla data.
                            - Se l'elemento nella terza posizione (indice 2) dell'array="25", allora viene eseguito il corpo dell'if, ovvero il metodo restituirà true.
                            - Se l'elemento non coincide, allora viene eseguito il corpo dell'else, ovvero il metodo restituirà false.
    */

    public boolean controlloGiornoCalcoloStipendio(String dataOggi){
        String[] data = dataOggi.split("-");
        String dataMese = data[2] + "-" + data[1];
        if(dataMese.equals("31-12")){
            cancellaVecchiSalari();
        }
        if(data[2].equals("25")){
            return true;
        }
        else{
            return false;
        }
    }

     /* Il metodo ottieniListaImpiegatiDaPagare estrae informazioni sugli impiegati dal dbms, crea oggetti "Utente" per ciascun impiegato e li aggiunge all'ArrayList impiegatiDaPagare.
     - impiegatiDaPagare.clear(): svuota l'ArrayList impiegatiDaPagare, rimuovendo eventuali elementi presenti in esso (la lista deve essere vuota prima di iniziare a popolarla con nuovi dati)
     - Viene definita una query che seleziona (SELECT) tutte le righe della tablla utenti che hanno stato_contratto=1 (sono attivi) dal dbms "citizenservices".
     - Dopo la creazione di un oggetto interfacciaDB e l'apertura di una connessione al dbms. viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
     - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto resultSet (conterrà tutte le righe selezionate dalla la query).
     -  (blocco while): utilizzato per iterare attraverso le righe dei risultati estratti dalla query SQL e per estrarre i dati da ciascuna riga; All'interno del ciclo while, viene valutata la condizione resultSet.next() (controlla se ci sono ulteriori righe nel ResultSet.)
		-> resultSet.next()=true, esiste una riga successiva nel ResultSet da leggere e viene eseguito il corpo del ciclo:
		   - All'interno del corpo del ciclo, vengono estratti i dati da ciascuna colonna della riga corrente del ResultSet utilizzando i metodi getString() o getInt() in base al tipo di dato.
		   - I dati estratti vengono utilizzati per creare un oggetto "Utente".
	       - Gli oggetti creati vengono quindi aggiunti all' arraylist "impiegatiDaPagare" utilizzando il metodo add().
    - Il ciclo continua ad iterare attraverso le righe del ResultSet fino a quando non ci sono più righe da elaborare.
		-> Se resultSet.next()=false, significa che tutte le righe sono state lette e non ci sono più righe successive nel ResultSet.
    - All'interno del blocco catch vengono gestite eventuali eccezioni SQL e viene stampato un messaggio di errore.
    */

    private void ottieniListaImpiegatiStatoAttivo(ArrayList<Utente> impiegatiDaPagare){
        impiegatiDaPagare.clear();
        String query = "SELECT utenti.* FROM citizenservices.utenti WHERE utenti.stato_contratto = 1 ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
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
                String cod_fiscale = resultSet.getString("cod_fiscale");
                int ore_da_svolgere = resultSet.getInt("ore_da_svolgere");
                Utente utente = new Utente(matr, cod_contratto, nome, cognome, telefono, IBAN, mail, ruolo, stato_contratto, cod_fiscale, ore_da_svolgere);
                impiegatiDaPagare.add(utente);
            }
        }
        catch (SQLException e){
            System.out.println("errore ottieni lista impiegati da pagare");
        }
    }

        /* Il metodo ottieniTurniMensili estrae i turni mensili dal database in base alle date di inizio e fine del mese corrente e li archivia nell'ArrayList turni:
     - turni.clear(): svuota l'ArrayList turni, rimuovendo tutti gli elementi presenti al suo interno (la lista deve essere vuota prima di iniziare a riempirla con nuovi dati).
     - String giorniMese = (..): Viene richiamato il metodo "OttieniMese" dalla classe "InterfacciaTMP" per ottenere una stringa contenente i giorni del mese corrente ed il risultato viene memorizzato nella variabile giorniMese.
     - String giorniMeseFormattato = (..): poiché la stringa dei giorni del mese (ottenuta prima) potrebbe contenere zeri iniziali nei numeri dei giorni, è necessario che questi siano rimossi utilizzando il metodo chiamato "formattaDataTogli0" dalla classe "CommonMethods" .  Il risultato formattato viene memorizzato in giorniMeseFormattato.
     - String[] InizioEFinemese = (..): La stringa dei giorni del mese formattata viene suddivisa in due parti, separando l'inizio e la fine del mese. Queste due parti vengono memorizzate in un array di stringhe "InizioEFinemese"
     - Le variabili inizioMese e fineMese vengono inizializzate con le due parti estratte dall'array InizioEFinemese. Queste due variabili rappresentano la data di inizio e la data di fine del mese corrente.
     - Viene definita una query che permette di selezionare i dati dalla tabella "turni" nel database "citizenservices" per le date comprese tra inizioMese e fineMese, con il codice di tipo presenza 1 o 2.
     - Dopo la creazione di un oggetto interfacciaDB e l'apertura di una connessione al dbms viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
     - Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare il valore specifico fornito attraverso i metodi setString al segnaposto corrispondente.
     - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto resultSet (conterrà tutte le righe selezionate dalla la query).
     - (blocco while): utilizzato per iterare attraverso le righe dei risultati estratti dalla query SQL e per estrarre i dati da ciascuna riga; All'interno del ciclo while, viene valutata la condizione resultSet.next() (controlla se ci sono ulteriori righe nel ResultSet.)
		-> resultSet.next()=true, esiste una riga successiva nel ResultSet da leggere e viene eseguito il corpo del ciclo:
		   - All'interno del corpo del ciclo, vengono estratti i dati da ciascuna colonna della riga corrente del ResultSet utilizzando i metodi getString() o getInt() in base al tipo di dato.
		   - I dati estratti vengono utilizzati per creare un oggetto "Turno".
	       - Gli oggetti creati vengono quindi aggiunti all' arraylist "turni" utilizzando il metodo add().
    - Il ciclo continua ad iterare attraverso le righe del ResultSet fino a quando non ci sono più righe da elaborare.
		-> Se resultSet.next()=false, significa che tutte le righe sono state lette e non ci sono più righe successive nel ResultSet.
    - In caso di eccezione SQL, viene catturata l'eccezione e chiamato il metodo lostConnection() dalla classe InterfacciaDB per gestire la disconnessione dal database.
    */

    private void ottieniTurniMensili(Utente utente, ArrayList<Turno> turni){
        turni.clear();
        String giorniMese = InterfacciaTMP.OttieniMese();
        String giorniMeseFormattato = CommonMethods.formattaDataTogli0(giorniMese);
        String[] InizioEFinemese = giorniMeseFormattato.split(",");
        String inizioMese = InizioEFinemese[0];
        String fineMese = InizioEFinemese[1];
        String query = "SELECT turni.* FROM citizenservices.turni WHERE turni.data >= ? AND turni.data <= ? AND (turni.cod_tipo_presenza = 1 OR turni.cod_tipo_presenza = 2 ) AND turni.ref_matricola = ? ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, inizioMese);
            pst.setString(2,fineMese);
            pst.setString(3,utente.getMatricola());
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                String ref_matricola = resultSet.getString("ref_matricola");
                int cod_servizio = resultSet.getInt("cod_servizio");
                String data = resultSet.getString("data");
                int cod_tipo_presenza = resultSet.getInt("cod_tipo_presenza");
                String ora_inizio = resultSet.getString("ora_inizio");
                String ora_fine = resultSet.getString("ora_fine");
                Turno turno = new Turno(ref_matricola, cod_servizio, data, cod_tipo_presenza, ora_inizio, ora_fine);
                turni.add(turno);
            }
        }
        catch(SQLException e){
            DB.lostConnection();
        }
    }

    /* Il metodo calcoloStipendio prende tre parametri in input matricola, mese e turni (un'ArrayList contenente oggetti Turno) e calcola lo stipendio di un dipendente in base al numero di ore lavorate in diversi nei diversi servizi ed in base alle ore di straordinario effettuate.
     - Vengono inizializzate variabili intere per:
        -> conteggiare il numero di ore lavorate nei quattro diversi tipi di servizio (ore_serv_1, ore_serv_2, ore_serv_3, ore_serv_4).
        -> conteggiare il numero di ore straordinarie (ore_straordinario).
        -> stabilire un valore fisso per le tasse (supponiamo 700).
     - (blocco for each): per ogni oggetto Turno nell'ArrayList il codice esegue le seguenti operazioni:
        -> Si inizia con uno switch che esamina il valore del campo cod_servizio nell'oggetto Turno corrente (turno) per determinare il tipo di servizio associato al turno.
        -> All'interno dello switch i vari casi corrispondono ai diversi valori possibili di cod_servizio (Ad esempio, case 1: corrisponde a quando cod_servizio è uguale a 1).
        -> All'interno di ciascun caso, ad esempio case 1:, c'è un'istruzione che incrementa la variabile corrispondente al tipo di servizio. Ad esempio, ore_serv_1++ aumenta il conteggio delle ore di servizio di tipo 1.
        -> Dopo ogni caso, c'è un'istruzione break che esce dallo caso dello switch e continua con l'iterazione successiva del ciclo for.
     - (blocco if): viene verificato se il campo cod_tipo_presenza dell'oggetto Turno corrente (turno) è uguale a 2 (l'impiegato è in ritardo).
                    -> Se la condizione è vera, vengono incrementate le tasse di 30 (penalità).
     - Alla fine del metodo, viene restituito un nuovo oggetto Stipendio con i parametri specificati, tra cui la matricola, il mese, il salario fisso di 1000, i conteggi delle ore di servizio, il valore delle tasse e uno stato "da accreditare".
    */

    private Stipendio calcoloStipendio(String matricola, String mese, ArrayList<Turno> turni){
        int ore_serv_1 = 0;
        int ore_serv_2 = 0;
        int ore_serv_3 = 0;
        int ore_serv_4 = 0;
        int ore_straordinario = 0;
        int tasse = 200;
        for(Turno turno: turni){
            switch (turno.getCod_servizio()){
                case 1: ore_serv_1++; break;
                case 2: ore_serv_2++; break;
                case 3: ore_serv_3++; break;
                case 4: ore_serv_4++; break;
            }
            if(turno.getCod_tipo_presenza() == 2){
                tasse = tasse + 30;
            }
        }
        return  new Stipendio(matricola, mese, 1000, ore_serv_1, ore_serv_2, ore_serv_3, ore_serv_4, ore_straordinario, tasse, "da accreditare");
    }

    /* Il metodo aggiungiStipendio() permette di aggiungere un oggetto Stipendio al dbms.
     - Viene definita una query che permette di inserire (INSERT INTO) all'interno della tabella "stipendi" del dbms "citizenservices" uno stipendio.
     - Dopo la creazione di un oggetto interfacciaDB e l'apertura di una connessione al dbms viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
     - Vengono impostati i valori dei parametri nella query utilizzando i metodi setString, setInt in base al parametro specificato.
     - Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare il valore specifico fornito attraverso il metodo setString al segnaposto corrispondente.
     - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto rows_updated (conterrà il numero di righe aggiornate con la query).
     - Alla fine del blocco try, il blocco catch(SQLException e) gestisce le eccezioni dovute ad errori durante l'inserimento dei dati nel database e viene stampato un messaggio di errore sulla console.
     */

    private void aggiungiStipendio(Stipendio stipendio){
        String query = "INSERT INTO citizenservices.stipendi(ref_matricola, mese, stipendio_base, ore_serv_1, ore_serv_2, ore_serv_3, ore_serv_4, ore_straordinario, tasse, stato) VALUES(?,?,?,?,?,?,?,?,?,?);";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
             pst.setString(1, stipendio.getRef_matricola());
             pst.setString(2, stipendio.getMese());
             pst.setInt(3, stipendio.getStipendio_base());
             pst.setInt(4,stipendio.getOre_serv_1());
             pst.setInt(5, stipendio.getOre_serv_2());
             pst.setInt(6, stipendio.getOre_serv_3());
             pst.setInt(7, stipendio.getOre_serv_4());
             pst.setInt(8, stipendio.getOre_straordinario());
             pst.setInt(9, stipendio.getTasse());
             pst.setString(10,stipendio.getStato());
             pst.executeUpdate();
        }
        catch(SQLException e){
        }
    }
}
