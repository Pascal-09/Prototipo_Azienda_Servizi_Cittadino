package COM.CS.GestioneStipendio.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Entity.Stipendio;
import COM.CS.Entity.Utente;
import COM.CS.GestioneStipendio.Control.ControlGestioneStipendio;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualizzaStipendio extends JFrame{
    private JPanel VisualizzaStipendio;
    private JComboBox meseComboBox;
    private JButton visualizzaStipendioButton;
    private JButton chiudiButton;
    private JLabel TitoloMeseMatricola;
    private JLabel SalarioTotaletext;
    private JLabel GratificheText;
    private JLabel OreStrordinarioText;
    private JLabel TasseText;
    private JLabel StraordinarioText;

    public VisualizzaStipendio(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 300); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(VisualizzaStipendio);
        setResizable(false);
        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        Utente utente = DUL.getUtente();
        ControlGestioneStipendio CGS = new ControlGestioneStipendio();
        TitoloMeseMatricola.setText("Dettagli dello stipendio per Mese - Matricola " + utente.getMatricola());
        String[] mesi = {"gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno", "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre"};
        for(String mese: mesi){
            meseComboBox.addItem(mese);
        }
        setVisible(true);
        visualizzaStipendioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indiceMese = meseComboBox.getSelectedIndex();
                String mese = mesi[indiceMese];
                Stipendio stipendio = CGS.ottieniStipendioImpiegato(utente.getMatricola(), mese);
                if(stipendio!=null){
                    TitoloMeseMatricola.setText("Dettagli dello stipendio per Mese " + mese + " - Matricola " + utente.getMatricola());
                    SalarioTotaletext.setText("Salario totale : " + stipendio.getStipendioTotale() + "€");
                    GratificheText.setText("Gratifiche : " + (stipendio.getOre_serv_1() * 10 + stipendio.getOre_serv_2() * 20 + stipendio.getOre_serv_3() * 30 + stipendio.getOre_serv_4() * 40) + "€");
                    OreStrordinarioText.setText("Ore di straordinario : " + stipendio.getOre_straordinario() + "h" );
                    TasseText.setText("Tasse : " + stipendio.getTasse() + "€");
                    StraordinarioText.setText("Straordinario : " + stipendio.getOre_straordinario() * 50 + "€");
                }
                else {
                    AvvisoErrore AE = new AvvisoErrore("stipendio non disponibile per il mese indicato");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
        chiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
    }
}
