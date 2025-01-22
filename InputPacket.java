//CLASS THAT IS SENT OVER THE NETWORK SERIALIZED TO CONVEY INPUTS FROM CLIENT TO SERVER
import java.awt.event.*;
import java.io.*;

public class InputPacket implements Serializable{
  boolean[] keys = new boolean[KeyEvent.KEY_LAST + 1]; //self explanatory
  long frame = 0;
  int mousex_offset = 0;
  int mousey_offset = 0;
  boolean mouse_pressed = false;
  int[] inventory_swap_request;

  boolean red_team = false; //team select information
  boolean decision_made = false;

  public InputPacket(boolean[] inputs, long counter, int mousex, int mousey, boolean pressed, int[] inventory_swap){
    this.keys = inputs;
    this.frame = counter;
    this.mousex_offset = mousex;
    this.mousey_offset = mousey;
    this.mouse_pressed = pressed;
    this.inventory_swap_request = inventory_swap;
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