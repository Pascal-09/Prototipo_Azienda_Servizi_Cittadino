package COM.CS.ControlloPresenza.Control;

import COM.CS.Commons.InterfacciaDB;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.Entity.Turno;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlControlloPresenza {

    public void ottieniRitardiEAssenzeDelGiorno(String data, ArrayList<Turno> turni) {
        turni.clear();
        String dataFormattata = CommonMethods.formattaDataTogli0(data);
        String query = "SELECT * FROM citizenservices.turni WHERE turni.data = ? AND turni.cod_tipo_presenza != 0 AND turni.cod_tipo_presenza != 1 ;";
        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, dataFormattata);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                String ref_matricola = resultSet.getString("ref_matricola");
                int cod_servizio = resultSet.getInt("cod_servizio");
                String dataDB = resultSet.getString("data");
                int cod_tipo_presenza = resultSet.getInt("cod_tipo_presenza");
                String ora_inizio = resultSet.getString("ora_inizio");
                String ora_fine = resultSet.getString("ora_fine");
                Turno turno = new Turno(ref_matricola, cod_servizio, dataDB, cod_tipo_presenza, ora_inizio, ora_fine);
                turni.add(turno);
            }
        }
        catch (SQLException e){
            System.out.println("errore ottieni Ritardi e assenze del giorno");
        }
    }

    public void ottieniListaPresenzeImpiegato(ArrayList<Turno> turni){
        turni.clear();
        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        String dataFormattata = CommonMethods.formattaDataTogli0(InterfacciaTMP.ottieniData());
        String query = "SELECT * FROM citizenservices.turni WHERE turni.data = ? AND turni.ref_matricola = ?;";
        InterfacciaDB DB = new InterfacciaDB();
        try(Connection con = DB.getConnection();
            PreparedStatement pst = con.prepareStatement(query);) {
            pst.setString(1, dataFormattata);
            pst.setString(2, DUL.getUtente().getMatricola());
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                String ref_matricola = resultSet.getString("ref_matricola");
                int cod_servizio = resultSet.getInt("cod_servizio");
                String dataDB = resultSet.getString("data");
                int cod_tipo_presenza = resultSet.getInt("cod_tipo_presenza");
                String ora_inizio = resultSet.getString("ora_inizio");
                String ora_fine = resultSet.getString("ora_fine");
                Turno turno = new Turno(ref_matricola, cod_servizio, dataDB, cod_tipo_presenza, ora_inizio, ora_fine);
                turni.add(turno);
            }
        }
        catch (SQLException e){
            System.out.println("errore ottieni presenze impiegato");
        }
    }
    public boolean controlloListaVuota(ArrayList<Turno> turni) {
        return turni.isEmpty();
    }
}
