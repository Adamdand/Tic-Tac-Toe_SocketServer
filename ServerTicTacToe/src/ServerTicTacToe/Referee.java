package ServerTicTacToe;

import java.io.IOException;

public class Referee {

	/**
	 * The X-Player. A Player data type.
	 */
	private Player xPlayer;

	/**
	 * The O-Player. A Player data type.
	 */
	private Player oPlayer;

	/**
	 * The Tic-Tac-Toe board. A Board data type.
	 */
	private Board board;



	/**
	 * Constructs a Referee object
	 */
	public Referee() {
	}



	/**
	 * Sets the opponent of X-Player and O-Player. Displays the board and plays the
	 * game.
	 * 
	 * @throws IOException
	 */
	public void runTheGame() throws IOException {
		xPlayer.setOpponent(oPlayer);
		oPlayer.setOpponent(xPlayer);
		xPlayer.play();
	}


	/**
	 * Sets the value of board with the specified value.
	 * 
	 * @param board Value is the new Board for board.
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * Sets the value of Player with the specified value.
	 * 
	 * @param xPlayer Value is the new Player for X-Player.
	 */
	public void setxPlayer(Player xPlayer) {
		this.xPlayer = xPlayer;
	}

	/**
	 * Sets the value of Player with the specified value.
	 * 
	 * @param oPlayer Value is the new Player for O-Player
	 */
	public void setoPlayer(Player oPlayer) {
		this.oPlayer = oPlayer;
	}

}