package COM.CS.ControlloPresenza.Interface;

import COM.CS.ControlloPresenza.Control.ControlControlloPresenza;
import COM.CS.ControlloPresenza.Interface.Renderer.PresenzeCellRenderer;
import COM.CS.Entity.Turno;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.CustomListModel;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VisualizzaPresenze extends JFrame {
    private JPanel VisualizzaPresenze;
    private JButton chiudiButton;
    private JList ListaPresenze;

    public VisualizzaPresenze(ArrayList<Turno> turni, ControlControlloPresenza CCP, DatiUtenteLoggato DUL){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 400); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(VisualizzaPresenze);
        setResizable(false);
        setVisible(true);

        CustomListModel<Turno> listModel = new CustomListModel<>(turni);
        PresenzeCellRenderer renderer = new PresenzeCellRenderer();
        ListaPresenze.setModel(listModel);
        ListaPresenze.setCellRenderer(renderer);

        chiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
    }
}
