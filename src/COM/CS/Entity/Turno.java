package COM.CS.Entity;

public class Turno {
    private String ref_matricola;
    private int cod_servizio;
    private String data;
    private int cod_tipo_presenza;
    private String ora_inizio;
    private String ora_fine;
    public Turno(String ref_matricola, int cod_servizio, String data,int cod_tipo_presenza, String ora_inizio, String ora_fine){
        this.ref_matricola=ref_matricola;
        this.cod_servizio=cod_servizio;
        this.data=data;
        this.cod_tipo_presenza=cod_tipo_presenza;
        this.ora_inizio=ora_inizio;
        this.ora_fine=ora_fine;
    }
    public void setRef_matricola(String ref_matricola){
        this.ref_matricola=ref_matricola;
    }
    public String getRef_matricola(){
        return this.ref_matricola;
    }
    public void setCod_servizio(int cod_servizio){
        this.cod_servizio=cod_servizio;
    }

    public int getCod_servizio() {
        return this.cod_servizio;
    }

    public void setData(String data){
        this.data=data;
    }
    public String getData(){
        return this.data;
    }
    public void setCod_tipo_presenza(int cod_tipo_presenza){
        this.cod_tipo_presenza=cod_tipo_presenza;
    }
    public int getCod_tipo_presenza(){
        return this.cod_tipo_presenza;
    }
    public void setOra_inizio(String ora_inizio){
        this.ora_inizio=ora_inizio;
    }
    public String getOra_inizio(){
        return this.ora_inizio;
    }

    public void setOra_fine(String ora_fine) {
        this.ora_fine = ora_fine;
    }

    public String getOra_fine() {
        return this.ora_fine;
    }

    public String toString(){
        return "giorno " + data + ", dalle ore " + ora_inizio + " alle ore " + ora_fine + ", nel servizio " + cod_servizio;
    }
}
