package COM.CS.GestioneTurnazione.Interface.Renderer;

import COM.CS.Entity.Richiesta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ListaRichiesteCellRenderer extends JPanel implements ListCellRenderer<Richiesta> {
    JPanel infoRichiedente;
    JPanel infoRichiesta;
    JLabel NomeLabel;
    JLabel CognomeLabel;
    JLabel MatricolaLabel;
    JLabel InizioLabel;
    JLabel FineLabel;
    JLabel MotivoLabel;
    JLabel StatoLabel;

    public ListaRichiesteCellRenderer(){
        setLayout(new BorderLayout());
        setBorder(new LineBorder(Color.BLACK));

        infoRichiedente = new JPanel();
        infoRichiesta = new JPanel();
        NomeLabel = new JLabel();
        CognomeLabel = new JLabel();
        MatricolaLabel = new JLabel();
        InizioLabel = new JLabel();
        FineLabel = new JLabel();
        MotivoLabel = new JLabel();
        StatoLabel = new JLabel();

        NomeLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        CognomeLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        MatricolaLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        InizioLabel.setBorder(new EmptyBorder(5,5,5,5));
        FineLabel.setBorder(new EmptyBorder(5,5,5,5));
        MotivoLabel.setBorder(new EmptyBorder(5,5,5,5));
        StatoLabel.setBorder(new EmptyBorder(5,5,5,5));

        infoRichiesta.setBackground(new Color(255,255,255));
        infoRichiedente.setBackground(new Color(255,255,255));

        infoRichiedente.add(NomeLabel,BorderLayout.NORTH);
        infoRichiedente.add(CognomeLabel, BorderLayout.CENTER);
        infoRichiedente.add(MatricolaLabel, BorderLayout.SOUTH);

        infoRichiesta.add(InizioLabel, BorderLayout.NORTH);
        infoRichiesta.add(FineLabel, BorderLayout.CENTER);
        infoRichiesta.add(MotivoLabel, BorderLayout.SOUTH);

        add(infoRichiedente, BorderLayout.NORTH);
        add(infoRichiesta, BorderLayout.CENTER);
        add(StatoLabel, BorderLayout.SOUTH);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Richiesta> list, Richiesta value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value!=null){
            MatricolaLabel.setText("Matricola: " + value.getRef_utente());
            NomeLabel.setText("Nome: " + value.getNome());
            CognomeLabel.setText("Cognome: " + value.getCognome());
            InizioLabel.setText("Inizio Astensione: " + value.getInizioPeriodo());
            FineLabel.setText("Fine Astensione: " + value.getFinePeriodo());
            switch (value.getCod_motivo()){
                case 0:
                    MotivoLabel.setText("Motivo: congedo parentale");
                    break;
                case 1:
                    MotivoLabel.setText("Motivo: sciopero");
                    break;
                case 3:
                    MotivoLabel.setText("Motivo: ferie");
                    break;
                default:
                    MotivoLabel.setText("Motivo: ");
                    break;
            }
            StatoLabel.setText("Stato: in Sospeso");
            if (isSelected) {
                infoRichiesta.setBackground(list.getSelectionBackground());
                infoRichiedente.setBackground(list.getSelectionBackground());
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                infoRichiedente.setBackground(new Color(255,255,255));
                infoRichiesta.setBackground(new Color(255,255,255));
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
        }
        return this;
    }
}
