package SOSGame.sprint1;

public class GeneralGame extends Game {
	private int blueScore = 0;
	private int redScore = 0;
	
	public GeneralGame (int size) {
		super(size);
	}
	
	@Override
	public boolean checkGameOver() {
		//game ends when board is full
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				if (board[r][c] == '\0') { //empty character
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public String getWinner() {
		if (blueScore > redScore) {
			return "Blue Player Wins!";
		}
		if (redScore > blueScore) {
			return "Red Player Wins!";
		}
		return "It's a Tie!";
	}
	
	public void updateScore(int row, int col) {
		//check if new placement created SOS
		int newPoints = countSOS(row, col);
		if (player1Turn) {
			blueScore += newPoints;
		}
		else {
			redScore += newPoints;
		}
	}
	
	
	private int countSOS(int row, int col) {
		int points = 0;
		char letter = board[row][col];
		
		// 8 directions
		int[][] dirs = {
				{1,0}, {-1,0}, //vertical
				{0,1}, {0,-1}, //horizontal
				{1,1}, {-1,-1}, //down-right, up-left
				{1,-1}, {-1,1} // down-left, up-right
		};
		
		for (int[] d : dirs) { //each element 'd' is an array {rowChange, columnChange}
			int dr = d[0]; //dr = how rows change, accessed from first index in array
			int dc = d[1]; //dc = how columns change, accessed from second index in array
			
			
			//current letter is 'O' = check for "SOS" with O in middle
			if (letter == 'O') {
				int r1 = row - dr;
				int c1 = col - dc;
				int r2 = row + dr;
				int c2 = col + dc;
				if (inBounds(r1, c1) && inBounds(r2, c2)) {
					if (board[r1][c1] == 'S' && board[r2][c2] == 'S') {
						points++;
					}
				}
			}
			
			//current letter is 'S' = check for SOS
			if (letter == 'S') {
				int r1 = row +dr;
				int c1 = col + dc;
				int r2 = row + 2*dr;
				int c2 = col + 2*dc;
				
				if (inBounds(r1, c1) && inBounds(r2, c2)) {
					if (board[r1][c1] == 'O' && board[r2][c2] == 'S') {
						points++;
					}
				}
			}
			
			
		}
		return points;	
	}
	
	private boolean inBounds(int r, int c) {
		return r>= 0 && r < size && c >= 0 && c < size;
	}
	

	
	

}
