package views.viva.views.Funcionalidades.Login.Controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import views.viva.views.App;
import views.viva.views.Funcionalidades.Login.Service.loginService;
import views.viva.views.Funcionalidades.Login.Service.loginService.*;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

import java.io.IOException;
import java.sql.SQLException;


public class telaLoginController {

    @FXML
    private Button btnEntrar;

    @FXML
    private ImageView imgLogo;

    @FXML
    private ImageView imgUsuario;

    @FXML
    private Label lblCabecalho;

    @FXML
    private Label lblNome;

    @FXML
    private Label lblSenha;

    @FXML
    private Label lblUsuario;

    @FXML
    private Pane paneCabecalho;

    @FXML
    private Pane panePrincipal;

    @FXML
    private TextField txtNome;

    @FXML
    private PasswordField txtSenha;

    @FXML
    void btnEntrarClicked(MouseEvent event) throws SQLException, IOException {
        loginService login = new loginService();
        boolean resultado = login.logar(txtNome, txtSenha);
        if (resultado) {
            App.setRoot("escolhaDeLogin");
        }else {
            txtNome.setText("");
            txtSenha.setText("");
            txtNome.setPromptText("Nome incorreto");
            txtSenha.setPromptText("Senha incorreta");
            txtSenha.setStyle("-fx-prompt-text-fill: #ff5a5c;");
            txtNome.setStyle("-fx-prompt-text-fill: #ff5a5c;");

        }
    }
    @FXML
    void btnOfflineClicked(MouseEvent event) throws SQLException, IOException{
        App.setRoot("novaTransacaoOffline");

    }


    @FXML
    void imgFecharClicked(MouseEvent event) {
        System.exit(0);
    }


}
