package SOSGame.sprint1;



public class SimpleGame extends Game {
	
	private boolean gameOver = false;
	private boolean winnerIsPlayer1;//true if player1 won, false if player 2 won
	private SOSInfo lastSOS; //store last SOS for GUI drawing
	
	public SOSInfo getLastSOS() {return lastSOS;}

	public SimpleGame(int size) {
		super(size);
	}
	
	@Override
	public boolean checkGameOver() {
		if (gameOver) return true;
		
		SOSInfo sos = findSOS(); //find any SOS on board
		if (sos != null) {
			gameOver = true;
			winnerIsPlayer1 = (sos.player == 1);
			lastSOS = sos; //save it to draw line in GUI
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
	private SOSInfo findSOS() {
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				if (board[r][c] == 'S') {
					int player = ownerBoard[r][c]; //who placed this S?
					if(player == 0) { //if cell empty, skip SOS check
						continue;
					}
					
					// horizontal
	                if (c + 2 < size && board[r][c+1] == 'O' && board[r][c+2] == 'S' &&
	                    ownerBoard[r][c+1] == player && ownerBoard[r][c+2] == player) {
	                    return makeSOS(r, c, r, c+1, r, c+2, 0, player);
	                }

	                // vertical
	                if (r + 2 < size && board[r+1][c] == 'O' && board[r+2][c] == 'S' &&
	                    ownerBoard[r+1][c] == player && ownerBoard[r+2][c] == player) {
	                    return makeSOS(r, c, r+1, c, r+2, c, 1, player);
	                }

	                // diagonal down-right
	                if (r + 2 < size && c + 2 < size &&
	                    board[r+1][c+1] == 'O' && board[r+2][c+2] == 'S' &&
	                    ownerBoard[r+1][c+1] == player && ownerBoard[r+2][c+2] == player) {
	                    return makeSOS(r, c, r+1, c+1, r+2, c+2, 2, player);
	                }

	                // diagonal down-left
	                if (r + 2 < size && c - 2 >= 0 &&
	                    board[r+1][c-1] == 'O' && board[r+2][c-2] == 'S' &&
	                    ownerBoard[r+1][c-1] == player && ownerBoard[r+2][c-2] == player) {
	                    return makeSOS(r, c, r+1, c-1, r+2, c-2, 3, player);
	                }
				}
			}
		}
		return null; //no SOS found
	}
	
	public static class SOSInfo {
		public int r1, c1;
		public int r2, c2;
		public int r3, c3;
		public int direction; // 0=horizontal, 1=vertical, 2=diag down-right, 3=diag down-left
		public int player; // 1=blue, 2=red
		
	}
	
	protected SOSInfo makeSOS(int r1,int c1,int r2,int c2,int r3,int c3,int direction,int player) {
        SOSInfo info = new SOSInfo();
        info.r1=r1; info.c1=c1;
        info.r2=r2; info.c2=c2;
        info.r3=r3; info.c3=c3;
        info.direction=direction;
        info.player=player;
        return info;
    }
	
	public boolean isGameOver() {
		return gameOver;
	}
	
}
