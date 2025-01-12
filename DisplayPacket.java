import java.io.*;

public class DisplayPacket implements Serializable{
  int player_x;
  int player_y;
  Map game_map;


  public DisplayPacket(int playerx, int playery, Map gamemap){
    this.player_x = playerx;
    this.player_y = playery;
    this.game_map = gamemap;
  }


  @Override
  public String toString(){
    return "working on the fighting side";
  }
}