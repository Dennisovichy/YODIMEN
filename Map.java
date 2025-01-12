import java.awt.*;
import java.io.Serializable;
import java.util.*;

class Map implements Serializable{
  ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>();
  static int tilesize = 40;
  public Map(){
    ArrayList<Tile> row = new ArrayList<Tile>();
    for(int i = 0; i < 10; i++){
      row.add(new Rock());
    }
    tiles.add(row);
    
    
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