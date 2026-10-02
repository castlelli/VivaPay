package views.viva.views.Entidades.Relatorio.Controller;

import com.itextpdf.text.DocumentException;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;
import views.viva.views.App;
import views.viva.views.Entidades.Evento.Model.eventos;
import views.viva.views.Entidades.Relatorio.Model.Caixa;
import views.viva.views.Entidades.Relatorio.Service.relatorioService;
import views.viva.views.Entidades.Transacao.Model.transacao;
import views.viva.views.Entidades.Transacao.Service.transacaoService;
import views.viva.views.Funcionalidades.API.APICommunication;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;
import views.viva.views.Funcionalidades.Relatorio.GerarPDF;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.time.format.DateTimeFormatter;

import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.saveProperties;
import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.setProperty;

public class gerarRelatorioController implements Initializable {

    @FXML
    private Button btnDeposito;

    @FXML
    private Button btnEstorno;

    @FXML
    private Button btnVenda;

    @FXML
    private Button btnCaixa;

    @FXML
    private Pane paneSideCadastro;

    @FXML
    private Pane paneSidePagamento;

    @FXML
    private Pane paneSidebar;

    @FXML
    private DatePicker cmbDataInicial;

    @FXML
    private DatePicker cmbDataFinal;

    @FXML
    void paneSideCadastroExited(MouseEvent event) {
        if(paneSideCadastro.isVisible() && sideCad == 1) {
            fechaSideCad();
        }
    }

    @FXML
    void paneSidePagamentoExited(MouseEvent event) {
        if(paneSidePagamento.isVisible() && sidepag == 1) {
            fechaSidePag();
        }
    }

    int verificador;

    String tipo = "Deposito";
    @FXML
    void btnDepositoClicked(MouseEvent event) {
        btnEstorno.setStyle("-fx-background-color: black;");
        btnVenda.setStyle("-fx-background-color: black;");
        btnCaixa.setStyle("-fx-background-color: black;");
        btnDeposito.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        verificador=2;
        tipo = "Deposito";
    }

    @FXML
    public void btnCaixaClicked(MouseEvent mouseEvent) {
        btnEstorno.setStyle("-fx-background-color: black;");
        btnVenda.setStyle("-fx-background-color: black;");
        btnDeposito.setStyle("-fx-background-color: black;");
        btnCaixa.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        verificador=2;
        tipo = "Saldo";
    }

    @FXML
    void btnEstornoClicked(MouseEvent event) {
        btnDeposito.setStyle("-fx-background-color: black;");
        btnVenda.setStyle("-fx-background-color: black;");
        btnCaixa.setStyle("-fx-background-color: black;");
        btnEstorno.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        verificador=2;
        tipo = "Estorno";
    }

    public void btnVendaClicked(MouseEvent mouseEvent) {
        btnEstorno.setStyle("-fx-background-color: black;");
        btnCaixa.setStyle("-fx-background-color: black;");
        btnDeposito.setStyle("-fx-background-color: black;");
        btnVenda.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        verificador=2;
        tipo = "Venda";
    }

    public void btnVendaEntered(MouseEvent mouseEvent) {
        String cor = btnVenda.getStyle();
        if(!cor.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
            btnVenda.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
            verificador=1;
        }
    }

    @FXML
    void btnEstornoEntered(MouseEvent event) {
        String cor = btnEstorno.getStyle();
        if(!cor.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
            btnEstorno.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
            verificador=1;
        }
    }

    @FXML
    void btnDepositoEntered(MouseEvent event) {
        String cor = btnDeposito.getStyle();
        if(!cor.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
            btnDeposito.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
            verificador=1;
        }
    }

    @FXML
    public void btnCaixaEntered(MouseEvent mouseEvent) {
        String cor = btnCaixa.getStyle();
        if(!cor.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
            btnCaixa.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
            verificador=1;
        }
    }

    public void btnVendaExited(MouseEvent mouseEvent) {
        String cor = btnVenda.getStyle();
        if(cor.contains("linear-gradient(to right, #ED9B50 , #d27300)") && verificador == 1){
            btnVenda.setStyle("-fx-background-color: black");
            verificador=2;
        }
    }

    @FXML
    void btnEstornoExited(MouseEvent event) {
        String cor = btnEstorno.getStyle();
        if(cor.contains("linear-gradient(to right, #ED9B50 , #d27300)") && verificador == 1){
            btnEstorno.setStyle("-fx-background-color: black");
            verificador=2;
        }
    }

    @FXML
    void btnDepositoExited(MouseEvent event) {
        String cor = btnDeposito.getStyle();
        if(cor.contains("linear-gradient(to right, #ED9B50 , #d27300)") && verificador == 1){
            btnDeposito.setStyle("-fx-background-color: black");
            verificador=2;
        }
    }
    
    @FXML
    public void btnCaixaExited(MouseEvent mouseEvent) {
        String cor = btnCaixa.getStyle();
        if(cor.contains("linear-gradient(to right, #ED9B50 , #d27300)") && verificador == 1){
            btnCaixa.setStyle("-fx-background-color: black");
            verificador=2;
        }
    }

    @FXML
    void btnGerarClicked(MouseEvent event) throws SQLException, ParseException, DocumentException, FileNotFoundException {
        if (cmbDataFinal.getValue() == null || cmbDataInicial.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Por favor insira as datas", ButtonType.OK);
            alert.showAndWait();
        } else {
            DateTimeFormatter formatterInicial = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataInicial = cmbDataInicial.getValue();
            DateTimeFormatter formatterISOInicial = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dataIniFormatadaISO = dataInicial.format(formatterISOInicial);
            String dataIniFormatada = dataInicial.format(formatterInicial);

            DateTimeFormatter formatterFinal = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataFinal = cmbDataFinal.getValue();
            DateTimeFormatter formatterISOFinal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dataFinalFormatadaISO = dataFinal.format(formatterISOFinal);
            String dataFinalFormatada = dataFinal.format(formatterFinal);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if (!tipo.equals("Saldo")) {
                String type = "";
                if (tipo.equals("Estorno")) {
                    type = "E";
                }
                if (tipo.equals("Deposito")) {
                    type = "C";
                }
                if (tipo.equals("Venda")) {
                    type = "D";
                }
                String url = "https://tcc-r46r.onrender.com/transacao/selecao/portipo/pordata/" + type + "/" + dataIniFormatadaISO + "/" + dataFinalFormatadaISO;

                transacaoService transacaoService = new transacaoService();
                List<transacao> tdsTransacoes = transacaoService.listaTransacaoRelatorio(url);

                relatorioService service = new relatorioService();
                Map<LocalDate, List<transacao>> transacoesPorDia = service.agruparTransacoesPorDia(tdsTransacoes);

                GerarPDF gerarPDF = new GerarPDF();
                gerarPDF.gerarPDF(tipo, dataIniFormatada, dataFinalFormatada, transacoesPorDia, stage);
            } else {
                String url = "https://tcc-r46r.onrender.com/transacao/selecao/tudo" + "/" + dataIniFormatadaISO + "/" + dataFinalFormatadaISO;
                transacaoService transacaoService = new transacaoService();
                List<transacao> tdsTransacoes = transacaoService.listaTransacao(url);

                Set<String> vouchersUnicos = new HashSet<>();
                List<transacao> transacoesFiltradas = new ArrayList<>();

                for (transacao t : tdsTransacoes) {
                    if (!vouchersUnicos.contains(t.getVoucher())) {
                        vouchersUnicos.add(t.getVoucher());
                        transacoesFiltradas.add(t);
                    }
                }
                relatorioService service = new relatorioService();
                Map<LocalDate, List<transacao>> transacoesPorDia = service.agruparTransacoesPorDia(transacoesFiltradas);

                for (Map.Entry<LocalDate, List<transacao>> entry : transacoesPorDia.entrySet()) {
                    LocalDate data = entry.getKey();
                    List<transacao> listaTransacoes = entry.getValue();
                    DateTimeFormatter formatterISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String dataFormatadaISO = data.format(formatterISO);

                    listaTransacoes.forEach(t -> {
                        String urlVoucher = "https://tcc-r46r.onrender.com/caixa/saldo/total/" + t.getVoucher() + "/" + dataFormatadaISO;
                        String token = propertiesController.getProperty("user.token");
                        String resultado = APICommunication.ReqGET(urlVoucher, token);
                        JSONObject json = new JSONObject(resultado);

                        JSONArray vouchers = json.getJSONArray("vouchers");
                        JSONObject voucherObject = vouchers.getJSONObject(0);
                        String voucher = voucherObject.getString("voucher");
                        String criadoEmString = voucherObject.getString("criadoEm");
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                        OffsetDateTime criadoEmDate = OffsetDateTime.parse(criadoEmString, dateTimeFormatter);
                        Timestamp criadoEm = Timestamp.from(criadoEmDate.toInstant());

                        double valorDepositos = json.getDouble("valorDepositos");
                        double valorEstorno = json.getDouble("valorEstorno");
                        double valorPagamento = json.getDouble("valorPagamento");

                        String valorDepositosString = json.getString("valorDepositosString");
                        String valorSaidaString = json.getString("valorSaidaString");
                        String valorPagamentoString = json.getString("valorPagamentoString");

                        t.setCriadoEmVoucher(criadoEm);
                        t.setVoucher(voucher);
                        t.setValorDepositos(valorDepositos);
                        t.setValorEstorno(valorEstorno);
                        t.setValorPagamento(valorPagamento);

                        t.setValorDepositosString(valorDepositosString);
                        t.setValorSaidaString(valorSaidaString);
                        t.setValorPagamentoString(valorPagamentoString);
                    });
                }
                GerarPDF gerarPDF = new GerarPDF();
                gerarPDF.gerarPDFCaixa(tipo, dataIniFormatada, dataFinalFormatada, transacoesPorDia, stage);
            }
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            btnDeposito.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");

    }


    private int sidepag = 0;
    private int sideCad = 0;
    private int side = 0;

    @FXML
    void btnOptionsClicked(MouseEvent event) throws InterruptedException {
        if(!paneSidebar.isVisible() && side == 0){
            abreSide();
        }
        else if(paneSidebar.isVisible() && side == 1){
            if(paneSideCadastro.isVisible()){
                fechaTodaSideCad();
            }
            else if(paneSidePagamento.isVisible()){
                fechaTodaSidePag();
            }
            else{
                fechaSide();
            }
        }
    }



    @FXML
    void btnSideCadEvenClicked(MouseEvent event) throws IOException {
        App.setRoot("cadastroEventos");
    }

    @FXML
    void btnSideRelatoriosClicked(MouseEvent event) throws IOException {
        App.setRoot("gerarRelatorio");
    }

    @FXML
    void btnSideCadLojasClicked(MouseEvent event) throws IOException {
        App.setRoot("cadastroLojas");
    }

    @FXML
    void btnSideCaixaClicked(MouseEvent event) throws IOException {
        App.setRoot("historicoCaixa");
    }

    @FXML
    void btnSideCancelClicked(MouseEvent event) throws IOException {
        App.setRoot("historicoCancel");
    }

    @FXML
    void btnSideEvenCadClicked(MouseEvent event) throws IOException {
        App.setRoot("eventosCadastrados");
    }

    @FXML
    void btnSideLogoutClicked(MouseEvent event) throws IOException {
        App.setRoot("telaLogin");
    }

    @FXML
    void btnSideLojasCadClicked(MouseEvent event) throws IOException {
        App.setRoot("lojascadastradas");
    }

    @FXML
    void btnSideNovaTranClicked(MouseEvent event) throws IOException {
        App.setRoot("novaTransacao");
    }

    @FXML
    void btnSidePagEntered(MouseEvent event) {
        if(!paneSidePagamento.isVisible() && sidepag == 0) {
            abreSidePag();
        }
    }

    @FXML
    void btnSideCadEntered(MouseEvent event) {
        if(!paneSideCadastro.isVisible() && sideCad == 0) {
            abreSideCad();
        }
    }

    @FXML
    void imgFecharClicked(MouseEvent event) throws IOException {
        propertiesController.logout();
        System.exit(0);
    }



    private void abreSidePag(){
        paneSidePagamento.setVisible(true);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(paneSidePagamento);
        transition.setToX(225);
        transition.setFromX(0);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        paneSideCadastro.setVisible(false);
        transition.setOnFinished(mudaSituacao -> {
            sidepag = 1;
            sideCad = 0;
        });
        transition.play();
    }

    private void fechaSidePag(){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(paneSidePagamento);
        transition.setToX(-100);
        transition.setFromX(225);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        transition.setOnFinished(mudaSituacao -> {
            paneSidePagamento.setVisible(false);
            sidepag = 0;
        });
        transition.play();
    }

    private void fechaTodaSidePag(){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(paneSidePagamento);
        transition.setToX(-100);
        transition.setFromX(225);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        transition.setOnFinished(mudaSituacao -> {
            paneSidePagamento.setVisible(false);
            fechaSide();
            sidepag = 0;
        });
        transition.play();
    }

    private void abreSideCad(){
        paneSideCadastro.setVisible(true);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(paneSideCadastro);
        transition.setToX(325);
        transition.setFromX(0);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        paneSidePagamento.setVisible(false);
        transition.setOnFinished(mudaSituacao -> {
            sidepag = 0;
            sideCad = 1;
        });
        transition.play();
    }

    private void fechaSideCad(){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(paneSideCadastro);
        transition.setToX(-100);
        transition.setFromX(325);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        transition.setOnFinished(mudaSituacao -> {
            paneSideCadastro.setVisible(false);
            sideCad = 0;
        });
        transition.play();
    }
    private void fechaTodaSideCad(){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(paneSideCadastro);
        transition.setToX(-100);
        transition.setFromX(325);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        transition.setOnFinished(mudaSituacao -> {
            paneSideCadastro.setVisible(false);
            fechaSide();
            sideCad = 0;
        });
        transition.play();
    }

    private void abreSide(){
        paneSideCancel.setVisible(true);
        paneSidebar.setVisible(true);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(paneSidebar);
        transition.setToX(0);
        transition.setFromX(-250);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        transition.setOnFinished(mudaSituacao -> {
            side = 1;
        });
        transition.play();
    }

    private void fechaSide(){
        paneSidebar.setVisible(true);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(paneSidebar);
        transition.setToX(-250);
        transition.setFromX(0);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        transition.setOnFinished(mudaSituacao -> {
            paneSidebar.setVisible(false);
            side = 0;
            sideCad = 0;
            sidepag = 0;
        });
        transition.play();
    }


    @FXML
    private Pane paneSideCancel;

    @FXML
    void paneSideCancelClicked(MouseEvent event) {
        if(paneSidebar.isVisible() && side == 1 && paneSideCancel.isVisible()){
            if(paneSideCadastro.isVisible()){
                fechaTodaSideCad();
                paneSideCancel.setVisible(false);
            }
            else if(paneSidePagamento.isVisible()){
                fechaTodaSidePag();
                paneSideCancel.setVisible(false);
            }
            else{
                fechaSide();
                paneSideCancel.setVisible(false);
            }
        }
    }



}
