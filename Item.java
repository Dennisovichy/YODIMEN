import java.awt.*;
import java.io.Serializable;

class Item implements Serializable{
    int cooldown; //every 100 ticks is a second
    int use_times; 
    int reload_cooldown;
    int total_usetimes;

    int cooldown_counter = 0;
    int uses_counter = 0;
    int reload_counter = 0;

    String id;

    Image sprite;

    public Item(String name){
        id = name;
        if(name.equals("pistol")){
            cooldown = 100;
            use_times = 7;
            reload_cooldown = 300;
            total_usetimes = -1;
        }
    }

    public void update(){
        if(uses_counter < use_times){
            if(cooldown_counter < cooldown){
                cooldown_counter++;
            }
            
        }
        else{
            if(reload_counter < reload_cooldown){
                reload_counter++;
            }
            if(reload_counter >= reload_cooldown){
                reload_counter = 0;
                uses_counter = 0;
                cooldown_counter = 0;
            }
        }
    }

    public void draw(Graphics g, int x, int y){
        g.setColor(Color.GREEN);
        g.fillRect(x - 30, y - 30, 60, 60);
    }

    @Override
    public String toString(){
        return id;
    }
}