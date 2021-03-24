package ServerTicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.Color;

public class Client extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private PrintWriter socketOut;
    private Socket palinSocket;
    private BufferedReader stdIn;
    private BufferedReader socketIn;

    private JTextArea box;
    private JPanel buttonsPanel;
    private JPanel inputPanel;
    private JPanel game;
    private JScrollPane scroll;
    
    private JLabel labelXType;
    private JLabel labelOType;
    private JLabel labelXUser;
    private JLabel labelOUser;
    
    private JTextField nameXType;
    private JTextField nameOType;
    private JTextField userXInput;
    private JTextField userOInput;
    
    private JButton [][] buttons;
    private JButton EnterButton;
    private JButton EnterButton1;


    /**
     * Create Client
     * @param serverName
     * @param portNumber
     * @param s
     * Set up buttons and panels for GUI for each Client
     */
    public Client(String serverName, int portNumber, String s) {
        super(s);
        Container c = getContentPane();
        //set GUI dimensions
        setSize(600, 400);
        setLayout(new BorderLayout());
        
        //program stops when GUI is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        box = new JTextArea("");
        box.setEditable(false);
        box.setBorder(BorderFactory.createTitledBorder("Display"));
        
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2));
        c.add("Center", buttonsPanel);

        //initalize 3X3 grid of buttons
        game = new JPanel(new GridLayout(3, 3));
        buttonsPanel.add(game);

        //setting up the 3X3 grid of buttons
        buttons = new JButton[3][3];
        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                buttons[col][row] = new JButton(" ");
                buttons[col][row].setEnabled(false);
                game.add(buttons[col][row]);
            }
        }
        //Button Pannel
        scroll = new JScrollPane(box);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        buttonsPanel.add(scroll);

        //South Pannel
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 5));
        c.add("South", inputPanel);

        //Labels
        labelXType = new JLabel();
        labelXType.setText("Player Symbol:");
        inputPanel.add(labelXType);

        nameXType = new JTextField("X");
        nameXType.setEditable(false);
        inputPanel.add(nameXType);

        //X Player Icon
        labelXUser = new JLabel();
        labelXUser.setText("    Player X Name:");
        inputPanel.add(labelXUser);

        //X Player field
        userXInput = new JTextField();
        inputPanel.add(userXInput);

        //X Player Button
        EnterButton = new JButton("Enter");
        inputPanel.add(EnterButton);

        //O Player Icon
        labelOType = new JLabel();
        labelOType.setText("Player Symbol:");
        inputPanel.add(labelOType);

        //O Player Button
        nameOType = new JTextField("O");
        nameOType.setEditable(false);
        inputPanel.add(nameOType);

        //O Player name field
        labelOUser = new JLabel();
        labelOUser.setText("    Player O Name:");
        inputPanel.add(labelOUser);

        //O Player Name input
        userOInput = new JTextField();
        inputPanel.add(userOInput);

        //O player enter button
        EnterButton1 = new JButton("Enter");
        inputPanel.add(EnterButton1);

        //Registers an ActionListener to each button on the board
        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                int thisRow = row;
                int thisCol = col;
                buttons[col][row].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            //User clicks position and it is sent through socket
                            socketOut.println(thisCol +" "+ thisRow);
                            //Stops user from pressing the same button
                            buttons[thisCol][thisRow].setEnabled(false);
                            
                            //change button boarder colour
                            //buttons[finalI][finalJ].setBorder(BorderFactory.createLineBorder(Color.GREEN));
                            
                            //change button background Colour
                            buttons[thisCol][thisRow].setBackground(Color.GREEN);
                    }
                });
            }
        }

        //XPlayer button action listener
        EnterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!userXInput.getText().trim().isEmpty()) {
                    userXInput.setEditable(false);
                    socketOut.println(userXInput.getText());
                }
                else {
                    box.append("\nPlayer 1(X) name: \n");
                }
            }
        });

        //OPlayer button action listener
        EnterButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!userOInput.getText().trim().isEmpty()) {
                    userOInput.setEditable(false);
                    socketOut.println(userOInput.getText());
                }
                else {
                    box.append("Player 2(O) name: \n");
                }
            }
        });
        //create sockets
        try {
            palinSocket = new Socket(serverName, portNumber);
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            socketIn = new BufferedReader(new InputStreamReader(palinSocket.getInputStream()));
            socketOut = new PrintWriter(palinSocket.getOutputStream(), true);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


  /**
   * ALL buttons off
   * when a player is waiting his turn
   * or at the end of the game
   */
    private void turnOffButtons() {
        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                buttons[col][row].setEnabled(false);
            }
        }
    }
  /**
   * ALL buttons on
   * when it is the players turn to make a move
   */
    private void turnOnButtons(){
        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
            	if(buttons[col][row].getText().equals(" ")) {
            		buttons[col][row].setEnabled(true);
                }
            }
        }
    }
    
    /**
     * Get players move
     * or end game
     * or make player wait
     */
    public void communicate() {
        String line = "";
        String response = "";

        while (!line.equals("QUIT")) {
            try {
                //Read from socket
                response = socketIn.readLine();

                if (response.equals("This is player X.")) {
                    userOInput.setEditable(false);
                }
                if (response.equals("This is player O.")) {
                    userXInput.setEditable(false);
                }
                //Set X name
                if (response.startsWith("PlayerX")) {
                    String[] name = response.split("\\s+");
                    userXInput.setText(name[1]);
                }
                //Set O name
                if (response.startsWith("PlayerO")) {
                    String[] name = response.split("\\s+");
                    userOInput.setText(name[1]);
                }
                //Show move to opponent
                if (response.startsWith("MARKING")) {
                	String[] coordinate = response.split("\\s+");
                	buttons[Integer.parseInt(coordinate[1])][Integer.parseInt(coordinate[2])].setText(coordinate[3]);
                	
                	//change boarder colour
                	//buttons[Integer.parseInt(coordinate[1])][Integer.parseInt(coordinate[2])].setBorder(BorderFactory.createLineBorder(Color.RED));
                	
                	//change button fill colour for opponent moves
                	buttons[Integer.parseInt(coordinate[1])][Integer.parseInt(coordinate[2])].setBackground(Color.RED);
                }
                
                //buttons turn back on when its your turn
                if (response.startsWith("It is your turn")) {
                    turnOnButtons();
                 }
                
                //buttons turn off when its not your turn
                if (response.startsWith("Waiting for")) {
                   turnOffButtons();
                }

                //buttons no longer do anything when the game is over
                if (response.startsWith("Game over!")) {
                    box.append(response);
                    turnOffButtons();
                    break;
                }
                //buttons no longer do anything when TIE
                if (response.equals("TIE!!!")) {
                    box.append(response);
                    turnOffButtons();
                    break;
                }
                // If the other player has left
                if (response.equals("Player has left")) {
                    box.append("The other player has left.");
                    break;
                }
                else {
                    final String end = response + "\n";
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                        	if(!end.startsWith("MARKING") && !end.startsWith("PlayerX ")
                                    && !end.startsWith("PlayerO ")) {
                                box.append(end);
                            }
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //close sockets at end of game
        try {
            stdIn.close();
            socketIn.close();
            socketOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * static main
     * @param args
     */
    public static void main(String[] args) {
        Client aClient = new Client ("localhost", 8888, "Tic-Tac-Toe");
        aClient.setVisible(true);
        aClient.communicate();
    }
}
