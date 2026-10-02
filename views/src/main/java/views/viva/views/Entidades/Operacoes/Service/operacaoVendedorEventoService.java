package views.viva.views.Entidades.Operacoes.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.json.JSONArray;
import org.json.JSONObject;
import views.viva.views.Entidades.Operacoes.Model.operacaoEvento;
import views.viva.views.Entidades.Operacoes.Model.operacaoVendedorEvento;
import views.viva.views.Funcionalidades.API.APICommunication;
import views.viva.views.Funcionalidades.Converter.DateTime.dateTimeConverter;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.util.*;

public class operacaoVendedorEventoService {

    private final dateTimeConverter dateTimeConverter = new dateTimeConverter();

    public int consultaQuantidadeOperacaoVendEvento(String url){
        String token = propertiesController.getProperty("user.token");
        String resultado = APICommunication.ReqGET(url, token);
        JSONObject json = new JSONObject(resultado);
        JSONArray jsonArray = json.getJSONArray("operacoes");
        int totalPages = (int) (Math.ceil(jsonArray.length() / 10.0) * 10);
        return totalPages;
    }

    public List<operacaoVendedorEvento> listaOperacaoVendEvento(String url, int page) throws SQLException, ParseException {
        try {
            String token = propertiesController.getProperty("user.token");
            String resultado = APICommunication.ReqGET(url + page, token);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JSONObject json = new JSONObject(resultado);
            JSONArray jsonArray = json.getJSONArray("operacoes");
            JSONArray vendedoresEvento = json.getJSONArray("vendedorEvento");
            JSONArray vendedores = json.getJSONArray("vendedor");
            JSONArray usuarios = json.getJSONArray("usuario");

            Map<Integer, Integer> vendedoresEventoMap = new HashMap<>();
            for (int i = 0; i < vendedoresEvento.length(); i++) {
                JSONObject voucherObject = vendedoresEvento.getJSONObject(i);
                int idVendedorEvento = voucherObject.getInt("idVendedorEvento");
                int idVendedor = voucherObject.getInt("idVendedor");
                vendedoresEventoMap.put(idVendedorEvento, idVendedor);
            }

            Map<Integer, String> vendedoresMap = new HashMap<>();
            for (int i = 0; i < vendedores.length(); i++) {
                JSONObject voucherObject = vendedores.getJSONObject(i);
                int idVendedor = voucherObject.getInt("idVendedor");
                String loja = voucherObject.getString("loja");
                vendedoresMap.put(idVendedor, loja);
            }

            Map<Integer, String> usuariosMap = new HashMap<>();
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject voucherObject = usuarios.getJSONObject(i);
                int idUsuario = voucherObject.getInt("idUsuario");
                String nomeUsuario = voucherObject.getString("nomeUsuario");
                usuariosMap.put(idUsuario, nomeUsuario);
            }

            ArrayList<operacaoVendedorEvento> listaoperacaoVendedorEvento = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                operacaoVendedorEvento operacaoVendedorEventoObj = objectMapper.readValue(jsonArray.getJSONObject(i).toString(), operacaoVendedorEvento.class);
                String dataOperacaoString = String.valueOf(operacaoVendedorEventoObj.getDataOperacao());

                Timestamp dataOperacao = null;
                if (dataOperacaoString != null) {
                    LocalDateTime dataOperacaoDateTime = operacaoVendedorEventoObj.getDataOperacao().toLocalDateTime();
                    dataOperacao = Timestamp.valueOf(dataOperacaoDateTime);
                }
                String nomeVendedor = vendedoresMap.get(vendedoresEventoMap.get(operacaoVendedorEventoObj.getIdVendedorEvento()));
                String nomeResponsavel = usuariosMap.get(operacaoVendedorEventoObj.getResponsavel());

                String dataHora = dateTimeConverter.formataData(dataOperacao);

                String tipo = converteTipo(operacaoVendedorEventoObj.getTipo());

                operacaoVendedorEventoObj.setTipo(tipo);
                operacaoVendedorEventoObj.setDataHora(dataHora);
                operacaoVendedorEventoObj.setNomeVendedor(nomeVendedor);
                operacaoVendedorEventoObj.setNomeResponsavel(nomeResponsavel);

                listaoperacaoVendedorEvento.add(operacaoVendedorEventoObj);
            }
            return listaoperacaoVendedorEvento;
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na consulta de operações de vendedor evento: " + e.getMessage(), ButtonType.OK);
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
