import java.awt.event.*;
import java.io.*;

public class InputPacket implements Serializable{
  boolean[] keys = new boolean[KeyEvent.KEY_LAST + 1];
  long frame = 0;
  int mousex_offset = 0;
  int mousey_offset = 0;
  boolean mouse_pressed = false;

  boolean red_team = false;
  boolean decision_made = false;

  public InputPacket(boolean[] inputs, long counter, int mousex, int mousey, boolean pressed){
    this.keys = inputs;
    this.frame = counter;
    this.mousex_offset = mousex;
    this.mousey_offset = mousey;
    this.mouse_pressed = pressed;
  }

  public InputPacket(boolean decision_made, boolean team){
    this.decision_made = decision_made;
    this.red_team = team;
  }

  public boolean keyPressed(int key){
    return keys[key];
  }

  @Override
  public String toString(){
    return "working on the fighting side";
  }
}