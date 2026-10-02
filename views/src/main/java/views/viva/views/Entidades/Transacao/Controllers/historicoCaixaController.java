package views.viva.views.Entidades.Transacao.Controllers;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.SneakyThrows;
import views.viva.views.App;
import views.viva.views.Entidades.Transacao.Service.transacaoService;
import views.viva.views.Entidades.Transacao.Model.transacao;
import views.viva.views.Funcionalidades.Buscar.Buscar;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;

import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.saveProperties;
import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.setProperty;

public class historicoCaixaController implements Initializable {

    @FXML
    private Button btnDeposito;

    @FXML
    private Button btnEstorno;

    @FXML
    private TableColumn<transacao, String> collumnDia;

    @FXML
    private TableColumn<transacao, String> collumnHora;

    @FXML
    private TableColumn<String, Integer> collumnValor;
    @FXML
    private TableColumn<transacao, String> collumnVoucher;

    @FXML
    private TableView<transacao> tblDados;

    transacaoService transacaoService = new transacaoService();

    private ObservableList<transacao> dadosEstorno;
    private ObservableList<transacao> dadosDeposito;

    int verificador;

    @FXML
    private Pagination pagination;
    int countPage = 1;
    int page = 1;

    private void initPagination(String url) {
        int total = transacaoService.consultaQuantidadeTransacoes(url);
        pagination.setPageCount(total/10);
        pagination.setCurrentPageIndex(0);
    }

    @FXML
    void btnDepositoClicked(MouseEvent event) throws SQLException, ParseException {
        buscando = 0;
        txtBusca.setText("");
        if(countPage != 1){
            initPagination("https://tcc-r46r.onrender.com/transacao/selecao/portipo/C");
            countPage = 1;
        }
        btnEstorno.setStyle("-fx-background-color: black;");
        btnDeposito.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        dadosDeposito = atualizaDadosDeposito();
        tblDados.setItems(dadosDeposito);
        verificador = 2;
    }

    @FXML
    void btnEstornoClicked(MouseEvent event) throws SQLException, ParseException {
        buscando = 0;
        txtBusca.setText("");
        if(countPage != 2){
            initPagination("https://tcc-r46r.onrender.com/transacao/selecao/portipo/E");
            countPage = 2;
        }
        btnDeposito.setStyle("-fx-background-color: black;");
        btnEstorno.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        dadosEstorno = atualizaDadosEstorno();
        tblDados.setItems(dadosEstorno);
        verificador = 2;
    }


    public void initTableDados() throws SQLException, ParseException {
        collumnDia.setCellValueFactory(new PropertyValueFactory("dia"));
        collumnHora.setCellValueFactory(new PropertyValueFactory("hora"));
        collumnValor.setCellValueFactory(new PropertyValueFactory("valorString"));
        collumnVoucher.setCellValueFactory(new PropertyValueFactory("voucher"));
        dadosDeposito = atualizaDadosDeposito();
        tblDados.setItems(dadosDeposito);
        verificador = 2;

    }

    public ObservableList<transacao> atualizaDadosDeposito() throws SQLException, ParseException {
        String url = "https://tcc-r46r.onrender.com/transacao/selecao/porquantidade/portipoporquantidade/C/"+page;
        return FXCollections.observableArrayList(transacaoService.listaTransacao(url));
    }

    public ObservableList<transacao> atualizaDadosEstorno() throws SQLException, ParseException {
        String url = "https://tcc-r46r.onrender.com/transacao/selecao/porquantidade/portipoporquantidade/E/"+page;
        return FXCollections.observableArrayList(transacaoService.listaTransacao(url));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnDeposito.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        try {
            tblDados.setEditable(false);
            initPagination("https://tcc-r46r.onrender.com/transacao/selecao/portipo/C");
            initTableDados();
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                page = newValue.intValue() + 1;
                if(buscando == 0) {
                    try {
                        if (countPage == 1) {
                            dadosDeposito = atualizaDadosDeposito();
                            tblDados.setItems(dadosDeposito);
                        }
                        if (countPage == 2) {
                            dadosEstorno = atualizaDadosEstorno();
                            tblDados.setItems(dadosEstorno);
                        }
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao mudar de pagina", ButtonType.OK);
                        alert.showAndWait();
                    }
                } else {
                    try {
                        buscando = 1;
                        int tipo = 0;
                        String corEstorno = btnDeposito.getStyle();
                        if(corEstorno.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
                            tipo=1;
                        }
                        String corDeposito = btnEstorno.getStyle();
                        if(corDeposito.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
                            tipo=2;
                        }
                        tblDados.setItems(buscar.buscaTransacoesPorVoucher(txtBusca, page, tipo));
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao mudar de pagina", ButtonType.OK);
                        alert.showAndWait();
                    }
                }
            }
        });
    }


    @FXML
    private Pane paneSideCadastro;

    @FXML
    private Pane paneSidePagamento;

    @FXML
    private Pane paneSidebar;

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

    public void btnDepositoEntered(MouseEvent mouseEvent) {
        String cor = btnDeposito.getStyle();
        if(!cor.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
            btnDeposito.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
            verificador=1;
        }
    }

    public void btnDepositoExited(MouseEvent mouseEvent) {
        String cor = btnDeposito.getStyle();
        if(cor.contains("linear-gradient(to right, #ED9B50 , #d27300)") && verificador == 1){
            btnDeposito.setStyle("-fx-background-color: black");
            verificador=2;
        }
    }

    public void btnEstornoEntered(MouseEvent mouseEvent) {
        String cor = btnEstorno.getStyle();
        if(!cor.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
            btnEstorno.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
            verificador=1;
        }
    }

    public void btnEstornoExited(MouseEvent mouseEvent) {
        String cor = btnEstorno.getStyle();
        if(cor.contains("linear-gradient(to right, #ED9B50 , #d27300)") && verificador == 1){
            btnEstorno.setStyle("-fx-background-color: black");
            verificador=2;
        }
    }

    @FXML
    private TextField txtBusca;

    private Buscar buscar = new Buscar();

    private int buscando = 0;

    public void btnBuscarCliked(MouseEvent mouseEvent) throws SQLException, ParseException {
        try {
            buscando = 1;
            int tipo = 0;
            String corEstorno = btnDeposito.getStyle();
            if (corEstorno.contains("linear-gradient(to right, #ED9B50 , #d27300)")) {
                initPagination("https://tcc-r46r.onrender.com/transacao/selecao/porvoucherportipo/" + txtBusca.getText() + "/C");
                tipo = 1;
            }
            String corDeposito = btnEstorno.getStyle();
            if (corDeposito.contains("linear-gradient(to right, #ED9B50 , #d27300)")) {
                initPagination("https://tcc-r46r.onrender.com/transacao/selecao/porvoucherportipo/" + txtBusca.getText() + "/E");
                tipo = 2;
            }
            tblDados.setItems(buscar.buscaTransacoesPorVoucher(txtBusca, page, tipo));
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao buscar Transacao por voucher" + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void btnBuscarEntered(MouseEvent mouseEvent) {
    }

    public void btnBuscarExited(MouseEvent mouseEvent) {

    }
}
