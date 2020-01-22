//this class represents server client

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Player{
    private static int idCounter = 0;
    private int id;
    private Socket socket;

    Player(Socket socket) {
        this.socket = socket;
        this.id = idCounter + 1;
        idCounter ++;
    }

    @Override
    public String toString() {
        return this.id + " - " + socket.getInetAddress() + ":" + socket.getLocalPort();
    }

    void disconnect(){
        log("disconnecting client " + toString());
        Main.activeClients.remove(this);
        try {
            socket.close();
        } catch (IOException e) {
            log("disconnecting failed");
            e.printStackTrace();
        }
    }

    private void log(String message){
        SimpleDateFormat sdf =  new SimpleDateFormat("DD-MM-YY HH:mm:ss");
        System.out.println("[" + sdf.format(Calendar.getInstance().getTime()) + "][" + socket.getLocalPort() + "]" + message);
    }

    Socket getSocket() {
        return socket;
    }

    int getId() {
        return id;
    }

    void send(String message){
        try {
            PrintWriter out = new PrintWriter(this.getSocket().getOutputStream(), true);
            out.println(message);
            out.close();
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
        }
    }

    String receive(){
        String message;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.getSocket().getInputStream()));
            message = in.readLine();
            in.close();
            return message;
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
            return null;
        }
    }
}


//todo socket should be a global field