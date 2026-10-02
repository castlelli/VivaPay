package views.viva.views.Entidades.Usuario.Service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import views.viva.views.Funcionalidades.API.APICommunication;
import views.viva.views.Funcionalidades.Properties.Controller.propertiesController;

public class usuarioService {
    public boolean cadastrarUsuario(TextField txtNome, TextField txtSenha, TextField txtLogin){
        try {
            String token = propertiesController.getProperty("user.token");
            String body = "{\"nome\": \"" + txtNome.getText() + "\", \"senha\": \"" + txtSenha.getText() + "\", \"login\": \"" + txtLogin.getText() + "\"}";
            String resultado = APICommunication.ReqPUT("https://tcc-r46r.onrender.com/cadastro/usuario", token, body);
            JSONObject jsonResponse = new JSONObject(resultado);
            return jsonResponse.getBoolean("sucesso");
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro no cadastro de usuario: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return false;
        }
    }
}
