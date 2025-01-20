import java.awt.*;
import java.io.Serializable;

class Projectile implements Serializable, Cloneable{
    public static final int FIZZLE = 0, DAMAGE = 1, EXPLODE = 2; //POSSIBLE BEHAVIORS ON CONTACT

    public static final int LARGE = 30, MEDIUM = 16, SMALL = 6; //HITBOX SIZES

    float x;
    float y;
    int display_x;
    int display_y;
    float velocity_x;
    float velocity_y;
    float face_angle;

    int hitbox_size;

    int collide_terrain;
    int collide_player;
    int damage;
    int lifetime;
    Player firer;

    Image sprite;

    boolean affected_gravity;

    public Projectile(int startx, int starty, float angle, String type, Player fire){
        int speed = 7;
        x = startx;
        y = starty;
        face_angle = angle;
        firer = fire;

        if(type.equals("bullet_small")){
            hitbox_size = SMALL;
            speed = 10;
            lifetime = -1;
            affected_gravity = false;
            collide_terrain = FIZZLE;
            collide_player = DAMAGE;
            damage = 7;
        }
        if(type.equals("laser")){
            hitbox_size = SMALL;
            speed = 10;
            lifetime = 30;
            affected_gravity = false;
            collide_terrain = DAMAGE;
            collide_player = FIZZLE;
            damage = 7;
        }

        velocity_x = (float)Math.cos(angle) * (float)speed;
        velocity_y = (float)Math.sin(angle) * (float)speed;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Projectile temp = (Projectile)super.clone();
        return temp;
    }

    public Rectangle getHitbox(){
        return new Rectangle(display_x - (hitbox_size/2), display_y - (hitbox_size/2), hitbox_size, hitbox_size);
    }

    public void update(){
        x = x + velocity_x;
        y = y + velocity_y;
        display_x = Math.round(x);
        display_y = Math.round(y);
        if(lifetime > 0){
            lifetime -= 1;
        }
    }

    public void draw(Graphics g){
        g.setColor(Color.CYAN);
        g.drawLine(display_x, display_y, display_x + (int)(Math.round((Math.cos(face_angle))*200.0)), display_y + (int)(Math.round((Math.sin(face_angle))*200.0)));
        Rectangle rec = getHitbox();
        g.drawRect(rec.x, rec.y, rec.width, rec.height);
    }

    public void draw(Graphics g, int px, int py, int camx, int camy){
        g.setColor(Color.CYAN);
        g.drawLine(display_x - (px-camx), display_y - (py-camy), display_x + (int)(Math.round((Math.cos(face_angle))*200.0)) - (px-camx), display_y + (int)(Math.round((Math.sin(face_angle))*200.0)) - (py-camy));
        Rectangle rec = getHitbox();
        g.drawRect(rec.x-(px-camx), rec.y-(py-camy), rec.width, rec.height);
    }

    public Tile checkMapCollision(Map map){
        Rectangle rect = getHitbox();
        for(Tile tile : map.build_map){
            if(tile != null){
                if(tile.getHitbox().intersects(rect)){
                    if(collide_terrain == DAMAGE){
                        tile.health -= damage;
                    }
                    return tile;
                }
            }
        }
        return null;
    }

    public Player checkPlayerCollision(Player[] players){
        for(Player check: players){
            if(check != null){
                if(!check.equals(firer)){
                    if(check.getHitbox().intersects(getHitbox())){
                        if(collide_player == DAMAGE){
                            check.health -= damage;
                        }
                        return check;
                    }
                }
            }
        }
        return null;
    }
}