package SOSGame.sprint1;

import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import java.util.Random;


public class SOSGame implements ActionListener{
	
	private Game game; //reference to game logic
	private boolean isGameOver = false; //flag for game over
	int currentBoardSize = 3; //default board size
	
	Random random = new Random();
	JFrame frame = new JFrame(); //creating new JFrame
	JPanel title_panel = new JPanel(); //creating title panel
	JPanel button_panel = new JPanel(); //creating button panel
	JPanel bottomPanel = new JPanel(); //creating bottom panel
	JPanel leftPanel = new JPanel(); //creating left panel
	JPanel rightPanel = new JPanel(); //creating right panel
	JPanel boardSizePanel = new JPanel(); //creating panel for board size text and text field
	
	JLabel textfield = new JLabel(); //creating text
	JLabel blueScoreLabel = new JLabel("Blue: 0"); //blue score label
	JLabel redScoreLabel = new JLabel("Red: 0"); //red score label
	JButton[] buttons; //establishing buttons for board
	JButton newGameButton = new JButton("New Game"); //creating the new game button
	JLabel boardSizeText = new JLabel("Board Size (3-10):");
	JTextField boardSize = new JTextField(5); //creating variable board size text field
	
	
	JRadioButton simpleButton = new JRadioButton ("Simple Game"); //creating button for simple game
	JRadioButton generalButton = new JRadioButton ("General Game"); //creating button for general game
	JRadioButton sButton = new JRadioButton("S"); //creating radio button for S letter
	JRadioButton oButton = new JRadioButton("O"); //creating radio button for O letter
	JCheckBox recordGame = new JCheckBox("Record Game"); //creating check box for S letter
	
	ButtonGroup gameGroup = new ButtonGroup(); //creating new button group for game choice buttons
	ButtonGroup choiceGroup = new ButtonGroup(); //creating a new button group for S or O choice buttons
	
	LineOverlay lineOverlay1 = new LineOverlay();
	JLayeredPane centerPane = new JLayeredPane(); //center layered pane
	
	SOSGame(Game game){
		this.game = game;
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //NEEDED, app will not close when clicking red X without this line
		frame.setSize(800, 800); //setting size of frame
		frame.setMinimumSize(new Dimension(1100, 800)); //ensures title flash fits
		frame.getContentPane().setBackground(new Color(50,50,50)); //set background color
		frame.setLayout(new BorderLayout());
		
		//title panel
		textfield.setBackground(new Color(25,25,25)); //setting background color for text = black
		textfield.setForeground(new Color(25,255,0)); //setting color of text = green
		textfield.setFont(new Font("Ink Free", Font.BOLD,75)); //setting font for text
		textfield.setHorizontalAlignment(JLabel.CENTER); //centering text
		textfield.setText("SOS Game"); //establishing title
		textfield.setOpaque(true);
		
		title_panel.setLayout(new BorderLayout()); //establishing our title panel
		title_panel.add(textfield); //adding text to title panel
		frame.add(title_panel,BorderLayout.NORTH); //setting position of title panel
		
		
		//adding our s and o buttons to our choice button group, setting S as default choice
		choiceGroup.add(sButton);
		choiceGroup.add(oButton);
		sButton.setSelected(true);
		
		//adding our left panel with our simple or general game buttons
		leftPanel.setLayout(new GridLayout(2,1,10,10));
		leftPanel.add(sButton);
		leftPanel.add(oButton);
		frame.add(leftPanel, BorderLayout.WEST); //setting position of left panel
		
		
		//adding radio buttons to our game button group
		gameGroup.add(simpleButton);
		simpleButton.setSelected(true);
		gameGroup.add(generalButton);
		
		//adding buttons, check box, and score board to our bottom panel
		bottomPanel.add(simpleButton);
		bottomPanel.add(generalButton);
		bottomPanel.add(recordGame);
		bottomPanel.add(blueScoreLabel);
		bottomPanel.add(redScoreLabel);
		blueScoreLabel.setForeground(Color.BLUE);
		redScoreLabel.setForeground(Color.RED);
		frame.add(bottomPanel, BorderLayout.SOUTH); //setting position of bottom panel
		
		//adding new game and board size changer on right panel
		boardSizePanel.setLayout(new BoxLayout(boardSizePanel, BoxLayout.Y_AXIS));
		boardSizeText.setAlignmentX(Component.CENTER_ALIGNMENT); //center text
		boardSizeText.setBorder(new EmptyBorder(0, 10, 0, 10)); //add padding around text for nicer looks
		
		boardSize.setAlignmentX(Component.CENTER_ALIGNMENT);//centering text field in panel
		boardSize.setHorizontalAlignment(JTextField.CENTER);
		boardSize.setPreferredSize(new Dimension(110, 25)); //suggests default size
        boardSize.setMaximumSize(new Dimension(110, 25)); //text field max size
        boardSize.setFont(new Font("SansSerif", Font.PLAIN, 12));//text field font
        boardSize.setText(String.valueOf(currentBoardSize));//sets initial value of text field to current board size
        
		boardSizePanel.add(boardSizeText);//adding boardSizeText to board size panel
		boardSizePanel.add(Box.createRigidArea(new Dimension(0, 5)));//adds a gap to separate label and text field
		boardSizePanel.add(boardSize);//add text field below label
		
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT); //center new game button horizontally in rightPanel
		rightPanel.add(newGameButton);// add new game button to panel
		rightPanel.add(Box.createRigidArea(new Dimension(0, 10))); // add vertical gap
		rightPanel.add(boardSizePanel); //add panel containing board size label and text field
		frame.add(rightPanel, BorderLayout.EAST);//setting position of right panel
		
		centerPane.setLayout(new OverlayLayout(centerPane)); // allows absolute positioning
        button_panel.setOpaque(false);
        lineOverlay1.setOpaque(false);
        
        centerPane.add(lineOverlay1);   // top layer (lines)
        centerPane.add(button_panel);   // bottom layer (buttons)
        frame.add(centerPane, BorderLayout.CENTER);
        
        
        // new game button logic
		newGameButton.addActionListener(e -> {
			int size;
			try {
	            size = Integer.parseInt(boardSize.getText());//acquiring size from text field
	            if (size < 3 || size > 10 ) size = 3; //ensuring board is at least 3x3 and no larger than 10x10 (user can't enter < 3 or > 10)
	        } catch (NumberFormatException ex) { //if user types something that is not a number
	            size = 3;
	        }
			
			boardSize.setText(String.valueOf(size));//updates board size field
			currentBoardSize = size; //setting board size
			isGameOver = false; //reset game over flag
			
			//create either SimpleGame or GeneralGame
			if (simpleButton.isSelected()) {
				this.game = new SimpleGame(size);
			}
			else {
				this.game = new GeneralGame(size);
			}
			
			// Reset scores
			blueScoreLabel.setText("Blue: 0"); 
			redScoreLabel.setText("Red: 0");
			
	        createBoard(currentBoardSize);//create new game board
	        firstTurn(); //call first turn
		});
		
		createBoard(currentBoardSize);//create initial game board
		firstTurn(); //call first turn after fully setting up
		frame.setVisible(true); // allowing the frame to be visible
	}
	
	class SOSButton extends JButton {
		SOSButton () {
			super();
			setOpaque(true);
			setBorder(BorderFactory.createLineBorder(Color.GRAY));
			setFont(new Font("MV Boli", Font.BOLD, 40));
		}
	}
	
	//structure to store SOS line info
	class SOSLine {
	    int startRow, startCol, endRow, endCol;
	    Color color;

	    SOSLine(int r1, int c1, int r3, int c3, Color color) {
	        this.startRow = r1;
	        this.startCol = c1;
	        this.endRow = r3;
	        this.endCol = c3;
	        this.color = color;
	    }
	}
	
	//overlay panel to draw lines over buttons
	class LineOverlay extends JPanel {
	    private static final long serialVersionUID = 1L;
	    List<SOSLine> lines = new ArrayList<>();

	    public void addLine(SOSLine line) {
	        lines.add(line);
	        repaint(); //repaint immediately after adding a line
	    }

	    public void clearLines() {
	        lines.clear();
	        repaint();
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setStroke(new BasicStroke(4));

	        for (SOSLine line : lines) {
	            g2.setColor(line.color);
	            
	            
	            //get buttons
	            JButton startButton = buttons[line.startRow * currentBoardSize + line.startCol];
	            JButton endButton   = buttons[line.endRow * currentBoardSize + line.endCol];
	            
	            // Calculate centers of buttons
	            int startX = startButton.getX() + startButton.getWidth() / 2;
	            int startY = startButton.getY() + startButton.getHeight() / 2;
	            int endX = endButton.getX() + endButton.getWidth() / 2;
	            int endY = endButton.getY() + endButton.getHeight() / 2;

	            g2.drawLine(startX, startY, endX, endY);
	        }
	    }
	}

	private void createBoard(int size) {
		button_panel.removeAll();//removing all old buttons
		button_panel.setLayout(new GridLayout(size, size, 2, 2));//establishing new grid 
		buttons = new SOSButton[size * size]; //creating new buttons
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new SOSButton();//create new button
			buttons[i].setFont(new Font("MV Boli", Font.BOLD, 120 / Math.max(size, 3)));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
            button_panel.add(buttons[i]);//add new button to button panel
		}
		
		button_panel.revalidate();
	    button_panel.repaint();

	    
	    // ensure LineOverlay covers buttons after layout
	    SwingUtilities.invokeLater(() -> {
	    	lineOverlay1.setBounds(button_panel.getBounds());
		    lineOverlay1.clearLines();
		    lineOverlay1.revalidate();
		    lineOverlay1.repaint();
	    });
	    
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {	
		//checking for game over
		if (isGameOver) {
			return;
		}
		
	    for (int i = 0; i < buttons.length; i++) { //loop through each button
	        if (e.getSource() == buttons[i] && buttons[i].getText().equals("")) { 

	            String letter = sButton.isSelected() ? "S" : "O"; 
	            int row = i / currentBoardSize;
	            int col = i % currentBoardSize;
	            
	            //who's turn is it?
	            boolean currentPlayer = game.isPlayer1Turn();
	            
	            // Make the move using Game.makeMove (handles placement and turn)
	            Game.MoveResult result = game.makeMove(row, col, letter.charAt(0));

	            if (result.moveMade) { 
	                // Update GUI to appropriate letter and color
	                buttons[i].setText(letter);
	                buttons[i].setForeground(currentPlayer ? Color.BLUE : Color.RED);

	                // Update score and draw SOS lines if GeneralGame
	                if (game instanceof GeneralGame) {
	                    GeneralGame gg = (GeneralGame) game;
	                    List<GeneralGame.SOSInfo> sosList = gg.getLastSOSList();
	                    if (sosList != null) {
	                    	for (GeneralGame.SOSInfo sos : sosList) {
	                    		Color lineColor = (sos.player == 1) ? Color.BLUE : Color.RED;
	                    		lineOverlay1.addLine(new SOSLine(sos.r1, sos.c1, sos.r3, sos.c3, lineColor));
	                    	}
	                    }
	                    
	                    // Update score labels
	                    blueScoreLabel.setText("Blue: " + gg.getBlueScore());
	                    redScoreLabel.setText("Red: " + gg.getRedScore());
	                }
	                
	             // If this is SimpleGame, draw the SOS line
                    if (game instanceof SimpleGame) {
                        SimpleGame sg = (SimpleGame) game;
                        SimpleGame.SOSInfo sos = sg.getLastSOS();
                        if (sos != null) { //if we have an SOS
                        	Color lineColor = (sos.player == 1) ? Color.BLUE : Color.RED;
                            lineOverlay1.addLine(new SOSLine(sos.r1, sos.c1, sos.r3, sos.c3, lineColor));
                            
                            // update score labels
                            int blue = (sos.player == 1) ? 1 : 0;
                            int red = (sos.player == 2) ? 1 : 0;
                            blueScoreLabel.setText("Blue: " + blue);
                            redScoreLabel.setText("Red: " + red);
                        }
                    }
                    

	                // Update turn text or show winner
	                if (result.gameOver) { //if game over
	                	isGameOver = true;
	                    textfield.setText(result.winner); //set text to congratulate winner
	                } else { //if game not over, set turn text
	                    textfield.setText(result.nextPlayerIs1 ? "Blue Player's Turn" : "Red Player's Turn"); 
	                }
	            }
	        }
	    }
	}
	
	public void firstTurn() {

		// Show "SOS Game" for 0.5s, then switch to player's turn
		//assistance from ChatGPT
		String gameType = simpleButton.isSelected() ? "Simple Game" : "General Game";
        textfield.setText("SOS Game - " + gameType);
        
        Timer t = new Timer(1000, e -> {
        	textfield.setText(game.isPlayer1Turn() ? "Blue Player's Turn" : "Red Player's Turn");
        });
        t.setRepeats(false);
        t.start();
	}
	

	public static void main(String[] args) {
	   new SOSGame(new SimpleGame(3)) ;// create an instance, default settings are a simple game with 3x3 grid
	}

}
