import java.awt.*;
import java.io.Serializable;

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

    public boolean pointCollision(int x, int y){
      return x > this.x && x < this.x + Map.tilesize && y > this.y && y < this.y + Map.tilesize;
    }

    public int getX(){return this.x;}
    public int getY(){return this.y;}
}