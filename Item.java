//CLASS THAT REPRESENTS ITEM OBJECTS (GUNS)
import java.awt.*;
import java.io.Serializable;
import javax.swing.ImageIcon;

class Item implements Serializable{
    int cooldown; //every 100 ticks is a second
    int use_times; 
    int reload_cooldown;
    int total_usetimes;

    private static int size = 200; //icons for wepaons in the inventory
    private static transient Image pistol = new ImageIcon("weapons/pistol.png").getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT);
    private static transient Image autogun = new ImageIcon("weapons/autogun.png").getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT);;
    private static transient Image drill = new ImageIcon("weapons/drill.png").getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT);;
    private static transient Image fabricator = new ImageIcon("weapons/fabricator.png").getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT);;

    int cooldown_counter = 0; //cooldowns for the weapons
    int uses_counter = 0;
    int reload_counter = 0;

    String id; //name of the weapon

    Image sprite;

    public Item(String name){ //determine what weapon, and do the stats of specified weapon
        id = name;
        if(name.equals("pistol")){
            cooldown = 100;
            use_times = 7;
            reload_cooldown = 300;
            total_usetimes = -1;
        }
        if(name.equals("autogun")){
            cooldown = 8;
            use_times = 30;
            reload_cooldown = 400;
            total_usetimes = -1;
        }
        if(name.equals("drill")){
            cooldown = 1;
            use_times = 1;
            reload_cooldown = 0;
            total_usetimes = -1;
        }
        if(name.equals("fabricator")){
            cooldown = 150;
            use_times = 1;
            reload_cooldown = 300;
            total_usetimes = -1;
        }
    }

    public void update(){ //update the weapons cooldown/reload
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

    public void draw(Graphics g, int x, int y){ //draw the weapon in the inventory of the player
        
        if(id.equals("pistol")){
            g.drawImage(pistol, x - size/2, y - size/2, null);
        }
        if(id.equals("autogun")){
            g.drawImage(autogun, x - size/2, y - size/2, null);
        }
        if(id.equals("drill")){
            g.drawImage(drill, x - size/2, y - size/2, null);
        }
        if(id.equals("fabricator")){
            g.drawImage(fabricator, x - size/2, y - size/2, null);
        }
        
    }

    @Override
    public String toString(){
        return id;
    }
}