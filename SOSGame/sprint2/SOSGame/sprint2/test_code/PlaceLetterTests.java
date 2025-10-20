package SOSGame.sprint2.test_code;

import SOSGame.sprint2.production_code.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceLetterTests {

  @Test
  void testPlaceLetterOnEmptySquare() {
    runPlaceLetterTest(new SOSGame(new SimpleGame(3)));
    runPlaceLetterTest(new SOSGame(new GeneralGame(3)));
  }
  
  private void runPlaceLetterTest(SOSGame gui) {
    gui.newGameButton.doClick(); 
    gui.sLetterButton.setSelected(true);
    int index = 0;
    boolean initialTurn = gui.getCurrentGame().isPlayer1Turn();
    gui.buttons[index].doClick();

    
    assertEquals("S", gui.buttons[index].getText());
	
    // Confirm turn has changed (unless SOS formed, which we assume did not happen here)
    boolean newTurn = gui.getCurrentGame().isPlayer1Turn();
    assertNotEquals(initialTurn, newTurn); // If player 1 moved, now player 2's turn
  }

  @Test
  void testPreventInvalidMove() {
    runPreventInvalidMoveTest(new SOSGame(new SimpleGame(3)));
    runPreventInvalidMoveTest(new SOSGame(new GeneralGame(3)));
  }

  private void runPreventInvalidMoveTest(SOSGame gui) {
    gui.newGameButton.doClick();

    // First move
    gui.sLetterButton.setSelected(true);
    int index = 0;
    gui.buttons[index].doClick();
    assertEquals("S", gui.buttons[index].getText());

    // Attempt invalid move in the same cell
    gui.oLetterButton.setSelected(true);
    gui.buttons[index].doClick();
    assertEquals("S", gui.buttons[index].getText()); // Cell should not change
  }

}
