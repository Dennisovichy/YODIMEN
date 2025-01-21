import java.awt.*;
import java.io.Serializable;

class Tile implements Serializable, Cloneable{
    private int x;
    private int y;
    private int type;
    private Image image;

    public int health = 30;
    private boolean[] collision = {true, true};
    private Rectangle hitbox;
    public boolean deleted = false;
    
    public Tile(int x, int y, int type, Color color, int health){
      this.x = x;
      this.y = y;
      this.type = type;
      
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Tile temp = (Tile)super.clone();
        return temp;
    }

    public int getType(){return this.type;}

    public Rectangle getHitbox(){
      return new Rectangle(x, y, Map.tilesize, Map.tilesize);
    }

    public boolean pointCollision(int x, int y){
      return x > this.x && x < this.x + Map.tilesize && y > this.y && y < this.y + Map.tilesize;
    }

    public int getX(){return this.x;}
    public int getY(){return this.y;}
}