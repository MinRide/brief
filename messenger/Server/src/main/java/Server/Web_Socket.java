package Server;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Web_Socket extends WebSocketServer{
    private Vector<Online_Connection> Connected_Users;
    
    private class Online_Connection{
        public WebSocket getConn(){
            return soc;
        }
        public String GetUser(){
            return user;
        }
        public Online_Connection(WebSocket a, String b){
            soc = a;
            user = b;
        }
        private WebSocket soc;
        private String user;
    }

    // Overrided functions
    @Override
    public void onMessage(WebSocket conn, String message){
        String[] Type_Of_Comand = message.split(" ");
        try{
            switch (Type_Of_Comand[0]){
                case "<LogIn>":
                    Log_In_Check(message,conn);
                break;
                case "<message>":
                    Message_From_User(message,conn);
                break;
                case "<recording>":
                    Sign_Up_Check(message, conn);
                break;
            }
        }catch(Exception e){} 
    }
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote){
        Online_Connection user = new Online_Connection(conn,"");
        for(int i=0; i!=Connected_Users.size(); i++)
            if (conn.equals(Connected_Users.elementAt(i).getConn())){
                user = Connected_Users.elementAt(i);
                break;
            }
        for (int i=0; i!=Connected_Users.size();i++)
            if (!Connected_Users.elementAt(i).equals(user))
                Connected_Users.elementAt(i).getConn().send("<offline> <User> "+user.GetUser()+" </User> </offline>");
        Connected_Users.remove(user);
    }
    @Override
    public void onError(WebSocket conn, Exception ex){}
    @Override
    public void onStart(){}
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake){}

    
    // Message propcessing
    private void Log_In_Check(String message, WebSocket conn) throws ClassNotFoundException, SQLException {
        String Username = Parse_Of_String("<login>", "</login>", message);
        String Password = Parse_Of_String("<password>", "</password>", message);
        
        ResultSet Result_Of_Request = DBclass.getUser(Username, Password);
        int count = 0;
        while(Result_Of_Request.next())
            count++;
        if (count == 1){
            conn.send("<ValidLogInStatus> valid </ValidLogInStatus>");
            Online_Connection us = new Online_Connection(conn,Username);
            Connected_Users.add(us);
            Send_Messages_From_DB(Username, conn);
            }
        else
            conn.send("<ValidLogInStatus> invalid </ValidLogInStatus>");
        
    }
    private void Message_From_User(String message, WebSocket conn) throws ClassNotFoundException, SQLException{
        Online_Connection user = new Online_Connection(null, null);
        String from = Parse_Of_String("<from>", "</from>", message);
        String to = Parse_Of_String("<to>", "</to>", message);
        String text = Parse_Of_String("<text>", "</text>", message);
        for (int i=0; i!=Connected_Users.size();i++)
            if (Connected_Users.elementAt(i).GetUser().equals(to))
                user = Connected_Users.elementAt(i);
        if (user.getConn() != null){
            String New_Message = "<NewMessage> <from> " + from + " </from> <text> " + text + " </text> </NewMessage>";
            user.getConn().send(New_Message);
        }
        DBclass.addNewMessege(from, to, text);
        Static_Class.Add_Message_To_DB(from,to,text);
    }
    private void Sign_Up_Check(String message, WebSocket conn) throws ClassNotFoundException, SQLException {
        String Username = Parse_Of_String("<login>","</login>",message);
        String Password = Parse_Of_String("<password>","</password>",message);
        ResultSet Result_Of_Request = DBclass.getUser(Username);
        int counter = 0;
        String mes;
        while(Result_Of_Request.next()){
            counter++;
        }
        if (counter == 0){
            mes = "<ValidRecordingStatus> " + "valid" + " </ValidRecordingStatus>";            
            conn.send(mes);
            Online_Connection us = new Online_Connection(conn,Username);
            Connected_Users.add(us);
            DBclass.addUser(Username, Password);
            Send_Messages_From_DB(Username, conn);
            for (int i = 0; i!=Connected_Users.size(); i++)
                if (!Connected_Users.elementAt(i).getConn().equals(conn))
                    Connected_Users.elementAt(i).getConn().send("<allUsers> <User> "+ Username + " </User> </allUsers>");
        }
        else{
            mes = "<ValidRecordingStatus> " + "invalid" + " </ValidRecordingStatus>";
            conn.send(mes);
        }

    }
    

    private void Send_Messages_From_DB(String Username,WebSocket conn) throws ClassNotFoundException, SQLException{
        ResultSet Result_Of_Request = DBclass.getAllUser(Username);
        Vector <String> Users_From_DB = new Vector<String>();
        while(Result_Of_Request.next())
            Users_From_DB.add(Result_Of_Request.getString(1));
        conn.send(Get_Users_From_DB(Users_From_DB));
        
        Result_Of_Request = DBclass.getAllMesseges(Username);
        String New_Message = "<OldMessages> ";
        while(Result_Of_Request.next()){
            New_Message += "<message> <from> " + Result_Of_Request.getString(2) + " </from> <to> " + Result_Of_Request.getString(3) + " </to> <text> " + Result_Of_Request.getString(4) + " </text> </message> ";
        }
        New_Message+= "</OldMessages>";
        conn.send(New_Message);
        
        for (int i = 0; i != Connected_Users.size(); i++ )
            if (!Connected_Users.elementAt(i).GetUser().equals(Username)){
                conn.send("<online> <User> " + Connected_Users.elementAt(i).GetUser() + " </User> </online>");
                Connected_Users.elementAt(i).getConn().send("<online> <User> " + Username + " </User> </online>");
            }
                
        
    }
    private String Parse_Of_String(String from, String to, String message){
        String[] Mas_Of_Words = message.split(" ");
        int a =  Arrays.asList(Mas_Of_Words).indexOf(from);
        int b =  Arrays.asList(Mas_Of_Words).indexOf(to);
        String Result = "";
        for (int i = a+1; i!=b;i++){
            if(i==a+1)
                Result += Mas_Of_Words[i];
            else
                Result += (" "+Mas_Of_Words[i]);
        }
        return Result;
    }
    private String Get_Users_From_DB(Vector <String> Users){
        String ans ="<allUsers> ";
        for(int i=0; i!=Users.size();i++)
            if (i==0)
                ans += ("<User> " + Users.elementAt(i) + " </User>");
            else 
                ans += (" <User> " + Users.elementAt(i) + " </User>");
        ans += " </allUsers>";
        return ans;
    }

    public Web_Socket(int port){
        super(new InetSocketAddress(port));
        Connected_Users = new Vector<Online_Connection>();
    }



}

