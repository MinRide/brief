module MyServer {
    requires javafx.controls;
    requires javafx.fxml;
    requires Java.WebSocket;
    requires java.sql;
    

    opens Server to javafx.fxml;
    exports Server;
}
