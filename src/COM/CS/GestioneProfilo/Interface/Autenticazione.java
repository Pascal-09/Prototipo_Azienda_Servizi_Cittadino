package COM.CS.GestioneProfilo.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;
import COM.CS.Commons.InterfacciaDB;
import COM.CS.Entity.Utente;
import COM.CS.GestioneProfilo.Control.ControlAutenticazione;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Autenticazione extends JFrame{
    //definizione elementi presenti sulla boundary
    private JPanel Autenticazione;
    private JTextField MatrInputID;
    private JPasswordField PswInputID;
    private JLabel MatrTextID;
    private JLabel PswTextID;
    private JButton RecuperaPswButton;
    private JButton accediButton;
    private JLabel IconaID;
    private JLabel IconaPsw;
    private JLabel IconaRecuperoPsw;
    private JLabel IconaAccedi;
    private JPanel SezioneIcone;
    private JPanel SezioneInterazione;

    public Autenticazione(){ //metodo costruttore, equivalente a create()
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(Autenticazione);
        setResizable(false);
        setVisible(true);

        //gestione del click del tasto accedi
        accediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControlAutenticazione CA = new ControlAutenticazione();//creazione controlAutenticazione
                String matrixInput = new String(MatrInputID.getText());
                String pswInput = new String(PswInputID.getText()); //ottenimento dati al click
                boolean checkCredenziali = CA.controlloCredenziali(matrixInput, pswInput); //la control controlla la sintassi di matrice e password
                if(checkCredenziali){ //se le credenziali inserite sono sintatticamente corrette
                    boolean found = CA.corrispondenzaCredenziali(matrixInput, pswInput);//la control controlla se matrice e password esistono sul DBMS
                    if(found){ //se esistono sul DB
                        InterfacciaDB DB = new InterfacciaDB();
                        Utente utente = CA.ottieniDatiUtente(matrixInput);
                        DatiUtenteLoggato DUL = new DatiUtenteLoggato(); //viene creata la entity utente (salvando i dati)
                        DUL.salvaDatiUtente(utente);
                        DUL.setTipoContratto(utente.getMatricola());
                        CommonMethods.mostraHomepage(utente.getRuolo());
                        dispose();
                    }
                    else{//non sono state trovate le credenziali sul DBMS -> messaggio di errore
                        AvvisoErrore AE = new AvvisoErrore("le credenziali inserite non corrispondono ad alcun utente");
                        AE.pack();
                        AE.setVisible(true);
                    }
                }
                else{//le credenziali sono sintatticamente errate -> messaggio di errore
                    AvvisoErrore AE = new AvvisoErrore("le credenziali inserite non sono corrette");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });

        //gestione del click del tasto recuperaPsw
        RecuperaPswButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecuperaPassword RP = new RecuperaPassword();
                RP.setVisible(true);
                dispose();
            }
        });
    }
}
