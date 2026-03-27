package COM.CS.ControlloPresenza.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.AvvisoSuccesso;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.ControlloPresenza.Control.ControlRegistraPresenza;
import COM.CS.Entity.Turno;
import COM.CS.Utili.CommonMethods;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RegistraUscita extends JFrame{
    private JPanel RegistraUscita;
    private JPanel SezioneIcone;
    private JPanel SezioneInterazione;
    private JTextField InputCognome;
    private JTextField InputMatricola;
    private JButton inviaButton;
    private JLabel IconaUscita1;
    private JLabel IconaUscita2;
    private JLabel IconaUscita3;
    private JLabel LabelNome;
    private JLabel LabelCognome;
    private JLabel LabelMatricola;
    private JTextField InputNome;

    public RegistraUscita() {
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(RegistraUscita);
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
                CRP.ottieniTurniGiornataIniziati(InterfacciaTMP.ottieniData(), matricola, turni);
                Turno turno = null;
                turno = CRP.cercaTurnoIniziatoImpiegato(matricola, turni);
                System.out.println(corrispondenzaTrovata);
                System.out.println(turno);
                if(corrispondenzaTrovata && turno!=null){
                    CRP.registraUscita(CommonMethods.formattaDataTogli0(InterfacciaTMP.ottieniOrario()),turno);
                    AvvisoSuccesso AS = new AvvisoSuccesso("Uscita registrata correttamente");
                    AS.pack();
                    AS.setVisible(true);
                    RegistraIngressoInPresenza RIIP = new RegistraIngressoInPresenza();
                    RIIP.setVisible(true);
                    dispose();
                }
                else{
                    AvvisoErrore AE = new AvvisoErrore("<html>Non è stato possibile reperire <br>le informazioni inserite<br>o la presenza i</html>");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
    }
}
