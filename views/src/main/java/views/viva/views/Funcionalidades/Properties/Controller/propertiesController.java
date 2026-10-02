package views.viva.views.Funcionalidades.Properties.Controller;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class propertiesController {

    private static Properties properties = new Properties();
    private static String propertiesFilePath = "src/main/resources/config.properties";


    public  static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public  static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public static void saveProperties() {
        try (OutputStream output = new FileOutputStream(propertiesFilePath)) {
            properties.store(output, "Atualizado por PropertiesExample");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logout(){
        propertiesController.setProperty("user.token", "");
        propertiesController.setProperty("user.nome", "");
        propertiesController.setProperty("user.id", "");
        propertiesController.saveProperties();
    }
}
