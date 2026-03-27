package COM.CS.GestioneComunicazioni.Interface;

import COM.CS.Commons.*;
import COM.CS.Entity.Comunicazione;
import COM.CS.GestioneComunicazioni.Control.ControlGestioneComunicazioni;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InviaComunicazione extends JFrame {
    private JPanel InviaComunicazione;
    private JTextField OggettoInput;
    private JButton mandaComunicazioneATuttiButton;
    private JButton mandaComunicazioneAllAmministratoreButton;
    private JButton chiudiButton;
    private JTextArea TestoInput;
    private JPanel oggetto;
    private JPanel contenuto;
    private JPanel delimitazione_area;
    private JPanel sezione_bottoni;

    public InviaComunicazione() { //metodo costruttore, equivalente a create()
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 300); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(InviaComunicazione);
        setResizable(false);
        setVisible(true);

        ControlGestioneComunicazioni CGC = new ControlGestioneComunicazioni();
        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        mandaComunicazioneATuttiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String oggetto = OggettoInput.getText();
                String testo = TestoInput.getText();
                if(oggetto.isBlank() || testo.isBlank()) {
                    AvvisoErrore AE = new AvvisoErrore("l'oggetto o il testo del messaggio è vuoto!");
                    AE.pack();
                    AE.setVisible(true);
                }
                else{
                    int indice = Comunicazione.calcolaCodiceComunicazione();
                    Comunicazione comunicazione1 = new Comunicazione(indice, DUL.getUtente().getMatricola(), null, 0, oggetto, testo, InterfacciaTMP.ottieniDataEOrario());
                    CGC.mandaComunicazioneATutti(comunicazione1);
                    OggettoInput.setText("");
                    TestoInput.setText("");
                }
            }
        });
        mandaComunicazioneAllAmministratoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oggetto = OggettoInput.getText();
                String testo = TestoInput.getText();
                if(oggetto.isBlank() || testo.isBlank()){
                    AvvisoErrore AE = new AvvisoErrore("l'oggetto o il testo del messaggio è vuoto!");
                    AE.pack();
                    AE.setVisible(true);
                }
                else{
                    int indice = Comunicazione.calcolaCodiceComunicazione();
                    Comunicazione comunicazione2 = new Comunicazione(indice, DUL.getUtente().getMatricola(), null, 1, oggetto, testo, InterfacciaTMP.ottieniDataEOrario());
                    CGC.mandaComunicazioneAdmin(comunicazione2);
                    OggettoInput.setText("");
                    TestoInput.setText("");
                }
            }
        });
        chiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
    }
}

