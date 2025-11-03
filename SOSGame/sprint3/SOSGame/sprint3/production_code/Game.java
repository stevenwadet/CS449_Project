package SOSGame.sprint3.production_code;

import java.util.Random;


/**
 * abstract class game.
 */
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
  
  /**
   * constructor for game.
   *
   * @param size the size of the board.
   */
  public Game(int size) { //
	
    if (size < 3 || size > 10) { 
      throw new IllegalArgumentException("Board size must be between 3 and 50.");
    }
    
    Random random = new Random();
    this.size = size;
    this.board = new char[size][size];
    this.player1Turn = random.nextBoolean(); // random first player turn
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
  
  public char[][] getBoard() {
    return board;
  }

  
  public boolean isPlayer1Turn() {
    return player1Turn;
  }

  /**
   * switches player turns.
   */
  public void switchTurn() {
    player1Turn = !player1Turn;
  }

  /**
   * places the user selected letter in the cell they chose.
   *
   * @param row row where cell is.
   * @param col column where cell is.
   * @param letter letter chosen by player.
   * @return true = letter placed, false = letter couldn't be placed.
   */
  public boolean placeLetter(int row, int col, char letter) {
    if (board[row][col] == '\0') { // empty cell
      board[row][col] = letter;
      ownerBoard[row][col] = player1Turn ? 1 : 2; // track player
      return true;
    }
    return false;
  }
  
  /**
   * returns the row and column of a cell.
   *
   * @param row row where cell is.
   * @param col column where cell is.
   * @return the character in the given cell of the board.
   */
  public char getCell(int row, int col) {
    return board[row][col];
  }

  /**
   * flag for a user getting another turn in general game.
   *
   * @return true = another turn, false = other player's turn
   */
  public boolean extraTurn() {
    // returns true if the player gets another turn
    return false; // default
  }

  /**
   * represents the result of a single move.
   */
  public class MoveResult {
    public boolean moveMade;      // whether the move was successful
    public boolean gameOver;       // whether the game ended after this move
    public String winner;          // winner text if gameOver
    public boolean nextPlayerIs1;  // whose turn is next (player1)
  }
  
  
  /**
   * making a move and checking for game end.
   *
   * @param row row where letter was placed.
   * @param col column where letter was placed.
   * @param letter letter that was placed.
   * @return a MoveResult object containing outcome of move and whose turn is next
   */
  public MoveResult makeMove(int row, int col, char letter) {
    MoveResult result = new MoveResult(); // store outcome of this move
    result.moveMade = placeLetter(row, col, letter); // attempt to place letter on board

    if (result.moveMade) { // if move successfully made 
      boolean extra = extraTurn(); // check to see if user has an extra turn
      if (!extra) {
        switchTurn();
      }
      result.gameOver = checkGameOver(); 
      result.winner = result.gameOver ? getWinner() : null; 
      result.nextPlayerIs1 = isPlayer1Turn(); // store whose turn it is (true = p1, false = p2)
    }

    return result;
  }
  /**
   * checks if game is over.
   *
   * @return true = game is over, false = game is not over.
   */
  
  public abstract boolean checkGameOver(); // each subclass has to implement its own version
  
  /**
   * gets the winner of the game.
   *
   * @return winner of the game.
   */
  public abstract String getWinner(); // each subclass has to implement its own version
}