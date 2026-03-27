package COM.CS.GestioneComunicazioni.Control;


import COM.CS.Commons.InterfacciaDB;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.Entity.Comunicazione;
import COM.CS.Entity.Turno;
import COM.CS.Utili.CommonMethods;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;
public class ControlNotificaRitardoAssenza {

    public static void notificaRitardi(Timer timer){
        timer.scheduleAtFixedRate(new NotificaRitardoTask(), 0, 20 * 60 * 1000);
    }

    public static void notificaAssenze(Timer timer){
        timer.scheduleAtFixedRate(new NotificaAssenzaTask(), 0, 20 * 60 * 1000);
    }

    static class NotificaRitardoTask extends TimerTask{
        @Override
        public void run(){
            ControlNotificaRitardoAssenza CNRA = new ControlNotificaRitardoAssenza();
            CNRA.notificaRitardo();
        }
    }

    static class NotificaAssenzaTask extends TimerTask{
        @Override
        public void run(){
            ControlNotificaRitardoAssenza CNRA = new ControlNotificaRitardoAssenza();
            CNRA.notificaAssenza();
        }
    }


    public void notificaRitardo(){
        ArrayList<Turno> turni = new ArrayList<>();
        ottieniTurniGiornataDaIniziare(InterfacciaTMP.ottieniData(),turni);
        if(!controlloListaTurniVuota(turni)){//ci sono turni da iniziare
            for(Turno turno: turni){
                if(controlloRitardo(turno.getOra_inizio(), InterfacciaTMP.ottieniOrario())){
                    Comunicazione ritardo = new Comunicazione(Comunicazione.calcolaCodiceComunicazione(), null, turno.getRef_matricola(), 2, "notifica ritardo", "aggiunta ritardo al turno " + turno.toString() , InterfacciaTMP.ottieniDataEOrario());
                    mandaNotificaRitardo(ritardo);
                    aggiornaPresenzaTurno(turno);
                }
            }
        }
    }
    private void notificaAssenza(){
        ArrayList<Turno> turni = new ArrayList<>();
        ottieniTurniGiornataInRitardo(turni);
        if(!controlloListaTurniVuota(turni)){
            for(Turno turno: turni){
                if(controlloAssenza(turno.getOra_inizio(), InterfacciaTMP.ottieniOrario())){
                    Comunicazione assenza = new Comunicazione(Comunicazione.calcolaCodiceComunicazione(), null, turno.getRef_matricola(), 3, "notifica assenza", "aggiunta assenza al turno " + turno.toString() , InterfacciaTMP.ottieniDataEOrario());
                    mandaNotificaAssenza(assenza);
                    aggiornaPresenzaTurno(turno);
                }
            }
        }
    }

    private void ottieniTurniGiornataDaIniziare(String data, ArrayList<Turno> turni){
        turni.clear();
        String dataFormattata = CommonMethods.formattaDataTogli0(data);
        String query = "SELECT turni.* FROM citizenservices.turni WHERE turni.data = ? AND turni.cod_tipo_presenza = 0 ORDER BY turni.ora_inizio ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, dataFormattata);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                String ref_matricola = resultSet.getString("ref_matricola");
                int cod_servizio = resultSet.getInt("cod_servizio");
                int cod_tipo_presenza = resultSet.getInt("cod_tipo_presenza");
                String ora_inizio = resultSet.getString("ora_inizio");
                String ora_fine = resultSet.getString("ora_fine");
                Turno turno = new Turno(ref_matricola, cod_servizio, dataFormattata, cod_tipo_presenza, ora_inizio, ora_fine);
                turni.add(turno);
            }
        }
        catch(SQLException e){
            System.out.println("errore ottieni turni giornata da iniziare");
        }
    }

    private void ottieniTurniGiornataInRitardo(ArrayList<Turno> turni){
        turni.clear();
        String dataFormattata = CommonMethods.formattaDataTogli0(InterfacciaTMP.ottieniData());
        String query = "SELECT turni.* FROM citizenservices.turni WHERE turni.data = ? AND turni.cod_tipo_presenza = 2 ORDER BY turni.ora_inizio ;";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, dataFormattata);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                String ref_matricola = resultSet.getString("ref_matricola");
                int cod_servizio = resultSet.getInt("cod_servizio");
                int cod_tipo_presenza = resultSet.getInt("cod_tipo_presenza");
                String ora_inizio = resultSet.getString("ora_inizio");
                String ora_fine = resultSet.getString("ora_fine");
                Turno turno = new Turno(ref_matricola, cod_servizio, dataFormattata, cod_tipo_presenza, ora_inizio, ora_fine);
                turni.add(turno);
            }
        }
        catch(SQLException e){
            DB.lostConnection();
        }
    }

    private boolean controlloListaTurniVuota(ArrayList<Turno> turni){
        return turni.isEmpty();
    }

    private boolean controlloRitardo(String inizioTurno, String orarioAttuale){
        String inizioTurnoFormattato = CommonMethods.formattaDataTogli0(inizioTurno);
        String orarioAttualeFormattato = CommonMethods.formattaDataTogli0(orarioAttuale);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try{
            Date inizioTurnoDate = sdf.parse(inizioTurnoFormattato);
            Date orarioAttualeDate = sdf.parse(orarioAttualeFormattato);
            long differenzaInMillisecondi = orarioAttualeDate.getTime() - inizioTurnoDate.getTime();
            int differenzaInMinuti = (int) (differenzaInMillisecondi / (60 * 1000));
            if(differenzaInMinuti>10 && differenzaInMinuti<40){
                return true;
            }
            else{
                return false;
            }
        }catch (ParseException e){
            System.out.println("errore controlloRitardo");
            return false;
        }
    }

    private boolean controlloAssenza(String inizioTurno, String orarioAttuale){
        String inizioTurnoFormattato = CommonMethods.formattaDataTogli0(inizioTurno);
        String orarioAttualeFormattato = CommonMethods.formattaDataTogli0(orarioAttuale);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try{
            Date inizioTurnoDate = sdf.parse(inizioTurnoFormattato);
            Date orarioAttualeDate = sdf.parse(orarioAttualeFormattato);
            long differenzaInMillisecondi = orarioAttualeDate.getTime() - inizioTurnoDate.getTime();
            int differenzaInMinuti = (int) (differenzaInMillisecondi / (60 * 1000));
            if(differenzaInMinuti>=40){
                return true;
            }
            else{
                return false;
            }
        }catch (ParseException e){
            System.out.println("errore controlloAssenza");
            return false;
        }
    }
    private void mandaNotificaRitardo(Comunicazione comunicazione){
        String query = "INSERT INTO citizenservices.comunicazioni(cod_comunicazione, ref_mittente, ref_destinatario, tipo_comunicazione, testo, orario_invio,oggetto) VALUES (?,?,?,?,?, ?,? )";
        InterfacciaDB DB= new InterfacciaDB();
        try (Connection con=DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)){
            pst.setInt(1, comunicazione.getCod_comunicazione());
            pst.setString(2, comunicazione.getRef_mittente());
            pst.setString(3,comunicazione.getRef_destinatario());
            pst.setInt(4,comunicazione.getTipo_comunicazione());
            pst.setString(5,comunicazione.getTesto());
            pst.setString(6,comunicazione.getOrarioInvio());
            pst.setString(7, comunicazione.getOggetto());
            int righeInserite= pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("errore manda notifica ritardo");
            DB.lostConnection();
        }
    }

    private void mandaNotificaAssenza(Comunicazione comunicazione){
        String query = "INSERT INTO citizenservices.comunicazioni(cod_comunicazione, ref_mittente, ref_destinatario, tipo_comunicazione, testo, orario_invio,oggetto) VALUES (?,?,?,?,?, ?,? )";
        InterfacciaDB DB= new InterfacciaDB();
        try (Connection con=DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query)){
            pst.setInt(1, comunicazione.getCod_comunicazione());
            pst.setString(2, comunicazione.getRef_mittente());
            pst.setString(3,comunicazione.getRef_destinatario());
            pst.setInt(4,comunicazione.getTipo_comunicazione());
            pst.setString(5,comunicazione.getTesto());
            pst.setString(6,comunicazione.getOrarioInvio());
            pst.setString(7, comunicazione.getOggetto());
            int righeInserite= pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("errore manda notifica assenza");
            DB.lostConnection();
        }
    }


    private void aggiornaPresenzaTurno(Turno turno){
        String query = "UPDATE citizenservices.turni SET turni.cod_tipo_presenza = 2 WHERE turni.ref_matricola = ? AND turni.data = ? AND turni.ora_inizio = ? AND turni.ora_fine = ?";
        InterfacciaDB DB = new InterfacciaDB();
        try (Connection con = DB.getConnection();
             PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, turno.getRef_matricola());
            pst.setString(2, turno.getData());
            pst.setString(3, turno.getOra_inizio());
            pst.setString(4, turno.getOra_fine());
            int rowsAffected = pst.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("errore aggiorna presenza turno");
        }
    }
}
