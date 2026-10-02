package views.viva.views.Funcionalidades.Login.Service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import views.viva.views.Funcionalidades.API.APICommunication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static views.viva.views.Funcionalidades.Properties.Controller.propertiesController.*;

public class loginService {


    public boolean logar(TextField txtNome, TextField txtSenha) throws SQLException {
        try {
            String json = APICommunication.getBearerToken(txtNome.getText(), txtSenha.getText());
            JSONObject jsonObj = new JSONObject(json);

            if (jsonObj.getBoolean("success")) {
                JSONObject nomeJSON = jsonObj.getJSONObject("obj")
                        .getJSONObject("usuario");
                JSONObject idJSON = jsonObj.getJSONObject("obj")
                        .getJSONObject("usuario");
                JSONObject tokenJSON = jsonObj.getJSONObject("obj")
                        .getJSONObject("usuario")
                        .getJSONObject("token");
                String token = tokenJSON.getString("token");
                String nome = nomeJSON.getString("nomeUsuario");
                int id = idJSON.getInt("idUsuario");
                setProperty("user.nome", nome);
                setProperty("user.token", token);
                setProperty("user.id", Integer.toString(id));
                saveProperties();
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Erro ao logar: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return false;
        }
    }

}
