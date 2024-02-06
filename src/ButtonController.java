import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ButtonController implements Initializable {

    @FXML
    private Button StartBtn;
    @FXML
    private Button outputField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void startGame(ActionEvent event){
        Main.changeScene(0);
        System.out.println("Game Started");
        Main.gameStart();
    }


}
