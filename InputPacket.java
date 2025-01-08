import java.io.*;

public class InputPacket implements Serializable{
  boolean[] keys;
  long frame;

  public InputPacket(boolean[] inputs, long counter){
    this.keys = inputs;
    this.frame = counter;
  }

  public boolean keyPressed(int key){
    return keys[key];
  }

  @Override
  public String toString(){
    return "working on the fighting side";
  }
}