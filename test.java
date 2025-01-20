import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
 
 Map map;

 int mousex;
 int mousey;

 int widthx;
 int widthy;

 int camx = 0;
 int camy = 0;

 boolean holding_left = false;
 boolean holding_right = false;

 int selected_type;

 //ArrayList<Tile> build_map = new ArrayList<>();

 //ArrayList<ArrayList<Tile>> build_map = new ArrayList<>();

 Scanner freeman = new Scanner(System.in);
 
 public GameP(){
  
  map = new Map();
  
  back = new ImageIcon("background.png").getImage();
  keys = new boolean[KeyEvent.KEY_LAST+1]; 
  
  setPreferredSize(new Dimension(800, 780));
  setFocusable(true);
  requestFocus();
  addKeyListener(this);
  addMouseListener(this);
  timer = new javax.swing.Timer(10, this);
  timer.start();


  this.selected_type = 0;
 }

 public void move(){
  if(screen == INTRO){
  
  }
  else if(screen == GAME){
    if(keys[KeyEvent.VK_W]){
      camy -= 40;
    }
    if(keys[KeyEvent.VK_S]){
      camy += 40;
    }
    if(keys[KeyEvent.VK_A]){
      camx -= 40;
    }
    if(keys[KeyEvent.VK_D]){
      camx += 40;
    }

    if(holding_left){
      boolean occupied = false;// flag to check if this space already has a tile
        for (Tile tile : this.map.build_map){
          if (tile.pointCollision(mousex + camx, mousey + camy)){// check if the mouse is colliding with any of the tiles to avoid two tiles inhabiting the same space
            occupied = true;
          }
        }
        if (!occupied){// no prexisting tile in this space
          this.map.build_map.add(new Tile(mousex - ((mousex + (0 - camx)) % Map.tilesize) + camx, mousey - ((mousey + (0 - camy)) % Map.tilesize) + camy, this.selected_type, ""));// adding a new tile at the mouse if there is no preexisting tile
        }
    }
    if(holding_right){
      for (int i = this.map.build_map.size() - 1; i >= 0; i --){
          if (this.map.build_map.get(i).pointCollision(mousex + camx, mousey + camy)){// check if the mouse is colliding with any of the tiles
            this.map.build_map.remove(this.map.build_map.get(i));// remove this tile
          }
        }
    }
  }
 }

  public void save(){
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter save num:");
    try (FileOutputStream file = new FileOutputStream("maps/Map" + scanner.nextLine());
    ObjectOutputStream outputStream = new ObjectOutputStream(file);){// stream the plot data to a file
        Map tmp = null;
        try {
            tmp = (Map)this.map.clone();// clone plot
        } catch (Exception e) {
        }
        outputStream.writeObject(tmp);// write it 
    } catch (Exception e) {
        System.out.println(e);
    }
  }

  public void load(){
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter save num:");
    try (FileInputStream file = new FileInputStream("maps/Map" + scanner.nextLine());
    ObjectInputStream inputStream = new ObjectInputStream(file);){// stream the plot data to a file
        this.map = (Map)inputStream.readObject();// write it 
    } catch (Exception e) {
        System.out.println(e);
    }
  }

 
 @Override
 public void actionPerformed(ActionEvent e){
  Point offset = new Point(0,0);
  Point mouse = MouseInfo.getPointerInfo().getLocation();
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
  switch (key) {
    case KeyEvent.VK_1 -> {this.selected_type = 1;}
    case KeyEvent.VK_2 -> {this.selected_type = 2;}
    case KeyEvent.VK_3 -> {this.selected_type = 3;}
    case KeyEvent.VK_4 -> {this.selected_type = 4;}
    case KeyEvent.VK_0 -> {save(); System.out.println("save");}
    case KeyEvent.VK_9 -> {load();System.out.println("load");}
  }
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
  switch (e.getButton()) {
      case MouseEvent.BUTTON1 -> {// left click (add tile)
        holding_left = true;
      }

      case MouseEvent.BUTTON3 -> {// right click (remove tile)
        holding_right = true;
      }
    }
  }

 @Override
 public void mouseReleased(MouseEvent e){
  switch (e.getButton()) {
      case MouseEvent.BUTTON1 -> {// left click (add tile)
        holding_left = false;
      }

      case MouseEvent.BUTTON3 -> {// right click (remove tile)
        holding_right = false;
      }
    }
  }
 

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
        g.setColor(Color.YELLOW);
        g.fillRect(300 + (0 - camx), 420 + (0 - camy), Map.tilesize, Map.tilesize);
        for (Tile tile : this.map.build_map) {
            switch (tile.getType()) {
              case 1 -> {g.setColor(new Color(255, 0, 255));}
              case 2 -> {g.setColor(new Color(255, 255, 0));}
              case 3 -> {g.setColor(new Color(0, 0, 255));}
              case 4 -> {g.setColor(new Color(255, 0, 0));}
            }
            g.fillRect(tile.getX() + (0 - camx), tile.getY() + (0 - camy), Map.tilesize, Map.tilesize);
        }
      }
    }
}