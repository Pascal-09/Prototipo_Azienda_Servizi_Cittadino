package COM.CS.GestioneTurnazione.Control;


import COM.CS.Commons.InterfacciaDB;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.Entity.Comunicazione;
import COM.CS.Entity.Turno;
import COM.CS.Entity.Utente;
import COM.CS.Utili.CommonMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class ControlGestioneAssenza {

    /*
     IL metodo gestisce l'assenza di un impiegato in un intervallo di date specifico.
     Esegue diverse operazioni, tra cui la formattazione delle date, il recupero dei turni dell'impiegato,
     la registrazione dell'assenza e la gestione dei sostituti.
     Elenco delle operazioni svolte dal metodo:
      1. Svuota l'ArrayList dei turni.
      2. Inizializza gli ArrayList per i sostituti e i nuovi turni.
      3. Formatta le date di inizio e fine rimuovendo zeri iniziali.
      4. Prepara la data per scorrere i giorni nel ciclo.
      5. Ottiene i turni in cui l'impiegato si assenterà.
      6. Registra l'assenza dell'impiegato nei turni.
      7. Verifica se l'ArrayList dei turni non è vuoto.
         - Se è vuoto, il metodo termina con un messaggio.
      8. Svuota l'ArrayList dei nuovi turni inizialmente.
      9. Itera attraverso i turni dell'impiegato.
         - Per ciascun turno:
         10. Verifica se il turno è previsto per la data corrente.
         11. Trova possibili sostituti tra gli impiegati disponibili.
         12. Verifica se l'ArrayList dei sostituti è vuoto.
             - Se è vuoto, calcola il numero di impiegati in servizio con l'assenza.
             14. Verifica se è necessario chiudere un servizio a bassa priorità e riassegnare gli impiegati.
                 - In caso il numero minimo sia stato raggiunto, non fare nulla.
                 - Altrimenti, chiudi il servizio a bassa priorità e riassegna gli impiegati.
             17. Se ci sono possibili sostituti, assegna loro il turno.
             18. Crea una comunicazione di variazione dei turni.
         19. Passa al giorno successivo.
      20. Stampa un messaggio se l'ArrayList dei turni è vuoto.
     */

    public ControlGestioneAssenza(String matricola, String dataInizio, String dataFine, ArrayList<Turno> turni){
        turni.clear();
        ArrayList<Utente> sostituti = new ArrayList<>();
        ArrayList<Turno> nuovi_turni = new ArrayList<>();
        //prepariamo le date per la ricerca sul DBMS
        String inizio = CommonMethods.formattaDataTogli0(dataInizio);
        String fine = CommonMethods.formattaDataTogli0(dataFine);
        //prepariamo la data per poter scorrere i giorni nel for
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dataInizio, formatter);
        ottieniTurniIntervalloDate(matricola, inizio, fine, turni); //ottieni turni in cui si assenterà l'impiegato
        registraAssenzaImpiegato(turni);
        if(!controllaListaVuota(turni)){
            nuovi_turni.clear(); //inizialmente non ci deve essere nessun nuovo turno
            for(Turno turno : turni){ //per ciascun turno dell'impiegato che si assenterà
                if(turno.getData().equals(CommonMethods.formattaDataTogli0(localDate.format(formatter)))){//turno nel giorno x tra data inizio e data fine
                    trovaSostituti(turni, sostituti); //trova impiegati che possono essere dei sostituti
                    if(controllaListaSostitutiVuota(sostituti)){ //non ci sono sostituti possibili
                        int n = ottieniNumeroImpiegatiServizioConAssenza(turno.getCod_servizio());
                        if(controlloRaggiungimentoNumeroMinimo(n)){
                            //non succede nulla
                        }
                        else{
                            chiudiServizioBassaPriorità();
                            riassegnaImpiegati();
                        }
                    }
                    else{ //ci sono dei possibili sostituti
                        assegnaTurnoAiSostituti(turno, sostituti,nuovi_turni);
                        creaComunicazioneVariazioneTurni(nuovi_turni);
                    }
                }
                localDate.plusDays(1);
            }
        }
        else{
            System.out.println("non ci sono turni");
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

    public void ottieniTurniIntervalloDate(String matricola, String dataInizio, String dataFine, ArrayList<Turno> turni){
        String query = "SELECT turni.* FROM citizenservices.turni WHERE turni.ref_matricola = ? AND turni.data >= ? AND turni.data <= ? ORDER BY turni.data ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(2, dataInizio);
            pst.setString(3, dataFine);
            pst.setString(1, matricola);
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


    public void registraAssenzaImpiegato(ArrayList<Turno> turni){
        for(Turno turno: turni){
            String query = "UPDATE citizenservices.turni SET cod_tipo_presenza = -1 WHERE turni.ref_matricola = ? AND turni.data = ? AND ora_inizio = ? AND ora_fine = ? ;";
            InterfacciaDB DB = new InterfacciaDB();
            try (Connection con = DB.getConnection();
                 PreparedStatement pst = con.prepareStatement(query);) {
                pst.setString(1, turno.getRef_matricola());
                pst.setString(2, turno.getData());
                pst.setString(3, turno.getOra_inizio());
                pst.setString(4, turno.getOra_fine());
                int rowsAffected  = pst.executeUpdate();
            }
            catch (SQLException e){
            }
        }
    }

    /* Il metodo controllaListaVuota verifica se la lista turni è vuota.
       - Per fare ciò, utilizza il metodo isEmpty() dell'oggetto ArrayList, che restituisce:
         -> true se la lista è vuota (cioè non contiene elementi).
         -> false se la lista ha almeno un elemento.
     */

    public boolean controllaListaVuota(ArrayList<Turno> turni){
        return turni.isEmpty();
    }


    /* Il metodo trovaSostituti cerca e aggiunge utenti idonei da un database al riferimento ArrayList sostituti in base a criteri specifici definiti nella query.
       Se l'operazione va a buon fine, la lista conterrà gli utenti idonei a sostituire quelli con turni non coperti.
     - sostituti.clear();: Questa istruzione svuota l'ArrayList sostituti, rimuovendo tutti gli elementi presenti al suo interno, in modo da prepararlo a ricevere i nuovi utenti trovati.
     - Viene definita una query che seleziona gli utenti dal database citizenservices che soddisfano determinate condizioni. Gli utenti devono avere uno "stato_contratto" uguale a 1, devono avere "ore_da_svolgere" superiori a 0 e il numero di turni a loro assegnati (risultante da una subquery) deve essere inferiore alle "ore_da_svolgere". Questa query verrà utilizzata successivamente per estrarre gli utenti idonei.
     - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
     - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto resultSet (contiene le righe selezionate dalla query).
     - (blocco while): utilizzato per iterare attraverso le righe dei risultati estratti dalla query SQL e per estrarre i dati da ciascuna riga; All'interno del ciclo while, viene valutata la condizione resultSet.next() (controlla se ci sono ulteriori righe nel ResultSet.)
		-> resultSet.next()=true, esiste una riga successiva nel ResultSet da leggere e viene eseguito il corpo del ciclo:
		   - All'interno del corpo del ciclo, vengono estratti i dati da ciascuna colonna della riga corrente del ResultSet utilizzando i metodi getString() o getInt() in base al tipo di dato.
		   - I dati estratti vengono utilizzati per creare oggetti "Utente" corrispondenti.
	       - Gli oggetti creati vengono quindi aggiunti all' ArrayList "sostituti" utilizzando il metodo add().
		   - Il ciclo continua ad iterare attraverso le righe del ResultSet fino a quando non ci sono più righe da elaborare.
		-> Se resultSet.next()=false, significa che tutte le righe sono state lette e non ci sono più righe successive nel ResultSet.
    - Se si verifica un'eccezione SQL durante l'esecuzione della query o il recupero dei dati, il blocco catch viene eseguito.
      In questo caso, il blocco catch è vuoto, il che significa che se si verifica un'eccezione SQL, la lista sostituti rimarrà vuota.
    */
    public void trovaSostituti(ArrayList<Turno> turni, ArrayList<Utente> sostituti){
        sostituti.clear();
        String query = "SELECT utenti.* FROM citizenservices.utenti WHERE stato_contratto = 1 AND ore_da_svolgere > 0 AND ore_da_svolgere > (SELECT COUNT(*) FROM citizenservices.turni WHERE utenti.matricola = turni.ref_matricola ) ;";
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
                sostituti.add(utente);
            }
        } catch (SQLException e) {
            //restituisce la listaImpiegati vuota
        }
    }

       /* Il metodo controllaListaVuota verifica se la lista dei sostituti è vuota.
       - Per fare ciò, utilizza il metodo isEmpty() dell'oggetto ArrayList che restituisce:
         -> true se la lista è vuota (cioè non contiene elementi).
         -> false se la lista ha almeno un elemento.
     */

    public boolean controllaListaSostitutiVuota(ArrayList<Utente> sostituti){
        return sostituti.isEmpty();
    }

    /* Il metodo assegnaTurnoAiSostituti seleziona casualmente un sostituto tra quelli disponibili, assegna a questo sostituto il turno specifico passato come parametro e aggiunge il nuovo turno all'ArrayList nuovi_turni.
     - Random r = new Random();: Viene creato un oggetto Random per generare un numero casuale che sarà utilizzato per selezionare casualmente un sostituto tra quelli disponibili.
     -  int s = r.nextInt(sostituti.size() - 1);: Viene generato un numero casuale tra 0 e sostituti.size() - 1 per selezionare un indice casuale nella lista dei sostituti.
     - Utente sostituto = sostituti.get(s);: Viene ottenuto l'utente selezionato casualmente dalla lista dei sostituti in base all'indice casuale.
     - sostituti.remove(s);: Il sostituto selezionato viene rimosso dalla lista dei sostituti in modo da non poter essere selezionato nuovamente.
     - Viene definita una stringa che nserisce un nuovo turno nella tabella turni del database citizenservices, con i valori specifici passati come parametri (La riga inserita rappresenta il turno assegnato a un sostituto selezionato casualmente)
     - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
     - Vengono impostati i valori dei parametri nella query utilizzando i metodi setString e setInt.
     - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto rowsAffected.
     - Viene creato un nuovo oggetto Turno che rappresenta il turno assegnato al sostituto e questo nuovo turno viene aggiunto all'ArrayList nuovi_turni.
     - In caso di eccezione SQL, il blocco catch viene eseguito ma non intraprende alcuna azione specifica.
    */

    public void assegnaTurnoAiSostituti(Turno turno, ArrayList<Utente> sostituti, ArrayList<Turno> nuovi_turni){
        Random r = new  Random();
        int s = r.nextInt(sostituti.size() - 1);
        Utente sostituto = sostituti.get(s);
        sostituti.remove(s);
        String query = "INSERT INTO citizenservices.turni(ref_matricola, cod_servizio, data, cod_tipo_presenza, ora_inizio, ora_fine) VALUES(?,?,?,?,?,?) ;";
        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query);){
            pst.setString(1,sostituto.getMatricola());
            pst.setInt(2, turno.getCod_servizio());
            pst.setString(3, turno.getData());
            pst.setInt(4, 0);
            pst.setString(5, turno.getOra_inizio());
            pst.setString(6, turno.getOra_fine());
            int rows_affected = pst.executeUpdate();
            nuovi_turni.add(new Turno(sostituto.getMatricola(), turno.getCod_servizio(), turno.getData(), 0, turno.getOra_inizio(), turno.getOra_fine()));
        }
        catch(SQLException e){
        }
    }

    /* Il metodo creaComunicazioneVariazioneTurni crea e inserisce comunicazioni di variazione di turno nel database per ciascun nuovo turno nell'ArrayList nuovi_turni.
     - (blocco for): Si itera attraverso ogni Turno nell'ArrayList nuovi_turni e per ognuno di essi:
                     -> Viene calcolato un nuovo codice di comunicazione chiamando il metodo calcolaCodiceComunicazione della classe Comunicazione.
                     -> Viene creato un oggetto Comunicazione con i parametri necessari.
                     -> Viene definita una query che inserisce una nuova comunicazione nel database citizenservices con i valori specifici forniti come parametri.
                     -> Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
                     -> Vengono impostati i valori dei parametri nella query utilizzando i metodi setString e setInt.
                     -> La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto rowsAffected.
                     -> In caso di eccezione SQL, il blocco catch viene eseguito ma non intraprende alcuna azione specifica.
     */

    public void creaComunicazioneVariazioneTurni(ArrayList<Turno> nuovi_turni){
        for(Turno turno: nuovi_turni){
            Comunicazione comunicazione = new Comunicazione(Comunicazione.calcolaCodiceComunicazione(), "citizenservices", turno.getRef_matricola(), 2 , "notifica variazione turno", turno.toString() , InterfacciaTMP.ottieniOrario());
            String query = "INSERT INTO citizenservices.comunicazioni(cod_comunicazione, ref_mittente, ref_destinatario, tipo_comunicazione, oggetto, testo, orario_invio) VALUES (?,?,?,?,?,?, ? )";
            InterfacciaDB DB= new InterfacciaDB();

            try (Connection con=DB.getConnection();
                 PreparedStatement pst = con.prepareStatement(query)){
                pst.setInt(1, comunicazione.getCod_comunicazione());
                pst.setString(2, comunicazione.getRef_mittente());
                pst.setString(3,comunicazione.getRef_destinatario());
                pst.setInt(4,comunicazione.getTipo_comunicazione());
                pst.setString(5, comunicazione.getOggetto());
                pst.setString(6,comunicazione.getTesto());
                pst.setString(7,comunicazione.getOrarioInvio());
                int righeInserite= pst.executeUpdate();
            } catch (SQLException e) {
            }
        }
    }


/* Il metodo ottieniNumeroImpiegatiServizioConAssenza restituisce sempre il valore -1.
 */
    public int ottieniNumeroImpiegatiServizioConAssenza(int cod_servizio){
        return -1;
    }

    /* Il metodo controlloRaggiungimentoNumeroMinimo verifica se il numero di impiegati di un servizio è maggiore o uguale a 1.
    Se è così, il metodo restituirà true, altrimenti restituirà false
*/

    public boolean controlloRaggiungimentoNumeroMinimo(int n_impiegati_servizio){
        if(n_impiegati_servizio >= 1){
            return true;
        }
        else {
            return false;
        }
    }

    public void chiudiServizioBassaPriorità(){

    }

    public void riassegnaImpiegati(){

    }



}
