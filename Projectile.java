import java.io.Serializable;

class Projectile implements Serializable, Cloneable{
    int x;
    int y;
    int velocity_x;
    int velocity_y;

    boolean affected_gravity;

    public Projectile(int startx, int starty, float angle, String type){
        
    }
}