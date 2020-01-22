import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Game {
    private static List<Socket> matchMaking = new ArrayList<>();
    private Socket client1;
    private Socket client2;



        Game(Socket client){
            matchMaking.add(client);        //add client to list of players waiting for a game
        }
}
