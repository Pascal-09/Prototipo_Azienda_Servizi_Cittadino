package COM.CS.Entity;

public class Stipendio {
    private String ref_matricola;
    private String mese;
    private int stipendio_base;
    private int ore_serv_1;
    private int ore_serv_2;
    private int ore_serv_3;
    private int ore_serv_4;
    private int ore_straordinario;
    private int tasse;
    private String stato;

    public Stipendio(String ref_matricola,String mese, int stipendio_base, int ore_serv_1,int ore_serv_2, int ore_serv_3, int ore_serv_4, int ore_straordinario, int tasse, String stato){
        this.ref_matricola=ref_matricola;
        this.mese=mese;
        this.stipendio_base=stipendio_base;
        this.ore_serv_1=ore_serv_1;
        this.ore_serv_2=ore_serv_2;
        this.ore_serv_3=ore_serv_3;
        this.ore_serv_4=ore_serv_4;
        this.ore_straordinario = ore_straordinario;
        this.tasse = tasse;
        this.stato = stato;
    }

    public void setRef_matricola(String ref_matricola){
        this.ref_matricola=ref_matricola;
    }
    public String getRef_matricola(){
        return this.ref_matricola;
    }
    public void setMese(String mese){
        this.mese=mese;
    }
    public String getMese(){
        return this.mese;
    }
    public void setStipendio_base(int stipendio_base){
        this.stipendio_base=stipendio_base;
    }
    public int getStipendio_base(){
        return this.stipendio_base;
    }

    public void setOre_serv_1(int ore_serv_1) {
        this.ore_serv_1 = ore_serv_1;
    }
    public int getOre_serv_1(){
        return this.ore_serv_1;
    }
    public void setOre_serv_2(int ore_serv_2){
        this.ore_serv_2=ore_serv_2;
    }
    public int getOre_serv_2(){
        return this.ore_serv_2;
    }
    public void setOre_serv_3(int ore_serv_3){
        this.ore_serv_3=ore_serv_3;
    }
    public int getOre_serv_3(){
        return this.ore_serv_3;
    }
    public void setOre_serv_4(int ore_serv_4){
        this.ore_serv_4=ore_serv_4;
    }
    public int getOre_serv_4(){
        return this.ore_serv_4;
    }
    public void setOre_straordinario(int ore_straordinario){
        this.ore_straordinario=ore_straordinario;
    }
    public int getOre_straordinario(){
        return this.ore_straordinario;
    }
    public void setTasse(int tasse){
        this.tasse=tasse;
    }
    public int getTasse(){
        return this.tasse;
    }
    public void setStato(String stato){
        this.stato = stato;
    }
    public String getStato(){
        return this.stato;
    }

    public int getStipendioTotale(){
        return this.stipendio_base + this.ore_serv_1 * 10 + this.ore_serv_2 * 20 + this.ore_serv_3 * 30 + this.ore_serv_4 * 40 - this.tasse;
    }
}
