package SOSGame.sprint1;

public class SimpleGame extends Game {
	
	private boolean gameOver = false;
	private boolean winnerIsPlayer1;//true if player1 won, false if player 2 won
	

	public SimpleGame(int size) {
		super(size);
	}
	
	@Override
	public boolean checkGameOver() {
		if (gameOver) return true;
		
		int winningPlayer = hasSOS();
		
		if (winningPlayer != 0) {
			gameOver = true;
			winnerIsPlayer1 = (winningPlayer == 1);
		}
		
		return gameOver;
		
	}
	
	@Override
	public String getWinner() {
		if (!gameOver) {
			return null;
		}
        return winnerIsPlayer1 ? "Blue Player Wins!" : "Red Player Wins!";
	}
	
	
	//returns 0 if no SOS found, 1 if player1 made SOS, 2 if player2 made SOS
	private int hasSOS() {
		//check horizontal, vertical, diagonal for "SOS"
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				if (board[r][c] == 'S') {
					int player = ownerBoard[r][c]; //who placed this S?
					if(player == 0) { //if cell empty, skip SOS check
						continue;
					}
					
					// horizontal
					//ownerBoard checks are to make sure SOS is made by same player
                    if (c + 2 < size && board[r][c+1] == 'O' && board[r][c+2] == 'S' &&
                        ownerBoard[r][c+1] == player && ownerBoard[r][c+2] == player) return player;

                    // vertical
                    if (r + 2 < size && board[r+1][c] == 'O' && board[r+2][c] == 'S' &&
                        ownerBoard[r+1][c] == player && ownerBoard[r+2][c] == player) return player;

                    // diagonal down-right
                    if (r + 2 < size && c + 2 < size &&
                        board[r+1][c+1] == 'O' && board[r+2][c+2] == 'S' &&
                        ownerBoard[r+1][c+1] == player && ownerBoard[r+2][c+2] == player) return player;

                    // diagonal down-left
                    if (r + 2 < size && c - 2 >= 0 &&
                        board[r+1][c-1] == 'O' && board[r+2][c-2] == 'S' &&
                        ownerBoard[r+1][c-1] == player && ownerBoard[r+2][c-2] == player) return player;
				}
			}
		}
		return 0;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
}
