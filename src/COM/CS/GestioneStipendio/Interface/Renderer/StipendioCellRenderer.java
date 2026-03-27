package COM.CS.GestioneStipendio.Interface.Renderer;

import COM.CS.Entity.Stipendio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class StipendioCellRenderer extends JPanel implements ListCellRenderer<Stipendio>{
    JLabel MatricolaLabel;
    JLabel StipendioLabel;
    public StipendioCellRenderer(){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new LineBorder(Color.BLACK));

        MatricolaLabel = new JLabel();
        StipendioLabel = new JLabel();

        MatricolaLabel.setBorder(new EmptyBorder(5,5,5,5));
        StipendioLabel.setBorder(new EmptyBorder(5,5,5,5));


        add(MatricolaLabel, BorderLayout.NORTH);
        add(StipendioLabel, BorderLayout.CENTER);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Stipendio> list, Stipendio value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value!=null){
            MatricolaLabel.setText("Matricola : " + value.getRef_matricola());
            StipendioLabel.setText("Stipendio da accreditare: " + value.getStipendioTotale() + "€");
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
