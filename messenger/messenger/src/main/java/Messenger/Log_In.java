package Messenger;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Log_In{
    @FXML
    private TextField log_inField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label LException;
    public void Log_In(ActionEvent act) throws URISyntaxException, IOException{
        Static_Class.Set_Log_In(this);
        Static_Class.Restart_Socket_Connection();
        Static_Class.Set_Username(log_inField.getText().trim());
        String login = log_inField.getText().trim();
        String password = passwordField.getText().trim();
        if (login.equals("") || login.equals(""))
            Log_In_Eror();
        else
            Static_Class.Check_Valid_Log_In_Status(login, password);
    }
    public void Sign_Up(ActionEvent act) throws IOException{
        App.setRoot("Sign_Up");
    }

    public void Set_New_Root() throws IOException{
        App.setRoot("Main_Window");
    }
    public void Log_In_Eror() throws IOException {
        App.setRoot("Log_In_Excep");
    }
    public void Log_In_Connection_Eror() throws IOException {
        App.setRoot("Log_In_Coner");
    }

}
