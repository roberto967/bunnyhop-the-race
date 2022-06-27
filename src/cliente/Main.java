package cliente;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent fxmlMain = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        Scene mainScene = new Scene(fxmlMain);

        Image icon = new Image("rabbit.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Bunny Hop - The Game");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}