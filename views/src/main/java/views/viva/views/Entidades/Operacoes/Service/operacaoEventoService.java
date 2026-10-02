package views.viva.views.Entidades.Operacoes.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.json.JSONArray;
import org.json.JSONObject;
import views.viva.views.Entidades.Operacoes.Model.operacaoEvento;
import views.viva.views.Funcionalidades.API.APICommunication;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.util.*;

import views.viva.views.Funcionalidades.Converter.DateTime.dateTimeConverter;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

public class operacaoEventoService {
    private final dateTimeConverter dateTimeConverter = new dateTimeConverter();

    public int consultaQuantidadeOperacaoEvento(String url){
        String token = propertiesController.getProperty("user.token");
        String resultado = APICommunication.ReqGET(url, token);
        JSONObject json = new JSONObject(resultado);
        JSONArray jsonArray = json.getJSONArray("operacoes");
        int totalPages = (int) (Math.ceil(jsonArray.length() / 10.0) * 10);
        return totalPages;
    }

    public List<operacaoEvento> listaOperacaoEvento(String url, int page) throws SQLException, ParseException {
        try {
            String token = propertiesController.getProperty("user.token");
            String resultado = APICommunication.ReqGET(url + page, token);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JSONObject json = new JSONObject(resultado);
            JSONArray jsonArray = json.getJSONArray("operacoes");
            JSONArray eventos = json.getJSONArray("evento");
            JSONArray usuarios = json.getJSONArray("usuario");

            Map<Integer, String> eventosMap = new HashMap<>();
            for (int i = 0; i < eventos.length(); i++) {
                JSONObject voucherObject = eventos.getJSONObject(i);
                int idEvento = voucherObject.getInt("idEvento");
                String nomeEvento = voucherObject.getString("nomeEvento");
                eventosMap.put(idEvento, nomeEvento);
            }

            Map<Integer, String> usuariosMap = new HashMap<>();
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject voucherObject = usuarios.getJSONObject(i);
                int idUsuario = voucherObject.getInt("idUsuario");
                String nomeUsuario = voucherObject.getString("nomeUsuario");
                usuariosMap.put(idUsuario, nomeUsuario);
            }

            ArrayList<operacaoEvento> listaOperacoesEvento = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                operacaoEvento operacaoEventoObj = objectMapper.readValue(jsonArray.getJSONObject(i).toString(), operacaoEvento.class);
                String dataOperacaoString = String.valueOf(operacaoEventoObj.getDataOperacao());

                Timestamp dataOperacao = null;
                if (dataOperacaoString != null) {
                    LocalDateTime dataOperacaoDateTime = operacaoEventoObj.getDataOperacao().toLocalDateTime();
                    dataOperacao = Timestamp.valueOf(dataOperacaoDateTime);
                }
                String nomeEvento = eventosMap.get(operacaoEventoObj.getIdEvento());
                String nomeResponsavel = usuariosMap.get(operacaoEventoObj.getResponsavel());

                String dataHora = dateTimeConverter.formataData(dataOperacao);

                String tipo = converteTipo(operacaoEventoObj.getTipo());

                operacaoEventoObj.setTipo(tipo);

                operacaoEventoObj.setDataHora(dataHora);
                operacaoEventoObj.setNomeEvento(nomeEvento);
                operacaoEventoObj.setNomeResponsavel(nomeResponsavel);

                listaOperacoesEvento.add(operacaoEventoObj);
            }
            return listaOperacoesEvento;
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na busca por operações de evento: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    private String converteTipo(String tipoChar) {
        if(Objects.equals(tipoChar, "A")){
            return "Ativo";
        }
        if(Objects.equals(tipoChar, "D")){
            return "Desativado";
        }
        else {
            return "Erro ao descompilar tipo";
        }
    }

}
