package COM.CS.GestioneComunicazioni.Control;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.AvvisoSuccesso;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.Utili.DatiUtenteLoggato;
import COM.CS.Commons.InterfacciaDB;
import COM.CS.Entity.Comunicazione;
import COM.CS.Entity.Servizio;
import COM.CS.Entity.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ControlGestioneComunicazioni {

 /* Il metodo mandaComunicazioneATutti accetta come parametro un oggetto di tipo comunicazione ed è responsabile dell'inserimento di una nuova comunicazione nel dbms.
    Viene definita stringa query che permette l'inserimento di una nuova riga nella tabella "comunicazioni" del dbms "citizenservices" (INSERT INTO) e la dichiarazione dei nomi delle colonne della tabella in cui verranno inseriti i dati.
    La query contiene i segnaposto ("?") che rappresentano i valori da inserire nelle colonne.
    Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed i segnaposto vengono sostituiti con i valori specifici forniti tramite il PreparedStatement.
    Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare i valori specifici forniti attraverso i metodi setInt, setString, ecc., ai segnaposto corrispondenti.
    Una volta che tutti i segnaposto sono stati sostituiti con i valori desiderati, la query completa verrà eseguita con pst.executeUpdate(), e i dati saranno inseriti nella tabella "comunicazioni".
    Viene poi effettuato un controllo per verificare se sono state inserite righe (righeInserite > 0) o meno; nel caso positivo viene creato un AvvisoSuccessoe e poi mostrato a video (altrimenti un AvvisoErrore).
    Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException chiamando il metodo lostConnection dell'oggetto DB che gestisce la mancata connessione al dbms.
    */

    public void mandaComunicazioneATutti(Comunicazione comunicazione){
        String query = "INSERT INTO citizenservices.comunicazioni(cod_comunicazione, ref_mittente, ref_destinatario, tipo_comunicazione, testo, orario_invio,oggetto) VALUES (?,?,?,?,?, ?,? )";
        InterfacciaDB DB= new InterfacciaDB();

        try (Connection con=DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)){
            pst.setInt(1, comunicazione.getCod_comunicazione());
            pst.setString(2, comunicazione.getRef_mittente());
            pst.setString(3,comunicazione.getRef_destinatario());
            pst.setInt(4,0);
            pst.setString(5,comunicazione.getTesto());
            pst.setString(6,comunicazione.getOrarioInvio());
            pst.setString(7, comunicazione.getOggetto());
            int righeInserite= pst.executeUpdate();
            if(righeInserite>0){
                AvvisoSuccesso AS = new AvvisoSuccesso("La comunicazione è stata inoltrata con successo");
                AS.pack();
                AS.setVisible(true);
            }else{
                AvvisoErrore AE= new AvvisoErrore("Invio non riuscito, riprova");
                AE.pack();
                AE.setVisible(true);
            }

        } catch (SQLException e) {
            DB.lostConnection();
        }
    }

    /* Il metodo mandaComunicazioneAdmin è analogo al metodo mandaComunicazioneATutti con l'unica differenza che in mandaComunicazioneAdmin si ha:
    - in mandaComunicazioneATutti si ha tipo_comunicazione=0 tramite la riga pst.setInt(4, 0) a voler sottolineare che tipo di comunicazione=1 indica che la comunicazione è destinata all'amministratore.
    - in mandaComunicazioneAdmin si ha tipo_comunicazione=1 tramite la riga pst.setInt(4, 1) a voler sottolineare che tipo di comunicazione=0 indica che la comunicazione è destinata a tutti gli impiegati.
    */

    public void mandaComunicazioneAdmin(Comunicazione comunicazione){
        String query = "INSERT INTO citizenservices.comunicazioni(cod_comunicazione, ref_mittente, ref_destinatario, tipo_comunicazione, oggetto, testo, orario_invio) VALUES (?,?,?,?,?,?, ? )";
        InterfacciaDB DB= new InterfacciaDB();

        try (Connection con=DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)){
            pst.setInt(1, comunicazione.getCod_comunicazione());
            pst.setString(2, comunicazione.getRef_mittente());
            pst.setString(3,comunicazione.getRef_destinatario());
            pst.setInt(4,1);
            pst.setString(5, comunicazione.getOggetto());
            pst.setString(6,comunicazione.getTesto());
            pst.setString(7,comunicazione.getOrarioInvio());


            int righeInserite= pst.executeUpdate();
            if(righeInserite>0){
                AvvisoSuccesso AS = new AvvisoSuccesso("La comunicazione è stata inoltrata con successo");
                AS.pack();
                AS.setVisible(true);
            }else{
                AvvisoErrore AE= new AvvisoErrore("Invio non riuscito, riprova");
                AE.pack();
                AE.setVisible(true);
            }

        } catch (SQLException e) {
            DB.lostConnection();
        }
    }

    /* Il metodo ottieniComunicazioniImpiegato serve a recuperare le comunicazioni destinate destinate a tutti gli impiegati (oppure ad un impiegato specifico) da una tabella del dbms e memorizzarle in un oggetto ArrayList di oggetti Comunicazione.
    inizialmente la listaComunicazioniImpiegato potrebbe contenere dati precedenti (o essere vuota); l'istruzione listaComunicazioniImpiegato.clear() rimuove tutti gli elementi correnti presenti nella lista, lasciandola vuota;
    questo serve a preparare la lista per contenere i nuovi dati che saranno recuperati dal dbms, rimuovendo tutti i dati precedenti che potrebbero essere stati presenti nella lista.
    Viene definita una query che seleziona le righe della tabella "comunicazioni" del dbms "citizenservices" (SELECT FROM-WHERE) tramite criteri di ricerca specifici.
    La query include una clausola WHERE costituita da 2 parti separate da OR, con ogni parte che definisce un criterio di ricerca diverso (cerchiamo le righe che soddisfano almeno uno dei due criteri)
    La query con clausola WHERE estrae comunicazioni in base a tre condizioni (righe che soddisfano almeno una delle condizioni specificate verranno incluse nei risultati della query.)
    - 1°) comunicazioni.ref_destinatario = ?,  Comunicazioni destinate alla matricola specifica fornita come parametro.
    - 2°) comunicazioni.tipo_comunicazione = ?, Comunicazioni con tipo 0, ovvero destinate a tutti gli impiegati (perché non stiamo specificando il parametro)

    La query contiene i segnaposto ("?") che rappresentano i valori da inserire nelle colonne.
    Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed i segnaposto vengono sostituiti con i valori specifici forniti tramite il PreparedStatement ("matricola" ed il valore "0") utilizzando i metodi setString(1, matricola) e setInt(2, 0).
    Viene eseguita la query di selezione utilizzando pst.executeQuery(), ed il risultato viene memorizzato in un oggetto ResultSet (conterrà le righe corrispondenti alle comunicazioni trovate)
    Viene poi eseguito un ciclo while (resultSet.next()) per scorrere tutte le righe risultanti al fine di estrarre per ogni riga  i dati relativi ai campi cod_comunicazione, ref_mittente, ref_destinatario, tipo_comunicazione, oggetto, testo, e orario_invio utilizzando i metodi getInt, getString, ecc. del ResultSet.
    Per ogni riga trovata, viene creato un oggetto Comunicazione con i dati estratti e questo oggetto viene aggiunto alla listaComunicazioniImpiegato utilizzando il metodo add(comunicazione).
    Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException chiamando il metodo lostConnection dell'oggetto DB che gestisce la mancata connessione al dbms.
    */


    public void ottieniComunicazioniImpiegato(String matricola, ArrayList<Comunicazione> listaComunicazioniImpiegato){
        listaComunicazioniImpiegato.clear();
        String query = "SELECT * FROM citizenservices.comunicazioni WHERE comunicazioni.ref_destinatario = ? OR comunicazioni.tipo_comunicazione = ?;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)
        ) {
            pst.setString(1, matricola);
            pst.setInt(2, 0);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                int cod_comunicazione = resultSet.getInt("cod_comunicazione");
                String ref_mittente = resultSet.getString("ref_mittente");
                String ref_destinatario = resultSet.getString("ref_destinatario");
                int tipo_comunicazione = resultSet.getInt("tipo_comunicazione");
                String oggetto = resultSet.getString("oggetto");
                String testo = resultSet.getString("testo");
                String orario_invio=resultSet.getString("orario_invio");
                Comunicazione comunicazione = new Comunicazione(cod_comunicazione, ref_mittente, ref_destinatario, tipo_comunicazione, oggetto, testo, orario_invio);
                listaComunicazioniImpiegato.add(comunicazione);
            }
        } catch (SQLException e) {
            DB.lostConnection();
        }
    }



/* Il metodo ottieniComunicazioniAdmin permette di cuperare le comunicazioni destinate a un amministratore (o utente con matricola specifica) da una tabella di database e memorizzarle in un oggetto ArrayList di oggetti Comunicazione.
    inizialmente la listaComunicazioniAdmin potrebbe contenere dati precedenti (o essere vuota); l'istruzione listaComunicazioniAdmin.clear() rimuove tutti gli elementi correnti presenti nella lista, lasciandola vuota;
    questo serve a preparare la lista per contenere i nuovi dati che saranno recuperati dal dbms, rimuovendo tutti i dati precedenti che potrebbero essere stati presenti nella lista.
    Viene definita una query che seleziona le righe della tabella "comunicazioni" del dbms "citizenservices" (SELECT FROM-WHERE) tramite criteri di ricerca specifici.
    La query include una clausola WHERE costituita da 3 parti separate da OR, con ogni parte che definisce un criterio di ricerca diverso (cerchiamo le righe che soddisfano almeno uno dei tre criteri)
    La query con clausola WHERE estrae le comunicazioni in base a tre condizioni (righe che soddisfano almeno una delle condizioni specificate verranno incluse nei risultati della query):
    - 1°) comunicazioni.ref_destinatario = ?: Comunicazioni destinate alla matricola specifica fornita come parametro.
    - 2°) comunicazioni.tipo_comunicazione = 0: Comunicazioni con tipo 0, ovvero destinate a tutti gli impiegati.
    - 3°) comunicazioni.tipo_comunicazione = 1: Comunicazioni con tipo 1, ovvero destinate all'amministratore
    La query contiene i segnaposto ("?") che rappresentano i valori da inserire nelle colonne.
    Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed il segnaposto viene sostituito con il valore specifico della matricola fornito tramite il PreparedStatement utilizzando il metodo setString(1, matricola)
    Viene eseguita la query di selezione utilizzando pst.executeQuery(), ed il risultato viene memorizzato in un oggetto ResultSet (conterrà le righe corrispondenti alle comunicazioni trovate)
    Viene poi eseguito un ciclo while (resultSet.next()) per scorrere tutte le righe risultanti al fine di estrarre per ogni riga  i dati relativi ai campi cod_comunicazione, ref_mittente, ref_destinatario, tipo_comunicazione, oggetto, testo, e orario_invio utilizzando i metodi getInt, getString, ecc. del ResultSet.
    Per ogni riga trovata, viene creato un oggetto Comunicazione con i dati estratti e questo oggetto viene aggiunto alla listaComunicazioniAdmin utilizzando il metodo add(comunicazione).
    Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException chiamando il metodo lostConnection dell'oggetto DB che gestisce la mancata connessione al dbms.
*/

    public void ottieniComunicazioniAdmin(String matricola, ArrayList<Comunicazione> listaComunicazioniAdmin){
        listaComunicazioniAdmin.clear();

        String query = "SELECT * FROM citizenservices.comunicazioni WHERE comunicazioni.ref_destinatario = ? OR comunicazioni.tipo_comunicazione = 0 OR comunicazioni.tipo_comunicazione = 1;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)
        ) {
            pst.setString(1, matricola);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                int cod_comunicazione = resultSet.getInt("cod_comunicazione");
                String ref_mittente = resultSet.getString("ref_mittente");
                String ref_destinatario = resultSet.getString("ref_destinatario");
                int tipo_comunicazione = resultSet.getInt("tipo_comunicazione");
                String oggetto = resultSet.getString("oggetto");
                String testo = resultSet.getString("testo");
                String orario_invio=resultSet.getString("orario_invio");
                Comunicazione comunicazione = new Comunicazione(cod_comunicazione, ref_mittente, ref_destinatario, tipo_comunicazione, oggetto, testo, orario_invio);
                listaComunicazioniAdmin.add(comunicazione);
            }
        } catch (SQLException e) {
            DB.lostConnection();
        }
    }

    /* Il metodo isListaComunicazioniVuota e accetta un parametro listaComunicazioni di tipo ArrayList<Comunicazione> per stabilire se l'ArrayList passato come argomento è vuoto o no tramite la restituzione di un valore booleano.
        Il metodo isEmpty() dell'oggetto ArrayList verifica se l'elenco è vuoto o no tramite un valore booleano:
         - true) se l'ArrayList è vuoto.
         - false) se l'ArrayList ha almeno un elemento.
        L'operatore (!) deriva dai sequence (***ATTENZIONARE IL PUNTO ESCLAMATIVO***).
     */

    public boolean isListaComunicazioniVuota(ArrayList<Comunicazione> listaComunicazioni) {
        return !listaComunicazioni.isEmpty();
    }


/* Il metodo aggiornaServiziAttivi() ha lo scopo di aggiornare lo stato di alcuni servizi nel dbms "citizenservices" in modo che siano contrassegnati come "attivi" in base a determinate condizioni temporali.
    La 1° istruzione chiama il metodo ottieniOrario() dalla classe InterfacciaTMP e divide la stringa risultante in un array di stringhe utilizzando il carattere "-" come delimitatore. L'obiettivo è ottenere i componenti dell'orario, e orario[0] rappresenta l'ora di inizio.
    La 2° istruzione definisce la creazione di una stringa oraInizio aggiungendo "0-0" all'ora di inizio precedentemente ottenuta.
    La 3° riga crea un oggetto Calendar con la data e l'orario correnti. La chiamata a Calendar.getInstance() restituisce un'istanza del calendario corrente, inizializzata con la data e l'orario attuali del sistema.
    L'oggetto calendar è utilizzato per ottenere il valore dell'anno corrente con calendar.get(Calendar.YEAR), il mese corrente con calendar.get(Calendar.MONTH), il giorno del mese corrente con calendar.get(Calendar.DAY_OF_MONTH).
    Quindi viene estratto l'anno, il mese e il giorno correnti e combinati per creare una stringa data nel formato "giorno-mese-anno".

    Viene creata una query che permette di eseguire l'aggiornamento (UPDATE) di apertura sulla tabella "servizi" del dbms "citizenservices"; questa query è strutturata in tre parti:
    1°) JOIN citizenservices.turni ON servizi.cod_servizio = turni.cod_servizio: (specifica un'operazione di JOIN) collega le rige della tabella "servizi" alle righe della tabella "turni" in base ad una condizione: i valori nella colonna "cod_servizio" in entrambe le tabelle devono essere uguali. (LE RIGHE CON LO STESSO COD_SERVIZIO sono collegate tra loro)
    2°) SET servizi.stato = 'attivo': (specifica cosa vogliamo aggiornare) Impostiamo la colonna "stato" nella tabella "servizi" ad 'attivo' per le righe risultanti dal JOIN effettuato nella fase precedente. Quindi, tutte le righe collegate alla tabella "turni" con la stessa chiave "cod_servizio" saranno contrassegnate come "attive".
    3°) WHERE turni.data = ? AND turni.ora_inizio = ?: (specifica la clausola WHERE che definisce le condizioni in base alle quali le righe vengono aggiornate). Le condizioni sono:ù
        turni.data = ? confronta il valore nella colonna "data" della tabella "turni" con un valore specifico, rappresentato dal parametro data che verrà passato in seguito nel codice.
        turni.ora_inizio = ?  confronta il valore nella colonna "ora_inizio" della tabella "turni" con un valore specifico, rappresentato dal parametro oraInizio che verrà passato in seguito nel codice.

    COSA FA LA QUERY: aggiorna il campo "stato" nella tabella "servizi" ad 'attivo' per tutte le righe in cui esiste una corrispondenza tra le tabelle "servizi" e "turni" basata sul valore della colonna "cod_servizio".
                      Questa corrispondenza è limitata dalle condizioni specificate nella clausola WHERE, che richiedono anche una corrispondenza tra la colonna "data" e il parametro data, nonché tra la colonna "ora_inizio" e il parametro oraInizio.
                      In questo modo, solo le righe che soddisfano tutte queste condizioni saranno contrassegnate come "attive".
    Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed i segnaposto vengono sostituiti con i valori forniti tramite il PreparedStatement utilizzando i metodi pst.setString(1, data) e pst.setString(2, oraInizio). Il primo segnaposto viene impostato con la data estratta ed il secondo segnaposto con oraInizio.
    Una volta che tutti i segnaposto sono stati sostituiti con i valori desiderati, la query completa verrà eseguita con pst.executeUpdate() e lo stato dei servizi verrà aggiornato.
    Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException chiamando il metodo lostConnection dell'oggetto DB che gestisce la mancata connessione al dbms.
*/


    public void aggiornaServiziAttivi() {
        String[] orario = InterfacciaTMP.ottieniOrario().split(":");
        String oraInizio = orario[0] + ":0:0";
        Calendar calendar = Calendar.getInstance();
        int anno = calendar.get(Calendar.YEAR);
        int mese = calendar.get(Calendar.MONTH) + 1;
        int giorno = calendar.get(Calendar.DAY_OF_MONTH);
        String data = anno + "-" + mese + "-" + giorno;

        String query = "UPDATE citizenservices.servizi " +
                "JOIN citizenservices.turni ON servizi.cod_servizio = turni.cod_servizio " +
                "SET " +
                "servizi.stato = 'attivo' WHERE turni.data = ? AND turni.ora_inizio = ? ;";

        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, data);
            pst.setString(2,oraInizio);
            pst.executeUpdate();
        }
        catch (SQLException e) {
            DB.lostConnection();
        }
    }

    /* Il metodo aggiornaServiziChiusi() ha lo scopo di aggiornare lo stato di alcuni servizi nel dbms "citizenservices" in modo che siano contrassegnati come "attivi" in base a determinate condizioni temporali.
    La 1° istruzione chiama il metodo ottieniOrario() dalla classe InterfacciaTMP e divide la stringa risultante in un array di stringhe utilizzando il carattere "-" come delimitatore. L'obiettivo è ottenere i componenti dell'orario, e orario[0] rappresenta l'ora di inizio.
    La 2° istruzione definisce la creazione di una stringa oraInizio aggiungendo "0-0" all'ora di inizio precedentemente ottenuta.
    La 3° riga crea un oggetto Calendar con la data e l'orario correnti. La chiamata a Calendar.getInstance() restituisce un'istanza del calendario corrente, inizializzata con la data e l'orario attuali del sistema.
    L'oggetto calendar è utilizzato per ottenere il valore dell'anno corrente con calendar.get(Calendar.YEAR), il mese corrente con calendar.get(Calendar.MONTH), il giorno del mese corrente con calendar.get(Calendar.DAY_OF_MONTH).
    Quindi viene estratto l'anno, il mese e il giorno correnti e combinati per creare una stringa data nel formato "giorno-mese-anno".
    Viene creata una query che permette di eseguire l'aggiornamento (UPDATE) di chiusura sulla tabella "servizi" del dbms "citizenservices"; questa query è strutturata in tre parti:
    1°) SET stato = 'chiuso': specifica quale colonna deve essere aggiornata e con quale valore. In questo caso, il campo "stato" verrà impostato su 'chiuso' per i record che soddisfano le condizioni specificate successivamente.
    2°) WHERE cod_servizio NOT IN (...): clausola che impone una condizione per determinare quali record verranno aggiornati.
        - L'uso di NOT IN (...) indica che verranno selezionati i record che NON si trovano nell'insieme di risultati generato dalla sottoquery tra parentesi.
        - In questo caso, si stanno cercando i record in cui il campo cod_servizio non è incluso nell'insieme risultante da una sottoquery. Quindi, verranno aggiornati i record in cui il campo cod_servizio non ha una corrispondenza nella sottoquery.
    3°) SELECT cod_servizio FROM citizenservices.turni ...: sottoquery che seleziona la colonna cod_servizio dalla tabella turni all'interno del dbms. Verrà utilizzata per identificare i servizi da escludere dall'aggiornamento.
    4°) WHERE turni.data = ? AND turni.ora_inizio = ?: filtra i record nella tabella citizenservices.turni in base a due condizioni:
        - turni.data = ?: condizione che cerca i record in cui il valore della colonna data nella tabella citizenservices.turni corrisponde al valore fornito tramite il parametro data.
        - turni.ora_inizio = ?: condizione che cerca i record in cui il valore della colonna ora_inizio nella tabella citizenservices.turni corrisponde al valore fornito tramite il parametro oraInizio.
        - La clausola WHERE serve ad identificare i record nella tabella turni che hanno una data e un'ora di inizio specifici in modo che i record nella tabella servizi possano essere aggiornati in base a questa corrispondenza (o meno).
    COSA FA LA QUERY: aggiorna il campo "stato" nella tabella citizenservices.servizi a 'chiuso' per tutti i servizi che non hanno una corrispondenza nella tabella citizenservices.turni con una specifica data e ora di inizio.
    Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed i segnaposto vengono sostituiti con i valori forniti tramite il PreparedStatement utilizzando pst.setString(1, data) e pst.setString(2, oraInizio). Il primo segnaposto viene impostato con la data estratta ed il secondo segnaposto con oraInizio.
    Una volta che tutti i segnaposto sono stati sostituiti con i valori desiderati, la query completa verrà eseguita con pst.executeUpdate() e lo stato dei servizi verrà aggiornato.
    Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException chiamando il metodo lostConnection dell'oggetto DB che gestisce la mancata connessione al dbms.
    */



    public void aggiornaServiziChiusi(){
        String[] orario = InterfacciaTMP.ottieniOrario().split(":");
        String oraInizio = orario[0] + ":0:0";
        Calendar calendar = Calendar.getInstance();
        int anno = calendar.get(Calendar.YEAR);
        int mese = calendar.get(Calendar.MONTH) + 1;
        int giorno = calendar.get(Calendar.DAY_OF_MONTH);
        String data = anno + "-" + mese + "-" + giorno;

        String query = "UPDATE citizenservices.servizi " +
                "SET stato = 'chiuso' " +
                "WHERE cod_servizio NOT IN (SELECT cod_servizio FROM citizenservices.turni WHERE turni.data = ? AND turni.ora_inizio = ? );";

        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, data);
            pst.setString(2,oraInizio);
            pst.executeUpdate();
        }
        catch (SQLException e) {
            DB.lostConnection();
        }
    }


    /* Il metodo ottieniDettagliServizi recupera i dettagli dei servizi dal dbms, li immagazzina in oggetti Servizio e li inserisce nell'ArrayList listaServizi.
    La 1° istruzione svuota l'ArrayList listaServizi in modo che eventuali dati preesistenti all'interno di esso vengano rimossi prima di inserire nuovi dati.
    Viene creata una query per selezionare TUTTI i dati dalla tabella "servizi" nel database "citizenservices" (tramite SELECT).
    Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.

    Una volta che tutti i segnaposto sono stati sostituiti con i valori desiderati, la query completa verrà eseguita con pst.executeUpdate() e lo stato dei servizi verrà aggiornato.
    Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException chiamando il metodo lostConnection dell'oggetto DB che gestisce la mancata connessione al dbms.
    L'istruzione SQL viene eseguita e il risultato viene memorizzato in un oggetto di tipo ResultSet.
    Il codice entra in un ciclo while che scorre tutte le righe del ResultSet:
    - int cod_servizio = resultSet.getInt("cod_servizio");: In ogni iterazione del ciclo, i valori della colonna "cod_servizio" vengono estratti dal ResultSet e memorizzati nella variabile cod_servizio come un intero.
    - String nome_servizio = resultSet.getString("nome_servizio");: Il valore nella colonna "nome_servizio" viene estratto e memorizzato nella variabile nome_servizio come una stringa.
    - String stato = resultSet.getString("stato");: Il valore nella colonna "stato" viene estratto e memorizzato nella variabile stato come una stringa.
    Successivamente con i dati estratti dal ResultSet, viene creato un oggetto Servizio.
    l'oggetto Servizio creato viene aggiunto all'ArrayList listaServizi, popolando così la lista con i dati recuperati dal database.
    Alla fine del ciclo while, tutti i dati sono stati estratti dal database e inseriti nell'ArrayList listaServizi.
    Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException chiamando il metodo lostConnection dell'oggetto DB che gestisce la mancata connessione al dbms.
    */


    public void ottieniDettagliServizi(ArrayList<Servizio> listaServizi) {
        listaServizi.clear();
        String query = "SELECT * FROM citizenservices.servizi";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);){
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                int cod_servizio = resultSet.getInt("cod_servizio");
                String nome_servizio = resultSet.getString("nome_servizio");
                String stato = resultSet.getString("stato");

                Servizio servizio = new Servizio(cod_servizio, nome_servizio, stato);
                listaServizi.add(servizio);
            }

        } catch (SQLException e) {
            DB.lostConnection();
        }
    }

    /* Il metodo ottieniImpiegatiAssegnatiAlServizio  consente di ottenere un elenco di impiegati assegnati a un servizio specifico da un database.
    La 1°  istruzione All'inizio usa il metodo clear() sull'oggetto impiegatiAssegnati,ArrayList di oggetti Utente per eliminare tutti i dati precedenti nell'ArrayList e prepararsi a popolarlo con nuovi dati.
    La 2° istruzione definisce una query che è strutturata in diverse parti:
    1°) SELECT U.*: seleziona tutte le colonne (*) dalla tabella degli utenti (utenti) nel database. Ciò significa che la query restituirà tutte le informazioni relative agli utenti assegnati al servizio specifico.
    2°) FROM citizenservices.utenti AS U: Definisce la tabella da cui vengono estratti i dati. La tabella è denominata "utenti" ed è preceduta da un alias U che consente di fare riferimento alla tabella in modo più conciso nel resto della query.
    3°) JOIN citizenservices.turni AS T ON U.matricola = T.ref_matricola: collega gli utenti ai loro turni associati.
    4°) JOIN citizenservices.servizi AS S ON T.cod_servizio = S.cod_servizio: collega i turni ai servizi a cui sono assegnati.
    5°) WHERE S.cod_servizio = ? AND T.data = ? AND T.ora_inizio = ?: Questa clausola WHERE aggiunge condizioni alla query. Le condizioni specificano che i risultati devono soddisfare tre criteri:
        - Il campo cod_servizio nella tabella dei servizi (S) deve essere uguale al valore passato come parametro (codiceServizio) nella funzione.
        - Il campo data nella tabella dei turni (T) deve essere uguale al valore passato come parametro (data) nella funzione.
        - Il campo ora_inizio nella tabella dei turni (T) deve essere uguale al valore passato come parametro (orario) nella funzione.
   COSA FA LA QUERY: Questa query SQL seleziona tutti i campi dalla tabella degli utenti (utenti) per gli utenti che sono assegnati a un servizio specifico (identificato dal parametro codiceServizio), in un giorno specifico (identificato dal parametro data) e in un orario specifico (identificato dal parametro orario).
                     Gli utenti selezionati sono quelli che soddisfano tutte e tre le condizioni specificate nella clausola WHERE.
   All'interno di un ciclo while, il codice utilizza i risultati estratti e li utilizza per creare oggetti Utente che verranno iterativamente aggiunti all'ArrayList impiegatiAssegnati.
   Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException chiamando il metodo lostConnection dell'oggetto DB che gestisce la mancata connessione al dbms.
    */


    public void ottieniImpiegatiAssegnatiAlServizio(int codiceServizio, ArrayList<Utente> impiegatiAssegnati) {
        impiegatiAssegnati.clear();
        String query =  "SELECT U.* " +
                        "FROM citizenservices.utenti AS U " +
                        "JOIN citizenservices.turni AS T ON U.matricola = T.ref_matricola " +
                        "JOIN citizenservices.servizi AS S ON T.cod_servizio = S.cod_servizio " +
                        "WHERE S.cod_servizio = ? AND T.data = ? AND T.ora_inizio = ? ;";

        Calendar calendar = Calendar.getInstance();
        int anno = calendar.get(Calendar.YEAR);
        int mese = calendar.get(Calendar.MONTH) + 1;
        int giorno = calendar.get(Calendar.DAY_OF_MONTH);
        String data = anno + "-" + mese + "-" + giorno;
        String orario = calendar.get(Calendar.HOUR_OF_DAY) + ":0:0";

        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, codiceServizio);
            pst.setString(2,data);
            pst.setString(3, orario);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                String matricola = resultSet.getString("matricola");
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
                impiegatiAssegnati.add(impiegato);
            }

        } catch (SQLException e) {
            //si restituisce una lista vuota
        }
    }
}
