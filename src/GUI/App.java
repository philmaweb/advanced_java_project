package GUI;

import Presenter.Presenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by Philipp on 2016-01-27.
 * Runs the whole Application
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //create View
        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        primaryStage.setTitle("RNA-Viewer 3000");
        //set logo file
        primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("logo.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
