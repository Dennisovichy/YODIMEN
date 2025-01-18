import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class test extends JFrame{
 GameP game= new GameP();
  
  public test() {
    super("YODIMEN");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(game);
    pack();
    setVisible(true);
  }
    
  public static void main(String[] arguments) {
    new test();  
  }
}

class GameP extends JPanel implements KeyListener, ActionListener, MouseListener{
 public static final int INTRO=0, GAME=1, END=2;
 private int screen = GAME;

 private boolean []keys;
 long counter = 0;
 javax.swing.Timer timer;
 Image back;
 Image frog_icon;

 int mousex;
 int mousey;

 int widthx;
 int widthy;

 int camx = 0;
 int camy = 0;

 //ArrayList<ArrayList<Tile>> build_map = new ArrayList<>();

 Scanner freeman = new Scanner(System.in);
 
 public GameP(){
  
  back = new ImageIcon("background.png").getImage();
  keys = new boolean[KeyEvent.KEY_LAST+1]; 
  
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
    if(keys[KeyEvent.VK_W]){
      camy -= 10;
    }
    if(keys[KeyEvent.VK_S]){
      camy += 10;
    }
    if(keys[KeyEvent.VK_A]){
      camx -= 10;
    }
    if(keys[KeyEvent.VK_D]){
      camx += 10;
    }
  }

 }
 
 @Override
 public void actionPerformed(ActionEvent e){
  Point offset = new Point(0,0);
  Point mouse = MouseInfo.getPointerInfo().getLocation();
  offset = getLocationOnScreen();
  try{
    offset = getLocationOnScreen();
  }
  catch(IllegalComponentStateException err){
    
  }
  mousex = mouse.x - offset.x;
  mousey = mouse.y - offset.y;

  widthx = getWidth();
  widthy = getHeight();

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
   
 }

 @Override
 public void mouseReleased(MouseEvent e){}

 @Override
 public void paint(Graphics g){
  if(screen == INTRO){
      
  }
  else if(screen == GAME){
    g.setColor(Color.WHITE);
    g.fillRect(0,0, 1500, 1500);
    g.setColor(Color.BLACK);
    for(int i = 0; i < (widthx/Map.tilesize + 5); i++){
      g.drawLine(((0 - camx) % Map.tilesize) + i*Map.tilesize, 0, ((0 - camx) % Map.tilesize) + i*Map.tilesize, widthy);
    }
    for(int i = 0; i < (widthy/Map.tilesize + 5); i++){
      g.drawLine(0, ((0 - camy) % Map.tilesize) + i*Map.tilesize, widthx,  ((0 - camy) % Map.tilesize) + i*Map.tilesize);
    }
  }
    }
}