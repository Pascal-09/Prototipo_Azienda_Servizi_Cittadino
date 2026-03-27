package COM.CS.Entity;

public class Utente{
    private String matricola;
    private int cod_contratto;
    private String nome;
    private String cognome;
    private String telefono;
    private String IBAN;
    private String mail;
    private int ruolo;
    private int stato_contratto;
    private String cod_fiscale;
    private int ore_da_svolgere;
    public Utente(String matricola, int cod_contratto, String nome, String cognome, String telefono, String IBAN, String mail, int ruolo, int stato_contratto, String cod_fiscale, int ore_da_svolgere){
        this.matricola = matricola;
        this.cod_contratto = cod_contratto;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.IBAN = IBAN;
        this.mail = mail;
        this.ruolo = ruolo;
        this.stato_contratto = stato_contratto;
        this.cod_fiscale = cod_fiscale;
        this.ore_da_svolgere = ore_da_svolgere;
    }


    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }
    public String getMatricola(){
        return this.matricola;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public  String getNome(){
        return this.nome;
    }
    public void setCognome(String cognome){
        this.cognome = cognome;
    }
    public String getCognome(){
        return this.cognome;
    }
    public void setCod_contratto(int cod_contratto){
        this.cod_contratto = cod_contratto;
    }
    public int getCod_contratto(){
        return this.cod_contratto;
    }
    public void setStato_contratto(int stato_contratto){
        this.stato_contratto = stato_contratto;
    }
    public int getStato_contratto(){
        return this.stato_contratto;
    }
    public void setTelefono(String telefono){
        this.telefono = telefono;
    }
    public String getTelefono(){
        return this.telefono;
    }
    public void setIBAN(String IBAN){
        this.IBAN = IBAN;
    }
    public String getIBAN(){
        return this.IBAN;
    }
    public void setMail(String mail){
        this.mail = mail;
    }
    public String getMail(){
        return this.mail;
    }
    public void setRuolo(int ruolo){
        this.ruolo = ruolo;
    }
    public int getRuolo(){
        return this.ruolo;
    }
    public String getCod_fiscale(){
        return this.cod_fiscale;
    }
    public void setCod_fiscale(String cod_fiscale){
        this.cod_fiscale = cod_fiscale;
    }
    public void setOreDaSvolgere(int ore_da_svolgere){
        this.ore_da_svolgere = ore_da_svolgere;
    }
    public int getOre_da_svolgere(){
        return this.ore_da_svolgere;
    }

    public void sottraiOreDaSvolgere(int h){
        this.ore_da_svolgere = this.ore_da_svolgere - h;
    }
}
