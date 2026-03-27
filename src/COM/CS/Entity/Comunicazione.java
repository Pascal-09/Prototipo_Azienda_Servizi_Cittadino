package COM.CS.Entity;

import COM.CS.Commons.InterfacciaDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Comunicazione{
    private int cod_comunicazione;
    private String ref_mittente;
    private String ref_destinatario;
    private int tipo_comunicazione;
    private String testo;
    private String orario_invio;

    private String oggetto;


    //Definisco i costruttori della classe.
    public Comunicazione(int cod_comunicazione, String ref_mittente, String ref_destinatario, int tipo_comunicazione, String oggetto, String testo, String orario_invio){
        this.cod_comunicazione=cod_comunicazione;
        this.ref_mittente=ref_mittente;
        this.ref_destinatario=ref_destinatario;
        this.tipo_comunicazione=tipo_comunicazione;
        this.oggetto = oggetto;
        this.testo=testo;
        this.orario_invio=orario_invio;
    }
    //Metodo Set per cod comunicazione:
    public void setCod_comunicazione(int cod_comunicazione){
        this.cod_comunicazione=cod_comunicazione;
    }
    //Metodo Get per cod comunicazione:
    public int getCod_comunicazione(){
        return this.cod_comunicazione;
    }
    //Metodo Set per ref_mittente:
    public void setRef_mittente(String ref_mittente){
        this.ref_mittente=ref_mittente;
    }
    //Metodo Get per ref_mittente
    public String getRef_mittente(){
        return this.ref_mittente;
    }
    //Metodo Set per ref_destinatario;
    public void setRef_destinario(String ref_destinatario){
        this.ref_destinatario=ref_destinatario;
    }
    //Metodo Get per ref_destinario
    public String getRef_destinatario(){
        return this.ref_destinatario;
    }
    //Metodo Set per tipo_comunicazione
    public void setTipo_comunicazione(int tipo_comunicazione){
        this.tipo_comunicazione=tipo_comunicazione;
    }
    //Metodo Get per tipo_comunicazione
    public int getTipo_comunicazione(){
        return this.tipo_comunicazione;
    }
    //Metodo Set per testo
    public void setTesto(String testo){
        this.testo=testo;
    }
    //Metodo Get per testo
    public String getTesto(){
        return this.testo;
    }
    //Metodo Set per Orario_invio
    public void setOrario_invio(String orario_invio){
        this.orario_invio=orario_invio;
    }
    //Metodo Get per Orario_invio
    public String getOrarioInvio(){
        return this.orario_invio;
    }

    public void setOggetto(String oggetto){
        this.oggetto = oggetto;
    }
    public String getOggetto(){
        return this.oggetto;
    }

    public static int calcolaCodiceComunicazione() {
        int c = 0;
        String query = "SELECT * FROM citizenservices.comunicazioni";
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
