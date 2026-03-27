package COM.CS.Commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InterfacciaTMP {

    public static String OttieniMese(){
        try {
            String data = ottieniData();
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInput = SDF.parse(data);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateInput);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date primoGiorno = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            Date ultimoGiorno = calendar.getTime();
            String inizio = SDF.format(primoGiorno);
            String fine = SDF.format(ultimoGiorno);
            StringBuilder sb = new StringBuilder();
            sb.append(inizio);
            sb.append(",");
            sb.append(fine);
            return sb.toString();

        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return "";
    }

    public static String OttieniStringaMese(){
        try {
            String data = ottieniData();
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInput = SDF.parse(data);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateInput);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String nomeMese = new SimpleDateFormat("MMMM", Locale.getDefault()).format(dateInput);
            return nomeMese;
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return "";
    }
    public static String OttieniSettimana(){
        try {
            String data = ottieniData();
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
            Date dateInput = SDF.parse(data);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateInput);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            Date startOfWeek = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 6);
            Date endOfWeek = calendar.getTime();
            String inizio = SDF.format(startOfWeek);
            String fine = SDF.format(endOfWeek);
            StringBuilder sb = new StringBuilder();
            sb.append(inizio);
            sb.append(",");
            sb.append(fine);
            return sb.toString();
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return "";
    }
    public static String ottieniData(){
        LocalDateTime dataCorrente = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dataCorrente.format(formatter);
    }

    public static String ottieniOrario(){
        LocalDateTime orarioCorrente = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return orarioCorrente.format(formatter);
    }

    public static String ottieniDataEOrario(){
        LocalDateTime orarioCorrente = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return orarioCorrente.format(formatter);
    }
}
