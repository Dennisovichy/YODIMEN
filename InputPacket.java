import java.io.*;

public class InputPacket implements Serializable{
  boolean[] keys;

  public InputPacket(boolean[] inputs){
    this.keys = inputs;
  }

  public boolean keyPressed(int key){
    return keys[key];
  }

  @Override
  public String toString(){
    return "working on the fighting side";
  }
}