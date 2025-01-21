import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.Serializable;
import javax.swing.*;

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

    public int health = 100;
    public boolean dead = false;
    public int holding_slot = 0;

    public Item held_item = null;

    private static transient Image[] bluewalks = {new ImageIcon("blue team sprites/blueteamwalk1.png").getImage(),new ImageIcon("blue team sprites/blueteamwalk2.png").getImage(),new ImageIcon("blue team sprites/blueteamwalk3.png").getImage(),new ImageIcon("blue team sprites/blueteamwalk4.png").getImage()};
    private static transient Image blueteamjumps = new ImageIcon("blue team sprites/blueteamjump.png").getImage();
    private static transient Image[] redwalks = {new ImageIcon("red team sprites/redteamwalk1.png").getImage(),new ImageIcon("red team sprites/redteamwalk2.png").getImage(),new ImageIcon("red team sprites/redteamwalk3.png").getImage(),new ImageIcon("red team sprites/redteamwalk4.png").getImage()};
    private static transient Image redteamjumps = new ImageIcon("red team sprites/redteamjump.png").getImage();
    private static transient Image corpse = new ImageIcon("blue team sprites/yodicorpse.png").getImage();

    private static transient Image eye = new ImageIcon("blue team sprites/eye.png").getImage();
    private static transient Image pistol = new ImageIcon("weapons/pistol.png").getImage();
    private static transient Image autogun = new ImageIcon("weapons/autogun.png").getImage();
    private static transient Image drill = new ImageIcon("weapons/drill.png").getImage();
    private static transient Image fabricator = new ImageIcon("weapons/fabricator.png").getImage();

    private int animation_frame = 0;

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
        this.lookat_angle = (float)(Math.atan2((float)offsetx,(float)offsety) + Math.PI);
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
        ,new Rectangle(x - (width/2) - collidebox_outset/2, y - (height/2) - collidebox_outset, width + (1*(collidebox_depth-collidebox_outset)), collidebox_depth)
        ,new Rectangle(x - (width/2) - collidebox_outset/2, y + (height/2) - (collidebox_depth-collidebox_outset), width + (1*(collidebox_depth-collidebox_outset)), collidebox_depth)
        };
        return output;
    }

    public void checkInput(boolean[] keys){
        movement_x = 0;
        if(keys[KeyEvent.VK_A] && keys[KeyEvent.VK_D]){
            movement_x = 0;
        }
        else if(keys[KeyEvent.VK_A]){
            movement_x = -5;
            animation_frame += 1;
        }
        else if(keys[KeyEvent.VK_D]){
            movement_x = 5;
            animation_frame -= 1;
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
            for(Tile tile : map.build_map){
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
            else{
                movement_y = 0;
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
        //System.out.println(health);

        int draw_x = x - (width/2) + (centx - px) - 2;
        int draw_y = y - (height/2) + (centy - py) - 4;

        if(dead){
            g.drawImage(corpse, draw_x, draw_y, null);
        }
        else{
            AffineTransform rot = new AffineTransform();
            rot.rotate(-lookat_angle - (Math.PI/2), 6, 6);
            AffineTransformOp rotOp = new AffineTransformOp(rot, AffineTransformOp.TYPE_BILINEAR);

            if(red_team){
                if(!colliding_down){
                    g.drawImage(redteamjumps, draw_x, draw_y, null);
                }
                else{
                    g.drawImage(redwalks[translateToArray(animation_frame)], draw_x, draw_y, null);
                }
            }
            else{
                if(!colliding_down){
                    g.drawImage(blueteamjumps, draw_x, draw_y, null);
                }
                else{
                    g.drawImage(bluewalks[translateToArray(animation_frame)], draw_x, draw_y, null);
                }
            }
            BufferedImage b_img = new BufferedImage(eye.getWidth(null), eye.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
            b_img.getGraphics().drawImage(eye, 0, 0, null);
            g2.drawImage(b_img, rotOp, x + (centx - px) - 6, y + (centy - py) - 25);

            rot = new AffineTransform();
            rot.rotate(-lookat_angle - (Math.PI/2), 50, 50);
            rotOp = new AffineTransformOp(rot, AffineTransformOp.TYPE_BILINEAR);
            b_img = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
            if(held_item != null){
                if(held_item.id.equals("pistol")){
                    b_img.getGraphics().drawImage(pistol, 0, 0, null);
                }
                else if(held_item.id.equals("autogun")){
                    b_img.getGraphics().drawImage(autogun, 0, 0, null);
                }
                else if(held_item.id.equals("drill")){
                    b_img.getGraphics().drawImage(drill, 0, 0, null);
                }
                else if(held_item.id.equals("fabricator")){
                    b_img.getGraphics().drawImage(fabricator, 0, 0, null);
                }
            }
            g2.drawImage(b_img, rotOp, x + (centx - px) - 50, y + (centy - py) - 49);
        }

        g2.setColor(Color.BLACK);
        Rectangle[] drawthese = getCollideBoxes();
        for(Rectangle draw : drawthese){
            g2.drawRect(draw.x + (centx - px), draw.y + (centy - py), draw.width, draw.height);
        }
        
    }

    private int translateToArray(int frame){
        if(frame > 0){
            return frame % bluewalks.length; //4, 0 [] 3, 3 [] 5, 1
        }
        if(frame < 0){
            if(((-1 * frame) % bluewalks.length) == 0){
                return 0;
            }
            return 4 - ((-1 * frame) % bluewalks.length);   //-1, 3 [] -2, 2[] -3, 1   -4, 0     -5, 3
        }
        return 0;
    }
}