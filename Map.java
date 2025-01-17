import java.awt.*;
import java.io.Serializable;
import java.util.*;

class Map implements Serializable, Cloneable{
  ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>();
  static int tilesize = 40;
  
  int default_camx = 300;
  int default_camy = 400;
  
  public Map(){
    ArrayList<Tile> row = new ArrayList<Tile>();
    for(int i = 0; i < 30; i++){
      row.add(new Rock(i*tilesize, 0));
    }
    tiles.add(row);
    for(int x = 0; x < 11; x++){
      ArrayList<Tile> row2 = new ArrayList<Tile>();
      for(int i = 0; i < 30; i++){
        row2.add(null);
      }
      tiles.add(row2);
    }
    ArrayList<Tile> row4 = new ArrayList<Tile>();
    for(int i = 0; i < 30; i++){
      row4.add(new Rock(i*tilesize, 12*tilesize));
    }
    tiles.add(row4);
    
    
  }
  public void draw(Graphics g){
    for(int y = 0; y < tiles.size(); y++){
      for(int x = 0; x < tiles.get(0).size(); x++){
        if(tiles.get(y).get(x) != null){
          g.setColor(Color.YELLOW);
          g.fillRect(x*tilesize, y*tilesize, tilesize, tilesize);
          g.setColor(Color.BLACK);
          Rectangle balls = tiles.get(y).get(x).getHitbox();
          g.drawRect(balls.x, balls.y, balls.width, balls.height);
        }
      }
    }
  }

  public void draw(Graphics g, int px, int py, int camx, int camy){
    g.setColor(Color.YELLOW);
    for(int y = 0; y < tiles.size(); y++){
      for(int x = 0; x < tiles.get(0).size(); x++){
        if(tiles.get(y).get(x) != null){
          g.fillRect(x*tilesize + (camx-px), y*tilesize + (camy-py), tilesize, tilesize);
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
    
    public Tile(int x, int y){
      this.x = x;
      this.y = y;
    }

    public Rectangle getHitbox(){
      return new Rectangle(x, y, Map.tilesize, Map.tilesize);
    }
}

class Rock extends Tile{
    
  
    public Rock(int x, int y){
      super(x, y);
    
    }
}