import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class SOSGame implements ActionListener{
	
	Random random = new Random();
	JFrame frame = new JFrame(); //creating new JFrame
	JPanel title_panel = new JPanel(); //creating title panel
	JPanel button_panel = new JPanel(); //creating button panel
	JPanel bottomPanel = new JPanel(); //creating bottom panel
	JPanel leftPanel = new JPanel(); //creating left panel
	JLabel textfield = new JLabel(); //creating text
	JButton[] buttons = new JButton[9]; //establishing 9 buttons for board
	JRadioButton simpleButton = new JRadioButton ("Simple Game"); //creating button for simple game
	JRadioButton generalButton = new JRadioButton ("General Game"); //creating button for general game
	JRadioButton sButton = new JRadioButton("S"); //creating radio button for S letter
	JRadioButton oButton = new JRadioButton("O"); //creating radio button for O letter
	JCheckBox recordGame = new JCheckBox("Record Game"); //creating check box for S letter
	ButtonGroup gameGroup = new ButtonGroup(); //creating new button group for game choice buttons
	ButtonGroup choiceGroup = new ButtonGroup(); //creating a new button group for S or O choice buttons
	boolean player1_turn; //establishing player turns
	
	
	SOSGame(){
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //NEEDED, app will not close when clicking red X without this line
		frame.setSize(800, 800); //setting size of frame
		frame.getContentPane().setBackground(new Color(50,50,50)); //set background color
		frame.setLayout(new BorderLayout());
		frame.setVisible(true); // allowing the frame to be visible
		
		textfield.setBackground(new Color(25,25,25)); //setting background color for text = black
		textfield.setForeground(new Color(25,255,0)); //setting color of text = green
		textfield.setFont(new Font("Ink Free", Font.BOLD,75)); //setting font for text
		textfield.setHorizontalAlignment(JLabel.CENTER); //centering text
		textfield.setText("SOS Game"); //establishing title
		textfield.setOpaque(true);
		
		title_panel.setLayout(new BorderLayout()); //establishing our title panel
		title_panel.setBounds(0,0,800,100); //establishing the bounds for our title panel
		
		button_panel.setLayout(new GridLayout(3,3)); //establishing our button panel to be a grid that is 3x3
		button_panel.setBackground(new Color(150,150,150));
		
		//adding our s and o buttons to our choice button group, setting S as default choice
		choiceGroup.add(sButton);
		choiceGroup.add(oButton);
		sButton.setSelected(true);
		
		//adding our left panel with our simple or general game buttons
		leftPanel.setLayout(new GridLayout(2,1,10,10));
		leftPanel.add(sButton);
		leftPanel.add(oButton);
		
		
		//adding radio buttons to our game button group
		gameGroup.add(simpleButton);
		simpleButton.setSelected(true);
		gameGroup.add(generalButton);
		
		//adding buttons and check box to our bottom panel
		bottomPanel.add(simpleButton);
		bottomPanel.add(generalButton);
		bottomPanel.add(recordGame);
		
		
		for (int i=0;i<9;i++) {
			buttons[i] = new JButton(); //create new button and assign it to i-th position in buttons array
			button_panel.add(buttons[i]); //add button to the panel that holds the grid
			buttons[i].setFont(new Font("MV Boli", Font.BOLD, 120)); //set font for buttons
			buttons[i].setFocusable(false); //removes the focus border that sometimes appears
			buttons[i].addActionListener(this); //adds ActionListener to button, allows game implementation
		}
		
		
		title_panel.add(textfield); //adding text to title panel
		frame.add(title_panel,BorderLayout.NORTH); //setting position of title panel
		frame.add(button_panel); //setting position of button panel
		frame.add(bottomPanel, BorderLayout.SOUTH); //setting position of bottom panel
		frame.add(leftPanel, BorderLayout.WEST); //setting position of left panel
		
		firstTurn();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    for (int i = 0; i < buttons.length; i++) {
	        if (e.getSource() == buttons[i]) {
	            
	        	// Only allow placing on an empty button
	            if (buttons[i].getText().equals("")) { 
	                
	                
	                String letter = sButton.isSelected() ? "S" : "O"; // Determine which letter is selected

	                // Set the text and color depending on the current player
	                if (player1_turn) {
	                    buttons[i].setForeground(new Color(0, 0, 255)); // Blue player
	                    buttons[i].setText(letter);
	                    player1_turn = false; //set turn to red player
	                    textfield.setText("Red Player's Turn"); //change text to reflect the change in turn
	                } else {
	                    buttons[i].setForeground(new Color(255, 0, 0)); // Red player
	                    buttons[i].setText(letter);
	                    player1_turn = true; //set turn to blue player
	                    textfield.setText("Blue Player's Turn"); //change text to reflect change in turn
	                }

	                /*
	                 * check(); 
	                 * ^ this function will be implemented to check for SOS
	                 */
	                
	                
	            }
	        }
	    }
	}

	
	public void firstTurn() {
		
		//the following try catch blocks allow us to flash the game title, then have the text be replaced by text of whoever's turn it is
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//the following if,else randomly selects a player for the first turn
		if(random.nextInt(2)==0) {
			player1_turn=true;
			textfield.setText("Blue Player's Turn");
		}
		else {
			player1_turn=false;
			textfield.setText("Red Player's Turn");
			
		}
	}
	

	public static void main(String[] args) {
	   SOSGame game =  new SOSGame(); // create an instance to show the GUI
	}

}
