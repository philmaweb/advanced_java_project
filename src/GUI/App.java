package GUI;

import Presenter.Presenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Philipp on 2016-01-27.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        //presenter is created by the controller
        primaryStage.setTitle("Project RNA-Viewer");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
