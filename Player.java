import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;

class BaseEntity{
    private int x;
    private int y;
    private Rectangle hitbox;
    private boolean[] collision = {true, true}; //collides with either team?
    public BaseEntity(int x, int y){
        this.x = x;
        this.y = y;
    }
}

public class Player implements Serializable, Cloneable{
    public int x;
    public int y;

    private int movement_x = 0;
    private int movement_y = 0;

    private int height = 70;
    private int width = 30;

    private int collidebox_depth = 20;
    private int collidebox_outset = 10;

    private boolean colliding_up = false;
    private boolean colliding_down = false;
    private boolean colliding_left = false;
    private boolean colliding_right = false;
    private Rectangle hitbox;
    private boolean[] collision = {true, true}; //collides with either team?
    public Player(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Player temp = (Player)super.clone();
        return temp;
    }

    public Rectangle getHitbox(){
        return new Rectangle(x-(width/2), y-(height/2), width, height);
    }

    public Rectangle[] getCollideBoxes(){
    //left
    //right
    //up
    //down
    Rectangle[] output = {new Rectangle(x - (width/2) - collidebox_outset, y - (height/2) + (collidebox_depth-collidebox_outset), collidebox_depth, height-(2*(collidebox_depth-collidebox_outset)))
        ,new Rectangle(x + (width/2) - (collidebox_depth-collidebox_outset), y - (height/2) + (collidebox_depth-collidebox_outset), collidebox_depth, height-(2*(collidebox_depth-collidebox_outset)))
        ,new Rectangle(x - (width/2) - collidebox_outset, y - (height/2) - collidebox_outset, width + (2*(collidebox_depth-collidebox_outset)), collidebox_depth)
        ,new Rectangle(x - (width/2) - collidebox_outset, y + (height/2) - (collidebox_depth-collidebox_outset), width + (2*(collidebox_depth-collidebox_outset)), collidebox_depth)
        };
        return output;
    }

    public void checkInput(boolean[] keys){
        movement_x = 0;
        if(keys[KeyEvent.VK_A]){
            movement_x = -5;
        }
        if(keys[KeyEvent.VK_D]){
            movement_x = 5;
        }
        if(keys[KeyEvent.VK_W]){
            if(colliding_down){
                movement_y = -20;
            }
        }
    }

    public void checkMapCollision(Map map){
        colliding_down = false;
        colliding_left = false;
        colliding_right = false;
        colliding_up = false;

        Rectangle[] rects = getCollideBoxes();
        for(ArrayList<Tile> tilelist: map.tiles){
            for(Tile tile : tilelist){
                if(tile != null){
                if(!colliding_left){
                    if(tile.getHitbox().intersects(rects[0])){
                        colliding_left = true;
                        //System.out.println("Left collide");
                    }
                }
                if(!colliding_right){
                    if(tile.getHitbox().intersects(rects[1])){
                        colliding_right = true;
                        //System.out.println("right collide");
                    }
                }
                if(!colliding_up){
                    if(tile.getHitbox().intersects(rects[2])){
                        colliding_up = true;
                        //System.out.println("up collide");
                    }
                }
                if(!colliding_down){
                    if(tile.getHitbox().intersects(rects[3])){
                        colliding_down = true;
                        //System.out.println("down collide");
                    }
                }
                }
            }
        }
    }

    public void updatePos(){
        if(!colliding_down){
            if(movement_y < 10){
                movement_y += 1;
            }
        }
        //trying to move left
        if(movement_x < 0){
            if(!colliding_left){
                x = x + movement_x;
            }
        }
        if(movement_x > 0){
            if(!colliding_right){
                x = x + movement_x;
            }
        }
        if(movement_y < 0){
            if(!colliding_up){
                y = y + movement_y;
            }
        }
        if(movement_y > 0){
            if(!colliding_down){
                y = y + movement_y;
            }
        }
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.fillRect(x - (width/2), y - (height/2), width, height);
        g2.setColor(Color.RED);
        Rectangle[] drawthese = getCollideBoxes();
        for(Rectangle draw : drawthese){
            g2.drawRect(draw.x, draw.y, draw.width, draw.height);
        }
    }

    public void draw(Graphics g, int px, int py){
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.fillRect(x - (width/2) + (300 - px), y - (height/2) + (400 - py), width, height);
        if(px == x && py == y){

        }
        //g2.fillRect(300 - (x - px) - (width/2),400 - (y - py) - (height/2), width, height);
        //System.out.println(x);
        g2.setColor(Color.RED);
        Rectangle[] drawthese = getCollideBoxes();
        for(Rectangle draw : drawthese){
            g2.drawRect(draw.x + (300 - px), draw.y + (400 - py), draw.width, draw.height);
        }
    }
}