package views.viva.views.Entidades.Vendedor.Controllers;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import views.viva.views.App;
import views.viva.views.Entidades.Evento.Model.eventos;
import views.viva.views.Entidades.Vendedor.Service.vendedorService;
import views.viva.views.Entidades.Usuario.Service.usuarioService;
import views.viva.views.Entidades.Evento.Service.eventoService;
import views.viva.views.Entidades.Vendedor.Model.vendedor;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static views.viva.views.Entidades.Vendedor.Service.vendedorService.isValidEmail;
import static views.viva.views.Entidades.Vendedor.Service.vendedorService.isValidEmail;
import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.*;

public class cadastroLojasController implements Initializable {

    @FXML
    private Button btnEmpresa;

    @FXML
    private Button btnMarca;

    @FXML
    private ComboBox<eventos> cblEvento;

    @FXML
    private ComboBox<vendedor> cblMarca;

    @FXML
    private Pane paneEmpresa;

    @FXML
    private Pane paneMarca;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtSenha;

    @FXML
    private Label  lblUsuario;

    vendedorService vendedorService = new vendedorService();
    usuarioService usuarioService = new usuarioService();
    eventoService eventoService = new eventoService();

    int verificador;

    private void limparCampos(){
        txtEmail.clear();
        txtNome.clear();
        txtSenha.clear();
        cblEvento.setValue(null);
        cblMarca.setValue(null);
    }


    @FXML
    void btnEmpresaClicked(MouseEvent event)  {
        btnMarca.setStyle("-fx-background-color: black"); //cor nova
        btnEmpresa.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        paneMarca.setVisible(false);
        paneEmpresa.setVisible(true);
        verificador=2;

    }

    @FXML
    void btnMarcaClicked(MouseEvent event) {
        btnEmpresa.setStyle("-fx-background-color: black;");
        btnMarca.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");
        paneEmpresa.setVisible(false);
        paneMarca.setVisible(true);
        verificador=2;
    }

    @FXML
    void btnCadastrarClicked(MouseEvent event) throws SQLException, ParseException {
        if(!paneEmpresa.isVisible() && paneMarca.isVisible()){
            boolean verificador = false;
            if (!isValidEmail(txtEmail)) {
                vendedorService.mostrarAlerta("E-mail Inválido", "Insira um e-mail válido para prosseguir!");
                limparCampos();
                return;
            }
            else{
                verificador = usuarioService.cadastrarUsuario(txtNome,txtSenha,txtEmail);
            }
            if(verificador) {
                vendedorService.cadastrarVendedor(txtNome,txtEmail, txtEmail);
                List<vendedor> vendedores = vendedorService.listaVendedores("https://tcc-r46r.onrender.com/vendedor/selecao/todos");
                cblMarca.getItems().addAll(vendedores);
                limparCampos();
            }
        }
        else{
            vendedorService.cadastrarVendedorEvento(cblMarca,cblEvento);
            limparCampos();
        }
    }

    List<vendedor> vendedores;
    List<eventos> eventos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUsuario.setTextAlignment(TextAlignment.CENTER);
        lblUsuario.setText("Olá, " + getProperty("user.nome"));
        try {
            vendedores = vendedorService.listaVendedores("https://tcc-r46r.onrender.com/vendedor/selecao/todos");
            eventos = eventoService.listaEventos("https://tcc-r46r.onrender.com/evento/selecao/todos");

            cblMarca.setItems(FXCollections.observableArrayList(vendedores));
            cblEvento.setItems(FXCollections.observableArrayList(eventos));

            btnMarca.setStyle("-fx-background-color: linear-gradient(to right, #ED9B50 , #d27300);");

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar dados: " + e.getMessage());
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
}
