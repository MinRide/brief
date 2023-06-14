package Messenger;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Main_Window implements Initializable{
    @FXML
    private ListView<String> List_Of_Users;
    @FXML
    private ListView<String> Chat;
    @FXML
    private TextField Username;
    @FXML
    private TextField Text_Of_Message;
    @FXML
    private TextField Name_And_Status;
    private String CurrentUser;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Static_Class.Set_Main_Window(this);
        Username.setText(Static_Class.Get_UserName());
        String[] Temp_List = {};
        List_Of_Users.getItems().addAll(Temp_List);
        List_Of_Users.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                CurrentUser = List_Of_Users.getSelectionModel().getSelectedItem();
                Chat.getItems().clear();
                Static_Class.Set_All_Messages_To_MW(CurrentUser);
                if (Static_Class.Is_Status_Online(CurrentUser))
                    Name_And_Status.setText(CurrentUser + " (Online)");
                else 
                Name_And_Status.setText(CurrentUser);
            }  
        });
    }
    public void Say_Message(ActionEvent act) throws IOException{
        if (!(Text_Of_Message.getText().trim().equals("")) && CurrentUser != null){
            Chat.getItems().add("You: "+ Text_Of_Message.getText().trim());
            Static_Class.Say_Message(CurrentUser,Text_Of_Message.getText().trim());
            Text_Of_Message.clear();
        }
    }
    public void Add_Message(String user, String mes){    
        if (CurrentUser != null && CurrentUser.equals(user))
            Chat.getItems().add(mes);
    }
    public void Add_User_To_List(String obj){
        List_Of_Users.getItems().add(obj);
    }
    public void Add_Message_To_List(String obj){
        Chat.getItems().add(obj);
    }
    public void Change_Status_To_Online(String user){
        if (Name_And_Status.getText().equals(user))
            Name_And_Status.setText(user+" (Online)");
    }
    public void Change_Status_To_Offlien(String user){
        if (Name_And_Status.getText().equals(user+" (В сети)"))
            Name_And_Status.setText(user);
    }
}