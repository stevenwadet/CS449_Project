package SOSGame.sprint4.test_code;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import SOSGame.sprint4.production_code.SimpleGame;
import SOSGame.sprint4.production_code.Game.MoveResult;



public class SimpleGameTest {
  private SimpleGame game;
  
  @Before
  public void setUp() {
    game = new SimpleGame(3);
  }
  
  @Test
  public void testInitialBoardIsEmpty() {
    for (int r = 0; r < game.getSize(); r++) {
      for (int c = 0; c < game.getSize(); c++) {
        assertEquals('\0', game.getCell(r, c));
      }
    }
  }
  
  @Test
  public void testValidMove() {
    MoveResult result = game.makeMove(0, 0, 'S');
    assertTrue(result.moveMade);
    assertEquals('S', game.getCell(0, 0));
  }
  
  @Test
  public void testInvalidMoveSameSpot() {
    MoveResult first = game.makeMove(1, 1, 'O');
    assertTrue(first.moveMade);
    MoveResult second = game.makeMove(1, 1, 'S');
    assertFalse(second.moveMade);
  }
  
  @Test
  public void testDetectSOS() {
    MoveResult r1 = game.makeMove(0, 0, 'S');
    MoveResult r2 = game.makeMove(0, 1, 'O');
    MoveResult r3 = game.makeMove(0, 2, 'S');
    
    assertNotNull("After forming SOS, getLastSOS() should be non-null", game.getLastSOS());
    assertTrue("SimpleGame should be game over after SOS", game.checkGameOver());
    assertNotNull("Winner should not be null", game.getWinner());
  }

  @Test
  public void testGameOverCondition() {
    for (int r = 0; r < game.getSize(); r++) {
      for (int c = 0; c < game.getSize(); c++) {
        game.makeMove(r, c, 'S');
      }
    }
    assertTrue(game.isGameOver());
  }
  
  @Test
  public void testGameEndsInDraw() {
    // Fill board with letters so no SOS is formed
    game.makeMove(0, 0, 'S');
    game.makeMove(0, 1, 'S');
    game.makeMove(0, 2, 'S');
    game.makeMove(1, 0, 'S');
    game.makeMove(1, 1, 'S');
    game.makeMove(1, 2, 'S');
    game.makeMove(2, 0, 'S');
    game.makeMove(2, 1, 'S');
    game.makeMove(2, 2, 'S');

    assertTrue("Board should be full, game over", game.isGameOver());
    assertEquals("It's a Tie!", game.getWinner());
  }
  
  
}
  
  
