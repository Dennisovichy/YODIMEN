import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

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
    }

    public void updatePos(){
        x = x + movement_x;
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
        //g2.fillRect(x - (width/2) + (300 - px), y - (height/2) + (400 - py), width, height);
        g2.fillRect(300 - (x - px) - (width/2),400 - (y - py) - (height/2), width, height);
        //System.out.println(x);
        g2.setColor(Color.RED);
        Rectangle[] drawthese = getCollideBoxes();
        for(Rectangle draw : drawthese){
            g2.drawRect(draw.x + (300 - px), draw.y + (400 - py), draw.width, draw.height);
        }
    }
}