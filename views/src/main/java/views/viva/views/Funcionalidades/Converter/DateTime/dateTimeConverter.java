package views.viva.views.Funcionalidades.Converter.DateTime;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class dateTimeConverter {

    public String converteHora(Timestamp dataHora){
        try {
            ZonedDateTime zonedDateTime = dataHora.toInstant().atZone(ZoneId.systemDefault());
            LocalTime horaLocalT = zonedDateTime.toLocalTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String horaI = String.valueOf(horaLocalT.format(formatter));
            String hora = " " + horaI;
            return hora;
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na conveção de hora: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    public String converteDia(Timestamp dataHora){
        try {
            ZonedDateTime zonedDateTime = dataHora.toInstant().atZone(ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dataLocal = zonedDateTime.toLocalDate();
            String dataI = String.valueOf(dataLocal.format(formatter));
            String data = " " + dataI;
            return data;
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na converção de dia: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    public String formataData(Timestamp dateTime){
        String data = this.converteDia(dateTime);
        String hora = this.converteHora(dateTime);
        return data + hora;
    }

}
