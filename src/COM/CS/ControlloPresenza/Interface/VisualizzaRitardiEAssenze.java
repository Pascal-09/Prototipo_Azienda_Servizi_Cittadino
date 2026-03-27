package COM.CS.ControlloPresenza.Interface;

import COM.CS.ControlloPresenza.Interface.Renderer.RitardiAssenzeCellRenderer;
import COM.CS.Entity.Turno;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.CustomListModel;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VisualizzaRitardiEAssenze extends JFrame{
    private JPanel VisualizzaRitardiEAssenze;
    private JList JListaPresenze;
    private JButton Chiudi;

    public VisualizzaRitardiEAssenze(ArrayList<Turno> turni){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(VisualizzaRitardiEAssenze);
        setResizable(false);
        setVisible(true);
        CustomListModel<Turno> customListModel = new CustomListModel<>(turni);
        RitardiAssenzeCellRenderer PCR = new RitardiAssenzeCellRenderer();
        JListaPresenze.setModel(customListModel);
        JListaPresenze.setCellRenderer(PCR);
        Chiudi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
    }

}



