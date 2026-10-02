package views.viva.views.Entidades.Vendedor.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;
import views.viva.views.Entidades.Evento.Model.eventos;
import views.viva.views.Entidades.Transacao.Model.transacao;
import views.viva.views.Entidades.Vendedor.Model.vendedor;
import views.viva.views.Entidades.Vendedor.Model.vendedorEvento;
import views.viva.views.Funcionalidades.API.APICommunication;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;
import views.viva.views.Funcionalidades.Converter.DateTime.dateTimeConverter;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class vendedorService {

    public int consultaQuantidadeVendedores(String url){
        String token = propertiesController.getProperty("user.token");
        String resultado = APICommunication.ReqGET(url, token);
        JSONObject json = new JSONObject(resultado);
        JSONArray jsonArray = json.getJSONArray("vendedores");
        int totalPages = (int) (Math.ceil(jsonArray.length() / 10.0) * 10);
        return totalPages;
    }

    public int consultaQuantidadeVendedoresEvento(String url){
        String token = propertiesController.getProperty("user.token");
        String resultado = APICommunication.ReqGET(url, token);
        JSONObject json = new JSONObject(resultado);
        JSONArray jsonArray = json.getJSONArray("vendedorEventos");
        int totalPages = (int) (Math.ceil(jsonArray.length() / 10.0) * 10);
        return totalPages;
    }

    public void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private final dateTimeConverter dateTimeConverter = new dateTimeConverter();

    public void cadastrarVendedor(TextField txtNome, TextField txtLogin, TextField txtEmail){
        try {
            if (!isValidEmail(txtEmail)) {
                mostrarAlerta("E-mail Inválido", "Insira um e-mail válido para prosseguir!");
                return;
            }
            String token = propertiesController.getProperty("user.token");
            String body = "{\"login\": \"" + txtLogin.getText() + "\", \"nome\": \"" + txtNome.getText() + "\"}";
            String resultado = APICommunication.ReqPUT("https://tcc-r46r.onrender.com/cadastro/vendedor", token, body);
            JSONObject jsonResponse = new JSONObject(resultado);

            if(jsonResponse.getBoolean("sucesso")){
                mostrarSucesso("Operação bem sucedida!","A loja foi cadastrada com sucesso!");
            }
            else{
                mostrarSucesso("Operação mal sucedida!","A loja não pôde ser cadastrada!\n" +
                        jsonResponse.getString("mensagem"));
            }
        } catch (Exception e ){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro no cadastro de vendedores: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void cadastrarVendedorEvento(ComboBox<vendedor> cblVendedor, ComboBox<eventos> cblEvento){
        try {
            int vendedorSelecionado = cblVendedor.getSelectionModel().getSelectedItem().getIdVendedor();
            int eventoSelecionado = cblEvento.getSelectionModel().getSelectedItem().getIdEvento();
            String token = propertiesController.getProperty("user.token");
            String body = "{\"id_vendedor\": \"" + vendedorSelecionado + "\", \"id_evento\": \"" + eventoSelecionado + "\"}";
            String resultado = APICommunication.ReqPUT("https://tcc-r46r.onrender.com/cadastro/vendedorevento", token, body);
            JSONObject jsonResponse = new JSONObject(resultado);
            if(jsonResponse.getBoolean("sucesso")){
                mostrarSucesso("Operação bem sucedida!","A empresa foi cadastrada com sucesso!");
            }
            else{
                mostrarSucesso("Operação mal sucedida!","A empresa não pôde ser cadastrada!\n" +
                        jsonResponse.getString("mensagem"));
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro no cadastro de vendedor evento: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public List<vendedor> listaVendedores(String url) throws SQLException, ParseException {
        try {
            String token = propertiesController.getProperty("user.token");
            String resultado = APICommunication.ReqGET(url, token);
            JSONObject json = new JSONObject(resultado);
            JSONArray jsonArray = json.getJSONArray("vendedores");
            JSONArray usuarios = json.getJSONArray("usuario");
            ArrayList<vendedor> listaVendedores = new ArrayList<>();

            Map<Integer, String> usuariosMap = new HashMap<>();
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject voucherObject = usuarios.getJSONObject(i);
                int idUsuario = voucherObject.getInt("idUsuario");
                String nomeUsuario = voucherObject.getString("nomeUsuario");
                usuariosMap.put(idUsuario, nomeUsuario);
            }

            for (Object jsonObj : jsonArray) {
                JSONObject vend = (JSONObject) jsonObj;
                int idVendedor = vend.getInt("idVendedor");
                long idUsuario = vend.getInt("idUsuario");
                String loja = vend.getString("loja");
                int responsavel = vend.getInt("responsavel");
                boolean ativoB = vend.getBoolean("ativo");
                String criadoEmString = vend.getString("criadoEm");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                OffsetDateTime criadoEmDate = OffsetDateTime.parse(criadoEmString, dateTimeFormatter);
                Timestamp criadoEm = Timestamp.from(criadoEmDate.toInstant());

                String ativo = ativoB ? "Ativo" : "Desativado";

                String nomeResponsavel = usuariosMap.get(responsavel);

                String dataHora = dateTimeConverter.formataData(criadoEm);
                vendedor vendedor = new vendedor(idVendedor, loja, idUsuario, responsavel, ativo, criadoEm, dataHora, nomeResponsavel);
                listaVendedores.add(vendedor);
            }

            return listaVendedores;
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na seleção de vendedores: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    public void alterarAtivoVendedor(int id_vendedor) throws SQLException {
        try {
            String token = propertiesController.getProperty("user.token");
            String url = "https://tcc-r46r.onrender.com/vendedor/alternarativo/" + id_vendedor;
            String body = "";
            String resultado = APICommunication.ReqPUT(url, token, body);
            JSONObject jsonResponse = new JSONObject(resultado);
            if (jsonResponse.getBoolean("sucesso")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "vendedor ativado/desativado com sucesso", ButtonType.OK);
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Falha na ativação/desativação de vendedor", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro desativação/ativação de vendedor: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void alterarAtivoVendedorEvento(int id_Vendevento) throws SQLException {
        try {
            String token = propertiesController.getProperty("user.token");
            String url = "https://tcc-r46r.onrender.com/vendedorevento/alternarativo/" + id_Vendevento;
            String body = "";
            String resultado = APICommunication.ReqPUT(url, token, body);
            JSONObject jsonResponse = new JSONObject(resultado);
            if (jsonResponse.getBoolean("sucesso")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "vendedor ativado/desativado com sucesso", ButtonType.OK);
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Falha na ativação/desativação de vendedor", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro desativação/ativação de vendedor evento: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public List<vendedorEvento> listaVendedorEventos(String url, int page) throws SQLException, ParseException {
        try {
            String token = propertiesController.getProperty("user.token");
            String resultado = APICommunication.ReqGET(url + page, token);
            JSONObject json = new JSONObject(resultado);

            JSONArray jsonArray = json.getJSONArray("vendedorEventos");
            JSONArray eventos = json.getJSONArray("evento");
            JSONArray vandedores = json.getJSONArray("vendedor");
            JSONArray usuarios = json.getJSONArray("usuario");

            Map<Integer, String> usuariosMap = new HashMap<>();
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject voucherObject = usuarios.getJSONObject(i);
                int idUsuario = voucherObject.getInt("idUsuario");
                String nomeUsuario = voucherObject.getString("nomeUsuario");
                usuariosMap.put(idUsuario, nomeUsuario);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            Map<Integer, String> eventoMap = new HashMap<>();
            for (int i = 0; i < eventos.length(); i++) {
                JSONObject eventoObject = eventos.getJSONObject(i);
                int idEvento = eventoObject.getInt("idEvento");
                String nomeEvento = eventoObject.getString("nomeEvento");
                eventoMap.put(idEvento, nomeEvento);
            }

            Map<Integer, String> vandedorMap = new HashMap<>();
            for (int i = 0; i < vandedores.length(); i++) {
                JSONObject vandedorObject = vandedores.getJSONObject(i);
                int idVendedor = vandedorObject.getInt("idVendedor");
                String nomeLoja = vandedorObject.getString("loja");
                vandedorMap.put(idVendedor, nomeLoja);
            }
            List<vendedorEvento> listaVendEventos = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                vendedorEvento vendedorEventoObj = objectMapper.readValue(jsonArray.getJSONObject(i).toString(), vendedorEvento.class);
                String criadoEmString = String.valueOf(vendedorEventoObj.getCriadoEm());

                Timestamp criadoEm = null;
                if (criadoEmString != null) {
                    LocalDateTime criadoEmDateTime = vendedorEventoObj.getCriadoEm().toLocalDateTime();
                    criadoEm = Timestamp.valueOf(criadoEmDateTime);
                }
                String nomeVendedor = vandedorMap.get(vendedorEventoObj.getIdVendedor());
                String nomeEvento = eventoMap.get(vendedorEventoObj.getIdEvento());
                String dataHora = dateTimeConverter.formataData(criadoEm);

                String ativo = vendedorEventoObj.getAtivo() ? "Ativo" : "Desativado";

                String nomeResponsavel = usuariosMap.get(vendedorEventoObj.getResponsavel());

                vendedorEventoObj.setNomeResponsavel(nomeResponsavel);
                vendedorEventoObj.setAtivoString(ativo);
                vendedorEventoObj.setNomeVendedor(nomeVendedor);
                vendedorEventoObj.setNomeEvento(nomeEvento);
                vendedorEventoObj.setCriadoEmString(dataHora);

                listaVendEventos.add(vendedorEventoObj);

            }

            return listaVendEventos;
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na seleção de vendedores evento: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static boolean isValidEmail(TextField txtEmail) {
        String email = txtEmail.getText();
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }

}

