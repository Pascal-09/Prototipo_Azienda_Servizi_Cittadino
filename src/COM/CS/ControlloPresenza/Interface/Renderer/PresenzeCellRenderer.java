package COM.CS.ControlloPresenza.Interface.Renderer;

import COM.CS.Entity.Turno;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PresenzeCellRenderer extends JPanel implements ListCellRenderer<Turno> {
    private JLabel data;
    private JLabel servizio;
    private JLabel orario;
    private JLabel presenza;

    private JPanel infoTurno;
    public PresenzeCellRenderer(){
        setLayout(new BorderLayout());

        setBorder(new LineBorder(Color.BLACK));

        infoTurno = new JPanel();
        data = new JLabel();
        servizio = new JLabel();
        orario = new JLabel();
        presenza = new JLabel();

        infoTurno.add(data, BorderLayout.NORTH);
        infoTurno.add(orario, BorderLayout.CENTER);
        infoTurno.add(servizio, BorderLayout.SOUTH);

        infoTurno.setBackground(new Color(255,255,255));
        presenza.setForeground(new Color(13, 152, 253));
        add(infoTurno, BorderLayout.NORTH);
        add(presenza, BorderLayout.CENTER);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Turno> list, Turno value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value!=null){
            data.setText("Data: " + value.getData());
            servizio.setText("Servizio: " + value.getCod_servizio());
            orario.setText("dalle ore " + value.getOra_inizio() + " alle ore " + value.getOra_fine());
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
