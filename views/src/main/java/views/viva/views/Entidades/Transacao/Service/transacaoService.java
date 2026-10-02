package views.viva.views.Entidades.Transacao.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import views.viva.views.Entidades.Evento.Model.eventos;
import views.viva.views.Entidades.Transacao.Model.transacao;
import views.viva.views.Funcionalidades.API.APICommunication;
import views.viva.views.Funcionalidades.Converter.DateTime.dateTimeConverter;
import views.viva.views.Funcionalidades.Impressora.*;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class transacaoService {
    String token = propertiesController.getProperty("user.token");
   // Integer voucherGerado;
    private final dateTimeConverter dateTimeConverter = new dateTimeConverter();

    public void deposito(TextField txtVoucher, TextField txtVal, ComboBox<eventos> cbmEvento,ComboBox<String> cbmMetodo){
        String metodo = verificaMetodo(cbmMetodo);
        String numericValue = txtVal.getText().replaceAll("[^\\d,]", "");
        numericValue = numericValue.replace(",", ".");
        TextField txtValor = new TextField();
        txtValor.setText(String.valueOf(numericValue));
        if(!txtVoucher.getText().isEmpty()){
                JSONObject jsonObj = this.depositoComVoucher(txtVoucher, txtValor, metodo);
                System.out.println(jsonObj.getString("mensagem"));
                this.verificaSucesso(jsonObj, jsonObj.getString("mensagem"), "Erro ao realizar deposito", true);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Este voucher já existe! Deseja imprimi-lo novamente? ", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                    Impressao.ImprimirComVoucher(jsonObj, txtVoucher.getText());
                }
        }
        else {
                JSONObject jsonResponseUsuario = this.cadastroUsuario();
                boolean sucessoUsuario = this.verificaSucesso(jsonResponseUsuario, "", "", false);
                if (sucessoUsuario) {
                    JSONObject jsonResponseCliente = this.cadastroCliente(jsonResponseUsuario);
                    boolean sucessoCliente = this.verificaSucesso(jsonResponseCliente, "", "", false);
                    if (sucessoCliente) {
                        JSONObject jsonResponseClienteEvento = this.cadastroClienteEvento(jsonResponseCliente, cbmEvento);
                        boolean sucessoClienteEvento = this.verificaSucesso(jsonResponseCliente, "", "", false);
                        if (sucessoClienteEvento) {
                            JSONObject jsonResponseDeposito = this.depositoSemVoucher(jsonResponseClienteEvento, txtValor, metodo);
                            Impressao.ImprimirSemVoucher(jsonResponseClienteEvento,txtValor.getText(),metodo);
                            this.verificaSucesso(jsonResponseDeposito, jsonResponseDeposito.getString("mensagem"), "Erro ao realizar deposito", true);
                        }
                    }
                }
        }
    }

    public void estorno(TextField txtVoucher, TextField txtVal, ComboBox<eventos> cbmEvento,ComboBox<String> cbmMetodo){
        String numericValue = txtVal.getText().replaceAll("[^\\d,]", "");
        numericValue = numericValue.replace(",", ".");
        TextField txtValor = new TextField();
        txtValor.setText(String.valueOf(numericValue));
        try {
            String token = propertiesController.getProperty("user.token");
            String body = "{\"voucher\": \"" + txtVoucher.getText() + "\", \"valor\": \"" + txtValor.getText() + "\"}";
            String resultado = APICommunication.ReqPUT("https://tcc-r46r.onrender.com/caixa/extorno", token, body);
            JSONObject jsonResponse = new JSONObject(resultado);
            this.verificaSucesso(jsonResponse, "Suceso ao realizar estorno\n" + jsonResponse.getString("mensagem") , "Erro ao realizar estorno", true);
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao realizar estorno", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public int consultaQuantidadeTransacoes(String url){
        String token = propertiesController.getProperty("user.token");
        String resultado = APICommunication.ReqGET(url, token);
        JSONObject json = new JSONObject(resultado);
        JSONArray jsonArray = json.getJSONArray("transacoes");
        int totalPages = (int) (Math.ceil(jsonArray.length() / 10.0) * 10);
        return totalPages;
    }

    public List<transacao> listaTransacao(String url) throws SQLException, ParseException {
        try {
            String token = propertiesController.getProperty("user.token");
            String resultado = APICommunication.ReqGET(url, token);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JSONObject json = new JSONObject(resultado);
            JSONArray jsonArray = json.getJSONArray("transacoes");
            JSONArray vouchers = json.getJSONArray("vouchers");

            Map<Integer, String> voucherMap = new HashMap<>();
            for (int i = 0; i < vouchers.length(); i++) {
                JSONObject voucherObject = vouchers.getJSONObject(i);
                int idClienteEvento = voucherObject.getInt("idClienteEvento");
                String voucher = voucherObject.getString("voucher");
                voucherMap.put(idClienteEvento, voucher);
            }

            List<transacao> listaTransacoes = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                transacao transacaoObj = objectMapper.readValue(jsonArray.getJSONObject(i).toString(), transacao.class);
                String criadoEmString = String.valueOf(transacaoObj.getCriadoEm());

                Timestamp criadoEm = null;
                if (criadoEmString != null) {
                    LocalDateTime criadoEmDateTime = transacaoObj.getCriadoEm().toLocalDateTime();
                    criadoEm = Timestamp.valueOf(criadoEmDateTime);
                }

                double valor = transacaoObj.getValor();
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator(',');
                symbols.setGroupingSeparator('.');
                DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

                String valorString = "R$ " + df.format(valor);

                String voucher = voucherMap.get(transacaoObj.getIdClienteEvento());
                String dia = dateTimeConverter.converteDia(criadoEm);
                String hora = dateTimeConverter.converteHora(criadoEm);

                transacaoObj.setValorString(valorString);
                transacaoObj.setValor(valor);
                transacaoObj.setVoucher(voucher);
                transacaoObj.setDia(dia);
                transacaoObj.setHora(hora);

                listaTransacoes.add(transacaoObj);
            }

            return listaTransacoes;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na consulta de transações: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return new ArrayList<>();
        }
    }

    public List<transacao> listaTransacaoRelatorio(String url) throws SQLException, ParseException {
        try {
            String token = propertiesController.getProperty("user.token");
            String resultado = APICommunication.ReqGET(url, token);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JSONObject json = new JSONObject(resultado);
            JSONArray jsonArray = json.getJSONArray("transacoes");
            JSONArray vouchers = json.getJSONArray("vouchers");
            double valorTipo = json.getDouble("valorTipo");
            String valorTipoString = json.getString("valorTipoString");

            Map<Integer, String> voucherMap = new HashMap<>();
            for (int i = 0; i < vouchers.length(); i++) {
                JSONObject voucherObject = vouchers.getJSONObject(i);
                int idClienteEvento = voucherObject.getInt("idClienteEvento");
                String voucher = voucherObject.getString("voucher");
                voucherMap.put(idClienteEvento, voucher);
            }

            List<transacao> listaTransacoes = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                transacao transacaoObj = objectMapper.readValue(jsonArray.getJSONObject(i).toString(), transacao.class);
                String criadoEmString = String.valueOf(transacaoObj.getCriadoEm());

                Timestamp criadoEm = null;
                if (criadoEmString != null) {
                    LocalDateTime criadoEmDateTime = transacaoObj.getCriadoEm().toLocalDateTime();
                    criadoEm = Timestamp.valueOf(criadoEmDateTime);
                }

                double valor = transacaoObj.getValor();
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator(',');
                symbols.setGroupingSeparator('.');
                DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

                String valorString = "R$ " + df.format(valor);

                String voucher = voucherMap.get(transacaoObj.getIdClienteEvento());
                String dia = dateTimeConverter.converteDia(criadoEm);
                String hora = dateTimeConverter.converteHora(criadoEm);

                transacaoObj.setValorString(valorString);
                transacaoObj.setValor(valor);
                transacaoObj.setVoucher(voucher);
                transacaoObj.setDia(dia);
                transacaoObj.setHora(hora);
                transacaoObj.setValorTipo(valorTipo);
                transacaoObj.setValorTipoString(valorTipoString);

                listaTransacoes.add(transacaoObj);
            }

            return listaTransacoes;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na consulta de transações: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return new ArrayList<>();
        }
    }


    public String verificaMetodo(ComboBox<String> cbmMetodo){
        if(cbmMetodo.getSelectionModel().getSelectedItem() == "Pix"){
            return "P";
        }
        if(cbmMetodo.getSelectionModel().getSelectedItem() == "Cartão"){
            return "C";
        }
        if(cbmMetodo.getSelectionModel().getSelectedItem() == "Dinheiro"){
            return "D";
        }
        else{
            return "Erro ao escolher metodo";
        }
    }

    public boolean verificaSucesso(JSONObject jsonObj, String sucesso, String fracasso, boolean exibe){
        if(jsonObj.getBoolean("sucesso")){
            if(exibe){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, sucesso, ButtonType.OK);
                alert.showAndWait();
            }
            return true;
        }
        else{
            if(exibe) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, fracasso, ButtonType.OK);
                alert.showAndWait();
            }
            return false;
        }
    }

    public JSONObject depositoComVoucher(TextField txtVoucher, TextField txtValor,String metodo){
        try {
            String token = propertiesController.getProperty("user.token");
            String body = "{\"voucher\": \"" + txtVoucher.getText() + "\", \"valor\": \"" + txtValor.getText() + "\", \"metodo\": \"" + metodo + "\"}";
            String resultado = APICommunication.ReqPUT("https://tcc-r46r.onrender.com/caixa/deposito",token,body);
            JSONObject jsonResponse = new JSONObject(resultado);
            return jsonResponse;
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro no deposito com voucher: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    public JSONObject cadastroUsuario(){
        try{
            String body = "{\"nome\": \"\", \"senha\": \"\", \"login\": \"\"}";
            String resultadoUsuario = APICommunication.ReqPUT("https://tcc-r46r.onrender.com/cadastro/usuario",token,body);
            JSONObject jsonResponseUsuario = new JSONObject(resultadoUsuario);
            return jsonResponseUsuario;
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro no cadastro de usuario: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return null;
        }

    }

    public JSONObject cadastroCliente(JSONObject jsonResponseUsuario){
        try {
            JSONObject jsonObject = jsonResponseUsuario.getJSONObject("usuario");
            int idUsuario = jsonObject.getInt("idUsuario");
            String bodyCliente = "{\"id_usuario\": \"" + idUsuario + "\"}";
            String resultadoCliente = APICommunication.ReqPUT("https://tcc-r46r.onrender.com/cadastro/cliente", token, bodyCliente);
            JSONObject jsonResponseCliente = new JSONObject(resultadoCliente);
            return jsonResponseCliente;
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro no cadastro de clietne: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    public JSONObject cadastroClienteEvento(JSONObject jsonResponseCliente, ComboBox<eventos> cbmEvento){
        try {
            JSONObject jsonObjectCLiente = jsonResponseCliente.getJSONObject("cliente");
            int idCLiente = jsonObjectCLiente.getInt("idCliente");
            int idEvento = cbmEvento.getSelectionModel().getSelectedItem().getIdEvento();
            String bodyClienteEvento = "{\"id_cliente\": \"" + idCLiente + "\", \"id_evento\": \"" + idEvento + "\"}";
            String resultadoClienteEvento = APICommunication.ReqPUT("https://tcc-r46r.onrender.com/cadastro/clienteevento", token, bodyClienteEvento);
            JSONObject jsonResponseClienteEvento = new JSONObject(resultadoClienteEvento);
            return jsonResponseClienteEvento;
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro no cadastro de cliente evento: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    public JSONObject depositoSemVoucher(JSONObject jsonResponseClienteEvento, TextField txtValor, String metodo){
        try{
            JSONObject jsonObjectClienteEvento = jsonResponseClienteEvento.getJSONObject("clienteEvento");
            int voucher = jsonObjectClienteEvento.getInt("voucher");
            String bodyDeposito = "{\"voucher\": \"" + Integer.toString(voucher) + "\", \"valor\": \"" + txtValor.getText() + "\", \"metodo\": \"" + metodo + "\"}";
            String resultadoDeposito = APICommunication.ReqPUT("https://tcc-r46r.onrender.com/caixa/deposito", token, bodyDeposito);
            JSONObject jsonResponseDeposito = new JSONObject(resultadoDeposito);
            return jsonResponseDeposito;
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro no cadastro de cliente evento: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    public void consultaSaldo(TextField txtVoucherSaldo, TextField txtValorSaldo, ComboBox<eventos> cbmEvento) {
        try {
            if (Objects.equals(txtVoucherSaldo.getText(), "")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Por favor insira um voucher", ButtonType.OK);
                alert.showAndWait();
            } else {
                String token = propertiesController.getProperty("user.token");
                String resultado = APICommunication.ReqGET("https://tcc-r46r.onrender.com/caixa/saldo/" + txtVoucherSaldo.getText(), token);
                JSONObject jsonResponse = new JSONObject(resultado);
                if (jsonResponse.getBoolean("sucesso")) {
                    double valor = jsonResponse.getDouble("saldo");
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setDecimalSeparator(',');
                    symbols.setGroupingSeparator('.');
                    DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

                    String saldo = df.format(valor);
                    txtValorSaldo.setText("R$ " + saldo);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "voucher não encontrado", ButtonType.OK);
                    alert.showAndWait();

                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao Buscar saldo", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
