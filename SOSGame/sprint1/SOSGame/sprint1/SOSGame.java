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

/**
 * sos game class.
 */
public class SOSGame implements ActionListener {

  private Game game; // reference to game logic
  private boolean isGameOver = false; // flag for game over
  int currentBoardSize = 3; // default board size

  Random random = new Random();
  JFrame frame = new JFrame(); 
  JPanel titlePanel = new JPanel(); 
  JPanel buttonPanel = new JPanel(); 
  JPanel bottomPanel = new JPanel(); 
  JPanel leftPanel = new JPanel(); 
  JPanel rightPanel = new JPanel(); 
  JPanel boardSizePanel = new JPanel(); 

  JLabel textfield = new JLabel();
  JLabel blueScoreLabel = new JLabel("Blue: 0"); 
  JLabel redScoreLabel = new JLabel("Red: 0"); 
  JButton[] buttons; // buttons vector for board
  JButton newGameButton = new JButton("New Game");
  JLabel boardSizeText = new JLabel("Board Size (3-10):");
  JTextField boardSize = new JTextField(5); // creating variable board size text field
  
  JRadioButton simpleButton = new JRadioButton("Simple Game");
  JRadioButton generalButton = new JRadioButton("General Game");
  JRadioButton sletterButton = new JRadioButton("S");
  JRadioButton oletterButton = new JRadioButton("O");
  JCheckBox recordGame = new JCheckBox("Record Game");

  ButtonGroup gameGroup = new ButtonGroup(); // button group for game choice buttons
  ButtonGroup choiceGroup = new ButtonGroup(); // button group for S or O choice buttons

  LineOverlay lineOverlay1 = new LineOverlay();
  JLayeredPane centerPane = new JLayeredPane(); // center layered pane

  SOSGame(Game game) {
    this.game = game;
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 800);
    frame.setMinimumSize(new Dimension(1100, 800)); // ensures title flash fits
    frame.getContentPane().setBackground(new Color(50, 50, 50)); // set background color
    frame.setLayout(new BorderLayout());
    // title panel
    textfield.setBackground(new Color(25, 25, 25)); // black
    textfield.setForeground(new Color(25, 255, 0)); // green
    textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
    textfield.setHorizontalAlignment(JLabel.CENTER); 
    textfield.setText("SOS Game");
    textfield.setOpaque(true);
    
    titlePanel.setLayout(new BorderLayout()); // establishing our title panel
    titlePanel.add(textfield); // adding text to title panel
    frame.add(titlePanel, BorderLayout.NORTH); // setting position of title panel
    
    // adding our s and o buttons to our choice button group, setting S as default choice
    choiceGroup.add(sletterButton);
    choiceGroup.add(oletterButton);
    sletterButton.setSelected(true);
    
    // adding our left panel with our simple or general game buttons
    leftPanel.setLayout(new GridLayout(2, 1, 10, 10));
    leftPanel.add(sletterButton);
    leftPanel.add(oletterButton);
    frame.add(leftPanel, BorderLayout.WEST); // setting position of left panel
    
    // adding radio buttons to our game button group
    gameGroup.add(simpleButton);
    simpleButton.setSelected(true);
    gameGroup.add(generalButton);
    
    // adding buttons, check box, and score board to our bottom panel
    bottomPanel.add(simpleButton);
    bottomPanel.add(generalButton);
    bottomPanel.add(recordGame);
    bottomPanel.add(blueScoreLabel);
    bottomPanel.add(redScoreLabel);
    blueScoreLabel.setForeground(Color.BLUE);
    redScoreLabel.setForeground(Color.RED);
    frame.add(bottomPanel, BorderLayout.SOUTH); // setting position of bottom panel
    
    // adding new game and board size changer on right panel
    boardSizeText.setAlignmentX(Component.CENTER_ALIGNMENT); // center text
    boardSizeText.setBorder(new EmptyBorder(0, 10, 0, 10)); // padding around text
    
    boardSize.setAlignmentX(Component.CENTER_ALIGNMENT);
    boardSize.setHorizontalAlignment(JTextField.CENTER);
    boardSize.setPreferredSize(new Dimension(110, 25)); // suggests default size
    boardSize.setMaximumSize(new Dimension(110, 25)); // text field max size
    boardSize.setFont(new Font("SansSerif", Font.PLAIN, 12));
    boardSize.setText(String.valueOf(currentBoardSize)); // initial value of text field
    
    boardSizePanel.setLayout(new BoxLayout(boardSizePanel, BoxLayout.Y_AXIS));
    boardSizePanel.add(boardSizeText); 
    boardSizePanel.add(Box.createRigidArea(new Dimension(0, 5))); // gap in space
    boardSizePanel.add(boardSize); 
    
    newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.add(newGameButton); 
    rightPanel.add(Box.createRigidArea(new Dimension(0, 10))); // add vertical gap
    rightPanel.add(boardSizePanel); // add panel containing board size label and text field
    frame.add(rightPanel, BorderLayout.EAST); 
    
    buttonPanel.setOpaque(false);
    lineOverlay1.setOpaque(false);
    
    centerPane.setLayout(new OverlayLayout(centerPane)); // allows absolute positioning
    centerPane.add(lineOverlay1); // top layer (lines)
    centerPane.add(buttonPanel); // bottom layer (buttons)
    frame.add(centerPane, BorderLayout.CENTER);
    
    // new game button logic
    newGameButton.addActionListener(e -> {
      int size;
      try {
        size = Integer.parseInt(boardSize.getText()); // acquiring size from text field
        if (size < 3 || size > 10) {
          size = 3; // ensuring board is at least 3x3 and no larger than 10x10
        }
      } catch (NumberFormatException ex) { // if user types something that is not a number
        size = 3;
      }
      
      boardSize.setText(String.valueOf(size)); // update board size field
      currentBoardSize = size; 
      isGameOver = false; // reset game over flag
      
      
      if (simpleButton.isSelected()) {
        this.game = new SimpleGame(size);
      } else {
        this.game = new GeneralGame(size);
      }

      // Reset scores
      blueScoreLabel.setText("Blue: 0");
      redScoreLabel.setText("Red: 0");
      
      createBoard(currentBoardSize); 
      firstTurn();
    });

    createBoard(currentBoardSize); // create initial game board
    firstTurn();
    frame.setVisible(true); // allowing the frame to be visible
  }
  
  class SOSButton extends JButton {
    SOSButton() {
      super();
      setOpaque(true);
      setBorder(BorderFactory.createLineBorder(Color.GRAY));
      setFont(new Font("MV Boli", Font.BOLD, 40));
    }
  }

  // structure to store SOS line info
  class SOSLine {
    int startRow;
    int startCol;
    int endRow;
    int endCol;
    Color color;

    SOSLine(int r1, int c1, int r3, int c3, Color color) {
      this.startRow = r1;
      this.startCol = c1;
      this.endRow = r3;
      this.endCol = c3;
      this.color = color;
    }
  }

  // overlay panel to draw lines over buttons
  class LineOverlay extends JPanel {
    private static final long serialVersionUID = 1L;
    List<SOSLine> lines = new ArrayList<>();
    
    public void addLine(SOSLine line) {
      lines.add(line);
      repaint(); // repaint immediately after adding a line
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

        // get buttons
        JButton startButton = buttons[line.startRow * currentBoardSize + line.startCol];
        JButton endButton = buttons[line.endRow * currentBoardSize + line.endCol];
        
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
    buttonPanel.removeAll(); // removing all old buttons
    buttonPanel.setLayout(new GridLayout(size, size, 2, 2)); // establishing new grid
    buttons = new SOSButton[size * size]; // creating new buttons

    for (int i = 0; i < buttons.length; i++) {
      buttons[i] = new SOSButton(); // create new button
      buttons[i].setFont(new Font("MV Boli", Font.BOLD, 200 / Math.max(size, 3)));
      buttons[i].setFocusable(false);
      buttons[i].addActionListener(this);
      buttonPanel.add(buttons[i]); // add new button to button panel
    }

    buttonPanel.revalidate();
    buttonPanel.repaint();
    
    // ensure LineOverlay covers buttons after layout
    SwingUtilities.invokeLater(() -> {
      lineOverlay1.setBounds(buttonPanel.getBounds());
      lineOverlay1.clearLines();
      lineOverlay1.revalidate();
      lineOverlay1.repaint();
    });

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // checking for game over
    if (isGameOver) {
      return;
    }

    for (int i = 0; i < buttons.length; i++) { // loop through each button
      if (e.getSource() == buttons[i] && buttons[i].getText().equals("")) {
        String letter = sletterButton.isSelected() ? "S" : "O";
        int row = i / currentBoardSize;
        int col = i % currentBoardSize;
        
        // who's turn is it?
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
            if (sos != null) { // if we have an SOS
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
          if (result.gameOver) { // if game over
            isGameOver = true;
            textfield.setText(result.winner); // set text to congratulate winner
          } else { // if game not over, set turn text
            textfield.setText(result.nextPlayerIs1 ? "Blue Player's Turn" : "Red Player's Turn");
          }
          
        }
      }
    }
  }

  /**
   * flash sos game for 0.5s, then switch to first player's turn.
   */
  public void firstTurn() {
    String gameType = simpleButton.isSelected() ? "Simple Game" : "General Game";
    textfield.setText("SOS Game - " + gameType);
    
    Timer t = new Timer(1000, e -> {
      textfield.setText(game.isPlayer1Turn() ? "Blue Player's Turn" : "Red Player's Turn");
    });
    t.setRepeats(false);
    t.start();
  }
  
  /**
   * initializes and starts new SOS game.
   *
   * @param args not used.
   */
  public static void main(String[] args) {
    new SOSGame(new SimpleGame(3)); 
  }

}
