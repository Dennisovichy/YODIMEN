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
 private int screen = INTRO;

 Menu menu = new Menu();
 private boolean []keys;
 long counter = 0;
 javax.swing.Timer timer;
 Image back;
 Image frog_icon;

 int mousex;
 int mousey;

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
   
  }

 }
 
 @Override
 public void actionPerformed(ActionEvent e){
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
  if(screen == INTRO){
    Point mouse = MouseInfo.getPointerInfo().getLocation();
   Point offset = getLocationOnScreen();
   mousex = mouse.x - offset.x;
   mousey = mouse.y - offset.y;
   menu.checkScreen(mousex, mousey);
   if(menu.current_screen.game){
    String address = JOptionPane.showInputDialog("Enter server address");
    int port = Integer.parseInt(JOptionPane.showInputDialog("Enter port number"));
    screen = GAME;
   }
  } 
 }

 @Override
 public void mouseReleased(MouseEvent e){}

 @Override
 public void paint(Graphics g){
  if(screen == INTRO){
      g.setColor(Color.WHITE);
      g.fillRect(0,0, 1500, 1500);
      menu.drawScreen(g);
  }
  else if(screen == GAME){
   // The last parameter is an ImageObserver. Back when images were not loaded
   // right away you would specify what object would be notified when it was loaded.
   // We are not doing that, so null will always be fine.
   
  }
    }
}