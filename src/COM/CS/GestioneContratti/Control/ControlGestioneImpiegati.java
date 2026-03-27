package COM.CS.GestioneContratti.Control;

import COM.CS.Commons.InterfacciaDB;
import COM.CS.Entity.Contratto;
import COM.CS.Entity.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlGestioneImpiegati {

    /* Il metodo filtraLista accetta vari parametri e filtra una lista di oggetti Utente in base ai criteri specificati.
        - listaFiltrata.clear(): Viene cancellato il contenuto dell'ArrayList listaFiltrata, per assicurarci che sia vuoto prima di iniziare a riempirlo con gli utenti filtrati.
        - (ciclo for): Il ciclo esegue le seguenti operazioni per ciascun oggetto Utente nell'ArrayList lista:
                      -> boolean matricolaMatch = matricola.isEmpty(): Per ciascun utente, viene verificato se la matricola specificata (matricola) è vuota o se corrisponde alla matricola dell'utente corrente.
                                                 - La variabile matricolaMatch sarà true se uno dei due casi è verificato.
                                                 - Questo verifica se la matricola dell'utente soddisfa il criterio specificato o se il campo matricola è vuoto.
                      -> boolean nomeMatch = nome.isEmpty() || utente.getNome().equals(nome): Per ciascun utente, viene verificato se il campo "nome" specificato (nome) è vuoto o se corrisponde al nome dell'utente corrente.
                                                 - La variabile nomeMatch sarà true se uno dei due casi è verificato,
                                                 - Questo verifica se il nome dell'utente soddisfa il criterio specificato o se il campo nome è vuoto.
                      -> boolean cognomeMatch = cognome.isEmpty() || utente.getCognome().equals(cognome): Questo passo è analogo a nomeMatch, ma verifica il campo "cognome" invece del campo "nome".
       - (alla fine di ogni iterazione): viene eseguita una condizione if per determinare se tutte e tre le condizioni (matricolaMatch, nomeMatch e cognomeMatch) sono soddisfatte per l'utente corrente:
                      -> Se tutte e tre le condizioni sono vere, significa che l'utente soddisfa tutti i criteri di filtro (matricola, nome e cognome) e viene aggiunto all'ArrayList listaFiltrata.
                      -> Se anche una delle tre condizioni è false, l'utente non soddisfa tutti i criteri e il codice all'interno del blocco if non verrà eseguito per quell'utente.
    */


        public void filtraLista(String matricola, String nome,String cognome, ArrayList<Utente>lista, ArrayList<Utente> listaFiltrata){
            listaFiltrata.clear();
            for (Utente utente : lista) {
                boolean matricolaMatch = matricola.isEmpty() || utente.getMatricola().equals(matricola);
                boolean nomeMatch = nome.isEmpty() || utente.getNome().equals(nome);
                boolean cognomeMatch = cognome.isEmpty() || utente.getCognome().equals(cognome);

                if (matricolaMatch && nomeMatch && cognomeMatch) {
                    listaFiltrata.add(utente);
                }
            }
        }

        public boolean controlloListaFiltrata(ArrayList<Utente> lista){
            return !lista.isEmpty();
        }

        /*public boolean controlloModifiche(){

        }*/

        public void modificaDatiImpiegato(String matricola, String IBAN,String telefono){

        }

        /* Il metodo ottieniListaImpiegati accetta un parametro Arraylist di nome listaImpiegati per gli oggetti di classe Utente per ottenere la lista degli impiegati in base allo specifico stato di contratto (stato_contratto = 1 = "impiegato attivo") e li memorizza nell'ArrayList di oggetti Utente.
            - listaImpiegati.clear(): Viene cancellato il contenuto dell'ArrayList listaImpiegati per assicurarci che sia vuoto prima di iniziare a riempirlo con gli impiegati ottenuti dal dbms.
            - Viene dichiarata una query che contiene un'istruzione SQL per selezionare (SELECT) tutte le righe della tabella "utenti" del dbms "citizenservices" che presentano "stato_contratto"=1.
            - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
            - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto ResultSet (conterrà tutte le righe selezionate dalla query).
            - (blocco while): utilizzato per iterare attraverso le righe dei risultati estratti dalla query SQL e per estrarre i dati da ciascuna riga; All'interno del ciclo while, viene valutata la condizione resultSet.next() (controlla se ci sono ulteriori righe nel ResultSet.)
		                      -> resultSet.next()=true, esiste una riga successiva nel ResultSet da leggere e viene eseguito il corpo del ciclo:
		                                                 - All'interno del corpo del ciclo, vengono estratti i dati da ciascun colonna della riga corrente del ResultSet utilizzando i metodi getString() o getInt() in base al tipo di dato.
		                                                 - I dati estratti vengono utilizzati per creare l'oggetto "Utente" corrispondente.
	                                                     - Gli oggetti creati vengono quindi aggiunti all'array listaImpiegati utilizzando il metodo add().
		                                                 - Il ciclo continua ad iterare attraverso le righe del ResultSet fino a quando non ci sono più righe da elaborare.
		                       ->Se resultSet.next()=false, significa che tutte le righe sono state lette e non ci sono più righe successive nel `ResultSet.
            - In caso di eccezioni SQL, il blocco catch(SQLException e) non fa nulla.
         */


        public void ottieniListaImpiegati(ArrayList<Utente> listaImpiegati){
            listaImpiegati.clear();
            String query = "SELECT * FROM citizenservices.utenti WHERE utenti.stato_contratto = 1;";
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
                    listaImpiegati.add(utente);
                }
            } catch (SQLException e) {
                //restituisce la listaImpiegati vuota
            }
        }



        /* Il metodo ottieniContrattoUtente permette di recuperare  un oggetto di tipo Contratto associato a un utente specifico, dato l'oggetto Utente fornito come argomento.
         - Viene definita una query che è strutturata in 4 parti:
            -> SELECT C.* FROM citizenservices.contratti AS C: Seleziona tutte le colonne (indicato da *) dalla tabella contratti nell'alias C (viene assegnato l'alias C per semplificare il riferimento successivo ai campi di questa tabella.)
                                                                In altre parole, la query restituirà tutti i campi della tabella dei contratti per le righe che soddisfano le condizioni specificate.

            -> INNER JOIN citizenservices.utenti AS U ON C.cod_contratto = U.cod_contratto:  effettua una "join" tra le tabelle contratti (alias C) e utenti (alias U) usando la colonna cod_contratto presente in entrambe le tabelle.
                                                                           Questo significa che verranno combinati i dati delle due tabelle in base al valore della colonna cod_contratto.

            -> WHERE U.matricola = ?: clausola che specifica che affinché una riga sia inclusa nel risultato, il alore della colonna matricola nella tabella utenti (alias U) deve essere uguale al valore specificato come parametro nella query (?).

         - COSA FA LA QUERY: estrae tutti i campi dalla tabella contratti per le righe in cui il valore della colonna matricola nella tabella utenti corrisponde al valore specificato come parametro.
                             Questo consente di ottenere tutti i contratti associati a un utente specifico in base alla sua matricola.

         - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
         - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto ResultSet (conterrà tutte le righe selezionate dalla query).
         - (blocco while): utilizzato per iterare attraverso le righe dei risultati estratti dalla query SQL e per estrarre i dati da ciascuna riga; All'interno del ciclo while, viene valutata la condizione resultSet.next() (controlla se ci sono ulteriori righe nel ResultSet.)
		                      -> resultSet.next()=true, esiste una riga successiva nel ResultSet da leggere e viene eseguito il corpo del ciclo:
		                                                 - All'interno del corpo del ciclo, vengono estratti i dati da ciascun colonna della riga corrente del ResultSet utilizzando i metodi getString() o getInt() in base al tipo di dato.
		                                                 - I dati estratti vengono utilizzati per creare l'oggetto "Contratto" corrispondente.
		                                                 - Il ciclo continua ad iterare attraverso le righe del ResultSet fino a quando non ci sono più righe da elaborare.
		                                                 -  Una volta creato, l'oggetto Contratto viene restituito.
		                       ->Se resultSet.next()=false, significa che tutte le righe sono state lette e non ci sono più righe successive nel `ResultSet.
          - In caso di eccezioni SQL, il blocco catch(SQLException e) non fa nulla.
         */


        public Contratto ottieniContrattoUtente(Utente utente){
            String query = "SELECT C.* "+
                    "FROM citizenservices.contratti AS C "+
            "INNER JOIN citizenservices.utenti AS U ON C.cod_contratto = U.cod_contratto " +
            "WHERE U.matricola = ? ;";
            InterfacciaDB DB = new InterfacciaDB();
            try(Connection con = DB.getConnection();
                PreparedStatement pst = con.prepareStatement(query);){
                pst.setString(1,utente.getMatricola());
                ResultSet resultSet = pst.executeQuery();
                while (resultSet.next()) {
                    int cod_contratto = resultSet.getInt("cod_contratto");
                    String data_licenziamento = resultSet.getString("data_licenziamento");
                    String data_assunzione = resultSet.getString("data_assunzione");
                    int cod_stato = resultSet.getInt("cod_stato");
                    String tipo_contratto = resultSet.getString("tipo_contratto");
                    Contratto contratto = new Contratto(cod_contratto, data_licenziamento, data_assunzione, cod_stato, tipo_contratto);
                    return contratto;
                }
            } catch (SQLException e) {
                return null;
            }
            return null;
        }
}
