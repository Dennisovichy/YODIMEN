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
 public static final int INTRO=0, GAME=1, CHOOSE=2, BLUEWIN = 3, REDWIN = 4; //GAME STATES
 private int screen = INTRO;
 Menu menu = new Menu(); //CREATE THE STARTING MENU

 YodiClient client; //OBJECT THAT MANAGES THE CONNECTION TO THE SERVER
 SpamSocket spamming; //THREAD THAT SPAMS OUTPUTS 
 DisplayPacket display_info = null; //DISPLAY PACKET INFORMATION
 private boolean []keys; 
 long counter = 0;
 javax.swing.Timer timer;
 Image back;
 Image frog_icon;

 int centerx; //CENTER OF THE SCREEN
 int centery;

 int mousex; //MOUSE POSITION
 int mousey;
 int mousex_offset = 0; //MOUSE POS FROM PLAYER
 int mousey_offset = 0;

 boolean mouse_pressed = false; 

 int item_start; //STARTING SLOT OF DRAGGED ITEM
 Item held = null; //WJAT ITEM IS BEING HELD?
 boolean holding_item = false; //ITEM IS BEING HELD?
 int[] swap_request = {-1, -1}; //CURRENT SWAP REQUEST, HAVE TO REMEMBER IT BECAUSE HAVE TO SPAM IT

 Font score_font = new Font("Consolas", Font.PLAIN, 30); //FONT USED FOR EVERYTHING

 Scanner freeman = new Scanner(System.in);
 
 public GamePanel(){
  
  back = new ImageIcon("background.png").getImage();
 
  keys = new boolean[KeyEvent.KEY_LAST+1]; 
  //127.0.0.1 local host
  //setPreferredSize(new Dimension(800, 780));
  Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
  setPreferredSize(new Dimension(screen.width, screen.height)); //SET WINDOW SIZE TO THE ENTIRE SCREEN
  setLocation(0,0); //SET IN CORNER
  setFocusable(true);
  requestFocus();
  addKeyListener(this);
  addMouseListener(this);
  timer = new javax.swing.Timer(1, this);
  timer.start();
 }

 public void move(){ //GREAT
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
  
  if(spamming != null){ //ANITQUATED
    spamming.make_swapRequest = true;
  }
  move(); 

  if(screen == GAME || screen == CHOOSE){ //GET THE DISPLAY INFORMATION
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
    menu.checkScreen(mousex, mousey); //CHECK BUTTONS BEING PRESSED
    if(menu.current_screen.game){ //IF PROGRESSED MENU, ASK FOR THE IP ADDRESS AND PORT NUMBER
      //String address = "127.0.0.1";
      String address = "";
      int port = 0;
      try{
        address = JOptionPane.showInputDialog("Enter server address");
        port = Integer.parseInt(JOptionPane.showInputDialog("Enter port number"));
      }
      catch(Exception d){

      }
      try {
          Socket test = new Socket(address, port); //IF THE ADDRESS AND PORT ARE INVALID...
          client = new YodiClient(test);
          spamming = new SpamSocket(client, keys, this);
          //client.sendInfoToServer(keys, counter, mousex_offset, mousey_offset, mouse_pressed);
          //client.sendInfoToServer(true, false);
          //spamming.start();
          screen = CHOOSE;
      } catch (Exception er) {
        System.out.println("caught"); //THEN GO BACK TO THE MAIN MENU
        menu.goBackScreen();
      }
      
    }
    }
    if(screen == CHOOSE){ //CHOOSE A TEAM
      menu.checkScreen(mousex, mousey);
    if(menu.current_screen == menu.red_team){
      client.sendInfoToServer(true, true); //INFORM SERVER THAT TEAM WAS CHOSEN
      spamming.start();
      screen = GAME; //START THE GAME
    }
    if(menu.current_screen == menu.blue_team){
      client.sendInfoToServer(true, false);
      spamming.start();
      screen = GAME;
    }
    } 
    if(screen == GAME){
      mouse_pressed = true; //CODE FOR DRAGGING ITEMS IN THE INVENTORY, SPECIFICALLY WHERE IT STARTS FROM
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
      if(holding_item){ //DETERMINE WHERE ENDPOINT OF DRAGGING ITEM IS, MAKE INVENTORY SWAP REQUEST IF APPROPIAATE
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
     g.setColor(Color.WHITE); //DRAW MENU AND INSTRUCTIONS
     g.fillRect(0,0, getWidth(), getHeight());
     menu.drawScreen(g);
     g.setColor(Color.BLACK);
     g.drawString("Objective: Destroy the other team's shop", centerx, centery);
     g.drawString("A and D to move", centerx, centery+50);
     g.drawString("W to jump, left click to fire", centerx, centery+100);
     g.drawString("Cycle inventory with numbers", centerx, centery+150);
  }
  else if(screen == GAME){
    g.setColor(new Color(255,255,255)); //RESET THE SCREEN
   g.fillRect(0,0,getWidth(), getHeight());
   if(display_info != null){        //IF THERE IS SOMETHING TO DISPLAY   

    int draw_posx = 0;
    int draw_posy = 0;
    for (Tile tile : display_info.game_map) { //DRAW THE TILES
      if(tile != null){
        /*
        switch (tile.getType()) {
          case 1 -> {g.setColor(new Color(255, 0, 255));}
          case 2 -> {g.setColor(new Color(255, 255, 0));}
          case 3 -> {g.setColor(new Color(0, 0, 255));}
          case 4 -> {g.setColor(new Color(255, 0, 0));}
        }
        draw_posx = tile.getX() + (centerx-display_info.player_x);
        draw_posy = tile.getY() + (centery-display_info.player_y);
        if((draw_posx + Map.tilesize) > 0 && draw_posx < getWidth() && (draw_posy + Map.tilesize) > 0 && draw_posy < getHeight()){
          g.fillRect(draw_posx, draw_posy, Map.tilesize, Map.tilesize);
          g.drawRect(tile.getX() + (centerx-display_info.player_x), tile.getY() + (centery-display_info.player_y), Map.tilesize, Map.tilesize);
        }
        */
       tile.draw(g, display_info.player_x, display_info.player_y, centerx, centery);
      }
    }

    int count = 0;
    Player temp = null; //STORE THE PLAYER OBJECT THAT YOU ARE PLAYING AS 
    for(Player play: display_info.players){ //DETERMINE WHICH PLAYER YOU ARE
      if(play != null){
        if(play.x == display_info.player_x && play.y == display_info.player_y){
          temp = play;
          if(play.on_shop){ //IF YOU'RE NEAR THE SHOP, DISPLAY THE BUYING OPTIONS
            g.setColor(Color.BLACK);
            g.setFont(score_font);
            g.drawString("Press to buy: \n T: Autogun(200) \n Y: Drill(200) \n U:Grenade Launcher(200)", 10, 30);
          }
        }
        count += 1;
        play.draw(g, display_info.player_x, display_info.player_y, centerx, centery); //DRAAW EVERY PLAYER
      }
    }
    for(Projectile proj : display_info.projectiles){ //DRAW ALL PROJECTILES
      proj.draw(g, display_info.player_x, display_info.player_y, centerx, centery);
    }  
    if(display_info.inventory != null){ //DRAW INVENTORY AND THE ITEMS TOO
      display_info.inventory.draw(g, centerx, getHeight());
      //System.out.println(Arrays.toString(display_info.inventory.hotbar));
    }
    if(temp != null){ //DRAW YOUR HEALTH  AND MONEY, ALSO CHECKS IF A TEAM HAS WON OR NOT. 
      g.setColor(Color.BLACK);
      g.setFont(score_font);
      g.drawString(temp.health + "/100 HP", 0 + 10, getHeight()-50);
      g.drawString("Money: "+temp.money, getWidth()-300, getHeight()-50);
      if(temp.victor.equals("blue")){
        screen = BLUEWIN;
      }
      if(temp.victor.equals("red")){
        screen = REDWIN;
      }
    }
    if(holding_item){ //DRAW ITEM THAT IS BEING DRAGGED
      held.draw(g, mousex, mousey);
    }
   }  
  }
  else if(screen == BLUEWIN){ //DRAW VICTORY SCREENS
    g.setColor(Color.BLUE);
     g.fillRect(0,0, getWidth(), getHeight());
     g.setColor(Color.BLACK);
     g.drawString("BLUE VICTORY!", centerx, centery);
  }
  else if(screen == REDWIN){
    g.setColor(Color.RED);
     g.fillRect(0,0, getWidth(), getHeight());
     g.setColor(Color.WHITE);
     g.drawString("RED VICTORY!", centerx, centery);
  }
  }
}

//class responsible for communication to server. Should not exist when not in a match
class YodiClient{
  // initialize socket and input output streams
    private Socket socket = null;
    private ByteArrayOutputStream bos;
    private ObjectInputStream input = null; //STREAMS
    private ObjectOutputStream out = null;

    private boolean send_turn = true;

    private DisplayPacket display = null;
 
    // constructor to put ip address and port
    public YodiClient(String address, int port)
    {
        // establish a connection
        try {
            socket = new Socket(address, port); //INIT SOCKET
            
            socket.setPerformancePreferences(0, 1, 0); //DESPERATE ATTEMPTS TO IMPROVE SOCKET PERFORMANCE
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
            socket.setSendBufferSize(100000);
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

    public void sendInfoToServer(boolean[] inputs, long counter, int mousex_offset, int mousey_offset, boolean mouse_pressed, int[] swap){ //"PROPER" VERSION WITH ALL INFO
      if(send_turn){
        InputPacket send = new InputPacket(inputs.clone(), counter, mousex_offset, mousey_offset, mouse_pressed, swap.clone()); //CREATE INPUT PACKET
        //InputPacket send = new InputPacket(inputs);
        try{
          out.writeObject(send); //WRITE THE NEWLY CREATED PACKET TO THE STREAM TO BE READ BY THE SERVER
          //out.flush();
          //send_turn = false;
        }
        catch(IOException e){
          System.out.println(e);
        }
      }
    }

    public void sendInfoToServer(boolean temp, boolean counter){ //LIMITED VERSION THAT ONLY SENDS TEAM CHOICE INFO
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

    public void readServerInfo(){ //READ DISPLAY INFO FROM SERVER
      try {
        display = (DisplayPacket)input.readObject(); //READ FROM STREAM
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

class SpamSocket extends Thread{ //THREAD THAT SPAMS INPUTS AND OUTPUTS 
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
        ref.readServerInfo(); //READ INFO FROM SERVER

        boolean press_mouse = game.mouse_pressed;
        if(game.holding_item){ //DON'T SHOOT IF YOU'RE DRAGGING AN ITEM
          press_mouse = false;
        }
        if(game.swap_request[0] != -1 || game.swap_request[1] != -1){ //ONLY RESET INVENTORY REQUEST IF SIGNAL FROM SERVER TO DO SO
          if(game.display_info.inventory != null){
            if(game.display_info.inventory.acknowledge_swap){
              game.swap_request[0] = -1;
              game.swap_request[1] = -1;
            }
          }
        }
        //if(make_swapRequest){
          ref.sendInfoToServer(this.keys, counter, game.mousex_offset, game.mousey_offset, press_mouse, game.swap_request); //SEND INPUTS TO THE SERVER
          make_swapRequest = false;
        //}
        //else{
          //int[] temp = {-1, -1};
          //ref.sendInfoToServer(this.keys, counter, game.mousex_offset, game.mousey_offset, press_mouse, temp);
        //}
        //ref.sendInfoToServer(true, false);
    }
  }
}