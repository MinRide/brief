module user {
    requires javafx.controls;
    requires javafx.fxml;
    requires Java.WebSocket;
 
    opens Messenger;
    exports Messenger;
}
