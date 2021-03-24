package ServerTicTacToe;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * make players move of tic-tac-toe game
 * @author Yunying Zhang, Adam D'Andrea
 * @since November 7, 2020
 * @version 1
 *
 */
public class Player {
	
    private String name;
    private Board board;
    private Player opponent;
    private char mark;
    private Socket socket;
    private PrintWriter socketOut;
    private BufferedReader socketIn;

    
    /**
     * Constructs a Player object
     * @param name: name of the player
     * @param mark: the mark of the player
     * @throws IOException
     */
    public Player(String name, char mark, Socket socket) throws IOException {
        this.name = name;
        this.mark = mark;
        this.socket = socket;
        socketOut = new PrintWriter(socket.getOutputStream(), true);
        socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    
    /**
     * the player and the opponent takes turn to play, until there is a winner or tie game
     * @throws IOException
     */
    public void play() throws IOException {
        
        try {
        	//set up name for player x
    	String xPlayerName = this.getName();
        socketOut.println("PlayerX " + xPlayerName);
        opponent.socketOut.println("PlayerX " + xPlayerName);

       //set up name for player o
        String oPlayerName = opponent.getName();
        socketOut.println("PlayerO " + oPlayerName);
        opponent.socketOut.println("PlayerX " + xPlayerName);

        while (!board.xWins() && !board.oWins() && !board.isFull()) {
        	//O-Player turn if X-Player hasn't won and the board is not full
            if (!board.xWins() && !board.isFull()) {
                opponent.socketOut.println("It is your turn, " + opponent.name);
                //turn off X players buttons if waiting for opponent to make move
                socketOut.println("Waiting for opponent to make a move...");
                opponent.makeMove();
                board.checkWinner(mark);
            }    
              	
        	//X-Player turn if O-Player hasn't won and the board is not full
                if (!board.oWins() && !board.isFull()) {
                    socketOut.println("It is your turn, " + name);
                    //turn off O players buttons if waiting for x player
                    opponent.socketOut.println("Waiting for opponent to make a move...");
                    makeMove();
                    board.checkWinner(mark);
                }
                
            }
        //prints on clients' screen for the game result
        if (board.xWins()) {
        	socketOut.println("Game over!" + " name" + " WINS!!!");
        	opponent.socketOut.println("Game Ended..." + " name" + " WINS!!!");
        }
        if (board.oWins()) {
        	socketOut.println("Game over!" + opponent.name + " WINS!!!");
            opponent.socketOut.println("Game over!" +  opponent.name + " WINS!!!");
        }
        if (!board.oWins() && !board.xWins()) {
            socketOut.println("TIE!!!");
            opponent.socketOut.println("TIE!!!!");
        }
        socketOut.println("Game Ended...");
        opponent.socketOut.println("Game over!");
    }catch(Exception e) {
    	socketOut.println("Player has left");
    	opponent.socketOut.println("Player has left");
    }
    }

   
    /**
     * moves the player, only one player is allowed to make move at at time
     * @throws IOException
     */
    public void makeMove() throws IOException {
        while (true) {
            //gets response from socket
            String response = socketIn.readLine();
            //Splits string into row and column
            String[] spot = response.split("\\s+");
            //Checks to see if their is a mark in that position already
            if (board.getMark(Integer.parseInt(spot[0]), Integer.parseInt(spot[1])) != mark &&
                    board.getMark(Integer.parseInt(spot[0]), Integer.parseInt(spot[1])) != opponent.mark) {
                board.addMark(Integer.parseInt(spot[0]), Integer.parseInt(spot[1]), mark);
                opponent.board.addMark(Integer.parseInt(spot[0]), Integer.parseInt(spot[1]), mark);
                socketOut.println("MARKING "+ spot[0]+" "+spot[1]+" "+ mark);
                opponent.socketOut.println("MARKING "+ spot[0]+" "+spot[1]+" "+ mark);
                break;
            } else {
                socketOut.println("Please choose a different position.");
            }
        }
    }

    public String getName() {
        return name;
    }
    
    public void setBoard(Board board) {
        this.board = board;
    }
   
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }   

}
