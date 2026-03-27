package COM.CS.GestioneComunicazioni.Interface.Renderer;

import COM.CS.Entity.Servizio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ServizioCellRenderer extends JPanel implements ListCellRenderer<Servizio> {

    private JLabel nomeServizio;
    private JLabel iconaApertura;
    private JLabel statoServizio;
    private JButton visualizzaImpButton;

    public ServizioCellRenderer(){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new LineBorder(Color.BLACK));

        nomeServizio = new JLabel();
        iconaApertura = new JLabel();
        statoServizio = new JLabel();
        visualizzaImpButton = new JButton();
        visualizzaImpButton.setOpaque(true);

        nomeServizio.setBorder(new EmptyBorder(2,10,2,10));
        iconaApertura.setBorder(new EmptyBorder(2,10,2,10));
        iconaApertura.setText("");
        statoServizio.setBorder(new EmptyBorder(2,10,2,10));
        visualizzaImpButton.setBorder(new EmptyBorder(2,10,2,0));

        visualizzaImpButton.setText("Visualizza Impiegati");
        visualizzaImpButton.setBackground(new Color(0,123,255));
        visualizzaImpButton.setForeground(new Color(225,225,225));

        add(nomeServizio);
        add(iconaApertura);
        add(statoServizio);
        add(visualizzaImpButton);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Servizio> list, Servizio value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value!=null){
            nomeServizio.setText(value.getNome_servizio());
            //controllo stato attivo
            switch (value.getStato()){
                case "attivo":
                    iconaApertura.setIcon(new ImageIcon("C:\\Users\\Sveva\\IdeaProjects\\CitizenServices\\src\\icone\\iconaAttiva.png"));
                    break;
                case "chiuso":
                    iconaApertura.setIcon(new ImageIcon("C:\\Users\\Sveva\\IdeaProjects\\CitizenServices\\src\\icone\\iconaChiuso.png"));
                    break;
            }
            statoServizio.setText(value.getStato());
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
