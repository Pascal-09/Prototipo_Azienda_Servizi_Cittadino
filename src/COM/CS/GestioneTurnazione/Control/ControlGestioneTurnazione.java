package COM.CS.GestioneTurnazione.Control;

import COM.CS.Commons.InterfacciaDB;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.Entity.Richiesta;
import COM.CS.Entity.Turno;
import COM.CS.Entity.Utente;
import COM.CS.Utili.CommonMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ControlGestioneTurnazione {

    /* Il metodo controlloGiornoCalcoloTurnazione verifica se una data specificata passata come input al metodo è una delle quattro date: 16 Ottobre, 16 Gennaio, 16 Aprile o 16 Luglio.
        - String[] dataSplit = data.split("-"): la stringa data viene suddivisa in parti separate utilizzando il carattere -.
                                                La data è fornita nel formato "AAAA-MM-GG", quindi questa operazione divide la data in tre parti: l'anno, il mese e il giorno.
        - String giorno = dataSplit[2]. La variabile giorno è inizializzata con il giorno estratto dalla data.
        - String mese = dataSplit[1]:  La variabile mese è inizializzata con il mese estratto dalla data.
        - (blocco if): verifica se il giorno è "16" e se il mese è uno dei quattro mesi specificati:
                       -> Ottobre (10), Gennaio (01), Aprile (04) o Luglio (07).
                       -> Se entrambe le condizioni sono verificate, il metodo restituirà true, indicando che la data corrisponde a una delle date specificate.
                       -> Altrimenti, restituirà false.
     */

    public boolean controlloGiornoCalcoloTurnazione(String data) {
        // 16 Ottobre, 16 Gennaio, 16 Aprile, 16 Luglio
        String[] dataSplit = data.split("-");
        String giorno = dataSplit[2];
        String mese = dataSplit[1];
        return giorno.equals("16") && (mese.equals("10") || mese.equals("01") || mese.equals("04") || mese.equals("07"));
    }

    /* Il metodo ottieniImpiegatiConStatoAttivo prende in input l'arraylist listaImpiegati di oggetti di classe Utente per ottenere gli impiegati con stato di contratto attivo (stato_contratto=1) dal dbmds e memorizzarli nella lista listaImpiegati.
     - Viene definita una query che seleziona tutti i campi dalla tabella "utenti" nel dbms "citizenservices" in cui si verificano le seguenti condizioni: Lo "stato_contratto"=1, Il "ruolo" !=-1, Il "ruolo" != 0.
     - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
     - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto ResultSet (conterrà tutte le righe selezionate dalla query).
     - (blocco while): utilizzato per iterare attraverso le righe dei risultati estratti dalla query SQL e per estrarre i dati da ciascuna riga; All'interno del ciclo while, viene valutata la condizione resultSet.next() (controlla se ci sono ulteriori righe nel ResultSet.)
		-> resultSet.next()=true, esiste una riga successiva nel ResultSet da leggere e viene eseguito il corpo del ciclo:
		   - All'interno del corpo del ciclo, vengono estratti i dati da ciascuna colonna della riga corrente del ResultSet utilizzando i metodi getString() o getInt() in base al tipo di dato.
		   - I dati estratti vengono utilizzati per creare oggetti "Utente" corrispondenti.
	       - Gli oggetti creati vengono quindi aggiunti all' array listaImpiegati utilizzando il metodo add().
		   - Il ciclo continua ad iterare attraverso le righe del ResultSet fino a quando non ci sono più righe da elaborare.
		-> Se resultSet.next()=false, significa che tutte le righe sono state lette enon ci sono più righe successive nel ResultSet.

    - Se si verifica un'eccezione di tipo SQLException durante l'esecuzione della query, viene catturata e gestita, stampando un messaggio di errore sulla console.
    */

    public void ottieniImpiegatiConStatoAttivo(ArrayList<Utente> listaImpiegati) {
        listaImpiegati.clear();
        String query = "SELECT utenti.* FROM citizenservices.utenti WHERE utenti.stato_contratto = 1 AND utenti.ruolo != -1 AND utenti.ruolo != 0 ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
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
                listaImpiegati.add(utente);
            }
        } catch (SQLException e) {
            System.out.println("errore ottieni impiegati");
            DB.lostConnection();
        }
    }


    /* Il metodo ottieniListaPeriodiAstensioneApprovati  ottiene una lista di periodi di astensione approvati dalle richieste presenti nel database e le memorizza in un'ArrayList di oggetti "Richiesta".
       - Viene definita una query che seleziona tutte le colonne dalla tabella richieste per le righe che soddisfano le condizioni specificate nel JOIN e nella clausola WHERE:
         estrarrà solo le righe che corrispondono a richieste con uno stato di 1 nella tabella richieste, ma solo se l'utente associato a quella richiesta ha un valore di ruolo diverso da 0 e -1 nella tabella utenti.
       - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
       - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto ResultSet (conterrà tutte le righe selezionate dalla query).
       - (blocco while): utilizzato per iterare attraverso le righe dei risultati estratti dalla query SQL e per estrarre i dati da ciascuna riga; All'interno del ciclo while, viene valutata la condizione resultSet.next() (controlla se ci sono ulteriori righe nel ResultSet.)
		-> resultSet.next()=true, esiste una riga successiva nel ResultSet da leggere e viene eseguito il corpo del ciclo:
		   - All'interno del corpo del ciclo, vengono estratti i dati da ciascuna colonna della riga corrente del ResultSet utilizzando i metodi getString() o getInt() in base al tipo di dato.
		   - I dati estratti vengono utilizzati per creare oggetti "Richiesta" corrispondenti.
	       - Gli oggetti creati vengono quindi aggiunti all' array Richiesta utilizzando il metodo add().
		   - Il ciclo continua ad iterare attraverso le righe del ResultSet fino a quando non ci sono più righe da elaborare.
		-> Se resultSet.next()=false, significa che tutte le righe sono state lette e non ci sono più righe successive nel ResultSet.
      - Se si verifica un'eccezione di tipo SQLException durante l'esecuzione della query, viene catturata e gestita, stampando un messaggio di errore sulla console.
    */

    public void ottieniListaPeriodiAstensioneApprovati(ArrayList<Richiesta> listaRichieste) {
        listaRichieste.clear();
        String query = "SELECT richieste.* FROM citizenservices.richieste JOIN citizenservices.utenti ON  utenti.matricola = richieste.ref_utente WHERE utenti.ruolo != 0 AND utenti.ruolo != -1 AND richieste.statoRichiesta = 1 ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
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
        } catch (SQLException e) {
            System.out.println("errore ottieni periodi di astensione approvati");
            DB.lostConnection();
        }
    }


     /* Il metodo haRichiesteApprovate  riceve tre parametri: un oggetto Utente, un'ArrayList di oggetti Richiesta e una stringa data per esaminare una lista di richieste e verificare se l'utente specifico ha richieste approvate per una data specifica.
        - Il metodo restituisce true se trova almeno una richiesta che soddisfa tutte le condizioni.
        - Altrimenti restituisce false.
     - SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"): Viene creato un oggetto SimpleDateFormat per formattare e analizzare le date nel formato "yyyy-MM-dd".
     - (blocco try): Inizia un blocco try per gestire eventuali eccezioni.
        - la stringa data viene analizzata per creare un oggetto Date chiamato dataRichiesta utilizzando il formato specificato in dateFormat.
        - (blocco for-each):  itera su ogni oggetto Richiesta nella lista listaRichieste:
                            - Dentro il ciclo, vengono eseguite diverse condizioni per determinare se una richiesta soddisfa i criteri per essere considerata "approvata" per la data specificata.
                              Nel dettaglio, affinché una richiesta sia "approvata" è necessario che:
                                - La matricola dell'utente (utente.getMatricola()) è uguale alla matricola associata alla richiesta (richiesta.getRef_utente()).
                                - La dataRichiesta deve essere >= data di inizio periodo della richiesta (richiesta.getInizioPeriodo()).
                                - La dataRichiesta deve essere <= data di fine periodo della richiesta (richiesta.getFinePeriodo())
                            - Se tutte le condizioni all'interno dell'istruzione if sono soddisfatte per almeno una delle richieste nella lista, il metodo restituisce true, indicando che l'utente ha richieste approvate per la data specificata.
       - Se si verifica un errore durante la conversione delle date, viene catturata un'eccezione nell'istruzione catch (Exception e), e verrà stampato un messaggio di errore con e.printStackTrace().
       - e.printStackTrace(): Stampa le informazioni sull'eccezione compreso il messaggio di errore sul flusso di output.
       - Se nessuna delle richieste soddisfa le condizioni sopra descritte, il metodo alla fine restituirà false.
    */

    public boolean haRichiesteApprovate(Utente utente, ArrayList<Richiesta> listaRichieste, String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dataRichiesta = dateFormat.parse(data);

            for (Richiesta richiesta : listaRichieste) {
                if (utente.getMatricola().equals(richiesta.getRef_utente()) &&
                        richiesta.getStatoRichiesta() == 1 &&
                        dataRichiesta.compareTo(dateFormat.parse(richiesta.getInizioPeriodo())) >= 0 &&
                        dataRichiesta.compareTo(dateFormat.parse(richiesta.getFinePeriodo())) <= 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            // Gestione dell'eccezione in caso di errore nella conversione delle date
            e.printStackTrace();
        }

        return false;
    }

    /* Il metodo scegliServizio accetta due parametri: un oggetto Utente chiamato impiegato e un array di valori booleani chiamato servizi e restituisce un valore intero che indica a quale servizio dovrà essere assegnato l'impiegato.
       - Il codice assegna priorità ai servizi in modo decrescente da 1 a 4, dove il servizio 1 ha la priorità più alta e il servizio 4 ha la priorità più bassa. Per fare ciò, il codice valuta il ruolo dell'utente e la disponibilità dei servizi.
       - Viene dichiarato un costrutto switch basato sul valore restituito dal metodo getRuolo() chiamato sull'oggetto impiegato.
       - In base al ruolo dell'utente, il metodo restituirà un valore intero.
       - Il costrutto switch può essere suddiviso in 4 casi corrispondenti ai diversi valori di ruolo dell'utente da 1 a 4:
         -> Caso 4: Se il ruolo è 4, il metodo restituirà 1.
                    - Se il ruolo dell'utente è 4, il metodo restituirà sempre 1, indipendentemente dalla disponibilità dei servizi.
                    - Questo significa che il servizio a priorità 1 sarà sempre offerto se ci sono utenti di ruolo 4.
         -> Caso 3: Se il ruolo è 3 e servizi[1] è vero, il metodo restituirà 1; altrimenti, restituirà 2.
                    - Se il ruolo dell'utente è 3, il metodo restituirà 1 se il servizio 2 è disponibile (servizi[1] è vero), in modo da garantire che il servizio a priorità 1 venga offerto il più a lungo possibile.
                    - Se il servizio 2 non è disponibile, restituirà 2, assegnando il servizio a priorità 2.
         -> Caso 2: Se il ruolo è 2, il metodo restituirà 1 se servizi[1] è vero, 2 se servizi[2] è vero, altrimenti restituirà 3.
                    - Se il ruolo dell'utente è 2, il metodo assegnerà il servizio a priorità 1 se è disponibile.
                    - in caso contrario il servizio a priorità 2 se è disponibile, e in caso contrario il servizio a priorità 3.
                    - Questo garantisce che il servizio a priorità 1 sia offerto prima degli altri.
         -> Caso 1: Se il ruolo è 1, il metodo restituirà 1 se servizi[1] è vero, 2 se servizi[2] è vero, 3 se servizi[3] è vero, altrimenti restituirà 4.
                    - Assegnerà il servizio a priorità 1 se è disponibile, altrimenti il servizio a priorità 2 se è disponibile,
                       quindi il servizio a priorità 3, e infine il servizio a priorità 4 se non ci sono altre opzioni.
         -> Default: Se il ruolo non corrisponde a nessuno dei casi specificati, il metodo restituirà -1.
     - RECAP: Il codice garantisce che il servizio a priorità 1 venga offerto il più a lungo possibile, seguendo un ordine decrescente di priorità se il servizio a priorità 1 non è disponibile.
    */


    private int scegliServizio(Utente impiegato, boolean[] servizi) {
        switch (impiegato.getRuolo()) {
            case 4:
                return 1;
            case 3:
                if (servizi[1]) {
                    return 1;
                } else {
                    return 2;
                }
            case 2:
                if (servizi[1]) {
                    return 1;
                } else if (servizi[2]) {
                    return 2;
                } else {
                    return 3;
                }
            case 1:
                if (servizi[1]) {
                    return 1;
                } else if (servizi[2]) {
                    return 2;
                } else if (servizi[3]) {
                    return 3;
                } else {
                    return 4;
                }
            default:
                return -1;
        }
    }

    /* Il metodo toOpen prende un array di booleani chiamato servizi come argomento e restituisce un valore booleano (true o false) e serve a determinare se è necessario aprire o chiudere una serie di "servizi" rappresentati da un array di booleani:
       - In particolare, il metodo restituirà true se almeno uno dei servizi è "da aprire," cioè se almeno uno degli elementi nell'array è false.
       - Al contrario, restituirà false se tutti i servizi sono "da chiudere" o "già aperti," cioè se tutti gli elementi nell'array sono true.
       - (blocco for-each): utilizzato per scorrere tutti gli elementi nell'array servizi e verificare se almeno uno di essi è false grazie ad un blocco if.
                            -> Se trova almeno un elemento false, il ciclo viene interrotto prematuramente con un return true, indicando che almeno un servizio è "da aprire.".
                            -> Se nessun elemento false viene trovato durante l'iterazione, il ciclo completa la sua esecuzione e il metodo restituirà false, indicando che tutti i servizi sono "da chiudere" oppure "già aperti."

    */
    private boolean toOpen(boolean[] servizi) {
        for (boolean b : servizi) {
            if (!b) {
                return true;
            }
        }
        return false;
    }

     /* Il metodo calcolaTurnazione genera una turnazione per gli impiegati in un periodo di 90 giorni, dalle 9:00 alle 17:00.
       -> Per ogni giorno e ora, il codice assegna servizi disponibili in modo casuale a impiegati, garantendo che gli impiegati abbiano ore da svolgere e non abbiano richieste approvate per la data corrente.
       ->I dettagli dei turni generati vengono memorizzati nella lista listaTurni, e gli impiegati vedono ridotte le loro ore da svolgere a mano a mano che vengono assegnati i servizi.
     - Il metodo accetta tre liste come argomenti: listaImpiegati (con stato "attivo"), listaRichieste ("approvate"), e listaTurni ("inizialmente vuota") per calcolare una turnazione per gli impiegati e aggiungere i turni risultanti alla lista listaTurni:
     - listaTurni.clear():  svuota completamente la lista listaTurni, rimuovendo tutti i suoi elementi precedenti.
     - Random r = new Random() utilizzato per generare numeri casuali.
     - int s:  variabile s di tipo intero non inizializzata ed usata come indice per accedere casualmente agli impiegati nella lista listaImpiegati.
     - int n_imp: variabile utilizzata per tenere traccia del numero di impiegati nella lista listaImpiegati.
     - Calendar calendar = Calendar.getInstance(): Viene creato un oggetto Calendar chiamato calendar e inizializzato con l'istanza corrente del calendario.
     - (blocco for): itera per un totale di 90 giorni, da d = 0 a d = 89 ed esegue per ogni iterazione le seguenti operazioni:
                    -> Dentro il ciclo for, vengono ottenuti i dettagli della data corrente, tra cui l'anno, il mese e il giorno utilizzando l'oggetto Calendar.
                    -> Anno, mese e giorno corrente vengono combinati per formare una stringa rappresentante la data.
                    ->  La data viene incrementata di un giorno utilizzando calendar.add(Calendar.DAY_OF_MONTH, 1) per passare al giorno successivo.
                    -> (ciclo for interno) La funzione entra in un ciclo che itera dalle 9:00 alle 18:00 (variabile h da 9 a 17):
                        - All'interno del ciclo interno, viene calcolato il numero di impiegati attualmente disponibili (n_imp) dalla dimensione dell'ArrayList listaImpiegati.
                        - Viene creato un array booleano servizi inizializzato con tutti i valori false.
                        - (ciclo while interno) ciclo che continua finché ci sono impiegati disponibili (n_imp > 0) e ci sono servizi da coprire (toOpen(servizi) restituisce true):
                            -> All'interno del ciclo while, viene generato un indice randomico s per selezionare un impiegato dalla lista (umero casuale tra 0 e listaImpiegati.size() - 2, che verrà utilizzato come indice per selezionare casualmente un impiegato dall'ArrayList listaImpiegati)
                            -> Viene ottenuto l'impiegato selezionato da listaImpiegati.
                            -> (blocco if interno) La funzione verifica se l'impiegato ha ore da svolgere e se non ha richieste approvate per la data corrente.
                                - Se le condizioni sono soddisfatte, viene scelto un servizio (cod_servizio) per l'impiegato e segnato come coperto nell'array servizi.
                                - Viene creato un oggetto Turno con le informazioni dell'impiegato, del servizio, della data e dell'orario.
                                - Il turno appena creato viene aggiunto all'ArrayList listaTurni.
                                - Viene decrementato il numero di ore da svolgere per l'impiegato selezionato.
                         - Il ciclo while continua finché ci sono impiegati disponibili o servizi da coprire.
                     -> Il ciclo interno termina, e la funzione passa al giorno successivo e ripete il processo per tutti i giorni del periodo specificato.
    */

    public void calcolaTurnazione(ArrayList<Utente> listaImpiegati, ArrayList<Richiesta> listaRichieste, ArrayList<Turno> listaTurni) {
        listaTurni.clear();
        Random r = new Random();
        int s;  //preparazione indice randomico
        int n_imp; //numero impiegati
        Calendar calendar = Calendar.getInstance();
        for (int d = 0; d < 90; d++) {
            int anno = calendar.get(Calendar.YEAR);
            int mese = calendar.get(Calendar.MONTH) + 1;
            int giorno = calendar.get(Calendar.DAY_OF_MONTH);
            String data = anno + "-" + mese + "-" + giorno;
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            for (int h = 9; h < 18; h++) {
                n_imp = listaImpiegati.size();
                boolean[] servizi = {false, false, false, false};
                while (n_imp > 0 && toOpen(servizi)) {
                    s = r.nextInt(listaImpiegati.size() - 1);
                    Utente impiegato = listaImpiegati.get(s);
                    if (impiegato.getOre_da_svolgere() > 0 && !haRichiesteApprovate(impiegato, listaRichieste, data)) {
                        int cod_servizio = scegliServizio(impiegato, servizi);
                        servizi[cod_servizio - 1] = true;
                        Turno t = new Turno(impiegato.getMatricola(), cod_servizio, data, 0, h + ":0:0", h + 1 + ":0:0");
                        listaTurni.add(t);
                        listaImpiegati.get(s).sottraiOreDaSvolgere(1);
                    }
                    n_imp--;
                }
            }
        }
    }

        /* Il metodo registraNuovaTurnazione registra nuove turnazioni nel dbms iterando attraverso una lista di oggetti Turno, creando un'istruzione SQL per ciascun oggetto e inserendo i dati nel dbms.
       - (blocco for-each):  itera attraverso ciascun oggetto Turno nella lista listaTurno e per ciascun oggetto Turno:
                            -> Viene creata una query di inserimento (INSERT INTO) nel dbms che specifica i campi della tabella turni in cui verranno inseriti i dati relativi al turno.
                            -> Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata l'istruzione SQL istaziando un oggetto PreparedStatement pst.
                            -> Vengono impostati i valori dei parametri nella query utilizzando i metodi setString, setInt in base al parametro specificato.
                            -> La query viene inviata al database per l'esecuzione.
       - Alla fine del blocco try, il blocco catch(SQLException e) gestisce le eccezioni dovute ad errori durante l'inserimento dei dati nel database e viene stampato un messaggio di errore sulla console.

    */

    public void registraNuovaTurnazione(Turno turno) {
            String query = "INSERT INTO citizenservices.turni(ref_matricola, cod_servizio, data, cod_tipo_presenza, ora_inizio, ora_fine) VALUES (?,?,?,?,?,?) ;";
            InterfacciaDB DB = new InterfacciaDB();
            try (Connection con = DB.getConnection();
                 PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, turno.getRef_matricola());
                pst.setInt(2, turno.getCod_servizio());
                pst.setString(3, turno.getData());
                pst.setInt(4, turno.getCod_tipo_presenza());
                pst.setString(5, turno.getOra_inizio());
                pst.setString(6, turno.getOra_fine());
                pst.executeUpdate();
            } catch (SQLException e) {
                System.out.println("ERRORE CARICAMENTO TURNI");
            }
    }

        /* Il metodo ottieniTurniSettimanali ottiene i turni di un impiegato per una settimana specifica dal database, utilizzando date di inizio e fine della settimana calcolate inizialmente; poi i risultati vengono memorizzati nell'ArrayList listaTurni.
        - listaTurni.clear(): inizialmente l'ArrayList listaTurni viene svuotato in modo da assicurarci che sia vuoto prima di aggiungere nuovi dati.
        - String settimana = InterfacciaTMP.OttieniSettimana(): Il metodo richiama OttieniSettimana dalla classe InterfacciaTMP per ottenere le date di inizio e fine della settimana corrente e le memorizza nella stringa settimana.
        - String[] inizioFine = settimana.split(",");: La stringa settimana viene suddivisa utilizzando la virgola come delimitatore ed i risultati vengono memorizzati in un array di stringhe inizioFine che separa le date di inizio e fine della settimana:
                                -> String inizioSettimana = inizioFine[0]: La data di inizio settimana viene estratta dall'array inizioFine e memorizzata nelle variabile inizioSettimana
                                -> String fineSettimana = inizioFine[1]: La data di fine settimana viene estratta dall'array inizioFine e memorizzata nelle variabile fineSettimana.
        - String inizio = CommonMethods.formattaDataAggiungi0(inizioSettimana): La data di inizio della settimana viene formattata utilizzando il metodo formattaDataAggiungi0 dalla classe CommonMethods ed il risultato viene memorizzato nella variabile inizio.
        - String fine = CommonMethods.formattaDataAggiungi0(fineSettimana): La data di fine della settimana viene formattata utilizzando il metodo formattaDataAggiungi0 dalla classe CommonMethods ed il risultato viene memorizzato nella variabile fine.
        - NOTA BENE): Il metodo formattaDataAggiungi0() riceve una data nel formato "giorno-mese-anno", controlla se il giorno ha una sola cifra, e se lo ha, rimuove lo "0" iniziale aggiungendo una sola cifra.
        - Viene definita una query che seleziona tutti i record dalla tabella turni dove la data è compresa tra inizio e fine e il campo ref_matricola corrisponde a matricola. I risultati verranno ordinati per data.
        - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
        - Vengono impostati i valori dei parametri nella query utilizzando i metodi setString.
        - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto ResultSet (conterrà tutte le righe selezionate dalla query).
        - All'interno del ciclo while, i dati relativi a ciascun turno (ref_matricola, cod_servizio, data, cod_tipo_presenza, ora_inizio e ora_fine) vengono estratti dal resultSet tramite i rispettivi metodi getString, getInt a seconda del tipo di parametro specificato.
        - Viene creato un oggetto Turno con i dati estratti.
        - L'oggetto Turno appena creato viene aggiunto all'ArrayList listaTurni.
        - Se si verifica un errore SQL durante l'esecuzione della query, viene catturata un'eccezione di tipo SQLException ed il metodo lostConnection dell'oggetto DB viene chiamato per gestire la situazione.
    */

    public void ottieniTurniSettimanali(String matricola, ArrayList<Turno> listaTurni) {
        listaTurni.clear();
        String settimana = InterfacciaTMP.OttieniSettimana();
        String[] inizioFine = settimana.split(",");
        String inizioSettimana = inizioFine[0];
        String fineSettimana = inizioFine[1];
        String inizio = CommonMethods.formattaDataAggiungi0(inizioSettimana);
        String fine = CommonMethods.formattaDataAggiungi0(fineSettimana);
        String query = "SELECT turni.* FROM citizenservices.turni WHERE turni.data >= ? AND turni.data <= ? AND turni.ref_matricola = ? ORDER BY turni.data ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, inizio);
            pst.setString(2, fine);
            pst.setString(3, matricola);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                String ref_matricola = resultSet.getString("ref_matricola");
                int cod_servizio = resultSet.getInt("cod_servizio");
                String data = resultSet.getString("data");
                int cod_tipo_presenza = resultSet.getInt("cod_tipo_presenza");
                String ora_inizio = resultSet.getString("ora_inizio");
                String ora_fine = resultSet.getString("ora_fine");
                Turno turno = new Turno(ref_matricola, cod_servizio, data, cod_tipo_presenza, ora_inizio, ora_fine);
                listaTurni.add(turno);
            }
        } catch (SQLException e) {
            DB.lostConnection();
        }
    }

    /* Il metodo controlloListaTurniVuota verifica se l'ArrayList listaTurni passato come argomento è vuoto o contiene elementi:
        - Restituirà true se la lista è vuota.
        - Restituirà false se contiene almeno un elemento.
    */

    public boolean controlloListaTurniVuota(ArrayList<Turno> listaTurni) {
        return listaTurni.isEmpty();
    }

     /* Il metodo ottieniImpiegatiCheHannoTurniNellaSettimanaCorrente ottiene gli impiegati (utenti) che hanno turni nella settimana corrente basandosi sulle date di inizio e fine della settimana.
     - listaImpiegati.clear();: Questa istruzione inizia svuotando l'ArrayList (lista sia vuota prima di aggiungere nuovi dati.)
     - String settimana = InterfacciaTMP.OttieniSettimana();: Il metodo chiama OttieniSettimana dalla classe InterfacciaTMP per ottenere le date di inizio e fine della settimana corrente. Queste date vengono memorizzate nella stringa settimana.
     - String[] inizioFine = settimana.split(",");: La stringa settimana viene suddivisa utilizzando la virgola come delimitatore ed i risultati vengono memorizzati in un array di stringhe inizioFine che separa le date di inizio e fine della settimana:
                                -> String inizioSettimana = inizioFine[0]: La data di inizio settimana viene estratta dall'array inizioFine e memorizzata nelle variabile inizioSettimana
                                -> String fineSettimana = inizioFine[1]: La data di fine settimana viene estratta dall'array inizioFine e memorizzata nelle variabile fineSettimana.
     - String inizio = CommonMethods.formattaDataAggiungi0(inizioSettimana): La data di inizio della settimana viene formattata utilizzando il metodo formattaDataAggiungi0 dalla classe CommonMethods ed il risultato viene memorizzato nella variabile inizio.
     - String fine = CommonMethods.formattaDataAggiungi0(fineSettimana): La data di fine della settimana viene formattata utilizzando il metodo formattaDataAggiungi0 dalla classe CommonMethods ed il risultato viene memorizzato nella variabile fine.
     - Viene definita una query SQL che seleziona gli impiegati (utenti) con turni nella settimana corrente. Questa query utilizza una clausola JOIN per unire le tabelle utenti e turni in base al campo matricola.
     - Vengono impostati i valori dei parametri nella query utilizzando i metodi setString.
     - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto ResultSet (conterrà tutte le righe selezionate dalla query).
     - All'interno del ciclo while, i dati relativi a ciascun turno (ref_matricola, cod_servizio, data, cod_tipo_presenza, ora_inizio e ora_fine) vengono estratti dal resultSet tramite i rispettivi metodi getString, getInt a seconda del tipo di parametro specificato.
     - Viene creato un oggetto Utente con i dati estratti.
     - L'oggetto Turno appena creato viene aggiunto all'ArrayList listaImpiegati.
     - Se si verifica un errore SQL durante l'esecuzione della query, viene catturata un'eccezione di tipo SQLException ed il metodo lostConnection dell'oggetto DB viene chiamato per gestire la situazione; viene anche stampato un messaggio di errore sulla console.
    */


    public void ottieniImpiegatiCheHannoTurniNellaSettimanaCorrente(ArrayList<Utente> listaImpiegati) {
        listaImpiegati.clear();
        String settimana = InterfacciaTMP.OttieniSettimana();
        String[] inizioFine = settimana.split(",");
        String inizioSettimana = inizioFine[0];
        String fineSettimana = inizioFine[1];
        String inizio = CommonMethods.formattaDataAggiungi0(inizioSettimana);
        String fine = CommonMethods.formattaDataAggiungi0(fineSettimana);
        String query = "SELECT DISTINCT utenti.* FROM citizenservices.utenti JOIN citizenservices.turni ON turni.ref_matricola = utenti.matricola WHERE turni.data >= ? AND turni.data <= ? ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, inizio);
            pst.setString(2, fine);
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
                listaImpiegati.add(utente);
            }
        } catch (SQLException e) {
            System.out.println("errore ottieni impiegati che hanno turni nella settimana corrente");
            DB.lostConnection();
        }
    }
}
    /*public static void main(String[] args){
        ControlGestioneTurnazione CGT = new ControlGestioneTurnazione();
        ArrayList<Utente> impiegatiAttivi = new ArrayList<>();
        ArrayList<Richiesta> richiesteApprovate = new ArrayList<>();
        ArrayList<Turno> nuoviTurni = new ArrayList<>();
        if(true) {//controllo giorno calcolo turnazione
            CGT.ottieniImpiegatiConStatoAttivo(impiegatiAttivi);
            CGT.ottieniListaPeriodiAstensioneApprovati(richiesteApprovate);
            CGT.calcolaTurnazione(impiegatiAttivi, richiesteApprovate, nuoviTurni);
        }
        System.out.println("turni:" + nuoviTurni.size());
        CGT.registraNuovaTurnazione(nuoviTurni);
        }
    }*/