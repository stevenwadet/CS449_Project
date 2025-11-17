package SOSGame.sprint4.production_code;

import java.util.Random;

/**
 * represents a computer-controlled player.
 * currently only supports easy mode (random move and random letter).
 * 
 */
public class ComputerPlayer {
  private final String difficulty;
  private final Random random = new Random();
  
  public ComputerPlayer(String difficulty) {
    this.difficulty = difficulty;
  }
  
  public int[] chooseMove(Game game) {
    if ("Easy".equalsIgnoreCase(difficulty)) {
      return chooseRandomMove(game);	
    } else {
      return chooseRandomMove(game);
    }
  }
  
  private int[] chooseRandomMove(Game game) {
    int size = game.getSize();
    char[][] board = game.getBoard();
    
    while (true) {
      int row = random.nextInt(size);
      int col = random.nextInt(size);
      if (board[row][col] == '\0') {
        char letter = random.nextBoolean() ? 'S' : 'O';
        return new int[] {row, col, letter};
      }
    }
  }
  
  public String getDifficulty() {
    return difficulty;
  }
  
}
