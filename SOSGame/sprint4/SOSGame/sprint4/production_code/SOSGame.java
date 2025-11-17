package SOSGame.sprint4.production_code;

import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import SOSGame.sprint4.production_code.Game;
import SOSGame.sprint4.production_code.GeneralGame;
import SOSGame.sprint4.production_code.SOSGame;
import SOSGame.sprint4.production_code.SimpleGame;

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

  public JLabel textfield = new JLabel();
  JLabel blueScoreLabel = new JLabel("Blue: 0"); 
  JLabel redScoreLabel = new JLabel("Red: 0"); 
  public JButton[] buttons; // buttons vector for board
  public JButton newGameButton = new JButton("New Game");
  JLabel boardSizeText = new JLabel("Board Size (3-10):");
  public JTextField boardSize = new JTextField(5); // creating variable board size text field
  
  public JRadioButton simpleButton = new JRadioButton("Simple Game");
  public JRadioButton generalButton = new JRadioButton("General Game");
  public JRadioButton sLetterButton = new JRadioButton("S");
  public JRadioButton oLetterButton = new JRadioButton("O");
  JCheckBox recordGame = new JCheckBox("Record Game");
  
  JLabel p1Label = new JLabel("Player 1:");
  JLabel p2Label = new JLabel("Player 2:");
  JLabel diffLabel = new JLabel("Difficulty:");
  
  JComboBox<String> p1Type = new JComboBox<>(new String[]{"Human", "Computer"});
  JComboBox<String> p2Type = new JComboBox<>(new String[]{"Human", "Computer"});
  JComboBox<String> diffType = new JComboBox<>(new String[]{"Easy", "Hard"});
  
  ButtonGroup gameGroup = new ButtonGroup(); // button group for game choice buttons
  ButtonGroup choiceGroup = new ButtonGroup(); // button group for S or O choice buttons
  
  LineOverlay lineOverlay1 = new LineOverlay();
  JLayeredPane centerPane = new JLayeredPane(); // center layered pane
  
  private void initializeFrame() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 800);
    frame.setMinimumSize(new Dimension(1100, 800)); // ensures title flash fits
    frame.getContentPane().setBackground(new Color(50, 50, 50)); // set background color
    frame.setLayout(new BorderLayout());
  }
  
  private void setupTitlePanel() {
    textfield.setBackground(new Color(25, 25, 25)); // black
    textfield.setForeground(new Color(25, 255, 0)); // green
    textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
    textfield.setHorizontalAlignment(JLabel.CENTER); 
    textfield.setText("SOS Game");
    textfield.setOpaque(true);
    titlePanel.setLayout(new BorderLayout()); // establishing our title panel
    titlePanel.add(textfield); // adding text to title panel
    frame.add(titlePanel, BorderLayout.NORTH); // setting position of title panel
  }
  
  private void setupLeftPanel() {
    choiceGroup.add(sLetterButton);
    choiceGroup.add(oLetterButton);
    sLetterButton.setSelected(true);
    leftPanel.setLayout(new GridLayout(2, 1, 10, 10));
    leftPanel.add(sLetterButton);
    leftPanel.add(oLetterButton);
    frame.add(leftPanel, BorderLayout.WEST); // setting position of left panel
  }
  
  private void setupBottomPanel() {
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
  }
  
  private void setupRightPanel() {
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
    
    addRightPanelOption(p1Label, p1Type);
    addRightPanelOption(p2Label, p2Type);
    addRightPanelOption(diffLabel, diffType);
    
    frame.add(rightPanel, BorderLayout.EAST); 
    
  }
  
  private void addRightPanelOption(JLabel label, JComboBox<String> combo) {
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    combo.setAlignmentX(Component.CENTER_ALIGNMENT);
    combo.setMaximumSize(new Dimension(130, 25));
    combo.setFont(new Font("SansSerif", Font.PLAIN, 12));
    rightPanel.add(label);
    rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    rightPanel.add(combo);
    rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
  }
  
  private void setupCenterPane() {
    buttonPanel.setOpaque(false);
    lineOverlay1.setOpaque(false);
    
    centerPane.setLayout(new OverlayLayout(centerPane)); // allows absolute positioning
    centerPane.add(lineOverlay1); // top layer (lines)
    centerPane.add(buttonPanel); // bottom layer (buttons)
    frame.add(centerPane, BorderLayout.CENTER); 
  }
  
  private void initializeUi() {
    initializeFrame();
    setupTitlePanel();
    setupLeftPanel();
    setupBottomPanel();
    setupRightPanel();
    setupCenterPane();
    frame.setVisible(true);
  }
  
  
  public SOSGame(Game game) {
    this.game = game;
    initializeUi();
    
    // new game button logic
    newGameButton.addActionListener(e -> startNewGame());
    createBoard(currentBoardSize); // create initial game board
    firstTurn();
    frame.setVisible(true); // allowing the frame to be visible
  }
  
  private void startNewGame() {
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
    
    SwingUtilities.invokeLater(() -> handleComputerTurns());
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
      
    });

  }
  
  private int getButtonIndex(ActionEvent e) {
    for (int i = 0; i < buttons.length; i++) {
      if (e.getSource() == buttons[i]) {
        return i;
      }
    }
    return -1;
  }
  
  private String getSelectedLetter() {
    return sLetterButton.isSelected() ? "S" : "O";
  }
  
  private void updateButton(int index, String letter, boolean currentPlayer) {
    buttons[index].setText(letter);
    buttons[index].setForeground(currentPlayer ? Color.BLUE : Color.RED);
  }
  
  private void drawSOSLines() {
    
    if (game instanceof GeneralGame) {
      GeneralGame gg = (GeneralGame) game;
      List<GeneralGame.SOSInfo> sosList = gg.getLastSOSList();
      if (sosList != null) {
        for (GeneralGame.SOSInfo sos : sosList) {
          Color lineColor = (sos.player == 1) ? Color.BLUE : Color.RED;
          lineOverlay1.addLine(new SOSLine(sos.r1, sos.c1, sos.r3, sos.c3, lineColor));
        }
      }
    } else if (game instanceof SimpleGame) {
      SimpleGame sg = (SimpleGame) game;
      SimpleGame.SOSInfo sos = sg.getLastSOS();
      if (sos != null) {
        Color lineColor = (sos.player == 1) ? Color.BLUE : Color.RED;
        int lastR = sg.getLastMoveRow();
        int lastC = sg.getLastMoveCol();

        // Draw line from first SOS cell → last move → last SOS cell
        lineOverlay1.addLine(new SOSLine(sos.r1, sos.c1, lastR, lastC, lineColor));
        lineOverlay1.addLine(new SOSLine(lastR, lastC, sos.r3, sos.c3, lineColor));
      }
    } 
  }
  
  private void updateScores() {
    if (game instanceof GeneralGame) {
      GeneralGame gg = (GeneralGame) game;
      blueScoreLabel.setText("Blue: " + gg.getBlueScore());
      redScoreLabel.setText("Red: " + gg.getRedScore());
    } else if (game instanceof SimpleGame) {
      SimpleGame sg = (SimpleGame) game;
      SimpleGame.SOSInfo sos = sg.getLastSOS();
      
      int blue = 0;
      int red = 0;
      if (sos != null) {
        blue = (sos.player == 1) ? 1 : 0;
        red = (sos.player == 2) ? 1 : 0;
      }
      
      blueScoreLabel.setText("Blue: " + blue);
      redScoreLabel.setText("Red: " + red);
    }
  }
  
  private void updateTurnOrWinner(Game.MoveResult result) {
    if (result.gameOver) {
      isGameOver = true;
      textfield.setText(result.winner);
    } else {
      textfield.setText(result.nextPlayerIs1 ? "Blue Player's Turn" : "Red Player's Turn");
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // checking for game over
    if (isGameOver) {
      return;
    }

    int buttonIndex = getButtonIndex(e);
    if (buttonIndex == -1 || !buttons[buttonIndex].getText().equals("")) {
      return;
    }
    
    int row = buttonIndex / currentBoardSize;
    int col = buttonIndex % currentBoardSize;
    String letter = getSelectedLetter();
    
    
    boolean currentPlayer = game.isPlayer1Turn();
    Game.MoveResult result = game.makeMove(row, col, letter.charAt(0));
    
    if (!result.moveMade) {
      return;
    }
    
    updateButton(buttonIndex, letter, currentPlayer);
    drawSOSLines();
    updateScores();
    updateTurnOrWinner(result);
    handleComputerTurns();
  }
  
  private void handleComputerTurns() {
    if (isGameOver) {
      return;
    }
    
    boolean isP1Turn = game.isPlayer1Turn();
    boolean isComputerTurn = 
       (isP1Turn && p1Type.getSelectedItem().equals("Computer")) 
       || (!isP1Turn && p2Type.getSelectedItem().equals("Computer"));
  
    if (!isComputerTurn) {
      return;
    }
  
    Timer aiTimer = new Timer(700, e -> {
      if (isGameOver) {
        return;
      }
  
      ComputerPlayer ai = new ComputerPlayer((String) diffType.getSelectedItem());
      int[] move = ai.chooseMove(game);
      int row = move[0];
      int col = move[1];
      char letter = (char) move[2];
  
      boolean currentPlayer = game.isPlayer1Turn();
      Game.MoveResult result = game.makeMove(row, col, letter);
  
      if (result.moveMade) {
        int index = row * currentBoardSize + col;
        updateButton(index, String.valueOf(letter), currentPlayer);
        drawSOSLines();
        updateScores();
        updateTurnOrWinner(result);
      } 
    
      if (!isGameOver) {
        handleComputerTurns();
      }
    
    });
   
    aiTimer.setRepeats(false);
    aiTimer.start();
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
  
  
  public Game getCurrentGame() {
    return game;
  }
  
  public String getSelectedGameMode() {
    if (simpleButton.isSelected()) {
      return "Simple";
    } else if (generalButton.isSelected()) {
      return "General";
    } else {
      return "None";
    }
  }
  
  /**
   * initializes and starts new SOS game.
   *
   * @param args not used.
   */
  public static void main(String[] args) {
    SOSGame sosGame = new SOSGame(new SimpleGame(3)); // default game option
  }

}
