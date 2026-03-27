package COM.CS.GestioneTurnazione.Interface.Renderer;

import COM.CS.Entity.Utente;
import COM.CS.GestioneTurnazione.Interface.VisualizzaTurnazione;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ListaImpiegatiTurnoSettimanaCellRenderer extends JPanel implements ListCellRenderer<Utente> {
    private JLabel NomeLabel;
    private JLabel CognomeLabel;
    private JLabel MatricolaLabel;
    private JLabel RuoloLabel;
    public JButton VisualizzaTurnazioneSettimanale;

    public ListaImpiegatiTurnoSettimanaCellRenderer(){
        setLayout(new BorderLayout()); // Initialize BorderLayout without gaps

        setBorder(new LineBorder(Color.BLACK));

        NomeLabel = new JLabel();
        CognomeLabel = new JLabel();
        MatricolaLabel = new JLabel();
        RuoloLabel = new JLabel();
        VisualizzaTurnazioneSettimanale = new JButton();

        NomeLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        CognomeLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        MatricolaLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        RuoloLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        VisualizzaTurnazioneSettimanale.setBorder(new EmptyBorder(10, 5, 5, 5));
        VisualizzaTurnazioneSettimanale.setText("Visualizza Turnazione Settimanale");

        VisualizzaTurnazioneSettimanale.setBackground(new Color(0, 123, 255));
        VisualizzaTurnazioneSettimanale.setForeground(new Color(255, 255, 255));

        JPanel nameCognomePanel = new JPanel();
        nameCognomePanel.setLayout(new BorderLayout());
        nameCognomePanel.add(CognomeLabel, BorderLayout.NORTH);
        nameCognomePanel.add(RuoloLabel, BorderLayout.CENTER);
        nameCognomePanel.setBackground(new Color(255,255,255));

        add(MatricolaLabel, BorderLayout.NORTH);
        add(nameCognomePanel, BorderLayout.SOUTH);
        add(NomeLabel, BorderLayout.CENTER);
        add(VisualizzaTurnazioneSettimanale, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Utente> list, Utente value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            MatricolaLabel.setText("Matricola: " + value.getMatricola());
            NomeLabel.setText("Nome: " + value.getNome());
            CognomeLabel.setText("Cognome: " + value.getCognome());
            RuoloLabel.setText("Ruolo: " + value.getRuolo());
        }
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
