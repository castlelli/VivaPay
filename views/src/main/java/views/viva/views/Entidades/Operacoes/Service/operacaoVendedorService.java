package views.viva.views.Entidades.Operacoes.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.json.JSONArray;
import org.json.JSONObject;
import views.viva.views.Entidades.Operacoes.Model.operacaoEvento;
import views.viva.views.Entidades.Operacoes.Model.operacaoVendedor;
import views.viva.views.Funcionalidades.API.APICommunication;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import views.viva.views.Funcionalidades.Converter.DateTime.dateTimeConverter;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

public class operacaoVendedorService {

    private final dateTimeConverter dateTimeConverter = new dateTimeConverter();

    public int consultaQuantidadeOperacaoVendedor(String url){
        String token = propertiesController.getProperty("user.token");
        String resultado = APICommunication.ReqGET(url, token);
        JSONObject json = new JSONObject(resultado);
        JSONArray jsonArray = json.getJSONArray("operacoes");
        int totalPages = (int) (Math.ceil(jsonArray.length() / 10.0) * 10);
        return totalPages;
    }

    public List<operacaoVendedor> listaOperacaoVendedor(String url, int page) {
        try {
            String token = propertiesController.getProperty("user.token");
            String resultado = APICommunication.ReqGET(url + page, token);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JSONObject json = new JSONObject(resultado);
            JSONArray jsonArray = json.getJSONArray("operacoes");
            JSONArray vendedores = json.getJSONArray("vendedor");
            JSONArray usuarios = json.getJSONArray("usuario");

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

            ArrayList<operacaoVendedor> listaOperacoesVendedor = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                operacaoVendedor operacaoVendedorObj = objectMapper.readValue(jsonArray.getJSONObject(i).toString(), operacaoVendedor.class);
                String dataOperacaoString = String.valueOf(operacaoVendedorObj.getDataOperacao());

                Timestamp dataOperacao = null;
                if (dataOperacaoString != null) {
                    LocalDateTime dataOperacaoDateTime = operacaoVendedorObj.getDataOperacao().toLocalDateTime();
                    dataOperacao = Timestamp.valueOf(dataOperacaoDateTime);
                }

                String nomeVendedor = vendedoresMap.get(operacaoVendedorObj.getIdVendedor());
                String nomeResponsavel = usuariosMap.get(operacaoVendedorObj.getResponsavel());

                String dataHora = dateTimeConverter.formataData(dataOperacao);

                String tipo = converteTipo(operacaoVendedorObj.getTipo());

                operacaoVendedorObj.setTipo(tipo);
                operacaoVendedorObj.setDataHora(dataHora);
                operacaoVendedorObj.setNomeVendedor(nomeVendedor);
                operacaoVendedorObj.setNomeResponsavel(nomeResponsavel);

                listaOperacoesVendedor.add(operacaoVendedorObj);
            }
            return listaOperacoesVendedor;
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
