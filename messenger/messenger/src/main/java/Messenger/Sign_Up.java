package Messenger;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Sign_Up {
    @FXML
    private TextField log_inField;
    @FXML
    private TextField passwordField1;
    @FXML
    private TextField passwordField2;

    public void Sign_Up(ActionEvent act) throws IOException {
        Static_Class.Set_Sign_Up(this);
        String login = log_inField.getText().trim();
        String pas1 = passwordField1.getText().trim();
        String pas2 = passwordField2.getText().trim();
        if (pas1.equals(pas2) && !(login.equals("")) && !(pas1.equals("")))
            Static_Class.Sign_Up(login, pas1);
    }

    public void Sign_Up_Eror() throws IOException {
        App.setRoot("Sign_Up_Excep");
    }

    public void Set_New_Root() throws IOException {
        Static_Class.Set_Username(log_inField.getText().trim());
        App.setRoot("Main_Window");
    }
}
