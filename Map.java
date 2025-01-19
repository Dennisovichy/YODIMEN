import java.awt.*;
import java.io.Serializable;
import java.util.*;

class Map implements Serializable, Cloneable{
  public ArrayList<Tile> build_map = new ArrayList<>();
  static int tilesize = 40;
  
  int default_camx = 300;
  int default_camy = 400;
  
  public Map(){
    
    
  }
  
  public void draw(Graphics g){// server
    for (Tile tile : build_map) {
      switch (tile.getType()) {
        case 1 -> {g.setColor(new Color(255, 0, 255));}
        case 2 -> {g.setColor(new Color(255, 255, 0));}
        case 3 -> {g.setColor(new Color(0, 0, 255));}
        case 4 -> {g.setColor(new Color(255, 0, 0));}
      }
      g.fillRect(tile.getX(), tile.getY(), tilesize, tilesize);
      g.drawRect(tile.getX(), tile.getY(), tilesize, tilesize);
    }
  }

  public void draw(Graphics g, int px, int py, int camx, int camy){// client
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

  @Override
    public Object clone() throws CloneNotSupportedException{
        Map temp = (Map)super.clone();
        return temp;
    }
}

