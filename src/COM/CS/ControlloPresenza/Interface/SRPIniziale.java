package COM.CS.ControlloPresenza.Interface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SRPIniziale extends JFrame{
    private JPanel SRPIniziale;
    private JButton Ingresso;
    private JButton Uscita;

    public SRPIniziale(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(SRPIniziale);
        setResizable(false);
        setVisible(true);
        Ingresso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistraIngressoInPresenza RIIP = new RegistraIngressoInPresenza();
                RIIP.setVisible(true);
                dispose();
            }
        });
        Uscita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistraUscita RU = new RegistraUscita();
                RU.setVisible(true);
                dispose();
            }
        });
    }
}
