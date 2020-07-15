package sample;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application{

    public static void main(String[] args) {

        Application.
                launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        //root.autosize();

        Scene scene = new Scene(root, 1400, 900);
        //stage.getIcons().add(new Image(getClass().getResource("icon.jpeg")));
        stage.getIcons().add(new Image("/sample/icon.png"));
        stage.setTitle("Clothoid");
        stage.setScene(scene);
        stage.show();
    }
}