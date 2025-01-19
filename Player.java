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

    public int lookat_x;
    public int lookat_y;
    public float lookat_angle = 0;

    private int movement_x = 0;
    private int movement_y = 0;

    private int height = 70;
    private int width = 30;

    private int collidebox_depth = 20;
    private int collidebox_outset = 10;

    public boolean red_team;

    public int holding_slot = 0;

    private boolean colliding_up = false;
    private boolean colliding_down = false;
    private boolean colliding_left = false;
    private boolean colliding_right = false;
    private Rectangle hitbox;
    private boolean[] collision = {true, true}; //collides with either team?
    public Player(int x, int y, boolean redteam){
        this.x = x;
        this.y = y;
        this.lookat_x = x;
        this.lookat_y = y;
        this.red_team = redteam;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Player temp = (Player)super.clone();
        return temp;
    }

    public void updateLookPos(int offsetx, int offsety){
        this.lookat_x = this.x - offsetx;
        this.lookat_y = this.y - offsety;
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
        if(keys[KeyEvent.VK_1]){
            holding_slot = 0;
        }
        if(keys[KeyEvent.VK_2]){
            holding_slot = 1;
        }
        if(keys[KeyEvent.VK_3]){
            holding_slot = 2;
        }
        if(keys[KeyEvent.VK_4]){
            holding_slot = 3;
        }
        if(keys[KeyEvent.VK_5]){
            holding_slot = 4;
        }
        if(keys[KeyEvent.VK_6]){
            holding_slot = 5;
        }
        if(keys[KeyEvent.VK_7]){
            holding_slot = 6;
        }
        if(keys[KeyEvent.VK_8]){
            holding_slot = 7;
        }
        if(keys[KeyEvent.VK_9]){
            holding_slot = 8;
        }
        if(keys[KeyEvent.VK_0]){
            holding_slot = 9;
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
        if(red_team){
            g2.setColor(Color.RED);
        }
        else{
            g2.setColor(Color.BLUE);
        }
        g2.fillRect(x - (width/2), y - (height/2), width, height);
        g2.setColor(Color.BLACK);
        Rectangle[] drawthese = getCollideBoxes();
        for(Rectangle draw : drawthese){
            g2.drawRect(draw.x, draw.y, draw.width, draw.height);
        }
    }

    public void draw(Graphics g, int px, int py, int centx, int centy){
        Graphics2D g2 = (Graphics2D)g;
        if(red_team){
            g2.setColor(Color.RED);
        }
        else{
            g2.setColor(Color.BLUE);
        }
        g2.fillRect(x - (width/2) + (centx - px), y - (height/2) + (centy - py), width, height);
        //g2.fillRect(300 - (x - px) - (width/2),400 - (y - py) - (height/2), width, height);
        //System.out.println(x);
        g2.setColor(Color.BLACK);
        Rectangle[] drawthese = getCollideBoxes();
        for(Rectangle draw : drawthese){
            g2.drawRect(draw.x + (centx - px), draw.y + (centy - py), draw.width, draw.height);
        }
    }
}