package views.viva.views.Entidades.Vendedor.Controllers;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import views.viva.views.App;
import views.viva.views.Entidades.Transacao.Model.transacao;
import views.viva.views.Entidades.Vendedor.Service.vendedorService;
import views.viva.views.Entidades.Vendedor.Model.vendedor;
import views.viva.views.Entidades.Vendedor.Model.vendedorEvento;
import views.viva.views.Funcionalidades.Buscar.Buscar;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.saveProperties;
import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.setProperty;

public class lojasCadastradasController implements Initializable {

    @FXML
    private Button btnEmpresa;

    @FXML
    private Button btnMarca;

    @FXML
    private TableColumn<vendedor, Boolean> columnAtivo;

    @FXML
    private TableColumn<vendedor, ZonedDateTime> columnCriadEm;

    @FXML
    private TableColumn<vendedor, String> columnNome;

    @FXML
    private TableColumn<vendedor, Long> columnResponsavel;

    @FXML
    private TableView<vendedorEvento> tblDadosVendEvento;

    @FXML
    private TableView<vendedor> tblDados;

    @FXML
    private TableColumn<vendedorEvento, Boolean> columnAtivoVendEven;

    @FXML
    private TableColumn<vendedorEvento, Timestamp> columnCriadEmVendEven;

    @FXML
    private TableColumn<vendedorEvento, String> columnEvento;

    @FXML
    private TableColumn<vendedorEvento, String> columnLojaVendEven;

    @FXML
    private TableColumn<vendedorEvento, Long> columnResponsavelVendEven;

    @FXML
    private Pane paneVendedor;

    @FXML
    private Pane paneVendedorEvento;

    private vendedor selecionado;
    private vendedorEvento vendEvenselecionado;

    vendedorService vendedorService = new vendedorService();

    private ObservableList<vendedor> dadosVendedor;
    private ObservableList<vendedorEvento> dadosVendedorEvento;


    private int verificador;

    @FXML
    private Pagination pagination;
    int countPage = 1;
    int page = 1;

    private void initPagination(String url) {
        int total = 0;
        if(countPage == 1){
            total = vendedorService.consultaQuantidadeVendedores(url);
        }
        if(countPage == 2){
            total = vendedorService.consultaQuantidadeVendedoresEvento(url);
        }
        pagination.setPageCount(total/10);
        pagination.setCurrentPageIndex(0);
    }

    @FXML
    void btnEmpresaClicked(MouseEvent event) throws SQLException, ParseException {
        if(countPage != 2){
            countPage = 2;
            initPagination("https://tcc-r46r.onrender.com/vendedorevento/selecao/todos");
        }
        txtBusca.setText("");
        btnMarca.setStyle("-fx-background-color: black"); //cor nova
        btnEmpresa.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        paneVendedor.setVisible(false);
        paneVendedorEvento.setVisible(true);
        dadosVendedorEvento = atualizaDadosVendedorEvento();
        tblDadosVendEvento.setItems(dadosVendedorEvento);
        verificador=2;
    }

    @FXML
    void btnMarcaClicked(MouseEvent event) throws SQLException, ParseException {
        if(countPage != 1){
            countPage = 1;
            initPagination("https://tcc-r46r.onrender.com/vendedor/selecao/todos");
        }
        txtBusca.setText("");
        btnEmpresa.setStyle("-fx-background-color: black;");
        btnMarca.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        paneVendedorEvento.setVisible(false);
        paneVendedor.setVisible(true);
        dadosVendedor = atualizaDadosVendedor();
        tblDados.setItems(dadosVendedor);
        verificador=2;
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
    void btnMarcaExited(MouseEvent event) {
        String cor = btnMarca.getStyle();
        if(cor.contains("linear-gradient(to right, #ED9B50 , #d27300)") && verificador == 1){
            btnMarca.setStyle("-fx-background-color: black");
            verificador=2;
        }
    }

    @FXML
    void btnAlterarAtivoCliked(MouseEvent event) throws SQLException, ParseException {
        if(paneVendedor.isVisible() && !paneVendedorEvento.isVisible()){
            if(selecionado != null){
                vendedorService.alterarAtivoVendedor(selecionado.getIdVendedor());
                dadosVendedor = atualizaDadosVendedor();
                tblDados.setItems(dadosVendedor);
                dadosVendedorEvento = atualizaDadosVendedorEvento();
                tblDadosVendEvento.setItems(dadosVendedorEvento);
            }
        }
        else if(paneVendedorEvento.isVisible() && !paneVendedor.isVisible()){
            if(vendEvenselecionado != null){
            vendedorService.alterarAtivoVendedorEvento(vendEvenselecionado.getIdVendedorEvento());
                dadosVendedor = atualizaDadosVendedor();
                tblDados.setItems(dadosVendedor);
                dadosVendedorEvento = atualizaDadosVendedorEvento();
                tblDadosVendEvento.setItems(dadosVendedorEvento);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnMarca.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        try {
            initTableVendedorEvento();
            initTableVendedor();
            initPagination("https://tcc-r46r.onrender.com/vendedor/selecao/todos");
            paneVendedor.setVisible(true);
            paneVendedorEvento.setVisible(false);
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        tblDadosVendEvento.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<vendedorEvento>() {
            @Override
            public void changed(ObservableValue<? extends vendedorEvento> observable, vendedorEvento oldValue, vendedorEvento newValue) {
                vendEvenselecionado = newValue;
            }
        });
        tblDados.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<vendedor>() {
            @Override
            public void changed(ObservableValue<? extends vendedor> observable, vendedor oldValue, vendedor newValue) {
                selecionado = newValue;
            }
        });
        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                page = newValue.intValue() + 1;
                try {
                    if (countPage == 1) {
                        dadosVendedor = atualizaDadosVendedor();
                        tblDados.setItems(dadosVendedor);
                    }
                    if (countPage ==2) {
                        dadosVendedorEvento = atualizaDadosVendedorEvento();
                        tblDadosVendEvento.setItems(dadosVendedorEvento);
                    }
                } catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao mudar de pagina", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
    }

    public void initTableVendedor() throws SQLException, ParseException {
        try {
            columnNome.setCellValueFactory(new PropertyValueFactory("loja"));
            columnResponsavel.setCellValueFactory(new PropertyValueFactory("nomeResponsavel"));
            columnAtivo.setCellValueFactory(new PropertyValueFactory("ativo"));
            columnCriadEm.setCellValueFactory(new PropertyValueFactory("criadoEmString"));
            dadosVendedor = atualizaDadosVendedor();
            tblDados.setItems(dadosVendedor);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na inicialização da tabela de vendedor: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void initTableVendedorEvento() throws SQLException, ParseException {
        try {
            columnLojaVendEven.setCellValueFactory(new PropertyValueFactory("nomeVendedor"));
            columnEvento.setCellValueFactory(new PropertyValueFactory("nomeEvento"));
            columnResponsavelVendEven.setCellValueFactory(new PropertyValueFactory("nomeResponsavel"));
            columnCriadEmVendEven.setCellValueFactory(new PropertyValueFactory("criadoEmString"));
            columnAtivoVendEven.setCellValueFactory(new PropertyValueFactory("ativoString"));
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro na inicialização da tabela de vendedor evento: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public ObservableList<vendedorEvento> atualizaDadosVendedorEvento() throws SQLException, ParseException {
        return FXCollections.observableArrayList(vendedorService.listaVendedorEventos("https://tcc-r46r.onrender.com/vendedorevento/selecao/porQuantidade/", page));
    }

    public ObservableList<vendedor> atualizaDadosVendedor() throws SQLException, ParseException {
        return FXCollections.observableArrayList(vendedorService.listaVendedores("https://tcc-r46r.onrender.com/vendedor/selecao/porQuantidade/"+ page));
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
                initPagination("https://tcc-r46r.onrender.com/vendedor/selecao/pornome/"+txtBusca.getText());
                tblDados.setItems(buscar.buscaVendedoresPorNome(txtBusca, page));
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao buscar Vendedor por nome \n" + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }}
        String corVendEven = btnEmpresa.getStyle();
        if (corVendEven.contains("linear-gradient(to right, #ED9B50 , #d27300)")) {
            try {
                countPage = 2;
                initPagination("https://tcc-r46r.onrender.com/vendedorevento/selecao/porloja/"+txtBusca.getText());
                tblDadosVendEvento.setItems(buscar.buscaVendedoresEventosPorNome(txtBusca, page));
            } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao buscar Vendedor evento por loja \n" + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }}
}

    public void btnBuscarEntered(MouseEvent mouseEvent) {
    }

    public void btnBuscarExited(MouseEvent mouseEvent) {

    }
}



