package GUI;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Created by Philipp on 2016-02-06.
 * used for Logging
 */
public class Logger extends TextArea {
    private boolean pausedScroll = false;
    private double scrollPosition = 0;

    public Logger(){
        super();
//        DOesnt work
//        this.scrollTopProperty().setValue(10);//always scroll by 10 pixels
    }

    public void setMessage(String data) {
        if (pausedScroll) {
            scrollPosition = this.getScrollTop();
            this.setText(data);
            this.setScrollTop(scrollPosition);
        } else {
            this.setText(data);
            this.setScrollTop(Double.MAX_VALUE);
        }
    }

    public void append(String s){
        this.appendText("\n"+s);
        Platform.runLater(() -> this.setScrollTop(Double.MAX_VALUE));
    }

    public void pauseScroll(Boolean pause) {
        pausedScroll = pause;
    }

}
