package SOSGame.sprint1;

import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import java.util.Random;




public class SOSGame implements ActionListener{
	
	private Game game; //reference to game logic
	
	Random random = new Random();
	JFrame frame = new JFrame(); //creating new JFrame
	JPanel title_panel = new JPanel(); //creating title panel
	JPanel button_panel = new JPanel(); //creating button panel
	JPanel bottomPanel = new JPanel(); //creating bottom panel
	JPanel leftPanel = new JPanel(); //creating left panel
	JPanel rightPanel = new JPanel(); //creating right panel
	JPanel boardSizePanel = new JPanel(); //creating panel for board size text and text field
	
	JLabel textfield = new JLabel(); //creating text
	JButton[] buttons; //establishing 9 buttons for board (NEEDS CHANGE)
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
	int currentBoardSize = 3; //default board size
	
	
	SOSGame(Game game){
		this.game = game;
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //NEEDED, app will not close when clicking red X without this line
		frame.setSize(800, 800); //setting size of frame
		frame.setMinimumSize(new Dimension(1100, 800)); //ensures title flash fits
		frame.getContentPane().setBackground(new Color(50,50,50)); //set background color
		frame.setLayout(new BorderLayout());
		
		textfield.setBackground(new Color(25,25,25)); //setting background color for text = black
		textfield.setForeground(new Color(25,255,0)); //setting color of text = green
		textfield.setFont(new Font("Ink Free", Font.BOLD,75)); //setting font for text
		textfield.setHorizontalAlignment(JLabel.CENTER); //centering text
		textfield.setText("SOS Game"); //establishing title
		textfield.setOpaque(true);
		
		title_panel.setLayout(new BorderLayout()); //establishing our title panel
		title_panel.setBounds(0,0,800,100); //establishing the bounds for our title panel
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
		
		//adding buttons and check box to our bottom panel
		bottomPanel.add(simpleButton);
		bottomPanel.add(generalButton);
		bottomPanel.add(recordGame);
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
		
		newGameButton.addActionListener(e -> {
			int size;
			try {
	            size = Integer.parseInt(boardSize.getText());//acquiring size from text field
	            if (size < 3 || size > 10 ) size = 3; //ensuring board is at least 3x3 and no larger than 10x10 (user can't enter < 3 or > 10)
	        } catch (NumberFormatException ex) { //if user types something that is not a number
	            size = 3;
	        }
			
			
			boardSize.setText(String.valueOf(size));
			currentBoardSize = size; //setting board size
			
			//create either SimpleGame or GeneralGame
			if (simpleButton.isSelected()) {
				this.game = new SimpleGame(size);
			}
			else {
				this.game = new GeneralGame(size);
			}
			
			
			
			
	        createBoard(currentBoardSize);//create new game board
	        firstTurn(); //call first turn
		});
		
		
		createBoard(currentBoardSize);//create initial game board
		firstTurn(); //call first turn after fully setting up
		frame.setVisible(true); // allowing the frame to be visible
	}
	
	private void createBoard(int size) {
		button_panel.removeAll();//removing all old buttons
		button_panel.setLayout(new GridLayout(size, size, 2, 2));//establishing new grid 
		buttons = new JButton[size * size]; //creating new buttons
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();//create new button
			buttons[i].setFont(new Font("MV Boli", Font.BOLD, 120 / Math.max(size, 3)));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
            button_panel.add(buttons[i]);//add new button to button panel
		}
		
		
		//making sure button is in the frame
		if (button_panel.getParent() == null) {
			frame.add(button_panel, BorderLayout.CENTER);
		}
		
		button_panel.revalidate();//forces panel to re-run layout manager, since it doesn't do it automatically
		button_panel.repaint();//redraw the panel
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {	
	    for (int i = 0; i < buttons.length; i++) {
	        if (e.getSource() == buttons[i] && buttons[i].getText().equals("")) {

	            String letter = sButton.isSelected() ? "S" : "O"; 
	            int row = i / currentBoardSize;
	            int col = i % currentBoardSize;
	            
	            
	            //who's turn is it?
	            boolean currentPlayer = game.isPlayer1Turn();
	            
	            // Make the move using Game.makeMove (handles placement and turn)
	            Game.MoveResult result = game.makeMove(row, col, letter.charAt(0));

	            if (result.moveMade) { 
	                // Update GUI
	                buttons[i].setText(letter);
	                buttons[i].setForeground(currentPlayer ? Color.BLUE : Color.RED);

	                // Update score if GeneralGame
	                if (game instanceof GeneralGame) {
	                    ((GeneralGame) game).updateScore(row, col);
	                }

	                // Update turn text or show winner
	                if (result.gameOver) {
	                    textfield.setText(result.winner);
	                    for (JButton btn : buttons) btn.setEnabled(false);
	                } else {
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
	   new SOSGame(new SimpleGame(3)) ;// create an instance to show the GUI
	}

}
