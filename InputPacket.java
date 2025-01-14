import java.io.*;

public class InputPacket implements Serializable{
  boolean[] keys;
  long frame;

  boolean red_team;
  boolean decision_made;

  public InputPacket(boolean[] inputs, long counter){
    this.keys = inputs;
    this.frame = counter;
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