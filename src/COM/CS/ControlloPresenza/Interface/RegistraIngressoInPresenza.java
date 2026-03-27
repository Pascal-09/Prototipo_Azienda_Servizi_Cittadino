package COM.CS.ControlloPresenza.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.AvvisoSuccesso;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.ControlloPresenza.Control.ControlRegistraPresenza;
import COM.CS.Entity.Turno;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RegistraIngressoInPresenza  extends JFrame{
    private JPanel RegistraIngressoInPresenza;
    private JPanel SezioneIcone;
    private JPanel SezioneInterazione;
    private JTextField InputNome;
    private JTextField InputCognome;
    private JTextField InputMatricola;
    private JLabel LabelNome;
    private JLabel LabelCognome;
    private JLabel LabelMatricola;
    private JButton inviaButton;
    private JLabel IconaPresenza1;
    private JLabel IconaPresenza2;
    private JLabel IconaPreesenza3;

    public RegistraIngressoInPresenza() {
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 400); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(RegistraIngressoInPresenza);
        setResizable(false);
        setVisible(true);
        ControlRegistraPresenza CRP = new ControlRegistraPresenza();
        ArrayList<Turno> turni = new ArrayList<>();
        inviaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = InputNome.getText();
                String cognome = InputCognome.getText();
                String matricola = InputMatricola.getText();
                boolean corrispondenzaTrovata = CRP.corrispondenzaDatiSPR(nome, cognome, matricola);
                CRP.ottieniTurniGiornataDaIniziare(matricola, InterfacciaTMP.ottieniData(), turni);
                Turno turno = null;
                turno = CRP.cercaTurnoImpiegato(matricola, InterfacciaTMP.ottieniOrario(), turni);
                if(corrispondenzaTrovata && turno!=null){
                    int orarioIngresso = CRP.controlloOrarioIngressoPresenza(turno.getOra_inizio());
                    if(orarioIngresso >= 0 && orarioIngresso<= 10){//ingresso in orario
                        CRP.registraPresenzaInOrario(CommonMethods.formattaDataTogli0(InterfacciaTMP.ottieniOrario()),turno);
                        //dovrebbe andare all'autenticazione
                    } else if (orarioIngresso > 10 && orarioIngresso < 40) {//ingresso in ritardo
                        CRP.registraPresenzaInRitardo(CommonMethods.formattaDataTogli0(InterfacciaTMP.ottieniOrario()),turno);
                        AvvisoErrore AE = new AvvisoErrore("si sta registrando un ingresso in ritardo");
                        AE.pack();
                        AE.setVisible(true);
                        //dovrebbe andare all'autenticazione
                    } else if (orarioIngresso > 40) {//assenza
                        AvvisoErrore AE = new AvvisoErrore("<html>è già stata registrata la tua assenza.<br>Hai superato i 40 minuti!</html>");
                        AE.pack();
                        AE.setVisible(true);
                    }
                }
                else{
                    AvvisoErrore AE = new AvvisoErrore("<html>Le informazioni inserite non sono corrette<br>o non è presente un turno nella giornata corrente</html>");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
    }
}
