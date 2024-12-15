import java.awt.*;
import java.util.*;

class Map{
  ArrayList<ArrayList<Tile>> tiles;

  public Map(){

  }
  public void draw(Graphics g){

  }
}

class Tile{
    private int x;
    private int y;
    private int type;
    private Image image;
    private boolean[] collision = {true, true};
    private Rectangle hitbox;
    
}