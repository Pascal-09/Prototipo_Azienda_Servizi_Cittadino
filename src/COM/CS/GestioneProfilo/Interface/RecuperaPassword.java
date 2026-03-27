package COM.CS.GestioneProfilo.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.AvvisoSuccesso;
import COM.CS.GestioneProfilo.Control.ControlRecuperaPassword;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecuperaPassword extends JFrame{
    private JPanel RecuperaPSW;
    private JTextField MatricolaInput;
    private JButton InviaCredenzialiButton;
    private JButton IndietroButton;
    private JLabel InserisciMatrText;
    private JPanel SezioneInterazione;
    private JPanel SezioneIcone;
    private JLabel InviaPswViaMailIcona;
    private JPanel Spazio;

    public RecuperaPassword(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 200); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(RecuperaPSW);
        setResizable(false);
        setVisible(true);
        ControlRecuperaPassword CRP = new ControlRecuperaPassword();
        IndietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Autenticazione A = new Autenticazione();
                A.setVisible(true);
                dispose();
            }
        });

        InviaCredenzialiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matricola = MatricolaInput.getText();
                if(CRP.controlloMatricolaNonVuota(matricola) && CRP.controlloEsistenzaMatricola(matricola)){
                    AvvisoSuccesso AS = new AvvisoSuccesso("il sistema ha inviato all'utente una mail contenente le credenziali in uso");
                    AS.pack();
                    AS.setVisible(true);
                }
                else{
                    AvvisoErrore AE = new AvvisoErrore("la matricola inserita è errata o non esiste. Riprova");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
    }
    public static void main(String[] args){
        new RecuperaPassword();
    }

}
