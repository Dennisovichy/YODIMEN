import java.awt.*;
import java.io.Serializable;
import java.util.*;

class Map implements Serializable, Cloneable{
  ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>();
  static int tilesize = 40;
  public Map(){
    ArrayList<Tile> row = new ArrayList<Tile>();
    for(int i = 0; i < 30; i++){
      row.add(new Rock());
    }
    tiles.add(row);
    for(int x = 0; x < 14; x++){
      ArrayList<Tile> row2 = new ArrayList<Tile>();
      for(int i = 0; i < 30; i++){
        row2.add(null);
      }
      tiles.add(row2);
    }
    ArrayList<Tile> row4 = new ArrayList<Tile>();
    for(int i = 0; i < 30; i++){
      row4.add(new Rock());
    }
    tiles.add(row4);
    
    
  }
  public void draw(Graphics g){
    g.setColor(Color.YELLOW);
    for(int y = 0; y < tiles.size(); y++){
      for(int x = 0; x < tiles.get(0).size(); x++){
        if(tiles.get(y).get(x) != null){
          g.fillRect(x*tilesize, y*tilesize, tilesize, tilesize);
        }
      }
    }
  }

  public void draw(Graphics g, int px, int py){
    g.setColor(Color.YELLOW);
    for(int y = 0; y < tiles.size(); y++){
      for(int x = 0; x < tiles.get(0).size(); x++){
        if(tiles.get(y).get(x) != null){
          g.fillRect(x*tilesize + (300-px), y*tilesize + (400-py), tilesize, tilesize);
        }
      }
    }
  }

  @Override
    public Object clone() throws CloneNotSupportedException{
        Map temp = (Map)super.clone();
        return temp;
    }
}

class Tile implements Serializable{
    private int x;
    private int y;
    private int type;
    private Image image;
    private boolean[] collision = {true, true};
    private Rectangle hitbox;
    
    public Tile(){
      
    }

    public Rectangle getHitbox(){
      return new Rectangle(x-Map.tilesize/2, y-Map.tilesize/2, Map.tilesize, Map.tilesize);
    }
}

class Rock extends Tile{
    
  
    public Rock(){
      
    
    }
}