import java.awt.*;
import java.util.*;

class ClientMenu{

}

class Button{
    int posx;
    int posy;
    int width;
    int height;

    Screen destination = null;

    public Button(int x, int y, int w, int h){
        posx = x;
        posy = y;
        width = w;
        height = h;
    }

    public void leadToScreen(Screen target){
        destination = target;
    }

    public boolean checkMouseOn(float x, float y){
        Rectangle test = new Rectangle(posx, posy, width, height);
        return test.contains(x, y);
    }
}

class Screen{
    boolean game;
    ArrayList<Button> buttons = new ArrayList<>();

    public Screen(boolean game){
        this.game = game;
    }

    public void addButton(Button fart){
        buttons.add(fart);
    }
}

