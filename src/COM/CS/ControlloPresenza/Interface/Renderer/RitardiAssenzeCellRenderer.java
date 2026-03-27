package COM.CS.ControlloPresenza.Interface.Renderer;

import COM.CS.Entity.Turno;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RitardiAssenzeCellRenderer extends JPanel implements ListCellRenderer<Turno>{
    private JLabel matricola;
    private JLabel servizio;
    private JLabel presenza;
    public RitardiAssenzeCellRenderer(){
        setLayout(new BorderLayout());

        setBorder(new LineBorder(Color.BLACK));


        matricola = new JLabel();
        servizio = new JLabel();
        presenza = new JLabel();

        presenza.setForeground(new Color(13, 152, 253));
        add(matricola, BorderLayout.NORTH);
        add(servizio, BorderLayout.CENTER);
        add(presenza, BorderLayout.SOUTH);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Turno> list, Turno value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value!=null){
            matricola.setText("Matricola: " + value.getRef_matricola());
            servizio.setText("Servizio: " + value.getCod_servizio());
            String tipoPresenza;
            switch (value.getCod_tipo_presenza()){
                case -1 : tipoPresenza = "Assente (A)"; break;
                case 1 : tipoPresenza = "Presente (P)"; break;
                case 2 : tipoPresenza = "Ritardo (R)"; break;
                default: tipoPresenza = "Non ancora svolto"; break;
            }
            presenza.setText("Tipo di Presenza: " + tipoPresenza);
        }
        if(isSelected){
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else{
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
