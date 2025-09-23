package SOSGame.sprint1;

import java.util.ArrayList;
import java.util.List;

public class GeneralGame extends Game {
	private int blueScore = 0;
	private int redScore = 0;
	private List<SOSInfo> lastMoveSOS = new ArrayList<>();
	
	public int getBlueScore() { return blueScore; }
	public int getRedScore() { return redScore; }
	
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
	public boolean extraTurn() {
	    // if lastMoveSOS contains SOSs, player gets another turn
	    return !lastMoveSOS.isEmpty();
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
	
	
	//handles GeneralGame score updates
	public MoveResult makeMove(int row, int col, char letter) {
		MoveResult result = new MoveResult();
        result.moveMade = placeLetter(row, col, letter);

        if (result.moveMade) {
            boolean currentPlayer = isPlayer1Turn();
            updateScore(row, col, currentPlayer); // compute SOSs and update score

            boolean extra = extraTurn(); // now correctly reflects SOSs created by this move
            if (!extra) switchTurn();

            result.gameOver = checkGameOver();
            result.winner = result.gameOver ? getWinner() : null;
            result.nextPlayerIs1 = isPlayer1Turn();
        }

        return result;
	}
	
	public void updateScore(int row, int col, boolean currentPlayer) {
	    lastMoveSOS.clear(); // clear previous move SOSs
	    
	    char letter = board[row][col]; //get letter just placed on board
	    int placedPlayer = currentPlayer ? 1 : 2; //which player placed letter

	    //direction vectors
	    int[][] dirs = {
	        {1,0}, {-1,0}, // vertical
	        {0,1}, {0,-1}, // horizontal
	        {1,1}, {-1,-1}, // diag down-right / up-left
	        {1,-1}, {-1,1}  // diag down-left / up-right
	    };

	    List<SOSInfo> sosFoundThisMove = new ArrayList<>(); //temp list to store all SOS found for this move
	    
	    
	    for (int[] d : dirs) { //loop over each direction
	        int dr = d[0];
	        int dc = d[1];

	        if (letter == 'O') { //case 1: player placed 'O'
	            int r1 = row - dr; // check cell before the 'O' in this direction
	            int c1 = col - dc;
	            int r2 = row + dr; // check cell after the 'O' in this direction
	            int c2 = col + dc;

	            if (inBounds(r1, c1) && inBounds(r2, c2)) { //ensure both positions are inside board
	                if (board[r1][c1] == 'S' && board[r2][c2] == 'S') { //pattern SOS found
	                	// normalize SOSInfo so r1/c1 = "start", r3/c3 = "end"
                        SOSInfo sos = createSOS(r1, c1, row, col, r2, c2, placedPlayer, dr, dc);
                        sosFoundThisMove.add(sos); //add to temp list
	                }
	            }
	        }

	        if (letter == 'S') { //case 2: player placed 'S'
	            int r1 = row + dr; //position one step in this direction
	            int c1 = col + dc;
	            int r2 = row + 2*dr; //position two steps in this direction
	            int c2 = col + 2*dc;

	            if (inBounds(r1, c1) && inBounds(r2, c2)) { //ensure both positions are inside board
	                if (board[r1][c1] == 'O' && board[r2][c2] == 'S') { //SOS found starting at this S
	                	SOSInfo sos = createSOS(row, col, r1, c1, r2, c2, placedPlayer, dr, dc);
                        sosFoundThisMove.add(sos); //add to temp list
	                }
	            }
	        }
	       
	    }
	    
	    //deduplicate identical SOS triples (avoid double counting from opposite directions)
	    List<SOSInfo> uniqueSOS = new ArrayList<>(); //stores deduplicated SOS
	    java.util.Set<String> seen = new java.util.HashSet<>(); //set of SOS keys already counted
	    for (SOSInfo s : sosFoundThisMove) { //check each SOS candidate
	    	int aR = s.r1; //first endpoint row
	    	int aC = s.c1; //first endpoint col
	    	int bR = s.r3; //second endpoint row
	    	int bC = s.c3; //second endpoint col
	    	String key;
	    	
	    	// normalizing order so our key is consistent (smallest coordinate pair first)
	    	if (aR < bR || (aR == bR && aC <= bC)) {
	    		key = aR + "," + aC + ":" + bR + "," + bC + ":" + s.direction;
	    	}
	    	else {
	    		key = bR + "," + bC + ":" + aR + "," + aC + ":" + s.direction;
	    	}
	    	
	    	if (!seen.contains(key)) { //only keep new SOS that hasn't been counted
	    		seen.add(key); //mark key as seen
	    		uniqueSOS.add(s); //add deduplicated SOS
	    	}
	    }
	    //update scores only once per found SOS
	    for (SOSInfo sos: uniqueSOS) {
	    	if (currentPlayer) blueScore++; 
	    	else redScore++;
	    }
	    
	    // store unique SOS list for GUI line drawing
	    lastMoveSOS.addAll(uniqueSOS);
	}
	
	private SOSInfo createSOS(int r1, int c1, int r2, int c2, int r3, int c3, int player, int dr, int dc) {
        int direction = directionFromDelta(dr, dc);

        // reorder for diagonals to make start always "top-left" or "top-right"
        if (direction == 2) { // diag down-right
            if (r1 > r3) {
                int tmpR = r1, tmpC = c1;
                r1 = r3; c1 = c3;
                r3 = tmpR; c3 = tmpC;
            }
        }
        if (direction == 3) { // diag down-left
            if (r1 > r3) {
                int tmpR = r1, tmpC = c1;
                r1 = r3; c1 = c3;
                r3 = tmpR; c3 = tmpC;
            }
        }

        return new SOSInfo(r1, c1, r2, c2, r3, c3, direction, player);
    }
	
	
	// map dr, dc to line direction for drawing
	private int directionFromDelta(int dr, int dc) {
		// horizontal
		if (dr == 0 && dc != 0)  {
			return 0; 
		}
		
		// vertical
        if (dr != 0 && dc == 0)  {
        	return 1; 
        }
        
        // diagonal down-right
        if (dr == dc) {
        	return 2; 
        }
        
        // diagonal down-left
        if (dr == -dc) {
        	return 3; 
        }
        return -1;
	}
	
	public List<SOSInfo> getLastSOSList() {
		return lastMoveSOS;
	}
	
	public class SOSInfo {
		public int r1, c1, r2, c2, r3, c3; // the three cells forming SOS
	    public int direction; // 0=horizontal,1=vertical,2=diagRight,3=diagLeft
	    public int player; // 1=player1, 2=player2
	    
	    public SOSInfo(int r1, int c1, int r2, int c2, int r3, int c3, int direction, int player) {
	        this.r1 = r1;
	        this.c1 = c1;
	        this.r2 = r2;
	        this.c2 = c2;
	        this.r3 = r3;
	        this.c3 = c3;
	        this.direction = direction;
	        this.player = player;
	    }
	}
	
	private boolean inBounds(int r, int c) {
		return r>= 0 && r < size && c >= 0 && c < size;
	}
	
}
