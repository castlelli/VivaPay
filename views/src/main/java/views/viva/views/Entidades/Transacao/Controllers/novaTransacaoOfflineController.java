package views.viva.views.Entidades.Transacao.Controllers;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import views.viva.views.App;
import views.viva.views.Entidades.Evento.Model.eventos;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.layout.Pane;
import javafx.util.Duration;

import views.viva.views.Entidades.Evento.Model.eventos;
import views.viva.views.Entidades.Evento.Service.eventoService;
import views.viva.views.Entidades.Transacao.Service.transacaoService;
import views.viva.views.Funcionalidades.Impressora.Impressao;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.*;

public class novaTransacaoOfflineController implements Initializable {

    @FXML
    private ComboBox<String> cbmMetodo;

    @FXML
    private Pane paneSideCancel;

    @FXML
    private Pane paneSidebar;

    @FXML
    private TextField txtValorDeposito;

    @FXML
    void btnEfetivarClicked(MouseEvent event) {
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String data = dataHoraAtual.format(formatador);
        String valor = txtValorDeposito.getText();
        String metodo = (String) cbmMetodo.getValue();
        Impressao.ImprimirOff(valor,metodo,data );
        txtValorDeposito.setText("");
    }


    @FXML
    void imgFecharClicked(MouseEvent event) {
        propertiesController.logout();
        System.exit(0);
    }


    @FXML
    void paneSideCancelClicked(MouseEvent event) {
        if(paneSidebar.isVisible() && side == 1 && paneSideCancel.isVisible()){
                fechaSide();
                paneSideCancel.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbmMetodo.getItems().add("Pix");
        cbmMetodo.getItems().add("Dinheiro");
        cbmMetodo.getItems().add("Cartão");
        cbmMetodo.getSelectionModel().selectFirst();

    }

    private int side = 0;

    @FXML
    void btnOptionsClicked(MouseEvent event) throws InterruptedException {
        if(!paneSidebar.isVisible() && side == 0){
            abreSide();
        }
        else if(paneSidebar.isVisible() && side == 1){
                fechaSide();
        }
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
        });
        transition.play();
    }

    public void btnSideLoginClicked(MouseEvent mouseEvent) throws IOException {
        App.setRoot("telaLogin");
    }
}
