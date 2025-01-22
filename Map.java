//CLASS THAT STORES AND UPDATES ALL OF THE TILES IN THE GAME
import java.awt.*;
import java.io.Serializable;
import java.util.*;

class Map implements Serializable, Cloneable{
  public ArrayList<Tile> build_map = new ArrayList<>(); //store all of the map tiles
  static int tilesize = 40;
  
  int default_camx = 300;
  int default_camy = 400;
  
  public Map(){
    
    
  }
  
  public void draw(Graphics g){// server (USELESS)
    for (Tile tile : build_map) {
      switch (tile.getType()) {
        case 0 -> {g.setColor(new Color(0, 0, 255));}
        case 1 -> {g.setColor(new Color(255, 0, 255));}
        case 2 -> {g.setColor(new Color(255, 255, 0));}
        case 3 -> {g.setColor(new Color(0, 0, 255));}
        case 4 -> {g.setColor(new Color(255, 0, 0));}
      }
      g.fillRect(tile.getX(), tile.getY(), tilesize, tilesize);
      g.drawRect(tile.getX(), tile.getY(), tilesize, tilesize);
    }
  }

  public void draw(Graphics g, int px, int py, int camx, int camy){// client (USELESS)
    for (Tile tile : build_map) {
      switch (tile.getType()) {
        case 1 -> {g.setColor(new Color(255, 0, 255));}
        case 2 -> {g.setColor(new Color(255, 255, 0));}
        case 3 -> {g.setColor(new Color(0, 0, 255));}
        case 4 -> {g.setColor(new Color(255, 0, 0));}
      }
      g.fillRect(tile.getX() + (camx-px), tile.getY() + (camy-py), tilesize, tilesize);
      g.drawRect(tile.getX() + (camx-px), tile.getY() + (camy-py), tilesize, tilesize);
    }
  }


  public void update(){ //determine if tile should be destroyed or not, remove destroyed tiles
    ArrayList<Integer> delete_tiles = new ArrayList<>();
    for(int i = 0; i < build_map.size(); i++){
      if(build_map.get(i).health <= 0){
        //System.out.println(build_map.get(i).health);
        delete_tiles.add(i);
      }
    }
    Collections.reverse(delete_tiles);
    for(int kill: delete_tiles){
      build_map.remove(kill);
    }
  }
  

  @Override
    public Object clone() throws CloneNotSupportedException{
        Map temp = (Map)super.clone();
        return temp;
    }
}

