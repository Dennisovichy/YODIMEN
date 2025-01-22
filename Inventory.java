//CLASS FOR MANAGING INVENTORIES OF PLAYERS AND STORING THE ITEMS
import java.awt.*;
import java.io.Serializable;

class Inventory implements Serializable, Cloneable{
    Item[] hotbar = new Item[10]; //hotbar of items
    boolean acknowledge_swap = false; //let client know if the swap was completed or not

    @Override
    public Object clone() throws CloneNotSupportedException{ //cloning so the information is preserved when sent
        Inventory temp = (Inventory)super.clone();
        return temp;
    }


    public void swapSlots(int[] slots){ //swap slots
        if(slots[0] != -1 && slots[1] != -1){
            Item temp = hotbar[slots[0]];
            hotbar[slots[0]] = hotbar[slots[1]];
            hotbar[slots[1]] = temp;
        }
    }

    public boolean addItem(String name){ //add items to inventory
        for(int i = 0; i < 10; i++){
            if(hotbar[i] == null){
                hotbar[i] = new Item(name);
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g, int midx, int boty){ //draw the inventory
        for(int i = 0; i < hotbar.length; i++){
            g.setColor(Color.DARK_GRAY);
            g.drawRect((midx - (hotbar.length/2 * 60)) + (i * 60), boty-80, 60, 60);
            if(hotbar[i] != null){
                hotbar[i].draw(g, (midx - (hotbar.length/2 * 60)) + (i * 60) + 30, boty-80 +30); //drawss the icons
            }
        }
    }

    public Item selectItem(int midx, int boty, int mousex, int mousey){ //see if mouse is hovering over item, return it if it is
        for(int i = 0; i < hotbar.length; i++){
            Rectangle test = new Rectangle((midx - (hotbar.length/2 * 60)) + (i * 60), boty-80, 60, 60);
            if(test.contains(mousex, mousey)){
                return hotbar[i];
            }
        }
        return null;
    }

    public int getSlot(int midx, int boty, int mousex, int mousey){ //for swapping items, see what slot mouse ends on
        for(int i = 0; i < hotbar.length; i++){
            Rectangle test = new Rectangle((midx - (hotbar.length/2 * 60)) + (i * 60), boty-80, 60, 60);
            if(test.contains(mousex, mousey)){
                return i;
            }
        }
        return -1;
    }

    public void updateSlots(){ //UPDATE
        for(Item item: hotbar){
            if (item != null){
                item.update();
            }
        }
    }
}
