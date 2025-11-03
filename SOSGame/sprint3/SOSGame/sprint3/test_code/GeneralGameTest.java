package SOSGame.sprint3.test_code;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import SOSGame.sprint3.production_code.GeneralGame;

public class GeneralGameTest {
  private GeneralGame game;

  @Before
  public void setUp() {
    game = new GeneralGame(3);
  }

  @Test
  public void testInitialScoresAreZero() {
    assertEquals(0, game.getBlueScore());
    assertEquals(0, game.getRedScore());
  }

  @Test
  public void testScoringSOS() {
    game.makeMove(0, 0, 'S');
    game.makeMove(0, 1, 'O');
    game.makeMove(0, 2, 'S');
    assertTrue(game.getBlueScore() > 0 || game.getRedScore() > 0);
  }

  @Test
  public void testSwitchTurns() {
    boolean currentPlayerIs1 = game.isPlayer1Turn();
    game.makeMove(1, 1, 'S');
    boolean nextPlayerIs1 = game.isPlayer1Turn();
    assertNotEquals(currentPlayerIs1, nextPlayerIs1);
  }

  @Test
  public void testGameOverWhenFull() {
    for (int r = 0; r < game.getSize(); r++) {
      for (int c = 0; c < game.getSize(); c++) {
        game.makeMove(r, c, 'S');
      }
    }
    assertTrue(game.checkGameOver());
  }
  
  @Test
  public void testWinnerDetermination() {
    game.makeMove(0, 0, 'S');
    game.makeMove(0, 0, 'O');
    game.makeMove(0, 0, 'S');
    String winner = game.getWinner();
    assertNotNull(winner);
  }
}
