//CLASS THAT REPRESENTS ALL THE DIFFERENT KINDS OF PROJECTILES AND THEIR INTERACTIONS
import java.awt.*;
import java.io.Serializable;

class Projectile implements Serializable, Cloneable{
    public static final int FIZZLE = 0, DAMAGE = 1, EXPLODE = 2; //POSSIBLE BEHAVIORS ON CONTACT

    public static final int LARGE = 30, MEDIUM = 16, SMALL = 6; //HITBOX SIZES

    float x; //PROPER POS
    float y;
    int display_x; //DISPLAY POS
    int display_y;
    float velocity_x; //VELOCITY 
    float velocity_y;
    float face_angle;

    String id; //NAME OF PROJECTILE

    int hitbox_size; //SIZE OF HITBOX
    int length; //SIZE OF DRAWN THING
    int collide_terrain; //BEHAVIOR WHEN IMPACT TERRAIN
    int collide_player; //BEHAVIOR WHEN IMPACT PLAYERS
    int damage; //DAMAGE
    int lifetime; ///LIFETIME OF PROJECTILE (WHEN DESPAWNS)
    Player firer; //PERSON WHO FIRED IT (PREVENT SHOOTING YOURSELF)

    Image sprite; //USELESS

    boolean affected_gravity; //DOES THE PROJECTILE DROP DUE TO GRAVITY

    public Projectile(int startx, int starty, float angle, String type, Player fire){
        int speed = 7; 
        x = startx;
        y = starty;
        face_angle = angle;
        firer = fire;
        id = type;

        if(type.equals("bullet_small")){ //ALLOCATE THE PROPER STATS DEPENDING ON WEAPON NAME
            hitbox_size = SMALL;
            speed = 20;
            length = 50;
            lifetime = 2000;
            affected_gravity = false;
            collide_terrain = FIZZLE;
            collide_player = DAMAGE;
            damage = 7;
        }
        if(type.equals("laser")){
            hitbox_size = SMALL;
            speed = 10;
            length = 25;
            lifetime = 30;
            affected_gravity = false;
            collide_terrain = DAMAGE;
            collide_player = FIZZLE;
            damage = 7;
        }
        if(type.equals("grenade")){
            hitbox_size = MEDIUM;
            speed = 10;
            length = 16;
            lifetime = 2000;
            affected_gravity = true;
            collide_terrain = EXPLODE;
            collide_player = EXPLODE;
            damage = 70;
        }
        if(type.equals("explosion")){
            hitbox_size = LARGE;
            speed = 20;
            length = 50;
            lifetime = 15;
            affected_gravity = false;
            collide_terrain = DAMAGE;
            collide_player = DAMAGE;
            damage = 15;
        }

        velocity_x = (float)Math.cos(angle) * (float)speed; //CALCULATE VELOCITY
        velocity_y = (float)Math.sin(angle) * (float)speed;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{ //CLONING
        Projectile temp = (Projectile)super.clone();
        return temp;
    }

    public Rectangle getHitbox(){ //HITBOX
        return new Rectangle(display_x - (hitbox_size/2), display_y - (hitbox_size/2), hitbox_size, hitbox_size);
    }

    public void update(){ //UPDATE THE POSITION AND LIFETIME AND IF GRAVITY VELOCITY GO DOWN
        x = x + velocity_x;
        y = y + velocity_y;
        display_x = Math.round(x);
        display_y = Math.round(y);
        if(lifetime > 0){
            lifetime -= 1;
        }
        if(affected_gravity){
            velocity_y += 0.1;
        }
    }

    public void draw(Graphics g){ //SERVERSIDE USELESS
        g.setColor(Color.CYAN);
        g.drawLine(display_x, display_y, display_x + (int)(Math.round((Math.cos(face_angle))*200.0)), display_y + (int)(Math.round((Math.sin(face_angle))*200.0)));
        Rectangle rec = getHitbox();
        g.drawRect(rec.x, rec.y, rec.width, rec.height);
    }

    public void draw(Graphics g, int px, int py, int camx, int camy){
        Graphics2D g2 = (Graphics2D) g;
        if(id.equals("bullet_small")){ //DRAW THE PROJECTILE BASED ON WHAT TYPE IT IS
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.YELLOW);
        }
        else if(id.equals("laser")){
            g2.setStroke(new BasicStroke(7));
            g2.setColor(Color.RED);
        }
        else if(id.equals("grenade")){
            g2.setColor(Color.CYAN);
            g2.fillOval(display_x - (px-camx) - (length/2), display_y - (py-camy) - (length/2), length, length);
        }
        else if(id.equals("explosion")){
            g2.setColor(Color.ORANGE);
            g2.fillOval(display_x - (px-camx) - (length/2), display_y - (py-camy) - (length/2), length, length);
        }
        g2.drawLine(display_x - (px-camx), display_y - (py-camy), display_x + (int)(Math.round((Math.cos(face_angle))*length)) - (px-camx), display_y + (int)(Math.round((Math.sin(face_angle))*length)) - (py-camy));
        g2.setStroke(new BasicStroke(1));
        Rectangle rec = getHitbox();
        g.drawRect(rec.x-(px-camx), rec.y-(py-camy), rec.width, rec.height);
        
    }

    public Tile checkMapCollision(Map map){ //CHECK IF PROJECTILE HAS HIT MAP, DO DAMAGE TO IT IF IT CAN
        Rectangle rect = getHitbox();
        for(Tile tile : map.build_map){
            if(tile.deleted != true){
                if(tile.getHitbox().intersects(rect)){
                    if(collide_terrain == DAMAGE || collide_terrain == EXPLODE){
                        tile.health -= damage;
                        if(tile.health <= 0){
                            tile.deleted = true;
                        }
                    }
                    return tile;
                }
            }
        }
        return null;
    }

    public Player checkPlayerCollision(Player[] players){ //CHECK IF PROJECTILE HIT PLAYER, DO DAMAGE TO PLAYER IF YES. DON'T DAMAGE IF HIT ORIGINAL SHOOTER
        for(Player check: players){
            if(check != null){
                if(!check.equals(firer)){
                    if(check.getHitbox().intersects(getHitbox())){
                        if(collide_player == DAMAGE || collide_terrain == EXPLODE){
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