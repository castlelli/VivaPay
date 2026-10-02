package views.viva.views.Entidades.Transacao.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import views.viva.views.App;
import views.viva.views.Entidades.Evento.Model.eventos;
import views.viva.views.Entidades.Evento.Service.eventoService;
import views.viva.views.Entidades.Transacao.Service.transacaoService;
import views.viva.views.Funcionalidades.Impressora.Impressao;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.*;

public class novaTransacaoController implements Initializable {

    @FXML
    private Button btnDeposito;

    @FXML
    private Button btnEstorno;

    @FXML
    private Button btnConsultaSaldo;

    @FXML
    private Pane paneDeposito;

    @FXML
    private Pane paneEstorno;

    @FXML
    private Pane paneSideCadastro;

    @FXML
    private Pane paneSidePagamento;

    @FXML
    private Pane paneSidebar;

    @FXML
    private Pane paneSaldo;

    @FXML
    private TextField txtValorDeposito;

    @FXML
    private TextField txtValorSaldo = new TextField();

    @FXML
    private TextField txtVoucherSaldo;

    @FXML
    private TextField txtVoucherDeposito;

    @FXML
    private TextField txtVoucherEstorno;

    @FXML
    private ComboBox<String> cbmMetodo;

    @FXML
    private ComboBox<eventos> cbmEvento;

    @FXML
    private Label  lblUsuario;

    @FXML
    private Label  lblMetodo;

    @FXML
    private Label  lblEvento;


    transacaoService transacaoService = new transacaoService();
    eventoService eventoService = new eventoService();
    Impressao novaImpressao= new Impressao();
    int verificador;

    @FXML
    void btnDepositoClicked(MouseEvent event) {
        btnEstorno.setStyle("-fx-background-color: black;");
        btnConsultaSaldo.setStyle("-fx-background-color: black;");
        btnDeposito.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        lblEvento.setVisible(true);
        lblMetodo.setVisible(true);
        cbmEvento.setVisible(true);
        cbmMetodo.setVisible(true);
        paneEstorno.setVisible(false);
        paneSaldo.setVisible(false);
        paneDeposito.setVisible(true);
        verificador=2;
    }

    @FXML
    void btnEstornoClicked(MouseEvent event) {
        btnDeposito.setStyle("-fx-background-color: black;");
        btnConsultaSaldo.setStyle("-fx-background-color: black;");
        btnEstorno.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        lblEvento.setVisible(true);
        lblMetodo.setVisible(true);
        cbmEvento.setVisible(true);
        cbmMetodo.setVisible(true);
        paneDeposito.setVisible(false);
        paneSaldo.setVisible(false);
        paneEstorno.setVisible(true);
        verificador=2;
    }
    public void btnConsultaSaldoClicked(MouseEvent mouseEvent) {
        btnDeposito.setStyle("-fx-background-color: black;");
        btnEstorno.setStyle("-fx-background-color: black;");
        btnConsultaSaldo.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        lblEvento.setVisible(false);
        lblMetodo.setVisible(false);
        cbmEvento.setVisible(false);
        cbmMetodo.setVisible(false);
        paneDeposito.setVisible(false);
        paneSaldo.setVisible(true);
        paneEstorno.setVisible(false);
        verificador=2;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUsuario.setText("Olá, " + getProperty("user.nome"));
        paneDeposito.setVisible(true);
        paneEstorno.setVisible(false);
        txtValorSaldo.setDisable(true);
        txtValorSaldo.setText("R$ 0,00");
        btnDeposito.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");

        addMoneyMask(txtValorDeposito);

        int limiteCaracteres = 5;
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            if (change.getControlNewText().length() <= limiteCaracteres) {
                return change;
            }
            return null;
        };

        TextFormatter<String> txtFormSaldo = new TextFormatter<>(filtro);
        TextFormatter<String> txtFormDep = new TextFormatter<>(filtro);
        TextFormatter<String> txtFormEst = new TextFormatter<>(filtro);

        txtVoucherSaldo.setTextFormatter(txtFormSaldo);
        txtVoucherEstorno.setTextFormatter(txtFormEst);
        txtVoucherDeposito.setTextFormatter(txtFormDep);

        try {
            cbmMetodo.getItems().addAll("Pix", "Dinheiro", "Cartão" );
            cbmMetodo.getSelectionModel().selectFirst();

            List<eventos> eventos = eventoService.listaEventos("https://tcc-r46r.onrender.com/evento/selecao/porativo/true");
            cbmEvento.setItems(FXCollections.observableArrayList(eventos));


        } catch (SQLException | ParseException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na inicialização dos componentes: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void addMoneyMask(TextField textField) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        textField.textProperty().addListener(new ChangeListener<String>() {
            private boolean changing = false;

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (changing) {
                    return;
                }

                changing = true;

                String digits = newValue.replaceAll("[^\\d]", "");
                if (digits.isEmpty()) {
                    textField.setText("");
                    changing = false;
                    return;
                }
                double parsedValue = Double.parseDouble(digits) / 100;
                String formattedValue = currencyFormat.format(parsedValue);
                textField.setText(formattedValue);
                textField.positionCaret(formattedValue.length());

                changing = false;
            }
        });
        textField.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, keyEvent -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        });
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
    public void btnConsultaSaldoEntered(MouseEvent mouseEvent) {
        String cor = btnConsultaSaldo.getStyle();
        if(!cor.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
            btnConsultaSaldo.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
            verificador=1;
        }
    }

    public void btnConsultaSaldoExited(MouseEvent mouseEvent) {
        String cor = btnConsultaSaldo.getStyle();
        if(cor.contains("linear-gradient(to right, #ED9B50 , #d27300)") && verificador == 1){
            btnConsultaSaldo.setStyle("-fx-background-color: black");
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

    @FXML
    void btnEfetivarClicked(MouseEvent event) {
        if(!paneDeposito.isVisible() && paneEstorno.isVisible() && !paneSaldo.isVisible()){
            transacaoService.estorno(txtVoucherEstorno,txtVoucherEstorno,cbmEvento,cbmMetodo);
            limparCampos();
        }
        if(paneDeposito.isVisible() && !paneEstorno.isVisible() && !paneSaldo.isVisible()){
            if(!cbmEvento.getSelectionModel().isEmpty()){
                    transacaoService.deposito(txtVoucherDeposito,txtValorDeposito,cbmEvento,cbmMetodo);
                    limparCampos();
            } else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Por favor, selecione um evento", ButtonType.OK);
                alert.showAndWait();
            }
        }
        if(paneSaldo.isVisible() && !paneEstorno.isVisible() && !paneDeposito.isVisible()){
            transacaoService.consultaSaldo(txtVoucherSaldo, txtValorSaldo, cbmEvento);
        }
    }

    private void limparCampos() {
        txtValorDeposito.setText("");
        txtVoucherDeposito.setText("");
        txtVoucherEstorno.setText("");
        txtVoucherSaldo.setText("");
        txtValorSaldo.setText("");
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
        propertiesController.logout();
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
    void imgFecharClicked(MouseEvent event) {
        propertiesController.logout();
        System.exit(0);
    }



    private void abreSidePag(){
        paneSidePagamento.setVisible(true);
        paneSidePagamento.setDisable(true);
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
            paneSidePagamento.setDisable(false);
            sidepag = 1;
            sideCad = 0;
        });
        transition.play();
    }

    private void fechaSidePag(){
        TranslateTransition transition = new TranslateTransition();
        paneSidePagamento.setDisable(true);
        transition.setNode(paneSidePagamento);
        transition.setToX(-100);
        transition.setFromX(225);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        transition.setOnFinished(mudaSituacao -> {
            paneSidePagamento.setDisable(false);
            paneSidePagamento.setVisible(false);
            sidepag = 0;
        });
        transition.play();
    }

    private void fechaTodaSidePag(){
        TranslateTransition transition = new TranslateTransition();
        paneSidePagamento.setDisable(true);
        transition.setNode(paneSidePagamento);
        transition.setToX(-100);
        transition.setFromX(225);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        transition.setOnFinished(mudaSituacao -> {
            paneSidePagamento.setVisible(false);
            paneSidePagamento.setDisable(false);
            fechaSide();
            sidepag = 0;
        });
        transition.play();
    }

    private void abreSideCad(){
        paneSideCadastro.setVisible(true);
        paneSideCadastro.setDisable(true);
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
            paneSideCadastro.setDisable(false);
            sidepag = 0;
            sideCad = 1;
        });
        transition.play();
    }

    private void fechaSideCad(){
        TranslateTransition transition = new TranslateTransition();
        paneSideCadastro.setDisable(true);
        transition.setNode(paneSideCadastro);
        transition.setToX(-100);
        transition.setFromX(325);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        transition.setOnFinished(mudaSituacao -> {
            paneSideCadastro.setVisible(false);
            paneSideCadastro.setDisable(false);
            sideCad = 0;
        });
        transition.play();
    }
    private void fechaTodaSideCad(){
        TranslateTransition transition = new TranslateTransition();
        paneSideCadastro.setDisable(true);
        transition.setNode(paneSideCadastro);
        transition.setToX(-100);
        transition.setFromX(325);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        transition.setOnFinished(mudaSituacao -> {
            paneSideCadastro.setVisible(false);
            paneSideCadastro.setDisable(false);
            fechaSide();
            sideCad = 0;
        });
        transition.play();
    }

    private void abreSide(){
        paneSideCancel.setVisible(true);
        paneSideCancel.setDisable(true);
        paneSidebar.setVisible(true);
        paneSidebar.setDisable(true);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(paneSidebar);
        transition.setToX(0);
        transition.setFromX(-250);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setRate(2);
        transition.setDuration(Duration.seconds(1));
        transition.setOnFinished(mudaSituacao -> {
            paneSidebar.setDisable(false);
            paneSideCancel.setDisable(false);
            side = 1;
        });
        transition.play();
    }

    private void fechaSide(){
        paneSidebar.setVisible(true);
        paneSidebar.setDisable(true);
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
            paneSidebar.setDisable(false);
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


    public void txtValorDepositoKReleased(KeyEvent keyEvent) {

    }

    public void txtValorSaldoReleased(KeyEvent keyEvent) {

    }
}
