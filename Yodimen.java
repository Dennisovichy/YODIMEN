//I think that this will be the client-side
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class Yodimen extends JFrame{
 GamePanel game= new GamePanel();
  
  public Yodimen() {
    super("YODIMEN");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(game);
    pack();
    setVisible(true);
  }
    
  public static void main(String[] arguments) {
    new Yodimen();  
  }
}

class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener{
 public static final int INTRO=0, GAME=1, END=2;
 private int screen = INTRO;

 YodiClient client;
 private boolean []keys;
 long counter = 0;
 javax.swing.Timer timer;
 Image back;
 Image frog_icon;

 Scanner freeman = new Scanner(System.in);
 
 public GamePanel(){
  
  back = new ImageIcon("background.png").getImage();
 
  keys = new boolean[KeyEvent.KEY_LAST+1]; 
  //127.0.0.1 local host
  String address = freeman.nextLine();
  int port = freeman.nextInt();
  System.out.println(address);
 
  client = new YodiClient(address, port);
  
  setPreferredSize(new Dimension(800, 780));
  setFocusable(true);
  requestFocus();
  addKeyListener(this);
  addMouseListener(this);
  timer = new javax.swing.Timer(10, this);
  timer.start();
 }

 public void move(){
  if(screen == INTRO){
   
  }
  else if(screen == GAME){
   
  }

 }
 
 @Override
 public void actionPerformed(ActionEvent e){
  counter += 1;
  //System.out.println(Arrays.toString(keys));
  if(keys[KeyEvent.VK_LEFT]){
    System.out.println(Arrays.toString(keys));
  }
  client.sendInfoToServer(keys, counter);
  move(); 
  repaint(); 

 }
 
 @Override
 public void keyReleased(KeyEvent ke){
  int key = ke.getKeyCode();
  keys[key] = false;
 } 
 
 @Override
 public void keyPressed(KeyEvent ke){
  //System.out.println("press");
  //client.sendInfoToServer(keys);
  int key = ke.getKeyCode();
  System.out.println("Press");
  keys[key] = true;
 }
 
 @Override
 public void keyTyped(KeyEvent ke){}
 @Override
 public void mouseClicked(MouseEvent e){}

 @Override
 public void mouseEntered(MouseEvent e){}

 @Override
 public void mouseExited(MouseEvent e){}

 @Override
 public void mousePressed(MouseEvent e){
  if(screen == INTRO){
   screen = GAME;
  } 
 }

 @Override
 public void mouseReleased(MouseEvent e){}

 @Override
 public void paint(Graphics g){
  if(screen == INTRO){
   g.setColor(new Color(255,255,255));
   g.fillRect(0,0,getWidth(), getHeight());     
  }
  else if(screen == GAME){
   // The last parameter is an ImageObserver. Back when images were not loaded
   // right away you would specify what object would be notified when it was loaded.
   // We are not doing that, so null will always be fine.
   
  }
    }
}

//class responsible for communication to server. Should not exist when not in a match
class YodiClient{
  // initialize socket and input output streams
    private Socket socket = null;
    private ByteArrayOutputStream bos;
    private DataInputStream input = null;
    private ObjectOutputStream out = null;
 
    // constructor to put ip address and port
    public YodiClient(String address, int port)
    {
        // establish a connection
        try {
            socket = new Socket(address, port);
            socket.setPerformancePreferences(0, 1, 0);
            socket.setTcpNoDelay(true);
            System.out.println("Connected");
 
            // takes input from terminal
            input = new DataInputStream(System.in);
 
            // sends output to the socket
            
            out = new ObjectOutputStream(socket.getOutputStream());
  
        }
        catch (UnknownHostException u) {
            System.out.println(u);
            return;
        }
        catch (IOException i) {
            System.out.println(i);
            return;
        }
      
 
        
                //out.writeUTF(line);
           
        
 
        // close the connection
        /*try {
            input.close();
            out.close();
            socket.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }*/
    }

    public void sendInfoToServer(boolean[] inputs, long counter){
      boolean[] temp = inputs.clone();
      InputPacket send = new InputPacket(temp, counter);
      //InputPacket send = new InputPacket(inputs);
      try{
        out.writeObject(send);
        
      }
      catch(IOException e){
        System.out.println(e);
      }
    }
}



