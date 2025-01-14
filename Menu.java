import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Menu{
    Screen current_screen;
    Screen title_screen = new Screen(false);
    Screen game_trigger = new Screen(true);

    JTextField field = new JTextField(5);

    public Menu(){
        Button play = new Button(20, 20, 100, 100, "pound");
        play.leadToScreen(game_trigger);
        title_screen.addButton(play);
        current_screen = title_screen;
    }

    public void checkScreen(int mx, int my){
        Screen temp = current_screen.pressButton(mx, my);
        if(temp != null){
            current_screen = temp;
        }
    }

    public void drawScreen(Graphics g){
        current_screen.drawScreen(g);
    }
}

class Button{
    int posx;
    int posy;
    int width;
    int height;

    String text;

    Screen destination = null;

    public Button(int x, int y, int w, int h, String tex){
        posx = x;
        posy = y;
        width = w;
        height = h;
        text = tex;
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

    public Screen pressButton(int x, int y){
        //System.out.println("EW");
        for(Button butter: buttons){
            if(butter.checkMouseOn(x, y)){
                System.out.println("On");
                return butter.destination;
            }
        }
        return null;
    }

    public void drawScreen(Graphics g){
        g.setColor(Color.BLACK);
        for(Button butter: buttons){
            g.drawRect(butter.posx, butter.posy, butter.width, butter.height);
        }
    }
}

