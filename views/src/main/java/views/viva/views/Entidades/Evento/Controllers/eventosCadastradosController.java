package views.viva.views.Entidades.Evento.Controllers;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import views.viva.views.App;
import views.viva.views.Entidades.Evento.Model.eventos;
import views.viva.views.Entidades.Evento.Service.eventoService;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;
import views.viva.views.Funcionalidades.Buscar.Buscar;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;

import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.saveProperties;
import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.setProperty;

public class eventosCadastradosController implements Initializable {

    @FXML
    private TableColumn<?, ?> columnAtivo;

    @FXML
    private TableColumn<?, ?> columnCriadoEm;

    @FXML
    private TableColumn<?, ?> columnHrFim;

    @FXML
    private TableColumn<?, ?> columnHrInicio;

    @FXML
    private TableColumn<?, ?> columnNome;

    @FXML
    private TableColumn<?, ?> columnResp;

    @FXML
    private TableView<eventos> tblDados;

    private eventos selecionado;


    @FXML
    private Pagination pagination;
    int page = 1;

    private void initPagination(String url) {
        int total = eventoService.consultaQuantidadeEvento(url);
        pagination.setPageCount(total/10);
        pagination.setCurrentPageIndex(0);
    }

    @FXML
    void btnAlterarAtivoClicked(MouseEvent event) throws SQLException, ParseException {
        if(selecionado != null){
            eventoService eventoService = new eventoService();
            eventoService.alterarAtivoEventos(selecionado.getIdEvento());
            tblDados.setItems(atualizaDados());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initTable();
            initPagination("https://tcc-r46r.onrender.com/evento/selecao/todos");

        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }

        tblDados.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<eventos>() {
            @Override
            public void changed(ObservableValue<? extends eventos> observable, eventos oldValue, eventos newValue) {
                selecionado = (eventos) newValue;
            }
        });
        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                page = newValue.intValue() + 1;
                try {
                    tblDados.setItems(atualizaDados());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void initTable() throws SQLException, ParseException {
        columnAtivo.setCellValueFactory(new PropertyValueFactory("ativo"));
        columnCriadoEm.setCellValueFactory(new PropertyValueFactory("dataHoraCriaEm"));
        columnNome.setCellValueFactory(new PropertyValueFactory("nomeEvento"));
        columnResp.setCellValueFactory(new PropertyValueFactory("nomeResponsavel"));
        columnHrFim.setCellValueFactory(new PropertyValueFactory("dataHoraFinal"));
        columnHrInicio.setCellValueFactory(new PropertyValueFactory("dataHoraInicio"));

        tblDados.setItems(atualizaDados());
    }

    public ObservableList<eventos> atualizaDados() throws SQLException, ParseException {
        eventoService eventoService = new eventoService();
        return FXCollections.observableArrayList(eventoService.listaEventos("https://tcc-r46r.onrender.com/evento/selecao/porquantidade/"+page));
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
        initPagination("https://tcc-r46r.onrender.com/evento/selecao/pornome/"+txtBusca.getText());
        tblDados.setItems(buscar.buscaEventosPorNome(txtBusca,page));
    }

    public void btnBuscarEntered(MouseEvent mouseEvent) {
    }

    public void btnBuscarExited(MouseEvent mouseEvent) {

    }
}
