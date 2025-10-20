package SOSGame.sprint2.production_code;

/**
 * simple game class.
 */
public class SimpleGame extends Game {
 
  private boolean gameOver = false;
  private boolean winnerIsPlayer1; // true if player1 won, false if player 2 won
  private SOSInfo lastSOS; // store last SOS for GUI drawing
  private boolean isTie = false;
  private int lastPlayer = 1; // 1=blue, 2=red
  private int lastMoveRow = -1;
  private int lastMoveCol = -1;

  public SOSInfo getLastSOS() {
    return lastSOS;
  }
  
  public int getLastMoveRow() {
    return lastMoveRow;
  }
  
  public int getLastMoveCol() {
	    return lastMoveCol;
	  }

  /**
   * create a simple game with specified board size.
   *
   * @param size the size of the board
   */
  public SimpleGame(int size) {
    super(size);
    
    if (size < 3 || size > 10) { 
      throw new IllegalArgumentException("Board size must be between 3 and 50.");
    }
  }
  

  /**
   * checks if board is full.
   *
   * @return true = full, false = not full
   */
  public boolean isBoardFull() {
    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        if (board[r][c] == '\0') {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean checkGameOver() {
    if (gameOver) {
      return true;
    }
    
    SOSInfo sos = findSOS(lastMoveRow, lastMoveCol, lastPlayer); 
    if (sos != null) { // if we have an SOS
      gameOver = true;
      winnerIsPlayer1 = (sos.player == 1);
      lastSOS = sos; // save it to draw line in GUI
    } else {
      if (isBoardFull()) {
        isTie = true; // game is a tie if board is full with no SOS
        gameOver = true; // set game to over
      }
    }

    return gameOver;
  }
  
  @Override
  public String getWinner() {
    if (isTie) {
      return "It's a Tie!";
    }
    if (!gameOver) {
      return null;
    }
    return winnerIsPlayer1 ? "Blue Player Wins!" : "Red Player Wins!";
  }



  @Override
  public MoveResult makeMove(int row, int col, char letter) {
    lastMoveRow = row;
    lastMoveCol = col;
    lastPlayer = isPlayer1Turn() ? 1 : 2;
    
    MoveResult result = super.makeMove(row, col, letter); // call Game.makeMove
    if (result.moveMade) {
      SOSInfo sos = findSOS(lastMoveRow, lastMoveCol, lastPlayer);
      if (sos != null) { 
        lastSOS = sos; // store for GUI line drawing
      }
    }

    return result;
  }

  // returns 0 if no SOS found, 1 if player1 made SOS, 2 if player2 made SOS
  private SOSInfo findSOS(int lastRow, int lastCol, int currentPlayer) {
    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        if (board[r][c] == 'S') {
          // horizontal
          if (c + 2 < size && board[r][c + 1] == 'O' && board[r][c + 2] == 'S') {
            if (lastRow == r && (lastCol == c || lastCol == c + 1 || lastCol == c + 2)) {
              return makeSOS(r, c, r, c + 1, r, c + 2, 0, currentPlayer);
            }
          }
          // vertical
          if (r + 2 < size && board[r + 1][c] == 'O' && board[r + 2][c] == 'S') {
            if (lastCol == c && (lastRow == r || lastRow == r + 1 || lastRow == r + 2)) {
              return makeSOS(r, c, r + 1, c, r + 2, c, 1, currentPlayer);	
            } 
          }
          // diagonal down-right
          if (r + 2 < size && c + 2 < size 
              && board[r + 1][c + 1] == 'O' && board[r + 2][c + 2] == 'S') {
            if ((lastRow == r && lastCol == c) || (lastRow == r + 1 && lastCol == c + 1) || (lastRow == r + 2 && lastCol == c + 2)) {
              return makeSOS(r, c, r + 1, c + 1, r + 2, c + 2, 2, currentPlayer);  
            }
          }
          // diagonal down-left
          if (r + 2 < size && c - 2 >= 0 
              && board[r + 1][c - 1] == 'O' && board[r + 2][c - 2] == 'S') {
            if ((lastRow == r && lastCol == c) || (lastRow == r + 1 && lastCol == c - 1) || (lastRow == r + 2 && lastCol == c - 2)) {
              return makeSOS(r, c, r + 1, c - 1, r + 2, c - 2, 3, currentPlayer);  
            }
          }
        }
      }
    }
    return null; // no SOS found
  }

  /**
   * contains information about a singular SOS.  
   */
  public static class SOSInfo {
    public int r1;
    public int c1;
    public int r2;
    public int c2;
    public int r3;
    public int c3;
    public int direction; // 0=horizontal, 1=vertical, 2=diag down-right, 3=diag down-left
    public int player; // 1=blue, 2=red
  }

  /**
   * creates an SOSInfo object that represents an SOS sequence.
   *
   * @param r1 row of first cell.
   * @param c1 column of first cell.
   * @param r2 row of second cell.
   * @param c2 column of second cell.
   * @param r3 row of third cell.
   * @param c3 column of third cell.
   * @param direction direction of the SOS.
   * @param player player who owns the SOS
   * @return SOSInfo object containing SOS details.
   */
  protected SOSInfo makeSOS(int r1, int c1, int r2, int c2, int r3, int c3, int direction, int player) {
    SOSInfo info = new SOSInfo();
    info.r1 = r1;
    info.c1 = c1;
    info.r2 = r2; 
    info.c2 = c2;
    info.r3 = r3; 
    info.c3 = c3;
    info.direction = direction;
    info.player = player;
    return info;
  }

  public boolean isGameOver() {
    return gameOver;
  }

}
