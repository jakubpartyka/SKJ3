import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Client{
    private static final String LOGOUT = "LOGOUT";
    private static final String PLAY   = "PLAY";
    private static final String LIST   = "LIST";

    private static PrintWriter out;
    private static BufferedReader in;

    private static Socket socket;
    private static String server_address = "localhost";
    private static int server_port       = 9090;

    public static void main(String[] args) throws IOException {
        //connect to server and stop if failed. (stack trace will be printed)
        if(!connect()){
            log("connection unsuccessful");
            return;
        }

        //set up communication with server
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String choice = "LOGOUT";
        do {
                //ask user for action
                System.out.println("what would you like to do?");
                System.out.println("PLAY, LIST, LOGOUT" );
                System.out.print("> ");

                //read user choice
                try{
                    choice = new Scanner(System.in).nextLine();
                }
                catch (Exception e){
                    disconnect();
                }
        } while ((!choice.equals(PLAY)) && (!choice.equals(LOGOUT) && (!choice.equals(LIST))));

        //send user choice to server
        send(choice);

        switch (choice){
            case LOGOUT  :
                disconnect();
                break;
            case PLAY    :
                break;
            case LIST    :
                System.out.println(receive());
                break;
            default :
                log("wrong choice code sent. Closing connection");
                disconnect();
        }

        disconnect();
    }

    private static boolean connect() {
        try {
            socket = new Socket(InetAddress.getByName(server_address), server_port);
            log("connected");
            return true;
        } catch (IOException e) {
            log("error");
            e.printStackTrace();
            return false;
        }
    }

    private static void log(String message){
        SimpleDateFormat sdf =  new SimpleDateFormat("DD-MM-YY HH:mm:ss");
        System.out.println("[" + sdf.format(Calendar.getInstance().getTime()) + "]C: " + message);
    }

    private static void disconnect(){
        out.println(3);                 //send disconnect message
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void send(String choice){
        out.println(choice);
        log("sent " + choice + " to server");
    }

    private static String receive(){
        try {
            return in.readLine();
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            log("connection was lost. client terminated");
        }
        return null;
    }




}
