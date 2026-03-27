package COM.CS.GestioneContratti.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.GestioneContratti.Control.ControlGestioneContratti;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TerminaContratto extends JFrame{
    private JPanel TerminaContratto;
    private JTextField MatricolaText;
    private JComboBox MotivoComboBox;
    private JButton terminaContrattoButton;
    private JButton chiudiButton;
    private JPanel SezioneTasti;
    private JLabel DataFineText;

    public TerminaContratto() {
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 320); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(TerminaContratto);
        setResizable(false);

        String dataFine = InterfacciaTMP.ottieniData();
        DataFineText.setText(dataFine);

        String[] motivoLicenziamento = {"inadempienza sul lavoro", "scadenza contratto  tempo determinato"};
        for(String opzione : motivoLicenziamento){
            MotivoComboBox.addItem(opzione);
        }
        ControlGestioneContratti CGC = new ControlGestioneContratti();
        chiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
        terminaContrattoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matricolaImpiegato = MatricolaText.getText();
                int index = MotivoComboBox.getSelectedIndex();
                String motivo = motivoLicenziamento[index];
                boolean matricolaEsiste = CGC.controlloEsistenzaMatricola(matricolaImpiegato);
                boolean campiRiempiti = CGC.controlloDatiFormTerminazioneContratto(matricolaImpiegato, motivo);
                if(matricolaEsiste && campiRiempiti){
                    InserisciPsw IP = new InserisciPsw(matricolaImpiegato, dataFine);
                    IP.setVisible(true);
                    dispose();
                }
                else{
                    AvvisoErrore AE = new AvvisoErrore("i dati inseriti o la matricola non sono corretti, riprova");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
    }
}
