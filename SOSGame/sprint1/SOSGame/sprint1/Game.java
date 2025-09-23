package SOSGame.sprint1;

import java.util.Random;

public abstract class Game {
	protected int size;
	protected char[][] board;
	protected int[][] ownerBoard; // 0 = empty, 1 = player1, 2 = player2
	/*
	 * ex of owner board:
	 *  [1, 0, 2],  // row 0: Player 1, empty, Player 2
  		[0, 1, 0],  // row 1: empty, Player 1, empty
  		[2, 0, 1]   // row 2: Player 2, empty, Player 1
	 */
	protected boolean player1Turn;
	
	public Game(int size) {
		this.size = size;
		this.board = new char[size][size];
		Random random = new Random();
		this.player1Turn = random.nextBoolean(); //random first player turn
		ownerBoard = new int[size][size];
		for (int r = 0; r < size; r++) {
		    for (int c = 0; c < size; c++) {
		        ownerBoard[r][c] = 0; // empty
		    }
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public boolean isPlayer1Turn() {
		return player1Turn;
	}
	
	public void switchTurn() {
		player1Turn = !player1Turn;
	}
	
	public boolean placeLetter(int row, int col, char letter) {
		if (board[row][col] == '\0') { //empty cell
			board[row][col] = letter;
			ownerBoard[row][col] = player1Turn ? 1 : 2; //track player
			return true;
		}
		return false;
	}
	
	public char getCell(int row, int col) {
		return board[row][col];
	}
	
	public boolean extraTurn() {
	    // returns true if the player gets another turn
	    return false; // default
	}
	
	public class MoveResult {
	    public boolean moveMade;      // whether the move was successful
	    public boolean gameOver;       // whether the game ended after this move
	    public String winner;          // winner text if gameOver
	    public boolean nextPlayerIs1;  // whose turn is next (player1)
	}

	public MoveResult makeMove(int row, int col, char letter) {
	    MoveResult result = new MoveResult(); //create new MoveResult object to store outcome of this move
	    result.moveMade = placeLetter(row, col, letter); //attempt to place letter on board

	    if (result.moveMade) { //if move successfully made
	    	
	        boolean extra = extraTurn();//check to see if user has an extra turn
	        if (!extra) {
	        	switchTurn();
	        }
	        result.gameOver = checkGameOver(); //update MoveResult to indicate if the game is over
	        result.winner = result.gameOver ? getWinner() : null; // if game over, get winner; otherwise, leave null
	        result.nextPlayerIs1 = isPlayer1Turn(); //store whose turn it is (true = p1, false = p2)
	    }

	    return result;
	}
	
	
	public abstract boolean checkGameOver();// each subclass has to implement its own version
	public abstract String getWinner();// each subclass has to implement its own version
}