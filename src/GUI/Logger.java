package GUI;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Created by Philipp on 2016-02-06.
 * used for Logging
 */
public class Logger extends TextArea {

    public Logger(){
        super();
    }

    /**
     * append to Logger
     * @param s
     */
    public void append(String s){
        //Might get out of sync, so we need to call from runLater
        Platform.runLater(
                () ->{
                    this.appendText("\n"+s);
                    this.setScrollTop(Double.MAX_VALUE);
                });
    }

}
