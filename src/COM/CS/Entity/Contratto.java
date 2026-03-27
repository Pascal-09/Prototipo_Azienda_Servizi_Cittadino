package COM.CS.Entity;

public class Contratto {
    private int cod_contratto;
    private String data_licenziamento;
    private String data_assunzione;
    private int cod_stato;
    private String tipo_contratto;

    public Contratto (int cod_contratto, String data_licenziamento,String data_assunzione, int cod_stato, String tipo_contratto){
        this.cod_contratto=cod_contratto;
        this.data_licenziamento=data_licenziamento;
        this.data_assunzione=data_assunzione;
        this.cod_stato=cod_stato;
        this.tipo_contratto = tipo_contratto;
    }
    //Metodo Set per cod_contratto:
    public void setCod_contratto(int cod_contratto){
        this.cod_contratto=cod_contratto;
    }
    //Metodo Get per cod_contratto:
    public int getCod_contratto(){
        return this.cod_contratto;
    }
    //Metodo Set per data_licenziamento:

    public void setData_licenziamento(String data_licenziamento){
        this.data_licenziamento=data_licenziamento;
    }
    //Metodo Get per data_licenziamento:
    public String getData_licenziamento(){
        return this.data_licenziamento;
    }
    //Metodo Set per Data assunzione:
    public void setData_assunzione(String data_assunzione){
        this.data_assunzione=data_assunzione;
    }
    //Metodo Get per Data Assunzione:
    public String getData_assunzione(){
        return this.data_assunzione;
    }
    //Metodo Set per cod_stato
    public void setCod_stato(int cod_stato){
        this.cod_stato=cod_stato;
    }
    //Metodo Get per cod_stato
    public int getCod_stato(){
        return this.cod_stato;
    }

    public String getTipo_contratto(){
        return this.tipo_contratto;
    }
    public void setTipo_contratto(String tipo_contratto){
        this.tipo_contratto = tipo_contratto;
    }
}
