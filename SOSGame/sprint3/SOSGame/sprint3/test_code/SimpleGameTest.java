package SOSGame.sprint3.test_code;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import SOSGame.sprint3.production_code.SimpleGame;
import SOSGame.sprint3.production_code.Game.MoveResult;



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
    assertTrue(r1.moveMade);
    
    MoveResult r2 = game.makeMove(0, 1, 'O');
    assertTrue(r2.moveMade);
    
    MoveResult r3 = game.makeMove(0, 2, 'S');
    assertTrue(r3.moveMade);
    
    assertNotNull("After forming SOS, getLastSOS() should be non-null", game.getLastSOS());
    
    assertTrue("SimpleGame should be game over after SOS", game.checkGameOver());
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
  
}
  
  
