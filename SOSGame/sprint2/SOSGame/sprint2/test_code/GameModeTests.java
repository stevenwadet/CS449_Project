package SOSGame.sprint2.test_code;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import SOSGame.sprint2.production_code.*;

public class GameModeTests {

	
  @Test
  void testSelectValidGameModeSimple() {
    SOSGame gui = new SOSGame(new SimpleGame(3));
    // simulate selecting board size and choosing Simple mode
    gui.boardSize.setText("5");
    gui.simpleButton.doClick();  // user clicks “Simple Game”
    gui.newGameButton.doClick();     // user clicks “New Game”

    // confirm configuration updated
    assertTrue(gui.getCurrentGame() instanceof SimpleGame);
    assertEquals("Simple", gui.getSelectedGameMode());
  }

  @Test
  void testSelectValidGameModeGeneral() {
    SOSGame gui = new SOSGame(new SimpleGame(3));

    gui.boardSize.setText("5");
    gui.generalButton.doClick();
    gui.newGameButton.doClick();

    assertTrue(gui.getCurrentGame() instanceof GeneralGame);
    assertEquals("General", gui.getSelectedGameMode());
  }
  
  @Test
  void testDefaultGameModeSimple() {
    SOSGame gui = new SOSGame(new SimpleGame(3));

    // simulate app load without selecting mode
    gui.boardSize.setText("5");
    gui.newGameButton.doClick();

    // confirm defaults
    assertTrue(gui.getCurrentGame() instanceof SimpleGame);
    assertTrue(gui.simpleButton.isSelected());
  }
  
  @Test
  void testStarNewGameWithChosenSizeAndMode() {
    SOSGame gui = new SOSGame(new SimpleGame(3));
    gui.boardSize.setText("6");
    gui.generalButton.setSelected(true);
    
    gui.newGameButton.doClick();
    
    assertTrue(gui.getCurrentGame() instanceof GeneralGame,
            "Expected current game to be a GeneralGame");
    assertEquals(6, gui.getCurrentGame().getSize(),
            "Expected board size to update to 6");
    assertEquals(36, gui.buttons.length,
            "Expected 36 buttons for a 6×6 board");
    assertTrue(gui.textfield.getText().contains("General Game"),
            "Expected the title to display the selected game mode");
  }

}
