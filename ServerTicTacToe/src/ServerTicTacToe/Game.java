
package ServerTicTacToe;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

//STUDENTS SHOULD ADD CLASS COMMENTS, METHOD COMMENTS, FIELD COMMENTS 

/**
 * The Game class assigns players, and begins the game.
 * @author Yunying Zhang, Adam D'Andrea
 * @since November 7, 2020
 * @version 2
 *
 */

public class Game implements Constants, Runnable {

	
	private Board theBoard;
	private Referee theRef;
	private Socket socket;
	private PrintWriter socketOut;
	private BufferedReader socketIn;
	private String name;
	private Player xPlayer = null, oPlayer = null;
	private int indexInServer;

	public Game(Socket socket, String name, BufferedReader socketIn, PrintWriter socketOut,int i) {
    	this.socket = socket;
    	this.name = name;
    	this.socketIn = socketIn;
    	this.socketOut = socketOut;
    	theBoard  = new Board();
    	indexInServer = i;
	}

	/**
	 * appoints a referee to the game and begins the game
	 * @param r the Referee
	 * @throws IOException
	 */
    public void appointReferee(Referee r) throws IOException {
        theRef = r;
    	theRef.runTheGame();
    }
   
    /**
     * runs the tic-tac-toe game
     */
	public void run() {
		try {
			
			//User input for X-Player			
			if (name.equals("Player 1")) {
				socketOut.println("Message:  LET THE BATTLE BEGIN!!!");
				socketOut.println("You are player X.");
				socketOut.println("Please enter your name of the 'X' player:");
				//User input for X-Player
				name = socketIn.readLine();
				//reads from user for the name
				while (name == null) {
					socketOut.println("Please try again: ");
					name = socketIn.readLine();
				}
				//creates a new Player object for X-Player
				xPlayer = new Player(name, LETTER_X,socket);
				xPlayer.setBoard(theBoard);
				//creates a new Referee object
				theRef = new Referee();
				theRef.setBoard(theBoard); 
				theRef.setxPlayer(xPlayer); 
				
				Game game = Server.users.get(indexInServer + 1);
				if (game.oPlayer != null ) {
					//sets o player for thread 
					theRef.setoPlayer(game.getoPlayer()); 
					// appoint the referee and start the game
					this.appointReferee(theRef); 
				}
			}
			
			//user input for O-Player
			if (name.equals("Player 2")) {
				socketOut.println("Message: LET THE BATTLE BEGIN!!!");
				socketOut.println("You are player O.");
				socketOut.println("Please enter your name of the 'O' player:");
				name = socketIn.readLine();
				Game game1 = Server.users.get(indexInServer-1);
				//reads from the user for name
				while (name == null) {
					System.out.print("Please try again: ");
					name = socketIn.readLine();
				}

				//creates a new Player object for O-Player
				oPlayer = new Player(name, LETTER_O,socket);
				oPlayer.setBoard(game1.getTheBoard());
				if (game1.xPlayer != null ) {
					//sets ref, xplayer, oplayer,board
					theRef = game1.theRef;
					theRef.setxPlayer(game1.getxPlayer());
					theRef.setoPlayer(oPlayer); 
					theRef.setBoard(game1.getTheBoard()); 
					game1.theRef.setoPlayer(oPlayer);
					//apppint referee and start game
					this.appointReferee(theRef); 
				}
			}
		}catch (IOException e){
				e.printStackTrace();
			}
	}
	//getters and setters
	public Board getTheBoard() {
		return theBoard;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Player getxPlayer() {
		return xPlayer;
	}

	public void setxPlayer(Player xPlayer) {
		this.xPlayer = xPlayer;
	}

	public Player getoPlayer() {
		return oPlayer;
	}

	public void setoPlayer(Player oPlayer) {
		this.oPlayer = oPlayer;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	

	
}