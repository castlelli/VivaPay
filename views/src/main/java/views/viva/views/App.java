package views.viva.views;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private double xOffset = 0;
    private double yOffset = 0;

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("telaLogin"), 1366, 768);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        //stage.initStyle(StageStyle.UNDECORATED);

        //enableWindowMovement(scene, stage);
        //enableWindowResize(stage, scene);

        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    private void enableWindowResize(Stage stage, Scene scene) {
        final double border = 10; // Tamanho da área de redimensionamento

        scene.setOnMouseMoved(event -> {
            if (event.getX() < border && event.getY() < border) {
                scene.setCursor(javafx.scene.Cursor.NW_RESIZE);
            } else if (event.getX() < border && event.getY() > scene.getHeight() - border) {
                scene.setCursor(javafx.scene.Cursor.SW_RESIZE);
            } else if (event.getX() > scene.getWidth() - border && event.getY() < border) {
                scene.setCursor(javafx.scene.Cursor.NE_RESIZE);
            } else if (event.getX() > scene.getWidth() - border && event.getY() > scene.getHeight() - border) {
                scene.setCursor(javafx.scene.Cursor.SE_RESIZE);
            } else if (event.getX() < border) {
                scene.setCursor(javafx.scene.Cursor.W_RESIZE);
            } else if (event.getX() > scene.getWidth() - border) {
                scene.setCursor(javafx.scene.Cursor.E_RESIZE);
            } else if (event.getY() < border) {
                scene.setCursor(javafx.scene.Cursor.N_RESIZE);
            } else if (event.getY() > scene.getHeight() - border) {
                scene.setCursor(javafx.scene.Cursor.S_RESIZE);
            } else {
                scene.setCursor(javafx.scene.Cursor.DEFAULT);
            }
        });

        scene.setOnMouseDragged(event -> {
            if (scene.getCursor() == javafx.scene.Cursor.W_RESIZE) {
                double newWidth = stage.getWidth() - (event.getScreenX() - stage.getX());
                stage.setWidth(newWidth > stage.getMinWidth() ? newWidth : stage.getMinWidth());
                stage.setX(event.getScreenX());
            } else if (scene.getCursor() == javafx.scene.Cursor.E_RESIZE) {
                stage.setWidth(event.getX());
            } else if (scene.getCursor() == javafx.scene.Cursor.N_RESIZE) {
                double newHeight = stage.getHeight() - (event.getScreenY() - stage.getY());
                stage.setHeight(newHeight > stage.getMinHeight() ? newHeight : stage.getMinHeight());
                stage.setY(event.getScreenY());
            } else if (scene.getCursor() == javafx.scene.Cursor.S_RESIZE) {
                stage.setHeight(event.getY());
            } else if (scene.getCursor() == javafx.scene.Cursor.NW_RESIZE) {
                double newWidth = stage.getWidth() - (event.getScreenX() - stage.getX());
                double newHeight = stage.getHeight() - (event.getScreenY() - stage.getY());
                stage.setWidth(newWidth > stage.getMinWidth() ? newWidth : stage.getMinWidth());
                stage.setHeight(newHeight > stage.getMinHeight() ? newHeight : stage.getMinHeight());
                stage.setX(event.getScreenX());
                stage.setY(event.getScreenY());
            } else if (scene.getCursor() == javafx.scene.Cursor.NE_RESIZE) {
                stage.setHeight(event.getY());
                stage.setWidth(event.getX());
            } else if (scene.getCursor() == javafx.scene.Cursor.SW_RESIZE) {
                double newWidth = stage.getWidth() - (event.getScreenX() - stage.getX());
                stage.setWidth(newWidth > stage.getMinWidth() ? newWidth : stage.getMinWidth());
                stage.setHeight(event.getY());
                stage.setX(event.getScreenX());
            } else if (scene.getCursor() == javafx.scene.Cursor.SE_RESIZE) {
                stage.setWidth(event.getX());
                stage.setHeight(event.getY());
            }
        });
    }

    private void enableWindowMovement(Scene root, Stage stage) {
        root.setOnMousePressed(event -> {
            // Armazena a posição inicial quando o mouse é pressionado
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            // Move a janela apenas se o cursor não estiver na área de redimensionamento
            if (root.getCursor() == javafx.scene.Cursor.DEFAULT) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }
}
