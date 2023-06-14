package Server;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class UI_Controller implements Initializable{
    @FXML
    private ListView<String> List_Of_Chats;
    @FXML
    private ListView<String> Chat;
    private String selectedChat;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Static_Class.Set_Controller(this);
        Vector<String> Temp_List = new Vector<String>();
        try {
            Temp_List = Static_Class.Get_All_Chats_From_DB();
        } catch (Exception e) {}
        List_Of_Chats.getItems().addAll(Temp_List);
        List_Of_Chats.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                selectedChat = List_Of_Chats.getSelectionModel().getSelectedItem();
                Chat.getItems().clear();
                try {
                    Vector<String> Temp_List = Static_Class.Get_Chat_From_DB(selectedChat);
                    Chat.getItems().setAll(Temp_List);
                } catch (Exception e) {}
            }
        });
    }
    public void add_Chat_To_List(String chat) {
        List_Of_Chats.getItems().add(chat);
    }
    public void add_Message_To_List_Of_Chats(String from, String to, String text) {
        if (selectedChat!=null && (selectedChat.equals(from + " <-> " + to) || selectedChat.equals(to + " <-> " + from)))
            Chat.getItems().add(from + ": "+ text);
    }
}
