package COM.CS.Entity;

public class Servizio {
    private int cod_servizio;
    private String nome_servizio;
    private String stato;
    public Servizio(int cod_servizio,String nome_servizio,String stato){
        this.cod_servizio=cod_servizio;
        this.nome_servizio=nome_servizio;
        this.stato=stato;
    }

    public void setCod_servizio(int cod_servizio){
        this.cod_servizio=cod_servizio;
    }
    public int getCod_servizio(){
        return this.cod_servizio;
    }
    public void setNome_servizio(String nome_servizio){
        this.nome_servizio=nome_servizio;
    }
    public String getNome_servizio(){
        return this.nome_servizio;
    }
    public void setStato(String stato){
        this.stato=stato;
    }

    public String getStato() {
        return this.stato;
    }
}
