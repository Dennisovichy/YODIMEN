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

    private Rectangle hitbox;
    private boolean[] collision = {true, true}; //collides with either team?
    public Player(int x, int y){
        this.x = x;
        this.y = y;
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
        g2.fillRect(x, y, 50, 50);
    }
}