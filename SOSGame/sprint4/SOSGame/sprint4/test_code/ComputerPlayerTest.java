package SOSGame.sprint4.test_code;

import SOSGame.sprint4.production_code.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComputerPlayerTest {
  
  @Test
  public void testAiChoosesEmptyCell() {
    Game game = new SimpleGame(5);
    ComputerPlayer ai = new ComputerPlayer("Easy");
    
    int[] move = ai.chooseMove(game);
    int row = move[0];
    int col = move[1];
    
    // cell must be empty
    assertEquals('\0', game.getCell(row, col));
  }
  
  @Test
  public void testAiChoosesWithinBounds() {
    Game game = new GeneralGame(7);
    ComputerPlayer ai = new ComputerPlayer("Easy");
    
    int[] move = ai.chooseMove(game);
    
    assertTrue(move[0] >= 0 && move[0] < game.getSize());
    assertTrue(move[1] >= 0 && move[1] < game.getSize());
  }
  
  @Test
  public void testAiChoosesValidLetter() {
    Game game = new SimpleGame(5);
    ComputerPlayer ai = new ComputerPlayer("Easy");
    
    int[] move = ai.chooseMove(game);
    char letter = (char) move[2];
    
    assertTrue(letter == 'S' || letter == 'O');
  }
  
  @Test
  public void testAiChoosesLegalMoveOnPartiallyFilledBoard() {
    Game game = new SimpleGame(4);
    ComputerPlayer ai = new ComputerPlayer("Easy");
    
    game.placeLetter(0, 0, 'S');
    game.placeLetter(1, 1, 'S');
    game.placeLetter(2, 2, 'S');
    
    int[] move = ai.chooseMove(game);
    int row = move[0];
    int col = move[1];
    
    assertEquals('\0', game.getCell(row, col));
  }
  
  @Test 
  public void testTwoAisDontCollide() {
    Game game = new GeneralGame(5);
    ComputerPlayer ai1 = new ComputerPlayer("Easy");
    ComputerPlayer ai2 = new ComputerPlayer("Easy");
    
    int[] move1 = ai1.chooseMove(game);
    int[] move2;
    
    game.placeLetter(move1[0], move1[1], (char) move1[2]);
    
    move2 = ai2.chooseMove(game);
    
    assertFalse(move1[0] == move2[0] && move1[1] == move2[1]); 
    
  }
}
