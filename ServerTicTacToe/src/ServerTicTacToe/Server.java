package ServerTicTacToe;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Server of the tic-tac-toe game
 * @author Yunying Zhang, Adam D'Andrea
 * @since November 7, 2020
 * @version 2
 *
 */
public class Server {

    private Socket socket;
    private ServerSocket serverSocket;
    private PrintWriter socketOut;
    private BufferedReader socketIn;
    private ExecutorService pool;
    static ArrayList<Game> users = new ArrayList<Game>();
    static int i = 0;

/**
 * connects to server socket in the constructor
 */
    public Server() {
        try {
            serverSocket = new ServerSocket(8888);
            pool = Executors.newCachedThreadPool();
            System.out.println("Server is running...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/**
 * runs the server
 */
    public void runServer() {
        try {
            while (true) {
                //accepts connection for players
                socket = serverSocket.accept();
                socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                socketOut = new PrintWriter(socket.getOutputStream(), true);
                //makes sure more than one players plays at one time
                if(users.size()!=0) {
                	System.out.println("A game is running...");
                }
                //initializes game object; user index is size/2+1 if more than 2 players
                Game game = new Game(socket, "Player " + ((users.size()%2)+1) ,socketIn, socketOut,i);
                //add client to client list
                users.add(game);
                //start thread
                pool.execute(game);
                //increase i after a new player joins
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socketIn.close();
            socketOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String [] args) {
        Server myServer = new Server();
        myServer.runServer();
    }
}