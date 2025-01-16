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
 public static final int INTRO=0, GAME=1, CHOOSE=2;
 private int screen = INTRO;
 Menu menu = new Menu();

 YodiClient client;
 SpamSocket spamming;
 DisplayPacket display_info = null;
 private boolean []keys;
 long counter = 0;
 javax.swing.Timer timer;
 Image back;
 Image frog_icon;

 int mousex;
 int mousey;

 Scanner freeman = new Scanner(System.in);
 
 public GamePanel(){
  
  back = new ImageIcon("background.png").getImage();
 
  keys = new boolean[KeyEvent.KEY_LAST+1]; 
  //127.0.0.1 local host
  
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
  
  Point mouse = MouseInfo.getPointerInfo().getLocation();
  Point offset = getLocationOnScreen();
  mousex = mouse.x - offset.x;
  mousey = mouse.y - offset.y;
  //System.out.println(mousex + ", " + mousey);

  //System.out.println(Arrays.toString(keys));
  if(keys[KeyEvent.VK_LEFT]){
    //System.out.println(Arrays.toString(keys));
  }
  //client.sendInfoToServer(keys, counter);
  move(); 

  if(screen == GAME || screen == CHOOSE){
    display_info = client.getDisplay();
  }
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
  //System.out.println("Press");
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
   menu.checkScreen(mousex, mousey);
   if(menu.current_screen.game){
    String address = "127.0.0.1";//JOptionPane.showInputDialog("Enter server address");
    int port = Integer.parseInt(JOptionPane.showInputDialog("Enter port number"));
    try {
        Socket test = new Socket(address, port);
        client = new YodiClient(test);
        spamming = new SpamSocket(client, keys);
        client.sendInfoToServer(keys, counter);
        //client.sendInfoToServer(true, false);
        spamming.start();
        screen = CHOOSE;
    } catch (Exception er) {
      System.out.println("caught");
      menu.goBackScreen();
    }
    
   }
  }
  if(screen == CHOOSE){
    menu.checkScreen(mousex, mousey);
   if(menu.current_screen == menu.red_team){
    client.sendInfoToServer(true, true);
    screen = GAME;
   }
   if(menu.current_screen == menu.blue_team){
    client.sendInfoToServer(true, false);
    screen = GAME;
   }
  } 
 }

 @Override
 public void mouseReleased(MouseEvent e){}

 @Override
 public void paint(Graphics g){
  if(screen == INTRO || screen == CHOOSE){
     g.setColor(Color.WHITE);
     g.fillRect(0,0, 1500, 1500);
     menu.drawScreen(g);
  }
  else if(screen == GAME){
    g.setColor(new Color(255,255,255));
   g.fillRect(0,0,getWidth(), getHeight());
   if(display_info != null){
    display_info.game_map.draw(g, display_info.player_x, display_info.player_y);
    int count = 0;
    for(Player play: display_info.players){
      if(play != null){
        count += 1;
        play.draw(g, display_info.player_x, display_info.player_y);
      }
    }
    //System.out.println(count);
   }  
  }
    }
}

//class responsible for communication to server. Should not exist when not in a match
class YodiClient{
  // initialize socket and input output streams
    private Socket socket = null;
    private ByteArrayOutputStream bos;
    private ObjectInputStream input = null;
    private ObjectOutputStream out = null;

    private boolean send_turn = true;

    private DisplayPacket display = null;
 
    // constructor to put ip address and port
    public YodiClient(String address, int port)
    {
        // establish a connection
        try {
            socket = new Socket(address, port);
            
            socket.setPerformancePreferences(0, 1, 0);
            socket.setTcpNoDelay(true);
            System.out.println("Connected");
 
 
            // sends output to the socket
            
            out = new ObjectOutputStream(socket.getOutputStream());


            // reads display info from server
            input = new ObjectInputStream(socket.getInputStream());
  
        }
        catch (UnknownHostException u) {
            System.out.println(u);
            return;
        }
        catch (IOException i) {
            System.out.println(i);
            return;
        }
 
    }

    public YodiClient(Socket sock)
    {
        // establish a connection
        try {
            socket = sock;
            
            socket.setPerformancePreferences(0, 1, 0);
            socket.setTcpNoDelay(true);
            System.out.println("Connected");
 
 
            // sends output to the socket
            
            out = new ObjectOutputStream(socket.getOutputStream());


            // reads display info from server
            input = new ObjectInputStream(socket.getInputStream());
  
        }
        catch (UnknownHostException u) {
            System.out.println(u);
            return;
        }
        catch (IOException i) {
            System.out.println(i);
            return;
        }
 
    }

    public void sendInfoToServer(boolean[] inputs, long counter){
      if(send_turn){
        //System.out.println("Ran");
        boolean[] temp = inputs.clone();
        InputPacket send = new InputPacket(temp, counter);
        //InputPacket send = new InputPacket(inputs);
        try{
          out.writeObject(send);
          out.flush();
          //send_turn = false;
        }
        catch(IOException e){
          System.out.println(e);
        }
      }
    }

    public void sendInfoToServer(boolean temp, boolean counter){
      if(send_turn){
        //System.out.println("Ran");
        boolean wbgat = temp;
        InputPacket send = new InputPacket(wbgat, counter);
        //InputPacket send = new InputPacket(inputs);
        try{
          out.writeObject(send);
          out.flush();
          //send_turn = false;
        }
        catch(IOException e){
          System.out.println(e);
        }
      }
    }

    public void readServerInfo(){
      try {
        display = (DisplayPacket)input.readObject();
        if(display != null){
          send_turn = true;
        }
      } 
      catch (Exception e) {
        System.out.println(e);
        System.out.println("SEIU");
      }
    }

    public DisplayPacket getDisplay(){
      return display;
    }
}

class SpamSocket extends Thread{
  YodiClient ref;
  boolean[] keys;
  public long counter = 0;

  public SpamSocket(YodiClient in, boolean[] keys){
    this.ref = in;
    this.keys = keys;
  }

  @Override
  public void run(){
    while (true) { 
        ref.readServerInfo();
        ref.sendInfoToServer(this.keys, counter);
        //ref.sendInfoToServer(true, false);
    }
  }
}



