package Messenger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Static_Class {
    private static String name;
    private static Log_In log_In_Con;
    private static Main_Window Main_Window_Con;
    private static Sign_Up Sign_Up_Con;
    private static Web_Socket Socket;
    private static Map<String, Vector<String>> messages = new HashMap<>();
    private static Vector<String> onlineUsers= new Vector<String>();
    public static void RecordingStatus(String status) throws IOException {
        if (status.equals("valid"))
            Sign_Up_Con.Set_New_Root();
        else
            Sign_Up_Con.Sign_Up_Eror();
    }
    public static void Set_Username(String Nname){
        name = Nname;
    }
    public static String Get_UserName(){
        return name;
    }
    public static void Set_Log_In(Log_In a){
        if (log_In_Con ==null)
            log_In_Con = a;
    }
    public static void Set_Main_Window(Main_Window a){
        if (Main_Window_Con == null)
            Main_Window_Con = a;
    }
    public static void Set_Sign_Up(Sign_Up a){
        if (Sign_Up_Con == null)
            Sign_Up_Con = a;
    }
    public static void Set_Web_Socket() throws URISyntaxException{
        if (Socket == null){
            
            URI serverUri = new URI("ws://localhost:368");
            Socket = new Web_Socket(serverUri);
            Socket.connect();
        }
    }
    public static void Add_User_To_MW(String user){
        Main_Window_Con.Add_User_To_List(user);
    }
    public static void New_Message(String user, String mes){
        Vector<String> a;
        if(messages.get(user) == null)
            a = new Vector<String>();
        else
            a = messages.get(user);
        a.add(user+": "+mes);
        messages.put(user,a);
        Main_Window_Con.Add_Message(user,user+": "+mes);
    }
    public static void Set_All_Messages_To_MW(String curUser){
        Vector<String> a = messages.get(curUser);
        if (a != null)
            for (int i = 0; i!=a.size();i++)
                Main_Window_Con.Add_Message_To_List(a.get(i)); 
    }
    public static void Set_DB_Messages_To_MW(String from, String to, String mes){
        Vector<String> a;
        if (name.equals(to)){
            if(messages.get(from) == null)
                a = new Vector<String>();
            else
                a = messages.get(from);
            a.add(from+": "+mes);
            messages.put(from,a);
            Main_Window_Con.Add_Message(from,from+": "+mes);
        } else{
            if(messages.get(to) == null)
                a = new Vector<String>();
            else
                a = messages.get(to);
            a.add("You: "+mes);
            messages.put(to,a);
            Main_Window_Con.Add_Message(to,"You: "+mes);
        }

    }
    public static void Change_Status_To_Online(String user) {
        onlineUsers.add(user);
        Main_Window_Con.Change_Status_To_Online(user);
    }
    public static void Change_Status_To_Offlien(String user) {
        onlineUsers.remove(user);
        Main_Window_Con.Change_Status_To_Offlien(user);
    }
    public static boolean Is_Status_Online(String user){
        return onlineUsers.contains(user);
    }
    public static void Close_Socket_Connection(){
        Socket.close();
    }
    public static void Sign_Up(String login, String password) throws IOException {
        Socket.Check_Valid_Sign_Up(login, password);
    }
    public static void Check_Valid_Log_In_Status(String login, String password) throws IOException{
        Socket.Check_Valid_Log_In(login, password);
    }
    public static void Say_Message(String Address, String message) throws IOException{
        Socket.Say_Message(Address, message);
        Vector<String> a;
        if(messages.get(Address) == null)
            a = new Vector<String>();
        else
            a = messages.get(Address);
        a.add("You: "+message);
        messages.put(Address,a);   
    } 
    public static void Restart_Socket_Connection(){
        if (Socket.isClosed())
            Socket.reconnect();
    }
    public static void Log_In_Valid_Ans(String status) throws IOException{
        if (status.equals("valid"))
            log_In_Con.Set_New_Root();
        else
            log_In_Con.Log_In_Eror();
    }
    public static void Log_In_Connection_Eror() throws IOException {
        if (log_In_Con != null)
            log_In_Con.Log_In_Connection_Eror();
    }
}
