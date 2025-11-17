package SOSGame.sprint4.test_code;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import SOSGame.sprint2.production_code.Game;
import SOSGame.sprint2.test_code.TestGame;

public class BoardSizeTests {
  
  @Test
  public void testBoardSizeInitialization() {
    int size = 5;
    Game game = new TestGame(size);

    assertEquals(size, game.getSize(), "Board size should match the input size.");
    assertNotNull(game.getBoard(), "Board should be initialized.");
    assertEquals(size, game.getBoard().length, "Board row count should match the size.");
    assertEquals(size, game.getBoard()[0].length, "Board column count should match the size.");
  }

  @Test
  public void testMinimumValidBoardSize() {
    int size = 3; // Assuming 3 is the minimum allowed
    Game game = new TestGame(size);
    assertEquals(size, game.getSize());
  }

  @Test
  public void testInvalidBoardSizeTooSmall() {
    int size = 2; // Too small
    assertThrows(IllegalArgumentException.class, () -> {
      new TestGame(size);
    });
  }

  @Test
  public void testInvalidBoardSizeNegative() {
    int size = -5;
    assertThrows(IllegalArgumentException.class, () -> {
      new TestGame(size);
    });
  }

  @Test
  public void testInvalidBoardSizeTooLarge() {
    int size = 100; // Assuming a max size limit
    assertThrows(IllegalArgumentException.class, () -> {
      new TestGame(size);
    });
  }
}
