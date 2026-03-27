package COM.CS.GestioneTurnazione.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Entity.Richiesta;
import COM.CS.Entity.Utente;
import COM.CS.GestioneTurnazione.Control.ControlGestioneRichieste;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RichiestaPeriodoAstensione extends JFrame{
    private JPanel RichiestaPeriodoAstensione;
    private JRadioButton CongedoPRadioButton;
    private JRadioButton scioperoRadioButton;
    private JRadioButton ferieRadioButton;
    private JTextField dataInizioText;
    private JTextField dataFineText;
    private JTextArea MotivoText;
    private JButton inviaRichiestaButton;
    private JButton chiudiButton;

    public RichiestaPeriodoAstensione(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 500); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(RichiestaPeriodoAstensione);
        setResizable(false);
        setVisible(true);
        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        Utente utente = DUL.getUtente();
        ControlGestioneRichieste CGR = new ControlGestioneRichieste();
        chiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonMethods.mostraHomepage(utente.getRuolo());
                dispose();
            }
        });
        inviaRichiestaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inizio = dataInizioText.getText();
                String fine = dataFineText.getText();
                String motivo = MotivoText.getText();
                boolean checkSelected = CongedoPRadioButton.isSelected() || scioperoRadioButton.isSelected() || ferieRadioButton.isSelected();
                boolean checkDate = CGR.controlloDateValide(inizio,fine);
                int n_ore = utente.getOre_da_svolgere();
                int cod_astensione = -1;
                if(CongedoPRadioButton.isSelected()){
                    cod_astensione = 0;
                }
                if(scioperoRadioButton.isSelected()){
                    cod_astensione = 1;
                }
                if(ferieRadioButton.isSelected()){
                    cod_astensione = 2;
                }
                int ore_richiedibili = CGR.ottieniNumeroOreRichiedibili(DUL.getTipoCotratto(), n_ore, cod_astensione);
                boolean checkDays = CGR.controlloRientraNelNumeroOreRichiedibili(inizio,fine, ore_richiedibili);
                if(checkSelected && checkDate && checkDays && !motivo.isBlank()){
                    int cod_richiesta = Richiesta.calcolaCodiceRichiesta();
                    Richiesta richiesta = new Richiesta(cod_richiesta,utente.getMatricola(), cod_astensione, utente.getNome(), utente.getCognome(), 0, inizio, fine);
                    CGR.mandaRichiestaPeriodoAstensione(richiesta);
                }

                else{
                    AvvisoErrore AE = new AvvisoErrore("<html>Controlla che le date inserite siano valide<br> oppure verifica che non sia già stato superato<br> il numero di ore di astensione richiedibili per la settimana </html>");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
        CongedoPRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scioperoRadioButton.setSelected(false);
                ferieRadioButton.setSelected(false);
            }
        });
        scioperoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CongedoPRadioButton.setSelected(false);
                ferieRadioButton.setSelected(false);
            }
        });
        ferieRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CongedoPRadioButton.setSelected(false);
                scioperoRadioButton.setSelected(false);
            }
        });
    }
}
