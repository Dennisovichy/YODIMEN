//CLASS THAT REPRESENTS THE TILES IN THE GAME, BOTH GENERALS TILES AND THE SHOP TILE

import java.awt.*;
import java.io.Serializable;
import javax.swing.ImageIcon;

class Tile implements Serializable, Cloneable{
    private int x;
    private int y;
    private int type;//0: regular tiles, 1: shop
    private Color color; //COLOR OF GENERAL TILE
    private static transient Image shop = new ImageIcon("tiles/shop.png").getImage(); //IMAGE FOR THE SHOP TILES

    public boolean isShop = false; //IS TILE SHOP?

    public int health = 30; //HEALTH OF THE TILE
    private boolean[] collision = {true, true};
    private Rectangle hitbox;
    public boolean deleted = false; 
    
    public Tile(int x, int y, int type, Color colo, int health){
      this.x = x;
      this.y = y;
      this.type = type;
      this.color = colo;
      this.health = health;

      if(type == 1){
        isShop = true; //INIT'D AS SHOP TILE
        this.health = 1000;
      }
      
    }

    public void draw(Graphics g, int px, int py, int camx, int camy){
      g.setColor(color);
      if(type == 0){
        g.fillRect(x - (px-camx), y - (py-camy), Map.tilesize, Map.tilesize); //DRAW AS COLOR IF GENERAL TILE
      }
      else if(type == 1){
        g.drawImage(shop, x - (px-camx), y - (py-camy), null); //DRAW THE IMAGE IF IT'S A SHOP TILE
      }
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Tile temp = (Tile)super.clone();
        return temp;
    }

    public int getType(){return this.type;}

    public Rectangle getHitbox(){ //RETURN HITBOX
      return new Rectangle(x, y, Map.tilesize, Map.tilesize);
    }

    public boolean pointCollision(int x, int y){ 
      return x > this.x && x < this.x + Map.tilesize && y > this.y && y < this.y + Map.tilesize;
    }

    public int getX(){return this.x;}
    public int getY(){return this.y;}
}