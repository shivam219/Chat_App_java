import java.io.*;/// for Buffer or Printwrite 
import java.net.*;// for Socket
import java.awt.*;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

// for netkworking
// Server
public class Alexa extends JFrame {

    private static final long serialVersionUID = 1L;
    ServerSocket server; // it only for Server
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Alexa");
    private JTextArea messegeArea = new JTextArea();
    JTextField text = new JTextField(20);
    // field.setText("Java Code Geeks");

    // // create a line border with the specified color and width
    // Border border = BorderFactory.createLineBorder(Color.BLUE, 5);

    // // set the border of this component
    // field.setBorder(border);

    // // add textfield to frame
    // add(field);
    private JTextField messegeInput = new JTextField();
    public Font font = new Font("Roboto", Font.PLAIN, 20);

    // Constructor
    public Alexa() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Alexa is online");
            System.out.println("waiting...");
            socket = server.accept();// Gettin refferenc of client socket
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // get byte data convet- character
            out = new PrintWriter(socket.getOutputStream());// goted data to print

            createGui(); // Create Gui
            handleEvents(); // Handle Event
            startReading(); // function calling
        } catch (Exception e) {
            System.out.println("Connection Close"); // e.printStackTrace();
        }
    }

    private void createGui() {
        this.setTitle("Messenger");
        this.setMinimumSize(new Dimension(500, 400));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // coponent text Coding
        heading.setFont(font);
        messegeArea.setFont(font);
        messegeInput.setFont(font);
        heading.setIcon(new ImageIcon("chat_app.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));// top, left, bottom, right
        messegeInput.setHorizontalAlignment(SwingConstants.CENTER);
        // Border task
        this.setLayout(new BorderLayout());
        // // Adding element
        messegeArea.setEditable(false);
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messegeArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messegeInput, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void handleEvents() {
        messegeInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("Key :" + e.getKeyCode());
                if (e.getKeyChar() == 10) {
                    String textToSend = messegeInput.getText();
                    messegeArea.append("Me :" + textToSend + "\n"); // 1
                    out.println(textToSend); // print Server window
                    out.flush(); // print Server window
                    messegeInput.setText("");
                    messegeInput.requestFocus();

                }
            }

        });
    }

    public void startReading() { // Getting character from bufferReader from Client

        Runnable r1 = () -> { // thread -- Reading data from bufferReader from Client
            System.out.println("Siri online");// Reader started...
            try {
                while (true) { // infinite time read == while(true)
                    String msg;

                    msg = br.readLine();// assigning msg to == string

                    if (msg.equals("exit")) {

                        // System.out.println("Client Close the chat");
                        JOptionPane.showMessageDialog(this, "Siri Close The Chat");
                        messegeInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Client : " + msg); // printing msg in Server Side
                    messegeArea.append("Siri  :" + msg + "\n");
                    text.setText(msg);
                    Border border = BorderFactory.createLineBorder(Color.BLUE, 3);
                    text.setBorder(border);
                }

            } catch (Exception e) {

                System.out.println("Connection Close"); // 1
                // e.printStackTrace(); // print error in terminal

            }
        };
        new Thread(r1).start();// starting Thread

    }

    public static void main(String[] args) {

        new Alexa();
    }

}