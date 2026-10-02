package views.viva.views.Entidades.Evento.Service;

import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;
import views.viva.views.Entidades.Evento.Model.eventos;
import views.viva.views.Funcionalidades.API.APICommunication;
import views.viva.views.Funcionalidades.Converter.DateTime.dateTimeConverter;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.getProperty;

public class eventoService {

    private final dateTimeConverter dateTimeConverter = new dateTimeConverter();

    private void mostrarSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static int consultaQuantidadeEvento(String url){
        String token = propertiesController.getProperty("user.token");
        String resultado = APICommunication.ReqGET(url, token);
        JSONObject json = new JSONObject(resultado);
        JSONArray jsonArray = json.getJSONArray("eventos");
        int totalPages = (int) (Math.ceil(jsonArray.length() / 10.0) * 10);
        return totalPages;
    }

    public void cadastrarEvento(TextField txtNomeEvento, TextField txtDescricaoEvento, TextField txtAbreviacaoEvento,
                                   DatePicker dtpHoraInicio, DatePicker dtpHoraFinal, TextField txtHoraInicio, TextField txtHoraFim) {
        try {
            LocalDate dataInicio = dtpHoraInicio.getValue();
            LocalDate dataFim = dtpHoraFinal.getValue();
            String dataInicioString = dataInicio.format(DateTimeFormatter.ISO_LOCAL_DATE);
            String dataFimString = dataFim.format(DateTimeFormatter.ISO_LOCAL_DATE);
            String dataHoraInicio = dataInicioString + "T" + txtHoraInicio.getText() + "Z";
            String dataHoraFim = dataFimString + "T" + txtHoraFim.getText() + "Z";

            String token = getProperty("user.token");
            String body = "{" + "\"nomeEvento\": \"" + txtNomeEvento.getText() + "\", " + "\"descricaoEvento\": \""
                    + txtDescricaoEvento.getText() + "\", " + "\"abreviacaoEvento\": \"" + txtAbreviacaoEvento.getText()
                    + "\", " + "\"horaInicio\": \"" + dataHoraInicio + "\", " + "\"horaFinal\": \""
                    + dataHoraFim + "\"" + "}";
            String resultado = APICommunication.ReqPUT("https://tcc-r46r.onrender.com/cadastro/evento", token, body);
            JSONObject jsonResponse = new JSONObject(resultado);
            if(jsonResponse.getBoolean("sucesso")){
                mostrarSucesso("Operação bem sucedida!","O evento foi cadastrado com sucesso!");
            } else{
                mostrarSucesso("Operação mal sucedida!","O evento não pôde ser cadastrado!\n" +
                        jsonResponse.getString("mensagem"));
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro no cadastro de eventos: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void zeraCampos(TextField txtNomeEvento, TextField txtDescricaoEvento, TextField txtAbreviacaoEvento,
                           DatePicker dtpHoraInicio, DatePicker dtpHoraFinal, TextField txtHoraInicio, TextField txtHoraFim){
        try {
            txtNomeEvento.setText("");
            txtDescricaoEvento.setText("");
            txtAbreviacaoEvento.setText("");
            dtpHoraInicio.setValue(null);
            dtpHoraFinal.setValue(null);
            txtHoraInicio.setText("");
            txtHoraFim.setText("");
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao apagar campos: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public List<eventos> listaEventos(String url) throws SQLException, ParseException {
        try {
            String token = getProperty("user.token");
            String resultado = APICommunication.ReqGET(url, token);
            JSONObject json = new JSONObject(resultado);
            JSONArray jsonArray = json.getJSONArray("eventos");
            JSONArray usuarios = json.getJSONArray("usuario");

            Map<Integer, String> usuariosMap = new HashMap<>();
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject voucherObject = usuarios.getJSONObject(i);
                int idUsuario = voucherObject.getInt("idUsuario");
                String nomeUsuario = voucherObject.getString("nomeUsuario");
                usuariosMap.put(idUsuario, nomeUsuario);
            }

            ArrayList<eventos> listaEventos = new ArrayList<>();

            for (Object jsonObj : jsonArray) {
                JSONObject event = (JSONObject) jsonObj;
                int idEvento = event.getInt("idEvento");
                int idInstituicao = event.getInt("idInstituicao");
                int responsavel = event.getInt("responsavel");
                String nomeEvento = event.getString("nomeEvento");
                String abreviacaoEvento = event.getString("abreviacaoEvento");
                String descricaoEvento = event.getString("descricaoEvento");
                boolean ativo = event.getBoolean("ativo");
                String horaInicioString = event.getString("horaInicio");
                String horaFinalString = event.getString("horaFinal");
                String criadoEmString = event.getString("criadoEm");

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                OffsetDateTime horaInicioDate = OffsetDateTime.parse(horaInicioString, dateTimeFormatter);
                OffsetDateTime horaFinalDate = OffsetDateTime.parse(horaFinalString, dateTimeFormatter);
                OffsetDateTime criadoEmDate = OffsetDateTime.parse(criadoEmString, dateTimeFormatter);

                Timestamp horaInicio = Timestamp.from(horaInicioDate.toInstant());
                Timestamp horaFinal = Timestamp.from(horaFinalDate.toInstant());
                Timestamp criadoEm = Timestamp.from(criadoEmDate.toInstant());


                String nomeResponsavel = usuariosMap.get(responsavel);

                String dataHoraCria = dateTimeConverter.formataData(horaInicio);
                String dataHoraFim = dateTimeConverter.formataData(horaFinal);
                String dataHoraIni = dateTimeConverter.formataData(criadoEm);

                eventos evento = new eventos(idEvento, idInstituicao, responsavel, ativo, nomeEvento, abreviacaoEvento, descricaoEvento, horaInicio, horaFinal, criadoEm, dataHoraCria, dataHoraIni, dataHoraFim, nomeResponsavel);
                listaEventos.add(evento);
            }
            return listaEventos;
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na busca por eventos: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    public void alterarAtivoEventos(int id_evento) throws SQLException {
        try {
            String token = getProperty("user.token");
            String url = "https://tcc-r46r.onrender.com/evento/alternarativo/" + id_evento;
            String body = "";
            String resultado = APICommunication.ReqPUT(url, token, body);
            JSONObject jsonResponse = new JSONObject(resultado);
            if (jsonResponse.getBoolean("sucesso")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Evento ativado/desativado com sucesso", ButtonType.OK);
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Falha na ativação/desativação de eventos", ButtonType.OK);
                alert.showAndWait();
            }
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na ativação/desativação de eventos: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void setTextFieldLimit(TextField textField, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getControlNewText().length() > maxLength) {
                return null;
            }
            return change;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);
    }

}
