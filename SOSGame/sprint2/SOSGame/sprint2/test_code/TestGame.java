package SOSGame.sprint2.test_code;

import SOSGame.sprint2.production_code.Game;

/**
 * dummy class for our tests.
 */
public class TestGame extends Game {
	
  public TestGame(int size) { 
    super(size); 
  }
  
  @Override
  public boolean checkGameOver() { 
    return false; 
  }
    
  @Override
  public String getWinner() { 
    return null; 
  }
}

