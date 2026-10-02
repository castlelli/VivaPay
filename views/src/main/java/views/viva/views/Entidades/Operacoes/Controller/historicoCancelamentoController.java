package views.viva.views.Entidades.Operacoes.Controller;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import views.viva.views.App;
import views.viva.views.Entidades.Operacoes.Model.*;
import views.viva.views.Entidades.Operacoes.Service.*;
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

public class historicoCancelamentoController implements Initializable {

    @FXML
    private Button btnEmpresa;

    @FXML
    private Button btnEvento;

    @FXML
    private Button btnMarca;

    @FXML
    private TableColumn<operacaoEvento, String> columnDataEvento;

    @FXML
    private TableColumn<operacaoEvento, String> columnEvento;

    @FXML
    private TableColumn<operacaoEvento, Integer> columnResponsavelEvento;

    @FXML
    private TableColumn<operacaoEvento, String> columnTipoEvento;

    @FXML
    private TableColumn<operacaoVendedorEvento, Integer> columnResponsavelVendEvento;

    @FXML
    private TableColumn<operacaoVendedorEvento, String> columnTipoVendEvento;

    @FXML
    private TableColumn<operacaoVendedorEvento, String> columnVendEvento;

    @FXML
    private TableColumn<operacaoVendedorEvento, String> columnDataVendEvento;

    @FXML
    private TableColumn<operacaoVendedor, String> columnVendedor;

    @FXML
    private TableColumn<operacaoVendedor, String> columntipoVendedor;

    @FXML
    private TableColumn<operacaoVendedor, Integer> columnResponsavelVendedor;

    @FXML
    private TableColumn<operacaoVendedor, String> columnDataVendedor;

    @FXML
    private TableView<operacaoVendedorEvento> tblEmpresa;

    @FXML
    private TableView<operacaoEvento> tblEvento;

    @FXML
    private TableView<operacaoVendedor> tblMarca;

    operacaoEventoService operacaoEventoService = new operacaoEventoService();
    operacaoVendedorService operacaoVendedorService = new operacaoVendedorService();
    operacaoVendedorEventoService operacaoVendedorEventoService = new operacaoVendedorEventoService();

    private ObservableList<operacaoEvento> dadosOperacaoEvento;
    private ObservableList<operacaoVendedor> dadosoperacaoVendedor;
    private ObservableList<operacaoVendedorEvento> dadosoperacaoVendedorEvento;

    int verificador;


    @FXML
    private Pagination pagination;
    int countPage = 1;
    int page = 1;

    private void initPagination(String url) {
        int total = 0;
        if(countPage == 1){
            total = operacaoVendedorService.consultaQuantidadeOperacaoVendedor(url);
        }
        if(countPage == 2){
            total = operacaoEventoService.consultaQuantidadeOperacaoEvento(url);
        }
        if(countPage == 3){
            total = operacaoVendedorEventoService.consultaQuantidadeOperacaoVendEvento(url);
        }
        pagination.setPageCount(total/10);
        pagination.setCurrentPageIndex(0);
    }

    @FXML
    void btnEmpresaEntered(MouseEvent event) {
        String cor = btnEmpresa.getStyle();
        if(!cor.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
            btnEmpresa.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
            verificador=1;
        }
    }

    @FXML
    void btnEmpresaExited(MouseEvent event) {
        String cor = btnEmpresa.getStyle();
        if(cor.contains("linear-gradient(to right, #ED9B50 , #d27300)") && verificador == 1){
            btnEmpresa.setStyle("-fx-background-color: black");
            verificador=2;
        }
    }

    @FXML
    void btnMarcaEntered(MouseEvent event) {
        String cor = btnMarca.getStyle();
        if(!cor.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
            btnMarca.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
            verificador=1;
        }
    }
    @FXML
    void btnMarcaExited(MouseEvent event) {
        String cor = btnMarca.getStyle();
        if(cor.contains("linear-gradient(to right, #ED9B50 , #d27300)") && verificador == 1){
            btnMarca.setStyle("-fx-background-color: black");
            verificador=2;
        }
    }

    @FXML
    void btnEventoEntered(MouseEvent event) {
            String cor = btnEvento.getStyle();
        if(!cor.contains("linear-gradient(to right, #ED9B50 , #d27300)")){
            btnEvento.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
            verificador=1;
        }
    }
    @FXML
    void btnEventoExited(MouseEvent event) {
        String cor = btnEvento.getStyle();
        if(cor.contains("linear-gradient(to right, #ED9B50 , #d27300)") && verificador == 1){
            btnEvento.setStyle("-fx-background-color: black");
            verificador=2;
        }
    }

    @FXML
    void btnEmpresaClicked(MouseEvent event) throws SQLException, ParseException {
        if(countPage != 3){
            countPage = 3;
            initPagination("https://tcc-r46r.onrender.com/operacoes/selecao/tudo/vendedorevento");
        }
        txtBusca.setText("");
        btnMarca.setStyle("-fx-background-color: black;");
        btnEvento.setStyle("-fx-background-color: black;");
        btnEmpresa.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);"); //nova cor!!!
        verificador = 2;
        tblEmpresa.setVisible(true);
        tblEvento.setVisible(false);
        tblMarca.setVisible(false);
        tblEmpresa.setItems(dadosoperacaoVendedorEvento);
    }

    @FXML
    void btnEventoClicked(MouseEvent event) throws SQLException, ParseException {
        if(countPage != 2){
            countPage = 2;
            initPagination("https://tcc-r46r.onrender.com/operacoes/selecao/tudo/evento");
        }
        txtBusca.setText("");
        btnMarca.setStyle("-fx-background-color: black;");
        btnEmpresa.setStyle("-fx-background-color: black;");
        btnEvento.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        verificador = 2;
        tblEmpresa.setVisible(false);
        tblEvento.setVisible(true);
        tblMarca.setVisible(false);
        tblEvento.setItems(dadosOperacaoEvento);
    }

    @FXML
    void btnMarcaClicked(MouseEvent event) throws SQLException, ParseException {
        if(countPage != 1){
            countPage = 1;
            initPagination("https://tcc-r46r.onrender.com/operacoes/selecao/tudo/vendedor");
        }
        txtBusca.setText("");
        btnEmpresa.setStyle("-fx-background-color: black;");
        btnEvento.setStyle("-fx-background-color: black;");
        btnMarca.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);"); //nova cor!!!
        verificador = 2;
        tblEmpresa.setVisible(false);
        tblEvento.setVisible(false);
        tblMarca.setVisible(true);
        tblMarca.setItems(dadosoperacaoVendedor);

    }

    public void initTableEvento() throws SQLException, ParseException {
        columnEvento.setCellValueFactory(new PropertyValueFactory("nomeEvento"));
        columnTipoEvento.setCellValueFactory(new PropertyValueFactory("tipo"));
        columnDataEvento.setCellValueFactory(new PropertyValueFactory("dataHora"));
        columnResponsavelEvento.setCellValueFactory(new PropertyValueFactory("nomeResponsavel"));
        dadosOperacaoEvento = atualizaDadosEvento();
        tblEvento.setItems(dadosOperacaoEvento);
    }
    public void initTableVendedor() throws SQLException, ParseException {
        columnVendedor.setCellValueFactory(new PropertyValueFactory("nomeVendedor"));
        columntipoVendedor.setCellValueFactory(new PropertyValueFactory("tipo"));
        columnDataVendedor.setCellValueFactory(new PropertyValueFactory("dataHora"));
        columnResponsavelVendedor.setCellValueFactory(new PropertyValueFactory("nomeResponsavel"));
        dadosoperacaoVendedor = atualizaDadosVendedor();
        tblMarca.setItems(dadosoperacaoVendedor);
    }
    public void initTableVendedorEvento() throws SQLException, ParseException {
        columnVendEvento.setCellValueFactory(new PropertyValueFactory("nomeVendedor"));
        columnTipoVendEvento.setCellValueFactory(new PropertyValueFactory("tipo"));
        columnDataVendEvento.setCellValueFactory(new PropertyValueFactory("dataHora"));
        columnResponsavelVendEvento.setCellValueFactory(new PropertyValueFactory("nomeResponsavel"));
        dadosoperacaoVendedorEvento = atualizaDadosVendedorEvento();
        tblEmpresa.setItems(dadosoperacaoVendedorEvento);
    }

    public ObservableList<operacaoEvento> atualizaDadosEvento() throws SQLException, ParseException {
        return FXCollections.observableArrayList(operacaoEventoService.listaOperacaoEvento("https://tcc-r46r.onrender.com/operacoes/selecao/evento/porQuantidade/", page));
    }
    public ObservableList<operacaoVendedor> atualizaDadosVendedor() throws SQLException, ParseException {
        return FXCollections.observableArrayList(operacaoVendedorService.listaOperacaoVendedor("https://tcc-r46r.onrender.com/operacoes/selecao/vendedor/porquantidade/", page));
    }
    public ObservableList<operacaoVendedorEvento> atualizaDadosVendedorEvento() throws SQLException, ParseException {
        return FXCollections.observableArrayList(operacaoVendedorEventoService.listaOperacaoVendEvento("https://tcc-r46r.onrender.com/operacoes/selecao/vendedorevento/porquantidade/", page));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnMarca.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        try {
            initTableEvento();
            initTableVendedorEvento();
            initTableVendedor();
            initPagination("https://tcc-r46r.onrender.com/operacoes/selecao/tudo/vendedor");
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                page = newValue.intValue() + 1;
                try {
                    if (countPage == 1) {
                        dadosoperacaoVendedor = atualizaDadosVendedor();
                        tblMarca.setItems(dadosoperacaoVendedor);
                    }
                    if (countPage == 2) {
                        dadosOperacaoEvento = atualizaDadosEvento();
                        tblEvento.setItems(dadosOperacaoEvento);
                    }
                    if (countPage == 3) {
                        dadosoperacaoVendedorEvento = atualizaDadosVendedorEvento();
                        tblEmpresa.setItems(dadosoperacaoVendedorEvento);
                    }
                } catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao mudar de pagina", ButtonType.OK);
                    alert.showAndWait();
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


    @FXML
    private TextField txtBusca;

    private Buscar buscar = new Buscar();

    public void btnBuscarCliked(MouseEvent mouseEvent) throws SQLException, ParseException {
        String corVendedor = btnMarca.getStyle();
        if (corVendedor.contains("linear-gradient(to right, #ED9B50 , #d27300)")) {
            try {
                countPage = 1;
                initPagination("https://tcc-r46r.onrender.com/operacoes/selecao/pornome/vendedor/"+txtBusca.getText());
                tblMarca.setItems(buscar.buscaOperacoesVendedoresPorNome(txtBusca, page));
            } catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao buscar operação de Vendedor por nome \n" + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }}
        String corVendEven = btnEmpresa.getStyle();
        if (corVendEven.contains("linear-gradient(to right, #ED9B50 , #d27300)")) {
            try {
                countPage = 2;
                initPagination("https://tcc-r46r.onrender.com/operacoes/selecao/pornome/vendedorevento/"+txtBusca.getText());
                tblEmpresa.setItems(buscar.buscaOperacoesVendedoresEventosPorNome(txtBusca, page));
            } catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao buscar operação de Vendedor evento por loja \n" + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
        String corEvento = btnEvento.getStyle();
        if (corEvento.contains("linear-gradient(to right, #ED9B50 , #d27300)")) {
            try {
                countPage = 3;
                initPagination("https://tcc-r46r.onrender.com/operacoes/selecao/pornome/evento/"+txtBusca.getText());
                tblEvento.setItems(buscar.buscaOperacoesEventosPorNome(txtBusca, page));
            } catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao buscar operação de evento por nome \n" + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    public void btnBuscarEntered(MouseEvent mouseEvent) {
    }

    public void btnBuscarExited(MouseEvent mouseEvent) {

    }
}
