import java.awt.*;
import java.awt.event.*;

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

public class Player{
    private int x;
    private int y;

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
}