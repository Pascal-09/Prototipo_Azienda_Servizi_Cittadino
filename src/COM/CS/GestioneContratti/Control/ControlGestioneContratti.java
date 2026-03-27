package COM.CS.GestioneContratti.Control;

import COM.CS.Commons.InterfacciaDB;
import COM.CS.Entity.Contratto;
import COM.CS.Entity.Utente;
import COM.CS.Utili.CommonMethods;

import java.sql.*;
import java.util.Random;

public class ControlGestioneContratti {

    private final Character[] caratteri = {'A','a','B','b','C','c','D','d','E','e','F','f','G','g','H','h','I','i','J','j','K','k','L','l','M','m','N','n','O','o','P','p','Q','q','R','r','S','s','T','t','U','u','V','v','W','w','X','x','Y','y','Z','z','0','1','2','3','4','5','6','7','8','9','+','-','@','*','_'};

    /* Il metodo controlloSintrassiContratti permette di eseguire il controllo di sintassi e validità dei dati inseriti in fase di registrazione di un nuovo contratto.
       Il metodo prende in input diverse stringhe rappresentanti i dati del contratto nome, cognome, email, residenza, IBAN,  telefono, il tipo di impiegato ed il codice fiscale.
       boolean blankCheck = !nome.isBlank() &&... definisce una variabile booleana che vale true se TUTTE le stringhe di input non sono vuote (cioè, non contengono solo spazi bianchi), altrimenti vale false. (controllo che assicura che i campi obbligatori siano compilati)
       boolean residenzaCheck
       Successivamente effettuiamo dei controlli di pattern sui parametri passati in input al metodo grazie al metodo matches() che restituisce un valore booleano se la stringa rispetta l'espressione regolare (che stabilisce il pattern) o false in caso contrario:

        - La variabile booleana residenzaCheck verifica se la stringa "residenza" è formattata correttamente rispettando il pattern definito dall'espressione regolare. Il pattern prevede:
            -> [A-Za-z].+: indica che la stringa deve iniziare con una lettera maiuscola o minuscola per indicare via NomeVia ed il punto indica che dopo la prima lettera, ci possono essere uno o più caratteri di qualsiasi tipo per indicare (numero civico XX)
            -> + .: spazio seguito da uno o più caratteri di qualsiasi tipo.
            -> , :  virgola seguita da uno spazio.
            -> [A-Za-z]+: Deve seguire un blocco di una o più lettere maiuscole o minuscole (per indicare il nome della città).
            -> ,:  virgola seguita da uno spazio.
            -> [A-Z]+: segue un blocco di una o più lettere maiuscole (che rappresenta la provincia (PA, CT) o il codice postale).
            -> Se il parametro residenza passato come input al metodo rispetto tutto il pattern definito, allora residenzaCheck=true, altrimenti residenzaCheck=false.

        - La variabile booleana mailCheck verifica se la stringa "mail" è formattata correttamente rispettando il pattern definito dall'espressione regolare. Il pattern prevede:
            -> [a-zA-Z0-9_.+-]: Indica che la parte locale dell'indirizzo email può contenere lettere maiuscole, lettere minuscole, cifre, e alcuni caratteri speciali come punti, trattini, underscore e plus.
            -> @: Rappresenta il carattere "@" obbligatorio in un indirizzo email.
            -> [a-zA-Z0-9-]+: Indica che il dominio dell'indirizzo email deve contenere lettere maiuscole, lettere minuscole e cifre.
            -> \\.: Rappresenta il punto nel dominio, ma poiché il punto è un carattere speciale in espressioni regolari, è necessario usare due backslash \\.
            -> [a-zA-Z0-9-.]+: Indica che il dominio può contenere lettere maiuscole, lettere minuscole, cifre e trattini.
            -> Se la stringa rispetta il modello specificato nell'espressione regolare, il metodo matches restituirà true, altrimenti restituirà false.

        - La variabile booleana IBANCheck verifica se la stringa IBAN segue un formato specifico comune per i codici IBAN nel formato internazionale. Il pattern prevede:
            -> [A-Z]{2}: Questa parte dell'espressione regolare indica che ci dovrebbero essere esattamente due caratteri maiuscoli consecutivi.
            -> [0-9]{2}: Questa parte indica che ci dovrebbero essere esattamente due cifre numeriche consecutive.
            -> [A-Z0-9]{4}: Si richiedono quattro caratteri consecutivi, che possono essere lettere maiuscole o cifre numeriche.
            -> [0-9]{7}: Questa parte richiede sette cifre numeriche consecutive.
            -> ([A-Z0-9]?){0,16}: Questo pezzo permette zero o più caratteri aggiuntivi, che possono essere lettere maiuscole o cifre numeriche, ma sono opzionali. L'intervallo {0,16} specifica che possono esserci da 0 a 16 di questi caratteri aggiuntivi.
            -> Se la stringa rispetta il modello specificato nell'espressione regolare, il metodo matches restituirà true, altrimenti restituirà false.

        - Diverso è il controllo fatto con telefonoCheck, deputato al solo controllo di cifre nel numero inserito:

           -> Inizialmente la variabile booleana telefonoCheck= true perché si presume che il numero di telefono sia valido (contiene solo cifre).
           -> Viene avviato un ciclo for-each che scorre ogni carattere della stringa telefono (il metodo toCharArray() converte la stringa telefono in un array di caratteri)
           -> All'interno del ciclo, per ciascun carattere c della stringa telefono, viene eseguito il seguente controllo:
              * Character.isDigit(c): verifica se il carattere c è una cifra numerica, ossia un numero da 0 a 9. Se c è una cifra, restituirà true; altrimenti, restituirà false.
              * Se la condizione è vera (cioè, se c non è una cifra), viene eseguita l'istruzione telefonoCheck = false che imposta telefonoCheck=false. Questo significa che il numero di telefono contiene almeno un carattere diverso da una cifra, quindi il risultato del controllo diventa false.

        - Al termine del ciclo viene effettuato un controllo per verificare la sintassi del codice fiscale secondo il pattern sepcificato; Il pattern prevede:
           -> [A-Z0-9]{16}: Questa parte dell'espressione regolare richiede esattamente 16 caratteri consecutivi che possono essere lettere maiuscole o cifre numeriche.

        L'istruzione return alla fine combina il risultato di vari controlli booleani e restituirà true solo se tutti i controlli sono true.
        Altrimenti, se anche uno dei controlli è false, restituirà false, indicando che i dati del contratto non rispettano i criteri di validità e sintassi richiesti.

    */

    public boolean controlloSintrassiContratto(String nome, String cognome, String mail, String residenza, String IBAN, String codFiscale, String telefono, String tipoImpiegato){
        boolean blankCheck = !nome.isBlank() && !cognome.isBlank() && !residenza.isBlank() && !codFiscale.isBlank() && !tipoImpiegato.isBlank() && !mail.isBlank() && !IBAN.isBlank() && !telefono.isBlank();
        boolean residenzaCheck = residenza.matches("^([A-Za-z].+ .+, [A-Za-z]+, [A-Z]+)$");
        boolean mailCheck = mail.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
        boolean IBANCheck = IBAN.matches("^[A-Z]{2}[0-9]{2}[A-Z0-9]{4}[0-9]{7}([A-Z0-9]?){0,16}$");
        boolean telefonoCheck = true;
        for(char c : telefono.toCharArray()){
            if(!Character.isDigit(c)){
                telefonoCheck = false;
            }
        }
        boolean cod_fiscaleCheck = codFiscale.matches("^[A-Z0-9]{16}$");
        return residenzaCheck && blankCheck && mailCheck && IBANCheck && telefonoCheck && cod_fiscaleCheck;


    }

    /* Il metodo verificaEsistenzaCodiceFiscale controlla se un codice fiscale esiste già nel database e restituisce true se non esiste, false se esiste o se si verifica un errore durante la verifica.
    Il metodo definisce una query che seleziona (SELECT) tutte le righe della tabella "utenti" contenute nel dbms "citizenservices" per le quali il "cod_fiscale" è uguale ad un valore non ancora specificato, rappresentato dal segnaposto "?".
    Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed il segnaposto viene sostituito con il valore specifico fornito tramite il PreparedStatement.
    Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare il valore specifico fornito attraverso il metodi setString al segnaposto corrispondente.
    Viene poi definito un blocco if-else per controllare se c'è almeno un risultato nella query:
        - (blocco if )Il metodo next() di resultSet sposta il cursore al primo record restituito dalla query. Se ci sono risultati,  verrà eseguito il blocco di codice all'interno di questo if
                      Quindi il metodo restituirà false a voler sottolineare che esiste già un record nel database con lo stesso codice fiscale.
        - (blocco else) Se non ci sono risultati nella query, ovvero il codice fiscale non esiste nel dbms, il blocco di codice all'interno di questo else verrà eseguito.
                        Quindi il metodo restuirà true a voler sottolineare che il codice fiscale specificato come argomento non esiste ancora nel database.
    Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException restituendo true.
    Questo può significare che c'è stato un errore nella connessione al database o nell'esecuzione della query.
    */

    public boolean verificaEsistenzaCodiceFiscale(String cod_fiscale){
        String query = "SELECT * FROM citizenservices.utenti WHERE utenti.cod_fiscale = ?;";
        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query)){
            pst.setString(1,cod_fiscale);
            ResultSet resultSet = pst.executeQuery();
            if(resultSet.next()){
                return false;
            }
            else{
                return true;
            }
        } catch (SQLException e) {
            return true;
        }
    }

    /* Il metodo creaMatricola prende in input il codice fiscale registrato nel contratto come base per la generazione di una matricola da assegnare all'impiegato.
       - Random rand = new Random() crea l' oggetto "rand" di tipo Random utilizzato per la generazione di numeri casuali.
       - StringBuilder string = new StringBuilder() crea l'oggetto "string" di tipo StringBuilder per costruire una matricola che sia definita dalla concatenazione di diverse sottostringhe.
       - Viene poi definito un blocco do-while:
         (blocco do-whiile) il codice all'interno di questo ciclo verrà eseguito almeno una volta e verrà continuato finché la condizione tra parentesi tonde controlloMatricolaCreata(string.toString()) è vera:
                            -> All'inizio, il codice all'interno del blocco do viene eseguito senza alcuna verifica preliminare:
                                - Inizia un ciclo for che eseguirà 10 iterazioni, una per ogni carattere della matricola.
                                - Viene generato un numero CASUALE tra 0 (incluso) e la lunghezza dell'array caratteri (escluso) utilizzando l'oggetto rand. Il metodo Math.abs è utilizzato per assicurarsi che il numero generato sia sempre positivo.
                                - Il carattere corrispondente alla posizione i-esima del codice fiscale viene aggiunto all'oggetto string utilizzando append.
                                - Viene aggiunto un carattere casuale estratto dall'array caratteri all'oggetto string. Il carattere casuale è scelto in base al numero casuale generato nell'istruzione precedente.
                            -> Dopo aver eseguito la prima volta il blocco di codice all'interno del do, il metodo passa alla verifica della condizione del ciclo while tra parentesi tonde per le iterazioni successive.
                            -> Ad ogni iterazione del ciclo for, string si allunga di due caratteri (uno dal codice fiscale e uno generato casualmente).
                            -> Dopo l'esecuzione di tutti i dieci passaggi del ciclo for, il codice all'interno del ciclo do è completo e viene valutata la condizione del while:
                               - string.toString() prende l'attuale valore di string e lo converte in una stringa.
                               - controlloMatricolaCreata(string.toString()) chiama la funzione controlloMatricolaCreata passando la matricola generata come argomento.
                               - Questa funzione verifica se la matricola generata esiste già nel database:
                                  (Se la matricola esiste) La funzione restituirà true, il ciclo do viene eseguito nuovamente, viene generata una nuova matricola e la condizione del while viene riesaminata.
                                  (Se la matricola non esiste) La funzione restituirà false, il ciclo do-while si interrompe e la matricola univoca viene restituita come risultato del metodo.
    */

    public String creaMatricola(String codice_fiscale){
        Random rand = new Random();
        StringBuilder string = new StringBuilder();
        do {
            for (int i = 0; i < 10; i++) {
                int numeroCasuale = Math.abs(rand.nextInt(caratteri.length));
                string.append(codice_fiscale.charAt(i));
                string.append(caratteri[numeroCasuale]);
            }
        }while(controlloMatricolaCreata(string.toString()));
        return string.toString();
    }

/* Il metodo controlloMatricolaCreata è utilizzato per verificare se una matricola specificata esiste già in un database.
    Viene definita una query che seleziona (SELECT) tutte le righe della tabella "utenti" contenute nel dbms "citizenservices" per le quali la "matricola" è uguale ad un valore non ancora specificato.
    Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed il segnaposto viene sostituito con il valore specifico fornito tramite il PreparedStatement.
    Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare il valore specifico fornito attraverso il metodo setString al segnaposto corrispondente.
    Viene poi definito un blocco if-else per controllare se c'è almeno un risultato nella query:
        - (blocco if )Il metodo next() di resultSet sposta il cursore al primo record restituito dalla query. Se ci sono risultati,  verrà eseguito il blocco di codice all'interno di questo if
                      Quindi il metodo restituirà true a voler sottolineare che esiste già un record nel database con lo stessa matricola.
        - (blocco else) Se non ci sono risultati nella query, ovvero la matricola non esiste nel dbms, il blocco di codice all'interno di questo else verrà eseguito.
                        Quindi il metodo restuirà false a voler sottolineare che la matricla specificata come argomento non esiste ancora nel database.
    Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException restituendo true.
    Questo può significare che c'è stato un errore nella connessione al database o nell'esecuzione della query.
 */

    private boolean controlloMatricolaCreata(String matricola){
        String query = "SELECT * FROM citizenservices.utenti WHERE utenti.matricola = ?;";
        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query)){
            pst.setString(1,matricola);
            ResultSet resultSet = pst.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e) {
            return true;
        }
    }

    /* Il metodo creaPsw è utilizzato per generare una password casuale composta da 6 cifre.
        - Random rand = new Random() crea l' oggetto "rand" di tipo Random utilizzato per la generazione di numeri casuali.
        - StringBuilder string = new StringBuilder() crea l'oggetto "string" di tipo StringBuilder per costruire una psw che sia definita dalla concatenazione di diverse sottostringhe.
        - (ciclo for): eseguirà 6 iterazioni per generare ciascuna delle sei cifre della password:
                      -> All'interno del ciclo, viene generato un numero casuale tra 0 (incluso) e 9 (incluso) utilizzando l'oggetto rand.
                      -> il numero casuale generato viene poi aggiunto all'oggetto string utilizzando il metodo append e questo processo si itera per sei volte.
                      -> Dopo che il ciclo for è completo, string conterrà una sequenza di sei cifre casuali.
        - La sequenza di cifre casuali viene poi restituita come stringa utilizzando il metodo toString() dell'oggetto string.
    */

    public String creaPsw(){
        Random rand = new Random();
        StringBuilder string = new StringBuilder();
        for(int i=0; i<6; i++){
            string.append(rand.nextInt(10));
        }
        return string.toString();

    }

    /* Il metodo calcolaCodiceContratto conta il numero di contratti presenti nel database e restituisce un nuovo codice di contratto incrementato di 1 rispetto al numero totale dei contratti; che può essere utilizzato per identificare un nuovo contratto all'interno del sistema.
        - Viene dichiarata una variabile intera c e inizializzata a 0 per tenere traccia del numero di contratti presenti nel database.
        - Viene definita una query che seleziona (SELECT) tutte le righe della tabella "contratti" contenute nel dbms "citizenservices".
        - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
        - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto ResultSet (conterrà tutte le righe della tabella contratti).
        - (blocco while): Il ciclo while verifica la condizione resultSet.next(), ovvero:   altrimenti restituirà false quando tutte le righe sono state lette.
                          -> Se resultSet.next()=true, esiste una riga successiva nel ResultSet da leggere e viene eseguito il corpo del ciclo:
                                                       - la variabile c viene incrementata di 1 ogni volta che una nuova riga viene letta.
                                                       - Dopo l'incremento, il controllo del ciclo ritorna alla condizione resultSet.next().
                                                       - Il ciclo ripete questo processo finché non si esauriscono le righe nel ResultSet.
                                                       - Dopo che il ciclo while è completo, c conterrà il numero totale di righe della tabella "contratti" presenti nel dbms.
                          -> Se resultSet.next()=false, significa che tutte le righe sono state lette e non ci sono più righe successive nel `ResultSet.
                                                       - Il metodo restituirà c + 1, ovvero restituirà un nuovo codice per un contratto, incrementando il numero di contratti attualmente presenti di 1.
       Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException, restituendo semplicemente 1 come valore predefinito.
       Questo può significare che c'è stato un errore nella connessione al database o nell'esecuzione della query.
    */

    public int calcolaCodiceContratto() {
        int c = 0;
        String query = "SELECT * FROM citizenservices.contratti;";
        InterfacciaDB DB= new InterfacciaDB();
        try(Connection con=DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query)){
            ResultSet resultSet = pst.executeQuery();
            while(resultSet.next()){
                c++;
            }
            return c+1;
        } catch (SQLException e) {
            return 1;
        }
    }

    /* Il metodo aggiungiContratto permette di aggiungere una riga alla tabell "contratto" del dbms "citizenservices"
      - Viene definita una query che inserisce una nuova riga (INSERT INTO) all'interno della tabella "contratti" del dbms "citizenservices" con la dichiarazione dei nomi delle colonne della tabella ed i segnaposto corrispondenti in ordine ai parametri che verranno sostituiti da valori specifici durante l'esecuzione della query
      - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
      - I segnaposto della query vengono sostituiti con i valori dei campi del contratto da aggiungere al database attraverso i metodi setInt, setString, ecc., sulla base dei dati contenuti nell'oggetto contratto.
      - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto rows_affected (che contiene la riga contratto appena inserita nella tabella "contratti" del dbms).
       Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException chiamando il metodo lostConnection dell'oggetto DB che gestisce la mancata connessione al dbms.
    */

    public void aggiungiContratto(Contratto contratto){
        String query1 = "INSERT INTO citizenservices.contratti(cod_contratto, data_licenziamento, data_assunzione, cod_stato, tipo_contratto) VALUES (?, ?, ?, ?,?);";
        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst1 = con.prepareStatement(query1)){
            pst1.setInt(1, contratto.getCod_contratto());
            pst1.setString(2, contratto.getData_licenziamento());
            pst1.setString(3, contratto.getData_assunzione());
            pst1.setInt(4, contratto.getCod_stato());
            pst1.setString(5, contratto.getTipo_contratto());
            pst1.executeUpdate();
        }
        catch(SQLException e){
            System.out.println("errore aggiungi contratto");
            DB.lostConnection();
        }
    }



    /* Il metodo aggiungiUtente permette di aggiungere una riga alla tabella "utente" del dbms "citizenservices"
      - Viene definita una query che inserisce una nuova riga (INSERT INTO) all'interno della tabella "utente" del dbms "citizenservices" con la dichiarazione dei nomi delle colonne della tabella ed i segnaposto corrispondenti in ordine ai parametri che verranno sostituiti da valori specifici durante l'esecuzione della query
      - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst.
      - I segnaposto della query vengono sostituiti con i valori dei campi dell'utente da aggiungere al database attraverso i metodi setInt, setString, ecc., sulla base dei dati contenuti nell'oggetto utente.
      - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto rows_affected (che contiene la riga appena inserita nella tabella "utente" del dbms).
       Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException.

    */

    public void aggiungiUtente(Utente utente, int cod_contratto, String psw){
        String query2 = "INSERT INTO citizenservices.utenti (matricola, cod_contratto, nome, cognome, telefono, IBAN, mail, ruolo, stato_contratto, password, cod_fiscale, ore_da_svolgere) VALUES (?, ?, ?, ?, ? , ? , ?, ?, ? , ?, ?, ?);";
        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst2 = con.prepareStatement(query2)){
            pst2.setString(1, utente.getMatricola());
            pst2.setInt(2, cod_contratto);
            pst2.setString(3, utente.getNome());
            pst2.setString(4, utente.getCognome());
            pst2.setString(5, utente.getTelefono());
            pst2.setString(6, utente.getIBAN());
            pst2.setString(7, utente.getMail());
            pst2.setInt(8, utente.getRuolo());
            pst2.setInt(9, utente.getStato_contratto());
            pst2.setString(10, psw);
            pst2.setString(11, utente.getCod_fiscale());
            pst2.setInt(12, utente.getOre_da_svolgere());
            pst2.executeUpdate();
            pst2.executeUpdate();
        }
        catch(SQLException e){
            System.out.println("errore aggiungi utente");
        }
    }
    //public void mandaCredenzialiNuovoImpiegatoViaMail(String matricola, String password){
            //
    //}

    /* Il metodo controlloEsistenzaMatricola verifica se una matricola specificata esiste nel database e restituisce true se esiste, false se non esiste (o si verifica un errore durante la verifica)
        - Viene definita una query che seleziona (SELECT) dalla tabella "utenti" del dbms "citizenservices" tutte le righe per le quali la "matricola" è uguale ad un valore non ancora specificato.
        - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed il segnaposto viene sostituito con il valore specifico fornito tramite il PreparedStatement.
        - Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare il valore specifico fornito attraverso il metodo setString al segnaposto corrispondente.
        - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto ResultSet (conterrà tutte le righe della tabella utenti con il valore di matricola specificato).
        - Viene utilizzato il metodo resultSet.next() per verificare se la query ha restituito almeno una riga.
            -> Se resultSet.next()= true  significa che la matricola esiste nel database, ed il metodo restituisce true.
            -> Se resultSet.next()= false  significa che la matricola non esiste nel database, ed il metodo restituisce false.
        - Se si verifica un'eccezione di tipo SQLException durante l'esecuzione del codice all'interno del blocco try, il metodo restituirà false.
          Questo può significare che c'è stato un errore nella connessione al database o nell'esecuzione della query.
    */

    public boolean controlloEsistenzaMatricola(String matricola){
        InterfacciaDB DB = new InterfacciaDB();
        String query = "SELECT * FROM citizenservices.utenti WHERE utenti.matricola = ? ;";
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query))
        {
            pst.setString(1, matricola);
            ResultSet resultSet = pst.executeQuery();
            return resultSet.next();
        }
        catch(SQLException ex){
            return false;
        }
    }

    /* Il metodo controlloDatiFormTerminazioneContratto Verificare se le stringhe "matricola" e "motivo" non sono vuote o composte solo da spazi bianchi.
       - Restituisce true se entrambe le condizioni sono vere, cioè se sia la matricola che il motivo NON CONTENGONO SPAZI BIANCHI (O NON SONO RIGHE VUOTE)
       - Restituisce false se almeno una tra "matricola e "motivo" è VUOTA o COMPOSTA SOLO DA SPAZI BIANCHI.
    */

    public boolean controlloDatiFormTerminazioneContratto(String matricola,String motivo){
        return  !matricola.isBlank() && !motivo.isBlank();
    }

    /* Il metodo ottieniPsw permette di ottenere SOLO UNA RIGA contenente la password di un utente associato ad una matricola specificata nel database.
        - Viene definita una query che seleziona il campo password (SELECT password) dalla tabella "utenti" contenuta nel dbms "citizenservices" in cui il campo "matricola" è uguale al valore specificato (rappresentato dal segnaposto ?).
        - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed il segnaposto viene sostituito con il valore specifico fornito tramite il PreparedStatement.
        - Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare il valore specifico fornito attraverso il metodo setString al segnaposto corrispondente.
        - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto ResultSet (conterrà la riga del campo password associata alla matricola)
        - (blocco if): viene utilizzato per determinare se la query SQL ha restituito almeno una riga di risultati tramite la verifica della condizione resultSet.next():
                       -> Se resultSet.next()=true) ci sono risultati da leggere (una matricola corrispondente è stata trovata nel database) e viene eseguito il corpo dell'if:
                                                    - String pswCriptata = resultSet.getString("password"): recupera il valore della colonna "password" dalla riga corrente del risultato (resultSet) e lo memorizza nella variabile pswCriptata (rappresenta la password criptata dell'utente)
                                                    - String psw = CommonMethods.decriptaPsw(pswCriptata): La password criptata (pswCriptata) viene passata al metodo CommonMethods.decriptaPsw(pswCriptata) per essere decriptata.
                                                    - Il risultato della decriptazione viene memorizzato nella variabile psw, che ora contiene la password in chiaro dell'utente.
                                                    - La password decriptata (psw) viene restituita come risultato del metodo.
        - Se si verifica un'eccezione di tipo SQLException durante l'esecuzione del codice all'interno del blocco try, viene gestita la connessione persa al database.
        - Alla fine del metodo, se non è stata restituita una password, viene restituita una stringa vuota.
     */


    public String ottieniPsw(String matricola){
        InterfacciaDB DB = new InterfacciaDB();
        String query = "SELECT password FROM citizenservices.utenti WHERE utenti.matricola = ? ;";
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query))
        {
            pst.setString(1, matricola);
            ResultSet resultSet = pst.executeQuery();
            if(resultSet.next()) {
                String pswCriptata = resultSet.getString("password");
                String psw = CommonMethods.decriptaPsw(pswCriptata);
                return psw;
            }
        }
        catch(SQLException ex){
            DB.lostConnection();
        }
        return "";
    }

    /* Il metodo confrontaPsw serve a confrontare due stringhe, psw e pswDDL (psw del datore di lavoro), per determinare se sono uguali:
       - Se psw=pswDDL) il metodo restituirà true.
       - Se psw!=pswDDL) il metodo restituirà false.
     */

    public boolean confrontaPsw(String psw, String pswDDL){
        return psw.equals(pswDDL);
    }

    /* Il metodo cambiaStatoImpiegatoUtenti permette di aggiornare lo stato del contratto contratto al valore 0 (impiegato licenziato) per quegli utenti il cui numero di matricola è stato specificato nella query.
       - Viene definita una query che aggiorna la riga della tabella "utenti" del dbms "citizenservices" settando a 0 lo stato_contratto (UPDATE...SET...WHERE ) per quell'impiegato la cui matricola è stata specificata nella query stessa.
       - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed il segnaposto viene sostituito con il valore specifico fornito tramite il PreparedStatement.
       - Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare il valore specifico fornito attraverso il metodo setString al segnaposto corrispondente.
       - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto rowsUpdated (conterrà tutte le righe aggiornate della tabella utenti secondo quanto definito nella query precedente)
       - (blocco if): utilizzato per determinare se la query di aggiornamento ha avuto successo nel cambiare lo stato di un impiegato utente.
                     - Se rowsUpdated > 0) Almeno una riga è stata modificata con successo dalla query di aggiornamento.
                                            -> il codice all'interno del blocco if viene eseguito.
                                            -> il metodo restituirà true, a voler sottolineare che la query di aggiornamento è stata eseguita con successo, e almeno una riga è stata modificata nel database (stato_contratto è stato impostato a 0)
                     - Se  rowsUpdated < 0) Nessuna riga è stata modificata.
                                            -> il codice all'interno del blocco else viene eseguito.
                                            -> l metodo restituirà false, a vole sottolineare che la query di aggiornamento potrebbe non aver avuto successo (nessuna riga è stata modificata) o si è verificato un errore durante l'esecuzione.
      - Se si verifica un'eccezione di tipo SQLException durante l'esecuzione del codice all'interno del blocco try, il metodo restituirà false.
     */

    public boolean cambiaStatoImpiegatoUtenti(String matricolaImpiegato){
        String query = "UPDATE citizenservices.utenti SET utenti.stato_contratto = 0 WHERE utenti.matricola = ? ;";
        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query)){
            pst.setString(1,matricolaImpiegato);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                return true;
            } else {
                return false;
            }
        }
        catch (SQLException ex){
            return false;
        }
    }

    /* Il metodo ambiaStatoImpiegatoContratti cambia lo stato di un impiegato nei contratti nel database, impostando il campo cod_stato a 0 e aggiornando la data di licenziamento.
        - Il metodo accetta due argomenti di tipo String, dataLicenziamento e matricolaImpiegato, che rappresentano la data di licenziamento e la matricola dell'impiegato di cui si desidera cambiare lo stato nel contratto.
        - Viene definita una query strutturata in tre parti:
          -> UPDATE citizenservices.contratti: specifica che stiamo eseguendo un'operazione di aggiornamento sulla tabella "contratti" nel dbms"citizenservices".
          -> JOIN citizenservices.utenti ON...:  combina i dati delle tabelle "contratti" e "utenti" sulla base della condizione specificata
                                                 stiamo collegando le righe in cui il campo "cod_contratto" nella tabella "contratti" corrisponde al campo "cod_contratto" nella tabella "utenti".
                                                 Ciò consente di collegare i contratti di un utente specifico utilizzando la matricola come filtro.
                                                 Il merge delle due tabelle serve solo a far figurare la colonna "matricola" che non è presente nella tabella "contratti" ma solo nella tabella "utente"
          -> WHERE citizenservices.utenti.matricola = ?: specifica la condizione in base alla quale vengono apportate le modifiche.
                                                In particolare, stiamo applicando le modifiche alle righe in cui il campo "matricola" nella tabella "utenti" è uguale al valore specificato, che è rappresentato da un altro segnaposto ?.
         - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed i segnaposto vengono sostituiti con i valori specifici forniti tramite il PreparedStatement.
         - Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare il valore specifico fornito attraverso il metodo setString al segnaposto corrispondente.
         - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto rowsUpdated (conterrà tutte le righe aggiornate della tabella utenti secondo quanto definito nella query).
         - (blocco if): utilizzato per determinare se la query di aggiornamento ha avuto successo nel cambiare lo stato di un impiegato utente.
                     - Se rowsUpdated > 0) Almeno una riga è stata modificata con successo dalla query di aggiornamento.
                                            -> il codice all'interno del blocco if viene eseguito.
                                            -> il metodo restituirà true, a voler sottolineare che la query di aggiornamento è stata eseguita con successo, e almeno una riga è stata modificata nel database (stato_contratto è stato impostato a 0)
                     - Se  rowsUpdated < 0) Nessuna riga è stata modificata.
                                            -> il codice all'interno del blocco else viene eseguito.
                                            -> il metodo restituirà false, a vole sottolineare che la query di aggiornamento potrebbe non aver avuto successo (nessuna riga è stata modificata) o si è verificato un errore durante l'esecuzione.
      - Se si verifica un'eccezione di tipo SQLException durante l'esecuzione del codice all'interno del blocco try, il metodo restituirà false.
     */


    public boolean cambiaStatoImpiegatoContratti(String dataLicenziamento, String matricolaImpiegato){
        String query = "UPDATE citizenservices.contratti " +
                "JOIN citizenservices.utenti ON citizenservices.contratti.cod_contratto = citizenservices.utenti.cod_contratto " +
                "SET " +
                "contratti.cod_stato = 0 , " +
                "contratti.data_licenziamento = ? " +
                "WHERE citizenservices.utenti.matricola = ? ;";

        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query)){
            pst.setString(2, dataLicenziamento);
            pst.setString(1, matricolaImpiegato);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
               return true;
            } else {
                return false;
            }
        }
        catch (SQLException ex){
            return false;
        }

    }

    //public void mandaMailTerminazioneContratto(String mail){

    //}


    /* Il metodo aggiornaRuolo ha lo scopo di aggiornare il ruolo di un utente nel database.
       - Viene definita una query che aggiorna la tabella "utenti" del dbms "citizeservices" aggiornando il "ruolo" di un impiegato sulla base della matricola inserita nella query stessa.
       - Dopo la creazione di un oggetto InterfacciaDB e l'apertura di una connessione al dbms, viene preparata un'istruzione SQL istaziando un oggetto PreparedStatement pst ed il segnaposto viene sostituito con il valore specifico fornito tramite il PreparedStatement.
       - Quando la query viene eseguita, il PreparedStatement viene utilizzato per associare il valore specifico fornito attraverso i metodi setString, setInt ai segnaposto corrispondenti.
       - Vengono impostati i valori dei segnaposti nella query con i valori effettivi della matricola e del ruolo utilizzando pst.setString(2, matricola) e pst.setInt(1, ruolo).
       - La query viene inviata al database per l'esecuzione ed il risultato della query viene memorizzato nell'oggetto rowsUpdated (conterrà tutte le righe aggiornate della tabella utenti secondo quanto definito nella query).
       - (blocco if): utilizzato per determinare se la query di aggiornamento ha avuto successo nel cambiare lo stato di un impiegato utente.
                     - Se rowsUpdated > 0) Almeno una riga è stata modificata con successo dalla query di aggiornamento.
                                            -> il codice all'interno del blocco if viene eseguito.
                                            -> il metodo restituirà true, a voler sottolineare che la query di aggiornamento è stata eseguita con successo, e almeno una riga è stata modificata nel database (stato_contratto è stato impostato a 0)
                     - Se  rowsUpdated < 0) Nessuna riga è stata modificata.
                                            -> il codice all'interno del blocco else viene eseguito.
                                            -> il metodo restituirà false, a vole sottolineare che la query di aggiornamento potrebbe non aver avuto successo (nessuna riga è stata modificata) o si è verificato un errore durante l'esecuzione.
        - Nel blocco catch, il codice gestisce le eccezioni di tipo SQLException chiamando il metodo lostConnection dell'oggetto DB che gestisce la mancata connessione al dbms.
    */

    public boolean aggiornaRuolo(String matricola, int ruolo){
        String query = "UPDATE citizenservices.utenti SET utenti.ruolo = ? WHERE utenti.matricola = ? ;";
        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query)){
            pst.setString(2, matricola);
            pst.setInt(1, ruolo);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                return true;
            } else {
                return false;
            }
        }
        catch (SQLException ex){
            System.out.println("errore aggiorna ruolo");
        }
        return false;
    }
}
