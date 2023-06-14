package Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Static_Class {
    private static UI_Controller Controller;
    private static Web_Socket Socket;
    public static void Set_Controller(UI_Controller a){
        Controller = a;
    }
    public static void Server_Start(){
        int post = 368;
        Socket = new Web_Socket(post);
        Socket.start();
    }
    public static void Server_Stop() throws InterruptedException{
        Socket.stop();
    }
    public static Vector<String> Get_Chat_From_DB(String chat) throws ClassNotFoundException, SQLException { 
        Vector<String> Result = new Vector<String>();
        String s = " <->";
        int i = chat.indexOf(s);
        String user1 = chat.substring(0,i);
        String user2 = chat.substring(i+s.length()+1);
        ResultSet resSet = DBclass.getAllMesseges(user1, user2);
        while (resSet.next()){
            String a = resSet.getString(2) + ": " + resSet.getString(4);
            Result.add(a);
        }
        return Result;
    }
    public static Vector<String> Get_All_Chats_From_DB() throws ClassNotFoundException, SQLException  {
        ResultSet resSet = DBclass.getAllMesseges();
        Vector<String> resultVector = new Vector<String>();
        String a;
        String b;
        while(resSet.next()){
            a = resSet.getString(2) +" <-> " + resSet.getString(3);
            b = resSet.getString(3) +" <-> " + resSet.getString(2);
            if (!resultVector.contains(a) && !resultVector.contains(b))
                resultVector.add(a);
        }
        return resultVector;
    }
    public static void Add_Message_To_DB(String from, String to, String text) throws ClassNotFoundException, SQLException {
        ResultSet resSet = DBclass.getAllMesseges(from,to);
        int counter = 0;
        while (resSet.next())
            counter++;
        if (counter == 1)
            Controller.add_Chat_To_List(from + " <-> " + to);
        else
            Controller.add_Message_To_List_Of_Chats(from, to, text);
    }
}
