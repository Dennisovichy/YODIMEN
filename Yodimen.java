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

 int centerx;
 int centery;

 int mousex;
 int mousey;
 int mousex_offset = 0;
 int mousey_offset = 0;

 boolean mouse_pressed = false;

 int item_start;
 Item held = null;
 boolean holding_item = false;
 int[] swap_request = {-1, -1};

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
    //System.out.println(held);
  }

 }
 
 @Override
 public void actionPerformed(ActionEvent e){
  counter += 1;
  Point offset = new Point(0,0);
  Point mouse = MouseInfo.getPointerInfo().getLocation();
  
  try{
    offset = getLocationOnScreen();
  }
  catch(IllegalComponentStateException err){
    
  }
  mousex = mouse.x - offset.x;
  mousey = mouse.y - offset.y;

  centerx = getWidth()/2;
  centery = getHeight()/2;
  mousex_offset = centerx - mousex;
  mousey_offset = centery - mousey;
  //System.out.println(mousex + ", " + mousey);

  //System.out.println(Arrays.toString(keys));
  if(keys[KeyEvent.VK_LEFT]){
    //System.out.println(Arrays.toString(keys));
  }
  
  if(spamming != null){
    spamming.make_swapRequest = true;
  }
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
  if(e.getButton() == MouseEvent.BUTTON1){
    if(screen == INTRO){
    menu.checkScreen(mousex, mousey);
    if(menu.current_screen.game){
      String address = "127.0.0.1";//JOptionPane.showInputDialog("Enter server address");
      int port = Integer.parseInt(JOptionPane.showInputDialog("Enter port number"));
      try {
          Socket test = new Socket(address, port);
          client = new YodiClient(test);
          spamming = new SpamSocket(client, keys, this);
          //client.sendInfoToServer(keys, counter, mousex_offset, mousey_offset, mouse_pressed);
          //client.sendInfoToServer(true, false);
          //spamming.start();
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
      spamming.start();
      screen = GAME;
    }
    if(menu.current_screen == menu.blue_team){
      client.sendInfoToServer(true, false);
      spamming.start();
      screen = GAME;
    }
    } 
    if(screen == GAME){
      mouse_pressed = true;
      if(!holding_item){
        if(display_info != null){
          if(display_info.inventory != null){
            held = display_info.inventory.selectItem(centerx, getHeight(), mousex, mousey);
            if(held != null){
              holding_item = true;
              item_start = display_info.inventory.getSlot(centerx, getHeight(), mousex, mousey);
            }
          }
        }
      }
    }
  }
 }

 @Override
 public void mouseReleased(MouseEvent e){
  if(e.getButton() == MouseEvent.BUTTON1){
    if(screen == GAME){
      mouse_pressed = false;
      if(holding_item){
        int end_point = display_info.inventory.getSlot(centerx, getHeight(), mousex, mousey);
        if(end_point != -1){
          swap_request[0] = item_start;
          swap_request[1] = end_point;
          spamming.make_swapRequest = true;
        }
        holding_item = false;
        held = null;
      }
    }
  }
 }

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
    for(Projectile proj : display_info.projectiles){
      proj.draw(g, display_info.player_x, display_info.player_y, centerx, centery);
    }
    display_info.game_map.draw(g, display_info.player_x, display_info.player_y, centerx, centery);
    int count = 0;
    for(Player play: display_info.players){
      if(play != null){
        count += 1;
        play.draw(g, display_info.player_x, display_info.player_y, centerx, centery);
      }
    }
    if(display_info.inventory != null){
      display_info.inventory.draw(g, centerx, getHeight());
      //System.out.println(Arrays.toString(display_info.inventory.hotbar));
    }
    if(holding_item){
      held.draw(g, mousex, mousey);
    }
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

    public void sendInfoToServer(boolean[] inputs, long counter, int mousex_offset, int mousey_offset, boolean mouse_pressed, int[] swap){
      if(send_turn){
        InputPacket send = new InputPacket(inputs.clone(), counter, mousex_offset, mousey_offset, mouse_pressed, swap.clone());
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
        catch(ConcurrentModificationException i){
          System.out.println("pounding");
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
  GamePanel game;
  boolean[] keys;
  public long counter = 0;
  boolean make_swapRequest = false;

  public SpamSocket(YodiClient in, boolean[] keys, GamePanel ref){
    this.ref = in;
    this.keys = keys;
    this.game = ref;
  }

  @Override
  public void run(){
    while (true) { 
        ref.readServerInfo();

        boolean press_mouse = game.mouse_pressed;
        if(game.holding_item){
          press_mouse = false;
        }
        if(game.swap_request[0] != -1 || game.swap_request[1] != -1){
          if(game.display_info.inventory != null){
            if(game.display_info.inventory.acknowledge_swap){
              game.swap_request[0] = -1;
              game.swap_request[1] = -1;
            }
          }
        }
        if(make_swapRequest){
          ref.sendInfoToServer(this.keys, counter, game.mousex_offset, game.mousey_offset, press_mouse, game.swap_request);
          make_swapRequest = false;
        }
        else{
          int[] temp = {-1, -1};
          ref.sendInfoToServer(this.keys, counter, game.mousex_offset, game.mousey_offset, press_mouse, temp);
        }
        //ref.sendInfoToServer(true, false);
    }
  }
}