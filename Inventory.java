import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;

class Inventory implements Serializable, Cloneable{
    Item[] hotbar = new Item[10];
    boolean acknowledge_swap = false;

    @Override
    public Object clone() throws CloneNotSupportedException{
        Inventory temp = (Inventory)super.clone();
        return temp;
    }


    public void swapSlots(int[] slots){
        if(slots[0] != -1 && slots[1] != -1){
            System.out.println("Bingus");
            Item temp = hotbar[slots[0]];
            hotbar[slots[0]] = hotbar[slots[1]];
            hotbar[slots[1]] = temp;
            System.out.println(Arrays.toString(hotbar));
        }
    }

    public boolean addItem(String name){
        for(int i = 0; i < 10; i++){
            if(hotbar[i] == null){
                hotbar[i] = new Item(name);
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g, int midx, int boty){
        for(int i = 0; i < hotbar.length; i++){
            g.setColor(Color.DARK_GRAY);
            g.drawRect((midx - (hotbar.length/2 * 60)) + (i * 60), boty-80, 60, 60);
            if(hotbar[i] != null){
                g.setColor(Color.GREEN);
                g.fillRect((midx - (hotbar.length/2 * 60)) + (i * 60), boty-80, 60, 60);
            }
        }
    }

    public Item selectItem(int midx, int boty, int mousex, int mousey){
        for(int i = 0; i < hotbar.length; i++){
            Rectangle test = new Rectangle((midx - (hotbar.length/2 * 60)) + (i * 60), boty-80, 60, 60);
            if(test.contains(mousex, mousey)){
                return hotbar[i];
            }
        }
        return null;
    }

    public int getSlot(int midx, int boty, int mousex, int mousey){
        for(int i = 0; i < hotbar.length; i++){
            Rectangle test = new Rectangle((midx - (hotbar.length/2 * 60)) + (i * 60), boty-80, 60, 60);
            if(test.contains(mousex, mousey)){
                return i;
            }
        }
        return -1;
    }
}
