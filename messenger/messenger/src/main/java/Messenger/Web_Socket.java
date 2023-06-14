package Messenger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;


public class Web_Socket extends WebSocketClient{
    @Override
    public void onOpen(ServerHandshake handshake){}
    @Override
    public void onClose(int code, String reason, boolean remote) {}
    @Override
    public void onError(Exception ex) {
        try {
            Static_Class.Log_In_Connection_Eror();
        } catch (IOException e) {}
    }   
    @Override
    public void onMessage(String message){
        String[] arrString = message.split(" ");
       try{
            switch (arrString[0]){
                case "<allUsers>":
                All_Users_From_DB(message);
                break;
                case "<online>":
                Check_Online_Status(message);
                break;
                case "<OldMessages>":
                All_Messages_From_DB(message);
                break;
                case "<ValidLogInStatus>":
                Valid_Log_In_Status(message);
                break;
                case "<ValidRecordingStatus>":
                Valid_Sign_Up_Status(message);
                break;
                case "<NewMessage>":
                New_Message(message);
                break;
                case "<offline>":
                Check_Offline_Status(message);
                break;
            }
        } catch(Exception e){}

    }
    public void Say_Message(String address, String message) throws IOException{
    if (isOpen()){
        String request = "<message> <from> " + Static_Class.Get_UserName() + " </from> <to> " + address + " </to> <text> " + message + " </text> </message>";
        send(request);
    }
    else
        Static_Class.Log_In_Connection_Eror();
    }
    public void Check_Valid_Log_In(String login, String password) throws IOException{
        if (isOpen()){
            String request = "<LogIn> " + "<login> " + login + " </login>" + " <password> " + password + " </password>" + " </LogIn>";
            send(request);
        }
        else
            Static_Class.Log_In_Connection_Eror();
    }
    public void Check_Valid_Sign_Up(String login, String password) throws IOException {
        if (isOpen()){
            String mes = "<recording> " + "<login> " + login + " </login> " + "<password> " + password + " </password>" + " </recording>";
            send(mes);
        }
        else 
            Static_Class.Log_In_Connection_Eror();
    }
    private String Parse(String from, String to, String message){
        String[] arrStrings = message.split(" ");
        int a =  Arrays.asList(arrStrings).indexOf(from);
        int b =  Arrays.asList(arrStrings).indexOf(to);
        String res = "";
        for (int i = a+1; i!=b;i++){
            if(i==a+1)
                res += arrStrings[i];
            else
                res += (" "+arrStrings[i]);
        }
        return res;
    }
    public Web_Socket(URI uri){
        super(uri);
    }
    private void All_Users_From_DB(String message){
        message = message.substring(11);
        while (!message.equals("</allUsers>")){
            String user = Parse("<User>", "</User>", message);
            int i = message.indexOf("</User>") ;
            message = message.substring(i + 8);
            Static_Class.Add_User_To_MW(user);
        }
    }
    private void Check_Online_Status(String message){
        String user = Parse("<User>", "</User>", message);
        Static_Class.Change_Status_To_Online(user);
        
    }
    private void All_Messages_From_DB(String message) {
        message = message.substring(14);
        while(!message.equals("</OldMessages>")){
            String userMessage = Parse("<message>","</message>",message);
            String from = Parse("<from>", "</from>", userMessage);
            String to = Parse("<to>", "</to>", userMessage);
            String text = Parse("<text>", "</text>", userMessage);
            Static_Class.Set_DB_Messages_To_MW(from, to, text);
            int i = message.indexOf("</message>");
            message = message.substring(i + 11);
        }
    }
    private void Valid_Log_In_Status(String message) throws IOException{
        String[] arrString = message.split(" ");
        Static_Class.Log_In_Valid_Ans(arrString[1]);
    }
    private void Valid_Sign_Up_Status(String message) throws IOException {
        String status = message.split(" ")[1];
        Static_Class.RecordingStatus(status);
    }
    private void New_Message(String message){
        String from = Parse("<from>", "</from>", message);
        String text = Parse("<text>", "</text>", message);
        Static_Class.New_Message(from,text); 
    }
    private void Check_Offline_Status(String message){
        String user = Parse("<User>","</User>",message);
        Static_Class.Change_Status_To_Offlien(user);
    }
}