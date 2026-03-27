package COM.CS.Entity;

import COM.CS.Commons.InterfacciaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Richiesta {
    private int cod_richiesta;
    private String ref_utente;
    private int cod_motivo;
    private String nome;
    private String cognome;
    private int statoRichiesta;
    private String inizioPeriodo;
    private String finePeriodo;

    public Richiesta(int cod_richiesta,String ref_utente, int cod_motivo, String nome, String cognome, int statoRichiesta,String inizioPeriodo, String finePeriodo){
        this.cod_richiesta=cod_richiesta;
        this.ref_utente=ref_utente;
        this.cod_motivo=cod_motivo;
        this.nome=nome;
        this.cognome=cognome;
        this.statoRichiesta=statoRichiesta;
        this.inizioPeriodo=inizioPeriodo;
        this.finePeriodo=finePeriodo;
    }

    public void setCod_richiesta(int cod_richiesta){
        this.cod_richiesta=cod_richiesta;
    }
    public int getCod_richiesta(){
        return this.cod_richiesta;
    }
    public void setRef_utente(String ref_utente){
        this.ref_utente=ref_utente;
    }
    public String getRef_utente(){
        return this.ref_utente;
    }
    public void setCod_motivo(int cod_motivo){
        this.cod_motivo=cod_motivo;
    }
    public int getCod_motivo(){
       return this.cod_motivo;
    }

    public void setNome(String nome){
        this.nome=nome;
    }
    public String getNome(){
        return this.nome;
    }
    public void setCognome(String cognome){
        this.cognome=cognome;
    }
    public String getCognome(){
        return this.cognome;
    }
    public void setStatoRichiesta(int statoRichiesta){
        this.statoRichiesta=statoRichiesta;
    }
    public int getStatoRichiesta(){
        return this.statoRichiesta;
    }
    public void setInizioPeriodo(String inizioPeriodo){
        this.inizioPeriodo=inizioPeriodo;
    }
    public String getInizioPeriodo(){
        return this.inizioPeriodo;
    }
    public void setFinePeriodo(String finePeriodo){
        this.finePeriodo=finePeriodo;
    }
    public String getFinePeriodo(){
        return this.finePeriodo;
    }

    public static int calcolaCodiceRichiesta() {
        int c = 0;
        String query = "SELECT * FROM citizenservices.richieste";
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
}
